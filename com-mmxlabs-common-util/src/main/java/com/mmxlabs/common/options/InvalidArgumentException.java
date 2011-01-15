/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.common.options;


public class InvalidArgumentException extends OptionsException {

	private String message;
	private String option;

	public InvalidArgumentException(String string) {
		this.message = string;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6971609184244683139L;

	public void setOption(String opt) {
		this.option = opt;
	}

	@Override
	public String getMessage() {
		return "Invalid argument for " + option + " : " + message;
	}

	
	
}
