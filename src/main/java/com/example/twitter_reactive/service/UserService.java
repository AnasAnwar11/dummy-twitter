package com.example.twitter_reactive.service;

import com.example.twitter_reactive.entity.User;
import com.example.twitter_reactive.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public UserService(UserRepository userRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.userRepository = userRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Mono<User> findUserById(String id) {
        return userRepository.findById(new ObjectId(id));
    }

    public Flux<User> findFollowersOfUser(String userId) {  // need to move to UserRepository
        MatchOperation matchUser = Aggregation.match(Criteria.where("_id").is(new ObjectId(userId)));
        LookupOperation lookupFollowers = Aggregation.lookup("users", "followedBy", "_id", "followers");
        ProjectionOperation projectFollowers = Aggregation.project("followers").andExclude("_id");
        UnwindOperation unwindFollowers = Aggregation.unwind("followers");
        ReplaceRootOperation replaceRootOperation = Aggregation.replaceRoot("followers");

        TypedAggregation<User> userTypedAggregation = Aggregation.newAggregation(
                User.class,
                matchUser,
                lookupFollowers,
                projectFollowers,
                unwindFollowers,
                replaceRootOperation
        );

        return reactiveMongoTemplate.aggregate(userTypedAggregation, User.class);
    }

    public Flux<User> findFollowingsOfUser(String userId) { // need to move to UserRepository
        MatchOperation matchUser = Aggregation.match(Criteria.where("_id").is(new ObjectId(userId)));
        LookupOperation lookupFollowedUsers = Aggregation.lookup("users", "follows", "_id", "follows");
        ProjectionOperation projectFollowedUsers = Aggregation.project("follows").andExclude("_id");
        UnwindOperation unwindFollowedUsers = Aggregation.unwind("follows");
        ReplaceRootOperation replaceRootOperation = Aggregation.replaceRoot("follows");

        TypedAggregation<User> userTypedAggregation = Aggregation.newAggregation(
                User.class,
                matchUser,
                lookupFollowedUsers,
                projectFollowedUsers,
                unwindFollowedUsers,
                replaceRootOperation
        );

        return reactiveMongoTemplate.aggregate(userTypedAggregation, User.class);
    }
}
