/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.nomination;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PanamaBookingPeriod;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * 
 * @author Simon Goodall and FM
 * 
 */
public class NominationAlertReportTransformer {
	public static class RowData {
		public final String scenario;
		public final String slotId;
		public String buyer;
		public String seller;
		public final String cn;
		public final String type;
		public final LocalDate date;
		public final String nominationType;
		public final String nominationComment;
		public final Slot slot;
		public String nomination;
		public LocalDate nominateBy;
		public final boolean pinned;
		public boolean warn;
		
		public RowData(final String scenario, final Slot slot, final boolean pinned, final NominationType nType) {
			super();
			this.scenario = scenario;
			this.slot = slot;
			this.slotId = slot.getName();
			if (slot instanceof DischargeSlot) {
				DischargeSlot ds = (DischargeSlot) slot;
				if (ds.getContract() != null) {
					this.buyer = ds.getContract().getName();
				} else if (ds.getSlotOrDelegateCounterparty() != null) {
					this.buyer = ds.getSlotOrDelegateCounterparty();
				}
				if (ds.getSlotOrDelegateEntity() != null) {
					this.seller = ds.getSlotOrDelegateEntity().getName();
				}
			} else if (slot instanceof LoadSlot) {
				LoadSlot ls = (LoadSlot) slot;
				if (ls.getContract() != null) {
					this.seller = ls.getContract().getName();
				} else if (ls.getSlotOrDelegateCounterparty() != null) {
					this.seller = ls.getSlotOrDelegateCounterparty();
				}
				if (ls.getSlotOrDelegateEntity() != null) {
					this.buyer = ls.getSlotOrDelegateEntity().getName();
				}
			}
			final Cargo c = slot.getCargo();
			this.cn = slot.getSlotOrDelegateCN();
			this.type = slot instanceof LoadSlot ? "Buy" : "Sell"; //for now
			this.date = slot.getWindowStart();
			this.nomination = "";
			this.nominateBy = null;
			this.pinned = pinned;
			
			switch (nType) {
			case SLOT_DATE_0:{
				this.nominationType = slot instanceof LoadSlot ? "Buy window" : "Sell window";
				this.nomination = Formatters.asLocalDateFormatter.render(slot.getWindowStartWithSlotOrPortTime().toLocalDate());
				this.nominateBy = slot.getSlotOrDelegateWindowNominationDate();
				this.nominationComment = slot.getWindowNominationComment() != null ? slot.getWindowNominationComment() : "";
				}
				break;
			case SLOT_DATE_1:{
				this.nominationType = slot instanceof LoadSlot ? "Buy window" : "Sell window";
				this.nomination = Formatters.asLocalDateFormatter.render(slot.getWindowStartWithSlotOrPortTime().toLocalDate());
				this.nominateBy = LocalDate.now();
				this.nominationComment = "";
				}
				break;
			case SLOT_DATE_2:{
				this.nominationType = slot instanceof LoadSlot ? "Buy window" : "Sell window";
				this.nomination = Formatters.asLocalDateFormatter.render(slot.getWindowStartWithSlotOrPortTime().toLocalDate());
				this.nominateBy = LocalDate.now();
				this.nominationComment = "";
				}
				break;
			case VESSEL:{
				this.nominationType = "Vessel";
				this.nominateBy = slot.getSlotOrDelegateVesselNominationDate();
				this.nominationComment = slot.getVesselNominationComment() != null ? slot.getVesselNominationComment() : "";
				if (c == null) break;
				VesselAssignmentType vat = c.getVesselAssignmentType();
				if (vat == null) break;
				if (vat instanceof VesselAvailability) {
					VesselAvailability va = (VesselAvailability) vat;
					Vessel v = va.getVessel();
					if (v == null) break;
					this.nomination = v.getName();
					break;
				} else if (vat instanceof CharterInMarket) {
					CharterInMarket cim = (CharterInMarket) vat;
					Vessel v = cim.getVessel();
					if (v == null) break;
					this.nomination = v.getName();
					break;
				} else if (vat instanceof CharterInMarketOverride) {
					CharterInMarketOverride cimo = (CharterInMarketOverride) vat;
					CharterInMarket cim = cimo.getCharterInMarket();
					if (cim == null) break;
					Vessel v = cim.getVessel();
					if (v == null) break;
					this.nomination = v.getName();
					break;
				}
				this.nomination = "";
				}
				break;
			case VOLUME:{
				this.nominationType = slot instanceof LoadSlot ? "Load volume" : "Discharge volume";
				this.nomination = getFormattedVolume(slot);
				this.nominateBy = slot.getSlotOrDelegateVolumeNominationDate();
				this.nominationComment = slot.getVolumeNominationComment() != null ? slot.getVolumeNominationComment() : "";
				}
				break;
			case PORT:{
				this.nominationType = slot instanceof LoadSlot ? "Source port" : "Destination port";
				this.nominateBy = slot.getSlotOrDelegatePortNominationDate();
				this.nominationComment = slot.getPortNominationComment() != null ? slot.getPortNominationComment() : "";
				final Port p = slot.getPort();
				if (p == null) break;
				this.nomination = p.getName();
			}
				break;
			case PORT_2:{
				this.nominationType = slot instanceof LoadSlot ? "Discharge port" : "Load port";
				this.nominateBy = slot.getSlotOrDelegatePortLoadNominationDate();
				this.nominationComment = slot.getPortLoadNominationComment() != null ? slot.getPortLoadNominationComment() : "";
				if (c == null) break;
				Slot secondSlot = null;
				for (final Slot s : c.getSlots()) {
					if (slot instanceof LoadSlot && s instanceof DischargeSlot) {
						secondSlot = s;
					} else if (slot instanceof DischargeSlot && s instanceof LoadSlot) {
						secondSlot = s;
					}
				}
				if (secondSlot == null) break;
				final Port p = secondSlot.getPort();
				if (p == null) break;
				this.nomination = p.getName();
			}
				break;
			default:
				this.nominationType = "";
				this.nominationComment = "";
				break;
			}
			if (this.nominateBy != null) {
				if (LocalDate.now().isAfter(this.nominateBy)) {
					this.warn = true;
				}
			}
		}
		
