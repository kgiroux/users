package com.giroux.kevin.dofustuff.users.persistence.repository;

import com.giroux.kevin.dofustuff.users.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    /**
     * Retrieve a use by is Login
     *
     * @param login
     * @return
     */
    UserEntity findByLoginOrEmail(String login, String email);


    /**
     * Retrieve a list of user by login or email and exclude a provide ID
     *
     * @param login
     * @param email
     * @param idUser
     * @return
     */
    List<UserEntity> findByLoginOrEmailAndIdUserNot(String login, String email, String idUser);

    /**
     * Retrieve a list of user by type
     *
     * @param type
     * @return
     */
    List<UserEntity> findByTypeUser(String type);

}

