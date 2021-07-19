package budget;

public class Category {
	        private long categoryID = System.currentTimeMillis(); // returns the current time in milliseconds
			private String name;
			
			public Category(long categoryID, String name) {   //Constructor
				// TODO Auto-generated constructor stub
				this.name = name;                           //Use of this keyword  
				this.categoryID = categoryID;
			}
			
			public Category(String name) {
				this.name = name;
			}
			
			public Category() {
				// TODO Auto-generated constructor stub     //Default constructor
			}
			
			public Long getCategoryID() {                   //getters and setters
				return categoryID;
			}
			
			public String getName() {
				return name;
			}
			
			public void setCategoryID(long categoryID) {
				this.categoryID = categoryID;
			}
			
			public void setName(String name) {
				this.name = name;
			}
			
			

}
