package Backend;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;            //import the SimpleDateFormat class from the java text package

public class DateUtil {
	
	/*The java.text.SimpleDateFormat class provides methods to format and parse date and time in java.*/
	
	public static Date toDate(String dateAsString) {  //The method takes a string object and converts it to date
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return df.parse(dateAsString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String toString(Date date) {      
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
		
		return df.format(date);                     //this method takes a date object and coverts it to string 
		
		
	}

	public static String getDateForDB(Date date)	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);                     // the method gets the format in yyyy/mm/dd as sql requires the format that way
	}
	
	public static String getYearAndMonth(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM");   
		return df.format(date);                                 //returns the month and year of the string/date
	}
	
	public static String getYear(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");   
		return df.format(date);                                  //returns just the year of the date 
	}
	
	

}
