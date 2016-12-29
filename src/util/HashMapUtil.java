package com.cbscap.util;

import java.util.HashMap;
import java.util.Iterator;

import com.cbscap.fw.dbutil.dbmanager.*;

//import org.apache.log4j.*;

/**
 * <p>����       : setMapToCondition()=>�������Ŀ� ',' ���̱�<p>
 */

/*
 * <p>Ŭ������   : HashMapUtil</p>
 * <p>Ư�̻���   : ���� ��ƿ Ŭ����</p>
 */
public class HashMapUtil {
    
    /**
     * <p>�޼ҵ��	: setMapToCondition</p>
     * <p>����		: HashMap�� �޾Ƽ� SQL �������� Replace </p>
     * <p>�޼ҵ��μ�1	: HashMap condMap - SMDO������ ������ �� �ؽ���</p>
     * <p>�޼ҵ��μ�2	: ISqlMeta data   - SQL ������ �� ������Ʈ</p>
     * <p>�޼ҵ��μ�3	: HashMap keyMap  - condMap���� ������ Key �ؽ���</p>
     * <p>�޼ҵ帮�ϰ�	: ISqlMeta result - �������� ó���� ��</p>
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
