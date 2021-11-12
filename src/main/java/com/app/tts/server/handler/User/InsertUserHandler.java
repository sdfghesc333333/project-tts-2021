package com.app.tts.server.handler.User;

import com.app.tts.services.UserServices;
import com.app.tts.util.AppParams;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

public class InsertUserHandler implements Handler<RoutingContext> {
//    @SuppressWarnings("unchecked")
    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.vertx().executeBlocking(future -> {
            try{
                JsonObject jsonRequest = routingContext.getBodyAsJson();
                String email = jsonRequest.getString(AppParams.S_EMAIL);
                String password = jsonRequest.getString(AppParams.S_PASSWORD);
                String confirmPassword = jsonRequest.getString("confirmPassword");

                JsonObject data = new JsonObject();

                routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.BAD_REQUEST.code());
                routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.BAD_REQUEST.reasonPhrase());
                data.put("email", email);

                LOGGER.info("--------email = " + email);
                Map user = UserServices.searchUserByEmail(email);
                LOGGER.info("test1-----------");

                boolean duplicate = false;
                if(user != null){
                    duplicate = true;
                }

                if(!password.equals(confirmPassword)){
                    LOGGER.info("testPassword");
                    duplicate = true;
                    data.put("message", "register failed, password and confirm password are not matched");
                } else if(duplicate){
                    data.put("message", "register failed, email is duplicated");
                }else if(!duplicate && isValid(email)){
                    LOGGER.info("test2---------------");
                    //dang ky thanh cong
                    Map resultInsert = insert(email, password);
//                    data.put("message", "register successed");
                    data.put(AppParams.RESPONSE_DATA, resultInsert);//c1
                    routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
                    routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
//                    routingContext.put(AppParams.RESPONSE_DATA, resultInsert);//c2
                }else{
                    data.put("message", "register failed");
                }
//                routingContext.put(AppParams.RESPONSE_DATA, data); // khong nhan Json
                routingContext.response().end(Json.encode(data));
                future.complete();
            }catch (Exception e){
                routingContext.fail(e);
            }
        }, asyncResult -> {
            if(asyncResult.succeeded()){
                routingContext.next();
            }else{
                routingContext.fail(asyncResult.cause());
            }
        });
    }

//    @SuppressWarnings("unchecked")
    public Map insert(String email, String password) throws SQLException{
        Map result = UserServices.insertUser(email, password);
        return result;
    }

    public static boolean isValid(String email){
        boolean valid = false;
        valid = EmailValidator.getInstance().isValid(email);
        return valid;
    }

    private static final Logger LOGGER = Logger.getLogger(InsertUserHandler.class.getName());
}
