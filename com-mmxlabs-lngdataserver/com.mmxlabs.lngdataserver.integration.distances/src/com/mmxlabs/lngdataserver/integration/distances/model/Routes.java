/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.distances.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author robert.erdin@minimaxlabs.com on 18/07/17.
 */
public class Routes {

	private Map<String, Route> routes = new HashMap<>();

	public Routes() {
		// for morphia/jackson
	}

	public Routes(Routes other) {
		this.routes = new HashMap<>(other.routes);
	}

	public Map<String, Route> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<String, Route> routes) {
		this.routes = routes;
	}
}
