package com.app.tts.services;

import java.sql.Date;
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
import com.app.tts.util.mapper.UserMapper;
import com.mchange.v2.cfg.DelayedLogItem.Level;

import io.netty.handler.codec.http.HttpResponseStatus;
import oracle.jdbc.OracleTypes;

public class UserService{
	private static DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static final String FIND_ALL_USER = "{call PKG_TTS_TRUONG.findAllUser(?,?,?)}";
	public static final String CREATE_USER = "{call PKG_TTS_TRUONG.create_user(?,?,?,?,?,?,?)}";
	public static final String FIND_USER_BY_ID = "{call PKG_TTS_TRUONG.findUserById(?,?,?,?)}";
	
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
			System.out.println("service :" + HttpResponseStatus.OK.code());
			throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
		}

		List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);

		LOGGER.info(Level.INFO + "=> All Users result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));
		List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}

		return result;
	}
	
	public static void insert(String id, String email, Date create, Date update, String avatar, String password) throws SQLException {
		Map inputParams = new LinkedHashMap<Integer, String>();
		inputParams.put(1, id);
		inputParams.put(2, email);
		inputParams.put(3, create);
		inputParams.put(4, update);
		inputParams.put(5, avatar);
		inputParams.put(6, password);
		
		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(7, OracleTypes.NUMBER);
		outputParamsTypes.put(8, OracleTypes.VARCHAR);
		outputParamsTypes.put(9, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(7, AppParams.RESULT_CODE);
		outputParamsNames.put(8, AppParams.RESULT_MSG);
		outputParamsNames.put(9, AppParams.RESULT_DATA);

		Map searchResultMap = DBProcedureUtil.execute(dataSource, CREATE_USER, inputParams, outputParamsTypes,
				outputParamsNames);

		int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
		if (resultCode != HttpResponseStatus.OK.code()) {
			System.out.println("service :" + HttpResponseStatus.OK.code());
			throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
		}

		List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);

		LOGGER.info(Level.INFO + "=> Users result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));
		List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}
	}

	public static void findUserByEmail(String email) throws SQLException {
		Map inputParams = new LinkedHashMap<Integer, String>();
		inputParams.put(1, email);
		
		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(2, OracleTypes.NUMBER);
		outputParamsTypes.put(3, OracleTypes.VARCHAR);
		outputParamsTypes.put(4, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(2, AppParams.RESULT_CODE);
		outputParamsNames.put(3, AppParams.RESULT_MSG);
		outputParamsNames.put(4, AppParams.RESULT_DATA);

		Map searchResultMap = DBProcedureUtil.execute(dataSource, CREATE_USER, inputParams, outputParamsTypes,
				outputParamsNames);

		int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
		if (resultCode != HttpResponseStatus.OK.code()) {
			System.out.println("service :" + HttpResponseStatus.OK.code());
			throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
		}

		List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);

		LOGGER.info(Level.INFO + "=> Users result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));
		List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}
	}
	
	public static List<Map> findUserById(String id) throws SQLException {

		Map inputParams = new LinkedHashMap<Integer, String>();
		inputParams.put(1, id);
		
		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(2, OracleTypes.NUMBER);
		outputParamsTypes.put(3, OracleTypes.VARCHAR);
		outputParamsTypes.put(4, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(2, AppParams.RESULT_CODE);
		outputParamsNames.put(3, AppParams.RESULT_MSG);
		outputParamsNames.put(4, AppParams.RESULT_DATA);

		Map searchResultMap = DBProcedureUtil.execute(dataSource, FIND_USER_BY_ID, inputParams, outputParamsTypes,
				outputParamsNames);

		int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
		if (resultCode != HttpResponseStatus.OK.code()) {
			System.out.println("service :" + HttpResponseStatus.OK.code());
			throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
		}

		List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);

		LOGGER.info(Level.INFO + "=> Users result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));
		List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}

		return result;
	}
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
}
