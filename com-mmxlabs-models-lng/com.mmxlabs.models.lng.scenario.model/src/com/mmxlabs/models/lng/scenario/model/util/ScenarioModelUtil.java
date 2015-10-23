/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

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

	/**
	 * Find the containing {@link LNGPortfolioModel} for this Schedule.
	 * 
	 * @param schedule
	 * @return
	 */
	@Nullable
	public static LNGPortfolioModel findPortfolioModel(@NonNull final Schedule schedule) {
		EObject container = schedule.eContainer();
		while (container != null && !(container instanceof LNGPortfolioModel)) {
			container = container.eContainer();
		}
		return (LNGPortfolioModel) container;
	}

	@Nullable
	public static LNGPortfolioModel findPortfolioModel(@NonNull final LNGScenarioModel scenarioModel) {
		return scenarioModel.getPortfolioModel();
	}

	@Nullable
	public static Schedule findSchedule(@NonNull final LNGScenarioModel scenarioModel) {
		final LNGPortfolioModel portfolioModel = findPortfolioModel(scenarioModel);
		if (portfolioModel != null) {
			final ScheduleModel scheduleModel = portfolioModel.getScheduleModel();
			if (scheduleModel != null) {
				return scheduleModel.getSchedule();
			}
		}
		return null;
	}

	@NonNull
	public static PricingModel getPricingModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final PricingModel pricingModel = lngScenarioModel.getPricingModel();
		if (pricingModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return pricingModel;
	}

	@NonNull
	public static CostModel getCostModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final CostModel costModel = lngScenarioModel.getCostModel();
		if (costModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return costModel;
	}

	@NonNull
	public static PortModel getPortModel(@NonNull final LNGScenarioModel lngScenarioModel) {
		final PortModel portModel = lngScenarioModel.getPortModel();
		if (portModel == null) {
			throw new IllegalArgumentException("Invalid scenario model");
		}
		return portModel;
	}
}
