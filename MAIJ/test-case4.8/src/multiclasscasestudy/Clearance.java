package multiclasscasestudy;

@critical
public class Clearance {

	@secrecy
	private String level;
	
	@secrecy
	private Integer number;
	
	public void SetLevel(String level) {
		
		this.level = level;
	}
	
	public void SetNumber(Integer number) {
		
		this.number = number;
	}
	
	public String GetClearance() {
		
		return level + " " + Integer.toString(number);
	}
}
