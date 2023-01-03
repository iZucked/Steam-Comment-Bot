/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse;

public interface IGatewayResponse {

	public int getResponseCode();

	public boolean isResultDownloaded();

	public boolean shouldPollAgain();

	public String getResultReason();

	public boolean isError();
}
