/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptionsPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.providers.IPortCVProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;

/**
 * Implementation of {@link ILNGVoyageCalculator}.
 * 
 * @author Simon Goodall
 * 
 */
public final class LNGVoyageCalculator implements ILNGVoyageCalculator {

	@Inject
	private IRouteCostProvider routeCostProvider;

	@Inject
	private IPortCVProvider portCVProvider;

	@Override
	public final void init() {
		if (routeCostProvider == null) {
			throw new IllegalStateException("Route Cost Provider is not set");
		}
		if (portCVProvider == null) {
			throw new IllegalStateException("Port CV Provider is not set");
		}
	}
	
	private List<Integer> loadIndicesStorage = new ArrayList<Integer>();
	private List<Integer> dischargeIndicesStorage = new ArrayList<Integer>();
	
	/**
	 * Calculate the fuel requirements between a pair of {@link IPortSlot}s. The {@link VoyageOptions} provides the specific choices to evaluate for this voyage (e.g. fuel choice, route, ...).
	 * 
	 * @param vessel
	 * @param from
	 * @param to
	 * @param options
	 * @param output
	 */
	@Override
	public final void calculateVoyageFuelRequirements(final VoyageOptions options, final VoyageDetails output) {
		output.setOptions(options);

		final IVessel vessel = options.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();
		final VesselState vesselState = options.getVesselState();

		final int equivalenceFactorM3ToMT = vesselClass.getBaseFuelConversionFactor();

		// Get distance for the route choice
		final long distance = options.getDistance();

		/**
		 * How much of the time given to us by the scheduler has to be spent travelling by an alternative route.
		 */
		final int additionalRouteTimeInHours = routeCostProvider.getRouteTransitTime(options.getRoute(), vesselClass);

		/**
		 * How much time is available to cover the distance, excluding time which must be spent traversing any canals
		 */
		final int availableTimeInHours = options.getAvailableTime() - additionalRouteTimeInHours;

		// Calculate speed
		// cast to int. as if long is required, then what are we doing?
		int speed;
		if (options.isCharterOutIdleTime()) {
			speed = vesselClass.getMaxSpeed();
		} else {
			speed = availableTimeInHours == 0 ? 0 : Calculator.speedFromDistanceTime(distance, availableTimeInHours);

			// speed calculation is not always correct - with a linear consumption
			// curve on base fuel for example, the best option is always either maximum speed
			// or minimum speed.

			if (distance != 0) {
				// Check NBO speed
				if (options.useNBOForTravel()) {
					final int nboSpeed = options.getNBOSpeed();
					if (speed < nboSpeed) {
						speed = nboSpeed;
					}
				}

				// Clamp speed to min bound
				final int minSpeed = vesselClass.getMinSpeed();
				if (speed < minSpeed) {
					speed = minSpeed;
				}

				// Clamp speed to max bound
				final int maxSpeed = vesselClass.getMaxSpeed();
				if (speed > maxSpeed) {
					speed = maxSpeed;
				}

				assert minSpeed <= maxSpeed;
				assert speed != 0;
			}
		}
		output.setSpeed(speed);
		// Calculate total, travel and idle time

		// May be longer than available time
		final int travelTimeInHours = speed == 0 ? 0 : Calculator.getTimeFromSpeedDistance(speed, distance);
		final int idleTimeInHours = Math.max(0, availableTimeInHours - travelTimeInHours);

		// We output travel time + canal time, but the calculations
		// below only need to care about travel time
		output.setTravelTime(travelTimeInHours + additionalRouteTimeInHours);
		output.setIdleTime(idleTimeInHours);

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this journey
		 */
		final long consuptionRateInMTPerHour = speed == 0 ? 0 : vesselClass.getConsumptionRate(vesselState).getRate(speed);
		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consuptionRateInMTPerHour, travelTimeInHours);

