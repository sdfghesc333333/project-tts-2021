package com.app.tts.server.handler.user;

import java.util.HashMap;
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

public class RegisterHandler implements Handler<RoutingContext>, SessionStore {


	@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {						
				HttpServerResponse response = (HttpServerResponse) routingContext.response();
				JsonObject jsonRequest = routingContext.getBodyAsJson();
				String email = jsonRequest.getString(AppParams.EMAIL);
				String avatar = jsonRequest.getString(AppParams.AVATAR);
				String f_password = jsonRequest.getString(AppParams.PASSWORD);
				String c_password = jsonRequest.getString("confirm password");
				
				if(f_password == null || f_password.equals("")) {
					response.end("ERROR: password not found!");
				}else if(email.equals("") || email == null) {
					response.end("ERROR: Email not found!");
				}else if(c_password.equals("") || c_password == null) {
					response.end(Json.encode("ERROR: Confirm password not found!"));
				}else
					if(f_password.equals(c_password)) {
						System.out.println(f_password);
						System.out.println(c_password);
						Map data = new HashMap();
						List<Map> users = UserService.findUserByEmail(email);
						data.put("list user", users);
						
						if(users.isEmpty()) {
							UserService.create(email, avatar, Md5Code.md5(f_password));
							routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.CREATED.code());
							routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.CREATED.reasonPhrase());
							//routingContext.put(AppParams.RESPONSE_DATA, data);
							response.end(Json.encode(HttpResponseStatus.CREATED.reasonPhrase()));
						}else {
							response.end(Json.encode("ERROR: Account already exist!"));
						}
					}else {
						response.end("ERROR: Password does not match!");
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

    private static final Logger LOGGER = Logger.getLogger(RegisterHandler.class.getName());
}