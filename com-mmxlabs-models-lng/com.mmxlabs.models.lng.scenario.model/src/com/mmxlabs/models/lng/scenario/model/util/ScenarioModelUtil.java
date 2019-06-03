/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * Utility class to navigate the {@link LNGScenarioModel}
 * 
 * @author Simon Goodall
 *
 */
public final class ScenarioModelUtil {

	private ScenarioModelUtil() {

	}
	
	/**
	 * Find the containing {@link LNGPortfolioModel} for this Schedule.
	 * 
	 * @param schedule
	 * @return
	 */
	@Nullable
	public static LNGReferenceModel findReferenceModel(@NonNull final Schedule schedule) {
		EObject container = schedule.eContainer();
		while (container != null && !(container instanceof LNGScenarioModel)) {
			container = container.eContainer();
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) container;
		if (scenarioModel != null) {
			return scenarioModel.getReferenceModel();
		}
		return null;
	}

	@Nullable
	public static LNGReferenceModel findReferenceModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			return ((LNGScenarioModel) scenario).getReferenceModel();
		}
		return null;
	}

	@Nullable
	public static LNGScenarioModel findScenarioModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			return ((LNGScenarioModel) scenario);
		}
		return null;
	}
	
	@Nullable
	public static LNGReferenceModel findReferenceModel(@NonNull final LNGScenarioModel scenarioModel) {
		return scenarioModel.getReferenceModel();
	}

	@Nullable
	public static Schedule findSchedule(@NonNull final IScenarioDataProvider scenarioDataProvider) {

		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenario;
			final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
			if (scheduleModel != null) {
				return scheduleModel.getSchedule();
			}
		}
		return null;
	}

	@Nullable
	public static Schedule findSchedule(@NonNull final LNGScenarioModel scenarioModel) {
		final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
		if (scheduleModel != null) {
			return scheduleModel.getSchedule();
		}
		return null;
	}

	@NonNull
	public static PricingModel getPricingModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGReferenceModel referenceModel = findReferenceModel(scenarioDataProvider);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final PricingModel pricingModel = referenceModel.getPricingModel();
		if (pricingModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return pricingModel;
	}

	@NonNull
	public static PricingModel getPricingModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final LNGReferenceModel referenceModel = findReferenceModel(lngScenarioModel);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final PricingModel pricingModel = referenceModel.getPricingModel();
		if (pricingModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return pricingModel;
	}

	/**
	 * Get the nominations model.
	 * @param scenarioDataProvider
	 * @return a copy of the nominations model.
	 * @throws IllegalArgumentException if arguments are invalid or model is missing.
	 */
	@NonNull
	public static NominationsModel getNominationsModel(final IScenarioDataProvider scenarioDataProvider) {
		final LNGScenarioModel scenarioModel = findScenarioModel(scenarioDataProvider);
		if (scenarioModel != null) {
			return getNominationsModel(scenarioModel);
		}
		else {
			throw new IllegalArgumentException("Invalid scenario data provider.");
		}
	}

	/**
	 * Get the nominations model.
	 * @param scenarioDataProvider
	 * @return a copy of the nominations model.
	 * @throws IllegalArgumentException if arguments are invalid or model is missing.
	 */
	@NonNull
	public static NominationsModel getNominationsModel(final LNGScenarioModel scenarioModel) {
		NominationsModel nominationsModel = null;
		if (scenarioModel != null) {
			nominationsModel = scenarioModel.getNominationsModel();
		}
		else {
			throw new IllegalArgumentException("Invalid scenario model.");
		}
		if (nominationsModel == null) {
			throw new IllegalArgumentException("Invalid scenario model: missing nominations model.");
		}
		return nominationsModel;
	}	

	
	@Nullable
	public static NominationsModel findNominationsModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGScenarioModel scenarioModel = findScenarioModel(scenarioDataProvider);
		return findNominationsModel(scenarioModel);
	}

	@Nullable
	public static NominationsModel findNominationsModel(final LNGScenarioModel scenarioModel) {
		NominationsModel nominationsModel = null;
		if (scenarioModel != null) {
			nominationsModel = scenarioModel.getNominationsModel();
		}
		return nominationsModel;
	}	
	
	@NonNull
	public static CostModel getCostModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGReferenceModel referenceModel = findReferenceModel(scenarioDataProvider);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final CostModel costModel = referenceModel.getCostModel();
		if (costModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return costModel;
	}

	@NonNull
	public static CostModel getCostModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final LNGReferenceModel referenceModel = findReferenceModel(lngScenarioModel);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final CostModel costModel = referenceModel.getCostModel();
		if (costModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return costModel;
	}

	@NonNull
	public static PortModel getPortModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final LNGReferenceModel referenceModel = findReferenceModel(lngScenarioModel);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final PortModel portModel = referenceModel.getPortModel();
		if (portModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return portModel;
	}

	@NonNull
	public static PortModel getPortModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGReferenceModel referenceModel = findReferenceModel(scenarioDataProvider);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final PortModel portModel = referenceModel.getPortModel();
		if (portModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return portModel;
	}

	@NonNull
	public static AnalyticsModel getAnalyticsModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
			final AnalyticsModel analyticsModel = lngScenarioModel.getAnalyticsModel();
			if (analyticsModel == null) {
				throw new IllegalArgumentException("Invalid scenario model");
			}
			return analyticsModel;
		}
		throw new IllegalArgumentException("Invalid scenario model");
	}

	@Nullable
	public static ActualsModel getActualsModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
			final ActualsModel actualsModel = lngScenarioModel.getActualsModel();

			return actualsModel;
		}
		return null;
	}

	@NonNull
	public static AnalyticsModel getAnalyticsModel(@NonNull final LNGScenarioModel lngScenarioModel) {

		final AnalyticsModel analyticsModel = lngScenarioModel.getAnalyticsModel();
		if (analyticsModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return analyticsModel;
	}

	@NonNull
	public static CargoModel getCargoModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
			final CargoModel cargoModel = lngScenarioModel.getCargoModel();
			if (cargoModel == null) {
				throw new IllegalArgumentException("Invalid scenario model");
			}
			return cargoModel;
		}
		throw new IllegalArgumentException("Invalid scenario model");
	}

	@NonNull
	public static CargoModel getCargoModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final CargoModel cargoModel = lngScenarioModel.getCargoModel();
		if (cargoModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return cargoModel;
	}

	@NonNull
	public static FleetModel getFleetModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGReferenceModel referenceModel = findReferenceModel(scenarioDataProvider);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final FleetModel fleetModel = referenceModel.getFleetModel();
		if (fleetModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return fleetModel;
	}

	@NonNull
	public static FleetModel getFleetModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final LNGReferenceModel referenceModel = findReferenceModel(lngScenarioModel);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final FleetModel fleetModel = referenceModel.getFleetModel();
		if (fleetModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return fleetModel;
	}

	@NonNull
	public static ScheduleModel getScheduleModel(@NonNull final LNGScenarioModel lngScenarioModel) {

		final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
		if (scheduleModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return scheduleModel;
	}

	@NonNull
	public static ScheduleModel getScheduleModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
			final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
			if (scheduleModel == null) {
				throw new IllegalArgumentException("Invalid scenario model");
			}
			return scheduleModel;
		}
		throw new IllegalArgumentException("Invalid scenario model");
	}

	@NonNull
	public static SpotMarketsModel getSpotMarketsModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGReferenceModel referenceModel = findReferenceModel(scenarioDataProvider);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final SpotMarketsModel spotMarketsModel = referenceModel.getSpotMarketsModel();
		if (spotMarketsModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return spotMarketsModel;
	}

	@NonNull
	public static SpotMarketsModel getSpotMarketsModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final LNGReferenceModel referenceModel = findReferenceModel(lngScenarioModel);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final SpotMarketsModel spotMarketsModel = referenceModel.getSpotMarketsModel();
		if (spotMarketsModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return spotMarketsModel;
	}

	@NonNull
	public static CommercialModel getCommercialModel(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		final LNGReferenceModel referenceModel = findReferenceModel(scenarioDataProvider);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final CommercialModel commercialModel = referenceModel.getCommercialModel();
		if (commercialModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return commercialModel;
	}

	@NonNull
	public static CommercialModel getCommercialModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final LNGReferenceModel referenceModel = findReferenceModel(lngScenarioModel);
		if (referenceModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		final CommercialModel commercialModel = referenceModel.getCommercialModel();
		if (commercialModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return commercialModel;
	}

	public static @Nullable LNGScenarioModel findScenarioModel(final @NonNull EObject source) {
		EObject container = source.eContainer();
		while (container != null && !(container instanceof LNGScenarioModel)) {
			container = container.eContainer();
		}
		return (LNGScenarioModel) container;
	}

	public static @NonNull ModelDistanceProvider getModelDistanceProvider(@NonNull final IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final
		ModelDistanceProvider extraDataProvider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);
		if (extraDataProvider == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return extraDataProvider;
	}

	public static @Nullable ADPModel getADPModel(@NonNull IScenarioDataProvider scenarioDataProvider) {
		@NonNull
		final EObject scenario = scenarioDataProvider.getScenario();
		if (scenario instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) scenario;
			return lngScenarioModel.getAdpModel();
		}
		throw new IllegalArgumentException("Invalid scenario model");
	}

	public static @Nullable ADPModel getADPModel(@NonNull LNGScenarioModel lngScenarioModel) {
		return lngScenarioModel.getAdpModel();
	}
}
