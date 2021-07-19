package Frontend;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Backend.DateUtil;
import Backend.LoadAndLog;
import Backend.Repository;
import budget.Category;
import budget.Expense;
import budget.Interfacetest;
import budget.ReadInput;
import budget.ReportService;

public class BudgetManager implements Interfacetest {
	private ReadInput input = new ReadInput();
	
	private Repository repo = Repository.getRepository();   //is a singleton object 
	//always returns only a single object with every call
	
	ReportService reportservice = new ReportService();

	LoadAndLog dbms = new LoadAndLog();
	
//	public BudgetManager() {                         //default constructor
//		prepareSampleData();
//	}
	
	private int choice;

	public void logUser()	{                        //is used to login the users into the program and database
		boolean signedIn = false;
		while (!signedIn)	{
			System.out.println("1. Sign In");
			System.out.println("2. Sign Up");
			System.out.print("Enter your choice: ");
			choice = input.readInt("li");
			switch (choice) {
				case 1 -> signedIn = dbms.signIn();     
				case 2 -> signedIn = dbms.signUp();
			}
			if (!signedIn && choice == 1)	{System.out.println("Couldn't sign in, please try again.");}
			else if (!signedIn && choice == 2)	{System.out.println("Couldn't sign up, please try again.");}
		}
		dbms.loadDB();
	}

	public void displayMenu() {
		logUser();
		while(true) {                       //runs in an infinite loop 
			printMenu();
			
			switch(choice) {
				case 1:
					addCategory();
					pressAnyKeyToContinue();
					break;
					
				case 2:
					categoryList();
					pressAnyKeyToContinue();
					break;
					
				case 3:
					expenseEntry();
					pressAnyKeyToContinue();
					break;	
					
				case 4:
					expenseList();
					pressAnyKeyToContinue();
					break;	
					
				case 5:
					monthlyExpense();
					pressAnyKeyToContinue();
					break;
					
				case 6:
					yearlyExpense();
					pressAnyKeyToContinue();
					break;	
					
				case 7:
					categoryExpense();
					pressAnyKeyToContinue();
					break;
					
				case 8:
					logOut();
					break;	
			}
		}
	}
	
	

	public void printMenu() {
		System.out.println("Welcome to the Budget Manager :)");
		System.out.println("------------Budget Manager Menu------------"); 
		System.out.println("1.Add Category");
		System.out.println("2.Category List");
		System.out.println("3.Enter Expense");
		System.out.println("4.Expense List");
		System.out.println("5.Monthly Expense");
		System.out.println("6.Yearly Expense");
		System.out.println("7.Category wise Expense");
		System.out.println("8.Log Out");
		System.out.println("-------------------------------------------");
		System.out.print("Enter your choice: ");
		choice = input.readInt("ui");
	}
	
	
	
	
	
	
	
