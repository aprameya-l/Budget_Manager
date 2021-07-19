package budget;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import Backend.DateUtil;
import Backend.Repository;



public class ReportService {
	Repository repo = Repository.getRepository();
	
	public Map<String,Float> calculateMonthlyExpenditure(){
		Map<String,Float> m = new TreeMap();
		for(Expense exp : repo.expList) {
			Date expDate = exp.getDate();
			String yearAndMonth = DateUtil.getYearAndMonth(expDate);
			
			if(m.containsKey(yearAndMonth)) {                //check if the map already contains the month and year
				Float total = m.get(yearAndMonth);           //the total will be the already present object 
				total = total + exp.getAmount();            //new total will be calculated by adding the expense amount 
				m.put(yearAndMonth, total);      //will be replaced by new total 
			}
			else {                                          //if a new month and year is added 
				m.put(yearAndMonth, exp.getAmount());
			}
			
		}
		return m;
	}
	
	public Map<String,Float> calculateYearlyExpenditure(){
		Map<String,Float> m = new TreeMap();
		for(Expense exp : repo.expList) {
			Date expDate = exp.getDate();
			String year = DateUtil.getYear(expDate);
			
			if(m.containsKey(year)) {                //check if the map already contains the year
				Float total = m.get(year);           //the total will be the already present object 
				total = total + exp.getAmount();            //new total will be calculated by adding the expense amount 
				m.put(year, total);      //will be replaced by new total 
			}
			else {                                          //if a new year is added 
				m.put(year, exp.getAmount());
			}
			
		}
		return m;
	}
	
	public Map<String,Float> calculateCategorizedExpenditure(){
		Map<String,Float> m = new TreeMap();                  //create a new treemap
		for(Expense exp : repo.expList) {               
			long categoryID = exp.getCategoryID();                
			String catName = this.getCategoryNameById(categoryID); //get category name
			if(m.containsKey(catName)) {                
				Float total = m.get(catName);            //if this category is already present in the map just add to it
				total = total + exp.getAmount();            
				m.put(catName, total);                  //m.put --> It is used to insert an entry in the map.
			}
			else {                                          
				m.put(catName, exp.getAmount());          //if new category is added 
			}
			
		}
		return m;
	}
	
	public String getCategoryNameById(Long categoryId) {
		for(Category c : repo.catList) {             //get each category in the list 
			if(c.getCategoryID().equals(categoryId)) {
				return c.getName();                  //if it matches with the selected category then return it
			}
			
		}
		return null; //if no such category exists 
	}
}
