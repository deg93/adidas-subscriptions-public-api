package es.davidenjuan.subscriptions.publicapi.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import es.davidenjuan.subscriptions.publicapi.domain.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public Optional<User> findOneByEmail(String email);

    public Optional<User> findOneByAccessId(String accessId);
}
