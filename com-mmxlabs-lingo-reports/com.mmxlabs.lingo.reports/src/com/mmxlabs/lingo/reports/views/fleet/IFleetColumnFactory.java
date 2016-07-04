/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;

public interface IFleetColumnFactory {

	/**
	 * Register a new column with the given ID to the provided manager. One of the available registerColumn methods is expected to be called on the manager object.
	 * 
	 * @param columnID
	 * @param manager
	 * @param builder
	 */
	void registerColumn(String columnID, EMFReportColumnManager manager, FleetBasedReportBuilder builder);
}
