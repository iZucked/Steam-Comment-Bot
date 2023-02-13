/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.fleet.IVesselSummaryColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.FleetBasedColumnFactory")
public interface IFleetBasedColumnFactoryExtension {

	@MapName("handlerID")
	String getHandlerID();

	@MapName("factory")
	IVesselSummaryColumnFactory getFactory();

}
