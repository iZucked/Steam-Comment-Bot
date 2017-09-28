package com.mmxlabs.lngdataserver.ui.server;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lngdataserver.ui.server.EndpointExtension")
public interface EndpointExtensionPoint {

	@MapName("class")
	EndpointExtension getInstance();

}
