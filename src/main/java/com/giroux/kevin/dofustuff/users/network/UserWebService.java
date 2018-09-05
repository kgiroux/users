package com.giroux.kevin.dofustuff.users.network;

import com.giroux.kevin.dofustuff.commons.security.User;
import com.giroux.kevin.dofustuff.commons.security.UserWrapper;
import com.giroux.kevin.dofustuff.error.ErrorProfile;
import com.giroux.kevin.dofustuff.security.JwtTokenGenerator;
import com.giroux.kevin.dofustuff.security.exceptions.BadRequestException;
import com.giroux.kevin.dofustuff.security.exceptions.UnauthorizedException;
import com.giroux.kevin.dofustuff.users.services.UserService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Users.
 *
 * @author Kévin Girou
 */
@RestController
@RequestMapping("/users")
public class UserWebService {

  /**
   * Type d'authentification.
   */
  private static final String AUTHENTICATION_TYPE = "Basic ";

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(UserWebService.class);

  /**
   * Instance du service.
   */
  private final UserService userService;

  /**
   * Token generator JWT.
   */
  private final JwtTokenGenerator tokenGenerator;

  /**
   * Constructor.
   *
   * @param pUserService    userService
   * @param pTokenGenerator tokenGenerator
   */
  @Autowired
  public UserWebService(UserService pUserService, JwtTokenGenerator pTokenGenerator) {
    userService = pUserService;
    tokenGenerator = pTokenGenerator;
  }

  /**
   * Create a User.
   *
   * @param pUser pUser that need to be created
   */
  @PutMapping(name = "Create/Update User",
              consumes = { MediaType.APPLICATION_JSON_VALUE },
              produces = { MediaType.APPLICATION_JSON_VALUE })
  public void createUser(
          @RequestBody
          final User pUser) {
    try {
      userService.createUser(pUser);
    } catch (IllegalArgumentException ex) {
      LOG.error("Error", ex);
      throw new BadRequestException(ex.getMessage());
    }
  }

  /**
   * Delete a User.
   *
   * @param pId of the user.
   */
  @DeleteMapping(name = "Delete User",
                 value = "/{pId}")
  public void deleteUser(
          @PathVariable("pId")
                  String pId) {
    userService.deleteUser(pId);
  }

  /**
   * Create or update pUser password.
   *
   * @param pUser User.
   */
  @PutMapping(name = "Create/Update User password",
              consumes = { MediaType.APPLICATION_JSON_VALUE },
              value = "/password")
  public void updatePassword(
          @RequestBody
          final UserWrapper pUser) {
    try {
      if (pUser.getUser() != null && pUser.getUpdatePassword() != null && !pUser.getUpdatePassword().equals("")) {
        userService.updateUser(pUser.getUser(), pUser.getUpdatePassword());
      } else {
        throw new BadRequestException(ErrorProfile.ERR_PROFILE_04.toString());
      }
    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }

  /**
   * Method that allow the resetting of the password.
   *
   * @param pId   pId of the pUser
   * @param pUser user where it is need to reset the password.
   */
  @PutMapping(name = "Create/Update User password (reset)",
              consumes = { MediaType.APPLICATION_JSON_VALUE },
              value = "/reset/{pId}")
  public void resetPassword(
          @PathVariable("pId")
                  String pId,
          @RequestBody
                  User pUser) {
    try {
      if (pId != null && !pId.isEmpty()) {
        userService.resetPassword(pUser);
      }
    } catch (IllegalArgumentException ex) {
      throw new BadRequestException(ex.getMessage());
    }
  }

  /**
   * Login.
   *
   * @param pCredentials credential encrypted
   * @return idToken
   */
  @PutMapping(name = "loginUser",
              produces = { MediaType.APPLICATION_JSON_VALUE },
              value = "/authenticate")
  public String loginUser(
          @RequestHeader(value = "Authorization",
                         required = false)
                  String pCredentials) {
    long startSecond = Instant.now().getEpochSecond();
    if (StringUtils.isEmpty(pCredentials) || !pCredentials.startsWith(AUTHENTICATION_TYPE)) {
      throw new UnauthorizedException(ErrorProfile.ERR_PROFILE_06.toString());
    }
    UsernamePasswordCredentials cr = getCredentials(pCredentials);
    if (cr == null) {
      throw new UnauthorizedException(ErrorProfile.ERR_PROFILE_07.toString());
    }
    // Retrieve the user that try to connect.
    User user = userService.retrieveUserByLogin(cr.getUserName());
    LOG.debug("Retrieve user in: {} seconds.", (Instant.now().getEpochSecond() - startSecond));

    startSecond = Instant.now().getEpochSecond();
    if (user == null) {
      throw new UnauthorizedException(ErrorProfile.ERR_PROFILE_08.toString());
    }

    // Check if some elements of authentication (pwd) are correct.
    if (!isAuthorized(user, cr.getPassword())) {
      throw new UnauthorizedException(ErrorProfile.ERR_PROFILE_09.toString());
    }
    LOG.debug("Control password in: {} seconds.", (Instant.now().getEpochSecond() - startSecond));

    // Create the JWT token
    startSecond = Instant.now().getEpochSecond();
    String token = tokenGenerator.generateToken(user);
    LOG.debug("Generate token in: {} seconds. ", (Instant.now().getEpochSecond() - startSecond));
    if (StringUtils.isEmpty(token)) {
      throw new UnauthorizedException(ErrorProfile.ERR_PROFILE_10.toString());
    }

    return token;

  }

  /**
   * Method that can extract credential from the header.
   *
   * @param pCredentials credential
   * @return Object that contain credential
   */
  private UsernamePasswordCredentials getCredentials(final String pCredentials) {
    String out = new String(Base64.decodeBase64(pCredentials.substring(AUTHENTICATION_TYPE.length())),
            StandardCharsets.UTF_8);
    String[] tabCredential = out.split(":", 2);
    UsernamePasswordCredentials usernamePasswordCredentials = null;
    if (tabCredential.length == 2) {
      usernamePasswordCredentials = new UsernamePasswordCredentials(tabCredential[0], tabCredential[1]);
    }
    return usernamePasswordCredentials;
  }

  /**
   * Method that can check credentials of the pUser.
   *
   * @param pUser          pUser
   * @param pGivenPassword password that need to be check
   * @return true if the password is correct, else false.
   */
  private boolean isAuthorized(final User pUser, final String pGivenPassword) {
    if (pUser == null || StringUtils.isEmpty(pUser.getPassword())) {
      return false;
    }
    String cryptedGivenPwd = userService.securePassword(pGivenPassword);
    return cryptedGivenPwd.equals(pUser.getPassword());
  }
}
