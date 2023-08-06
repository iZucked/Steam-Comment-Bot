/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.general.upstreamcargo;

import java.time.LocalDateTime;

public class UpstreamPosition {
	String id;
//	String externalId;

	// Mutable fields
	String port;
	LocalDateTime date;
	String vessel; // charter no? infer from dates?

	// Immutable fields
	String type;
	String contract;
	String expression;
}
