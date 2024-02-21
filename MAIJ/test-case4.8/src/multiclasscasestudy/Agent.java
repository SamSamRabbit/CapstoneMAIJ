package multiclasscasestudy;

@critical
public class Agent extends Staff{

	public Agent(Name name, String jobTitle) {
		super(name, jobTitle);
	}

	@secrecy
	private Clearance clearance;
	
	public void SetClearance(Clearance clearance) {
		
		this.clearance = clearance;
	}
	
	public String GetClearance() {
		
		return clearance.GetClearance();
	}
}
