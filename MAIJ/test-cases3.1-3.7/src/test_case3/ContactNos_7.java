package test_case3;

@critical
public class ContactNos_7 {

	private String name;
	@secrecy
	private String teleNo;
	
	public void SetName(String _name) {
		this.name = _name;
	}
	
	public String GetName() {
		return name;
	}
	
	public void SetTelephone(String _teleNo) {
		this.teleNo = _teleNo;
	}
	
	public String GetTelephone() {
		return teleNo;
	}
}
