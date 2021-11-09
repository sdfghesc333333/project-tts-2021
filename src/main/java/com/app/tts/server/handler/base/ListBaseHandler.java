package com.app.tts.server.handler.base;

import com.app.tts.data.type.RedisKeyEnum;
import com.app.tts.services.BaseService;
import com.app.tts.services.RedisService;
import com.app.tts.session.redis.SessionStore;
import com.app.tts.util.AppParams;
import com.app.tts.util.ParamUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ListBaseHandler implements Handler<RoutingContext>, SessionStore {
	@Override
	public void handle(RoutingContext routingContext) {

		routingContext.vertx().executeBlocking(future -> {
			try {

				Map listBaseRedis = RedisService.getMap(RedisKeyEnum.BASES_MAP);
				if (listBaseRedis == null || listBaseRedis.isEmpty()) {
					listBaseRedis = getListBaseFromDB();
				}

				routingContext.put(AppParams.RESPONSE_CODE, HttpResponseStatus.OK.code());
				routingContext.put(AppParams.RESPONSE_MSG, HttpResponseStatus.OK.reasonPhrase());
				routingContext.put(AppParams.RESPONSE_DATA, listBaseRedis);
				future.complete();
			} catch (Exception e) {
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

	public static Map getListBaseFromDB() throws SQLException {
		Map listBaseDB = new HashMap();
		List<Map> listBaseAndGroup = BaseService.getListBase();
		Set<String> listBaseGroupId = new HashSet();

		for (Map baseAndGroup : listBaseAndGroup) {
			//get base id
			String baseId = ParamUtil.getString(baseAndGroup, AppParams.ID);
			String baseGroupId = ParamUtil.getString(baseAndGroup, AppParams.GROUP_ID);
			String strColor = ParamUtil.getString(baseAndGroup, AppParams.COLORS);
			listBaseGroupId.add(baseGroupId);

			//get list size
//			List<Map> listSize = BaseSizeService.getSizeAndPrice(baseId);

			// get list colors
//			List<Map> list_colors = BaseColorService.getColor(strColor);

//			baseAndGroup.put("sizes", listSize);
//			baseAndGroup.put("colors", list_colors);
		}


		//list base group
		for (String groupId : listBaseGroupId) {
			List<Map> listBaseGroup = new ArrayList();
			String baseGroupName = "";
			for (Map baseAndGroup : listBaseAndGroup) {
				String baseGroupId = ParamUtil.getString(baseAndGroup, AppParams.GROUP_ID);
				if (groupId.equals(baseGroupId)) {
					listBaseGroup.add(baseAndGroup);
					baseGroupName = ParamUtil.getString(baseAndGroup, AppParams.GROUP_NAME);
				}
			}
			listBaseDB.put(baseGroupName, listBaseGroup);
		}

		RedisService.persistMap(RedisKeyEnum.BASES_MAP, listBaseDB);

		return listBaseDB;
	}

	private static final Logger LOGGER = Logger.getLogger(ListBaseHandler.class.getName());

}
