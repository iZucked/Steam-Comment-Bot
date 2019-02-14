/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports.model;

import java.util.LinkedList;
import java.util.List;

public class PortGroup implements IPortGroup {

	private String name;
	private List<IPortGroup> groups = new LinkedList<>();
	private List<Port> ports = new LinkedList<>();

	public List<IPortGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<IPortGroup> groups) {
		this.groups = groups;
	}

	public List<Port> getPorts() {
		return ports;
	}

	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getGroupID() {
		return "PG_" + name.toLowerCase();
	}
}
