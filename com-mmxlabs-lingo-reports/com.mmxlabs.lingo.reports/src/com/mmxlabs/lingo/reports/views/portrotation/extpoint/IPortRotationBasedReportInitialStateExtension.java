/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.PortRotationBasedReportInitialState")
public interface IPortRotationBasedReportInitialStateExtension {

	@MapName("id")
	String getViewID();

	@MapName("customisable")
	String getCustomisable();

	@MapName("InitialColumn")
	InitialColumn[] getInitialColumns();

	public interface InitialColumn {
		@MapName("id")
		String getID();
	}

}
