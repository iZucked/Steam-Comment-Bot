/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoModel;
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
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ECanalEntry;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProviderEditor;

/**
 * @author robert
 */
public class PanamaSlotsTransformer implements ITransformerExtension {

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

			for (final VesselGroupCanalParameters vesselGroupsMaxIdles : this.canalBookings.getVesselGroupCanalParameters()) {
				final Set<Vessel> vessels = SetUtils.getObjects(vesselGroupsMaxIdles.getVesselGroup());

				// Note: There is a difference between vessels.size() == 0 and vesselGroupsMaxIdles.getVesselGroup().size() == 0.
				// The first means there are no vessels belonging to the assigned groups. The latter means there were no vessels or groups set.

				if (vesselGroupsMaxIdles.getVesselGroup().isEmpty()) {
					// Default if vessels list is empty.
					panamaBookingsProviderEditor.setNorthboundMaxIdleDays(vesselGroupsMaxIdles.getNorthboundWaitingDays());
					panamaBookingsProviderEditor.setSouthboundMaxIdleDays(vesselGroupsMaxIdles.getSouthboundWaitingDays());
				} else {
					// Specific vessels group.
					for (final Vessel vessel : vessels) {
						final IVessel oVessel = modelEntityMap.getOptimiserObject(vessel, IVessel.class);
						panamaBookingsProviderEditor.setNorthboundMaxIdleDays(oVessel, vesselGroupsMaxIdles.getNorthboundWaitingDays());
						panamaBookingsProviderEditor.setSouthboundMaxIdleDays(oVessel, vesselGroupsMaxIdles.getSouthboundWaitingDays());
					}
				}
			}

		}
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
