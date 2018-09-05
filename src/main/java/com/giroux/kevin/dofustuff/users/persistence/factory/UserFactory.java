package com.giroux.kevin.dofustuff.users.persistence.factory;

import com.giroux.kevin.dofustuff.commons.security.TypeUser;
import com.giroux.kevin.dofustuff.commons.security.User;
import com.giroux.kevin.dofustuff.commons.utils.Factory;
import com.giroux.kevin.dofustuff.users.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * UserFactory : Factory that convert Model (front) to Entity (database), and Entity (database) to Model (front).
 *
 * @author Kévin Giroux.
 */
@Component
public class UserFactory implements Factory<UserEntity, User> {

  /**
   * Factory for user.
   *
   * @param pUserEntity entity
   * @return User
   */
  @Override
  public User entityToDto(final UserEntity pUserEntity) {
    return entityToDto(pUserEntity, true);
  }

  /**
   * Transform Entity to DTO.
   *
   * @param pUserEntity     user entity that will be converted.
   * @param pReturnPassword if we return the password on the model or not
   * @return User from database.
   */
  public User entityToDto(final UserEntity pUserEntity, boolean pReturnPassword) {
    User u = new User();
    u.setId(pUserEntity.getIdUser());
    u.setEmail(pUserEntity.getEmail());
    if (pUserEntity.getTypeUser() != null) {
      u.setTypeUser(TypeUser.valueOf(pUserEntity.getTypeUser()));
    } else {
      u.setTypeUser(TypeUser.UNKNOWN);
    }

    if (pReturnPassword) {
      u.setPassword(pUserEntity.getPassword());
    }
    u.setLogin(pUserEntity.getLogin());
    return u;
  }

  /**
   * Transform a DTO to Entity.
   *
   * @param pUser that need to be converted
   * @return UserEntity
   */
  @Override
  public UserEntity dtoToEntity(final User pUser) {
    UserEntity u = new UserEntity();
    u.setEmail(pUser.getEmail());
    u.setPassword(pUser.getPassword());
    u.setLogin(pUser.getLogin());
    u.setIdUser(pUser.getId());
    if (pUser.getTypeUser() != null) {
      u.setTypeUser(pUser.getTypeUser().toString());
    } else {
      u.setTypeUser(TypeUser.UNKNOWN.toString());
    }
    return u;
  }

}
