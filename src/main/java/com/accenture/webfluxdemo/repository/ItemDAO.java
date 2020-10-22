package com.accenture.webfluxdemo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.accenture.webfluxdemo.model.Item;

import reactor.core.publisher.Mono;

public interface ItemDAO extends ReactiveMongoRepository<Item, String> {

	Mono<Item> findByDescription(String description);
	
}
