package com.app.tts.server.handler.User;

import com.app.tts.services.UserServices;
import com.app.tts.util.AppParams;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public class DeleteUserHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = Logger.getLogger(DeleteUserHandler.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.vertx().executeBlocking(future -> {
            try{
                JsonObject jsonRequest = routingContext.getBodyAsJson();
                String email = jsonRequest.getString(AppParams.S_EMAIL);

                JsonObject data = new JsonObject();

                routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.BAD_REQUEST.code());
                routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.BAD_REQUEST.reasonPhrase());
                data.put("email", email);

                LOGGER.info("---email = " + email);
                Map user = UserServices.searchUserByEmail(email);

                boolean duplicate = false;
                if(user != null){
                    duplicate = true;
                }
                if(duplicate){
                    deleteUser(email);
                    data.put("message", "delete successed");
                    routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
                    routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
                }else{
                    data.put("message", "delete failed");
                }
                routingContext.response().end(Json.encodePrettily(data));
                future.complete();
            }catch (Exception e){
                routingContext.fail(e);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()){
                routingContext.next();
            }else{
                routingContext.fail(asyncResult.cause());
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void deleteUser(String email) throws SQLException {
        UserServices.deleteUser(email);
    }
}
