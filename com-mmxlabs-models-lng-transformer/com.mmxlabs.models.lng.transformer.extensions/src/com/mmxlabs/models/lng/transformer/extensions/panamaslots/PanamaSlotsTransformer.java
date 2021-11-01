/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.impl.PanamaSeasonalityCurve;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProviderEditor;

/**
 * @author robert
 */
public class PanamaSlotsTransformer implements ITransformerExtension {

	private static final String PanamaTimeZone = "America/Panama";

	@Inject
	private IPanamaBookingsProviderEditor panamaBookingsProviderEditor;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private IScenarioDataProvider scenarioDataProvider;

	private CanalBookings canalBookings;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		final PortModel portModel = ScenarioModelUtil.getPortModel(rootObject);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		if (!potentialPanama.isPresent()) {
			return;
		}

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);
		canalBookings = cargoModel.getCanalBookings();
	}

	@Override
	public void finishTransforming() {
		final ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

		final Map<ECanalEntry, List<IRouteOptionBooking>> panamaSlots = new HashMap<>();

		if (canalBookings != null) {

			for (final CanalBookingSlot eBooking : this.canalBookings.getCanalBookingSlots()) {

				final Port port = modelDistanceProvider.getCanalPort(RouteOption.PANAMA, eBooking.getCanalEntrance());

				// No need to worry about change of daylight savings for Panama ports, so using atStartOfDay should be ok:
				// "In Central America, the countries from Guatemala to Costa Rica use Central Standard Time UTC−06:00 year-round, but Panama uses Eastern Standard Time (UTC−05:00) year-round."
				final int date = dateAndCurveHelper.convertTime(eBooking.getBookingDate().atStartOfDay(port.getZoneId()).plusHours(CanalBookingSlot.BOOKING_HOURS_OFFSET));

				final IRouteOptionBooking oBooking;
				Set<Vessel> eVessels;
				Set<IVessel> oVessels;
				if (eBooking.getVessel() != null) {
					eVessels = SetUtils.getObjects(eBooking.getVessel());
					oVessels = getOptimiserVessels(eVessels);
				}
				else if (eBooking.getBookingCode() != null) {
					eVessels = SetUtils.getObjects(eBooking.getBookingCode().getVesselGroup());
					oVessels = getOptimiserVessels(eVessels);
				}
				else {
					//Otherwise empty set, indicating booking can be used for any vessel.
					oVessels = Collections.emptySet();
				}

				final @NonNull ECanalEntry oCanalEntrance = mapCanalEntry(eBooking.getCanalEntrance());

				if (oVessels != null && oVessels.size() > 0) {
					oBooking = IRouteOptionBooking.of(date, oCanalEntrance, ERouteOption.PANAMA, oVessels);
				} else {
					oBooking = IRouteOptionBooking.of(date, oCanalEntrance, ERouteOption.PANAMA);
				}
				panamaSlots.computeIfAbsent(oCanalEntrance, key -> new LinkedList<>()).add(oBooking);

				modelEntityMap.addModelObject(eBooking, oBooking);
			}

			// Sort bookings by date.
			for (final ECanalEntry entry : ECanalEntry.values()) {
				final List<IRouteOptionBooking> list = panamaSlots.get(entry);
				if (list != null) {
					Collections.sort(list);
				}
			}

			panamaBookingsProviderEditor.setBookings(panamaSlots);
			panamaBookingsProviderEditor.setArrivalMargin(canalBookings.getArrivalMarginHours());
			final List<PanamaSeasonalityRecord> seasonalityRecords = this.canalBookings.getPanamaSeasonalityRecords();
			final List<PanamaSeasonalityCurve> seasonalityCurve = new ArrayList<PanamaSeasonalityCurve>();
			int startYear = modelEntityMap.getEarliestTime().toLocalDate().getYear();
			int endYear = modelEntityMap.getLatestTime().toLocalDate().getYear() + 1;
			
			for (final VesselGroupCanalParameters vesselGroupsMaxIdles : this.canalBookings.getVesselGroupCanalParameters()) {
				
				final Set<Vessel> vessels = SetUtils.getObjects(vesselGroupsMaxIdles.getVesselGroup());
				final Set<PanamaSeasonalityRecord> psrs = seasonalityRecords.stream()
						.filter(psr -> psr.getVesselGroupCanalParameter().equals(vesselGroupsMaxIdles)).collect(Collectors.toSet());

				// Note: There is a difference between vessels.size() == 0 and vesselGroupsMaxIdles.getVesselGroup().size() == 0.
				// The first means there are no vessels belonging to the assigned groups. The latter means there were no vessels or groups set.

				if (vesselGroupsMaxIdles.getVesselGroup().isEmpty()) {
					// Default if vessels list is empty.
					if (seasonalityRecords.isEmpty()) {
						addDefaultPanamaSeasonalityCurve(seasonalityCurve, null, //
								LocalDateTime.of(2050, 1, 1, 00, 00).atZone(ZoneId.of(PanamaTimeZone)), 0, 0);
					} else {
						addPanamaSeasonalityCurve(seasonalityCurve,null, startYear, endYear, psrs);
					}
				} else {
					// Specific vessels group.
					for (final Vessel vessel : vessels) {
						final IVessel oVessel = modelEntityMap.getOptimiserObject(vessel, IVessel.class);
						if (psrs.size() == 1) {
							psrs.forEach(psr -> {
								addDefaultPanamaSeasonalityCurve( seasonalityCurve, oVessel, //
										LocalDateTime.of(endYear, 12, 31, 00, 00).atZone(ZoneId.of(PanamaTimeZone)), 
										psr.getNorthboundWaitingDays(), psr.getSouthboundWaitingDays());
							});
						} else {
							addPanamaSeasonalityCurve(seasonalityCurve,oVessel, startYear, endYear, psrs);
						}
					}
				}
			}
			// Should NOT really happen. Unless scenario is broken or more validation required
			if (seasonalityCurve.isEmpty()) {
				addDefaultPanamaSeasonalityCurve(seasonalityCurve, null, //
						LocalDateTime.of(2050, 1, 1, 00, 00).atZone(ZoneId.of(PanamaTimeZone)), 0, 0);
			}
			panamaBookingsProviderEditor.setPanamaMaxIdleDays(seasonalityCurve);
		}
	}

	protected void addDefaultPanamaSeasonalityCurve(final List<PanamaSeasonalityCurve> seasonalityCurve, final IVessel oVessel,final ZonedDateTime zdt,  int nb, int sb) {
		int changePoints[] = {dateAndCurveHelper.convertTime(zdt)};
		int nbWaitingDays[] = {nb};
		int sbWaitingDays[] = {sb};
		seasonalityCurve.add(new PanamaSeasonalityCurve(oVessel, changePoints,nbWaitingDays,sbWaitingDays));
	}

	protected void addPanamaSeasonalityCurve(final List<PanamaSeasonalityCurve> seasonalityCurve, final IVessel oVessel, int startYear, int endYear, final Set<PanamaSeasonalityRecord> psrs) {
		int year = startYear;
		int size = (endYear - year + 1) * psrs.size();
		int[] icp = new int[size];
		int[] inb = new int[size];
		int[] isb = new int[size];
		int i = 0;
		while(year <= endYear) {
			for (final PanamaSeasonalityRecord psr : psrs) {
				int startMonth = psr.getStartMonth();
				int startDay = psr.getStartDay();
				int psrYear = year;
				if (startMonth == 0 || startDay == 0) {
					startMonth = 12;
					startDay = 31;
				} else {
					if (psr.getStartYear() > 2000) {
						psrYear = psr.getStartYear();
					}
					if (startMonth == 2 && startDay == 29 && !Year.isLeap(psrYear)) {
						startDay = 28;
					}
				}
				icp[i] = dateAndCurveHelper.convertTime(LocalDateTime.of(psrYear, startMonth, startDay, 23, 59)//
						.atZone(ZoneId.of(PanamaTimeZone)));
				inb[i] = psr.getNorthboundWaitingDays();
				isb[i] = psr.getSouthboundWaitingDays();
				i++;
			}
			year++;
		}
		bubbleSort(icp, inb, isb);
		seasonalityCurve.add(new PanamaSeasonalityCurve(oVessel, icp,inb,isb));
	}
	
	private void bubbleSort(int[] a, int[] b, int[] c) {
	    boolean sorted = false;
	    while(!sorted) {
	        sorted = true;
	        for (int i = 0; i < a.length - 1; i++) {
	            if (a[i] > a[i+1]) {
	            	setNewSortOrder(i, a);
	            	setNewSortOrder(i, b);
	            	setNewSortOrder(i, c);
	                sorted = false;
	            }
	        }
	    }
	}
	
	private void setNewSortOrder(int i, int[] array) {
		int temp = array[i];
		array[i] = array[i+1];
		array[i+1] = temp;
	}

	private Set<IVessel> getOptimiserVessels(final Set<Vessel> eVessels) {
		final Set<IVessel> oVessels = new HashSet<>();
		for (final Vessel vessel : eVessels) {
			final IVessel oVessel = modelEntityMap.getOptimiserObject(vessel, IVessel.class);
			oVessels.add(oVessel);
		}
		return oVessels;
	}

	private @NonNull ECanalEntry mapCanalEntry(final CanalEntry canalEntrance) {
		switch (canalEntrance) {
		case NORTHSIDE:
			return ECanalEntry.NorthSide;
		case SOUTHSIDE:
			return ECanalEntry.SouthSide;
		}
		throw new IllegalArgumentException();
	}

}
