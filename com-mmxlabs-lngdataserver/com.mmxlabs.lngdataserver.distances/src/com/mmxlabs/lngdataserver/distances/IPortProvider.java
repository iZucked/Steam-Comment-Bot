package com.mmxlabs.lngdataserver.distances;

import com.mmxlabs.models.lng.port.Port;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

public interface IPortProvider {

	List<Port> getPorts();

	Port getPortById(String mmxId);

	Port getPortByExactName(String name);

	Port getPortByApproxName(String approxName);

	@NonNull
	String getVersion();
}
