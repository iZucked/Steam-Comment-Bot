package com.mmxlabs.lngdataserver.ui.server;

import org.eclipse.jetty.server.Handler;

public interface EndpointExtension {
	Handler getHandler();
}
