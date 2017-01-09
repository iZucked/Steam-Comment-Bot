/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.timezone;

/**
 * An {@link Exception} wrapping an error message from a {@link ITimezoneProvider} implementation. This indicates an error response from the service, rather than an error contacting the service.
 * 
 * @author Simon Goodall
 * @since 2.1
 */
public class TimezoneProviderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TimezoneProviderException(final String message) {
		super(message);
	}
}