	public void pressAnyKeyToContinue() {
		System.out.println("Press any key to continue");
		try {
			System.in.read();                        //reads any key from the user 
		} catch (IOException e) {                    //use of try-catch block 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addCategory() {
		System.out.print("Enter Category Name:");
		String catName = input.readLine("cat");
		Category cat = new Category(catName);
		repo.catList.add(cat);                      // adds the category object to the category list in the repository 
		System.out.println("Category Added:"+catName);
	}
	
	public void categoryList() {
		System.out.println("Listing category");
		List<Category> clist = repo.catList;     //get category list by creating a duplicate list
		for(int i=0; i<clist.size(); i++) {
			Category c = clist.get(i);           //get each category object from the list 
			System.out.println((i+1)+", "+c.getName()+ ", " + c.getCategoryID());
		}
	}
	
	public void expenseEntry() {
		System.out.println("Enter Expense details:");
		categoryList();
		System.out.println("Enter the category:");
		int catChoice = input.readInt("exp");
		
		Category selectedCat = repo.catList.get(catChoice-1);//index (catChoice-1) gives the index of the selected category
		System.out.println("My Category:"+ selectedCat.getName());
		
		System.out.println("Enter Amount:");
		float amount = input.readFloat("exp");
		
		System.out.println("Enter Remark:");
		String remark = input.readLine("exp");
		
		System.out.println("Enter Date(DD/MM/YYYY):");
		String dateAsString = input.readLine("expd");
		Date date = DateUtil.toDate(dateAsString);     //converting the string input to Date format by using DateUtil class
		
		//create expense object 
		Expense ex = new Expense();
		ex.setCategoryID(selectedCat.getCategoryID());
		ex.setAmount(amount);
		ex.setDate(date);
		ex.setRemarks(remark);
		
		//storing expense object in repository
		repo.expList.add(ex);
		
		
		 class Threading extends Thread{                //class to show inheritance and threading 
		    	
		    	public void run() {                     // thread.start() executes this method
					try {
						dbms.insertExpense(ex);         //inserts expense into the database
					}
					catch(Exception e) {
						System.out.println("Exception caught");
					}
				}	
			}
			
		 Threading thread = new Threading();
			thread.start();                              //starts the run method and inserts the expense into the database
		
		
		System.out.println("Expense Added.");
	}
	
	public void expenseList() {
		// TODO Auto-generated method stub
		System.out.println("Listing Expense");
		List<Expense> expList = repo.expList;
		for(int i=0; i<expList.size(); i++) {              //traverse through the expense list by using a for loop 
			Expense exp = expList.get(i);
			
			//the method getCategoryNameById gets the name of the category by it's id
			String catName = reportservice.getCategoryNameById(exp.getCategoryID());  
			
			 //converting the date type to string type using the DateUtil class
			String dateString = DateUtil.getDateForDB(exp.getDate());   
			
			System.out.println((i+1)+", "+ exp.getExpenseID()+", "+exp.getCategoryID()+", "+catName+", "+exp.getAmount()+", "+exp.getRemarks()+", "+dateString);
		}
	}
	
	public void categoryExpense() {
		System.out.println("Calculating category wise expense");
		
		// get a map of the result of the calculated categorized expenses
		Map<String, Float>resultMapOfCategories = reportservice.calculateCategorizedExpenditure(); 
		Set<String> categories = resultMapOfCategories.keySet();        //gets the set of keys in the map into Set<String>
		Float netTotal = 0.0F;
		for(String categoryName : categories) {
			Float catWiseTotal = resultMapOfCategories.get(categoryName);
			netTotal = netTotal + catWiseTotal;                         //to get the net total spent on a category 
			System.out.println(categoryName + ":" + resultMapOfCategories.get(categoryName));
		}
		
		System.out.println("----------------------------------------");
		System.out.println("Net Total:"+netTotal);
		
	}

	public void yearlyExpense() {
		
		System.out.println("Calculating yearly expense");
		Map<String, Float>resultMapOfYear = reportservice.calculateYearlyExpenditure();
		Set<String> keys = resultMapOfYear.keySet();                   //gets the set of keys of the result map from calculating the yearly expenditure
		Float total = 0.0F;
		for(String year : keys) {
			Float exp = resultMapOfYear.get(year);                     // we get the total by adding the exp of each year by getting the keys 
			total = total+ exp;
			System.out.println(year + ": " + resultMapOfYear.get(year));
		}
		System.out.println("-----------------------------------------");
		System.out.println("Total Expenses:"+total);
		
	}

	public void monthlyExpense() {
		
		System.out.println("Calculating monthly expense");
		Map<String,Float>resultMap = reportservice.calculateMonthlyExpenditure();
		Set<String> keys = resultMap.keySet();
		for(String yearAndMonth : keys) {
			
			System.out.println(yearAndMonth+ ": "+resultMap.get(yearAndMonth));
		}
		
	}


	public void logOut() {
		System.out.println("Logging out...");
		delay();
		System.out.println("You have been successfully logged out");
		System.exit(0);
	}	
	
	public void delay() {                               // used to create some unique ids 
		try {
			Thread.sleep(10);                          //use of threading/thread.sleep
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*public void prepareSampleData() {
		Category catRent = new Category("Rent");               //Prepare sample data for 5 objects 
		delay();                                               //using delay to get unique ids as milliseconds is used as ids
		Category catShop = new Category("Shopping");
		delay();
		Category catBill = new Category("Bills");
		delay();
		Category catFood = new Category("Food");
		delay();
		Category catFuel = new Category("Fuel");
		
		
		
		repo.catList.add(catRent);
		repo.catList.add(catShop);
		repo.catList.add(catBill);
		repo.catList.add(catFood);
		repo.catList.add(catFuel);
		
		//January 2020
		Expense e1 = new Expense(catRent.getCategoryID(), 15000.0F, DateUtil.toDate("01/01/2020"),"N/A");
		Expense e2 = new Expense(catShop.getCategoryID(), 3000.0F, DateUtil.toDate("04/01/2020"),"N/A");
		//Feb 2020
		Expense e3 = new Expense(catBill.getCategoryID(), 4000.0F, DateUtil.toDate("05/02/2020"),"N/A");
		Expense e4 = new Expense(catRent.getCategoryID(), 15000.0F, DateUtil.toDate("01/02/2020"),"N/A");
		//Mar 2020s
		Expense e5 = new Expense(catRent.getCategoryID(), 15000.0F, DateUtil.toDate("01/03/2020"),"N/A");
		Expense e6 = new Expense(catFuel.getCategoryID(), 2000.0F, DateUtil.toDate("07/03/2020"),"N/A");
		//April 2020
		Expense e7 = new Expense(catRent.getCategoryID(), 15000.0F, DateUtil.toDate("01/04/2020"),"N/A");
	    Expense e8 = new Expense(catFood.getCategoryID(), 3000.0F, DateUtil.toDate("07/04/2020"),"N/A");
	    
	    
	   //January 2021
	  	Expense e9 = new Expense(catRent.getCategoryID(), 16000.0F, DateUtil.toDate("11/01/2021"),"N/A");
	  	Expense e10 = new Expense(catBill.getCategoryID(), 4000.0F, DateUtil.toDate("07/01/2021"),"N/A");
	  	//Feb 2021
	  	Expense e11 = new Expense(catShop.getCategoryID(), 7000.0F, DateUtil.toDate("15/02/2021"),"N/A");
	  	Expense e12 = new Expense(catRent.getCategoryID(), 16000.0F, DateUtil.toDate("23/02/2021"),"N/A");
	  	//Mar 2021
	  	Expense e13 = new Expense(catRent.getCategoryID(), 16000.0F, DateUtil.toDate("17/03/2021"),"N/A");
	  	Expense e14 = new Expense(catFood.getCategoryID(), 2000.0F, DateUtil.toDate("28/03/2021"),"N/A");
	  	//April 2021
	  	Expense e15 = new Expense(catRent.getCategoryID(), 16000.0F, DateUtil.toDate("19/04/2021"),"N/A");
	  	Expense e16 = new Expense(catFuel.getCategoryID(), 5000.0F, DateUtil.toDate("22/04/2021"),"N/A");
	    
	    repo.expList.add(e1);
	    repo.expList.add(e2);
	    repo.expList.add(e3);
	    repo.expList.add(e4);
	    repo.expList.add(e5);
	    repo.expList.add(e6);
	    repo.expList.add(e7);
	    repo.expList.add(e8);
	    repo.expList.add(e9);
	    repo.expList.add(e10);
	    repo.expList.add(e11);
	    repo.expList.add(e12);
	    repo.expList.add(e13);
	    repo.expList.add(e14);
	    repo.expList.add(e15);
	    repo.expList.add(e16);
	    
	}*/
	
	
	

	
	
	
   
	
	
	
}	


	

