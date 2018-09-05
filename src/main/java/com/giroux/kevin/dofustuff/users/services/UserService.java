package com.giroux.kevin.dofustuff.users.services;

import com.giroux.kevin.dofustuff.commons.security.User;

/**
 * Interface de UserService.
 *
 * @author Kévin Giroux
 */
public interface UserService {

    /**
     * Retrieve a User by a login.
     *
     * @param pUserName username
     * @return User
     */
    User retrieveUserByLogin(String pUserName);

    /**
     * Secure a password.
     *
     * @param pPasswordToSecure password
     * @return password that was secure
     */
    String securePassword(String pPasswordToSecure);


    /**
     * Create a user into the database.
     *
     * @param pUser User that need to be created
     */
    void createUser(User pUser);


    /**
     * Mise a jour de password.
     *
     * @param pUser           utilsateur
     * @param pUpdatePassword password
     */
    void updateUser(User pUser, String pUpdatePassword);

    /**
     * Suppression d'un utilisateur
     *
     * @param pId id de l'utilisateur
     */
    void deleteUser(String pId);

    /**
     * Reset du password.
     *
     * @param pUser pUser for the password reset
     */
    void resetPassword(User pUser);
}
