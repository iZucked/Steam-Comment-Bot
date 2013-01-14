/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.modelCorrector;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * The {@link LNGModelCorrectorScenarioServiceListener} is an {@link IScenarioServiceListener} implementation intended to be registered as a OSGi service. It adds itself a a listener to
 * {@link IScenarioService}s and hooks in to the post load event. It will then modify any scenarios being loaded using the {@link LNGModelCorrector} should they be in an inconsistent state. For
 * example new object references added to the metamodel may not be correctly populated in any existing models.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGModelCorrectorScenarioServiceListener extends ScenarioServiceListener {

	private final Set<IScenarioService> scenarioServices = new HashSet<IScenarioService>();

	private final LNGModelCorrector corrector = new LNGModelCorrector();

	public LNGModelCorrectorScenarioServiceListener() {
	}

	public void hookExisting(final IScenarioService scenarioService) {
		hookExisting(scenarioService.getServiceModel());
	}

	private void hookExisting(final Container container) {
		for (final Container c : container.getElements()) {
			if (c instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) c;
				final EObject obj = scenarioInstance.getInstance();
				if (obj instanceof MMXRootObject) {
					final MMXRootObject rootObject = (MMXRootObject) obj;

					final EditingDomain ed = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
					if (ed != null) {
						// TODO: Run this async to avoid blocking?
						final ScenarioLock lock = scenarioInstance.getLock(ScenarioLock.EDITORS);
						lock.awaitClaim();
						try {
							corrector.correctModel(rootObject, ed);
						} finally {
							lock.release();
						}
					}
				}
			}
			hookExisting(c);
		}
	}

	@Override
	public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
		final EObject obj = scenarioInstance.getInstance();
		if (obj instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) obj;

			final EditingDomain ed = (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
			if (ed != null) {
				final ScenarioLock lock = scenarioInstance.getLock(ScenarioLock.EDITORS);
				lock.awaitClaim();
				try {
					corrector.correctModel(rootObject, ed);
				} finally {
					lock.release();
				}
			}
		}
	}

	public void dispose() {
		for (final IScenarioService s : scenarioServices) {
			s.removeScenarioServiceListener(this);
		}
		scenarioServices.clear();
	}

	public void bind(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		hookExisting(scenarioService);
		scenarioService.addScenarioServiceListener(this);
	}

	public void unbind(final IScenarioService scenarioService) {
		scenarioServices.add(scenarioService);
		scenarioService.removeScenarioServiceListener(this);
	}

}
