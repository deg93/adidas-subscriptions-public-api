package es.davidenjuan.subscriptions.publicapi.service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.davidenjuan.subscriptions.publicapi.domain.User;
import es.davidenjuan.subscriptions.publicapi.exception.ResourceNotFoundException;
import es.davidenjuan.subscriptions.publicapi.repository.UserRepository;
import es.davidenjuan.subscriptions.publicapi.service.dto.UserDTO;
import es.davidenjuan.subscriptions.publicapi.service.mapper.UserMapper;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Creates a new user.
     * @param userDTO user to create.
     * @return created user.
     */
    public UserDTO create(UserDTO userDTO) {
        log.info("Request to create user (email: {})", userDTO.getEmail());

        // Persist user
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setAccessId(UUID.randomUUID().toString());
        userDTO.setCreationDate(ZonedDateTime.now());
        userDTO = save(userDTO);
        log.info("User created with ID {}", userDTO.getId());

        return userDTO;
    }

    /**
     * Creates or updates the specified user.
     * @param userDTO user to create/update.
     * @return created/updated user.
     */
    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Retrieves a user by its ID.
     * @param id ID of the user to return.
     * @return found user.
     */
    public Optional<UserDTO> findOne(String id) {
        log.info("Request to find user {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(userMapper::toDto);
    }

    /**
     * Retrieves a user by its email.
     * @param email email of the user to return.
     * @return found user.
     */
    public Optional<UserDTO> findOneByEmail(String email) {
        log.info("Request to find user by email {}", email);
        Optional<User> userOptional = userRepository.findOneByEmail(email);
        return userOptional.map(userMapper::toDto);
    }

    /**
     * Retrieves a user by its access ID.
     * @param accessId accessId of the user to return.
     * @return found user.
     */
    public Optional<UserDTO> findOneByAccessId(String accessId) {
        log.info("Request to find user by access ID {}", accessId);
        Optional<User> userOptional = userRepository.findOneByAccessId(accessId);
        return userOptional.map(userMapper::toDto);
    }

    /**
     * Asserts that the specified user exists and then returns it.
     * @param id user ID to assert its existence.
     * @return found user.
     * @throws ResourceNotFoundException if any user exists for the specified ID.
     */
    public UserDTO assertUserExistsAndReturn(String id) throws ResourceNotFoundException {
        log.info("Request to assert user {} existence", id);
        return findOne(id).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
    }

    /**
     * Asserts that one user exists for the given access ID and then returns it.
     * @param accessId user access ID to assert its existence.
     * @return found user.
     * @throws ResourceNotFoundException if any user exists for the specified access ID.
     */
    public UserDTO assertUserExistsByAccessIdAndReturn(String accessId) throws ResourceNotFoundException {
        log.info("Request to assert user existence by access ID {}", accessId);
        return findOneByAccessId(accessId).orElseThrow(() -> new ResourceNotFoundException("User with access ID " + accessId + " not found"));
    }
}
