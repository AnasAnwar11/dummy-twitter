package com.example.twitter_reactive.controller;

import com.example.twitter_reactive.entity.Tweet;
import com.example.twitter_reactive.entity.TweetContent;
import com.example.twitter_reactive.service.FileService;
import com.example.twitter_reactive.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RestController
public class TweetController {

    private final TweetService tweetService;
    private final FileService fileService;

    @Autowired
    public TweetController(TweetService tweetService, FileService fileService) {
        this.tweetService = tweetService;
        this.fileService = fileService;
    }

    @PostMapping("/tweets/{user-id}")    // need to find a way to detect current userId
    public Mono<Tweet> postTweet(@RequestPart("text")String tweetText, @RequestPart("image")FilePart tweetImage,
                                 @PathVariable("user-id") String userId) {
        return fileService.saveImage(tweetImage)
                .map(imageId -> TweetContent.builder().text(tweetText).imageId(imageId).build())
                .map(tweetContent ->
                    Tweet.builder().content(tweetContent).postedBy(userId).likedBy(Collections.emptyList()).build())
                .flatMap(tweetService::saveTweet);
    }

    @GetMapping("/tweets/text/{tweet-id}")
    public Mono<Tweet> getTweetText(@PathVariable("tweet-id") String tweetId) {
        return tweetService.getTweetById(tweetId);
    }

    @GetMapping("/tweets/image/{imageId}")
    public Mono<Void> getTweetImage(@PathVariable String imageId, ServerWebExchange exchange) {
        return fileService.loadImage(imageId, exchange);
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

    @GetMapping("/tweet/liked-by/{user-id}")
    public Flux<Tweet> getTweetsLikedByUser(@PathVariable("user-id")String userId) {
        return tweetService.getTweetsLikedByUser(userId);
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
