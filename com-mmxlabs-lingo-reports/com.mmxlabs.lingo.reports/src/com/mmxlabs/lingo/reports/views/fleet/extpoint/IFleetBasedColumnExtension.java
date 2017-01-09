/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.fleet.IFleetColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.FleetBasedColumn")
public interface IFleetBasedColumnExtension {

	@MapName("id")
	String getColumnID();

	@MapName("handlerID")
	String getHandlerID();

	// TODO: This does not work, returns null. Peaberry does not seem to hit creation code.

	@MapName("class")
	IFleetColumnFactory createFactory();

}
