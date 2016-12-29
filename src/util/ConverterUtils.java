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
 * �� Ŭ������ Converter Ŭ������ ���õ� util �޼ҵ���� �����ϱ� ���Ͽ�
 * �ۼ��� Utility Ŭ�����̴�.
 *
 * �� Ŭ������ ���������� �����ϱ� ����,
 * ��� TDO�� Java Beans �Ծ࿡ ���� �ۼ��Ǿ�� �ϸ�
 * TDO ������ ��� Field���� String type���� ����Ǿ�� �Ѵ�.
 *
 * @author   ������
 * @version  1.0.0, 2007-10-31
 * @since    2007-10-31
 */
public class ConverterUtils
{

	public static final  String EMPTY_STR_SYMBOL = "NULL";

    private static final Logger logger           = Logger.getLogger( ConverterUtils.class );


    /**
     * Compiler�� ���� ���ʿ��� public ���� �����ڰ� �ڵ����� �������� �ʵ���
     * private ���� �����ڸ� �����Ѵ�.
     * �� �����ڴ� ��𿡼��� ȣ����� �ʴ´�.
     */
    private ConverterUtils() {}




    /**
     * origTdo�� �Ӽ� ������ destSdo�� �����Ѵ�.
     *
     * @param   origTdo                    ���� TDO instance
     * @param   destSdo                    ���� ��� SDO instance
     * @throws  NullPointerException       origTdo �Ǵ� destSdo�� null�� ���
     * @throws  IntrospectionException     BeanInfo�� ���� Introspection�� �����Ͽ��� ���
     * @throws  IllegalAccessException     getter �Ǵ� setter �޼ҵ忡 ���� ������ ���� ���
     * @throws  InvocationTargetException  getter �Ǵ� setter ����� ���ܻ�Ȳ�� �߻��� ���
     */
    public static void copyProperties(IDO origTdo, WrcDatasetWrapper destSdo) throws IntrospectionException
                                                                                   , IllegalAccessException
                                                                                   , InvocationTargetException
    {
        copyProperties(origTdo, destSdo, (String[])null);
    }
    /**
     * origTdo�� �Ӽ� ������ destSdo�� �����Ѵ�.
     *
     * @param   origTdo                    ���� TDO instance
     * @param   destSdo                    ���� ��� SDO instance
     * @param   dateProps                  '-'�� �����Ͽ��� �� ��¥���� �Ӽ������ �迭
     * @throws  NullPointerException       origTdo �Ǵ� destSdo�� null�� ���
     * @throws  IntrospectionException     BeanInfo�� ���� Introspection�� �����Ͽ��� ���
     * @throws  IllegalAccessException     getter �Ǵ� setter �޼ҵ忡 ���� ������ ���� ���
     * @throws  InvocationTargetException  getter �Ǵ� setter ����� ���ܻ�Ȳ�� �߻��� ���
     */
    public static void copyProperties(IDO origTdo, WrcDatasetWrapper destSdo, String[] dateProps) throws IntrospectionException
                                                                                                       , IllegalAccessException
                                                                                                       , InvocationTargetException
    {
        if(origTdo == null || destSdo == null)
            throw new NullPointerException("���� TDO ��ü �Ǵ� ���� ��� SDO�� instance�� null�Դϴ�.");

        PropertyDescriptor[] origDesc     = Introspector.getBeanInfo( origTdo.getClass() ).getPropertyDescriptors();
        List                 datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );

