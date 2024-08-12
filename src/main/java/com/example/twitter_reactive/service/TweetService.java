package com.example.twitter_reactive.service;

import com.example.twitter_reactive.entity.Tweet;
import com.example.twitter_reactive.entity.User;
import com.example.twitter_reactive.repository.TweetRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    public TweetService(TweetRepository tweetRepository, ReactiveMongoTemplate reactiveMongoTemplate) {
        this.tweetRepository = tweetRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Mono<Tweet> getTweetById(String tweetId) {
        return tweetRepository.findById(new ObjectId(tweetId));
    }

    public Flux<Tweet> getTweetsByUser(String userId) {
        return tweetRepository.findAllByPostedBy(new ObjectId(userId));
    }

    public Mono<Tweet> postTweet(Tweet tweet, String userId) {
        tweet.setPostedBy(new ObjectId(userId));
        tweet.setLikedBy(new ArrayList<>());
        return tweetRepository.save(tweet);
    }

    public Mono<Integer> likeTweet(String tweetId, String userId) {
        return tweetRepository.updateAddLikedBy(new ObjectId(tweetId), new ObjectId(userId));
    }

    public Mono<Integer> unLikeTweet(String tweetId, String userId) {
        return tweetRepository.updateRemoveLikedBy(new ObjectId(tweetId), new ObjectId(userId));
    }


    public Flux<Tweet> getTweetsPostedByFollowedUsers(String userId) { // need to move to TweetRepository
        MatchOperation matchUser = Aggregation.match(Criteria.where("_id").is(new ObjectId(userId)));
        LookupOperation lookupTweets = Aggregation.lookup("tweets", "follows", "postedBy", "postedByFollowedUsers");
        ProjectionOperation projectTweets = Aggregation.project("postedByFollowedUsers").andExclude("_id");
        UnwindOperation unwindTweetArray = Aggregation.unwind("postedByFollowedUsers");
        ReplaceRootOperation replaceRootOperation = Aggregation.replaceRoot("postedByFollowedUsers");

        TypedAggregation<User> userTypedAggregation = Aggregation.newAggregation(
                User.class,
                matchUser,
                lookupTweets,
                projectTweets,
                unwindTweetArray,
                replaceRootOperation
        );

        return reactiveMongoTemplate.aggregate(userTypedAggregation, Tweet.class);
    }
}
