package com.accenture.webfluxdemo.playground.operations;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FilterTest {

	List<String> names = Arrays.asList("nicolas", "santiago", "shark");
	
	@Test
	public void filterTest() {
		
		// Al subscribirse, el publisher va a pasar todos los valores, que seran filtrados.
		Flux<String> namesFlux = Flux.fromIterable(names) 
				.filter(n -> n.startsWith("s"))
				.log();
		
		StepVerifier.create(namesFlux)
			.expectNext("santiago", "shark")
			.verifyComplete();
		
	}
	
	@Test
	public void filterTestLength() {
		
		// Al subscribirse, el publisher va a pasar todos los valores, que seran filtrados.
		Flux<String> namesFlux = Flux.fromIterable(names) 
				.filter(n -> n.length() > 5 )
				.log();
		
		StepVerifier.create(namesFlux)
			.expectNext("nicolas", "santiago")
			.verifyComplete();
		
	}
	
	
}
