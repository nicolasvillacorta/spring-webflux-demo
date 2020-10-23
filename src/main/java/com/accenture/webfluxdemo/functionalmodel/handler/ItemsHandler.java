package com.accenture.webfluxdemo.functionalmodel.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.accenture.webfluxdemo.model.Item;
import com.accenture.webfluxdemo.repository.ItemDAO;

import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

	@Autowired
	ItemDAO itemDAO;
	
	public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(itemDAO.findAll(), Item.class);
		
	}
	
	public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {
		
		String id = serverRequest.pathVariable("id");
		Mono<Item> itemMono = itemDAO.findById(id);
		
		return itemMono
				.flatMap(item -> ServerResponse.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(item)) // El itemMono es un mono, pero el flatMap me da un "Item", por eso uso este estatico.
				)
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public  Mono<ServerResponse> createOneItem(ServerRequest serverRequest) {
		
		Mono<Item> itemToBeInserted = serverRequest.bodyToMono(Item.class); // El save del ODM recibe tipos reactor, no POJOS.
		
		return itemToBeInserted.flatMap(item -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(itemDAO.save(item), Item.class));
				
				
	}
	
	public  Mono<ServerResponse> deleteOneItem(ServerRequest serverRequest) {
		
		String id = serverRequest.pathVariable("id");
		Mono<Void> deletedItem = itemDAO.deleteById(id);

		return deletedItem.then(ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deletedItem, Void.class));
		
	}
	
	public  Mono<ServerResponse> updateOneItem(ServerRequest serverRequest) {
		
		String id = serverRequest.pathVariable("id");
		
		Mono<Item> updatedItem = 
				serverRequest.bodyToMono(Item.class)
					.flatMap(item -> {
						Mono<Item> itemMono = itemDAO.findById(id)
							.flatMap(currentItem -> {
								currentItem.setDescription(item.getDescription());
								currentItem.setPrice(item.getPrice());
								return itemDAO.save(currentItem);
							});
						return itemMono;
					});

		return updatedItem
					.flatMap(item -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(item))
					).switchIfEmpty(ServerResponse.notFound().build());
	}
}
