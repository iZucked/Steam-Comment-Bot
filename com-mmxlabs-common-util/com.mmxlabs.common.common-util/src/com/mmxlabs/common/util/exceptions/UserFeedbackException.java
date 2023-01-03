/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.util.exceptions;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

/**
 * This subclass of {@link RuntimeException} is intended to be used to create
 * messages to display to the user about issues detected deep in the code stack.
 * try/catch handlers should largely pass it through to the upper levels until a
 * UI component can display it.
 * 
 * @author Simon Goodall
 *
 */
public class UserFeedbackException extends RuntimeException {
	private static final long serialVersionUID = 8927498775203781300L;

	private final transient List<Object> additionalInfo;

	public UserFeedbackException(@NonNull String message) {
		this(message, null);
	}

	public UserFeedbackException(@NonNull String message, List<Object> additionalInfo) {
		super(message);
		this.additionalInfo = additionalInfo;
	}

	public List<Object> getAdditionalInfo() {
		return additionalInfo;
	}
}
