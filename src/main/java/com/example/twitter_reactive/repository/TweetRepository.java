package com.example.twitter_reactive.repository;

import com.example.twitter_reactive.entity.Tweet;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TweetRepository extends ReactiveMongoRepository<Tweet, String> {

    Flux<Tweet> findAllByPostedBy(String String);

    @Query("{ '_id' : ?0 }")
    @Update("{ $addToSet : { 'likedBy' : ?1 } }")
    Mono<Integer> updateAddLikedBy(String tweetId, String userId);

    @Query("{ '_id' : ?0 }")
    @Update("{ $pull : { 'likedBy' : ?1 } }")
    Mono<Integer> updateRemoveLikedBy(String tweetId, String userId);
}
