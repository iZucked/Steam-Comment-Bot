/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lngdataservice.client.vessel.model.Vessel;

@NonNullByDefault
public interface IVesselsProvider {

	String getVersion();

	/**
	 * Return a Collection of identifiers used as from or to ports.
	 * 
	 * @return
	 */
	Collection<Vessel> getVessels();
	
	Vessel getVesselById(final String id);
}
