package com.giroux.kevin.dofustuff.users.persistence.impl;

import com.giroux.kevin.dofustuff.commons.security.User;
import com.giroux.kevin.dofustuff.users.persistence.UserPersistence;
import com.giroux.kevin.dofustuff.users.persistence.entity.UserEntity;
import com.giroux.kevin.dofustuff.users.persistence.factory.UserFactory;
import com.giroux.kevin.dofustuff.users.persistence.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("userPersistenceImpl")
public class UserPersistenceImpl implements UserPersistence {
  /**
   * Repository for user.
   */
  private final UserRepository userRepository;
  /**
   * Factory for user.
   */
  private final UserFactory    userFactory;

  /**
   * Constructor.
   *
   * @param pUserRepository repository for user
   * @param pUserFactory    factory for user
   */
  @Autowired
  public UserPersistenceImpl(final UserRepository pUserRepository, final UserFactory pUserFactory) {
    userFactory = pUserFactory;
    userRepository = pUserRepository;
  }

  /**
   * retrieveUser by login.
   *
   * @param pLogin login that need to be found
   * @return User
   */
  @Override
  public User retrieveUserByLogin(String pLogin) {
    UserEntity entity = userRepository.findByLoginOrEmail(pLogin, pLogin);
    if (entity != null) {
      return userFactory.entityToDto(entity, true);
    }
    return null;

  }

  /**
   * Retrieve the user with is pLogin / pEmail.
   *
   * @param pLogin  pLogin
   * @param pEmail  pEmail
   * @param pIdUser pIdUser
   * @return list of users
   */
  @Override
  public List<User> retrieveUserByLoginOrByEmailOrIdUserNot(final String pLogin, final String pEmail,
          final String pIdUser) {
    return userRepository.findByLoginOrEmailAndIdUserNot(pLogin, pEmail, pIdUser).stream()
            .map(entity -> userFactory.entityToDto(entity, true)).collect(Collectors.toList());
  }

  /**
   * Insert User to database.
   *
   * @param pUser pUser to Insert
   */
  @Override
  public void createUser(final User pUser) {
    userRepository.save(userFactory.dtoToEntity(pUser));
  }

  /**
   * User Id that need to be deleted.
   *
   * @param pIdUserToDelete id of user
   */
  @Override
  public void deleteUser(final String pIdUserToDelete) {
    if (retrieveUserById(pIdUserToDelete) != null) {
      userRepository.delete(retrieveUserById(pIdUserToDelete));
    }

  }

  /**
   * Get a user from Id.
   *
   * @param pId of the user that need to be found
   * @return the user found in the database
   */
  private UserEntity retrieveUserById(String pId) {
    return userRepository.findById(pId).orElse(null);
  }

}
