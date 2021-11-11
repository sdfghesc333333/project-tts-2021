package com.app.tts.util.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import com.app.tts.util.AppParams;
import com.app.tts.util.ParamUtil;

public class UserMapper {
	public static Map format(Map queryData) {

		Map resultMap = new LinkedHashMap<>();
		resultMap.put(AppParams.ID, ParamUtil.getString(queryData, AppParams.S_ID));
		resultMap.put(AppParams.EMAIL, ParamUtil.getString(queryData, AppParams.S_EMAIL));
		resultMap.put(AppParams.CREATE, ParamUtil.getString(queryData, AppParams.D_CREATE));
		resultMap.put(AppParams.UPDATE, ParamUtil.getString(queryData, AppParams.D_UPDATE));
		resultMap.put(AppParams.AVATAR, ParamUtil.getString(queryData, AppParams.S_AVATAR));
		resultMap.put(AppParams.PASSWORD, ParamUtil.getString(queryData, AppParams.S_PASSWORD));
		return resultMap;
	}
}
