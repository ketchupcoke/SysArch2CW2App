package sysarch;

public class IPAddress {
	
	private String ipNumber;
	private String domainName;
	private String location;
	private String postcode;
	private String network;
	private String company;
	private String coords;
	
	public IPAddress(String setIPNumber, String setDomainName) {
		ipNumber = setIPNumber;
		domainName = setDomainName;
	}
	
	
	
	// Setters
	
	public void setIPNumber(String setIPNumber) {
		ipNumber = setIPNumber;
	}
	
	public void setDomainName(String setDomainName) {
		domainName = setDomainName;
	}
	
	public void setLocation(String setLocation) {
		location = setLocation;
	}
	
	public void setCoords(String setCoords) {
		coords = setCoords;
	}
	
	public void setPostcode(String setPostcode) {
		postcode = setPostcode;
	}
	
	public void setNetwork(String setNetwork) {
		network = setNetwork;
	}
	
	public void setCompany(String setCompany) {
		company = setCompany;
	}
	
	
	
	// Getters
	
	public String getIPNumber() {
		return ipNumber;
	}
	
	public String getDomainName() {
		return domainName;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getCoords() {
		return coords;
	}
	
	public String getPostcode() {
		return postcode;
	}
	
	public String getNetwork() {
		return network;
	}
	
	public String getCompany() {
		return company;
	}
	
}
