package com.giroux.kevin.dofustuff.users.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity pour les profils
 *
 * @author mfreitas
 */
@Entity
@Table(name = "profile")
public class ProfileEntity {

    /**
     * Id du profil
     */
    @Id
    @Column(name = "idProfile", unique = true, nullable = false, length = 50)
    private String idProfile;

    /**
     * Nom du profil
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * visibility
     */
    @Column(name = "visible")
    private boolean visible;


    public String getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(String idProfile) {
        this.idProfile = idProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }


}
