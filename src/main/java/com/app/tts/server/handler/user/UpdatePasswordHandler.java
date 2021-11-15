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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;

public class UpdatePasswordHandler implements Handler<RoutingContext>, SessionStore {@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {			
				HttpServerResponse response = routingContext.response();
				JsonObject jsonRequest = routingContext.getBodyAsJson();
				String c_email = jsonRequest.getString(AppParams.EMAIL);
				String o_password = jsonRequest.getString(AppParams.PASSWORD);
				String n_password = jsonRequest.getString("new password");
				String c_password = jsonRequest.getString("confirm password");
				String c_avatar = null;
				
				Map data = new HashMap();
				List<Map> users = UserService.findUserByEmail(c_email);
				/*for (Map m : users) {
					data.put()
				}*/
				
				List<Map> oldUser = UserService.findUserByEmail(c_email);
				
				
				/*Map data = new HashMap();
				List<Map> oldUser = UserService.findUserByEmail(c_email);
				data.put("old user", oldUser);
				
				if(email != null) {
					if(oldUser.isEmpty()) {
						response.end("ERROR: user not found!");
					}else {
						UserService.update(c_email, c_avatar, Md5Code.md5(n_password));
						routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
						routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());							
						data.put("new user", UserService.findUserByEmail(c_email));
		                response.end(Json.encodePrettily(data));
					}
				}else {
					response.end(Json.encode("ERROR: email already exist!"));
				}	*/			
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

	private static final Logger LOGGER = Logger.getLogger(UpdatePasswordHandler.class.getName());

}