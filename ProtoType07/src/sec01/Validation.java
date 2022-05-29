package sec01;

import java.util.*;

public class Validation {
	public static boolean dataValidate(HashMap<String, Object> hm, String data) {
		if(((Vector<String>)hm.get("validate1")).contains(data)) return false;
		return true;
	}
	public static boolean telValidate(HashMap<String, Object> hm, String tel) {
		if(((Vector<String>)hm.get("validate2")).contains(tel)) return false;
		return true;
	}
	public static boolean telFormValidate(String tel) {
		if(tel.matches("(010)-([1-9][0-9]{3})-([0-9]{4})")||tel.matches("(011)-([1-9][0-9]{2})-([0-9]{4})")) return true;
		return false;
	}
	public static boolean pwValidate(String pw) {
		if(pw.length() >= 8) return true;
		else return false;
	}
	public static boolean tblNameValidate(String tblName) {
		if(tblName.matches("^([a-z]+)TBL$")) return true;
		else return false;
	}
}
