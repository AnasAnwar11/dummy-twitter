package com.example.twitter_reactive.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "tweets")
public class Tweet {
    private ObjectId _id;
    private ObjectId postedBy;
    private List<ObjectId> likedBy;
    private TweetContent content;
}
