package es.davidenjuan.subscriptions.publicapi.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import es.davidenjuan.subscriptions.publicapi.domain.enumeration.Gender;

@SuppressWarnings("serial")
public class UserDTO implements Serializable {

    private String id;

    private String accessId;

    @NotNull
    private String email;

    private String firstName;

    private Gender gender;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private ZonedDateTime creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDTO)) {
            return false;
        }
        return id != null && id.equals(((UserDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserDTO [id=");
        builder.append(id);
        builder.append(", accessId=");
        builder.append(accessId);
        builder.append(", email=");
        builder.append(email);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", gender=");
        builder.append(gender);
        builder.append(", birthDate=");
        builder.append(birthDate);
        builder.append(", creationDate=");
        builder.append(creationDate);
        builder.append("]");
        return builder.toString();
    }
}
