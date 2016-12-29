/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

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
	 * Find the containing {@link LNGScenarioModel} for this Schedule.
	 * 
	 * @param schedule
	 * @return
	 */
	@Nullable
	public static LNGScenarioModel findScenarioModel(@NonNull final Schedule schedule) {
		EObject container = schedule.eContainer();
		while (container != null && !(container instanceof LNGScenarioModel)) {
			container = container.eContainer();
		}
		return (LNGScenarioModel) container;
	}

	@Nullable
	public static LNGScenarioModel findScenarioModel(@NonNull final CargoModel cargoModel) {
		final EObject container = cargoModel.eContainer();
		if (container instanceof LNGScenarioModel) {
			return (LNGScenarioModel) container;
		}
		return null;
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
	public static LNGReferenceModel findReferenceModel(@NonNull final LNGScenarioModel scenarioModel) {
		return scenarioModel.getReferenceModel();
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
	public static AnalyticsModel getAnalyticsModel(@NonNull final LNGScenarioModel lngScenarioModel) {
	 
		final AnalyticsModel analyticsModel = lngScenarioModel.getAnalyticsModel();
		if (analyticsModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return analyticsModel;
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
		return lngScenarioModel.getScheduleModel();
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
}
