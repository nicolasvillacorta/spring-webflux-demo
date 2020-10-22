package com.accenture.webfluxdemo.handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

// Se testea igual que el controller, usando el webTestClient, pero no podemos usar la anotacion @WebFluxTest. Solo funciona 
// 	con @RestControllers, en su lugar tenemos que usar @SpringBootTest y @AutoConfigureWebTestClient.
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class SampleHandlerFunctionTest {

	@Autowired
	WebTestClient webTestClient;
	
	@Test
	public void fluxApproach() {
		Flux<Integer> integerFlux = webTestClient.get().uri("/functional/flux")
					.accept(MediaType.APPLICATION_JSON)
					.exchange() // Se subscribe el subscriber...
					.expectStatus().isOk()
					.returnResult(Integer.class)
					.getResponseBody();
		
		StepVerifier.create(integerFlux)
					.expectSubscription()
					.expectNext(1, 2, 3, 4)
					.verifyComplete();
	}
	
	@Test
	public void monoTest() {
		
		int expectedValue = 1;
		
		webTestClient.get().uri("/functional/mono")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Integer.class)
				.consumeWith( response -> Assertions.assertEquals(expectedValue, response.getResponseBody()));
		
	}
}
