package Backend;

import java.util.ArrayList;
import java.util.List;

import budget.Category;
import budget.Expense;

/* The class Repository is used like a database and it is declared as a singleton class*/

public class Repository {
	public List<Expense> expList = new ArrayList();    //declared public as it can be used elsewhere 
	public List<Category> catList = new ArrayList();
	private static Repository repository;
	private Repository(){            //we should not let multiple objects of the repo to be created 
                                     //this is why we create a singleton class		
	}
	
	public static Repository getRepository() {
		if(repository==null) {                //if the object is null we create a new object
			repository = new Repository();
		}
		return repository;                     // and return it.
	}
}