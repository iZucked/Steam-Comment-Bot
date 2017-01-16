/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.lingo.reports.views.fleet.IFleetColumnFactory;

@ExtensionBean("com.mmxlabs.lingo.reports.FleetBasedColumnFactory")
public interface IFleetBasedColumnFactoryExtension {

	@MapName("handlerID")
	String getHandlerID();

	@MapName("factory")
	IFleetColumnFactory getFactory();

}
