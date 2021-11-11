package com.app.tts.services;

import java.sql.SQLException;
import java.util.ArrayList;
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
	public static final String CREATE_USER = "{call PKG_TTS_TRUONG.create_user(?,?,?,?,?,?)}";
	public static final String FIND_USER_BY_ID = "{call PKG_TTS_TRUONG.findUserById(?,?,?,?)}";
	public static final String FIND_USER_BY_EMAIL = "{call PKG_TTS_TRUONG.findUserByEmail(?,?,?,?)}";
	public static final String UPDATE_USER = "{call PKG_TTS_TRUONG.update_user(?,?,?,?,?,?)}";
	public static final String DELETE_USER = "{call PKG_TTS_TRUONG.deleteUserByEmail(?,?,?,?)}";
	
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
	
	
	public static void insert(String email, String avatar, String password) throws SQLException {
		Map inputParams = new LinkedHashMap<Integer, String>();
		inputParams.put(1, email);
		inputParams.put(2, avatar);
		inputParams.put(3, password);
		
		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(4, OracleTypes.NUMBER);
		outputParamsTypes.put(5, OracleTypes.VARCHAR);
		outputParamsTypes.put(6, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(4, AppParams.RESULT_CODE);
		outputParamsNames.put(5, AppParams.RESULT_MSG);
		outputParamsNames.put(6, AppParams.RESULT_DATA);

		Map searchResultMap = DBProcedureUtil.execute(dataSource, CREATE_USER, inputParams, outputParamsTypes,
				outputParamsNames);

		int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
		if (resultCode != HttpResponseStatus.CREATED.code()) {
			System.out.println("service :" + HttpResponseStatus.CREATED.code());
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
	
	public static List<Map> findUserByEmail(String email) throws SQLException {

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

		Map searchResultMap = DBProcedureUtil.execute(dataSource, FIND_USER_BY_EMAIL, inputParams, outputParamsTypes,
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
	
	public static void update(String email, String avatar, String password) throws SQLException {
		Map inputParams = new LinkedHashMap<Integer, String>();
		inputParams.put(1, email);
		inputParams.put(2, avatar);
		inputParams.put(3, password);
		
		Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
		outputParamsTypes.put(4, OracleTypes.NUMBER);
		outputParamsTypes.put(5, OracleTypes.VARCHAR);
		outputParamsTypes.put(6, OracleTypes.CURSOR);

		Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
		outputParamsNames.put(4, AppParams.RESULT_CODE);
		outputParamsNames.put(5, AppParams.RESULT_MSG);
		outputParamsNames.put(6, AppParams.RESULT_DATA);

		Map searchResultMap = DBProcedureUtil.execute(dataSource, UPDATE_USER, inputParams, outputParamsTypes,
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
	
	public static void delete(String email) throws SQLException {
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

		Map searchResultMap = DBProcedureUtil.execute(dataSource, DELETE_USER, inputParams, outputParamsTypes,
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
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
}
