package com.mmxlabs.lngdataserver.distances;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IDistanceProvider {

	String getVersion();

	int getDistance(String from, String to, Via route);

	/**
	 * Return a Collection of identifiers used as from or to ports.
	 * 
	 * @return
	 */
	Collection<String> getKnownPorts();
}
