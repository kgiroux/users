package com.giroux.kevin.dofustuff.users.network.exception;

/**
 * Exception that is raised for an not allowed user.
 *
 * @author Kevin Giroux
 */
public class NotAllowedException extends Exception {
  /**
   * Constructor.
   *
   * @param pMessage the message.
   */
  public NotAllowedException(String pMessage) {
    super(pMessage);
  }

  /**
   * Serial Id.
   */
  private static final long serialVersionUID = 5703904076212195614L;

}
