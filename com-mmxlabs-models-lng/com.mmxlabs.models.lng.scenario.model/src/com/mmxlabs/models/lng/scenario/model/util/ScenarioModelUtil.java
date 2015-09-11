/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
}
