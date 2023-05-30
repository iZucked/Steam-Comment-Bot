/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.distanceupdate.model;

/**
 * Class mapping port replacement by MMXID. The message will be displayed to users in the distance updater wizard.
 * 
 * @author Simon Goodall
 *
 */
public class PortReplacement {

	private String oldPort;
	private String newPort;
	private String message;

	public String getOldPort() {
		return oldPort;
	}

	public void setOldPort(final String oldPort) {
		this.oldPort = oldPort;
	}

	public String getNewPort() {
		return newPort;
	}

	public void setNewPort(final String newPort) {
		this.newPort = newPort;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
