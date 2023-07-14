/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.time.LocalDate;

import com.mmxlabs.lingo.reports.emissions.columns.ColumnGroup;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrder;
import com.mmxlabs.lingo.reports.emissions.columns.ColumnOrderLevel;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoIgnore;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.cii.CIIAccumulatableEventModel;

/**
 * Emissions report data
 */
@SchemaVersion(1)
public class VesselEmissionAccountingReportModelV1 extends AbstractEmissionAccountingReportModel implements IDeltaDerivable, CIIAccumulatableEventModel {

	private static final String BASE_FUEL_GROUP = "BASE_FUEL_GROUP";
	private static final String BASE_FUEL_TITLE = "Base Fuel";
	private static final String LNG_GROUP = "LNG_GROUP";
	private static final String LNG_TITLE = "LNG";
	private static final String PILOT_LIGHT_GROUP = "PILOT_LIGHT_GROUP";
	private static final String PILOT_LIGHT_TITLE = "Pilot Light";
	private static final String METHANE_SLIP_GROUP_ID = "METHANE_SLIP_GROUP_ID";
	
	
	@ColumnGroup(id = ID_COLUMN_GROUP, headerTitle = "", position = ColumnOrder.FIRST)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public String eventType;

	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.THIRD)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public String baseFuelType;

	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.THIRD)
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.SECOND)
	public Long baseFuelConsumed;

	@ColumnGroup(id = BASE_FUEL_GROUP, headerTitle = BASE_FUEL_TITLE, position = ColumnOrder.THIRD)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long baseFuelEmissions;

	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("NBO")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long consumedNBO;

	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("FBO")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long consumedFBO;

	@ColumnGroup(id = LNG_GROUP, headerTitle = LNG_TITLE, position = ColumnOrder.FOURTH)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long emissionsLNG;

	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.FIFTH)
	@ColumnName("Type")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public String pilotLightFuelType;

	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.FIFTH)
	@ColumnName("Consumed")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long pilotLightFuelConsumption;

	@ColumnGroup(id = PILOT_LIGHT_GROUP, headerTitle = PILOT_LIGHT_TITLE, position = ColumnOrder.FIFTH)
	@ColumnName("Emissions")
	@ColumnOrderLevel(ColumnOrder.THIRD)
	public Long pilotLightEmission;

	@ColumnGroup(id = METHANE_SLIP_GROUP_ID, headerTitle = "", position = ColumnOrder.THIRD_FROM_END)
	@ColumnName("Methane Slip")
	@ColumnOrderLevel(ColumnOrder.FIRST)
	public Long methaneSlip;
	
	@LingoIgnore
	private double totalEmissionForCII;
	@LingoIgnore
	private LocalDate eventStartForCII;
	@LingoIgnore
	private LocalDate eventEndForCII;
	@LingoIgnore
	private Vessel vesselForCII;
	@LingoIgnore
	private Event eventForCII;

	@Override
	public boolean equals(Object other) {
		if (other instanceof final VesselEmissionAccountingReportModelV1 otherModel) {
			return super.equals(other) && this.pilotLightEmission == otherModel.pilotLightEmission && this.methaneSlip == otherModel.methaneSlip && this.emissionsLNG == otherModel.emissionsLNG
					&& this.consumedFBO == otherModel.consumedFBO && this.consumedNBO == otherModel.consumedNBO;
		}
		return false;
	}

	@Override
	public void initZeroes() {
		super.initZeroes();
		pilotLightEmission = 0L;
		methaneSlip = 0L;
		emissionsLNG = 0L;
		consumedFBO = 0L;
		consumedNBO = 0L;
		baseFuelConsumed = 0L;
		baseFuelEmissions = 0L;
		pilotLightFuelConsumption = 0L;
	}

	@Override
	public void setDelta(IDeltaDerivable first, IDeltaDerivable second) {
		if (first instanceof final VesselEmissionAccountingReportModelV1 firstModel && second instanceof final VesselEmissionAccountingReportModelV1 secondModel) {
			super.setDelta(firstModel, secondModel);
			this.methaneSlip = firstModel.methaneSlip - secondModel.methaneSlip;
			this.emissionsLNG = firstModel.emissionsLNG - secondModel.emissionsLNG;
			this.consumedFBO = firstModel.consumedFBO - secondModel.consumedFBO;
			this.consumedNBO = firstModel.consumedNBO - secondModel.consumedNBO;
			if (firstModel.baseFuelType.equals(secondModel.baseFuelType)) {
				this.baseFuelType = firstModel.baseFuelType;
				this.baseFuelConsumed = firstModel.baseFuelConsumed - secondModel.baseFuelConsumed;
			}
			this.baseFuelEmissions = firstModel.baseFuelEmissions - secondModel.baseFuelEmissions;
			if (firstModel.pilotLightFuelType.equals(secondModel.pilotLightFuelType)) {
				this.pilotLightFuelType = firstModel.baseFuelType;
				this.pilotLightFuelConsumption = firstModel.pilotLightFuelConsumption - secondModel.pilotLightFuelConsumption;
			}
			this.pilotLightEmission = firstModel.pilotLightEmission - secondModel.pilotLightEmission;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void addToTotal(IDeltaDerivable summand) {
		if (summand instanceof final VesselEmissionAccountingReportModelV1 summandModel) {
			super.addToTotal(summand);
			this.pilotLightEmission += summandModel.pilotLightEmission;
			this.methaneSlip += summandModel.methaneSlip;
			this.emissionsLNG += summandModel.emissionsLNG;
			this.consumedFBO += summandModel.consumedFBO;
			this.consumedNBO += summandModel.consumedNBO;
			this.baseFuelEmissions += summandModel.baseFuelEmissions;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean isNonZero() {
		return super.isNonZero() || pilotLightEmission != 0L || methaneSlip != 0L || emissionsLNG != 0L || consumedFBO != 0L || consumedNBO != 0L || baseFuelEmissions != 0L;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public Vessel getCIIVessel() {
		return vesselForCII;
	}

	@Override
	public Event getCIIEvent() {
		return eventForCII;
	}

	@Override
	public LocalDate getCIIStartDate() {
		return eventStart.toLocalDate();
	}

	@Override
	public LocalDate getCIIEndDate() {
		return eventEnd.toLocalDate();
	}

	@Override
	public double getTotalEmissionForCII() {
		return totalEmissionForCII;
	}

	@Override
	public void setCIIVessel(Vessel vessel) {
		this.vesselForCII = vessel;
	}

	@Override
	public void setCIIEvent(Event event) {
		this.eventForCII = event;
	}

	@Override
	public void setCIIStartDate(LocalDate startDate) {
		this.eventStartForCII = startDate;
	}

	@Override
	public void setCIIEndDate(LocalDate endDate) {
		this.eventEndForCII = endDate;
	}

	@Override
	public void addToTotalEmissionForCII(double emission) {
		this.totalEmissionForCII += emission;
	}
}
