package com.cbscap.util;


/**
 * <p>전각 <-> 반각
 *
 * <pre><code>
 *  예>
 *  '１' <-> '1'
 *  '＠' <-> '@'
 * </code></pre>
 *
 * @author 김대호
 */
public class ReplaceChar {
	public ReplaceChar() {}

	/**
	 * 전각 -> 반각
	 * @param 전각
	 * @return 반각, null또는""입력시 ""리턴
	 */
	public static String replaceChar(String fileCntn) {
		if(fileCntn == null || fileCntn.length() < 1) {
			return "";
		}
		String retVal = fileCntn.replace('０', '0');
		retVal = retVal.replace('１', '1');
		retVal = retVal.replace('２', '2');
		retVal = retVal.replace('３', '3');
		retVal = retVal.replace('４', '4');
		retVal = retVal.replace('５', '5');
		retVal = retVal.replace('６', '6');
		retVal = retVal.replace('７', '7');
		retVal = retVal.replace('８', '8');
		retVal = retVal.replace('９', '9');
		retVal = retVal.replace('Ａ', 'A');
		retVal = retVal.replace('Ｂ', 'B');
		retVal = retVal.replace('Ｃ', 'C');
		retVal = retVal.replace('Ｄ', 'D');
		retVal = retVal.replace('Ｅ', 'E');
		retVal = retVal.replace('Ｆ', 'F');
		retVal = retVal.replace('Ｇ', 'G');
		retVal = retVal.replace('Ｈ', 'H');
		retVal = retVal.replace('Ｉ', 'I');
		retVal = retVal.replace('Ｊ', 'J');
		retVal = retVal.replace('Ｋ', 'K');
		retVal = retVal.replace('Ｌ', 'L');
		retVal = retVal.replace('Ｍ', 'M');
		retVal = retVal.replace('Ｎ', 'N');
		retVal = retVal.replace('Ｏ', 'O');
		retVal = retVal.replace('Ｐ', 'P');
		retVal = retVal.replace('Ｑ', 'Q');
		retVal = retVal.replace('Ｒ', 'R');
		retVal = retVal.replace('Ｓ', 'S');
		retVal = retVal.replace('Ｔ', 'T');
		retVal = retVal.replace('Ｕ', 'U');
		retVal = retVal.replace('Ｖ', 'V');
		retVal = retVal.replace('Ｗ', 'W');
		retVal = retVal.replace('Ｘ', 'X');
		retVal = retVal.replace('Ｙ', 'Y');
		retVal = retVal.replace('Ｚ', 'Z');
		retVal = retVal.replace('ａ', 'a');
		retVal = retVal.replace('ｂ', 'b');
		retVal = retVal.replace('ｃ', 'c');
		retVal = retVal.replace('ｄ', 'd');
		retVal = retVal.replace('ｅ', 'e');
		retVal = retVal.replace('ｆ', 'f');
		retVal = retVal.replace('ｇ', 'g');
		retVal = retVal.replace('ｈ', 'h');
		retVal = retVal.replace('ｉ', 'i');
		retVal = retVal.replace('ｊ', 'j');
		retVal = retVal.replace('ｋ', 'k');
		retVal = retVal.replace('ｌ', 'l');
		retVal = retVal.replace('ｍ', 'm');
		retVal = retVal.replace('ｎ', 'n');
		retVal = retVal.replace('ｏ', 'o');
		retVal = retVal.replace('ｐ', 'p');
		retVal = retVal.replace('ｑ', 'q');
		retVal = retVal.replace('ｒ', 'r');
		retVal = retVal.replace('ｓ', 's');
		retVal = retVal.replace('ｔ', 't');
		retVal = retVal.replace('ｕ', 'u');
		retVal = retVal.replace('ｖ', 'v');
		retVal = retVal.replace('ｗ', 'w');
		retVal = retVal.replace('ｘ', 'x');
		retVal = retVal.replace('ｙ', 'y');
		retVal = retVal.replace('ｚ', 'z');
		retVal = retVal.replace('～', '~');
		retVal = retVal.replace('｀', '`');
		retVal = retVal.replace('！', '!');
		retVal = retVal.replace('＠', '@');
		retVal = retVal.replace('＃', '#');
		retVal = retVal.replace('＄', '$');
		retVal = retVal.replace('％', '%');
		retVal = retVal.replace('＾', '^');
		retVal = retVal.replace('＆', '&');
		retVal = retVal.replace('＊', '*');
		retVal = retVal.replace('（', '(');
		retVal = retVal.replace('）', ')');
		retVal = retVal.replace('－', '-');
		retVal = retVal.replace('＿', '_');
		retVal = retVal.replace('＝', '=');
		retVal = retVal.replace('＋', '+');
		retVal = retVal.replace('［', '[');
		retVal = retVal.replace('］', ']');
		retVal = retVal.replace('｛', '{');
		retVal = retVal.replace('｝', '}');
		retVal = retVal.replace('＇', '\'');
		retVal = retVal.replace('＂', '"');
		retVal = retVal.replace('；', ';');
		retVal = retVal.replace('：', ':');
		retVal = retVal.replace('，', ',');
		retVal = retVal.replace('．', '.');
		retVal = retVal.replace('／', '/');
		retVal = retVal.replace('＜', '<');
		retVal = retVal.replace('＞', '>');
		retVal = retVal.replace('？', '?');
		retVal = retVal.replace('￦', '\\');
		retVal = retVal.replace('　', ' ');
		retVal = retVal.replace('｜', '|');

		return retVal;
	}

