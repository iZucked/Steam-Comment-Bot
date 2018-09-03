/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IDistanceProvider {

	String getVersion();

	double getDistance(String from, String to, Via route);

	/**
	 * Return a Collection of identifiers used as from or to ports.
	 * 
	 * @return
	 */
	Collection<String> getKnownPorts();
}
