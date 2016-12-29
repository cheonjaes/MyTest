package com.cbscap.util;

import com.cbscap.util.Util;

import java.util.Date;


/**
 * 음력 <-> 양력 변환 유틸리티
 * @author 김대호
 * </pre>
 */
public class CalendarUtil {
    private static Date init_date;
    private static int syear;
    private static int smonth;
    private static int sday;
    private static int lyear;
    private static int lmonth;
    private static int lday;
    private static int m1;
    private static int m2;
    private static int i;
    private static int j;
    private static int i1;
    private static int j1;
    private static int jcount;
    private static int w;
    private static int m0;
    private static int k1;
    private static int k2;
    private static int n2;
    private static boolean ll;
    private static boolean leapyes;
    private static boolean leap;
    private static int[] dt;
    private static long td;
    private static long td0;
    private static long td1;
    private static long td2;
    private static long k11;
    private static long y;
    private static final String[] yuk = {
            "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"
        };
    private static final String[] gap = {
            "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"
        };
    private static final String[] ddi = {
            "쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양", "원숭이", "닭", "개", "돼지"
        };
    private static final int[] m = {
            31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
        };
    private static final String[] week = { "일", "월", "화", "수", "목", "금", "토" };
    private static final int[] kk = {
            1, 2, 1, 2, 1, 2, 2, 3, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1,
            2, 2, 0, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 0, 2, 1, 1, 2, 1, 3, 2,
            1, 2, 2, 1, 2, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 2, 1, 2, 1,
            2, 1, 1, 2, 1, 2, 1, 2, 0, 2, 2, 1, 2, 3, 2, 1, 1, 2, 1, 2, 1, 2, 2,
            1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1,
            2, 0, 1, 2, 3, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
            2, 1, 2, 2, 0, 1, 1, 2, 1, 1, 2, 3, 2, 2, 1, 2, 2, 2, 1, 1, 2, 1, 1,
            2, 1, 2, 1, 2, 2, 2, 0, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 2, 1,
            2, 1, 2, 3, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2,
            0, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 2, 1, 2, 3, 2, 2, 1, 2, 1,
            2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 0, 1, 2, 1, 1, 2, 1,
            2, 2, 3, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0, 2, 1, 2,
            1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 1, 2, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2,
            2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 0, 2, 2, 1, 2, 2, 1, 1, 2, 1, 2,
            1, 2, 0, 1, 2, 2, 1, 4, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2,
            1, 2, 1, 2, 1, 0, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 0, 1, 2, 3, 1,
            2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0, 2,
            1, 2, 1, 1, 2, 3, 1, 2, 2, 1, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1,
            2, 0, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 0, 2, 2, 1, 2, 2, 3, 1, 2,
            1, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 0, 1, 2, 1, 2, 1,
            2, 2, 1, 2, 1, 2, 1, 0, 2, 1, 3, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1,
            1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 0, 1, 2, 1, 1, 2, 1, 2, 3, 2, 2, 1, 2,
            2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 0, 2, 1, 2, 1, 1, 2, 1, 1, 2,
            1, 2, 2, 0, 2, 1, 2, 2, 1, 3, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1,
            2, 1, 2, 1, 1, 2, 0, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 0, 2, 1, 2,
            2, 3, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 0,
            2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 1, 2, 3, 1, 2, 1, 1, 2, 2, 1,
            2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 0, 1, 2, 2, 1, 1, 2, 3,
            1, 2, 1, 2, 2, 1, 2, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 1, 0, 2, 2, 2, 1,
            2, 1, 2, 1, 1, 2, 1, 2, 0, 1, 2, 2, 1, 2, 4, 1, 2, 1, 2, 1, 1, 2, 1,
            2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 0, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2,
            1, 0, 2, 1, 1, 4, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1,
            2, 2, 2, 1, 0, 2, 2, 1, 1, 2, 1, 1, 4, 1, 2, 2, 1, 2, 2, 2, 1, 1, 2,
            1, 1, 2, 1, 2, 1, 2, 0, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 2, 2,
            1, 2, 2, 1, 4, 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2,
            0, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 1, 1, 2, 1, 4, 1, 2, 1, 2,
            2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 0, 2, 1, 1, 2, 1, 1,
            2, 1, 2, 2, 1, 2, 0, 2, 2, 3, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2,
            1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 2, 2, 1, 2, 1, 2, 1, 3, 2, 1, 2, 1, 2,
            2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2,
            1, 2, 0, 1, 2, 1, 2, 1, 4, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 2, 1,
            2, 2, 1, 2, 2, 0, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 0, 2, 1, 1, 4,
            1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0, 2,
            1, 2, 1, 2, 1, 1, 2, 3, 2, 1, 2, 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1,
            2, 0, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 2, 1, 2, 1, 2, 2, 3, 2,
            1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 0, 1, 2, 1, 1, 2,
            1, 2, 2, 1, 2, 2, 1, 0, 2, 1, 2, 1, 3, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1,
            2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1,
            0, 2, 2, 2, 3, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 1, 2, 1,
            2, 1, 2, 0, 1, 2, 2, 1, 2, 1, 2, 3, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
            2, 1, 2, 1, 2, 1, 0, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 0, 1, 2, 1,
            1, 2, 3, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 0,
            2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 2, 1, 0, 2, 2, 1, 2, 3, 1, 2, 1, 1, 2,
            2, 1, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 0, 2, 2, 1, 2, 1, 2, 1,
            2, 3, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 0, 2, 2, 1, 2,
            1, 2, 2, 1, 2, 1, 2, 1, 0, 2, 1, 1, 2, 1, 2, 4, 1, 2, 2, 1, 2, 1, 2,
            1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2,
            2, 0, 2, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1,
            2, 1, 2, 2, 0, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 3, 2, 2, 1, 2, 2, 1, 2,
            1, 2, 1, 1, 2, 1, 2, 0, 1, 2, 2, 1, 2, 2, 1, 2, 1, 2, 1, 1, 0, 2, 1,
            2, 2, 1, 2, 3, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1,
            0, 2, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 0, 1, 2, 1, 1, 2, 3, 1, 2, 1,
            2, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 0, 1, 2, 2, 1, 1, 2,
            1, 1, 2, 1, 2, 2, 0, 1, 2, 2, 3, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2,
            1, 2, 1, 2, 1, 1, 2, 1, 2, 0, 1, 2, 2, 1, 2, 2, 1, 2, 3, 2, 1, 1, 2,
            1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 0, 1, 1, 2, 1, 2, 1, 2, 2, 1, 2,
            2, 1, 0, 2, 1, 1, 2, 1, 3, 2, 2, 1, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2,
            1, 2, 2, 2, 1, 0, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 0, 2, 2, 2, 1,
            3, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 0, 2,
            2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 0, 1, 2, 3, 2, 2, 1, 2, 1, 2, 2, 1,
            1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 1, 1, 2, 1, 2, 1, 2, 3,
            2, 2, 1, 2, 2, 1, 1, 2, 1, 1, 2, 1, 2, 2, 2, 1, 2, 0, 2, 1, 1, 2, 1,
            1, 2, 1, 2, 2, 1, 2, 0, 2, 2, 1, 1, 2, 3, 1, 2, 1, 2, 1, 2, 2, 2, 1,
            2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 1,
            0, 2, 1, 2, 4, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1,
            2, 1, 2, 0, 1, 2, 1, 2, 1, 2, 1, 2, 2, 3, 2, 1, 2, 1, 2, 1, 1, 2, 1,
            2, 2, 2, 1, 2, 2, 0, 1, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 0, 2, 1, 1,
            2, 1, 3, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 0,
            2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 0, 2, 1, 2, 2, 3, 2, 1, 1, 2, 1,
            2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 0, 2, 1, 2, 1, 2, 2, 1,
            2, 1, 2, 1, 2, 0, 1, 2, 3, 2, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1,
            2, 1, 2, 2, 1, 2, 2, 1, 0, 2, 1, 2, 1, 1, 2, 3, 2, 1, 2, 2, 2, 1, 2,
            1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 0, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2,
            2, 0, 1, 2, 2, 1, 2, 3, 1, 2, 1, 1, 2, 2, 1, 2, 2, 1, 2, 2, 1, 1, 2,
            1, 1, 2, 2, 0, 1, 2, 1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 0, 2, 1, 2, 3, 2,
            1, 2, 2, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0, 1, 2,
            1, 1, 2, 1, 2, 3, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 2, 2, 1,
            0, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1, 2, 0, 2, 2, 1, 2, 1, 1, 4, 1, 1,
            2, 1, 2, 2, 2, 2, 1, 2, 1, 1, 2, 1, 1, 2, 1, 2, 0, 2, 2, 1, 2, 1, 2,
            1, 2, 1, 1, 2, 1, 0, 2, 2, 1, 2, 2, 3, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2,
            2, 1, 2, 2, 1, 2, 1, 2, 1, 0, 2, 1, 1, 2, 1, 2, 2, 1, 2, 2, 1, 2, 0,
            1, 2, 3, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 1, 2, 1, 1, 2, 1, 1, 2, 2, 1,
            2, 2, 0
        };

