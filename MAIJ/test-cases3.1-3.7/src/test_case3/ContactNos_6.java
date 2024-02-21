package test_case3;

@critical
public class ContactNos_6 {

	private String name;
	@secrecy
	private String teleNo;
	
	public void SetDetails(String _name, String _teleNo) {
		this.name = _name;
		this.teleNo = _teleNo;
	}
	
	public String[] GetDetails() {
		String[] s = {name, teleNo};
		return s;
	}
}
