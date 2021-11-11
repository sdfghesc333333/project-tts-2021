package com.app.tts.server.handler.base;

import com.app.tts.services.ListBaseGroupColorSizeService_Test;
import com.app.tts.util.AppParams;
import com.app.tts.util.ParamUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.sql.SQLException;
import java.util.*;

public class ListBaseGroupColorSizeHandler_Test implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.vertx().executeBlocking(future -> {
            try {
                Map mapBase = getListBGCSFromDB();
                routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
                routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
                routingContext.put(AppParams.RESPONSE_DATA, mapBase);
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

    public static Map getListBGCSFromDB() throws SQLException{
        Map listBaseDB = new HashMap();
        Map mapInput = ListBaseGroupColorSizeService_Test.getListBaseGroupColorSize();

        List<Map> listBase = ParamUtil.getListData(mapInput, AppParams.RESULT_DATA);
        List<Map> listColor = ParamUtil.getListData(mapInput, AppParams.RESULT_DATA_2);
        List<Map> listSize = ParamUtil.getListData(mapInput, AppParams.RESULT_DATA_3);

        Set<String> listGroupId = new HashSet<>();
//        Set<String> listBaseId = new HashSet<>();

        for(Map base : listBase){
            String groupId = ParamUtil.getString(base, AppParams.GROUP_ID);
            listGroupId.add(groupId);

//            String baseId = ParamUtil.getString(base, AppParams.BASE_ID);
//            listBaseId.add(baseId);
        }

        for(String group_id : listGroupId){
            List<Map> listMapBase = new ArrayList<>();
            String groupName = "";
            for(Map base : listBase){

                String baseId = ParamUtil.getString(base, AppParams.BASE_ID);
                //ghep theo color
                List<Map> listColorBase = new ArrayList<>();
                for(Map color: listColor){
                    String baseColorId = ParamUtil.getString(color, AppParams.BASE_ID);
                    if(baseId.equals(baseColorId)){
                        listColorBase.add(color);
                    }
                }
                base.put(AppParams.COLORS, listColorBase);

                //ghep theo size
                List<Map> listSizeBase = new ArrayList<>();
                for(Map size: listSize){
                    String baseSizeId = ParamUtil.getString(size, AppParams.BASE_ID);
                    if(baseId.equals(baseSizeId)){
                        listSizeBase.add(size);
                    }
                }
                base.put(AppParams.SIZES, listSizeBase);

                //gop theo group
                String baseGroupId = ParamUtil.getString(base, AppParams.GROUP_ID);
                if(group_id.equals(baseGroupId)){
                    listMapBase.add(base);
                    groupName = ParamUtil.getString(base, AppParams.GROUP_NAME);
                }
            }
            listBaseDB.put(groupName, listMapBase);
        }
        return listBaseDB;
    }
}
