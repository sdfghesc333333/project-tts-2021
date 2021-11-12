package com.app.tts.server.handler.User;

import com.app.tts.services.UserServices;
import com.app.tts.util.AppParams;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GetAllUserHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = Logger.getLogger(GetAllUserHandler.class.getName());
    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.vertx().executeBlocking(future -> {
            try{
                // 2 dong sau co nghia gi ?
                routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
                routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
                List<Map> resultMap = UserServices.getAllUser();
                LOGGER.info("result get all ------: " + resultMap);
                JsonObject data = new JsonObject();
                data.put(AppParams.RESPONSE_DATA, resultMap);
                routingContext.response().end(Json.encodePrettily(data));
                future.complete();
            }catch (Exception e){
                routingContext.fail(e);
            }
        }, asyncResult -> {
            if(asyncResult.succeeded()){
                routingContext.next();
            }else {
                routingContext.fail(asyncResult.cause());
            }
        });
    }
}
