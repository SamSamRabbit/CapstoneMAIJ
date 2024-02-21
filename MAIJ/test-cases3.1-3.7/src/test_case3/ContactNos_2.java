package test_case3;

@critical
public class ContactNos_2 {

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
	
	private void SetName(String _name) {
		this.name = _name;
	}
	
	private String GetName() {
		return name;
	}
	
	private void SetArea(String _area) {
		this.areaCode = _area;
	}
	
	private void SetOffice(String _office) {
		this.officeNo = _office;
	}
	
	private String GetArea() {
		return areaCode;
	}
	
	private String GetOffice() {
		return officeNo;
	}
}
