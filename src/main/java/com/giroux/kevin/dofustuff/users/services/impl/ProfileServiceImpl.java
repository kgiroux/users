package com.giroux.kevin.dofustuff.users.services.impl;

import com.giroux.kevin.dofustuff.commons.security.Profile;
import com.giroux.kevin.dofustuff.users.persistence.ProfilePersistence;
import com.giroux.kevin.dofustuff.users.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Ensemble de services permettant de gérer les profiles
 *
 * @author sngo
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    /**
     * Service de stockage des profils
     */
    @Autowired
    @Qualifier("ProfilePersistenceImpl")
    private ProfilePersistence prfPersistence;


    /**
     * Service de création des profiles
     *
     * @param nouveauPrf à créer
     */
    @Override
    public void creerOuModierProfile(Profile nouveauPrf) {
        prfPersistence.creerOuModifierProfile(nouveauPrf);
    }

    /**
     * Service permettant de récupérer l'ensemble des profiles
     *
     * @return
     */
    @Override
    public List<Profile> recupererTousProfile() {
        List<Profile> listProfile = prfPersistence.recupererProfiles();
        if (!CollectionUtils.isEmpty(listProfile)) {
            Collections.sort(listProfile);
            return listProfile;
        }
        return new ArrayList<>();
    }
    

    @Override
    public Profile getRetrieve(String id) {
        return prfPersistence.recupererProfile(id);
    }

    /**
     * Service de suppression d'un profil
     *
     * @param id
     */
    @Override
    public void supprimerProfile(String id) {
        Profile prf = prfPersistence.recupererProfile(id);
        if (prf != null) {
            prfPersistence.supprimerProfile(id);
        }
    }

}
