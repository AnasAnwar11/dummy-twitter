package com.example.twitter_reactive.controller;

import com.example.twitter_reactive.entity.Tweet;
import com.example.twitter_reactive.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping("/tweets/{user-id}")    // need to find a way to detect current userId
    public Mono<Tweet> postTweet(@RequestBody Tweet tweet, @PathVariable("user-id") String userId) {
        return tweetService.postTweet(tweet, userId);
    }

    @GetMapping("/tweets/{tweet-id}")
    public Mono<Tweet> getTweet(@PathVariable("tweet-id") String tweetId) {
        return tweetService.getTweetById(tweetId);
    }

    @GetMapping("/tweet/like-count/{tweet-id}")
    public Mono<Integer> getTweetLikeCount(@PathVariable("tweet-id") String tweetId) {
        return tweetService.getTweetById(tweetId).map(tweet -> tweet.getLikedBy().size());  // need to find an alternative
    }

    @PostMapping("/tweet/like/{tweet-id}/{user-id}")    // need to detect the ids
    public Mono<Integer> likeTweet(@PathVariable("tweet-id") String tweetId, @PathVariable("user-id") String userId) {
        return tweetService.likeTweet(tweetId, userId);
    }

    @PostMapping("/tweet/un-like/{tweet-id}/{user-id}")    // need to detect the ids
    public Mono<Integer> unLikeTweet(@PathVariable("tweet-id") String tweetId, @PathVariable("user-id") String userId) {
        return tweetService.unLikeTweet(tweetId, userId);
    }

    @GetMapping("/tweets/by-user/{user-id}")
    public Flux<Tweet> getTweetsByUser(@PathVariable("user-id") String userId) {
        return tweetService.getTweetsByUser(userId);
    }

    @GetMapping("/tweets/by-followee/{user-id}")
    public Flux<Tweet> getTweetsByFollowee(@PathVariable("user-id") String userId) {
        return tweetService.getTweetsPostedByFollowedUsers(userId);
    }
}
