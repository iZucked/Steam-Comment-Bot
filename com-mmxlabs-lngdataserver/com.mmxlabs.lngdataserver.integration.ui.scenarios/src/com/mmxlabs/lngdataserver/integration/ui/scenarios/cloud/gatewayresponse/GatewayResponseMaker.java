/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.gatewayresponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class GatewayResponseMaker {

	private GatewayResponseMaker() {
	}

	public static IGatewayResponse makeGatewayResponse(final CloseableHttpResponse response) {
		final int responseCode = response.getStatusLine().getStatusCode();
		if (responseCode >= 200 && responseCode < 400) {
			return new Success(responseCode);
		} else {
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
