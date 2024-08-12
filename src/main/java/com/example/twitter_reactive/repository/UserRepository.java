package com.example.twitter_reactive.repository;

import com.example.twitter_reactive.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {
}
