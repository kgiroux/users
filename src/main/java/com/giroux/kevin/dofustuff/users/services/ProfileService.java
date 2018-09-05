package com.giroux.kevin.dofustuff.users.services;

import com.giroux.kevin.dofustuff.commons.security.Profile;

import java.util.List;

public interface ProfileService {
    /**
     * Service de création des profiles
     *
     * @param nouveauPrf à créer
     */
    void creerOuModierProfile(Profile nouveauPrf);

    /**
     * Service permettant de récupérer l'ensemble des profiles
     *
     * @return
     */
    List<Profile> recupererTousProfile();


    Profile getRetrieve(String id);

    /**
     * Service de suppression d'un profil
     *
     * @param id
     */
    void supprimerProfile(String id);
}
