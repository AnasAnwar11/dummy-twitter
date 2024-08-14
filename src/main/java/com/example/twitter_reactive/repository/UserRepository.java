package com.example.twitter_reactive.repository;

import com.example.twitter_reactive.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
