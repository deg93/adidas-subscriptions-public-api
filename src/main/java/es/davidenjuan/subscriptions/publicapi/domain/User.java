package es.davidenjuan.subscriptions.publicapi.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import es.davidenjuan.subscriptions.publicapi.domain.enumeration.Gender;

@SuppressWarnings("serial")
@Document(collection = "user")
public class User implements Serializable {

    @Id
    private String id;

    @NotNull
    @Field("access_id")
    private String accessId;

    @NotNull
    @Field("email")
    private String email;

    @Field("first_name")
    private String firstName;

    @Field("gender")
    private Gender gender;

    @NotNull
    @Field("birth_date")
    private LocalDate birthDate;

    @NotNull
    @Field("creation_date")
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
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [id=");
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
