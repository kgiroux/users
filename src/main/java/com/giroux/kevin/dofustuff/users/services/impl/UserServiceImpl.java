package com.giroux.kevin.dofustuff.users.services.impl;

import com.giroux.kevin.dofustuff.commons.security.Profile;
import com.giroux.kevin.dofustuff.commons.security.TypeUser;
import com.giroux.kevin.dofustuff.commons.security.User;
import com.giroux.kevin.dofustuff.error.ErrorProfile;
import com.giroux.kevin.dofustuff.users.persistence.UserPersistence;
import com.giroux.kevin.dofustuff.users.services.ProfileService;
import com.giroux.kevin.dofustuff.users.services.UserService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Implementation du service.
 */
@Service
public class UserServiceImpl implements UserService {

  /**
   * Instance du logger.
   */
  private static final Logger          LOG                          = LoggerFactory.getLogger(UserPersistence.class);
  /**
   * String ERROR for the mail template.
   */
  private static final String          COULD_NOT_READ_MAIL_TEMPLATE = "Could not read mail template";
  /**
   * Instance de User Persistence.
   */
  private final        UserPersistence userPersistence;

  /**
   * Mail expediteur.
   */
  @Value("${mail.from}") private    String         fromAddress;
  /**
   * Mail URL.
   */
  @Value("${mail.url}") private     String         mailUrl;
  /**
   * Activation des mails.
   */
  @Value("${mail.enabled}") private Boolean        mailEnabled;
  /**
   * Resources Loader.
   */
  private final                     ResourceLoader resourceLoader;
  /**
   * JavaMailSender.
   */
  private final                     JavaMailSender sender;
  /**
   * Service Profile.
   */
  private final                     ProfileService profileService;

  /**
   * DefaultPassword.
   */
  private static final String DEFAULT_PSWD = "dofustuff";

  /**
   * Constructor.
   *
   * @param pUserPersistence persistence
   * @param pResourceLoader  resource loader for the loading template
   * @param pJavaMailSender  JavaSender
   * @param pProfileService  pProfileService
   */
  @Autowired
  public UserServiceImpl(final UserPersistence pUserPersistence, final ResourceLoader pResourceLoader,
          final JavaMailSender pJavaMailSender, final ProfileService pProfileService) {
    userPersistence = pUserPersistence;
    resourceLoader = pResourceLoader;
    sender = pJavaMailSender;
    profileService = pProfileService;
  }

  /**
   * Create a user into the database.
   *
   * @param pUser user that need to be create
   */
  @Override
  public void createUser(final User pUser) {
    List<User> userToCheck = userPersistence
            .retrieveUserByLoginOrByEmailOrIdUserNot(pUser.getLogin(), pUser.getEmail(), pUser.getId());

    Profile profile = profileService.getRetrieve("default_profile");
    List<Profile> profiles = pUser.getProfiles();
    if (profiles == null) {
      profiles = new ArrayList<>();
    }
    if (profile != null && !profiles.contains(profile)) {
      profiles.add(profile);
    }

    if (CollectionUtils.isEmpty(userToCheck)) {
      // Set the default profile for a new user
      pUser.setProfiles(profiles);
    } else {
      LOG.error("Cannot create the user. This user {} already exist int the database.", pUser.getLogin());
      throw new IllegalArgumentException(ErrorProfile.ERR_PROFILE_11.toString());
    }
    String pwd;
    if (!TypeUser.ANDROID_USER.equals(pUser.getTypeUser())) {
      pwd = securePassword(generateRandomPassword(8));
    } else {
      pwd = securePassword(pUser.getPassword());
    }
    try {
      // Perform a double encryption on the password
      pUser.setPassword(pwd);
      userPersistence.createUser(pUser);
      if (mailEnabled) {
        // send email with generated password
        if (!TypeUser.ANDROID_USER.equals(pUser.getTypeUser())) {
          sendNewAccountEmail(pUser.getEmail(), pwd, pUser.getLogin());
        } else {
          sendNewAccountAndroidEmail(pUser.getEmail(), pUser.getLogin());
        }

      }
    } catch (MessagingException | IOException e) {
      LOG.error("Could not create the user", e);
      throw new IllegalArgumentException(ErrorProfile.ERR_PROFILE_04.toString());
    }

  }

  /**
   * Retrieve a user by a provide Login.
   *
   * @param pUserName nom de l'utilisateur
   * @return the user.
   */
  @Override
  public User retrieveUserByLogin(String pUserName) {
    return userPersistence.retrieveUserByLogin(pUserName);
  }

  /**
   * Delete a User.
   *
   * @param pId de l'utilisateur.
   */
  @Override
  public void deleteUser(String pId) {
    userPersistence.deleteUser(pId);
  }

  /**
   * SecurePassword before insert to database.
   *
   * @param pPasswordToSecure mot de passe a sécurisé.
   * @return le mot de passe sécurisé.
   */
  @Override
  public String securePassword(String pPasswordToSecure) {
    MessageDigest messageDigest = DigestUtils.getSha512Digest();
    byte[] arrayToEncrypte = messageDigest.digest(pPasswordToSecure.getBytes());
    StringBuilder sb = new StringBuilder();
    for (byte anArrayToEncrypte : arrayToEncrypte) {
      sb.append(Integer.toString((anArrayToEncrypte & 0xff) + 0x100, 16).substring(1));
    }
    return sb.toString();
  }

