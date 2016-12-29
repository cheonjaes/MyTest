package com.cbscap.util;

/**
 * <p>날짜 포맷을 바꿔주는 유틸리티
 * <p>ex) yyyy-MM <-> yyyyMM
 *
 * @author 김대호
 */
public class DateFormatUtil
{
    /**
     * 날짜 포맷의 delimiter ( "-" )
     */
    static final String delimiter = "-"; // 날짜 포맷 상수값

    /**
     * <p>날짜포맷 변환
     * <p>yyyyMMdd -> yyyy-MM-dd
     * <pre><code>
     * format("20070901") return "2007-09-01"
     * format("2007-09-01") return "2007-09-01"
     * format("2007-0901") return "2007-0901"
     * format(null) return null
     * </code></pre>
     *
     * @param value yyyyMMdd형식의 문자열 Date
     * @return yyyy-MM-dd 형식의 문자열 Date
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
     * <p>날짜포맷 변환
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
     * <p>년월포맷 변경
     * <p>yyyyMM -> yyyy-MM
     *
     * <pre><code>
     * format("200709") return "2007-09"
     * format(null) return null
     * </code></pre>
     * @param value yyyyMM형 년월
     * @return yyyy-MM형 년월
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
     * <p>년월포맷 변경
     * <p>yyyy-MM -> yyyyMM
     *
     * <pre><code>
     * format("2007-09") return "200709"
     * format(null) return null
     * </code></pre>
     *
     * @param value yyyy-MM 형식 년월
     * @return yyyyMM 형식 년월
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
     * 임시 테스트
     * @param args
     */
    public static void  main(String[] args)
    {
        System.out.println("날짜format>>"+format("20051212"));
        System.out.println("날짜unformat>>"+unformat("2005-12-12"));

        System.out.println("날짜format>>"+formatYyyyMm("200512"));
        System.out.println("날짜unformat>>"+unformatYyyyMm("2005-12"));
    }
}
