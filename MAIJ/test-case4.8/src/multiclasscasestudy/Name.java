package multiclasscasestudy;

public class Name {
	
	private String firstName;
	private String lastName;
	
	
	public Name(String firstName, String lastName) {
		
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String GetName() {
		
		return firstName + " " + lastName;
	}

}
