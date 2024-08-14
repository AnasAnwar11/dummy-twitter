package com.example.twitter_reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "tweets")
public class Tweet {
    private String _id;
    private String postedBy;
    private List<String> likedBy;
    private TweetContent content;
}
