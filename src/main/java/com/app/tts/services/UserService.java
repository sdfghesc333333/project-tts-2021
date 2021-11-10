package com.app.tts.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.app.tts.error.exception.OracleException;
//import com.app.tts.error.exception.OracleException;
import com.app.tts.util.AppParams;
import com.app.tts.util.DBProcedureUtil;
import com.app.tts.util.ParamUtil;

import io.netty.handler.codec.http.HttpResponseStatus;
import oracle.jdbc.OracleTypes;

public class UserService{
	private static DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static final String FIND_ALL_USER = "{call PKG_TTS_TRUONG.findAllUser(?,?,?)}";

	public static List<Map> findAllUser() throws SQLException {

		Map inputParams = new LinkedHashMap<Integer, String>();
		
		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(1, OracleTypes.NUMBER);
		outputParamsTypes.put(2, OracleTypes.VARCHAR);
		outputParamsTypes.put(3, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(1, AppParams.RESULT_CODE);
		outputParamsNames.put(2, AppParams.RESULT_MSG);
		outputParamsNames.put(3, AppParams.RESULT_DATA);

		Map searchResultMap = DBProcedureUtil.execute(dataSource, FIND_ALL_USER, inputParams, outputParamsTypes,
				outputParamsNames);

		int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);

		if (resultCode != HttpResponseStatus.OK.code()) {
			throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
		}

		Map resultMap = new HashMap<>();
		List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);

//		if (!resultDataList.isEmpty()) {
//			resultMap = format(resultDataList.get(0));
//		}

		LOGGER.info("=> All Users result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));

		return resultDataList;
	}

	private static Map format(Map queryData) {

		Map resultMap = new LinkedHashMap<>();
		Map printTable = new LinkedHashMap<>();
		Map image = new LinkedHashMap<>();
		resultMap.put(AppParams.ID, ParamUtil.getString(queryData, AppParams.S_ID));
		resultMap.put(AppParams.EMAIL, ParamUtil.getString(queryData, AppParams.S_EMAIL));
		resultMap.put(AppParams.CREATE, ParamUtil.getString(queryData, AppParams.D_CREATE));
		resultMap.put(AppParams.UPDATE, ParamUtil.getString(queryData, AppParams.D_UPDATE));
		resultMap.put(AppParams.AVATAR, ParamUtil.getString(queryData, AppParams.S_AVATAR));
		resultMap.put(AppParams.PASSWORD, ParamUtil.getString(queryData, AppParams.S_PASSWORD));
		return resultMap;
	}



	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
}
