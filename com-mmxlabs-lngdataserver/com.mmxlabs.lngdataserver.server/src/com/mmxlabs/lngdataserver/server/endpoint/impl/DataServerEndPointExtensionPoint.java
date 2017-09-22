/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.server.endpoint.impl;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.lngdataserver.server.DataServerEndPoint")
public interface DataServerEndPointExtensionPoint {

	@MapName("class")
	public Class<?> getEndpointClass();

}
