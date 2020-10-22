package com.accenture.webfluxdemo.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

// Este test ahora esta rompiendo por un problema con contexto de spring, debe ser alguna anotacion.
@WebFluxTest // Genera la instancia del WebTestClient, solo funciona para testing de controladores no funcionales.
@DirtiesContext
class ReactiveControllerTest {

	@Autowired // La anotacion WebFluxTest genera el webClient en el contexto.
	WebTestClient webTestClient;
	
	@Test
	public void fluxApproach1() {
		
		Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
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
	public void fluxApproach2() {
		
		webTestClient.get().uri("/flux")
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectHeader().contentType(MediaType.APPLICATION_JSON)
					.expectBodyList(Integer.class)
					.hasSize(4);
		// En este test solo evaluamos la cantidad de values de respuesta, no el contenido.
	}
	
	@Test
	public void fluxApproach3() {
		
		List<Integer> expectedIntegerList = Arrays.asList(1, 2 , 3, 4);
		
		EntityExchangeResult<List<Integer>> entityExchangeResult = webTestClient.get().uri("/flux")
					.accept(MediaType.APPLICATION_JSON)
					.exchange()
					.expectStatus().isOk()
					.expectBodyList(Integer.class)
					.returnResult();
		
		Assertions.assertEquals(expectedIntegerList, entityExchangeResult.getResponseBody());
	}
	
	@Test
	public void fluxApproach4() {
		
		List<Integer> expectedIntegerList = Arrays.asList(1, 2 , 3, 4);
		
		webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Integer.class)
				.consumeWith(response -> { // Se usa para obtener el resultado y usar el assert dentro del consumer. Consumer es una intefaz funcional que recibe un parametro y hace algo con el, no devuelve nada.
					Assertions.assertEquals(expectedIntegerList, response.getResponseBody());
				});
	}
	
	@Test
	public void fluxStream() {
		
		Flux<Long> longStreamFlux = webTestClient.get().uri("/fluxstream")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange() 
				.expectStatus().isOk()
				.returnResult(Long.class)
				.getResponseBody();
	
		StepVerifier.create(longStreamFlux)
				.expectSubscription()
				.expectNext(0l, 1l, 2l, 3l)
				.thenCancel() // --> Llegado a este punto cancela la subscripcion
				.verify();
	}
	
	@Test
	public void monoTest() {
		
		int expectedValue = 1;
		
		webTestClient.get().uri("/mono")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Integer.class)
				.consumeWith( response -> Assertions.assertEquals(expectedValue, response.getResponseBody()));
		
	}
	

}