  @Override
  public void updateUser(final User pUser, String pUpdatePassword) {
    if (pUser != null && pUpdatePassword != null && !pUpdatePassword.isEmpty()) {
      List<User> userToCheck = userPersistence
              .retrieveUserByLoginOrByEmailOrIdUserNot(pUser.getLogin(), pUser.getLogin(), pUser.getId());
      if (!CollectionUtils.isEmpty(userToCheck)) {
        if (userToCheck.size() == 1) {
          User userFromBd = userToCheck.get(0);
          pUser.setPassword(securePassword(pUser.getPassword()));
          if (pUser.getPassword().equals(userFromBd.getPassword())) {
            userFromBd.setPassword(securePassword(pUpdatePassword));
            userPersistence.createUser(userFromBd);
          } else {
            throw new IllegalArgumentException(ErrorProfile.ERR_PROFILE_05.toString());
          }
        }
      } else {
        throw new IllegalArgumentException(ErrorProfile.ERR_PROFILE_03.toString());
      }
    } else {
      throw new IllegalArgumentException(ErrorProfile.ERR_PROFILE_04.toString());
    }

  }

  @Override
  public void resetPassword(User pUser) {
    if (pUser != null) {
      List<User> userToCheck = userPersistence
              .retrieveUserByLoginOrByEmailOrIdUserNot(pUser.getLogin(), pUser.getEmail(), pUser.getId());
      if (!CollectionUtils.isEmpty(userToCheck)) {
        if (userToCheck.size() == 1) {
          User userFromBd = userToCheck.get(0);
          String pwd = generateRandomPassword(8);
          try {
            userFromBd.setPassword(securePassword(securePassword(pwd)));
            userPersistence.createUser(userFromBd);
            if (mailEnabled) {
              sendResetEmail(userFromBd.getEmail(), pwd);
            }
          } catch (MessagingException | IOException e) {
            userFromBd.setPassword(securePassword(securePassword(DEFAULT_PSWD)));
            userPersistence.createUser(userFromBd);
          }
        }
      } else {
        throw new IllegalArgumentException(ErrorProfile.ERR_PROFILE_03.toString());
      }
    }
  }

  private String generateRandomPassword(final int pLength) {
    Random random = new Random();
    String chars = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789!@#$";
    StringBuilder token = new StringBuilder(pLength);
    for (int i = 0; i < pLength; i++) {
      token.append(chars.charAt(random.nextInt(chars.length())));
    }
    return token.toString();
  }

  private String fileToString(String path) throws IOException {
    InputStream is = resourceLoader.getResource("classpath:" + path).getInputStream();
    BufferedReader bf = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    return bf.lines().collect(Collectors.joining());
  }

  /**
   * Send Email to web user.
   *
   * @param pEmail email where we need to send the message
   * @param pPwd   the generated password
   * @param pLogin the login
   * @throws IOException        if there is a problem of the generation of the message
   * @throws MessagingException if there is a problem with the sending of the mail.
   */
  private void sendNewAccountEmail(final String pEmail, final String pPwd, final String pLogin)
          throws IOException, MessagingException {
    try {
      String mailBody = fileToString("mail/newAccount.html").replaceAll("%pwd%", pPwd).replaceAll("%login%", pLogin);
      sendEmail(pEmail, "Account's Creation", mailBody);
    } catch (IOException e) {
      LOG.error(COULD_NOT_READ_MAIL_TEMPLATE, e);
      throw e;
    }
  }

  /**
   * Send a mail for android Users.
   *
   * @param pEmail email where the mail need to be send
   * @param pLogin login of the user
   * @throws IOException        if there is a problem with the mail
   * @throws MessagingException if there is a problem with send mail.
   */
  private void sendNewAccountAndroidEmail(final String pEmail, final String pLogin)
          throws IOException, MessagingException {
    try {
      String mailBody = fileToString("mail/newAccountAndroidWithoutPassword.html").replaceAll("%login%", pLogin);
      sendEmail(pEmail, "Création de compte", mailBody);
    } catch (IOException e) {
      LOG.error(COULD_NOT_READ_MAIL_TEMPLATE, e);
      throw e;
    }
  }

  /**
   * Send a pEmail for resetting the password of the user.
   *
   * @param pEmail pEmail of the target user.
   * @param pPwd   newer password.
   * @throws IOException        if there is a problem with email template
   * @throws MessagingException if there is a problem with the process of sending email
   */
  private void sendResetEmail(final String pEmail, final String pPwd) throws IOException, MessagingException {
    try {
      String mailBody = fileToString("mail/resetPassword.html").replaceAll("%pwd%", pPwd);
      sendEmail(pEmail, "Réinitialisation de mot de passe", mailBody);
    } catch (IOException e) {
      LOG.error(COULD_NOT_READ_MAIL_TEMPLATE, e);
      throw e;
    }
  }

  /**
   * Method that send a pEmail to user.
   *
   * @param pEmail    where we need to send information
   * @param pSubject  Subject of the pEmail
   * @param pMailBody Content of the pEmail
   * @throws MessagingException If it failed to send an pEmail.
   */
  private void sendEmail(final String pEmail, final String pSubject, final String pMailBody) throws MessagingException {
    MimeMessage message = sender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setTo(pEmail);
    helper.setFrom(fromAddress);
    helper.setText(pMailBody, true);
    helper.setSubject(pSubject);
    sender.send(message);
  }

}
