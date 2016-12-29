package com.cbscap.util;

import java.util.HashMap;
import java.util.Iterator;

import com.cbscap.fw.dbutil.dbmanager.*;

//import org.apache.log4j.*;

/**
 * <p>내용       : setMapToCondition()=>숫자형식에 ',' 붙이기<p>
 */

/*
 * <p>클래스명   : HashMapUtil</p>
 * <p>특이사항   : 공통 유틸 클래스</p>
 */
public class HashMapUtil {
    
    /**
     * <p>메소드명	: setMapToCondition</p>
     * <p>설명		: HashMap을 받아서 SQL 조건절을 Replace </p>
     * <p>메소드인수1	: HashMap condMap - SMDO에서온 조건이 들어간 해쉬맵</p>
     * <p>메소드인수2	: ISqlMeta data   - SQL 문장이 들어간 오브젝트</p>
     * <p>메소드인수3	: HashMap keyMap  - condMap에서 가져올 Key 해쉬맵</p>
     * <p>메소드리턴값	: ISqlMeta result - 공백제거 처리된 값</p>
     */
    public static void setMapToCondition(HashMap condMap, ISqlMeta data, HashMap keyMap){
    	Iterator keyIterator = condMap.keySet().iterator();
    	
    	String condKey      = null;
    	String condValue    = null;
    	SqlMapper keyValue  = null;
	
    	while(keyIterator.hasNext()){
    		condKey   = String.valueOf(keyIterator.next());
    		condValue = String.valueOf(condMap.get(condKey));
	
    		if(keyMap.containsKey(condKey)){
    			keyValue = (SqlMapper)keyMap.get(condKey);
        		if( keyValue.isString() ){
        			data.setParam(keyValue.getColName(), "'" + condValue + "'");
        		} else {
        			data.setParam(keyValue.getColName(), condValue);
        		}
    		}
    	}
    }      

}
