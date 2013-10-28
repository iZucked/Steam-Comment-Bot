package com.mmxlabs.models.lng.transformer.extensions.redirection;

import com.google.inject.Inject;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.EndPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class RedirectionVoyageCostCalculator implements IVoyageCostCalculator {

	
	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	private final ICurve salesPriceCurve;

	
	public RedirectionVoyageCostCalculator(ICurve salesPrice) {
		salesPriceCurve = salesPrice;	
	}
	
	@Override
	public VoyagePlan calculateShippingCosts(
			ILoadPriceCalculator loadPriceCalculator, IPort loadPort,
			IPort dischargePort, int loadTime, long loadVolumeInM3,
			IVessel vessel, int cargoCVValue, String route) {
	
		final VoyagePlan notionalPlan = new VoyagePlan();
	
		final Integer distance = distanceProvider.get(route).get(loadPort, dischargePort);
		if (distance == null || distance.intValue() == Integer.MAX_VALUE) {
			return null;
		}
	
		// Generate a notional voyage plan
		final int travelTime = getNotionalSpeed() == 0 ? 0 : Calculator.getTimeFromSpeedDistance(getNotionalSpeed(), distance);
		// TODO: Extend API to contain port default
		final int notionalLoadDuration = 24;// durationProvider.getDefaultValue();
		final int notionalDischargeDuration = 24;// durationProvider.getDefaultValue();
	
		// Determine notional port visit times.
		final int notionalDischargeTime = loadTime + notionalLoadDuration + travelTime;
		final int notionalReturnTime = notionalDischargeTime + notionalDischargeDuration + travelTime;
		final int[] arrivalTimes = new int[] { loadTime, notionalDischargeTime, notionalReturnTime };
	
		final LoadSlot notionalLoadSlot = new LoadSlot();
		notionalLoadSlot.setPort(loadPort);
		notionalLoadSlot.setTimeWindow(new TimeWindow(loadTime, loadTime));
		notionalLoadSlot.setLoadPriceCalculator(loadPriceCalculator);
		notionalLoadSlot.setCargoCVValue(cargoCVValue);
		notionalLoadSlot.setCooldownForbidden(true);
		notionalLoadSlot.setMaxLoadVolume(loadVolumeInM3);
		notionalLoadSlot.setMinLoadVolume(loadVolumeInM3);
	
		final DischargeSlot notionalDischargeSlot = new DischargeSlot();
		notionalDischargeSlot.setPort(dischargePort);
		notionalDischargeSlot.setTimeWindow(new TimeWindow(notionalDischargeTime, notionalDischargeTime));
		notionalDischargeSlot.setDischargePriceCalculator(new MarketPriceContract(salesPriceCurve, 0, OptimiserUnitConvertor.convertToInternalConversionFactor(1)));
	
		final PortSlot notionalReturnSlot = new EndPortSlot();
		notionalReturnSlot.setPort(loadPort);
		notionalReturnSlot.setTimeWindow(new TimeWindow(notionalReturnTime, notionalReturnTime));
	
		// Calculate new voyage requirements
		// TODO: Optimise over fuel choices using a VPO?
		{
			final VoyageDetails ladenDetails = new VoyageDetails();
			{
				final VoyageOptions ladenOptions = new VoyageOptions();
				ladenOptions.setAvailableTime(travelTime);
				ladenOptions.setAllowCooldown(false);
				ladenOptions.setAvailableLNG(travelTime);
				ladenOptions.setDistance(distance);
				ladenOptions.setFromPortSlot(notionalLoadSlot);
				ladenOptions.setNBOSpeed(getNotionalSpeed());
				ladenOptions.setRoute(route);
				ladenOptions.setShouldBeCold(true);
				ladenOptions.setToPortSlot(notionalDischargeSlot);
				ladenOptions.setUseFBOForSupplement(false);
				ladenOptions.setUseNBOForIdle(true);
				ladenOptions.setUseNBOForTravel(true);
				ladenOptions.setVessel(vessel);
				ladenOptions.setVesselState(VesselState.Laden);
				ladenOptions.setWarm(false);
	
				voyageCalculator.calculateVoyageFuelRequirements(ladenOptions, ladenDetails);
			}
			final VoyageDetails ballastDetails = new VoyageDetails();
			{
				final VoyageOptions ballastOptions = new VoyageOptions();
				ballastOptions.setAvailableTime(travelTime);
				ballastOptions.setAllowCooldown(false);
				ballastOptions.setAvailableLNG(travelTime);
				ballastOptions.setDistance(distance);
				ballastOptions.setFromPortSlot(notionalDischargeSlot);
				ballastOptions.setNBOSpeed(getNotionalSpeed());
				ballastOptions.setRoute(route);
				ballastOptions.setShouldBeCold(true);
				ballastOptions.setToPortSlot(notionalReturnSlot);
				ballastOptions.setUseFBOForSupplement(false);
				ballastOptions.setUseNBOForIdle(true);
				ballastOptions.setUseNBOForTravel(true);
				ballastOptions.setVessel(vessel);
				ballastOptions.setVesselState(VesselState.Ballast);
				ballastOptions.setWarm(false);
	
				voyageCalculator.calculateVoyageFuelRequirements(ballastOptions, ballastDetails);
			}
	
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
			voyageCalculator.calculateVoyagePlan(notionalPlan, vessel, CollectionsUtil.toArrayList(arrivalTimes), sequence);
	
			return notionalPlan;
		}
	
	}


	private int getNotionalSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
