/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;

/**
 * Big helper class for any report based on {@link CargoAllocation}s, {@link OpenSlotAllocation}s, or other events. This builds the internal report data model and handles pin/diff comparison hooks.
 * Currently this class also some generic columns used in these reports but these should be broken out into separate classes as part of FogBugz: 51/
 * 
 * @author Simon Goodall
 * 
 */
public class PortRotationBasedReportBuilder extends AbstractReportBuilder {
	public static final String PORT_ROTATION_REPORT_TYPE_ID = "PORT_ROTATION_REPORT_TYPE_ID";
	private PortRotationReportView report;

	public PortRotationBasedReportBuilder() {
	}

	public PortRotationReportView getReport() {
		return report;
	}

	public void setReport(final PortRotationReportView report) {
		this.report = report;
	}
}