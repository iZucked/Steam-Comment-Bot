/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
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
		final Optional<@NonNull Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		if (potentialPanama.isEmpty()) {
			return;
		}

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);
		canalBookings = cargoModel.getCanalBookings();
	}

	@Override
	public void finishTransforming() {
		if (canalBookings != null) {
			{
				final ModelDistanceProvider modelDistanceProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
				final Map<@NonNull ECanalEntry, List<@NonNull IRouteOptionBooking>> panamaSlots = new EnumMap<>(ECanalEntry.class);
				for (final CanalBookingSlot eBooking : this.canalBookings.getCanalBookingSlots()) {
					final Port port = modelDistanceProvider.getCanalPort(RouteOption.PANAMA, eBooking.getCanalEntrance());

					// No need to worry about change of daylight savings for Panama ports, so using
					// atStartOfDay should be ok:
					// "In Central America, the countries from Guatemala to Costa Rica use Central
					// Standard Time UTC−06:00 year-round, but Panama uses Eastern Standard Time
					// (UTC−05:00) year-round."
					final int date = dateAndCurveHelper.convertTime(eBooking.getBookingDate().atStartOfDay(port.getZoneId()).plusHours(CanalBookingSlot.BOOKING_HOURS_OFFSET));

					final IRouteOptionBooking oBooking;
					Set<Vessel> eVessels;
					Set<IVessel> oVessels;
					if (eBooking.getVessel() != null) {
						eVessels = SetUtils.getObjects(eBooking.getVessel());
						oVessels = getOptimiserVessels(eVessels);
					} else if (eBooking.getBookingCode() != null) {
						eVessels = SetUtils.getObjects(eBooking.getBookingCode().getVesselGroup());
						oVessels = getOptimiserVessels(eVessels);
					} else {
						// Otherwise empty set, indicating booking can be used for any vessel.
						oVessels = Collections.emptySet();
					}

					final @NonNull ECanalEntry oCanalEntrance = mapCanalEntry(eBooking.getCanalEntrance());

					if (oVessels != null && !oVessels.isEmpty()) {
						oBooking = IRouteOptionBooking.of(date, oCanalEntrance, ERouteOption.PANAMA, oVessels);
					} else {
						oBooking = IRouteOptionBooking.of(date, oCanalEntrance, ERouteOption.PANAMA);
					}
					panamaSlots.computeIfAbsent(oCanalEntrance, key -> new LinkedList<>()).add(oBooking);

					modelEntityMap.addModelObject(eBooking, oBooking);
				}

				// Sort bookings by date.
				panamaSlots.values().forEach(Collections::sort);
				panamaBookingsProviderEditor.setBookings(panamaSlots);
			}
			panamaBookingsProviderEditor.setArrivalMargin(canalBookings.getArrivalMarginHours());

			final List<PanamaSeasonalityRecord> seasonalityRecords = this.canalBookings.getPanamaSeasonalityRecords();
			final List<PanamaSeasonalityCurve> seasonalityCurves = new ArrayList<>();
			final int startYear;
			{
				final int earliestScenarioYear = modelEntityMap.getEarliestTime().toLocalDate().getYear();
				// Use year before since optimiser make subtract travel times in internal calculations.
				startYear = earliestScenarioYear - 1;
			}
			final int endYear;
			{
				final int latestScenarioYear = modelEntityMap.getLatestTime().toLocalDate().getYear();
				// adding couple of more years as a buffer
				endYear = latestScenarioYear + 2;
			}

			for (final VesselGroupCanalParameters vgcp : this.canalBookings.getVesselGroupCanalParameters()) {
				final Set<Vessel> vessels = SetUtils.getObjects(vgcp.getVesselGroup());
				if (!vgcp.getVesselGroup().isEmpty() && vessels.isEmpty()) {
					// Redundant transformation - skip. Also, allowed to not have seasonality entries for these groups since it makes no difference to optimisation.
					continue;
				}

				final List<PanamaSeasonalityRecord> ePsrs = seasonalityRecords.stream() //
						.filter(psr -> vgcp == psr.getVesselGroupCanalParameter()) //
						.toList();

				final List<PanamaSeasonalityRecord> nonYearPsrs = ePsrs.stream() //
						.filter(psr -> !psr.isSetStartYear()) //
						.collect(Collectors.toCollection(ArrayList::new));
				nonYearPsrs.sort((psr1, psr2) -> {
					final int monthCompare = Integer.compare(psr1.getStartMonth(), psr2.getStartMonth());
					return monthCompare != 0 ? monthCompare : Integer.compare(psr1.getStartDay(), psr2.getStartDay());
				});

				final List<PanamaSeasonalityRecord> withYearPsrs = ePsrs.stream() //
						.filter(psr -> psr.isSetStartYear()) //
						.collect(Collectors.toCollection(ArrayList::new));
				withYearPsrs.sort((psr1, psr2) -> getStartDate(psr1).compareTo(getStartDate(psr2)));

				// Note: There is a difference between vessels.size() == 0 and
				// vesselGroupsMaxIdles.getVesselGroup().size() == 0.
				// The first means there are no vessels belonging to the assigned groups. The
				// latter means there were no vessels or groups set.

				if (ePsrs.isEmpty()) {
					throw new IllegalStateException(String.format("Panama vessel group %s should have at least one waiting days entry", ScenarioElementNameHelper.getName(vgcp, "<Unknown>")));
				}
				final List<IVessel> oVessels = new LinkedList<>();
				if (vgcp.getVesselGroup().isEmpty()) {
					oVessels.add(null);
				} else {
					for (final Vessel eVessel : vessels) {
						final IVessel oVessel = modelEntityMap.getOptimiserObject(eVessel, IVessel.class);
						oVessels.add(oVessel);
					}
				}

				// Reuse waiting days for each vessel seasonality curve
				final SequencedWaitingDays sequencedWaitingDays = buildSequencedWaitingDays(startYear, endYear, nonYearPsrs, withYearPsrs);
				oVessels.forEach(oVessel -> addPanamaSeasonalityCurve(seasonalityCurves, oVessel, sequencedWaitingDays));
			}
			// Should NOT really happen. Unless scenario is broken or more validation
			// required
			if (seasonalityCurves.isEmpty()) {
				// Exists for some tests - should really throw an exception here.
				final SequencedWaitingDays sequencedWaitingDays = transformToSequencedWaitingDays(startYear, endYear,
						Collections.singletonList(RollingSeasonalityContainer.buildGlobalContainer(0, 0)));
				addPanamaSeasonalityCurve(seasonalityCurves, null, sequencedWaitingDays);
				// throw new IllegalStateException("No seasonalities generated");
			}
			panamaBookingsProviderEditor.setPanamaMaxIdleDays(seasonalityCurves);
		}
	}

	private @NonNull List<@NonNull ISeasonalityChangePointTripleContainer> buildSequencedSeasonalityContainers(final @NonNull List<@NonNull PanamaSeasonalityRecord> sortedNonYearPsrs,
			final @NonNull List<@NonNull PanamaSeasonalityRecord> sortedWithYearPsrs) {
		final Integer firstConcreteYear;
		if (!sortedWithYearPsrs.isEmpty()) {
			final PanamaSeasonalityRecord firstWithYearPsr = sortedWithYearPsrs.get(0);
			firstConcreteYear = firstWithYearPsr.getStartYear();
		} else {
			firstConcreteYear = null;
		}
		final @NonNull List<@NonNull ISeasonalityChangePointTripleContainer> sequencedSeasonalityContainers = new ArrayList<>(3);
		final ISeasonalityChangePointTripleContainer firstRollingSeason;
		if (sortedNonYearPsrs.isEmpty()) {
			if (firstConcreteYear == null) {
				// No waiting day information at all - default to zero waiting days
				firstRollingSeason = RollingSeasonalityContainer.buildGlobalContainer(0, 0);
			} else {
				// Only concrete data provided - pre-concrete waiting days is zero and stops at the first concrete year
				firstRollingSeason = RollingSeasonalityContainer.buildLeftSideContainer(firstConcreteYear, 0, 0);
			}
		} else {
			if (firstConcreteYear == null) {
				// Only non-year information - all seasons are globally rolling
				firstRollingSeason = RollingSeasonalityContainer.buildGlobalContainer(sortedNonYearPsrs);
			} else {
				// Have both pre-concrete waiting days and concrete waiting days - build a left side rolling container that stops at the first concrete year
				firstRollingSeason = RollingSeasonalityContainer.buildLeftSideContainer(firstConcreteYear, sortedNonYearPsrs);
			}
		}
		sequencedSeasonalityContainers.add(firstRollingSeason);
		if (!sortedWithYearPsrs.isEmpty()) {
			// Want to rollover initial waiting days if concrete waiting days don't start on 1 Jan
			final int rolloverNbWaitingDays;
			final int rolloverSbWaitingDays;
			{
				// Not ideal but only run at transformation so not an issue in the grand scheme of things
				final @NonNull List<@NonNull PanamaWaitingDaysTriple> triples = firstRollingSeason.getChangePointsFor(firstConcreteYear - 1, dateAndCurveHelper);
				final PanamaWaitingDaysTriple lastTriple = triples.get(triples.size() - 1);
				rolloverNbWaitingDays = lastTriple.northboundWaitingDays();
				rolloverSbWaitingDays = lastTriple.southboundWaitingDays();
			}
			sequencedSeasonalityContainers.add(CollectedConcreteSeasonsContainer.from(sortedWithYearPsrs, rolloverNbWaitingDays, rolloverSbWaitingDays));
		}

		// build rolling seasons that roll on the right
		final ISeasonalityChangePointTripleContainer finalSeasonalityContainer = sequencedSeasonalityContainers.get(sequencedSeasonalityContainers.size() - 1).buildRightSideRollingChangePoints();
		if (finalSeasonalityContainer != null) {
			sequencedSeasonalityContainers.add(finalSeasonalityContainer);
		}
		return sequencedSeasonalityContainers;
	}

	private @NonNull ISeasonalityChangePointTripleContainer getNextSeasonalityContainer(final int currentYear,
			final @NonNull Iterator<@NonNull ISeasonalityChangePointTripleContainer> iterTripleContainers) {
		while (iterTripleContainers.hasNext()) {
			final ISeasonalityChangePointTripleContainer nextSeasonality = iterTripleContainers.next();
			if (nextSeasonality.appliesToYear(currentYear)) {
				return nextSeasonality;
			}
		}
		throw new IllegalStateException("Could not find seasonality for required year");
	}

	private SequencedWaitingDays transformToSequencedWaitingDays(int startYear, int endYear, List<ISeasonalityChangePointTripleContainer> tripleContainers) {
		// build yearly sequenced triples
		final Iterator<@NonNull ISeasonalityChangePointTripleContainer> iterTripleContainers = tripleContainers.iterator();
		ISeasonalityChangePointTripleContainer currentSeasonalityContainer = iterTripleContainers.next();
		final List<PanamaWaitingDaysTriple> sequencedTriples = new LinkedList<>();
		for (int year = startYear; year <= endYear; ++year) {
			if (!currentSeasonalityContainer.appliesToYear(year)) {
				currentSeasonalityContainer = getNextSeasonalityContainer(year, iterTripleContainers);
			}
			sequencedTriples.addAll(currentSeasonalityContainer.getChangePointsFor(year, dateAndCurveHelper));
		}

		// compress changepoints, i.e. if consecutive seasons have the same waiting days, remove the latter.
		final Iterator<PanamaWaitingDaysTriple> iterChangePointTriples = sequencedTriples.iterator();
		PanamaWaitingDaysTriple previousTriple = iterChangePointTriples.next();
		while (iterChangePointTriples.hasNext()) {
			final PanamaWaitingDaysTriple currentTriple = iterChangePointTriples.next();
			if (previousTriple.waitingDaysEqual(currentTriple)) {
				iterChangePointTriples.remove();
			} else {
				previousTriple = currentTriple;
			}
		}

		// Construct the final arrays of changepoints/waiting days
		final int[] changePoints = new int[sequencedTriples.size()];
		final int[] northboundWaitingDays = new int[sequencedTriples.size()];
		final int[] southboundWaitingDays = new int[sequencedTriples.size()];

		int i = 0;
		for (final PanamaWaitingDaysTriple triple : sequencedTriples) {
			changePoints[i] = triple.changePoint();
			northboundWaitingDays[i] = triple.northboundWaitingDays();
			southboundWaitingDays[i] = triple.southboundWaitingDays();
			++i;
		}
		return new SequencedWaitingDays(changePoints, northboundWaitingDays, southboundWaitingDays);
	}

	private SequencedWaitingDays buildSequencedWaitingDays(int startYear, int endYear, final List<PanamaSeasonalityRecord> nonYearPsrs, final List<PanamaSeasonalityRecord> withYearPsrs) {
		final @NonNull List<@NonNull ISeasonalityChangePointTripleContainer> sequencedSeasonalityContainers = buildSequencedSeasonalityContainers(nonYearPsrs, withYearPsrs);
		if (sequencedSeasonalityContainers.isEmpty()) {
			throw new IllegalStateException("Seasonality data not present");
		}
		return transformToSequencedWaitingDays(startYear, endYear, sequencedSeasonalityContainers);
	}

	private void addPanamaSeasonalityCurve(final List<PanamaSeasonalityCurve> seasonalityCurves, final IVessel vessel, final SequencedWaitingDays waitingDays) {
		seasonalityCurves.add(new PanamaSeasonalityCurve(vessel, waitingDays.changePoints(), waitingDays.northboundWaitingDays(), waitingDays.southboundWaitingDays()));
	}

	private Set<IVessel> getOptimiserVessels(final Set<Vessel> eVessels) {
		final Set<IVessel> oVessels = new HashSet<>();
		for (final Vessel vessel : eVessels) {
			final IVessel oVessel = modelEntityMap.getOptimiserObject(vessel, IVessel.class);
			oVessels.add(oVessel);
		}
		return oVessels;
	}

	private static @NonNull ECanalEntry mapCanalEntry(final CanalEntry canalEntrance) {
		switch (canalEntrance) {
		case NORTHSIDE:
			return ECanalEntry.NorthSide;
		case SOUTHSIDE:
			return ECanalEntry.SouthSide;
		}
		throw new IllegalArgumentException();
	}

	private static @NonNull LocalDate getStartDate(final @NonNull PanamaSeasonalityRecord psr) {
		if (psr.getStartYear() == 0) {
			throw new IllegalArgumentException("Panama season start date: year missing");
		}
		if (psr.getStartDay() < 1 || psr.getStartDay() > 31) {
			throw new IllegalArgumentException("Panama season start date: invalid start date");
		}
		return LocalDate.of(psr.getStartYear(), psr.getStartMonth(), psr.getStartDay());
	}

}