        for(int i=0; i<origDesc.length; i++) {
            String propName = origDesc[i].getName();
            if( "class".equals(propName) )
                continue;  // skip

            Method getter = origDesc[i].getReadMethod();
            if(getter == null)
                throw new IllegalAccessException("Property [" + propName + "]�� ���� ���� �� �����ϴ�.");

            String propValue = (String)getter.invoke(origTdo, null);
            if(datePropList.contains(propName) && stringValue(propValue).length() > 0)
                propValue = propValue.replaceAll("-", "");

            boolean rslt = destSdo.set(propName, propValue);
            if(!rslt)
                logger.warn("TDO ==> SDO �Ӽ� ���� ���� : [name|value] = [" + propName + "|" + propValue + "]");
        }
    }


    /**
     * origTdoMap ������ Entry ������ destSdo�� �����Ѵ�.
     *
     * @param   origTdoMap                 ���� TDO instance
     * @param   destSdo                    ���� ��� SDO instance
     * @throws  NullPointerException       origTdoMap �Ǵ� destSdo�� null�� ���
     * @throws  IllegalArgumentException   origTdoMap�� ��� ���� ���
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                     )
    {
        copyProperties(origTdoMap, destSdo, (String[])null, false);
    }
    /**
     * origTdoMap ������ Entry ������ destSdo�� �����Ѵ�.
     *
     * @param   origTdoMap                 ���� TDO instance
     * @param   destSdo                    ���� ��� SDO instance
     * @param   dateProps                  '-'�� �����Ͽ��� �� ��¥���� �Ӽ������ �迭
     * @throws  NullPointerException       origTdoMap �Ǵ� destSdo�� null�� ���
     * @throws  IllegalArgumentException   origTdoMap�� ��� ���� ���
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                      , String[]          dateProps
                                     )
    {
        copyProperties(origTdoMap, destSdo, dateProps, false);
    }
    /**
     * origTdoMap ������ Entry ������ destSdo�� �����Ѵ�.
     *
     * @param   origTdoMap                 ���� TDO instance
     * @param   destSdo                    ���� ��� SDO instance
     * @param   needsConform               origTdoMap ������ Entry ���� Java code-conventino �꿡 ���� ������ �ʿ䰡 �ִ��� ����
     *                                     (true�� ��� �Ӽ��� ������ ���� ���� �����Ѵ�. ��) TDO : SDO = AA_BB_CC : aaBbCc )
     * @throws  NullPointerException       origTdoMap �Ǵ� destSdo�� null�� ���
     * @throws  IllegalArgumentException   origTdoMap�� ��� ���� ���
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                      , boolean           needsConform
                                     )
    {
        copyProperties(origTdoMap, destSdo, (String[])null, needsConform);
    }
    /**
     * origTdoMap ������ Entry ������ destSdo�� �����Ѵ�.
     *
     * @param   origTdoMap                 ���� TDO instance
     * @param   destSdo                    ���� ��� SDO instance
     * @param   dateProps                  '-'�� �����Ͽ��� �� ��¥���� �Ӽ������ �迭
     * @param   needsConform               origTdoMap ������ Entry ���� Java code-conventino �꿡 ���� ������ �ʿ䰡 �ִ��� ����
     *                                     (true�� ��� �Ӽ��� ������ ���� ���� �����Ѵ�. ��) TDO : SDO = AA_BB_CC : aaBbCc )
     * @throws  NullPointerException       origTdoMap �Ǵ� destSdo�� null�� ���
     * @throws  IllegalArgumentException   origTdoMap�� ��� ���� ���
     */
    public static void copyProperties(  Map               origTdoMap
                                      , WrcDatasetWrapper destSdo
                                      , String[]          dateProps
                                      , boolean           needsConform
                                     )
    {
        if(origTdoMap == null || destSdo == null)
            throw new NullPointerException("���� TDO map �Ǵ� ���� ��� SDO�� instance�� null�Դϴ�.");
        if(origTdoMap.isEmpty())
            throw new IllegalArgumentException("���� TDO map�� ��� �ֽ��ϴ�.");

        List datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );

        for(Iterator iter=origTdoMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry)iter.next();
            String    key   = needsConform ? conformToSdoAttrName( entry.getKey().toString() ) : entry.getKey().toString();
            String    value = (entry.getValue() == null) ? (String)null : entry.getValue().toString();

            if(datePropList.contains(key) && stringValue(value).length() > 0)
                value = value.replaceAll("-", "");

            boolean rslt = destSdo.set(key, value);
            if(!rslt)
                logger.warn("TDO ==> SDO �Ӽ� ���� ���� : [name|value] = [" + key + "|" + value + "]");
        }
    }
    /**
     * �־��� �Ӽ����� SDO �Ӽ��� �˸°� ��ȯ�Ͽ� ��ȯ�Ѵ�.
     * (SDO�� �Ӽ����� Java code-convention�� ������. ��) AA_BB_CC ==> aaBbCc)
     *
     * @param   attrName                  ��ȯ ��� �Ӽ���
     * @return                            SDO �Ӽ��� �˸°� ��ȯ�� ���ڿ�
     * @throws  NullPointerException      ��ȯ ��� �Ӽ����� null�� ���
     * @throws  IllegalArgumentException  ��ȯ ��� �Ӽ����� �� ���ڿ��� ���
     */
    private static String conformToSdoAttrName(String attrName)
    {
        if(attrName == null)
            throw new NullPointerException("��ȯ ��� �Ӽ����� null�Դϴ�.");
        if(attrName.length() == 0)
            throw new IllegalArgumentException("��ȯ ��� �Ӽ����� �� ���ڿ��Դϴ�.");

        String[]     nameArray = attrName.split("_");
        StringBuffer buff      = new StringBuffer( attrName.length() );
        for(int i=0; i<nameArray.length; i++) {
            buff.append( capitalize( nameArray[i] ) );
        }

        return (buff.length() == 1) ? buff.toString().toLowerCase() : Character.toLowerCase( buff.charAt(0) ) + buff.substring(1);
    }
    /**
     * �־��� ���ڿ��� ù���ڸ� �빮�ڷ�, �������� �ҹ��ڷ� ��ȯ�� ���ڿ��� ��ȯ�Ѵ�.
     *
     * @param   src                       ��ȯ ��� ���ڿ�
     * @return                            �־��� ���ڿ��� ù���ڸ� �빮�ڷ�, �������� �ҹ��ڷ� ��ȯ�� ���ڿ��� ��ȯ
     * @throws  NullPointerException      ��ȯ ��� ���ڿ��� null�� ���
     */
    private static String capitalize(String src)
    {
        if(src == null)
            throw new NullPointerException("��ȯ ��� ���ڿ��� null�Դϴ�.");
        if(src.length() == 0)
            return src;

        char firstCh = Character.toUpperCase(src.charAt(0));

        return (src.length() == 1) ? String.valueOf(firstCh) : firstCh + src.substring(1).toLowerCase();
    }





    /**
     * origSdo�� �Ӽ� ������ destTdo�� �����Ѵ�.
     *
     * @param   origSdo                    ���� SDO instance
     * @param   destTdo                    ���� ��� TDO instance
     * @param   propNames                  SDO�� �Ӽ��� �迭
     * @throws  NullPointerException       origSdo �Ǵ� destTdo�� null�� ���
     * @throws  IllegalArgumentException   SDO �Ӽ��� �迭�� null �Ǵ� ���̰� 0�� ���
     * @throws  IntrospectionException     BeanInfo�� ���� Introspection�� �����Ͽ��� ���
     * @throws  IllegalAccessException     getter �Ǵ� setter �޼ҵ忡 ���� ������ ���� ���
     * @throws  InvocationTargetException  getter �Ǵ� setter ����� ���ܻ�Ȳ�� �߻��� ���
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
     * origSdo�� �Ӽ� ������ destTdo�� �����Ѵ�.
     *
     * @param   origSdo                    ���� SDO instance
     * @param   destTdo                    ���� ��� TDO instance
     * @param   propNames                  SDO�� �Ӽ��� �迭
     * @param   dateProps                  '-'�� �߰��Ͽ��� �� ��¥���� �Ӽ������ �迭
     * @throws  NullPointerException       origSdo �Ǵ� destTdo�� null�� ���
     * @throws  IllegalArgumentException   SDO �Ӽ��� �迭�� null �Ǵ� ���̰� 0�� ���
     * @throws  IntrospectionException     BeanInfo�� ���� Introspection�� �����Ͽ��� ���
     * @throws  IllegalAccessException     getter �Ǵ� setter �޼ҵ忡 ���� ������ ���� ���
     * @throws  InvocationTargetException  getter �Ǵ� setter ����� ���ܻ�Ȳ�� �߻��� ���
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
            throw new NullPointerException("���� SDO ��ü �Ǵ� ���� ��� TDO�� instance�� null�Դϴ�.");
        if(propNames == null || propNames.length == 0)
            throw new IllegalArgumentException("SDO �Ӽ��� �迭�� null �Ǵ� ���̰� 0�Դϴ�.");

        PropertyDescriptor[] destDesc = Introspector.getBeanInfo( destTdo.getClass() ).getPropertyDescriptors();

        Map  descMap      = getDescriptorMap( destDesc );
        List datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );

        for(int i=0; i<propNames.length; i++) {
            String             propValue  = origSdo.get( propNames[i] );
            PropertyDescriptor descriptor = (PropertyDescriptor)descMap.get( propNames[i] );
            if(descriptor == null) {
                logger.warn("SDO ==> TDO �Ӽ� ���� ���� : [name|value] = [" + propNames[i] + "|" + propValue + "]");
                continue;
            }

            Method setter = descriptor.getWriteMethod();
            if(setter == null)
                throw new IllegalAccessException("Property [" + propNames[i] + "]�� ���� �� �� �����ϴ�.");

            if(datePropList.contains(propNames[i]) && stringValue(propValue).length() > 0) {
                if( DateUtil.isValid(propValue, "yyyyMMdd") )
                    propValue = DateFormatUtil.format( propValue );
                else if( DateUtil.isValid(propValue, "yyyyMM") )
                    propValue = DateFormatUtil.formatYyyyMm( propValue );
                else
                    throw new IllegalArgumentException("��¥���� �Ӽ� ���� �������� �ʴ� �����Դϴ�. : [name|value] = [" + propNames[i] + "|" + propValue + "]");
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
     * origSdo�� �Ӽ� ������ destTdoMap�� �����Ѵ�.
     *
     * @param   origSdo                    ���� SDO instance
     * @param   destTdoMap                 ���� ��� TDO map
     * @param   propNames                  SDO�� �Ӽ��� �迭
     * @throws  NullPointerException       origSdo �Ǵ� destTdoMap�� null�� ���
     * @throws  IllegalArgumentException   SDO �Ӽ��� �迭�� null �Ǵ� ���̰� 0�� ���
     */
    public static void copyProperties(  WrcDatasetWrapper origSdo
                                      , Map               destTdoMap
                                      , String[]          propNames
                                     )
    {
        copyProperties(origSdo, destTdoMap, propNames, (String[])null);
    }
    /**
     * origSdo�� �Ӽ� ������ destTdoMap�� �����Ѵ�.
     *
     * @param   origSdo                    ���� SDO instance
     * @param   destTdoMap                 ���� ��� TDO map
     * @param   propNames                  SDO�� �Ӽ��� �迭
     * @param   dateProps                  '-'�� �߰��Ͽ��� �� ��¥���� �Ӽ������ �迭
     * @throws  NullPointerException       origSdo �Ǵ� destTdoMap�� null�� ���
     * @throws  IllegalArgumentException   SDO �Ӽ��� �迭�� null �Ǵ� ���̰� 0�� ���
     */
    public static void copyProperties(  WrcDatasetWrapper origSdo
                                      , Map               destTdoMap
                                      , String[]          propNames
                                      , String[]          dateProps
                                     )
    {
        if(origSdo == null || destTdoMap == null)
            throw new NullPointerException("���� SDO ��ü �Ǵ� ���� ��� TDO map�� null�Դϴ�.");
        if(propNames == null || propNames.length == 0)
            throw new IllegalArgumentException("SDO �Ӽ��� �迭�� null �Ǵ� ���̰� 0�Դϴ�.");

        List datePropList = Arrays.asList( dateProps == null ? new String[0] : dateProps );
        for(int i=0; i<propNames.length; i++) {
            String propValue = origSdo.get( propNames[i] );

            if(datePropList.contains(propNames[i]) && stringValue(propValue).length() > 0) {
                if( DateUtil.isValid(propValue, "yyyyMMdd") )
                    propValue = DateFormatUtil.format( propValue );
                else if( DateUtil.isValid(propValue, "yyyyMM") )
                    propValue = DateFormatUtil.formatYyyyMm( propValue );
                else
                    throw new IllegalArgumentException("��¥���� �Ӽ� ���� �������� �ʴ� �����Դϴ�. : [name|value] = [" + propNames[i] + "|" + propValue + "]");
            }

            destTdoMap.put(propNames[i], propValue);
        }
    }



    /**
     * value�� null �Ǵ� ���ڿ� 'NULL'�� ��� ���̰� 0�� ���ڿ���, �׿��� ��� ���� ���� value�� ��ȯ�Ѵ�.
     * null üũ�� �ܼ�ȭ�ϱ� ���� ������ �޼ҵ��̴�.
     *
     * @param   value         ���� ���� ��
     * @return                value�� null �Ǵ� ���ڿ� 'NULL'�� ��� ���̰� 0�� ���ڿ���, �׿��� ��� ���� ���� value�� ��ȯ
     */
    public static String stringValue(String value) {
        return stringValue(value, "");
    }
    /**
     * value�� null �Ǵ� ���ڿ� 'NULL'�� ��� defaultValue��, �׿��� ��� ���� ���� value�� ��ȯ�Ѵ�.
     * null üũ�� �ܼ�ȭ�ϱ� ���� ������ �޼ҵ��̴�.
     *
     * @param   value         ���� ��
     * @param   defaultValue  value�� null �Ǵ� ���ڿ� 'NULL'�� ��� ��ȯ�� �⺻��
     * @return                value�� null �Ǵ� ���ڿ� 'NULL'�� ��� defaultValue��, �׿��� ��� ���� ���� value�� ��ȯ
     */
    public static String stringValue(String value, String defaultValue) {
        return (value == null || EMPTY_STR_SYMBOL.equals(value)) ? defaultValue : value;
    }


    /**
     * value�� double������ ��ȯ�Ͽ� ��ȯ�Ѵ�. ����, value�� null�� ��� 0�� ��ȯ�Ѵ�.
     *
     * @param   value         ���� ��
     * @return                value�� null�� �ƴ� ���� double������ ��ȯ�� value��, null�� ��� 0�� ��ȯ
     */
    public static double doubleValue(String value) {
        return doubleValue(value, 0d);
    }
    /**
     * value�� double������ ��ȯ�Ͽ� ��ȯ�Ѵ�. ����, value�� null�� ��� defaultValue�� ��ȯ�Ѵ�.
     *
     * @param   value         ���� ��
     * @param   defaultValue  value�� null�� ��� ��ȯ�� �⺻��
     * @return                value�� null�� �ƴ� ���� double������ ��ȯ�� value��, null�� ��� defaultValue�� ��ȯ
     */
    public static double doubleValue(String value, double defaultValue) {
        return (value == null) ? defaultValue : Double.parseDouble( value );
    }


    /**
     * value�� long������ ��ȯ�Ͽ� ��ȯ�Ѵ�. ����, value�� null�� ��� 0�� ��ȯ�Ѵ�.
     *
     * @param   value         ���� ��
     * @return                value�� null�� �ƴ� ���� long������ ��ȯ�� value��, null�� ��� 0�� ��ȯ
     */
    public static long longValue(String value) {
        return longValue(value, 0L);
    }
    /**
     * value�� long������ ��ȯ�Ͽ� ��ȯ�Ѵ�. ����, value�� null�� ��� defaultValue�� ��ȯ�Ѵ�.
     *
     * @param   value         ���� ��
     * @param   defaultValue  value�� null�� ��� ��ȯ�� �⺻��
     * @return                value�� null�� �ƴ� ���� long������ ��ȯ�� value��, null�� ��� defaultValue�� ��ȯ
     */
    public static long longValue(String value, long defaultValue) {
        return (long)doubleValue(value, (double)defaultValue);
    }
}
