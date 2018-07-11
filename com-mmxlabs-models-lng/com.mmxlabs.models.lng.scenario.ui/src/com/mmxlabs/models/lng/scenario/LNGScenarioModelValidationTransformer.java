/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.validation.IValidationRootObjectTransformerService;

public class LNGScenarioModelValidationTransformer implements IValidationRootObjectTransformerService {

	@Override
	public Collection<? extends EObject> getTargets(final EObject rootObject) {

		final List<EObject> l = new LinkedList<>();

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

			l.add(lngScenarioModel.getActualsModel());
			l.add(lngScenarioModel.getCargoModel());
			l.add(lngScenarioModel.getReferenceModel());
			l.add(lngScenarioModel.getScheduleModel());

			l.add(lngScenarioModel.getAnalyticsModel());

			while (l.remove(null))
				;

		} else {
			l.add(rootObject);
		}
		return l;
	}

}
