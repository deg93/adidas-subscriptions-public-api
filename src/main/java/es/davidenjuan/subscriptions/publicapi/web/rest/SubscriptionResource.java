package es.davidenjuan.subscriptions.publicapi.web.rest;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.davidenjuan.subscriptions.publicapi.exception.ResourceNotFoundException;
import es.davidenjuan.subscriptions.publicapi.security.AuthoritiesConstants;
import es.davidenjuan.subscriptions.publicapi.security.SecurityUtils;
import es.davidenjuan.subscriptions.publicapi.service.SubscriptionService;
import es.davidenjuan.subscriptions.publicapi.service.dto.SubscriptionDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);

    private final SubscriptionService subscriptionService;

    public SubscriptionResource(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @ApiOperation("Creates a new subscription")
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionDTO> createSubscription(
        @ApiParam("The subscription to create")
        @RequestBody
        @Valid
        SubscriptionDTO subscription
    ) {
        log.info("REST request to create subscription");
        subscription = subscriptionService.create(subscription);
        subscription.setUserAccessId(null); // User access ID only can be retrieved by confirmation email
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    @ApiOperation("Returns existing subscriptions")
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptions(
        @ApiParam("User access ID for which subscriptions will be returned. If not specified, all subscriptions will be returned (only accesible for administrators).")
        @RequestParam(required = false)
        String userAccessId
    ) {
        log.info("REST request to get subscriptions (userAccessId: {})", userAccessId);

        // Only administrators can retrieve all subscriptions
        List<SubscriptionDTO> subscriptions;
        if (StringUtils.hasText(userAccessId)) {
            subscriptions = subscriptionService.findByUserAccessId(userAccessId);
        } else {
            SecurityUtils.assertCurrentUserHasAnyOfAuthorities(AuthoritiesConstants.ADMIN);
            subscriptions = subscriptionService.findAll();
        }

        return ResponseEntity.ok(subscriptions);
    }

    @ApiOperation("Returns the specified subscription")
    @GetMapping("/subscriptions/{id}")
    public ResponseEntity<SubscriptionDTO> getSubscription(
        @ApiParam("ID of the subscription to return")
        @PathVariable
        String id
    ) {
        log.info("REST request to get subscription {}", id);
        Optional<SubscriptionDTO> subscriptionOpt = subscriptionService.findOne(id).map(subscription -> {
            subscription.setUserAccessId(null); // User access ID only can be retrieved by confirmation email
            return subscription;
        });
        ResponseEntity<SubscriptionDTO> response = subscriptionOpt.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Subscription " + id + " not found"));
        return response;
    }

    @ApiOperation("Cancels the specified subscription")
    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<Void> cancelSubscription(
        @ApiParam("ID of the subscription to cancel")
        @PathVariable
        String id
    ) {
        log.info("REST request to cancel subscription {}", id);
        subscriptionService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}
