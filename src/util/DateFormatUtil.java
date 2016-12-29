package com.cbscap.util;

/**
 * <p>��¥ ������ �ٲ��ִ� ��ƿ��Ƽ
 * <p>ex) yyyy-MM <-> yyyyMM
 *
 * @author ���ȣ
 */
public class DateFormatUtil
{
    /**
     * ��¥ ������ delimiter ( "-" )
     */
    static final String delimiter = "-"; // ��¥ ���� �����

    /**
     * <p>��¥���� ��ȯ
     * <p>yyyyMMdd -> yyyy-MM-dd
     * <pre><code>
     * format("20070901") return "2007-09-01"
     * format("2007-09-01") return "2007-09-01"
     * format("2007-0901") return "2007-0901"
     * format(null) return null
     * </code></pre>
     *
     * @param value yyyyMMdd������ ���ڿ� Date
     * @return yyyy-MM-dd ������ ���ڿ� Date
     */
    public static String format(String value)
    {
        if (value == null) {
            return null;
        }

        if (value.length() < 8) {
            return value;
        }

        StringBuffer sBuf = new StringBuffer(10);
        sBuf.append(value.substring(0, 4)).append(delimiter)
            .append(value.substring(4, 6)).append(delimiter).append(value.substring(
                6, 8));

        return sBuf.toString();
    }

    /**
     * <p>��¥���� ��ȯ
     * <p>yyyyMMdd -> yyyy-MM-dd
     * <pre><code>
     * format("20070901") return "20070901"
     * format("2007-09-01") return "20070901"
     * format("2007-0901") return "2007-0901"
     * format(null) return null
     * </code></pre>
     *
     * @param value
     * @return
     */
    public static String unformat(String value)
    {
        if (value == null)
        {
            return null;
        }

        if (value.length() < 10)
        {
            return value;
        }

        StringBuffer sBuf = new StringBuffer(8);
        sBuf.append(value.substring(0, 4)).append(value.substring(5, 7)).append(value.substring(
                8, 10));

        return sBuf.toString();

    }

    /**
     * <p>������� ����
     * <p>yyyyMM -> yyyy-MM
     *
     * <pre><code>
     * format("200709") return "2007-09"
     * format(null) return null
     * </code></pre>
     * @param value yyyyMM�� ���
     * @return yyyy-MM�� ���
     */
    public static String formatYyyyMm(String value)
    {
        if (value == null) {
            return null;
        }

        if (value.length() < 6) {
            return value;
        }

        StringBuffer sBuf = new StringBuffer(10);
        sBuf.append(value.substring(0, 4)).append(delimiter)
            .append(value.substring(4, 6));

        return sBuf.toString();
    }

    /**
     * <p>������� ����
     * <p>yyyy-MM -> yyyyMM
     *
     * <pre><code>
     * format("2007-09") return "200709"
     * format(null) return null
     * </code></pre>
     *
     * @param value yyyy-MM ���� ���
     * @return yyyyMM ���� ���
     */
    public static String unformatYyyyMm(String value)
    {
        if (value == null)
        {
            return null;
        }

        if (value.length() < 7)
        {
            return value;
        }

        StringBuffer sBuf = new StringBuffer(8);
        sBuf.append(value.substring(0, 4)).append(value.substring(5, 7));

        return sBuf.toString();

    }

    /**
     * �ӽ� �׽�Ʈ
     * @param args
     */
    public static void  main(String[] args)
    {
        System.out.println("��¥format>>"+format("20051212"));
        System.out.println("��¥unformat>>"+unformat("2005-12-12"));

        System.out.println("��¥format>>"+formatYyyyMm("200512"));
        System.out.println("��¥unformat>>"+unformatYyyyMm("2005-12"));
    }
}
