/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.options;

public class InvalidArgumentException extends OptionsException {

	private final String message;
	private String option;

	public InvalidArgumentException(final String string) {
		this.message = string;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6971609184244683139L;

	public void setOption(final String opt) {
		this.option = opt;
	}

	@Override
	public String getMessage() {
		return "Invalid argument for " + option + " : " + message;
	}

}