		public RowData() {
			super();
			this.scenario = "";
			this.slot = null;
			this.slotId = "";
			this.buyer = "";
			this.seller = "";
			this.cn = "";
			this.type = "";
			this.date = null;
			this.nomination = "";
			this.nominateBy = null;
			this.nominationType = "";
			this.nominationComment = "";
			this.pinned = false;
			this.warn = false;
		}
		
		private String getFormattedVolume(final Slot slot) {
			String volume = "";
			if (slot != null) {
				
				double minValue = slot.getSlotOrDelegateMinQuantity();
				double maxValue = slot.getSlotOrDelegateMaxQuantity();
				String vol = "";
				String minVol = "";
				String maxVol = "";
				String opt = "";
				final double operationalTolerance = slot.getSlotOrDelegateOperationalTolerance() * 100.0;
				if (operationalTolerance != 0.0) {
					opt = String.format(" +/-%.0f", operationalTolerance);
				}
				
				if (slot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.MMBTU) {
					minValue = minValue / 1_000_000.0;
					maxValue = maxValue / 1_000_000.0;
					if (minValue != 0) minVol = String.format("%.3f", minValue);
					if (maxValue != Integer.MAX_VALUE) maxVol = String.format("%.3f", maxValue);
				} else {
					if (minValue != 0) minVol = String.format("%.0f", minValue);
					if (maxValue != Integer.MAX_VALUE) maxVol = String.format("%.0f", maxValue);
					vol = String.format(" %s", slot.getSlotOrDelegateVolumeLimitsUnit());
				}
				
				if (minVol.equals("")) {
					volume = maxVol + vol + opt;
				} else if(maxVol.equals("")) {
					volume = minVol + vol + opt;
				} else if (!minVol.equals("") && !maxVol.equals("")){
					volume = minVol + "-" + maxVol + vol + opt;
				}
			}
			return volume;
		}
	}
	
	public enum NominationType{
		SLOT_DATE_0, SLOT_DATE_1, SLOT_DATE_2, VESSEL, VOLUME, PORT, PORT_2
	}

	@NonNull
	public List<RowData> transform(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult, boolean pinned) {
		final String scenario = scenarioResult.getModelRecord().getName();
		List<RowData> result = new ArrayList<>();
		final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		assert scenarioModel != null;
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		assert cargoModel != null;
		for(final Slot s : cargoModel.getDischargeSlots()) {
				doTransform(pinned, scenario, result, s);
		}
		for(final Slot s : cargoModel.getLoadSlots()) {
			doTransform(pinned, scenario, result, s);
		}
		
		return result;
	}

	private void doTransform(boolean pinned, final String scenario, List<RowData> result, final Slot s) {
		//Only picking up the slots which are not done, and have a window nomination date
		if (!s.isWindowNominationIsDone() && s.getSlotOrDelegateWindowNominationDate() != null) {
			final RowData r = new RowData(scenario, s, pinned, NominationType.SLOT_DATE_0);
			result.add(r);
		}
		// here we add vessel nomination
		if (!s.isVesselNominationDone()
		&& s.getSlotOrDelegateVesselNominationDate() != null) {
			final RowData r = new RowData(scenario, s, pinned, NominationType.VESSEL);
			result.add(r);
		}
		// here we add volume nomination
		if (!s.isVolumeNominationDone()
		&& s.getSlotOrDelegateVolumeNominationDate() != null) {
			final RowData r = new RowData(scenario, s, pinned, NominationType.VOLUME);
			result.add(r);
		}
		// here we add port nomination
		if (!s.isPortNominationDone()
		&& s.getSlotOrDelegatePortNominationDate() != null) {
			final RowData r = new RowData(scenario, s, pinned, NominationType.PORT);
			result.add(r);
		}
		// here we add paired slot port nomination
		if (!s.isPortLoadNominationDone()
		&& s.getSlotOrDelegatePortLoadNominationDate() != null) {
			final RowData r = new RowData(scenario, s, pinned, NominationType.PORT_2);
			result.add(r);
		}
	}
}
