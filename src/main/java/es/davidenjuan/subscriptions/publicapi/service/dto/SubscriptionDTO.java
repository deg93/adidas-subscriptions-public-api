package es.davidenjuan.subscriptions.publicapi.service.dto;

import java.time.LocalDate;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import es.davidenjuan.subscriptions.publicapi.domain.enumeration.Gender;

public class SubscriptionDTO {

    private String id;

    private String userId;

    private String userAccessId;

    @NotNull
    @Email
    private String email;

    private String firstName;

    private Gender gender;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    @AssertTrue
    private Boolean consent;

    @NotEmpty
    private String newsletterId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccessId() {
        return userAccessId;
    }

    public void setUserAccessId(String userAccessId) {
        this.userAccessId = userAccessId;
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

    public Boolean getConsent() {
        return consent;
    }

    public void setConsent(Boolean consent) {
        this.consent = consent;
    }

    public String getNewsletterId() {
        return newsletterId;
    }

    public void setNewsletterId(String newsletterId) {
        this.newsletterId = newsletterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDTO)) {
            return false;
        }
        return id != null && id.equals(((SubscriptionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SubscriptionDTO [id=");
        builder.append(id);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", userAccessId=");
        builder.append(userAccessId);
        builder.append(", email=");
        builder.append(email);
        builder.append(", firstName=");
        builder.append(firstName);
        builder.append(", gender=");
        builder.append(gender);
        builder.append(", birthDate=");
        builder.append(birthDate);
        builder.append(", consent=");
        builder.append(consent);
        builder.append(", newsletterId=");
        builder.append(newsletterId);
        builder.append("]");
        return builder.toString();
    }
}
