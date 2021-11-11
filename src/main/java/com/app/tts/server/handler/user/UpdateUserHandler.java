package com.app.tts.server.handler.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.app.tts.services.UserService;
import com.app.tts.session.redis.SessionStore;
import com.app.tts.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;

public class UpdateUserHandler implements Handler<RoutingContext>, SessionStore {@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {			
				HttpServerResponse response = routingContext.response();
				JsonObject jsonRequest = routingContext.getBodyAsJson();
				String email = jsonRequest.getString(AppParams.EMAIL);
				String avatar = jsonRequest.getString(AppParams.AVATAR);
				String password = jsonRequest.getString(AppParams.PASSWORD);
				
				Map data = new HashMap();
				List<Map> oldUser = UserService.findUserByEmail(email);
				data.put("old user", oldUser);
				
				if(email != null) {
					if(oldUser.isEmpty()) {
						response.end("ERROR: user not found!");
					}else {
						UserService.update(email, avatar, password);
						routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
						routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());							
						data.put("new user", UserService.findUserByEmail(email));
		                response.end(Json.encodePrettily(data));
					}
				}else {
					response.end(Json.encode("ERROR: email already exist!"));
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

	private static final Logger LOGGER = Logger.getLogger(UpdateUserHandler.class.getName());

}