package com.accenture.webfluxdemo.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.accenture.webfluxdemo.model.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class ItemDAOTest {

	@Autowired
	ItemDAO itemDAO;
	
	List<Item> itemList = Arrays.asList(
										new Item(null, "Samsung TV", 400.0), 
										new Item(null, "LG TV", 420.0),
										new Item(null, "Apple Watch", 299.99), 
										new Item(null, "Beats Headphones", 149.99),
										new Item("ABC", "Sony Headphones", 149.99)
										);
	
	@BeforeEach
	public void setUp() {
		itemDAO.deleteAll()
				.thenMany(Flux.fromIterable(itemList)) // Va a tomar un flux de los items.
				.flatMap(itemDAO::save) // Va a guardarlos uno por uno.
				.doOnNext((item -> {
					System.out.println("Inserted item is: " + item);
				})) // En onNext va a printear el nombre.
				.blockLast(); // Este va a esperar que todos los items se hayan guardado antes de seguir y hacer el get, sino el get 
							  // 	va a ejecutarse antes de haber guardado los items.
	}
	
	@Test
	public void getAllItems() {
		
		StepVerifier.create(itemDAO.findAll())
				.expectSubscription()
				.expectNextCount(5)
				.verifyComplete();
		
	}
	
	@Test
	public void getItemByID() {
		
		StepVerifier.create(itemDAO.findById("ABC"))
				.expectSubscription()
				.expectNextMatches(item -> item.getDescription().equals("Sony Headphones"))
				.verifyComplete();
				
	}
	
	@Test
	public void findItemByDescription() {
		StepVerifier.create(itemDAO.findByDescription("Sony Headphones").log())
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}
	
	@Test
	public void saveItem() {
		
		Item newItem = new Item(null, "iPhone 6s", 550.0);
		
		Mono<Item> savedItem = itemDAO.save(newItem);
		
		StepVerifier.create(savedItem.log("saveItem: "))
				.expectSubscription()
				.expectNextMatches(item -> item.getId() != null && item.getDescription().equals("iPhone 6s"))
				.verifyComplete();
		
	}
	
	@Test
	public void updateItem() {
		
		double newPrice = 520.0;
		
		Mono<Item> updatedItem = itemDAO.findByDescription("LG TV")
			.map(item -> {
				item.setPrice(newPrice);
				return item;
			})
			.flatMap(itemDAO::save);
		
		StepVerifier.create(updatedItem.log())
			.expectSubscription()
			.expectNextMatches(item -> item.getPrice() == 520.0)
			.verifyComplete();
	}
	
	@Test
	public void deleteItemById() {
		
		Mono<Void> deletedItem = itemDAO.findByDescription("LG TV") // Mono<Item>
				.flatMap(itemDAO::delete);

		StepVerifier.create(deletedItem.log())
				.expectSubscription()
				.verifyComplete();
		
		StepVerifier.create(itemDAO.findAll().log("New item list: "))
				.expectSubscription()
				.expectNextCount(4)
				.verifyComplete();
		
	}
}
