package test_case3;

@critical
public class ContactNos_5 {

	private String name;
	@secrecy
	private String areaCode;
	@secrecy
	private String officeNo;
	
	public void SetDetails(String _name, String _area, String _office) {
		this.name = _name;
		this.areaCode = _area;
		this.officeNo = _office;
	}
	
	public String[] GetDetails() {
		String[] s = {name, areaCode, officeNo};
		return s;
	}
}
