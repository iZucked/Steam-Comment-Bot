/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.actuals;

import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.LNGScenarioTransformer;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProviderEditor;

/**
 * A {@link ContractTransformer} implementation which creates and binds a new Spot Charter In vessel to every load slot. For {@link LoadSlot}s bound to a {@link Cargo}, the existing
 * {@link ElementAssignment} will be queried and the assigned {@link VesselClass} will be used to generate a vessel. For {@link LoadSlot}s with no {@link Cargo} (and thus no existing element
 * assignment) we pick the first {@link VesselClass} in the {@link FleetModel} list. Currently no hire cost rate is used.
 * 
 * @author Simon Goodall
 * 
 */
public class ActualsTransformer implements ITransformerExtension {

	private ModelEntityMap modelEntityMap;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	private IActualsDataProviderEditor actualsDataProviderEditor;

	private LNGScenarioModel lngScenarioModel;

	@Override
	public void startTransforming(final LNGScenarioModel lngScenarioModel, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.lngScenarioModel = lngScenarioModel;
		this.modelEntityMap = modelEntityMap;
	}

	@Override
	public void finishTransforming() {
		final ActualsModel actualsModel = findActualsModel(lngScenarioModel);
		if (actualsModel != null) {
			for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
				final int baseFuelPricePerMT = OptimiserUnitConvertor.convertToInternalPrice(cargoActuals.getBaseFuelPrice());
				final int vesselCharterCostPerDay = (int) OptimiserUnitConvertor.convertToInternalDailyCost(cargoActuals.getCharterRatePerDay());
				final long insuranceCosts = (int) OptimiserUnitConvertor.convertToInternalDailyCost(cargoActuals.getInsurancePremium());

				// FIXME: It is possible that SlotActuals order is not chronological
				IPortSlot lastSlot = null;
				for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
					final Slot slot = slotActuals.getSlot();
					assert slot != null;

					final int cargoCV = OptimiserUnitConvertor.convertToInternalConversionFactor(slotActuals.getCV());
					final int lngPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(slotActuals.getPriceDOL());
					// Rounding problems here?
					final long lngVolumeInM3 = OptimiserUnitConvertor.convertToInternalVolume(Math.round(slotActuals.getVolumeInM3()));
					final long lngVolumeInMMBTu = OptimiserUnitConvertor.convertToInternalVolume(slotActuals.getVolumeInMMBtu());

					final long baseFuelConsumptionInMT = OptimiserUnitConvertor.convertToInternalVolume(slotActuals.getBaseFuelConsumption());
					final long portBaseFuelConsumptionInMT = OptimiserUnitConvertor.convertToInternalVolume(slotActuals.getPortBaseFuelConsumption());
					final long portCosts = OptimiserUnitConvertor.convertToInternalFixedCost(slotActuals.getPortCharges());

					final long capacityCosts = OptimiserUnitConvertor.convertToInternalFixedCost(slotActuals.getCapacityCharges());
					final long crewBonusCosts = OptimiserUnitConvertor.convertToInternalFixedCost(slotActuals.getCrewBonus());

					final int arrivalTime = dateHelper.convertTime(slotActuals.getOperationsStartAsDateTime());
					final int departTime = dateHelper.convertTime(slotActuals.getOperationsEndAsDateTime());
					final int visitDuration = departTime - arrivalTime;

					final int distance = slotActuals.getDistance();
					final long routeCosts = OptimiserUnitConvertor.convertToInternalFixedCost(slotActuals.getRouteCosts());
					final Route routeObject = slotActuals.getRoute();
					final ERouteOption route = routeObject == null ? ERouteOption.DIRECT : LNGScenarioTransformer.mapRouteOption(routeObject);

					final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
					if (slot instanceof LoadSlot && slotActuals instanceof LoadActuals) {
						final LoadActuals loadActuals = (LoadActuals) slotActuals;
						final long startHeelInM3 = OptimiserUnitConvertor.convertToInternalVolume(loadActuals.getStartingHeelM3());
						actualsDataProviderEditor.createLoadSlotActuals((ILoadOption) portSlot, arrivalTime, visitDuration, portCosts, cargoCV, startHeelInM3, lngVolumeInM3, lngVolumeInMMBTu,
								lngPricePerMMBTu, portBaseFuelConsumptionInMT, baseFuelConsumptionInMT, baseFuelPricePerMT, vesselCharterCostPerDay, distance, routeCosts, route);

						actualsDataProviderEditor.createLoadSlotExtraActuals((ILoadOption) portSlot, capacityCosts, crewBonusCosts, insuranceCosts);

						// Slight hack - getting internal implementation, but currently best way to ensure actual CV is used
						final ILoadOption loadOption = modelEntityMap.getOptimiserObject(slot, ILoadOption.class);
						if (loadOption instanceof LoadOption) {
							final LoadOption loadOption2 = (LoadOption) loadOption;
							loadOption2.setCargoCVValue(cargoCV);
						}

					} else if (slot instanceof DischargeSlot && slotActuals instanceof DischargeActuals) {
						final DischargeActuals dischargeActuals = (DischargeActuals) slotActuals;
						final long endHeelInM3 = OptimiserUnitConvertor.convertToInternalVolume(dischargeActuals.getEndHeelM3());
						actualsDataProviderEditor.createDischargeSlotActuals((IDischargeOption) portSlot, arrivalTime, visitDuration, portCosts, cargoCV, endHeelInM3, lngVolumeInM3, lngVolumeInMMBTu,
								lngPricePerMMBTu, portBaseFuelConsumptionInMT, baseFuelConsumptionInMT, distance, routeCosts, route);

						actualsDataProviderEditor.createDischargeSlotExtraActuals((IDischargeOption) portSlot, capacityCosts, crewBonusCosts);

						lastSlot = portSlot;
					} else {
						// / error
					}
				}

				{
					final ReturnActuals returnActuals = cargoActuals.getReturnActuals();
					if (returnActuals != null) {
						if (returnActuals.getOperationsStartAsDateTime() != null && returnActuals.getTitleTransferPoint() != null) {
							final int arrivalTime = dateHelper.convertTime(returnActuals.getOperationsStartAsDateTime());
							final IPort returnPort = modelEntityMap.getOptimiserObject(returnActuals.getTitleTransferPoint(), IPort.class);
							final long heelInM3 = OptimiserUnitConvertor.convertToInternalVolume(returnActuals.getEndHeelM3());

							actualsDataProviderEditor.setNextDestinationActuals(lastSlot, returnPort, arrivalTime, heelInM3);
						}
					}
				}
			}
		}

		this.modelEntityMap = null;
		this.lngScenarioModel = null;

	}

	@Nullable
	private ActualsModel findActualsModel(final LNGScenarioModel lngScenarioModel) {
		return lngScenarioModel.getActualsModel();
	}
}
