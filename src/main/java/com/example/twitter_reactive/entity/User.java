package com.example.twitter_reactive.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class User {
    private ObjectId _id;
    private int age;
    private String email;
    private String name;
    private List<ObjectId> follows;
    private List<ObjectId> followedBy;
}
