/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.services.permissions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserPermissions {
	private Map<String, List<String>> services = new HashMap<>();
	private List<String> global = new LinkedList<>();

	public void addServicePermission(String service, String... permissions) {
		for (String permission : permissions) {
			services.computeIfAbsent(service, k -> new LinkedList<>()).add(permission);
		}
	}

	public Map<String, List<String>> getServices() {
		return services;
	}

	public void setServices(Map<String, List<String>> services) {
		this.services = services;
	}

	public List<String> getGlobal() {
		return global;
	}

	public void setGlobal(List<String> global) {
		this.global = global;
	}
}
