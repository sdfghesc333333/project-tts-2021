package com.app.tts.server.handler.base;

import com.app.tts.services.ListBaseService;
import com.app.tts.util.AppParams;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.sql.SQLException;
import java.util.*;

public class ListBaseHandler2 implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.vertx().executeBlocking(future -> {
            try{
                Map mapBase = getListBaseFromDB();
                routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
                routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
                routingContext.put(AppParams.RESPONSE_DATA, mapBase);
                future.complete();
            }catch (Exception e){
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

    public static Map getListBaseFromDB() throws SQLException{
        List<Map> listBase = ListBaseService.getListBase();
        Map mapBase = new HashMap();
        for(int number = 0; number < listBase.size(); number++){
            mapBase.put(number, listBase.get(number));
        }
        return mapBase;
    }
}
