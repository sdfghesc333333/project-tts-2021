package com.app.tts.server.handler.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.app.tts.services.UserService;
import com.app.tts.session.redis.SessionStore;
import com.app.tts.util.AppParams;
import com.app.tts.util.ParamUtil;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;

public class AllUserHandler implements Handler<RoutingContext>, SessionStore {
	@Override
	public void handle(RoutingContext routingContext) {

		//HttpServerResponse response = (HttpServerResponse) routingContext.response();
		
		routingContext.vertx().executeBlocking(future -> {
			try {				
				HttpServerRequest httpServerRequest = routingContext.request();
				
				Map data = new HashMap();
				List<Map> listUserGroup = UserService.findAllUser();
				System.out.println("handler 30");
				data.put("listUserGroup", listUserGroup);
				
				//JsonArray listUsersJson = new JsonArray(listUserm);
				
				LOGGER.info("Users result: " + listUserGroup);
				routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
				routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
				routingContext.put(AppParams.RESPONSE_DATA, data);
				System.out.println("handler 38");
				future.complete();
			} catch (Exception e) {
				System.out.println("handler 41");
				routingContext.fail(e);
			}
		}, asyncResult -> {
			if (asyncResult.succeeded()) {
				System.out.println("handler 46");
				routingContext.next();
			} else {
				System.out.println("handler 49");
				routingContext.fail(asyncResult.cause());
			}
		});
	}
	
	/*public static Map getAllUserFromDB() throws SQLException {
		Map listUserDB = new HashMap();
		List<Map> listUser = UserService.findAllUser();
		Set<String> listUserId = new HashSet();

		for (Map userAndId : listUser) {
			//get base id
			String id = ParamUtil.getString(userAndId, AppParams.ID);
			String email = ParamUtil.getString(userAndId, AppParams.EMAIL);
			String avatar = ParamUtil.getString(userAndId, AppParams.AVATAR);
			String password = ParamUtil.getString(userAndId, AppParams.PASSWORD);
			listUserId.add(id);

			//get list size
//			List<Map> listSize = BaseSizeService.getSizeAndPrice(baseId);

			// get list colors
//			List<Map> list_colors = BaseColorService.getColor(strColor);

//			baseAndGroup.put("sizes", listSize);
//			baseAndGroup.put("colors", list_colors);
		}


		//list base group
		for (String groupId : listUserId) {
			List<Map> listBaseGroup = new ArrayList();
			String baseGroupName = "";
			for (Map baseAndGroup : listUser) {
				String baseGroupId = ParamUtil.getString(baseAndGroup, AppParams.GROUP_ID);
				if (groupId.equals(baseGroupId)) {
					listBaseGroup.add(baseAndGroup);
					baseGroupName = ParamUtil.getString(baseAndGroup, AppParams.GROUP_NAME);
				}
			}
			listUserDB.put(baseGroupName, listBaseGroup);
		}

		RedisService.persistMap(RedisKeyEnum.BASES_MAP, listUserDB);

		return listUserDB;
	}*/

	private static final Logger LOGGER = Logger.getLogger(AllUserHandler.class.getName());

}

