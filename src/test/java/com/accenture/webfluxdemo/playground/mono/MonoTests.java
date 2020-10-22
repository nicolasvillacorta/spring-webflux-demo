package com.accenture.webfluxdemo.playground.mono;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTests {

	
	@Test
	public void monoTest() {
		
		Mono<String> stringMono = Mono.just("Spring");
		
		StepVerifier.create(stringMono.log())
			.expectNext("Spring")
				.verifyComplete();
	}
	
	@Test
	public void monoTest_Error() {
		
 		StepVerifier.create(Mono.error(new RuntimeException("Mono exception")).log())
 			.expectError(RuntimeException.class)
				.verify();
	}
	
}
