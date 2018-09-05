package com.giroux.kevin.dofustuff.users.persistence;

import com.giroux.kevin.dofustuff.commons.security.User;

import java.util.List;

public interface UserPersistence {

    /**
     * retrieveUser by login
     *
     * @param pLogin login that need to be found
     * @return User
     */
    User retrieveUserByLogin(String pLogin);

    /**
     * Récupération d'un utilisateur par son login / email
     *
     * @param login  login
     * @param email  email
     * @param idUser idUser
     * @return list des utlisateurs
     */
    List<User> retrieveUserByLoginOrByEmailOrIdUserNot(String login, String email, String idUser);

    /**
     * Insert User to database
     *
     * @param user user to Insert
     */
    void createUser(User user);

    /**
     * User Id that need to be deleted
     *
     * @param idUserToDelete id of user
     */
    void deleteUser(String idUserToDelete);

}
