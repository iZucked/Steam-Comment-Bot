/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse;

record Success(int responseCode) implements IGatewayResponse {

	@Override
	public int getResponseCode() {
		return this.responseCode;
	}

	@Override
	public boolean isResultDownloaded() {
		return true;
	}

	@Override
	public boolean shouldPollAgain() {
		return false;
	}

	@Override
	public String getResultReason() {
		return "";
	}

	@Override
	public boolean isError() {
		return false;
	}

}