    static {
        init_date = null;
        dt = new int[163];
        init();
    }

    /**
     * 테스트
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(CalendarUtil.solarToLunar("20071001"));
        System.out.println(CalendarUtil.lunarToSolar("20070821", false));
    }

    /**
     * <p>클래스 로딩때 유틸리티 초기화
     */
    public static void init() {
        for (i = 0; i < 163; i++) {
            dt[i] = 0;

            for (j = 0; j < 12; j++) {
                switch (kk[(i * 13) + j]) {
                case 1: // '\001'
                case 3: // '\003'
                    dt[i] = dt[i] + 29;

                    break;

                case 2: // '\002'
                case 4: // '\004'
                    dt[i] = dt[i] + 30;

                    break;
                }
            }

            switch (kk[(i * 13) + 12]) {
            case 1: // '\001'
            case 3: // '\003'
                dt[i] = dt[i] + 29;

                break;

            case 2: // '\002'
            case 4: // '\004'
                dt[i] = dt[i] + 30;

                break;
            }
        }

        td1 = 686686;
    }

    /**
     * 양력을 음력으로 변환
     * @param solarDay 양력일자 yyyyMMdd
     * @return 음력일자 yyyyMMdd
     */
    public static String solarToLunar(String solarDay) {
        syear = Integer.parseInt(solarDay.substring(0, 4));
        smonth = Integer.parseInt(solarDay.substring(4, 6));
        sday = Integer.parseInt(solarDay.substring(6, 8));
        k11 = syear - 1;
        td2 = (((k11 * 365L) + (k11 / 4L)) - (k11 / 100L)) + (k11 / 400L);
        ll = ((syear % 400) == 0) ||
            (((syear % 100) != 0) && ((syear % 4) == 0));

        if (ll) {
            m[1] = 29;
        } else {
            m[1] = 28;
        }

        if (!verifyDate(syear, smonth, sday, "S")) {
            return null;
        }

        for (i = 0; i < (smonth - 1); i++) {
            td2 = td2 + (long) m[i];
        }

        td2 = td2 + (long) sday;
        td = (td2 - td1) + 1L;
        td0 = dt[0];

        for (i = 0; i < 163; i++) {
            if (td <= td0) {
                break;
            }

            td0 = td0 + (long) dt[i + 1];
        }

        lyear = i + 1881;
        td0 = td0 - (long) dt[i];
        td = td - td0;

        if (kk[(i * 13) + 12] != 0) {
            jcount = 13;
        } else {
            jcount = 12;
        }

        m2 = 0;

        for (j = 0; j < jcount; j++) {
            if (kk[(i * 13) + j] <= 2) {
                m2++;
            }

            if (kk[(i * 13) + j] <= 2) {
                m1 = kk[(i * 13) + j] + 28;
            } else {
                m1 = kk[(i * 13) + j] + 26;
            }

            if (td <= (long) m1) {
                break;
            }

            td = td - (long) m1;
        }

        m0 = j;
        lmonth = m2;
        lday = (int) td;
        w = (short) (int) (td2 % 7L);
        i = (int) ((td2 + 4L) % 10L);
        j = (int) ((td2 + 2L) % 12L);
        i1 = (lyear + 6) % 10;
        j1 = (lyear + 8) % 12;

        /*
           String s1 = "음력=>"+Integer.toString(lyear)+"년"+Integer.toString(lmonth)+"월"
            +Integer.toString(lday)+"일,"+week[w]+"요일"+yuk[i1]+gap[j1]+","
            +ddi[j1]+"의해";
         */
        String s1 = String.valueOf(lyear) +
            Util.format(String.valueOf(lmonth), 2, '0') +
            Util.format(String.valueOf(lday), 2, '0');

        return s1;
    }

