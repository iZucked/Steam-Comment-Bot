/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.util.Map;

public class DumpAccountInfo {

	public static void main(String[] args) {

		Util.withService(service -> {
			try {
				Map<String, String> accountDetails = service.getAccountDetails();
				for (Map.Entry<String, String> e : accountDetails.entrySet()) {
					System.out.printf("%s: %s %n", e.getKey(), e.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
