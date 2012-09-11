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
	
	private final String text;
	
	private ColumnType(final String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}

