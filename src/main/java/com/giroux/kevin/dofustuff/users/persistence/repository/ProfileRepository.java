package com.giroux.kevin.dofustuff.users.persistence.repository;

import com.giroux.kevin.dofustuff.users.persistence.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Interface pour faire le lien entre la base jdbc et l'entité profil
 *
 * @author sngo
 */
@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, String> {
    public List<ProfileEntity> findByVisible(boolean visibility);
}
