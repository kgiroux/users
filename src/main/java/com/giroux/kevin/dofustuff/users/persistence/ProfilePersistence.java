package com.giroux.kevin.dofustuff.users.persistence;

import com.giroux.kevin.dofustuff.commons.security.Profile;

import java.util.List;

/**
 * Interface listant les méthodes nécessaire pour la persistence des profiles
 *
 * @author sngo
 */
public interface ProfilePersistence {

    /**
     * Méthode de création d'un prf
     *
     * @param prf prf à créer
     */
    void creerOuModifierProfile(Profile prf);

    /**
     * Méthode permettant de récupérer un prf à partir de son id
     *
     * @param id id de la rgt à récuperer
     */
    Profile recupererProfile(String id);

    /**
     * Méthode permettant de récupérer l'ensemble des profils
     *
     * @return les profils, liste vide si aucun
     */
    List<Profile> recupererProfiles();

    /**
     * Méthode de suppression d'un profil
     *
     * @param id id du profil à supprimer
     */
    void supprimerProfile(String id);

}
