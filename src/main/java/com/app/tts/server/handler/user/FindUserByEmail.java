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
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;

public class FindUserByEmail implements Handler<RoutingContext>, SessionStore {@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {								
				HttpServerRequest httpServerRequest = routingContext.request();
				String userEmail = httpServerRequest.getParam("email");
				LOGGER.info("---userEmail = "+ userEmail);
				Map data = new HashMap();
				LOGGER.info("h26");
				List<Map> users = UserService.findUserByEmail(userEmail);
				LOGGER.info("h28");
				data.put("list user", users);
				LOGGER.info("Users result: " + users);
				
				routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
				routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
				routingContext.put(AppParams.RESPONSE_DATA, data);
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

	private static final Logger LOGGER = Logger.getLogger(FindUserByEmail.class.getName());

}