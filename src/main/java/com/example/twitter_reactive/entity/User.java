package com.example.twitter_reactive.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "users")
public class User {
    private String _id;
    private int age;
    private String email;
    private String name;
    private List<String> follows;
    private List<String> followedBy;
}
