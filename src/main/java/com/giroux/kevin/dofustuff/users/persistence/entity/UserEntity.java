package com.giroux.kevin.dofustuff.users.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity pour les users.
 *
 * @author kgiroux
 */
@Entity
@Table(name = "users")
public class UserEntity {
  /**
   * Id for login.
   */
  @Id
  @Column(name = "idUser",
          unique = true,
          nullable = false,
          length = 100)
  private                        String idUser;
  /**
   * User login.
   */
  @Column(name = "login",
          unique = true,
          nullable = false,
          length = 100) private  String login;
  /**
   * User password hash.
   */
  @Column(name = "password",
          nullable = false,
          length = 512) private  String password;
  /**
   * User email.
   */
  @Column(name = "email",
          unique = true,
          nullable = false,
          length = 512) private  String email;
  /**
   * User type.
   */
  @Column(name = "type") private String typeUser;

  /**
   * Retrieve id of the user.
   *
   * @return idUser
   */
  public String getIdUser() {
    return idUser;
  }

  /**
   * Define the id of the user.
   *
   * @param pIdUser id of the user that will be set
   */
  public void setIdUser(final String pIdUser) {
    idUser = pIdUser;
  }

  /**
   * retrieve the login of the user.
   *
   * @return the login
   */
  public String getLogin() {
    return login;
  }

  /**
   * Define the login.
   *
   * @param pLogin login og the user that will be set
   */
  public void setLogin(final String pLogin) {
    login = pLogin;
  }

  /**
   * Retrieve the password of the user.
   *
   * @return the user password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Defined the pPassword of the user.
   *
   * @param pPassword pPassword that will be set.
   */
  public void setPassword(final String pPassword) {
    password = pPassword;
  }

  /**
   * Retrieve the email of the user.
   *
   * @return the email of the user
   */
  public String getEmail() {
    return email;
  }

  /**
   * Define the mail of the user.
   *
   * @param pEmail that will be set for the user.
   */
  public void setEmail(final String pEmail) {
    email = pEmail;
  }

  /**
   * Retrive the type of user.
   *
   * @return the typeUser of the user.
   */
  public String getTypeUser() {
    return typeUser;
  }

  /**
   * Defined the type of User.
   *
   * @param pTypeUser the pTypeUser to set
   */
  public void setTypeUser(final String pTypeUser) {
    typeUser = pTypeUser;
  }

}

