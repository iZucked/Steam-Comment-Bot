/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.contingencytime;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IExtraIdleTimeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.shared.port.IPortProvider;

/**
 */
public class ContingencyIdleTimeTransformer implements ISlotTransformer {

	@Inject
	private IPortProvider portProvider;
	
	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IExtraIdleTimeProviderEditor extraIdleTimeProviderEditor;

	private LNGScenarioModel rootObject;

	/**
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CONTINGENCY_IDLE_TIME)) {
			return;
		}

		final PortModel portModel = ScenarioModelUtil.getPortModel(rootObject);
		final ContingencyMatrix matrix = portModel.getContingencyMatrix();
		if (matrix != null) {
			for (final ContingencyMatrixEntry entry : matrix.getEntries()) {
				final int extraIdleTime = 24 * entry.getDuration();
				final IPort fromPort = portProvider.getPortForName(entry.getFromPort().getName());
				final IPort toPort = portProvider.getPortForName(entry.getToPort().getName());
				if (fromPort != null && toPort != null) {
					extraIdleTimeProviderEditor.setExtraIdleTimeOnVoyage(fromPort, toPort, extraIdleTime);
				}
			}
		}

		rootObject = null;

	}

	@Override
	public void slotTransformed(@NonNull final Slot<?> modelSlot, @NonNull final IPortSlot optimiserSlot) {
		//Add in extra idle time prior to visit.
		int bufferTime = modelSlot.getSlotOrDelegateDaysBuffer();
		if (bufferTime > 0) {
			ISequenceElement element = this.portSlotProvider.getElement(optimiserSlot);
			int extraIdleTimeInHours = 24 * bufferTime;
			this.extraIdleTimeProviderEditor.setExtraIdleTimeBeforeVisit(element, optimiserSlot, extraIdleTimeInHours);
		}
	}
}
