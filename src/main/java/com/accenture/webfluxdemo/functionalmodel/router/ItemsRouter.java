package com.accenture.webfluxdemo.functionalmodel.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.accenture.webfluxdemo.functionalmodel.handler.ItemsHandler;
import com.accenture.webfluxdemo.util.Constants;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemsRouter {

	@Bean
	public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler) {

		return RouterFunctions
				.route(GET(Constants.Endpoints.ITEM_FUNCTIONAL_ENDPOINT),itemsHandler::getAllItems)
				.andRoute(GET(Constants.Endpoints.ITEM_FUNCTIONAL_ENDPOINT + "/{id}"), itemsHandler::getOneItem)
				.andRoute(POST(Constants.Endpoints.ITEM_FUNCTIONAL_ENDPOINT)
						.and(accept(MediaType.APPLICATION_JSON)), itemsHandler::createOneItem)
				.andRoute(DELETE(Constants.Endpoints.ITEM_FUNCTIONAL_ENDPOINT + "/{id}"), itemsHandler::deleteOneItem)
				.andRoute(PUT(Constants.Endpoints.ITEM_FUNCTIONAL_ENDPOINT + "/{id}")
						.and(accept(MediaType.APPLICATION_JSON)), itemsHandler::updateOneItem);
		
	}
	
}
