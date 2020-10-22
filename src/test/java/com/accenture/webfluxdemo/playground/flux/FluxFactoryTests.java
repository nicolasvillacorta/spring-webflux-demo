package com.accenture.webfluxdemo.playground.flux;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


public class FluxFactoryTests {
	
	
	List<String> names = Arrays.asList("nicolas", "santiago", "shark");
	
	
	@Test
	public void fluxUsingIterable() {
		
		Flux<String> namesFlux = Flux.fromIterable(names)
				.log();
		
		StepVerifier.create(namesFlux)
			.expectNext("nicolas", "santiago", "shark")
			.verifyComplete();

		
	}
	
	@Test
	public void fluxUsingArray() {
		
		String[] namesArray = new String[]{"nicolas", "santiago", "shark"};
		
		Flux<String> namesFlux = Flux.fromArray(namesArray);
		
		StepVerifier.create(namesFlux)
			.expectNext("nicolas", "santiago", "shark")
			.verifyComplete();
		
	}
	
	@Test
	public void fluxUsingStream() {
		
		Flux<String> namesFlux = Flux.fromStream(names.stream());
	
		StepVerifier.create(namesFlux)
			.expectNext("nicolas", "santiago", "shark")
			.verifyComplete(); // En el verify el stream va a comenzar a emitir los datos.
		
	}
	
	@Test
	public void fluxUsingRange() {
		
		Flux<Integer> integerFlux = Flux.range(1, 5).log(); // Empezando de 1, 5 elementos.
		
		StepVerifier.create(integerFlux)
			.expectNext(1, 2, 3, 4, 5)
			.verifyComplete();

	}
}
