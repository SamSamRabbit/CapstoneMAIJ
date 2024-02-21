package test_case3;

@critical
public class ContactNos_3 {

	private String name;
	@secrecy
	private String areaCode;
	@secrecy
	private String officeNo;
	
	public void SetDetails(String _name, Integer _area, Integer _office) {
		this.name = _name;
		this.areaCode = ParseIntToString(_area);
		this.officeNo = ParseIntToString(_office);
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
