package com.example.twitter_reactive.service;

import com.example.twitter_reactive.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Mono<String> saveImage(FilePart image) {
        return fileRepository.save(image);
    }

    public Mono<Void> loadImage(String id, ServerWebExchange exchange) {
        return fileRepository.load(id, exchange);
    }
}
