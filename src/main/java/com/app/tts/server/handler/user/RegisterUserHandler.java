package com.app.tts.server.handler.user;

import com.app.tts.encode.Md5Code;

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

public class RegisterUserHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext rc) {
        rc.vertx().executeBlocking(future -> {
            try {
                JsonObject jsonRequest = rc.getBodyAsJson();
                String email = jsonRequest.getString(AppParams.EMAIL);
                String password = jsonRequest.getString(AppParams.PASSWORD);
                String confirmPassword = jsonRequest.getString("confirmPassword");
                String username = jsonRequest.getString(AppParams.USERNAME);
                String address = jsonRequest.getString(AppParams.ADDRESS);
                String phone = jsonRequest.getString(AppParams.PHONE);


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
                if (!password.equals(confirmPassword)) {

                    data.put("message", "đăng ký thất bại! , 2 mật khẩu không trùng nhau");
                } else if (18 <= password.length() && password.length() <= 6) {

                    data.put("message", "đăng ký thất bại! ,  mật khẩu phải từ 6 đến 18 kí tự");
                } else if (phone.length() != 10) {
                    data.put("message", "đăng ký thất bại! ,  yêu cầu nhập đủ 10 số điện thoại");
                } else if (!isValid(email)) {
                    data.put("message", "đăng ký thất bại! , email không hợp lệ");
                } else if (duplicate) {
                    data.put("message", "đăng ký thất bại! , đã có người sử dụng email này");
                } else if (!duplicate && isValid(email)) {
                    // Đăng ký thành công
                    List<Map> userJson = UserService.insertUser(email, Md5Code.md5(password), username, address, phone, "active");
                    data.put("đăng ký thành công: ", userJson);
                    rc.put(AppParams.RESPONSE_CODE, HttpResponseStatus.CREATED.code());
                    rc.put(AppParams.RESPONSE_MSG, HttpResponseStatus.CREATED.reasonPhrase());
                } else {
                    data.put("message", "đăng ký thất bại");
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

    private static final Logger LOGGER = Logger.getLogger(RegisterUserHandler.class.getName());
}
