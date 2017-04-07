/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

public class ScheduleModelValidationHelper {

	public static boolean isMainScheduleModel(@Nullable EObject target) {
		if (target == null) {
			return false;
		}
		if (target instanceof ScheduleModel) {
			if (target.eContainer() instanceof LNGScenarioModel) {
				return true;
			} else {
				return false;
			}
		}
		return isMainScheduleModel(target.eContainer());
	}
}
