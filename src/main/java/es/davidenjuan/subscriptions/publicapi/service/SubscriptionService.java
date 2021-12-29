package es.davidenjuan.subscriptions.publicapi.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.davidenjuan.subscriptions.publicapi.configuration.ApplicationProperties;
import es.davidenjuan.subscriptions.publicapi.exception.UserAlreadySubscribedException;
import es.davidenjuan.subscriptions.publicapi.security.AuthoritiesConstants;
import es.davidenjuan.subscriptions.publicapi.security.SecurityUtils;
import es.davidenjuan.subscriptions.publicapi.security.jwt.TokenProvider;
import es.davidenjuan.subscriptions.publicapi.service.dto.SubscriptionDTO;
import es.davidenjuan.subscriptions.publicapi.service.dto.UserDTO;

@Service
public class SubscriptionService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final RestTemplate restTemplate;

    private final String internalApiUri;

    public SubscriptionService(UserService userService, TokenProvider tokenProvider, RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.restTemplate = restTemplate;
        this.internalApiUri = applicationProperties.getInternalApi().getUri();
    }

    /**
     * Creates a new subscription.
     * @param subscriptionDTO subscription to create.
     * @return created subscription.
     */
    public SubscriptionDTO create(SubscriptionDTO subscriptionDTO) {
        log.info("Request to create subscription");

        // Search user by subscription email (and update if required) or create it
        Optional<UserDTO> userOpt = userService.findOneByEmail(subscriptionDTO.getEmail()).map((user) -> updateUserIfRequired(user, subscriptionDTO));
        UserDTO user = userOpt.orElseGet(() -> userService.create(buildUserFromSubscription(subscriptionDTO)));
        log.info("Subscription user ready: {}", user.getId());

        // Send subscription creation request
        subscriptionDTO.setUserId(user.getId());
        subscriptionDTO.setUserAccessId(user.getAccessId());
        String jwt = createJwtForUser(user.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubscriptionDTO> requestEntity = new HttpEntity<SubscriptionDTO>(subscriptionDTO, headers);
        ResponseEntity<SubscriptionDTO> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(internalApiUri, requestEntity, SubscriptionDTO.class);
        } catch (HttpClientErrorException.Conflict e) {
            log.warn("User {} already subscribed to {}", subscriptionDTO.getEmail(), subscriptionDTO.getNewsletterId());
            throw new UserAlreadySubscribedException("User " + subscriptionDTO.getEmail() + " already subscribed to " + subscriptionDTO.getNewsletterId());
        }
        log.info("Subscription created successfully");

        return responseEntity.getBody();
    }

    /**
     * Returns all the subscriptions.
     * @return all the subscriptions.
     */
    public List<SubscriptionDTO> findAll() {
        log.info("Request to get all subscriptions");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + SecurityUtils.getCurrentUserJWT().get()); // Only administrators can run this method (authenticated users)
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(headers);
        ResponseEntity<List<SubscriptionDTO>> responseEntity = restTemplate.exchange(internalApiUri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<SubscriptionDTO>>() {});
        return responseEntity.getBody();
    }

    /**
     * Returns the list of subscriptions for the given user access ID.
     * @param userAccessId user access ID for which subscriptions will be returned.
     * @return subscriptions of the specified user.
     */
    public List<SubscriptionDTO> findByUserAccessId(String userAccessId) {
        log.info("Request to get subscriptions for user access ID {}", userAccessId);

        // Search user by given access ID
        UserDTO user = userService.assertUserExistsByAccessIdAndReturn(userAccessId);
        log.info("User for access ID {} found: {}", userAccessId, user.getId());

        // Return found user subscriptions
        return findByUserId(user.getId());
    }

    /**
     * Returns the list of subscriptions for the given user.
     * @param userId valid user ID for which subscriptions will be returned.
     * @return subscriptions of the specified user.
     */
    private List<SubscriptionDTO> findByUserId(String userId) {
        log.info("Request to get subscriptions for user {}", userId);
        String url = internalApiUri + "?userId=" + userId;
        String jwt = createJwtForUser(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(headers);
        ResponseEntity<List<SubscriptionDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<SubscriptionDTO>>() {});
        return responseEntity.getBody();
    }

    /**
     * Returns the subscription with the specified ID.
     * @param id ID of the subscription to return.
     * @return found subscription.
     */
    public Optional<SubscriptionDTO> findOne(String id) {
        log.info("Request to get subscription with ID {}", id);
        String url = internalApiUri + "/" + id;
        String jwt = createJwtForUser("anonymous");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(headers);
        Optional<SubscriptionDTO> result;
        try {
            ResponseEntity<SubscriptionDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SubscriptionDTO.class);
            result = Optional.of(responseEntity.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Subscription {} not found", id);
            result = Optional.empty();
        }
        return result;
    }

    /**
     * Cancels the subscription with the specified ID.
     * @param id ID of the subscription to cancel.
     */
    public void cancel(String id) {
        log.info("Request to cancel subscription with ID {}", id);
        String url = internalApiUri + "/" + id;
        String jwt = createJwtForUser("anonymous");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
    }

    /**
     * For a given (valid) subscription, returns an instance of a {@link UserDTO}.
     * @param subscriptionDTO subscription for which {@link UserDTO} instance will be built.
     * @return built {@link UserDTO} instance.
     */
    private UserDTO buildUserFromSubscription(SubscriptionDTO subscriptionDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(subscriptionDTO.getEmail());
        userDTO.setFirstName(subscriptionDTO.getFirstName());
        userDTO.setGender(subscriptionDTO.getGender());
        userDTO.setBirthDate(subscriptionDTO.getBirthDate());
        return userDTO;
    }

    /**
     * Updates user data if the specified subscription user data does not match with specified user.
     * @param user user to update its data if required.
     * @param subscription subscription with (possible) updated user data.
     * @return updated user (or the same user if not updated).
     */
    private UserDTO updateUserIfRequired(UserDTO user, SubscriptionDTO subscription) {
        // Evaluate if user data required to be updated
        boolean updateRequired =
            !Objects.equals(user.getFirstName(), subscription.getFirstName()) ||
            !Objects.equals(user.getGender(), subscription.getGender()) ||
            !Objects.equals(user.getBirthDate(), subscription.getBirthDate());
        if (!updateRequired) {
            return user;
        }
        log.info("User data must be updated");

        // Update user data
        user.setFirstName(subscription.getFirstName());
        user.setGender(subscription.getGender());
        user.setBirthDate(subscription.getBirthDate());
        return userService.save(user);
    }

    /**
     * Creates a JWT for the specified user ID.
     * @param userId user ID for which JWT will be created.
     * @return created JWT.
     */
    private String createJwtForUser(String userId) {
        Collection<? extends GrantedAuthority> authorities =
            Arrays.asList(AuthoritiesConstants.ANONYMOUS)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(userId, "", authorities);
        return tokenProvider.createToken(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
