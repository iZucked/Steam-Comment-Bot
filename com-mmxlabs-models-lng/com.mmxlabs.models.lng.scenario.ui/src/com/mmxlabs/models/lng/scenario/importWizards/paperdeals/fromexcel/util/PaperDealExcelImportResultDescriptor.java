package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

public class PaperDealExcelImportResultDescriptor {
	public PaperDealExcelImportResultDescriptor(String paperDealName, int rowNumber, int columnNumber, String message) {
		super();
		this.paperDealName = paperDealName;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		this.message = message;
	}
	public String paperDealName;
	public int rowNumber;
	public int columnNumber;
	public String message;
}
