package com.accenture.webfluxdemo.util.initialize;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.accenture.webfluxdemo.model.Item;
import com.accenture.webfluxdemo.repository.ItemDAO;

import reactor.core.publisher.Flux;

@Component
public class ItemDataInitializer implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemDataInitializer.class);
	
	@Autowired
	ItemDAO itemDAO;
	
	@Override
	public void run(String... args) throws Exception {
		initialDataSetup(); // Se ejecuta en el startup y agrega 4 registros a la base de datos.
	}
	
	private void initialDataSetup() {
		itemDAO.deleteAll()
			.thenMany(Flux.fromIterable(data())) // Obtiene un nuevo flujo en el pipeline.
			.flatMap(itemDAO::save)
			.subscribe(item -> LOGGER.info("Item insertado de commandLineRuner: " + item));
	}
	
	private List<Item> data(){
		
		return Arrays.asList(
				new Item("pepito", "iPhone 6s", 399.99),
				new Item("juancito", "Camiseta de boquita", 329.99),
				new Item("joseph", "Apple Watch", 349.99),
				new Item("ABC", "Auriculares Sony", 19.99));
	}
}
