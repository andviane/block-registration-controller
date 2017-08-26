package com.smartvalor.db;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class BusinessEntity {

    @Id
    @Column(updatable=false)
    private UUID id;
    private String name;
    private String form;
    private Date dateOfIncorporation;
    private String placeOfIncorporation;
    private String registrationNumber;
    
    // Entity may have more one official address (Switzerland, USA departments)
    @OneToMany(targetEntity = Address.class, mappedBy = "businessEntity")
    private Set<Address> address;
    
    // Entity may have more than one  representative even if there is only one department.
    @ManyToMany(targetEntity = Person.class)    
    private Set<Person> representative;
    
    @CreationTimestamp
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;

    private boolean reviewed;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Date getDateOfIncorporation() {
        return dateOfIncorporation;
    }

    public void setDateOfIncorporation(Date dateOfIncorporation) {
        this.dateOfIncorporation = dateOfIncorporation;
    }

    public String getPlaceOfIncorporation() {
        return placeOfIncorporation;
    }

    public void setPlaceOfIncorporation(String placeOfIncorporation) {
        this.placeOfIncorporation = placeOfIncorporation;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public Set<Person> getRepresentative() {
		return representative;
	}

	public void setRepresentative(Set<Person> representative) {
		this.representative = representative;
	}
}
