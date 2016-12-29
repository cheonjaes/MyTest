package com.cbscap.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cbscap.fw.entity.WrcDatasetWrapper;
import com.cbscap.fw.entity.c.IDO;


/**
 * 이 클래스는 Converter 클래스와 관련된 util 메소드들을 구현하기 위하여
 * 작성된 Utility 클래스이다.
 *
 * 이 클래스가 정상적으로 동작하기 위해,
 * 모든 TDO는 Java Beans 규약에 따라 작성되어야 하며
 * TDO 내부의 모든 Field들은 String type으로 선언되어야 한다.
 *
 * @author   진성혁
 * @version  1.0.0, 2007-10-31
 * @since    2007-10-31
 */
public class ConverterUtils
{

	public static final  String EMPTY_STR_SYMBOL = "NULL";

    private static final Logger logger           = Logger.getLogger( ConverterUtils.class );


    /**
     * Compiler에 의해 불필요한 public 기초 생성자가 자동으로 생성되지 않도록
     * private 기초 생성자를 정의한다.
     * 이 생성자는 어디에서도 호출되지 않는다.
     */
    private ConverterUtils() {}




    /**
     * origTdo의 속성 값들을 destSdo에 복사한다.
     *
     * @param   origTdo                    원본 TDO instance
     * @param   destSdo                    복사 대상 SDO instance
     * @throws  NullPointerException       origTdo 또는 destSdo가 null일 경우
     * @throws  IntrospectionException     BeanInfo를 통한 Introspection에 실패하였을 경우
     * @throws  IllegalAccessException     getter 또는 setter 메소드에 접근 권한이 없을 경우
     * @throws  InvocationTargetException  getter 또는 setter 실행시 예외상황이 발생한 경우
     */
    public static void copyProperties(IDO origTdo, WrcDatasetWrapper destSdo) throws IntrospectionException
                                                                                   , IllegalAccessException
                                                                                   , InvocationTargetException
    {
        copyProperties(origTdo, destSdo, (String[])null);
    }
    /**
     * origTdo의 속성 값들을 destSdo에 복사한다.
     *
     * @param   origTdo                    원본 TDO instance
     * @param   destSdo                    복사 대상 SDO instance
     * @param   dateProps                  '-'을 제거하여야 할 날짜유형 속성명들의 배열
     * @throws  NullPointerException       origTdo 또는 destSdo가 null일 경우
     * @throws  IntrospectionException     BeanInfo를 통한 Introspection에 실패하였을 경우
     * @throws  IllegalAccessException     getter 또는 setter 메소드에 접근 권한이 없을 경우
     * @throws  InvocationTargetException  getter 또는 setter 실행시 예외상황이 발생한 경우
     */
    public static void copyProperties(IDO origTdo, WrcDatasetWrapper destSdo, String[] dateProps) throws IntrospectionException
                                                                                                       , IllegalAccessException
                                                                                                       , InvocationTargetException
    {
        if(origTdo == null || destSdo == null)
            throw new NullPointerException("원본 TDO 객체 또는 복사 대상 SDO의 instance가 null입니다.");

        PropertyDescriptor[] origDesc     = Introspector.getBeanInfo( origTdo.getClass() ).getPropertyDescriptors();
        List                 datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );

