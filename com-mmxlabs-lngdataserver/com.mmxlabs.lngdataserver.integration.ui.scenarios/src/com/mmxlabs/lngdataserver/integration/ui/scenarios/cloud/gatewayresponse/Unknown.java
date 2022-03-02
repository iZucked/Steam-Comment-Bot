/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse;

record Unknown(int responseCode, boolean isResultDownloaded) implements IGatewayResponse {

	@Override
	public int getResponseCode() {
		return responseCode;
	}

	@Override
	public boolean shouldPollAgain() {
		return true;
	}

	@Override
	public String getResultReason() {
		return "";
	}

	@Override
	public boolean isError() {
		return true;
	}

}
