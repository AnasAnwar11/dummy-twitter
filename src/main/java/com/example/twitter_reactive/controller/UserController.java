package com.example.twitter_reactive.controller;

import com.example.twitter_reactive.entity.User;
import com.example.twitter_reactive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @GetMapping("/user/{id}/follower-count")
    public Mono<Integer> getFollowerCount(@PathVariable String id) {
        return userService.findUserById(id).map(user -> user.getFollowedBy().size());   // need to find alternative
    }

    @GetMapping("/user/{id}/following-count")
    public Mono<Integer> getFollowingCount(@PathVariable String id) {
        return userService.findUserById(id).map(user -> user.getFollows().size());   // need to find alternative
    }

    @GetMapping("/user/{id}/followers")
    public Flux<User> getFollowersOfUser(@PathVariable String id) {
        return userService.findFollowersOfUser(id);
    }

    @GetMapping("/user/{id}/followings")
    public Flux<User> getFollowingsOfUser(@PathVariable String id) {
        return userService.findFollowingsOfUser(id);
    }
}
