/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnGroup;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;

@SchemaVersion(1)
public class CargoEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel implements IDeltaDerivable {

	private static final String EMISSION_TYPES_GROUP = "EMISSIONS_TYPES_GROUP";
	private static final String SHIPPING_GROUP = "SHIPPING_GROUP";
	private static final String SHIPPING_TITLE = "Shipping";
	private static final String OTHER_GROUP = "OTHER_GROUP";
	private static final String CH4_GROUP_ID = "CH4_GROUP";
	private static final String CH4_GROUP_TITLE = "CH4";

	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.THIRD)
	@ColumnName("Upstream")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long upstreamEmission;

	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.THIRD)
	@ColumnName("Pipeline")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public Long pipelineEmission;

	@ColumnGroup(id = EMISSION_TYPES_GROUP, headerTitle = "", position = ColumnOrder.THIRD)
	@ColumnName("Liquefaction")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long liquefactionEmission;

	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("Base Fuel")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long baseFuelEmission;

	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public Long nbo;

	@ColumnGroup(id = SHIPPING_GROUP, headerTitle = SHIPPING_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long fbo;

	@ColumnGroup(id = OTHER_GROUP, headerTitle = "", position = ColumnOrder.FIFTH)
	@ColumnName("Pilot Light")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long pilotLightEmission;

	@ColumnGroup(id = CH4_GROUP_ID, headerTitle = CH4_GROUP_TITLE, position = ColumnOrder.THIRD_FROM_END)
	@ColumnName("Methane Slip")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long methaneSlip;

	@ColumnGroup(id = CH4_GROUP_ID, headerTitle = CH4_GROUP_TITLE, position = ColumnOrder.THIRD_FROM_END)
	@ColumnName("CO2e")
	@ColumnOrderLevel(ColumnOrder.LAST)
	public Long methaneSlipEmissionsCO2;

	@Override
	public boolean equals(Object other) {
		if (other instanceof final CargoEmissionAccountingReportModelV1 otherModel) {
			return super.equals(other) && this.pilotLightEmission == otherModel.pilotLightEmission && this.methaneSlip == otherModel.methaneSlip && this.fbo == otherModel.fbo
					&& this.nbo == otherModel.nbo && this.liquefactionEmission == otherModel.liquefactionEmission && this.pipelineEmission == otherModel.pipelineEmission
					&& this.methaneSlipEmissionsCO2 == otherModel.methaneSlipEmissionsCO2 && this.baseFuelEmission == otherModel.baseFuelEmission
					&& this.upstreamEmission == otherModel.upstreamEmission;
		}
		return false;
	}

	@Override
	public void initZeroes() {
		super.initZeroes();
		this.baseFuelEmission = 0L;
		this.pilotLightEmission = 0L;
		this.upstreamEmission = 0L;
		this.liquefactionEmission = 0L;
		this.pipelineEmission = 0L;
		this.nbo = 0L;
		this.fbo = 0L;
		this.methaneSlip = 0L;
		this.methaneSlipEmissionsCO2 = 0L;
	}

	@Override
	public void setDelta(IDeltaDerivable first, IDeltaDerivable second) {
		if (first instanceof final CargoEmissionAccountingReportModelV1 firstModel && second instanceof final CargoEmissionAccountingReportModelV1 secondModel) {
			super.setDelta(firstModel, secondModel);
			this.baseFuelEmission = firstModel.baseFuelEmission - secondModel.baseFuelEmission;
			this.pilotLightEmission = firstModel.pilotLightEmission - secondModel.pilotLightEmission;
			this.upstreamEmission = firstModel.upstreamEmission - secondModel.upstreamEmission;
			this.liquefactionEmission = firstModel.liquefactionEmission - secondModel.liquefactionEmission;
			this.pipelineEmission = firstModel.pipelineEmission - secondModel.pipelineEmission;
			this.nbo = firstModel.nbo - secondModel.nbo;
			this.fbo = firstModel.fbo - secondModel.fbo;
			this.methaneSlip = firstModel.methaneSlip - secondModel.methaneSlip;
			this.methaneSlipEmissionsCO2 = firstModel.methaneSlipEmissionsCO2 - secondModel.methaneSlipEmissionsCO2;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void addToTotal(IDeltaDerivable summand) {
		if (summand instanceof final CargoEmissionAccountingReportModelV1 summandModel) {
			super.addToTotal(summand);
			this.baseFuelEmission += summandModel.baseFuelEmission;
			this.pilotLightEmission += summandModel.pilotLightEmission;
			this.upstreamEmission += summandModel.upstreamEmission;
			this.liquefactionEmission += summandModel.liquefactionEmission;
			this.pipelineEmission += summandModel.pipelineEmission;
			this.nbo += summandModel.nbo;
			this.fbo += summandModel.fbo;
			this.methaneSlip += summandModel.methaneSlip;
			this.methaneSlipEmissionsCO2 += summandModel.methaneSlipEmissionsCO2;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean isNonZero() {
		return super.isNonZero() || this.baseFuelEmission != 0L || this.pilotLightEmission != 0L || this.upstreamEmission != 0L || this.liquefactionEmission != 0L || this.pipelineEmission != 0L
				|| this.nbo != 0L || this.fbo != 0L || this.methaneSlip != 0L || this.methaneSlipEmissionsCO2 != 0L;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
