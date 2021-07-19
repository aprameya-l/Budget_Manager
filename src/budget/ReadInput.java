package budget;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Backend.Repository;

/*
Should be called as: <object_name>.read<Int|Float|String>(string: <value>)
<value> can be:
        "udb" to read userName from the LoadAndLog object
        "pdb" to read password from the LoadAndLog object
        "li" to read inputs from the logUser()
        "ui" to read inputs from the printMenu()
        "cat" to read inputs from the addCategory()
        "exp" to read inputs from the expenseEntry()
        "expd" to read date inputs from expenseEntry()
*/

public class ReadInput {
    private static Scanner input = new Scanner(System.in);

    private static class UIReadException extends Exception {
    	@Override
        public String getMessage()   {                                     //method overriding shown 
            return "The input value should be greater than 0 and less than or equal to 8.";
        }
    }

    private static class LIReadException extends Exception  {
        public void getErrorInfo()  {
            System.out.println("The input value should be greater than 0 and less than or equal to 2.");
        }
    }

    private static class CategoryListOutOfBound extends Exception {
        public void getErrorInfo()  {
            Repository repo = Repository.getRepository();
            int size = repo.catList.size();
            if (size == 0)  {
                System.out.println("Category list is empty.");
            }
            else    {
                System.out.println("The input value should be greater than 0 and less than or equal to " + size);
            }
        }
    }

    private static class HasSpecialCharacter extends Exception   {
        public void getErrorInfo()  {
            System.out.println("The input should not have any special characters except \"()\".");
        }
    }

    private static class EmptyString extends Exception   {
        public String getErrorInfo()  {
           //System.out.println("The input should not be empty.");
        	return super.getMessage();                                 //"use" of "super" keyword
        }
    }

    private static class InvalidDateFormat extends Exception    {
        public void getErrorInfo() {
            System.out.println("The date should be in the form DD/MM/YYYY.");
        }
    }

    public int readInt(String type) {
        int value = 0;
        boolean correctInput = false;
        if (type.equals("ui") || type.equals("li"))   {
            while (!correctInput)   {
                try {
                	
                	
                	//we keep getting the input until the correctInput is true  
                    value = input.nextInt();
                    if (type.equals("ui") && (value < 1 || value > 8)) {
                    	
                    	//if the value given by the user for printMenu() is greater than 8 or less than 1, throws exception
                    	throw new UIReadException();               
                    	}
                    
                    if (type.equals("li") && (value < 1 || value > 2)) {
                    	
                    	//if the value given by the user for logUser is greater than 2 or less than 1, throws exception
                    	throw new LIReadException();
                    	}
                    
                    correctInput = true;
                }
                catch (java.util.InputMismatchException e)    {
                    System.out.println("The value should be an integer.");
                }
                catch (UIReadException e)   {
                    System.out.println(e.getMessage());
                }
                catch (LIReadException e)   {
                    e.getErrorInfo();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        else {
        	int size = Repository.getRepository().catList.size();
            while (!correctInput)   {
                try {
                    input.nextLine();
                    value = input.nextInt();
                    if (value < 1 || value > size) {
                    	//if the size of the input is less than 1 or greater than the size of the list, throws exception 
                    		throw new CategoryListOutOfBound();
                    	}
                    correctInput = true;
                }
                catch (java.util.InputMismatchException e)  {
                    System.out.println("The value should be a integer.");
                }
                catch (CategoryListOutOfBound e)   {
                    e.getErrorInfo();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return value;
    }

    public String readLine(String type) {
        boolean correctInput = false;
        String value = "";
        if (type.equals("expd"))   {
            String dd = "(0[1-9]|(1|2)\\d|3[0-1])";              //   \\d-->represents all digits
            String mm = "(0[1-9]|1[0-2])";
            String dateFormat = "^"+dd+"/"+mm+"/\\d\\d\\d\\d$";   // the string starts from ^ to $
            Pattern p = Pattern.compile(dateFormat);
            while (!correctInput) {
                try {
                    value = input.nextLine().trim();
                    Matcher m = p.matcher(value);
                    if (!m.find()) {                              // if the pattern is not found then throws exception 
                        throw new InvalidDateFormat();
                    }
                    correctInput = true;
                } catch (InvalidDateFormat e) {
                    e.getErrorInfo();
                }
                catch   (Exception e)   {
                    System.out.println(e);
                }
            }
        }
        else if (!type.substring(1).equals("db")) {
            Pattern p = Pattern.compile("[^a-z0-9() ]", Pattern.CASE_INSENSITIVE);
            input.nextLine();
            while (!correctInput)   {
                try {
                    value = input.nextLine().trim();
                    Matcher m = p.matcher(value);                   //
                    if (m.find()) {                                 //m.find() returns a boolean true if there is a special character in value
                    		throw new HasSpecialCharacter();        //if the string has special characters, throws exception 
                    	}
                    if (value.length() == 0) {
                    		throw new EmptyString();               //if the value length is zero throws exception 
                    	}
                    correctInput = true;
                }
                catch (HasSpecialCharacter e) {
                    e.getErrorInfo();
                }
                catch (EmptyString e)   {
                    e.getErrorInfo();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        else    {
            if (type.equals("udb")) {
            	
            	input.nextLine();              
            	
            }
            while (!correctInput)   {
                try {
                    value = input.nextLine().trim();
                    if (value.length() == 0) {
                    	
                    	throw new EmptyString();
                    	
                    }
                    correctInput = true;
                }
                catch (EmptyString e)   {
                    e.getErrorInfo();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return value;
    }

    public float readFloat(String type)  {
        boolean correctInput = false;
        float value = 0;
        while (!correctInput)   {
            try {
                input.nextLine();
                value = input.nextFloat();
                correctInput = true;
            }
            catch (Exception e) {
                System.out.println("The input should be in decimal number form.");
            }
        }
        return value;
    }
}
