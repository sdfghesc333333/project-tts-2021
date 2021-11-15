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

public class UserService extends MasterService {

	public static final String FIND_ALL_USER = "{call PKG_TTS_TRUONG.findAllUser(?,?,?)}";
	public static final String CREATE_USER = "{call PKG_TTS_TRUONG.create_user(?,?,?,?,?,?)}";
	public static final String FIND_USER_BY_ID = "{call PKG_TTS_TRUONG.findUserById(?,?,?,?)}";
	public static final String FIND_USER_BY_EMAIL = "{call PKG_TTS_TRUONG.findUserByEmail(?,?,?,?)}";
	public static final String UPDATE_USER = "{call PKG_TTS_TRUONG.update_user(?,?,?,?,?,?)}";
	public static final String DELETE_USER = "{call PKG_TTS_TRUONG.deleteUserByEmail(?,?,?,?)}";
	
	public static List<Map> findAllUser() throws SQLException {
		List<Map> resultDataList = searchAll(FIND_ALL_USER, new Object[] {});
		List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}
		return result;
	}
	
	
	public static void create(String email, String avatar, String password) throws SQLException {
		/*Map inputParams = new LinkedHashMap<Integer, String>();
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

		LOGGER.info(Level.INFO + "=> Users result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));*/
		excuteQuery(CREATE_USER, new Object[] {email, avatar, password});

	}
	
	public static List<Map> findUserById(String id) throws SQLException {
		/*List<Map> resultDataList = excuteQuery(FIND_USER_BY_ID, new Object[] {id});
		List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}
		return resultDataList;*/
		return excuteQuery(FIND_USER_BY_ID, new Object[] {id});
	}
	
	public static List<Map> findUserByEmail(String email) throws SQLException {
		/*Map resultDataList = searchOne(FIND_USER_BY_EMAIL, new Object[]{email});
		List<Map> list = new ArrayList<Map>(resultDataList.values());
		List<Map> result = new ArrayList();
		for (Map b : list) {
			b = UserMapper.format(b);
			result.add(b);
		}
		return result;*/
		return excuteQuery(FIND_USER_BY_EMAIL, new Object[]{email});
	}
	
	public static void update(String email, String avatar, String password) throws SQLException {
		update(UPDATE_USER, new Object[] {email, avatar, password});
		/*List<Map> result = new ArrayList();
		for (Map b : resultDataList) {
			b = UserMapper.format(b);
			result.add(b);
		}*/
	}
	
	public static void delete(String email) throws SQLException {
		delete(DELETE_USER, new Object[] {email});
	}
	
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
}
