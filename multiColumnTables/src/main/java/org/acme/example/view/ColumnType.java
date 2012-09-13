package org.acme.example.view;

public enum ColumnType {
	ID("ID"),
	TITLE("Title"),
	FSW_NAME("FSW Name"),
	RAW("Raw"),
	VALUE("Value"),
	UNIT("Unit"),
	ERT("ERT"),
	SCLK("SCLK"),
	SCET("SCET");
		
	private ColumnType(final String text) {
	}
	
	public static String getDisplayName(String name) {
		if(name.equals("ID"))
			return "ID";
		else if(name.equals("TITLE"))
			return "Title";
		else if(name.equals("FSW_NAME"))
			return "FSW Name";
		else if(name.equals("RAW"))
			return "Raw";
		else if(name.equals("VALUE"))
			return "Value";
		else if(name.equals("UNIT"))
			return "Unit";
		else if(name.equals("ERT"))
			return "ERT";
		else if(name.equals("SCLK"))
			return "SCLK";
		else if(name.equals("SCET"))
			return "SCET";
		else
			return "(unknown type)";
	}
	
}

