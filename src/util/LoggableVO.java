package com.cbscap.util;

/**
 * <p>getter/setter를 가진 Entity클래스의 속성변수/값을 얻기위한 로깅클래스
 * @author 김대호
 */
public class LoggableVO {

	public LoggableVO() {
	}

	/**
	 * <p>이 클래스를 상속받으면 로깅할때 toString()을 그대로 사용할수 있다.
	 * @return 변수명 / 값의 String
	 */
	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		String fName = null;
		String uName = null;
		Class cls = this.getClass();
		String value = null;
		java.lang.reflect.Field[] f = cls.getDeclaredFields();

		sBuffer.append("[============ ");
		sBuffer.append(cls.getName());
		sBuffer.append(" ============\n");
		int len = f.length;
		for (int i = 0; i < len; i++) {
			try {
				fName = f[i].getName();
				uName = fName.substring(0, 1).toUpperCase() +
					fName.substring(1, fName.length());

				java.lang.reflect.Method m = cls.getMethod("get" + uName,
					new Class[] {});
				value = String.valueOf( m.invoke(this, new Object[] {}));
				sBuffer.append("\t#");
				sBuffer.append(i + 1);
				sBuffer.append("#");
				sBuffer.append(fName);
				sBuffer.append("|");
				sBuffer.append(value);
				sBuffer.append("|");
				if ( (i + 1) % 5 == 0 && i != (len - 1)) {
					sBuffer.append("\n");
				}
			}
			catch (Exception e) {

			}
		}
		sBuffer.append("\n]");

		return sBuffer.toString();
	}

}
