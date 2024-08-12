package com.example.twitter_reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterReactiveApplication {

	private static final Logger log = LoggerFactory.getLogger(TwitterReactiveApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TwitterReactiveApplication.class, args);
	}

}
