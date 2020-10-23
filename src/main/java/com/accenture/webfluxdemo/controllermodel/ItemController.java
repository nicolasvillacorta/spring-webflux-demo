package com.accenture.webfluxdemo.controllermodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.webfluxdemo.model.Item;
import com.accenture.webfluxdemo.repository.ItemDAO;
import com.accenture.webfluxdemo.util.Constants;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemController {

	@Autowired
	ItemDAO itemDAO;
	
	@GetMapping(Constants.Endpoints.ITEM_CONTROLLER_ENDPOINT)
	public Flux<Item> getAllItems() {
		return itemDAO.findAll();
	}
	
	@GetMapping(Constants.Endpoints.ITEM_CONTROLLER_ENDPOINT + "/{id}")
	public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id){
		
		return itemDAO.findById(id)
				.map(item -> new ResponseEntity<>(item, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@PostMapping(Constants.Endpoints.ITEM_CONTROLLER_ENDPOINT)
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Item> createItem(@RequestBody Item item){
		return itemDAO.save(item)
				.log();
	}
	
	// La documentacion de WebFlux dice que aunque no retornen nada los endpoints, hagamos que los metodos
	// 	retornen una de las clases de Reactor, asi funciona bien el evento, sino retorna null y puede romper.
	@DeleteMapping(Constants.Endpoints.ITEM_CONTROLLER_ENDPOINT + "/{id}")
	public Mono<Void> deleteItem(@PathVariable String id){ 
		return itemDAO.deleteById(id)
				.log();
	}
	
	@PutMapping(Constants.Endpoints.ITEM_CONTROLLER_ENDPOINT + "/{id}")
	public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item){
		return itemDAO.findById(id)
					.flatMap(currentItem -> {
						currentItem.setPrice(item.getPrice());
						currentItem.setDescription(item.getDescription());
						return itemDAO.save(currentItem);
					}) // Null safe.
					.map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
					.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
