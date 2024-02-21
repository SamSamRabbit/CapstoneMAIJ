package test_case3;

@critical
public class ContactNos_1 {

	public String name;
	@secrecy
	public String areaCode;
	@secrecy
	public String officeNo;
	
	public void SetName(String _name) {
		this.name = _name;
	}
	
	public String GetName() {
		return name;
	}
	
	public void SetTelephone(String _area, String _office) {
		this.areaCode = _area;
		this.officeNo = _office;
	}
	
	public void SetArea(String _area) {
		this.areaCode = _area;
	}
	
	public void SetOffice(String _office) {
		this.officeNo = _office;
	}
	
	public String GetTelephone() {
		return "Area Code: " + areaCode + "\n" + "Office Number: " + officeNo;
	}
	
	public String GetArea() {
		return areaCode;
	}
	
	public String GetOffice() {
		return officeNo;
	}
}
