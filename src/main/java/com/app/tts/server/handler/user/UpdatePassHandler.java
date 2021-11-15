package com.app.tts.server.handler.User;

import com.app.tts.services.UserService;
import com.app.tts.util.AppParams;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UpdatePassHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext rc) {
        rc.vertx().executeBlocking(future -> {
            try {
                JsonObject jsonRequest = rc.getBodyAsJson();
                String email = jsonRequest.getString(AppParams.EMAIL);
                String passwordold = jsonRequest.getString("passwordold");
                String password = jsonRequest.getString(AppParams.PASSWORD);
                String confirmPassword = jsonRequest.getString("confirmPassword");


                Map data = new HashMap();

                rc.put(AppParams.RESPONSE_CODE, HttpResponseStatus.BAD_REQUEST.code());
                rc.put(AppParams.RESPONSE_MSG, HttpResponseStatus.BAD_REQUEST.reasonPhrase());
//                data.put("email", email);
                List<Map> user = UserService.getPassByEmail(email);

                boolean duplicate = false;
                if (!user.isEmpty()) {
                    duplicate = true;
                }
                if (!password.equals(confirmPassword)) {
                    data.put("message", "đăng ký thất bại! , 2 mật khẩu không trùng nhau");
                } else if (18 <= password.length() && password.length() <= 6) {
                    data.put("message", "đăng ký thất bại! , mật khẩu từ 6 đến 18 kí tự ");
                } else if (!user.equals(passwordold)) {
                    data.put("message", "đăng ký thất bại! , mật khẩu cũ không đúng");
                } else if (!duplicate) {
                    List<Map> userJson = UserService.updatePass(email, password);
                    data.put("đổi mật khẩu thành công: ", userJson);
                    rc.put(AppParams.RESPONSE_CODE, HttpResponseStatus.BAD_REQUEST.code());
                    rc.put(AppParams.RESPONSE_MSG, HttpResponseStatus.BAD_REQUEST.reasonPhrase());
                } else {
                    data.put("message", " thất bại");
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

    private static final Logger LOGGER = Logger.getLogger(UpdatePassHandler.class.getName());
}
