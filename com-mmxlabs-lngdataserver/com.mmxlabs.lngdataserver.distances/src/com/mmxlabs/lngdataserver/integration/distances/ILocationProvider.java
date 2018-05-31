/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import com.mmxlabs.models.lng.port.Port;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public interface ILocationProvider {

	List<Port> getPorts();

	Port getPortById(String mmxId);

	Port getPortByExactName(String name);

	Port getPortByApproxName(String approxName);

	@NonNull
	String getVersion();
}
