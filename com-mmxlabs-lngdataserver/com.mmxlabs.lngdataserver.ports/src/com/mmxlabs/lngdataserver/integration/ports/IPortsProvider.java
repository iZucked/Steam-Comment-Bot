package com.mmxlabs.lngdataserver.integration.ports;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lngdataserver.port.model.Port;

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
