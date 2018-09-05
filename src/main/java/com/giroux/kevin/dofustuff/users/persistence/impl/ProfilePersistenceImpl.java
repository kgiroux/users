package com.giroux.kevin.dofustuff.users.persistence.impl;

import com.giroux.kevin.dofustuff.commons.security.Profile;
import com.giroux.kevin.dofustuff.users.persistence.ProfilePersistence;
import com.giroux.kevin.dofustuff.users.persistence.entity.ProfileEntity;
import com.giroux.kevin.dofustuff.users.persistence.factory.ProfileFactory;
import com.giroux.kevin.dofustuff.users.persistence.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de la persistence de right
 *
 * @author sngo
 */
@Repository("ProfilePersistenceImpl")
public class ProfilePersistenceImpl implements ProfilePersistence {

    /**
     * Interface de communication avec PostgreSQL
     */
    @Autowired
    private ProfileRepository prfRepository;

    /**
     * Factory de Profile
     */
    @Autowired
    private ProfileFactory prfFactory;

    @Override
    public void creerOuModifierProfile(Profile prf) {
        prfRepository.save(prfFactory.dtoToEntity(prf));
    }

    @Override
    public void supprimerProfile(String id) {
        if (prfRepository.findById(id).isPresent()) {
            prfRepository.delete(prfRepository.findById(id).get());
        }
    }

    @Override
    public List<Profile> recupererProfiles() {
        List<Profile> prfRecuperes = new ArrayList<>();
        Iterable<ProfileEntity> prfs = prfRepository.findByVisible(true);
        for (ProfileEntity prfEntity : prfs) {
            prfRecuperes.add(prfFactory.entityToDto(prfEntity));
        }
        return prfRecuperes;
    }

    @Override
    public Profile recupererProfile(String id) {
        return prfFactory.entityToDto(prfRepository.findById(id).orElse(null));
    }

}
