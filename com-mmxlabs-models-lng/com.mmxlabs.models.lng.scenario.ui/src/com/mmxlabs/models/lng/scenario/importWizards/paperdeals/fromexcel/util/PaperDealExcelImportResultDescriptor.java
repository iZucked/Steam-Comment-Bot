package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

public class PaperDealExcelImportResultDescriptor {
	
	public enum MessageType{
		INFO,
		ERROR
	}
	
	public enum MessageContext{
		PAPER_DEAL,
		COMMODITY_CURVE
	}
	
	public PaperDealExcelImportResultDescriptor(MessageType type, String paperDealName, int rowNumber, int columnNumber, String message) {
		super();
		this.type = type;
		this.paperDealName = paperDealName;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		this.message = message;
	}
	public MessageType type;
	public String paperDealName;
	public int rowNumber;
	public int columnNumber;
	public String message;
	
	public String getPaperDealName() {
		return paperDealName;
	}
	
	public MessageType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
