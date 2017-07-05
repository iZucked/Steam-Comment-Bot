/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.adapterfactories;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.models.lng.transformer.longterm.LNGLongTermJobDescriptor;
import com.mmxlabs.models.lng.transformer.longterm.LNGSchedulerLongTermJobControl;
import com.mmxlabs.models.lng.transformer.longterm.LightWeightSchedulerJobControl;
import com.mmxlabs.models.lng.transformer.longterm.LightWeightSchedulerJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGRunAllSimilarityJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGRunMultipleSeedsJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerEvaluationJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerManyJobsControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerOptimiserJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerRunMultipleSeedsJobControl;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobControl;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSlotInsertionJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.actionablesets.CreateActionableSetPlanJobControl;
import com.mmxlabs.models.lng.transformer.ui.actionablesets.CreateActionableSetPlanJobDescriptor;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

/**
 * {@link IAdapterFactory} to convert a {@link LNGSchedulerJobDescriptor} into an {@link IJobControl} - specifically a {@link LNGSchedulerJobUtils}.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGJobControlAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class<T> adapterType) {

		if (adaptableObject instanceof LNGRunAllSimilarityJobDescriptor) {

			final LNGRunAllSimilarityJobDescriptor descriptor = (LNGRunAllSimilarityJobDescriptor) adaptableObject;
			if (descriptor.isOptimising()) {
				try {
					// return new LNGSchedulerRunAllSimilarityJobControl(descriptor);
					return (T) new LNGSchedulerManyJobsControl(descriptor);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		} else if (adaptableObject instanceof LNGRunMultipleSeedsJobDescriptor) {

			final LNGRunMultipleSeedsJobDescriptor descriptor = (LNGRunMultipleSeedsJobDescriptor) adaptableObject;
			if (descriptor.isOptimising()) {
				try {
					// return new LNGSchedulerRunAllSimilarityJobControl(descriptor);
					return (T) new LNGSchedulerRunMultipleSeedsJobControl(descriptor);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		} else if (adaptableObject instanceof LNGSchedulerJobDescriptor) {

			final LNGSchedulerJobDescriptor descriptor = (LNGSchedulerJobDescriptor) adaptableObject;
			if (descriptor.isOptimising()) {
				return (T) new LNGSchedulerOptimiserJobControl(descriptor);
			} else {
				return (T) new LNGSchedulerEvaluationJobControl(descriptor);
			}
		} else if (adaptableObject instanceof LNGSlotInsertionJobDescriptor) {
			final LNGSlotInsertionJobDescriptor descriptor = (LNGSlotInsertionJobDescriptor) adaptableObject;
			return (T) new LNGSchedulerInsertSlotJobControl(descriptor);
		} else if (adaptableObject instanceof CreateActionableSetPlanJobDescriptor) {
			final CreateActionableSetPlanJobDescriptor descriptor = (CreateActionableSetPlanJobDescriptor) adaptableObject;
			return (T) new CreateActionableSetPlanJobControl(descriptor);
		} else if (adaptableObject instanceof LNGLongTermJobDescriptor) {
			final LNGLongTermJobDescriptor descriptor = (LNGLongTermJobDescriptor) adaptableObject;
			return (T) new LNGSchedulerLongTermJobControl(descriptor);
		} else if (adaptableObject instanceof LightWeightSchedulerJobDescriptor) {
			final LightWeightSchedulerJobDescriptor descriptor = (LightWeightSchedulerJobDescriptor) adaptableObject;
			return (T) new LightWeightSchedulerJobControl(descriptor);
		}
		return (T) null;

	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IJobControl.class };
	}
}