	/**
	 * 반각 -> 전각
	 * @param 반각
	 * @return 전각, null또는""입력시 ""리턴
	 */
	public static String replaceCharQuard(String fileCntn) {
		if(fileCntn == null || fileCntn.length() < 1) {
			return "";
		}

		String retVal = fileCntn.replace ('0','０');
		retVal = retVal.replace   ('1','１');
		retVal = retVal.replace   ('2','２');
		retVal = retVal.replace   ('3','３');
		retVal = retVal.replace   ('4','４');
		retVal = retVal.replace   ('5','５');
		retVal = retVal.replace   ('6','６');
		retVal = retVal.replace   ('7','７');
		retVal = retVal.replace   ('8','８');
		retVal = retVal.replace   ('9','９');
		retVal = retVal.replace   ('A','Ａ');
		retVal = retVal.replace   ('B','Ｂ');
		retVal = retVal.replace   ('C','Ｃ');
		retVal = retVal.replace   ('D','Ｄ');
		retVal = retVal.replace   ('E','Ｅ');
		retVal = retVal.replace   ('F','Ｆ');
		retVal = retVal.replace   ('G','Ｇ');
		retVal = retVal.replace   ('H','Ｈ');
		retVal = retVal.replace   ('I','Ｉ');
		retVal = retVal.replace   ('J','Ｊ');
		retVal = retVal.replace   ('K','Ｋ');
		retVal = retVal.replace   ('L','Ｌ');
		retVal = retVal.replace   ('M','Ｍ');
		retVal = retVal.replace   ('N','Ｎ');
		retVal = retVal.replace   ('O','Ｏ');
		retVal = retVal.replace   ('P','Ｐ');
		retVal = retVal.replace   ('Q','Ｑ');
		retVal = retVal.replace   ('R','Ｒ');
		retVal = retVal.replace   ('S','Ｓ');
		retVal = retVal.replace   ('T','Ｔ');
		retVal = retVal.replace   ('U','Ｕ');
		retVal = retVal.replace   ('V','Ｖ');
		retVal = retVal.replace   ('W','Ｗ');
		retVal = retVal.replace   ('X','Ｘ');
		retVal = retVal.replace   ('Y','Ｙ');
		retVal = retVal.replace   ('Z','Ｚ');
		retVal = retVal.replace   ('a','ａ');
		retVal = retVal.replace   ('b','ｂ');
		retVal = retVal.replace   ('c','ｃ');
		retVal = retVal.replace   ('d','ｄ');
		retVal = retVal.replace   ('e','ｅ');
		retVal = retVal.replace   ('f','ｆ');
		retVal = retVal.replace   ('g','ｇ');
		retVal = retVal.replace   ('h','ｈ');
		retVal = retVal.replace   ('i','ｉ');
		retVal = retVal.replace   ('j','ｊ');
		retVal = retVal.replace   ('k','ｋ');
		retVal = retVal.replace   ('l','ｌ');
		retVal = retVal.replace   ('m','ｍ');
		retVal = retVal.replace   ('n','ｎ');
		retVal = retVal.replace   ('o','ｏ');
		retVal = retVal.replace   ('p','ｐ');
		retVal = retVal.replace   ('q','ｑ');
		retVal = retVal.replace   ('r','ｒ');
		retVal = retVal.replace   ('s','ｓ');
		retVal = retVal.replace   ('t','ｔ');
		retVal = retVal.replace   ('u','ｕ');
		retVal = retVal.replace   ('v','ｖ');
		retVal = retVal.replace   ('w','ｗ');
		retVal = retVal.replace   ('x','ｘ');
		retVal = retVal.replace   ('y','ｙ');
		retVal = retVal.replace   ('z','ｚ');
		retVal = retVal.replace   ('~','～');
		retVal = retVal.replace   ('`','｀');
		retVal = retVal.replace   ('!','！');
		retVal = retVal.replace   ('@','＠');
		retVal = retVal.replace   ('#','＃');
		retVal = retVal.replace   ('$','＄');
		retVal = retVal.replace   ('%','％');
		retVal = retVal.replace   ('^','＾');
		retVal = retVal.replace   ('&','＆');
		retVal = retVal.replace   ('*','＊');
		retVal = retVal.replace   ('(','（');
		retVal = retVal.replace   (')','）');
		retVal = retVal.replace   ('-','－');
		retVal = retVal.replace   ('_','＿');
		retVal = retVal.replace   ('=','＝');
		retVal = retVal.replace   ('+','＋');
		retVal = retVal.replace   ('[','［');
		retVal = retVal.replace   (']','］');
		retVal = retVal.replace   ('{','｛');
		retVal = retVal.replace   ('}','｝');
		retVal = retVal.replace   ('\'', '＇');
		retVal = retVal.replace   ('"','＂');
		retVal = retVal.replace   (';','；');
		retVal = retVal.replace   (':','：');
		retVal = retVal.replace   (',','，');
		retVal = retVal.replace   ('.','．');
		retVal = retVal.replace   ('/','／');
		retVal = retVal.replace   ('<','＜');
		retVal = retVal.replace   ('>','＞');
		retVal = retVal.replace   ('?','？');
		retVal = retVal.replace   ('\\','￦');
		retVal = retVal.replace   (' ','　');
		retVal = retVal.replace   ('|','｜');
		return retVal;
	}
}