        for(int i=0; i<origDesc.length; i++) {
            String propName = origDesc[i].getName();
            if( "class".equals(propName) )
                continue;  // skip

            Method getter = origDesc[i].getReadMethod();
            if(getter == null)
                throw new IllegalAccessException("Property [" + propName + "]의 값을 읽을 수 없습니다.");

            String propValue = (String)getter.invoke(origTdo, null);
            if(datePropList.contains(propName) && stringValue(propValue).length() > 0)
                propValue = propValue.replaceAll("-", "");

            boolean rslt = destSdo.set(propName, propValue);
            if(!rslt)
                logger.warn("TDO ==> SDO 속성 복사 실패 : [name|value] = [" + propName + "|" + propValue + "]");
        }
    }


    /**
     * origTdoMap 각각의 Entry 정보를 destSdo에 복사한다.
     *
     * @param   origTdoMap                 원본 TDO instance
     * @param   destSdo                    복사 대상 SDO instance
     * @throws  NullPointerException       origTdoMap 또는 destSdo가 null일 경우
     * @throws  IllegalArgumentException   origTdoMap이 비어 있을 경우
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                     )
    {
        copyProperties(origTdoMap, destSdo, (String[])null, false);
    }
    /**
     * origTdoMap 각각의 Entry 정보를 destSdo에 복사한다.
     *
     * @param   origTdoMap                 원본 TDO instance
     * @param   destSdo                    복사 대상 SDO instance
     * @param   dateProps                  '-'을 제거하여야 할 날짜유형 속성명들의 배열
     * @throws  NullPointerException       origTdoMap 또는 destSdo가 null일 경우
     * @throws  IllegalArgumentException   origTdoMap이 비어 있을 경우
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                      , String[]          dateProps
                                     )
    {
        copyProperties(origTdoMap, destSdo, dateProps, false);
    }
    /**
     * origTdoMap 각각의 Entry 정보를 destSdo에 복사한다.
     *
     * @param   origTdoMap                 원본 TDO instance
     * @param   destSdo                    복사 대상 SDO instance
     * @param   needsConform               origTdoMap 각각의 Entry 명을 Java code-conventino 룰에 따라 수정할 필요가 있는지 여부
     *                                     (true일 경우 속성명 매핑을 예와 같이 수행한다. 예) TDO : SDO = AA_BB_CC : aaBbCc )
     * @throws  NullPointerException       origTdoMap 또는 destSdo가 null일 경우
     * @throws  IllegalArgumentException   origTdoMap이 비어 있을 경우
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                      , boolean           needsConform
                                     )
    {
        copyProperties(origTdoMap, destSdo, (String[])null, needsConform);
    }
    /**
     * origTdoMap 각각의 Entry 정보를 destSdo에 복사한다.
     *
     * @param   origTdoMap                 원본 TDO instance
     * @param   destSdo                    복사 대상 SDO instance
     * @param   dateProps                  '-'을 제거하여야 할 날짜유형 속성명들의 배열
     * @param   needsConform               origTdoMap 각각의 Entry 명을 Java code-conventino 룰에 따라 수정할 필요가 있는지 여부
     *                                     (true일 경우 속성명 매핑을 예와 같이 수행한다. 예) TDO : SDO = AA_BB_CC : aaBbCc )
     * @throws  NullPointerException       origTdoMap 또는 destSdo가 null일 경우
     * @throws  IllegalArgumentException   origTdoMap이 비어 있을 경우
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                      , String[]          dateProps
                                      , boolean           needsConform
                                     )
    {
        if(origTdoMap == null || destSdo == null)
            throw new NullPointerException("원본 TDO map 또는 복사 대상 SDO의 instance가 null입니다.");
        if(origTdoMap.isEmpty())
            throw new IllegalArgumentException("원본 TDO map이 비어 있습니다.");

        List datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );

        for(Iterator iter=origTdoMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry)iter.next();
            String    key   = needsConform ? conformToSdoAttrName( entry.getKey().toString() ) : entry.getKey().toString();
            String    value = (entry.getValue() == null) ? (String)null : entry.getValue().toString();

            if(datePropList.contains(key) && stringValue(value).length() > 0)
                value = value.replaceAll("-", "");

            boolean rslt = destSdo.set(key, value);
            if(!rslt)
                logger.warn("TDO ==> SDO 속성 복사 실패 : [name|value] = [" + key + "|" + value + "]");
        }
    }
    /**
     * 주어진 속성명을 SDO 속성명에 알맞게 변환하여 반환한다.
     * (SDO의 속성명은 Java code-convention에 따른다. 예) AA_BB_CC ==> aaBbCc)
     *
     * @param   attrName                  변환 대상 속성명
     * @return                            SDO 속성명에 알맞게 변환된 문자열
     * @throws  NullPointerException      변환 대상 속성명이 null일 경우
     * @throws  IllegalArgumentException  변환 대상 속성명이 빈 문자열일 경우
     */
    private static String conformToSdoAttrName(String attrName)
    {
        if(attrName == null)
            throw new NullPointerException("변환 대상 속성명이 null입니다.");
        if(attrName.length() == 0)
            throw new IllegalArgumentException("변환 대상 속성명이 빈 문자열입니다.");

        String[]     nameArray = attrName.split("_");
        StringBuffer buff      = new StringBuffer( attrName.length() );
        for(int i=0; i<nameArray.length; i++) {
            buff.append( capitalize( nameArray[i] ) );
        }

        return (buff.length() == 1) ? buff.toString().toLowerCase() : Character.toLowerCase( buff.charAt(0) ) + buff.substring(1);
    }
    /**
     * 주어진 문자열의 첫문자를 대문자로, 나머지를 소문자로 변환한 문자열을 반환한다.
     *
     * @param   src                       변환 대상 문자열
     * @return                            주어진 문자열의 첫문자를 대문자로, 나머지를 소문자로 변환한 문자열을 반환
     * @throws  NullPointerException      변환 대상 문자열이 null일 경우
     */
    private static String capitalize(String src)
    {
        if(src == null)
            throw new NullPointerException("변환 대상 문자열이 null입니다.");
        if(src.length() == 0)
            return src;

        char firstCh = Character.toUpperCase(src.charAt(0));

        return (src.length() == 1) ? String.valueOf(firstCh) : firstCh + src.substring(1).toLowerCase();
    }





    /**
     * origSdo의 속성 값들을 destTdo에 복사한다.
     *
     * @param   origSdo                    원본 SDO instance
     * @param   destTdo                    복사 대상 TDO instance
     * @param   propNames                  SDO의 속성명 배열
     * @throws  NullPointerException       origSdo 또는 destTdo가 null일 경우
     * @throws  IllegalArgumentException   SDO 속성명 배열이 null 또는 길이가 0일 경우
     * @throws  IntrospectionException     BeanInfo를 통한 Introspection에 실패하였을 경우
     * @throws  IllegalAccessException     getter 또는 setter 메소드에 접근 권한이 없을 경우
     * @throws  InvocationTargetException  getter 또는 setter 실행시 예외상황이 발생한 경우
     */
    public static void copyProperties(  WrcDatasetWrapper origSdo
                                      , IDO               destTdo
                                      , String[]          propNames
                                     ) throws IntrospectionException
                                            , IllegalAccessException
                                            , InvocationTargetException
    {
        copyProperties(origSdo, destTdo, propNames, (String[])null);
    }
    /**
     * origSdo의 속성 값들을 destTdo에 복사한다.
     *
     * @param   origSdo                    원본 SDO instance
     * @param   destTdo                    복사 대상 TDO instance
     * @param   propNames                  SDO의 속성명 배열
     * @param   dateProps                  '-'을 추가하여야 할 날짜유형 속성명들의 배열
     * @throws  NullPointerException       origSdo 또는 destTdo가 null일 경우
     * @throws  IllegalArgumentException   SDO 속성명 배열이 null 또는 길이가 0일 경우
     * @throws  IntrospectionException     BeanInfo를 통한 Introspection에 실패하였을 경우
     * @throws  IllegalAccessException     getter 또는 setter 메소드에 접근 권한이 없을 경우
     * @throws  InvocationTargetException  getter 또는 setter 실행시 예외상황이 발생한 경우
     */
    public static void copyProperties(  WrcDatasetWrapper origSdo
                                      , IDO               destTdo
                                      , String[]          propNames
                                      , String[]          dateProps
                                     ) throws IntrospectionException
                                            , IllegalAccessException
                                            , InvocationTargetException
    {
        if(origSdo == null || destTdo == null)
            throw new NullPointerException("원본 SDO 객체 또는 복사 대상 TDO의 instance가 null입니다.");
        if(propNames == null || propNames.length == 0)
            throw new IllegalArgumentException("SDO 속성명 배열이 null 또는 길이가 0입니다.");

        PropertyDescriptor[] destDesc = Introspector.getBeanInfo( destTdo.getClass() ).getPropertyDescriptors();

        Map  descMap      = getDescriptorMap( destDesc );
        List datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );

        for(int i=0; i<propNames.length; i++) {
            String             propValue  = origSdo.get( propNames[i] );
            PropertyDescriptor descriptor = (PropertyDescriptor)descMap.get( propNames[i] );
            if(descriptor == null) {
                logger.warn("SDO ==> TDO 속성 복사 실패 : [name|value] = [" + propNames[i] + "|" + propValue + "]");
                continue;
            }

            Method setter = descriptor.getWriteMethod();
            if(setter == null)
                throw new IllegalAccessException("Property [" + propNames[i] + "]에 값을 쓸 수 없습니다.");

            if(datePropList.contains(propNames[i]) && stringValue(propValue).length() > 0) {
                if( DateUtil.isValid(propValue, "yyyyMMdd") )
                    propValue = DateFormatUtil.format( propValue );
                else if( DateUtil.isValid(propValue, "yyyyMM") )
                    propValue = DateFormatUtil.formatYyyyMm( propValue );
                else
                    throw new IllegalArgumentException("날짜유형 속성 값이 지원하지 않는 패턴입니다. : [name|value] = [" + propNames[i] + "|" + propValue + "]");
            }

            setter.invoke(destTdo, new Object[]{ propValue });
        }
    }
    private static Map getDescriptorMap(PropertyDescriptor[] descriptors)
    {
        Map map = new HashMap( (int)Math.ceil(descriptors.length * 4/3d) );
        for(int i=0; i<descriptors.length; i++) {
            map.put(descriptors[i].getName(), descriptors[i]);
        }
        return map;
    }


    /**
     * origSdo의 속성 값들을 destTdoMap에 복사한다.
     *
     * @param   origSdo                    원본 SDO instance
     * @param   destTdoMap                 복사 대상 TDO map
     * @param   propNames                  SDO의 속성명 배열
     * @throws  NullPointerException       origSdo 또는 destTdoMap이 null일 경우
     * @throws  IllegalArgumentException   SDO 속성명 배열이 null 또는 길이가 0일 경우
     */
    public static void copyProperties(  WrcDatasetWrapper origSdo
                                      , Map               destTdoMap
                                      , String[]          propNames
                                     )
    {
        copyProperties(origSdo, destTdoMap, propNames, (String[])null);
    }
    /**
     * origSdo의 속성 값들을 destTdoMap에 복사한다.
     *
     * @param   origSdo                    원본 SDO instance
     * @param   destTdoMap                 복사 대상 TDO map
     * @param   propNames                  SDO의 속성명 배열
     * @param   dateProps                  '-'을 추가하여야 할 날짜유형 속성명들의 배열
     * @throws  NullPointerException       origSdo 또는 destTdoMap이 null일 경우
     * @throws  IllegalArgumentException   SDO 속성명 배열이 null 또는 길이가 0일 경우
     */
    public static void copyProperties(  WrcDatasetWrapper origSdo
                                      , Map               destTdoMap
                                      , String[]          propNames
                                      , String[]          dateProps
                                     )
    {
        if(origSdo == null || destTdoMap == null)
            throw new NullPointerException("원본 SDO 객체 또는 복사 대상 TDO map이 null입니다.");
        if(propNames == null || propNames.length == 0)
            throw new IllegalArgumentException("SDO 속성명 배열이 null 또는 길이가 0입니다.");

        List datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );
        for(int i=0; i<propNames.length; i++) {
            String propValue = origSdo.get( propNames[i] );

            if(datePropList.contains(propNames[i]) && stringValue(propValue).length() > 0) {
                if( DateUtil.isValid(propValue, "yyyyMMdd") )
                    propValue = DateFormatUtil.format( propValue );
                else if( DateUtil.isValid(propValue, "yyyyMM") )
                    propValue = DateFormatUtil.formatYyyyMm( propValue );
                else
                    throw new IllegalArgumentException("날짜유형 속성 값이 지원하지 않는 패턴입니다. : [name|value] = [" + propNames[i] + "|" + propValue + "]");
            }

            destTdoMap.put(propNames[i], propValue);
        }
    }



    /**
     * value가 null 또는 문자열 'NULL'일 경우 길이가 0인 빈문자열을, 그외의 경우 원본 값인 value를 반환한다.
     * null 체크를 단순화하기 위해 구현된 메소드이다.
     *
     * @param   value         위한 원본 값
     * @return                value가 null 또는 문자열 'NULL'일 경우 길이가 0인 빈문자열을, 그외의 경우 원본 값인 value를 반환
     */
    public static String stringValue(String value) {
        return stringValue(value, "");
    }
    /**
     * value가 null 또는 문자열 'NULL'일 경우 defaultValue를, 그외의 경우 원본 값인 value를 반환한다.
     * null 체크를 단순화하기 위해 구현된 메소드이다.
     *
     * @param   value         원본 값
     * @param   defaultValue  value가 null 또는 문자열 'NULL'일 경우 반환할 기본값
     * @return                value가 null 또는 문자열 'NULL'일 경우 defaultValue를, 그외의 경우 원본 값인 value를 반환
     */
    public static String stringValue(String value, String defaultValue) {
        return (value == null || EMPTY_STR_SYMBOL.equals(value)) ? defaultValue : value;
    }


    /**
     * value를 double형으로 변환하여 반환한다. 만약, value가 null일 경우 0을 반환한다.
     *
     * @param   value         원본 값
     * @return                value가 null이 아닐 경우는 double형으로 변환된 value를, null일 경우 0을 반환
     */
    public static double doubleValue(String value) {
        return doubleValue(value, 0d);
    }
    /**
     * value를 double형으로 변환하여 반환한다. 만약, value가 null일 경우 defaultValue를 반환한다.
     *
     * @param   value         원본 값
     * @param   defaultValue  value가 null일 경우 반환할 기본값
     * @return                value가 null이 아닐 경우는 double형으로 변환된 value를, null일 경우 defaultValue를 반환
     */
    public static double doubleValue(String value, double defaultValue) {
        return (value == null) ? defaultValue : Double.parseDouble( value );
    }


    /**
     * value를 long형으로 변환하여 반환한다. 만약, value가 null일 경우 0을 반환한다.
     *
     * @param   value         원본 값
     * @return                value가 null이 아닐 경우는 long형으로 변환된 value를, null일 경우 0을 반환
     */
    public static long longValue(String value) {
        return longValue(value, 0L);
    }
    /**
     * value를 long형으로 변환하여 반환한다. 만약, value가 null일 경우 defaultValue를 반환한다.
     *
     * @param   value         원본 값
     * @param   defaultValue  value가 null일 경우 반환할 기본값
     * @return                value가 null이 아닐 경우는 long형으로 변환된 value를, null일 경우 defaultValue를 반환
     */
    public static long longValue(String value, long defaultValue) {
        return (long)doubleValue(value, (double)defaultValue);
    }
}
