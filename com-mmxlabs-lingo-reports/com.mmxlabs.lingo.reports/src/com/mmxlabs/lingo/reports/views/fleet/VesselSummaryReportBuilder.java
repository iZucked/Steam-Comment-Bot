/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet;

import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.views.AbstractReportBuilder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * Big helper class for any report based on {@link CargoAllocation}s, {@link OpenSlotAllocation}s, or other events. This builds the internal report data model and handles pin/diff comparison hooks.
 * Currently this class also some generic columns used in these reports but these should be broken out into separate classes as part of FogBugz: 51/
 * 
 * @author Simon Goodall
 * 
 */
public class VesselSummaryReportBuilder extends AbstractReportBuilder {
	public static final String VESSEL_SUMMARY_REPORT_TYPE_ID = "FLEET_REPORT_TYPE_ID";

	public static final OptionInfo ROW_FILTER_SPOT_CHARTER_INS = new OptionInfo("ROW_FILTER_SPOT_CHARTER_INS", "Spot", "spotcharter");
	public static final OptionInfo ROW_FILTER_TIME_CHARTERS = new OptionInfo("ROW_FILTER_TIME_CHARTERS", "Charted", "timecharter");
	public static final OptionInfo ROW_FILTER_OWNED = new OptionInfo("ROW_FILTER_OWNED", "Owned", "owned");

	/** All filters (note this order is also used in the {@link ConfigurableVesselSummaryReport} dialog */
	public VesselSummaryReportBuilder() {
		ROW_FILTER_ALL = new OptionInfo[] { ROW_FILTER_OWNED, ROW_FILTER_TIME_CHARTERS, ROW_FILTER_SPOT_CHARTER_INS };
		/** All filters (note this order is also used in the {@link ConfigurableVesselSummaryReport} dialog */
		DIFF_FILTER_ALL = new OptionInfo[] { DIFF_FILTER_PINNDED_SCENARIO };
	}

	public boolean showEvent(final Sequence sequence) {
		if (sequence.isFleetVessel()) {
			return rowFilterInfo.contains(ROW_FILTER_OWNED.id);
		}
		if (sequence.isTimeCharterVessel()) {
			return rowFilterInfo.contains(ROW_FILTER_TIME_CHARTERS.id);
		}
		if (sequence.isSpotVessel()) {
			return rowFilterInfo.contains(ROW_FILTER_SPOT_CHARTER_INS.id);
		}
		return false;
	}

}
