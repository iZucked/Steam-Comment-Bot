/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util;

public class ExcelImportResultDescriptor {
	
	public enum MessageType{
		INFO,
		ERROR
	}
	
	public enum MessageContext{
		PAPER_DEAL("Paper deal"),
		COMMODITY_CURVE("Commodity curve");

		private final String value;
		
		MessageContext(String string) {
			value = string;
		}
		
		@Override
		public String toString() { return value; }
	}
	
	private MessageType type;
	private MessageContext context;
	private String paperDealName;
	private int rowNumber;
	private int columnNumber;
	private String message;
	
	public ExcelImportResultDescriptor(MessageType type, MessageContext context, String paperDealName, int rowNumber, int columnNumber, String message) {
		this.type = type;
		this.context = context;
		this.paperDealName = paperDealName;
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		this.message = message;
	}
	
	public ExcelImportResultDescriptor(MessageType type, MessageContext context, String message) {
		this(type, context, "", -1, -1, message);
	}

	public MessageType getType() {
		return type;
	}

	public MessageContext getContext() {
		return context;
	}

	public String getPaperDealName() {
		return paperDealName;
	}

	public int getRowNumber() {
		return rowNumber + 1;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public String getMessage() {
		return message;
	}

	
	
	@Override
	public String toString() {
		return message;
	}
}
