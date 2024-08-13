package com.example.twitter_reactive.repository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Repository
public class FileRepository {

    private final ReactiveGridFsTemplate reactiveGridFsTemplate;

    @Autowired
    public FileRepository(ReactiveGridFsTemplate reactiveGridFsTemplate) {
        this.reactiveGridFsTemplate = reactiveGridFsTemplate;
    }

    public Mono<String> save(FilePart image) {
        return image.filename().isEmpty() ? Mono.just("") :
                reactiveGridFsTemplate.store(image.content(), image.filename())
                .map(ObjectId::toHexString);
    }

    public Mono<Void> load(String id, ServerWebExchange exchange) {
        ObjectId objectId = new ObjectId(id);
        return reactiveGridFsTemplate.findOne(new Query(Criteria.where("_id").is(objectId)))
                .flatMap(reactiveGridFsTemplate::getResource)
                .flatMap(resource -> exchange.getResponse().writeWith(resource.getContent()));  // delayElements(Duration.ofSeconds(1)) for test
    }
}
