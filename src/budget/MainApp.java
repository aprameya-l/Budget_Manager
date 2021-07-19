package budget;

import Frontend.BudgetManager;

public class MainApp {
	public static void main(String[] args) {
		BudgetManager manager = new BudgetManager();            //create an new budgetmanager object 
		manager.displayMenu();                                  // displayMenu() displays the menu and starts the budgetmanager
	}
}
