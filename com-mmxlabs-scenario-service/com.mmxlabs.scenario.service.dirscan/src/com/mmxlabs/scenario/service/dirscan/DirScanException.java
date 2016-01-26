/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan;

public class DirScanException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String serviceName;

	public DirScanException(String serviceName) {
		super();
		this.serviceName = serviceName;
	}

	public DirScanException(String serviceName, final String message) {
		super(message);
		this.serviceName = serviceName;
	}

	public DirScanException(String serviceName, final String message, final Throwable cause) {
		super(message, cause);
		this.serviceName = serviceName;
	}

	public DirScanException(String serviceName, final Throwable cause) {
		super(cause);
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}
}
