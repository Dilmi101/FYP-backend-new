package com.fyp.ehb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EhbUtil {
	
    public static final String TIME_ZONE = "Asia/Colombo";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	public static synchronized String computeSHA256Hash(String value){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(value.getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized Date getZonedDateTime(Date date, String timeZone) {
		try {
			
			TimeZone zone = TimeZone.getTimeZone(timeZone);
			if("GMT".equalsIgnoreCase(zone.getID())) {
				zone = TimeZone.getTimeZone(TIME_ZONE);
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(zone);
			
			String dateStr = df.format(date);
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static synchronized Date dateFromString(String dateString, String format) throws ParseException {
		Date date;
		if("yyyy-MM-dd HH:mm:ss".equalsIgnoreCase(format)) {
			date = new SimpleDateFormat(ISO_DATE_FORMAT).parse(dateString);
		}else if("yyyy-MM".equalsIgnoreCase(format)) {
			date = new SimpleDateFormat("yyyy-MM").parse(dateString);
		}else if("yyyy".equalsIgnoreCase(format)) {
			date = new SimpleDateFormat("yyyy").parse(dateString);
		}else if("yyyy-MM-dd ~".equalsIgnoreCase(format)) {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		}else if("dd-MM-yyyy-Echannelling".equalsIgnoreCase(format)) {
			date = new SimpleDateFormat("HH:mm dd-MM-yyyy E").parse(dateString);
		} else if("dd-MM-yyyy-Lending-Bill-Presentment".equalsIgnoreCase(format)) {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
		}
		else {
			date = new SimpleDateFormat(ISO_DATETIME_FORMAT).parse(dateString);
		}

		return date;

	}
}
