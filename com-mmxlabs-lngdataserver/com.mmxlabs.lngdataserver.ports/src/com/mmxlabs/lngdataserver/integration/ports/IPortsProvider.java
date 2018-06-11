/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lngdataservice.client.ports.model.Port;

@NonNullByDefault
public interface IPortsProvider {

	String getVersion();

	/**
	 * Return a Collection of identifiers used as from or to ports.
	 * 
	 * @return
	 */
	Collection<Port> getPorts();
	
	Port getPortById(final String id);
}
