/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse;

record NotFound() implements IGatewayResponse {

	@Override
	public int getResponseCode() {
		return 404;
	}

	@Override
	public boolean isResultDownloaded() {
		return false;
	}

	@Override
	public boolean shouldPollAgain() {
		return false;
	}

	@Override
	public String getResultReason() {
		return "Result ID not found";
	}

	@Override
	public boolean isError() {
		return true;
	}
}
