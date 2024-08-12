package com.example.twitter_reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TweetContent {
    private String text;
    private String image;   // to consider later
}
