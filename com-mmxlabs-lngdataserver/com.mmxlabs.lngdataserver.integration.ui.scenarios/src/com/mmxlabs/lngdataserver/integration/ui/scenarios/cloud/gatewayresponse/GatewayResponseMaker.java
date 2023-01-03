/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import okhttp3.Response;

@NonNullByDefault
public class GatewayResponseMaker {

	private GatewayResponseMaker() {
	}

	public static IGatewayResponse makeGatewayResponse(final Response response) {
		if (response.isSuccessful()) {
			return new Success(response.code());
		} else {
			final int responseCode = response.code();
			// response code 200..300 covered by previous branch
			switch (responseCode) {
			case 404 -> {
				return new NotFound();
			}
			case 410 -> {
				return new ResultGone();
			}
			default -> {
				return new Unknown(responseCode, false);
			}
			}
		}
	}
}
