package com.app.tts.services;

import com.app.tts.util.AppParams;
import com.app.tts.util.ParamUtil;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UserServices extends MasterService{
    public static final String INSERT_USER = "{call PKG_PHUONG.INSERT_USER(?,?,?,?,?)}";
    private static final String SEARCH_USER_BY_EMAIL = "{call PKG_PHUONG.SEARCH_USER_BY_EMAIL(?,?,?,?)}";
    private static final String  UPDATE_USER = "{call PKG_PHUONG.UPDATE_USER(?,?,?,?,?)}";
    private static final String  DELETE_USER = "{call PKG_PHUONG.DELETE_USER(?,?,?,?)}";
    private static final String GET_ALL_USER = "{call PKG_PHUONG.GET_ALL_USER(?,?,?)}";

    public static List<Map> getAllUser() throws SQLException{
        List<Map> resultMap = searchAll(GET_ALL_USER, new Object[]{});
        return resultMap;
    }

    public static List<Map> deleteUser(String email) throws SQLException{
//        Map inputParams = new LinkedHashMap<Integer, String>();
//        inputParams.put(1, email);
//
//        Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
//        outputParamsTypes.put(2, OracleTypes.NUMBER);
//        outputParamsTypes.put(3, OracleTypes.VARCHAR);
//
//        Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
//        outputParamsNames.put(2, AppParams.RESULT_CODE);
//        outputParamsNames.put(3, AppParams.RESULT_MSG);
//
//        Map searchResultMap = DBProcedureUtil.execute(dataSource, DELETE_USER,
//                inputParams, outputParamsTypes, outputParamsNames);
//
//        int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
//
//        if(resultCode != HttpResponseStatus.OK.code()){
//            throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
//        }
        List<Map> resultDataList = excuteQuery(DELETE_USER, new Object[]{email});
        LOGGER.info("deleteUser------------" + resultDataList);

//        LOGGER.info("delete user result: " + ParamUtil.getString(resultDataList, AppParams.RESULT_MSG));

        return resultDataList;
    }

    public static List<Map> updateUser(String email, String password) throws SQLException{
//        Map inputParams = new LinkedHashMap<Integer, String>();
//        inputParams.put(1, email);
//        inputParams.put(2, password);
//
//        Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
//        outputParamsTypes.put(3, OracleTypes.NUMBER);
//        outputParamsTypes.put(4, OracleTypes.VARCHAR);
//
//        Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
//        outputParamsNames.put(3, AppParams.RESULT_CODE);
//        outputParamsNames.put(4, AppParams.RESULT_MSG);
//
//        Map searchResultMap = DBProcedureUtil.execute(dataSource, UPDATE_USER,
//                inputParams, outputParamsTypes, outputParamsNames);
//
//        int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
//
//        if(resultCode != HttpResponseStatus.OK.code()){
//            throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
//        }
//
//        LOGGER.info("update user result: " + ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
        List<Map> updateResultMap = update(UPDATE_USER, new Object[]{email, password});

        LOGGER.info("updateResultMap------" + updateResultMap);
        return updateResultMap;
    }

    public static Map searchUserByEmail(String email) throws SQLException{
//        Map inputParams = new LinkedHashMap<Integer, String>();
//        inputParams.put(1, email);
//
//        Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
//        outputParamsTypes.put(2, OracleTypes.NUMBER);
//        outputParamsTypes.put(3, OracleTypes.VARCHAR);
//        outputParamsTypes.put(4, OracleTypes.CURSOR);
//
//        Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
//        outputParamsNames.put(2, AppParams.RESULT_CODE);
//        outputParamsNames.put(3, AppParams.RESULT_MSG);
//        outputParamsNames.put(4, AppParams.RESULT_DATA);
//
//        Map searchResultMap = DBProcedureUtil.execute(dataSource, SEARCH_USER_BY_EMAIL,
//                inputParams, outputParamsTypes, outputParamsNames);
//
//        int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
//
//        if(resultCode != HttpResponseStatus.OK.code()){
//            throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
//        }
//
//        Map resultMap = new HashMap();
//
//        List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);
//
//        if(!resultDataList.isEmpty()){
//            resultMap = format(resultDataList.get(0));
//        }
//
//        LOGGER.info("=> Get user result2: " + resultMap);
        Map searchResultMap = searchOne(SEARCH_USER_BY_EMAIL, new Object[]{email});
        return  searchResultMap;
    }

    public static Map insertUser(String email, String password) throws SQLException{
//        Map inputParams = new LinkedHashMap<Integer, String>();
//        inputParams.put(1, email);
//        inputParams.put(2, password);
//
//        Map<Integer, Integer> outputParamsTypes = new LinkedHashMap<>();
//        outputParamsTypes.put(3, OracleTypes.NUMBER);
//        outputParamsTypes.put(4, OracleTypes.VARCHAR);
//        outputParamsTypes.put(5, OracleTypes.CURSOR);
//
//        Map<Integer, String> outputParamsNames = new LinkedHashMap<>();
//        outputParamsNames.put(6, AppParams.RESULT_CODE);
//        outputParamsNames.put(7, AppParams.RESULT_MSG);
//        outputParamsNames.put(8, AppParams.RESULT_DATA);
//
//        Map searchResultMap = DBProcedureUtil.execute(dataSource, INSERT_USER, inputParams,
//                outputParamsTypes, outputParamsNames);
//
//        int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);
//
//        if(resultCode != HttpResponseStatus.CREATED.code()){
//            throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
//        }
//
//        Map resultMap = new HashMap();
//        List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);
//
//        if(!resultDataList.isEmpty()){
//            resultMap = format(resultDataList.get(0));
//        }
//        LOGGER.info("=> insert user result: " + ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA));

        System.out.println("INPUT ----------" + email + "-------" + password);
        Map resultMap = insert(INSERT_USER, new Object[]{email, password});

        return resultMap;
    }

    private static Map format(Map queryData) throws SQLException {

        Map resultMap = new LinkedHashMap<>();
        resultMap.put(AppParams.ID, ParamUtil.getString(queryData, AppParams.S_ID));
        resultMap.put(AppParams.CREATE, ParamUtil.getString(queryData, AppParams.D_CREATE));
        resultMap.put(AppParams.UPDATE, ParamUtil.getString(queryData, AppParams.D_UPDATE));
        resultMap.put(AppParams.AVATAR, ParamUtil.getString(queryData, AppParams.S_AVATAR));
        resultMap.put(AppParams.EMAIL, ParamUtil.getString(queryData, AppParams.S_EMAIL));
        resultMap.put(AppParams.PASSWORD, ParamUtil.getString(queryData, AppParams.S_PASSWORD));
        return resultMap;
    }

    private static final Logger LOGGER = Logger.getLogger(UserServices.class.getName());

}
