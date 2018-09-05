package com.giroux.kevin.dofustuff.users.persistence.factory;

import com.giroux.kevin.dofustuff.commons.security.Profile;
import com.giroux.kevin.dofustuff.commons.utils.Factory;
import com.giroux.kevin.dofustuff.users.persistence.entity.ProfileEntity;
import org.springframework.stereotype.Component;

/**
 * Factory des profils
 *
 * @author sngo
 */
@Component
public class ProfileFactory implements Factory<ProfileEntity, Profile> {

    /**
     * Créer un dto à partir d'un entité
     *
     * @param entity
     * @return
     */
    @Override
    public Profile entityToDto(ProfileEntity entity) {
        Profile profile = new Profile();
        if (entity != null) {
            profile.setId(entity.getIdProfile());
            profile.setName(entity.getName());
        }

        return profile;
    }

    /**
     * Créer une entité à partir d'un dto
     *
     * @param dto
     * @return
     */
    @Override
    public ProfileEntity dtoToEntity(Profile dto) {
        ProfileEntity profile = new ProfileEntity();
        profile.setIdProfile(dto.getId());
        profile.setName(dto.getName());
        profile.setVisible(true);
        return profile;
    }

}
