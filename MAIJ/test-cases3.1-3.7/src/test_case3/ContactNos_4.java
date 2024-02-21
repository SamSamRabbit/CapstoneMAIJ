package test_case3;

@critical
public class ContactNos_4 {
	
	private String name;
	@secrecy
	private String areaCode;
	@secrecy
	private String officeNo;
	
	public void SetName(String _name) {
		this.name = _name;
	}
	
	public String GetName() {
		return name;
	}
	
	public void SetTelephone(Integer _area, Integer _office) {
		this.areaCode = ParseIntToString(_area);
		this.officeNo = ParseIntToString(_office);
	}
	
	public String GetTelephone() {
		return "Area Code: " + areaCode + "\n" + "Office Number: " + officeNo;
	}
	
	private void SetArea(Integer _area) {
		this.areaCode = ParseIntToString(_area);
	}
	
	private void SetOffice(Integer _office) {
		this.officeNo = ParseIntToString(_office);
	}
	
	private String GetArea() {
		return areaCode;
	}
	
	private String GetOffice() {
		return officeNo;
	}
	
	private String ParseIntToString(Integer _int) {
		return Integer.toString(_int);
	}
}
