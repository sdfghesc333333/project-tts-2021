package com.app.tts.services;

import com.app.tts.error.exception.OracleException;
import com.app.tts.util.AppParams;
import com.app.tts.util.DBProcedureUtil;
import com.app.tts.util.ParamUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import oracle.jdbc.OracleTypes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.app.tts.services.ListBaseService.format;

public class ListBaseGroupColorSizeService extends MasterService{
    public static final String GET_LIST_DATA = "{call PKG_PHUONG.LIST_GROUP_BASE_COLOR_SIZE(?,?,?,?,?)}";

    public static Map getListBaseGroupColorSize() throws SQLException{
        Map inputData = new LinkedHashMap<Integer, String>();

        Map<Integer, Integer> outputParamTypes = new LinkedHashMap<>();
        outputParamTypes.put(1, OracleTypes.NUMBER);
        outputParamTypes.put(2, OracleTypes.VARCHAR);
        outputParamTypes.put(3, OracleTypes.CURSOR);
        outputParamTypes.put(4, OracleTypes.CURSOR);
        outputParamTypes.put(5, OracleTypes.CURSOR);

        Map<Integer, String> outputParamNames = new LinkedHashMap<>();
        outputParamNames.put(1, AppParams.RESULT_CODE);
        outputParamNames.put(2, AppParams.RESULT_MSG);
        outputParamNames.put(3, AppParams.RESULT_DATA);
        outputParamNames.put(4, AppParams.RESULT_DATA_2);
        outputParamNames.put(5, AppParams.RESULT_DATA_3);

        Map searchResultMap = DBProcedureUtil.execute(dataSource, GET_LIST_DATA,
                inputData, outputParamTypes, outputParamNames);

        int resultCode = ParamUtil.getInt(searchResultMap, AppParams.RESULT_CODE);

        if(resultCode != HttpResponseStatus.OK.code()){
            throw new OracleException(ParamUtil.getString(searchResultMap, AppParams.RESULT_MSG));
        }

        Map result = new LinkedHashMap();
        List<Map> resultDataList = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA);
        List<Map> resultDataList2 = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA_2);
        List<Map> resultDataList3 = ParamUtil.getListData(searchResultMap, AppParams.RESULT_DATA_3);

        result.put(AppParams.RESULT_DATA, formatList(resultDataList));
        result.put(AppParams.RESULT_DATA_2, resultDataList2);
        result.put(AppParams.RESULT_DATA_3, resultDataList3);

        return result;
    }

    public static List<Map> formatList(List<Map> inputList){
        List<Map> result = new ArrayList<>();
        for(Map map : inputList){
            map = format(map);
            result.add(map);
        }
        return result;
    }
}
