package com.cbscap.util;


/**
 * <p>���� <-> �ݰ�
 *
 * <pre><code>
 *  ��>
 *  '��' <-> '1'
 *  '��' <-> '@'
 * </code></pre>
 *
 * @author ���ȣ
 */
public class ReplaceChar {
	public ReplaceChar() {}

	/**
	 * ���� -> �ݰ�
	 * @param ����
	 * @return �ݰ�, null�Ǵ�""�Է½� ""����
	 */
	public static String replaceChar(String fileCntn) {
		if(fileCntn == null || fileCntn.length() < 1) {
			return "";
		}
		String retVal = fileCntn.replace('��', '0');
		retVal = retVal.replace('��', '1');
		retVal = retVal.replace('��', '2');
		retVal = retVal.replace('��', '3');
		retVal = retVal.replace('��', '4');
		retVal = retVal.replace('��', '5');
		retVal = retVal.replace('��', '6');
		retVal = retVal.replace('��', '7');
		retVal = retVal.replace('��', '8');
		retVal = retVal.replace('��', '9');
		retVal = retVal.replace('��', 'A');
		retVal = retVal.replace('��', 'B');
		retVal = retVal.replace('��', 'C');
		retVal = retVal.replace('��', 'D');
		retVal = retVal.replace('��', 'E');
		retVal = retVal.replace('��', 'F');
		retVal = retVal.replace('��', 'G');
		retVal = retVal.replace('��', 'H');
		retVal = retVal.replace('��', 'I');
		retVal = retVal.replace('��', 'J');
		retVal = retVal.replace('��', 'K');
		retVal = retVal.replace('��', 'L');
		retVal = retVal.replace('��', 'M');
		retVal = retVal.replace('��', 'N');
		retVal = retVal.replace('��', 'O');
		retVal = retVal.replace('��', 'P');
		retVal = retVal.replace('��', 'Q');
		retVal = retVal.replace('��', 'R');
		retVal = retVal.replace('��', 'S');
		retVal = retVal.replace('��', 'T');
		retVal = retVal.replace('��', 'U');
		retVal = retVal.replace('��', 'V');
		retVal = retVal.replace('��', 'W');
		retVal = retVal.replace('��', 'X');
		retVal = retVal.replace('��', 'Y');
		retVal = retVal.replace('��', 'Z');
		retVal = retVal.replace('��', 'a');
		retVal = retVal.replace('��', 'b');
		retVal = retVal.replace('��', 'c');
		retVal = retVal.replace('��', 'd');
		retVal = retVal.replace('��', 'e');
		retVal = retVal.replace('��', 'f');
		retVal = retVal.replace('��', 'g');
		retVal = retVal.replace('��', 'h');
		retVal = retVal.replace('��', 'i');
		retVal = retVal.replace('��', 'j');
		retVal = retVal.replace('��', 'k');
		retVal = retVal.replace('��', 'l');
		retVal = retVal.replace('��', 'm');
		retVal = retVal.replace('��', 'n');
		retVal = retVal.replace('��', 'o');
		retVal = retVal.replace('��', 'p');
		retVal = retVal.replace('��', 'q');
		retVal = retVal.replace('��', 'r');
		retVal = retVal.replace('��', 's');
		retVal = retVal.replace('��', 't');
		retVal = retVal.replace('��', 'u');
		retVal = retVal.replace('��', 'v');
		retVal = retVal.replace('��', 'w');
		retVal = retVal.replace('��', 'x');
		retVal = retVal.replace('��', 'y');
		retVal = retVal.replace('��', 'z');
		retVal = retVal.replace('��', '~');
		retVal = retVal.replace('��', '`');
		retVal = retVal.replace('��', '!');
		retVal = retVal.replace('��', '@');
		retVal = retVal.replace('��', '#');
		retVal = retVal.replace('��', '$');
		retVal = retVal.replace('��', '%');
		retVal = retVal.replace('��', '^');
		retVal = retVal.replace('��', '&');
		retVal = retVal.replace('��', '*');
		retVal = retVal.replace('��', '(');
		retVal = retVal.replace('��', ')');
		retVal = retVal.replace('��', '-');
		retVal = retVal.replace('��', '_');
		retVal = retVal.replace('��', '=');
		retVal = retVal.replace('��', '+');
		retVal = retVal.replace('��', '[');
		retVal = retVal.replace('��', ']');
		retVal = retVal.replace('��', '{');
		retVal = retVal.replace('��', '}');
		retVal = retVal.replace('��', '\'');
		retVal = retVal.replace('��', '"');
		retVal = retVal.replace('��', ';');
		retVal = retVal.replace('��', ':');
		retVal = retVal.replace('��', ',');
		retVal = retVal.replace('��', '.');
		retVal = retVal.replace('��', '/');
		retVal = retVal.replace('��', '<');
		retVal = retVal.replace('��', '>');
		retVal = retVal.replace('��', '?');
		retVal = retVal.replace('��', '\\');
		retVal = retVal.replace('��', ' ');
		retVal = retVal.replace('��', '|');

		return retVal;
	}

