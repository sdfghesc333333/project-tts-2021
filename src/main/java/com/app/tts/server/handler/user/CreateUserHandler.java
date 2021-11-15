package com.app.tts.server.handler.user;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.app.tts.encode.Md5Code;
import com.app.tts.services.UserService;
import com.app.tts.session.redis.SessionStore;
import com.app.tts.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;

public class CreateUserHandler implements Handler<RoutingContext>, SessionStore {
	@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {						
				HttpServerResponse response = routingContext.response();
				JsonObject jsonRequest = routingContext.getBodyAsJson();
				String email = jsonRequest.getString(AppParams.EMAIL);
				String avatar = jsonRequest.getString(AppParams.AVATAR);
				String password = jsonRequest.getString(AppParams.PASSWORD);
				
				List<Map> users = UserService.findUserByEmail(email);
				
				if(email.equals("")) {
					System.out.println("null");
					response.end(Json.encode("ERROR: email null!"));
				}else {
					if(users.isEmpty()) {
						UserService.create(email, avatar, Md5Code.md5(password));
						routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.CREATED.code());
						routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.CREATED.reasonPhrase());
						//routingContext.put(AppParams.RESPONSE_DATA, data);
						response.end(Json.encode(HttpResponseStatus.CREATED.reasonPhrase()));
					}else {
						response.end(Json.encode("ERROR: email already exist!"));
					}
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

	private static final Logger LOGGER = Logger.getLogger(CreateUserHandler.class.getName());

}