package com.app.tts.server.handler.user;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.app.tts.services.UserService;
import com.app.tts.session.redis.SessionStore;
import com.app.tts.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;

public class DeleteUserByEmail implements Handler<RoutingContext>, SessionStore {@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {								
				HttpServerResponse response = routingContext.response();
				HttpServerRequest httpServerRequest = routingContext.request();
				String userEmail = httpServerRequest.getParam("email");			
				
				List<Map> users = UserService.findUserByEmail(userEmail);
				
				if(userEmail != null) {
					if(users.isEmpty()) {
						response.end(Json.encode("ERROR: email already exist!"));						
					}else {
						UserService.delete(userEmail);
						routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
						routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
						response.end(Json.encode(HttpResponseStatus.CREATED.reasonPhrase()));
					}
				}else {
					response.end(Json.encode("ERROR: email null!"));
				}
				future.complete();
			} catch (Exception e) {
				routingContext.fail(e);
			}
		}, asyncResult -> {
			if (asyncResult.succeeded()) {
				routingContext.next();
			} else {
				routingContext.fail(asyncResult.cause());
			}
		});
	}

	private static final Logger LOGGER = Logger.getLogger(DeleteUserByEmail.class.getName());

}