	/**
	 * �ݰ� -> ����
	 * @param �ݰ�
	 * @return ����, null�Ǵ�""�Է½� ""����
	 */
	public static String replaceCharQuard(String fileCntn) {
		if(fileCntn == null || fileCntn.length() < 1) {
			return "";
		}

		String retVal = fileCntn.replace ('0','��');
		retVal = retVal.replace   ('1','��');
		retVal = retVal.replace   ('2','��');
		retVal = retVal.replace   ('3','��');
		retVal = retVal.replace   ('4','��');
		retVal = retVal.replace   ('5','��');
		retVal = retVal.replace   ('6','��');
		retVal = retVal.replace   ('7','��');
		retVal = retVal.replace   ('8','��');
		retVal = retVal.replace   ('9','��');
		retVal = retVal.replace   ('A','��');
		retVal = retVal.replace   ('B','��');
		retVal = retVal.replace   ('C','��');
		retVal = retVal.replace   ('D','��');
		retVal = retVal.replace   ('E','��');
		retVal = retVal.replace   ('F','��');
		retVal = retVal.replace   ('G','��');
		retVal = retVal.replace   ('H','��');
		retVal = retVal.replace   ('I','��');
		retVal = retVal.replace   ('J','��');
		retVal = retVal.replace   ('K','��');
		retVal = retVal.replace   ('L','��');
		retVal = retVal.replace   ('M','��');
		retVal = retVal.replace   ('N','��');
		retVal = retVal.replace   ('O','��');
		retVal = retVal.replace   ('P','��');
		retVal = retVal.replace   ('Q','��');
		retVal = retVal.replace   ('R','��');
		retVal = retVal.replace   ('S','��');
		retVal = retVal.replace   ('T','��');
		retVal = retVal.replace   ('U','��');
		retVal = retVal.replace   ('V','��');
		retVal = retVal.replace   ('W','��');
		retVal = retVal.replace   ('X','��');
		retVal = retVal.replace   ('Y','��');
		retVal = retVal.replace   ('Z','��');
		retVal = retVal.replace   ('a','��');
		retVal = retVal.replace   ('b','��');
		retVal = retVal.replace   ('c','��');
		retVal = retVal.replace   ('d','��');
		retVal = retVal.replace   ('e','��');
		retVal = retVal.replace   ('f','��');
		retVal = retVal.replace   ('g','��');
		retVal = retVal.replace   ('h','��');
		retVal = retVal.replace   ('i','��');
		retVal = retVal.replace   ('j','��');
		retVal = retVal.replace   ('k','��');
		retVal = retVal.replace   ('l','��');
		retVal = retVal.replace   ('m','��');
		retVal = retVal.replace   ('n','��');
		retVal = retVal.replace   ('o','��');
		retVal = retVal.replace   ('p','��');
		retVal = retVal.replace   ('q','��');
		retVal = retVal.replace   ('r','��');
		retVal = retVal.replace   ('s','��');
		retVal = retVal.replace   ('t','��');
		retVal = retVal.replace   ('u','��');
		retVal = retVal.replace   ('v','��');
		retVal = retVal.replace   ('w','��');
		retVal = retVal.replace   ('x','��');
		retVal = retVal.replace   ('y','��');
		retVal = retVal.replace   ('z','��');
		retVal = retVal.replace   ('~','��');
		retVal = retVal.replace   ('`','��');
		retVal = retVal.replace   ('!','��');
		retVal = retVal.replace   ('@','��');
		retVal = retVal.replace   ('#','��');
		retVal = retVal.replace   ('$','��');
		retVal = retVal.replace   ('%','��');
		retVal = retVal.replace   ('^','��');
		retVal = retVal.replace   ('&','��');
		retVal = retVal.replace   ('*','��');
		retVal = retVal.replace   ('(','��');
		retVal = retVal.replace   (')','��');
		retVal = retVal.replace   ('-','��');
		retVal = retVal.replace   ('_','��');
		retVal = retVal.replace   ('=','��');
		retVal = retVal.replace   ('+','��');
		retVal = retVal.replace   ('[','��');
		retVal = retVal.replace   (']','��');
		retVal = retVal.replace   ('{','��');
		retVal = retVal.replace   ('}','��');
		retVal = retVal.replace   ('\'', '��');
		retVal = retVal.replace   ('"','��');
		retVal = retVal.replace   (';','��');
		retVal = retVal.replace   (':','��');
		retVal = retVal.replace   (',','��');
		retVal = retVal.replace   ('.','��');
		retVal = retVal.replace   ('/','��');
		retVal = retVal.replace   ('<','��');
		retVal = retVal.replace   ('>','��');
		retVal = retVal.replace   ('?','��');
		retVal = retVal.replace   ('\\','��');
		retVal = retVal.replace   (' ','��');
		retVal = retVal.replace   ('|','��');
		return retVal;
	}
}
