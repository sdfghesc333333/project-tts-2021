package com.app.tts.server.handler.User;

import com.app.tts.services.UserService;
import com.app.tts.util.AppParams;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UpdateUserHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext rc) {
        rc.vertx().executeBlocking(future -> {
            try {
                JsonObject jsonRequest = rc.getBodyAsJson();
                String email = jsonRequest.getString(AppParams.EMAIL);
                String username = jsonRequest.getString(AppParams.USERNAME);
                String address = jsonRequest.getString(AppParams.ADDRESS);
                String phone = jsonRequest.getString(AppParams.PHONE);
                String state = jsonRequest.getString(AppParams.STATE);


                Map data = new HashMap();

                rc.put(AppParams.RESPONSE_CODE, HttpResponseStatus.BAD_REQUEST.code());
                rc.put(AppParams.RESPONSE_MSG, HttpResponseStatus.BAD_REQUEST.reasonPhrase());
//                data.put("email", email);

                LOGGER.info("---email = " + email);
                List<Map> user = UserService.getUserByEmail(email);

                boolean duplicate = false;
                if (!user.isEmpty()) {
                    duplicate = true;
                }
               if (username.isEmpty()) {
                    duplicate = true;
                    data.put("message", "sửa thất bại! ,  chưa nhập username");
                } else if (!isValid(email)) {
                    data.put("message", "sửa thất bại! , email không hợp lệ");
                } else if (!duplicate) {
                    data.put("message", "sửa thất bại! , không tìm được email này" + email);
                } else if (duplicate && isValid(email)) {

                    List<Map> userJson = UserService.updateUser(email,username,address, phone, state);
                    data.put("sửa thành công: ", userJson);
                   rc.put(AppParams.RESPONSE_CODE, HttpResponseStatus.BAD_REQUEST.code());
                   rc.put(AppParams.RESPONSE_MSG, HttpResponseStatus.BAD_REQUEST.reasonPhrase());
                } else {
                    data.put("message", "sửa thất bại");
                }
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

    public static boolean isValid(String email) {
        boolean valid = false;
        valid = EmailValidator.getInstance().isValid(email);
        return true;
    }
    private static final Logger LOGGER = Logger.getLogger(UpdateUserHandler.class.getName());

}
