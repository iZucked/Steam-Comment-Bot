package com.mmxlabs.scenario.service.dirscan;


public class DirScanException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DirScanException() {
		super();
	}

	public DirScanException(final String message) {
		super(message);
	}

	public DirScanException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DirScanException(final Throwable cause) {
		super(cause);
	}
}
