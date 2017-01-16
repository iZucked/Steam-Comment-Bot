/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.extpoint;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.FleetBasedReportInitialState")
public interface IFleetBasedReportInitialStateExtension {

	@MapName("id")
	String getViewID();

	@MapName("customisable")
	String getCustomisable();

	@MapName("InitialColumn")
	InitialColumn[] getInitialColumns();

	@MapName("InitialRowType")
	InitialRowType[] getInitialRows();

	@MapName("InitialDiffOption")
	InitialDiffOption[] getInitialDiffOptions();

	public interface InitialColumn {
		@MapName("id")
		String getID();
	}

	public interface InitialRowType {
		@MapName("rowType")
		String getRowType();
	}

	public interface InitialDiffOption {
		@MapName("option")
		String getOption();
	}

}
