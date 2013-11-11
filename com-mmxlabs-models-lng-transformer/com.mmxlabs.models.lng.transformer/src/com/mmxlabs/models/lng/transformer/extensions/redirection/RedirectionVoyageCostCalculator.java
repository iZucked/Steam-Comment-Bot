package com.mmxlabs.models.lng.transformer.extensions.redirection;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortVisitDurationProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class RedirectionVoyageCostCalculator extends AbstractVoyageCostCalculator {

	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IPortVisitDurationProvider portVisitDurationProvider;

	@Override
	public VoyagePlan calculateShippingCosts(@NonNull final IPort loadPort, @NonNull final IPort dischargePort, final int loadTime, final int dischargeTime, @NonNull final IVessel vessel,
			final int notionalSpeed, final int cargoCVValue, @NonNull final String route, final int baseFuelPricePerMT, @NonNull final ISalesPriceCalculator salesPrice) {

		final VoyagePlan notionalPlan = new VoyagePlan();

		final Integer distance = distanceProvider.get(route).get(loadPort, dischargePort);
		if (distance == null || distance.intValue() == Integer.MAX_VALUE) {
			return null;
		}

		// Generate a notional voyage plan
		final int travelTime = Calculator.getTimeFromSpeedDistance(notionalSpeed, distance);
		// TODO: Use actual if present
		final int notionalLoadDuration = portVisitDurationProvider.getVisitDuration(loadPort, PortType.Load);
		final int notionalDischargeDuration = portVisitDurationProvider.getVisitDuration(dischargePort, PortType.Discharge);

		// Determine notional port visit times.
		final int notionalReturnTime = dischargeTime + notionalDischargeDuration + travelTime;
		final int[] arrivalTimes = new int[] { loadTime, dischargeTime, notionalReturnTime };

		final LoadSlot notionalLoadSlot = new LoadSlot();
		notionalLoadSlot.setPort(loadPort);
		notionalLoadSlot.setTimeWindow(new TimeWindow(loadTime, loadTime));
		notionalLoadSlot.setCargoCVValue(cargoCVValue);
		notionalLoadSlot.setCooldownForbidden(true);
		notionalLoadSlot.setMaxLoadVolume(vessel.getCargoCapacity());
		notionalLoadSlot.setMinLoadVolume(vessel.getCargoCapacity());

		final DischargeSlot notionalDischargeSlot = new DischargeSlot();
		notionalDischargeSlot.setPort(dischargePort);
		notionalDischargeSlot.setTimeWindow(new TimeWindow(dischargeTime, dischargeTime));
		notionalDischargeSlot.setDischargePriceCalculator(salesPrice);

		final PortSlot notionalReturnSlot = new EndPortSlot();
		notionalReturnSlot.setPort(loadPort);
		notionalReturnSlot.setTimeWindow(new TimeWindow(notionalReturnTime, notionalReturnTime));

		// Calculate new voyage requirements
		{
			final VoyageDetails ladenDetails = calculateVoyageDetails(VesselState.Laden, vessel, route, distance, dischargeTime - notionalLoadDuration - loadTime, notionalLoadSlot,
					notionalDischargeSlot);

			final VoyageDetails ballastDetails = calculateVoyageDetails(VesselState.Ballast, vessel, route, distance, notionalReturnTime - notionalDischargeDuration - dischargeTime,
					notionalDischargeSlot, notionalReturnSlot);

			final PortDetails loadDetails = new PortDetails();
			loadDetails.setOptions(new PortOptions());
			loadDetails.getOptions().setPortSlot(notionalLoadSlot);
			loadDetails.getOptions().setVisitDuration(notionalLoadDuration);

			final PortDetails dischargeDetails = new PortDetails();
			dischargeDetails.setOptions(new PortOptions());
			dischargeDetails.getOptions().setPortSlot(notionalDischargeSlot);
			dischargeDetails.getOptions().setVisitDuration(notionalDischargeDuration);

			final PortDetails returnDetails = new PortDetails();
			returnDetails.setOptions(new PortOptions());
			returnDetails.getOptions().setPortSlot(notionalReturnSlot);
			returnDetails.getOptions().setVisitDuration(0);

			final Object[] sequence = new Object[] { loadDetails, ladenDetails, dischargeDetails, ballastDetails, returnDetails };
			notionalPlan.setSequence(sequence);
			voyageCalculator.calculateVoyagePlan(notionalPlan, vessel, baseFuelPricePerMT, CollectionsUtil.toArrayList(arrivalTimes), sequence);

			return notionalPlan;
		}
	}
}
