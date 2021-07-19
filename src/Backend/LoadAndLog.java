package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import budget.Category;
import budget.Expense;
import budget.ReadInput;

public class LoadAndLog {
    private static Connection connection = getConnection();            //getConnection() is used to connect the database hosted on a server to this project
    private static int userID;

    private static Connection getConnection() {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://34.219.51.35:3306/budget_manager";
            String userName = "aprameya";
            String password = "9353883615";
            Class.forName(driver);                                     //To load the methods from an external library mysql-connector-java-8.0.25.jar which has the driver name "driver"
            Connection connection = DriverManager.getConnection(url, userName, password);   //this line connects to the database and returns an instance of Connection type
            System.out.println("Connected to database successfully.");
            return connection;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean signUp() {                                   //the class signUp 
        ReadInput input = new ReadInput();
        int count = 0;
        System.out.print("Enter userName: ");
        String userName = input.readLine("udb");
        System.out.print("Enter password: ");
        String password = input.readLine("pdb");
        
        
      //The sql command to get the user_id if there exists a row with the given username
        
        String command = String.format("SELECT user_id FROM users WHERE user_name = '%s';", userName);  
        try {
            PreparedStatement selectCommand = connection.prepareStatement(command);  //uploading this command to the database sql command space
            ResultSet result = selectCommand.executeQuery();            //Since this command returns a table it will be assigned to a ResultSet instance
            while  (result.next())  {count++;};                         //result.next() is used to traverse all the rows we get from the database
        }
        catch (Exception e) {
            System.out.println(e);
        }
        if (count != 0) {return false;}                              // if count>0, there already exist a user with the same username, hence the program won't allow to create a new user 
        else    {
            boolean createdNew = false;
            
            //is used to insert a new username and password
            command = String.format("INSERT INTO users (user_name, password) VALUES ('%s', '%s');", userName, password);
            try {
                PreparedStatement insertCommand = connection.prepareStatement(command);
                
              //since this command doesn't return anything, instead adds a new row in the table users we don't have to assign it to ResultSet
                insertCommand.executeUpdate();                        
                command = String.format("SELECT user_id FROM users WHERE user_name='%s' AND password='%s';", userName, password);
                PreparedStatement selectCommand = connection.prepareStatement(command);
                ResultSet result = selectCommand.executeQuery();
                while (result.next()) {   
                
                	userID = result.getInt("user_id");                  //we store the userID which will be used to get the data of the expenses made by that user
                }
                createdNew = true;
            }
            catch (Exception e) {
                System.out.println(e);
            }
            return createdNew;
        }
    }

    public boolean signIn() {
        ReadInput input = new ReadInput();
        System.out.print("Enter userName: ");
        String userName = input.readLine("udb");
        System.out.print("Enter password: ");
        String password = input.readLine("pdb");
        String command = String.format("SELECT user_id FROM users WHERE user_name = '%s' AND password = '%s';", userName, password);
        try {
            PreparedStatement selectCommand = connection.prepareStatement(command);
            ResultSet result = selectCommand.executeQuery();
            while (result.next()) {
                userID = result.getInt("user_id");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        
      //if the database doesn't return any table then all the values in the ResultSet instance will be zero
        if (userID != 0)    {return true;}                   
        else    {return false;}
    }

    public boolean insertExpense(Expense e) {
        boolean insertedSuccessfully = false;
        long expenseID = e.getExpenseID();
        long categoryID = e.getCategoryID();
        String categoryName = getCategoryNameById(e.getCategoryID());
        float amount = e.getAmount();
        String remarks = e.getRemarks();
        String date = DateUtil.getDateForDB(e.getDate());
        String command = String.format("INSERT INTO expenses (user_id, expense_id, category_id, category_name, amount, remarks, date) VALUES (%d, %d, %d, '%s', %f, '%s', '%s');", userID, expenseID, categoryID, categoryName, amount, remarks, date);
        try {
            PreparedStatement insertCommand = connection.prepareStatement(command);
            insertCommand.executeUpdate();
            insertedSuccessfully = true;
        } catch (Exception err) {
            System.out.println(err);
        }
        return insertedSuccessfully;
    }

    public void loadDB() {
        Repository repo = Repository.getRepository();
        String command = String.format("SELECT DISTINCT category_name, category_id FROM expenses WHERE user_id=%d", userID);
        try {
            PreparedStatement selectCommand = connection.prepareStatement(command);
            ResultSet  result = selectCommand.executeQuery();
            while (result.next())   {
                Category c = new Category(result.getLong("category_id"), result.getString("category_name"));
                repo.catList.add(c);                                              //for adding the category object to the list of categories
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        command = String.format("SELECT expense_id, category_id, amount, remarks, date FROM expenses WHERE user_id=%d;", userID);
        try {
            PreparedStatement selectCommand = connection.prepareStatement(command);
            ResultSet result = selectCommand.executeQuery();
            while(result.next())    {
                Expense e = new Expense(result.getLong("expense_id"), result.getLong("category_id"), result.getFloat("amount"),result.getDate("date") , result.getString("remarks"));
                repo.expList.add(e);                                             //for adding the expense object to the list of expenses
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
     String getCategoryNameById(Long categoryId) {
		for(Category c : Repository.getRepository().catList) {             //get each category in the list 
			if(c.getCategoryID().equals(categoryId)) {
				return c.getName();                  //if it matches with the selected category then return it
			}
			
		}
		return null; //if no such category exists 
	}

    
}
