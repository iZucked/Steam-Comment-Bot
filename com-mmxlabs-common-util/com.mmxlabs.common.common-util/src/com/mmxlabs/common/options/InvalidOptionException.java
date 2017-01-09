/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.options;

public class InvalidOptionException extends OptionsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1326382489991283946L;
	private final String option;

	@Override
	public String getMessage() {
		return "No Such Option: " + option;
	}

	public InvalidOptionException(final String option) {
		this.option = option;
	}

}
