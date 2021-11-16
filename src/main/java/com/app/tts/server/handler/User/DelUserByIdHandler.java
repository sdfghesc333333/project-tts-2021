package com.app.tts.server.handler.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.app.tts.services.UserService;
import com.app.tts.util.AppParams;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;

public class DelUserByIdHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext rc) {
        rc.vertx().executeBlocking(future -> {
            try {
                HttpServerRequest httpServerRequest = rc.request();
                String userId = httpServerRequest.getParam("userId");
                LOGGER.info("---userId  = "+ userId);
                Map data = new HashMap();

                List<Map> user=  UserService.delUserById(userId);

                data.put("xóa thành công ID: " + userId +
                        " || "+ "ALL_user", user);

                rc.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
                rc.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
                rc.put(AppParams.RESPONSE_DATA, data);
                future.complete();
            } catch (Exception e) {
                rc.fail(e);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                rc.next();
            } else {
                rc.fail(asyncResult.cause());
            }
        });
    }
    private static final Logger LOGGER = Logger.getLogger(DelUserByIdHandler.class.getName());
}