    /**
     * 음력을 양력으로 변환
     * @param lunarDay 음력일자 yyyyMMdd
     * @param leapYn 양력일자 yyyyMMdd
     * @return
     */
    public static String lunarToSolar(String lunarDay, boolean leapYn) {
        lyear = Integer.parseInt(lunarDay.substring(0, 4));
        lmonth = Integer.parseInt(lunarDay.substring(4, 6));
        lday = Integer.parseInt(lunarDay.substring(6, 8));
        leapyes = leapYn;

        if (!leapyes && !verifyDate(lyear, lmonth, lday, "L-")) {
            return null;
        }

        if (leapyes && !verifyDate(lyear, lmonth, lday, "L+")) {
            return null;
        }

        m1 = -1;
        td = 0L;

        if (lyear != 1881) {
            m1 = lyear - 1882;

            for (i = 0; i <= m1; i++) {
                for (j = 0; j < 13; j++) {
                    td = td + (long) kk[(i * 13) + j];
                }

                if (kk[(i * 13) + 12] == 0) {
                    td = td + 336L;
                } else {
                    td = td + 362L;
                }
            }
        }

        m1++;
        n2 = lmonth - 1;
        m2 = -1;

        do {
            m2++;

            if (kk[(m1 * 13) + m2] > 2) {
                td = td + 26L + (long) kk[(m1 * 13) + m2];
                n2++;

                continue;
            }

            if (m2 == n2) {
                break;
            }

            td = td + 28L + (long) kk[(m1 * 13) + m2];
        } while (true);

        if (leapyes) {
            td = td + 28L + (long) kk[(m1 * 13) + m2];
        }

        td = td + (long) lday + 29L;
        m1 = 1880;

        do {
            m1++;
            leap = ((m1 % 400) == 0) || (((m1 % 100) != 0) && ((m1 % 4) == 0));

            if (leap) {
                m2 = 366;
            } else {
                m2 = 365;
            }

            if (td < (long) m2) {
                break;
            }

            td = td - (long) m2;
        } while (true);

        syear = m1;
        m[1] = m2 - 337;
        m1 = 0;

        do {
            m1++;

            if (td <= (long) m[m1 - 1]) {
                break;
            }

            td = td - (long) m[m1 - 1];
        } while (true);

        smonth = m1;
        sday = (int) td;
        y = (long) syear - 1L;
        td = (((y * 365L) + (y / 4L)) - (y / 100L)) + (y / 400L);
        leap = ((syear % 400) == 0) ||
            (((syear % 100) != 0) && ((syear % 4) == 0));

        if (leap) {
            m[1] = 29;
        } else {
            m[1] = 28;
        }

        for (i = 0; i < (smonth - 1); i++) {
            td = td + (long) m[i];
        }

        td = td + (long) sday;
        w = (int) (td % 7L);
        i = (int) (td % 10L);
        i = (i + 4) % 10;
        j = (int) (td % 12L);
        j = (j + 2) % 12;
        k1 = (lyear + 6) % 10;
        k2 = (lyear + 8) % 12;

        /*
           String s2 = "양력=>"+Integer.toString(syear)+"년"+Integer.toString(smonth)+"월"
            +Integer.toString(sday)+"일-"+week[w]+"요일";
         */
        String s2 = String.valueOf(syear) +
            Util.format(String.valueOf(smonth), 2, '0') +
            Util.format(String.valueOf(sday), 2, '0');

        return s2;
    }

    private static boolean verifyDate(int k, int l, int l1, String s) {
        if ((k < 1881) || (k > 2043) || (l < 1) || (l > 12)) {
            return false;
        }

        if (s.equals("S") && (l1 > m[l - 1])) {
            return false;
        }

        if (s.equals("L+")) {
            if (kk[((k - 1881) * 13) + 12] < 1) {
                return false;
            }

            if (kk[((k - 1881) * 13) + l] < 3) {
                return false;
            }

            if ((kk[((k - 1881) * 13) + l] + 26) < l1) {
                return false;
            }
        }

        if (s.equals("L-")) {
            j = l - 1;

            for (i = 1; i <= 12; i++) {
                if (kk[(((k - 1881) * 13) + i) - 1] > 2) {
                    j++;
                }
            }

            if (l1 > (kk[((k - 1881) * 13) + j] + 28)) {
                return false;
            }
        }

        return true;
    }
}