		// Calculate fuel requirements
		if (options.useNBOForTravel()) {
			final long nboRateInM3PerHour = vesselClass.getNBORate(vesselState);
			/**
			 * The total quantity of LNG inevitably boiled off in this journey, in M3
			 */
			final long nboProvidedInM3 = Calculator.quantityFromRateTime(nboRateInM3PerHour, travelTimeInHours);
			/**
			 * The total quantity of LNG inevitably boiled off in this journey, in MT. Normally less than the amount boiled off in M3
			 */
			final long nboProvidedInMT = Calculator.convertM3ToMT(nboProvidedInM3, equivalenceFactorM3ToMT);

			if (nboProvidedInMT < requiredConsumptionInMT) {
				/**
				 * How many MT of base-fuel-or-equivalent are required after the NBO amount has been used
				 */
				final long diffInMT = requiredConsumptionInMT - nboProvidedInMT;
				if (options.useFBOForSupplement()) {
					// Use FBO for remaining quantity
					final long diffInM3 = Calculator.convertMTToM3(diffInMT, equivalenceFactorM3ToMT);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, diffInM3);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, diffInMT);
					output.setFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, 0);
				} else {
					// Use base for remaining quantity
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
					output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
					output.setFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, diffInMT);
				}
			}

			// TODO There is an edge case here where the supplemental base is less than pilot light
			// in which case we ought to bump it up to the right amount.
			if (output.getFuelConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT) == 0) {
				final long pilotLightRateINMTPerHour = vesselClass.getPilotLightRate();
				final long pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerHour, travelTimeInHours);
				output.setFuelConsumption(FuelComponent.PilotLight, FuelUnit.MT, pilotLightConsumptionInMT);
			}

			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, nboProvidedInM3);
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.MT, nboProvidedInMT);
		} else {
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
			output.setFuelConsumption(FuelComponent.NBO, FuelUnit.MT, 0);
			output.setFuelConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
			output.setFuelConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
			output.setFuelConsumption(FuelComponent.Base, FuelUnit.MT, requiredConsumptionInMT);
			output.setFuelConsumption(FuelComponent.PilotLight, FuelUnit.MT, 0);
		}
		if (!options.isCharterOutIdleTime()) {
			final long idleNBORateInM3PerHour = vesselClass.getIdleNBORate(vesselState);

			if (options.useNBOForIdle()) {
				final long nboRequiredInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerHour, idleTimeInHours);
				final long nboRequiredInMT = Calculator.convertM3ToMT(nboRequiredInM3, equivalenceFactorM3ToMT);

				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, nboRequiredInM3);
				output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, nboRequiredInMT);
				output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, 0);

				final long idlePilotLightRateINMTPerHour = vesselClass.getIdlePilotLightRate();
				final long idlePilotLightConsumptionInMT = Calculator.quantityFromRateTime(idlePilotLightRateINMTPerHour, idleTimeInHours);
				output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, idlePilotLightConsumptionInMT);
			} else {
				final long idleRateInMTPerHour = vesselClass.getIdleConsumptionRate(vesselState);

				int remainingIdleTimeInHours = idleTimeInHours;

				// long idleConsumptionInMT = Calculator.quantityFromRateTime(idleRateInMTPerHour, idleTimeInHours);

				final long cooldownVolume = vesselClass.getCooldownVolume();
				if (options.useNBOForTravel()) {
					// Run down boil off after travel. On ballast voyages running on
					// NBO, It is necessary to keep a minimum heel of LNG whilst
					// travelling. Once in port, the heel can boil-off as there is
					// no need to keep it around. Base fuel requirements for idling
					// are much less than that provided by boil-off.

					// There is more boil-off than required consumption. We need to
					// work out how long we could provide energy for based on
					// boil-off time rather than use the raw quantity directly.
					long heelInM3 = vesselClass.getMinHeel();

					// How long will the boil-off last
					int deltaTimeInHours = Calculator.getTimeFromRateQuantity(idleNBORateInM3PerHour, heelInM3);
					// If delta idle time is greater than the current idle time, only count current idle time use.
					if (deltaTimeInHours > idleTimeInHours) {
						deltaTimeInHours = idleTimeInHours;
						heelInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerHour, deltaTimeInHours);
					}

					// TODO: Review this code block regarding cooldown logic
					if (options.shouldBeCold()) {
						/**
						 * How many hours the vessel has been empty and warming up.
						 */
						final int warmingTimeInHours = idleTimeInHours - deltaTimeInHours;

						if (warmingTimeInHours > vesselClass.getWarmupTime()) {
							if (options.getAllowCooldown() && (idleTimeInHours > (vesselClass.getWarmupTime() + vesselClass.getCooldownTime()))) {
								output.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, cooldownVolume);
								// don't use any idle base during the cooldown
								remainingIdleTimeInHours -= vesselClass.getCooldownTime();
							} else {
								// warming time = idle - delta
								// therefore we need
								// idle - delta = vesselClass.getWarmupTime();
								// delta = idle - warmup
								deltaTimeInHours = idleTimeInHours - vesselClass.getWarmupTime();
								heelInM3 = Calculator.quantityFromRateTime(idleNBORateInM3PerHour, deltaTimeInHours);
							}
						}
					}

					remainingIdleTimeInHours -= deltaTimeInHours;

					final long heelInMT = Calculator.convertM3ToMT(heelInM3, equivalenceFactorM3ToMT);
					output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, heelInM3);
					output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, heelInMT);

					final long idlePilotLightRateINMTPerHour = vesselClass.getIdlePilotLightRate();
					final long idlePilotLightConsumptionInMT = Calculator.quantityFromRateTime(idlePilotLightRateINMTPerHour, deltaTimeInHours);
					output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, idlePilotLightConsumptionInMT);
				} else {
					output.setFuelConsumption(FuelComponent.IdlePilotLight, FuelUnit.MT, 0);
					output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3, 0);
					output.setFuelConsumption(FuelComponent.IdleNBO, FuelUnit.MT, 0);

					if (options.shouldBeCold()) {
						// we can only do a cooldown here, because there is no LNG.
						// this situation shouldn't get presented to us by the
						// sequence scheduler unless it's unavoidable.
						if (options.isWarm() || (options.getAvailableTime() > vesselClass.getWarmupTime())) {
							output.setFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3, cooldownVolume);
							remainingIdleTimeInHours -= vesselClass.getCooldownTime();
						}
					}
				}

				if (remainingIdleTimeInHours > 0) {
					// Use base for remaining quantity
					final long idleConsumptionInMT = Calculator.quantityFromRateTime(idleRateInMTPerHour, remainingIdleTimeInHours);
					output.setFuelConsumption(FuelComponent.IdleBase, FuelUnit.MT, idleConsumptionInMT);
				}
			}
		}
		// Route Additional Consumption
		/**
		 * Base fuel requirement for canal traversal
		 */
		final long routeRequiredConsumptionInMT = Calculator.quantityFromRateTime(routeCostProvider.getRouteFuelUsage(options.getRoute(), vesselClass, vesselState), additionalRouteTimeInHours);
		if (routeRequiredConsumptionInMT > 0) {

			if (options.useNBOForTravel()) {

				final long nboRouteRateInM3PerHour = routeCostProvider.getRouteNBORate(options.getRoute(), vesselClass, vesselState);

				/**
				 * How much NBO is produced while in the canal (M3)
				 */
				final long routeNboProvidedInM3 = Calculator.quantityFromRateTime(nboRouteRateInM3PerHour, additionalRouteTimeInHours);
				/**
				 * How much NBO is produced while in the canal (MTBFE)
				 */
				final long routeNboProvidedInMT = Calculator.convertM3ToMT(routeNboProvidedInM3, equivalenceFactorM3ToMT);

				/**
				 * How much FBO is produced while in the canal (M3)
				 */
				long routeFboProvidedInM3;
				/**
				 * How much FBO is produced while in the canal (MTBFE)
				 */
				long routeFboProvidedInMT;

				/**
				 * How much supplement is needed over and above NBO while in the canal (MTBFE)
				 */
				final long routeDiffInMT;

				/**
				 * Consumed pilot light
				 */
				final long pilotLightConsumptionInMT;

				if (routeNboProvidedInMT < routeRequiredConsumptionInMT) {
					// Need to supplement
					if (options.useFBOForSupplement()) {
						routeDiffInMT = 0;
						routeFboProvidedInMT = routeRequiredConsumptionInMT - routeNboProvidedInMT;
						routeFboProvidedInM3 = Calculator.convertMTToM3(routeFboProvidedInMT, equivalenceFactorM3ToMT);

						final long pilotLightRateINMTPerHour = vesselClass.getPilotLightRate();
						pilotLightConsumptionInMT = Calculator.quantityFromRateTime(pilotLightRateINMTPerHour, additionalRouteTimeInHours);

					} else {
						routeDiffInMT = routeRequiredConsumptionInMT - routeNboProvidedInMT;
						routeFboProvidedInMT = 0;
						routeFboProvidedInM3 = 0;
						pilotLightConsumptionInMT = 0;
					}

				} else {
					routeDiffInMT = 0;
					routeFboProvidedInMT = 0;
					routeFboProvidedInM3 = 0;
					pilotLightConsumptionInMT = 0;
				}

				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.M3, routeNboProvidedInM3);
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.MT, routeNboProvidedInMT);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.M3, routeFboProvidedInM3);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.MT, routeFboProvidedInMT);
				output.setRouteAdditionalConsumption(FuelComponent.Base_Supplemental, FuelUnit.MT, routeDiffInMT);

				output.setRouteAdditionalConsumption(FuelComponent.PilotLight, FuelUnit.MT, pilotLightConsumptionInMT);
			} else {
				// Base fuel only
				output.setRouteAdditionalConsumption(FuelComponent.Base, FuelUnit.MT, routeRequiredConsumptionInMT);
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.M3, 0);
				output.setRouteAdditionalConsumption(FuelComponent.NBO, FuelUnit.MT, 0);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.M3, 0);
				output.setRouteAdditionalConsumption(FuelComponent.FBO, FuelUnit.MT, 0);
				output.setRouteAdditionalConsumption(FuelComponent.PilotLight, FuelUnit.MT, 0);
			}
		}
		output.setRouteCost(routeCostProvider.getRouteCost(options.getRoute(), vesselClass, vesselState));
	}

	/**
	 * Calculates the fuel consumptions for a sequence of alternating PortDetails / VoyageDetails
	 * objects, populating the fuel price fields of those objects and returning a total consumption
	 * array. 
	 * 
	 * @param vessel
	 * @param sequence
	 * @return
	 * 
	 * @author Simon McGregor
	 */
	public long [] calculateVoyagePlanFuelConsumptions(final IVessel vessel, final Object... sequence) { 
		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
		final long[] fuelConsumptions = new long[FuelComponent.values().length];

		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];

				// DO NOT INCLUDE THE LAST SEQUENCE ELEMENT IN THE TOTAL COST FOR THE PLAN
				if (i < sequence.length - 1) {
					for (final FuelComponent fc : FuelComponent.values()) {
						fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc);
					}
					details.setFuelUnitPrice(FuelComponent.Base, baseFuelPricePerMT);
				}

			} else {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];

				for (final FuelComponent fc : FuelComponent.values()) {
					fuelConsumptions[fc.ordinal()] += details.getFuelConsumption(fc, fc.getDefaultFuelUnit());
					fuelConsumptions[fc.ordinal()] += details.getRouteAdditionalConsumption(fc, fc.getDefaultFuelUnit());
				}

				details.setFuelUnitPrice(FuelComponent.Base, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.Base_Supplemental, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.IdleBase, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.PilotLight, baseFuelPricePerMT);
				details.setFuelUnitPrice(FuelComponent.IdlePilotLight, baseFuelPricePerMT);

			}
		}
		return fuelConsumptions;	
	}
	
	public List<Integer> findLoadIndices(List<Integer> storage, Object... sequence) {
		if (storage == null) {
			storage = new ArrayList<Integer>();
		}
		else {
			storage.clear();
		}
		
		/* ignore the last element in the sequence, to avoid double-counting 
		 * (it will be included in the next sequence)
		 */
		for (int i = 0; i < sequence.length / 2; i++) {
			int index = i * 2;
			final PortDetails details = (PortDetails) sequence[index];
			final IPortSlot slot = details.getOptions().getPortSlot();
			if (slot instanceof ILoadSlot) {
				storage.add(index);
			} 
		}
		
		return storage;
	}
	
	public List<Integer> findDischargeIndices(List<Integer> storage, Object... sequence) {
		if (storage == null) {
			storage = new ArrayList<Integer>();
		}
		else {
			storage.clear();
		}
		
		for (int i = 0; i < sequence.length / 2; i++) {
			int index = i * 2;
			final PortDetails details = (PortDetails) sequence[index];
			final IPortSlot slot = details.getOptions().getPortSlot();
			if (slot instanceof IDischargeSlot) {
				storage.add(index);
			} 
		}
		
		return storage;
	}
	
	public int findLoadIndex(Object... sequence) {
		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getOptions().getPortSlot();
				if (slot instanceof ILoadSlot) {
					if (i == (sequence.length - 1)) {
						// End of run, so skip
					} else {
						return i;
					}
				} 
			}
		}
				
		return -1;
	}
				
	public int findDischargeIndex(Object... sequence) {
		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 0) {
				// Port Slot
				final PortDetails details = (PortDetails) sequence[i];
				final IPortSlot slot = details.getOptions().getPortSlot();
				if (slot instanceof IDischargeSlot) {
					return i;
				} 
			}
		}
				
		return -1;
	}
	
	public void sanityCheckVesselState(int loadIdx, int dischargeIdx, Object... sequence) {
		// If load or discharge has been set, then the other must be too.
		assert (loadIdx < 0 && dischargeIdx < 0) || (loadIdx >=0 && dischargeIdx >= 0);
		
		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 1) {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];

				if (loadIdx <= i && i < dischargeIdx) 
					assert(details.getOptions().getVesselState() == VesselState.Laden);
				else 
					assert(details.getOptions().getVesselState() == VesselState.Ballast);
			}
		}
		
	}

	public int calculateCooldownPrices(final IVesselClass vesselClass, final int[] arrivalTimes, Object...sequence) {
		int cooldownM3Price = 0;

		for (int i = 0; i < sequence.length; ++i) {
			if ((i & 1) == 1) {
				assert sequence[i] instanceof VoyageDetails;

				final VoyageDetails details = (VoyageDetails) sequence[i];
				if (details.getOptions().shouldBeCold() && (details.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3) > 0)) {
					final IPort port = details.getOptions().getToPortSlot().getPort();

					final int cooldownTime = arrivalTimes[i / 2] - vesselClass.getCooldownTime();

					// TODO is this how cooldown gas ought to be priced?
					if (details.getOptions().getToPortSlot() instanceof ILoadSlot) {
						// TODO double check how/if this affects caching
						// decisions
						final int cooldownPricePerMMBTU = port.getCooldownPriceCalculator().calculateCooldownUnitPrice((ILoadSlot) details.getOptions().getToPortSlot(), cooldownTime);
						cooldownM3Price = Calculator.costPerM3FromMMBTu(cooldownPricePerMMBTU, ((ILoadSlot) details.getOptions().getToPortSlot()).getCargoCVValue());
					} else {
						final int cooldownPricePerMMBTU = port.getCooldownPriceCalculator().calculateCooldownUnitPrice(cooldownTime);
						cooldownM3Price = Calculator.costPerM3FromMMBTu(cooldownPricePerMMBTU, portCVProvider.getPortCV(port));
					}

					details.setFuelUnitPrice(FuelComponent.Cooldown, cooldownM3Price);
				}
			} else {
				assert sequence[i] instanceof PortDetails;
			}
		}
		
		return cooldownM3Price;
	}
	 
	/**
	 * Returns an array of LNG prices corresponding to each index on an interleaved sequence of port / voyage details.
	 * LNG is priced at the discharge value for the next discharge, or the previous discharge if there is no next discharge
	 * in the sequence. 
	 * 
	 * This method expects either no load or discharge ports, or at least one load and at least one discharge port. Behaviour
	 * in any other situation is undefined.  
	 * 
	 * @param loadIndices
	 * @param dischargeIndices
	 * @param arrivalTimes
	 * @param sequence
	 * @return A list of LNG prices, or null if there was no way to establish LNG prices.
	 */
	public int [] getLngEffectivePrices(List<Integer> loadIndices, List<Integer> dischargeIndices, int [] arrivalTimes, final Object...sequence) {
		// TODO: does not need to be this long
		int [] result = new int [sequence.length];
		
		// require at least one load and discharge port, or no loads and no discharges
		assert((loadIndices.isEmpty()) == (dischargeIndices.isEmpty()));
		
		// no loads or discharges
		if (loadIndices.isEmpty()) {
			IPortSlot firstSlot = ((PortDetails) sequence[0]).getOptions().getPortSlot();

			// price LNG based on the heel value at the first slot
			if (firstSlot instanceof IHeelOptionsPortSlot) {
				final IHeelOptions options = ((IHeelOptionsPortSlot) firstSlot).getHeelOptions();
				if (options.getHeelLimit() > 0) {
					int price = Calculator.costPerM3FromMMBTu(options.getHeelUnitPrice(), options.getHeelCVValue());
					for (int i = 0; i < sequence.length; i++) {
						result[i] = price;
					}
					return result;
				}
			}
			// or refuse to price the LNG otherwise
			return null;
		}				
		
		// further logic is for the load / discharge case
		
		// base cargo cv value on the last load slot before a discharge slot (there will not be further load/discharge pairs, due to
		// how sequences are broken up)
		int lastLoadIndex = loadIndices.get(loadIndices.size()-1);
		int cargoCvValue = ((ILoadSlot) ((PortDetails) sequence[lastLoadIndex]).getOptions().getPortSlot()).getCargoCVValue();
		
		int prevDischargeIndex = 0;
		int lngValue = 0;
		
		// step through the discharge ports
		for (int i: dischargeIndices) {
			final IPortSlot slot = ((PortDetails) sequence[i]).getOptions().getPortSlot();
			IDischargeSlot dischargeSlot = (IDischargeSlot) slot;

			// calculate the effective LNG value based on this discharge slot 
			int dischargeUnitPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, arrivalTimes[i / 2]);
			lngValue = Calculator.costPerM3FromMMBTu(dischargeUnitPrice, cargoCvValue);
			
			// and apply the value to prices on all preceding voyages 
			for (int j = prevDischargeIndex; j < i; j++) {
				result[j] = lngValue;
			}
		}	
		
		// remember the final value coming out of the loop
		int finalLngValue = lngValue;
		
		// now apply the value from the last discharge port to all voyages following it
		int finalDischargeIndex = dischargeIndices.get(dischargeIndices.size()-1);
		for (int j = finalDischargeIndex; j < sequence.length; j++) {
			result[j] = finalLngValue;
		}					
		
		return result;
	}
	
	/**
	 * Given a sequence of {@link IPortDetails} interleaved with {@link VoyageDetails}, compute the total base fuel and LNG requirements, taking into account initial conditions, load and discharge
	 * commitments etc. It is assumed that the first and last {@link IPortDetails} will be a Loading slot or other slot where the vessel state is set to a known state. Intermediate slots are any other
	 * type of slot (e.g. one discharge, multiple waypoints, etc). If the first slot is a load slot, then this is the only reason we should see a discharge slot.
	 * 
	 * NOTE: this method intentionally ignores the last sequence element when calculating the total cost of a voyage plan, in order to avoid double-counting elements, since voyage plans are expected
	 * to come in A-B-C C-D-E E-F-G chains, where the last element of the chain does not get counted.
	 * 
	 * @param sequence
	 * @param dischargeTime
	 * @param loadTime
	 */
	@Override
	public final int calculateVoyagePlan(final VoyagePlan voyagePlan, final IVessel vessel, final int[] arrivalTimes, final Object... sequence) {
		/* TODO: instead of taking an interleaved List<Object> as a parameter, this would have a far more informative signature
		 * (and cleaner logic?) if it passed a list of IPortDetails and a list of VoyageDetails as separate parameters. Worth doing at 
		 * some point? 
		*/

		// Ensure odd number of elements
		assert (sequence.length % 2) == 1;

		final IVesselClass vesselClass = vessel.getVesselClass();

		final int loadIdx = findLoadIndex(sequence);
		final int dischargeIdx = findDischargeIndex(sequence);
		sanityCheckVesselState(loadIdx, dischargeIdx, sequence);
		
		long availableHeelinM3 = Long.MAX_VALUE;

		final long[] fuelConsumptions = calculateVoyagePlanFuelConsumptions(vessel, sequence);

		final int baseFuelPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();

		/**
		 * Accumulates route costs due to canal decisions.
		 */
		int routeCostAccumulator = 0;

		// block to force limited scope for temporary local variables 
		{
			final PortDetails details = (PortDetails) sequence[0];
			final IPortSlot slot = details.getOptions().getPortSlot();
			// set available heel if we start from a heel options slot
			if ((slot instanceof IHeelOptionsPortSlot)) {
				availableHeelinM3 = ((IHeelOptionsPortSlot) slot).getHeelOptions().getHeelLimit();
			}
		}
		
		// The last sequence element that used some kind of boil-off
		VoyageDetails lastBoiloffElement = null;

		// add up route cost and find the last boiloff element
		for (int i = 0; i < sequence.length; ++i) {
			if ((i % 2) == 1) {
				// Voyage
				final VoyageDetails details = (VoyageDetails) sequence[i];
				// add route cost
				routeCostAccumulator += details.getRouteCost();
				for (final FuelComponent fc: FuelComponent.getLNGFuelComponents()) {
					final long fuelConsumption = details.getFuelConsumption(fc, fc.getDefaultFuelUnit());
					// If this is some sort of boil-off, then record the use
					if (fuelConsumption > 0) {
						lastBoiloffElement = details;
					}
				}				
			}
		}

		int capacityViolations = 0;

		// If load or discharge has been set, then the other must be too.
		assert ((loadIdx == -1) == (dischargeIdx == -1));

		// long loadVolumeInM3 = 0;
		// long dischargeVolumeInM3 = 0;
		//
		// final int loadUnitPrice = 0;
		int dischargeUnitPrice = 0;

		// final int loadM3Price = 0;
		int dischargeM3Price = 0;

		int cargoCVValue = 0;

		// the LNG which will be required to complete the sequence, including 
		// NBO, FBO and any minimum heel if travelling on NBO		
		long lngCommitmentInM3;
		
		// Load/Discharge sequence
		if ((loadIdx != -1) && (dischargeIdx != -1)) {
			final PortDetails loadDetails = (PortDetails) sequence[loadIdx];
			final ILoadSlot loadSlot = (ILoadSlot) loadDetails.getOptions().getPortSlot();
			final PortDetails dischargeDetails = (PortDetails) sequence[dischargeIdx];
			final IDischargeSlot dischargeSlot = (IDischargeSlot) dischargeDetails.getOptions().getPortSlot();

			// Store unit prices for later on
			dischargeUnitPrice = dischargeSlot.getDischargePriceCalculator().calculateSalesUnitPrice(dischargeSlot, arrivalTimes[dischargeIdx / 2]);
			// Store cargoCVValue
			cargoCVValue = loadSlot.getCargoCVValue();

			dischargeM3Price = Calculator.costPerM3FromMMBTu(dischargeUnitPrice, cargoCVValue);

			// the LNG which would by default be consumed for the given fuel usage choices during travel & idle
			lngCommitmentInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];

			// Store this value now as we may change it below during the heel calculations
			voyagePlan.setLNGFuelVolume(lngCommitmentInM3);

			// Any violations after this point are slot constraint violations

			long minDischargeVolumeInM3 = dischargeSlot.getMinDischargeVolume();

			boolean boiloffWasUsed = (lastBoiloffElement != null);
			
			// Min Heel adjustments. The VoyageDetails tells us how much gas we consumed on the voyage. This may include some of the vessel min heel during the idle time.
			// If a minimum heel is specified, this is the amount which has to remain in the tanks after 
			// travel on NBO. 
			if (boiloffWasUsed && vesselClass.getMinHeel() > 0) {
				//
				final long minHeelInM3 = vesselClass.getMinHeel();
				// There are two states;
				// * Full Travel NBO + Full Idle NBO
				// * Full Travel NBO + Idle NBO boil-off then Base Fuel
				// plus different rules if laden or ballast.
				// If this is a laden leg, then we have chosen not to boil-off on the ballast leg. In this case we can discharge our min heel.
				// If this is a ballast leg, then the min heel continues until we get to our final destination - then whatever is left is lost.

				// First of all, determine how much heel will be left over after travel and idle
				long remainingHeelInM3;

				final long idleNBOInM3 = lastBoiloffElement.getFuelConsumption(FuelComponent.IdleNBO, FuelUnit.M3);
				if (idleNBOInM3 == 0) {
					// As we have detected some NBO use, but it is not idle, then it must be travel NBO, therefore full heel is available.
					// current lngConsumedInM3 value will not include min heel
					assert lastBoiloffElement.getFuelConsumption(FuelComponent.NBO, FuelUnit.M3) > 0;
					remainingHeelInM3 = minHeelInM3;
				} else if (idleNBOInM3 < minHeelInM3) {
					// Partial use during voyage idle time - current lngConsumedInM3 value will include some of min heel
					remainingHeelInM3 = minHeelInM3 - idleNBOInM3;
				} else {
					// Assume heel fully consumed - current lngConsumedInM3 value will include min heel
					remainingHeelInM3 = 0;
				}
				// If we will have some LNG left after travel, allocate it depending on laden or ballast legs
				if (remainingHeelInM3 > 0) {
					if (lastBoiloffElement.getOptions().getVesselState() == VesselState.Laden) {
						// Discharge the heel, make money!
						minDischargeVolumeInM3 += remainingHeelInM3;
						voyagePlan.setRemainingHeelInM3(remainingHeelInM3, VoyagePlan.HeelType.DISCHARGE);
					} else {
						// Add heel to the voyage consumed quantity for capacity constraint purposes. However it is not tracked otherwise
						// TODO: Roll over into new voyage plan
						lngCommitmentInM3 += remainingHeelInM3;
						voyagePlan.setRemainingHeelInM3(remainingHeelInM3, VoyagePlan.HeelType.END);
					}
				}

			}

			final long cargoCapacityInM3 = vesselClass.getCargoCapacity();
			
			if (lngCommitmentInM3 > cargoCapacityInM3) {
				// This is a real issue - hit physical constraints - reject
				// dischargeDetails.setCapacityViolation(CapacityViolationType.VESSEL_CAPACITY, lngConsumed - cargoCapacity);
				// Should we do this? - Could continue calculations and return a large number
				return -1;
			}

			final long minLoadVolumeInM3 = loadSlot.getMinLoadVolume();
			final long maxDischargeVolumeInM3 = dischargeSlot.getMaxDischargeVolume();

			// We cannot load more than is available or which would exceed
			// vessel capacity.
			final long upperLoadLimitInM3 = Math.min(cargoCapacityInM3, loadSlot.getMaxLoadVolume());

			if (minLoadVolumeInM3 - lngCommitmentInM3 > maxDischargeVolumeInM3) {
				if (minLoadVolumeInM3 - lngCommitmentInM3 < 0) {
					// discharge breach
					dischargeDetails.setCapacityViolation(CapacityViolationType.MAX_DISCHARGE, (minLoadVolumeInM3 - lngCommitmentInM3) - maxDischargeVolumeInM3);
					++capacityViolations;
				} else {
					// load breach
					loadDetails.setCapacityViolation(CapacityViolationType.MIN_LOAD, minLoadVolumeInM3 - (maxDischargeVolumeInM3 + lngCommitmentInM3));
					++capacityViolations;
				}
			}

			if (minDischargeVolumeInM3 + lngCommitmentInM3 > upperLoadLimitInM3) {

				if (upperLoadLimitInM3 - lngCommitmentInM3 < 0) {
					// load breach
					loadDetails.setCapacityViolation(CapacityViolationType.MAX_LOAD, lngCommitmentInM3 - upperLoadLimitInM3);
					++capacityViolations;
				} else {
					// discharge breach
					dischargeDetails.setCapacityViolation(CapacityViolationType.MIN_DISCHARGE, upperLoadLimitInM3 - lngCommitmentInM3);
					++capacityViolations;
				}
			}

			// Sanity checks
			assert lngCommitmentInM3 >= 0;
			assert lngCommitmentInM3 <= cargoCapacityInM3;
		} 
		else { 		// was not a load / discharge sequence 
			lngCommitmentInM3 = fuelConsumptions[FuelComponent.NBO.ordinal()] + fuelConsumptions[FuelComponent.FBO.ordinal()] + fuelConsumptions[FuelComponent.IdleNBO.ordinal()];
			if (lngCommitmentInM3 > availableHeelinM3) {
				final PortDetails portDetails = (PortDetails) sequence[2];
				portDetails.setCapacityViolation(CapacityViolationType.MAX_HEEL, lngCommitmentInM3 - availableHeelinM3);
				++capacityViolations;
			}
		}

		List<Integer> loadIndices = findLoadIndices(loadIndicesStorage, sequence);
		List<Integer> dischargeIndices = findDischargeIndices(dischargeIndicesStorage, sequence);
		
		// Process details, filling in LNG prices
		// TODO: I don't really like altering the details at this stage of
		// processing, but this is where the information is being processed.
		// Can this be moved into the scheduler? If so, we need to ensure the
		// same price is used in all valid voyage legs.
		int [] prices = getLngEffectivePrices(loadIndices, dischargeIndices, arrivalTimes, sequence); 
		
		// set the LNG values for the voyages
		if (prices != null) {
			int numVoyages = sequence.length / 2;
			for (int i = 0; i < numVoyages; i++) {
				int index = i * 2 + 1;
				final VoyageDetails details = (VoyageDetails) sequence[index];
				for (FuelComponent fc: FuelComponent.getLNGFuelComponents()) {
					details.setFuelUnitPrice(fc, prices[index]);
				}
			}
		}		
		
		// check for cooldown violations
		for (int i = 0; i < sequence.length; ++i) {
			if ((i & 1) == 1) {
				assert sequence[i] instanceof VoyageDetails;

				final VoyageDetails details = (VoyageDetails) sequence[i];

				if (details.getOptions().shouldBeCold() && (details.getFuelConsumption(FuelComponent.Cooldown, FuelUnit.M3) > 0)) {
					final IPort port = details.getOptions().getToPortSlot().getPort();

					if ((loadIdx != -1) && (dischargeIdx != -1) && port.shouldVesselsArriveCold()) {
						// Cooldown violation! -- Assume index 4 is next load
						// TODO: THIS IS NOT A VALID ASSUMPTION!

						final PortDetails portDetails = (PortDetails) sequence[4];
						portDetails.setCapacityViolation(CapacityViolationType.FORCED_COOLDOWN, 1);
						++capacityViolations;
					}

				}
			} else {
				assert sequence[i] instanceof PortDetails;
			}
		}

		// Store results in plan
		voyagePlan.setSequence(sequence);

		// We can just loop over each component here for consumptions ....
		for (final FuelComponent fc : FuelComponent.values()) {
			final long consumption = fuelConsumptions[fc.ordinal()];
			voyagePlan.setFuelConsumption(fc, consumption);
		}
		// .. but costs need to be calculated differently

		for (final FuelComponent fc : FuelComponent.getBaseFuelComponents())
			voyagePlan.setTotalFuelCost(fc, Calculator.costFromConsumption(fuelConsumptions[fc.ordinal()], baseFuelPricePerMT));

		for (final FuelComponent fc : FuelComponent.getLNGFuelComponents())
			voyagePlan.setTotalFuelCost(fc, Calculator.costFromConsumption(fuelConsumptions[fc.ordinal()], dischargeM3Price));

		int cooldownM3Price = calculateCooldownPrices(vesselClass, arrivalTimes, sequence);
		voyagePlan.setTotalFuelCost(FuelComponent.Cooldown, Calculator.costFromConsumption(fuelConsumptions[FuelComponent.Cooldown.ordinal()], cooldownM3Price));

		voyagePlan.setTotalRouteCost(routeCostAccumulator);

		voyagePlan.setCapacityViolations(capacityViolations);

		return capacityViolations;
	}

	/**
	 * @since 2.0
	 * 
	 */
	@Override
	public final List<Object> generateFuelCostCalculatedSequence(final Object... baseSequence) {
		final List<Object> result = new ArrayList<Object>(baseSequence.length);
		for (final Object element : baseSequence) {

			// Loop through basic elements, calculating voyage requirements
			// to build up basic voyage plan details.
			if (element instanceof VoyageOptions) {
				final VoyageOptions options = (VoyageOptions) element;

				final VoyageDetails voyageDetails = new VoyageDetails();
				voyageDetails.setOptions(options);
				// Calculate voyage cost
				calculateVoyageFuelRequirements(options, voyageDetails);
				result.add(voyageDetails);
			} else if (element instanceof PortOptions) {
				final PortOptions options = ((PortOptions) element).clone();
				final PortDetails details = new PortDetails();
				details.setOptions(options);
				calculatePortFuelRequirements(options, details);
				result.add(details);
			} else {
				System.err.println("Warning: non-Option type in option sequence: " + element);
				result.add(element);
			}
		}

		return result;
	}

	/**
	 * @author Simon McGregor
	 * 
	 *         Populate a PortDetails object with correct fuel costs based on a PortOptions object.
	 * 
	 * @param options
	 *            The PortOptions to use, specifying vessel class, vessel state and port visit duration.
	 * @param details
	 *            The PortDetails to set the correct fuel consumption for.
	 * @since 2.0
	 */
	public final void calculatePortFuelRequirements(final PortOptions options, final PortDetails details) {
		details.setOptions(options);
		//
		// TODO fix up port details, call this method
		// Add in port costs?
		//
		final IVessel vessel = options.getVessel();
		final IVesselClass vesselClass = vessel.getVesselClass();
		// final VesselState vesselState = options.getVesselState();

		/**
		 * The number of MT of base fuel or MT-equivalent of LNG required per hour during this port visit
		 */
		final long consumptionRateInMTPerHour;

		final PortType portType = options.getPortSlot().getPortType();

		// temporary kludge: ignore non-load non-discharge ports for port consumption
		if (portType == PortType.Load || portType == PortType.Discharge)
			consumptionRateInMTPerHour = vesselClass.getInPortConsumptionRate(portType);
		else
			consumptionRateInMTPerHour = 0;

		final int visitDuration = options.getVisitDuration();

		/**
		 * The total number of MT of base fuel OR MT-equivalent of LNG required for this journey, excluding any extra required for canals
		 */
		final long requiredConsumptionInMT = Calculator.quantityFromRateTime(consumptionRateInMTPerHour, visitDuration);

		details.setFuelConsumption(FuelComponent.Base, requiredConsumptionInMT);
	}

	public void setRouteCostDataComponentProvider(final IRouteCostProvider provider) {
		this.routeCostProvider = provider;
	}

	/**
	 * @since 2.0
	 */
	public void setPortCVProvider(final IPortCVProvider portCVProvider) {
		this.portCVProvider = portCVProvider;
	}

}
