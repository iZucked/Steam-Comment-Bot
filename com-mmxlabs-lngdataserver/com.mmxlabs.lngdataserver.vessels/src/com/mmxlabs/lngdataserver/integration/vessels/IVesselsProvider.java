package com.mmxlabs.lngdataserver.integration.vessels;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.lngdataserver.vessel.model.Vessel;

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
