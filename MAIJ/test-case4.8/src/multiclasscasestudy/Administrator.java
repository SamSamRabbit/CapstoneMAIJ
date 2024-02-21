package multiclasscasestudy;

public class Administrator extends Staff{

	private String jobTitle;
	
	private Administrator(Name name, String jobTitle) {
		super(name, jobTitle);
		this.jobTitle = jobTitle;
	}
	
	public void SetJobTitle(String jobTitle) {
		
		this.jobTitle = jobTitle;
	}
	
	public String GetJobTitle() {
		
		return jobTitle;
	}
}
