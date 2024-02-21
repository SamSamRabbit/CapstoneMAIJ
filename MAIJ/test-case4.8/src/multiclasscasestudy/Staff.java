package multiclasscasestudy;

@critical
public class Staff {

	protected Name name;
	
	@secrecy
	protected String jobTitle;
	
	public Staff(Name name, String jobTitle) {
		
		this.name = name;
		this.jobTitle = jobTitle;
	}
	
	public String GetName() {
		
		return name.GetName();
	}
	
	public void SetJobTitle(String jobTitle) {
		
		this.jobTitle = jobTitle;
	}
	
	public String GetJobTitle() {
		
		return jobTitle;
	}
	
}
