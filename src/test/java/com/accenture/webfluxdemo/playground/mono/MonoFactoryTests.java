package com.accenture.webfluxdemo.playground.mono;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoFactoryTests {

	@Test
	public void monoUsingJustOrEmpty() {
		
		Mono<String> mono = Mono.justOrEmpty(null); // Mono.empty();
		
		StepVerifier.create(mono.log())
			.verifyComplete();
	}
	
	@Test
	public void monoUsingSupplier() {
		
		// Supplier es una interfaz funcional  de java 8. Representa una funcion que no recibe argumentos y produce un valor del tipo T.
		Supplier<String> stringSupplier = () -> "javaShark";
		
		Mono<String> stringMono = Mono.fromSupplier(stringSupplier);

		StepVerifier.create(stringMono.log())
			.expectNext("javaShark")
			.verifyComplete();
	}
	
	@Test
	public void asd() {
		
	}
	
}
