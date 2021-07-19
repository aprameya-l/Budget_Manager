package budget;

import java.util.Date;

public class Expense {
	private long expenseID = System.currentTimeMillis();     //creates random numbers which is used as expense id
	private long categoryID;
	private Date date;
	private String remarks;
	private float amount;
	
	public Expense(long categoryID, float amount, Date date, String remark) {  //constructor
		this.amount = amount;
		this.date = date;
		this.remarks = remark;
		this.categoryID = categoryID;
	}

	public Expense(long expenseID,long categoryID, float amount, Date date, String remark)	{
		this.amount = amount;
		this.expenseID = expenseID;
		this.date = date;
		this.remarks = remark;
		this.categoryID = categoryID;
	}
	
	public Expense() {
		                                       //default constructor
	}
	
	
	//declaration of getters and setters 
	
	public long getCategoryID() {
		return categoryID;
	}
	
	public void setExpenseID(long expenseID) {
		this.expenseID = expenseID;
	}
	
	public long getExpenseID() {
		return expenseID;
	}
	
	public void setCategoryID(long categoryID) {
		this.categoryID = categoryID;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public float getAmount() {
		return amount;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
}
