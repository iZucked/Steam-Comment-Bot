/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.adapterfactories;

import java.io.IOException;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGRunAllSimilarityJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGRunMultipleSeedsJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerEvaluationJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerManyJobsControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerOptimiserJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerRunMultipleSeedsJobControl;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSchedulerInsertSlotJobControl;
import com.mmxlabs.models.lng.transformer.ui.analytics.LNGSlotInsertionJobDescriptor;
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
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		} else if (adaptableObject instanceof LNGRunMultipleSeedsJobDescriptor) {

			final LNGRunMultipleSeedsJobDescriptor descriptor = (LNGRunMultipleSeedsJobDescriptor) adaptableObject;
			if (descriptor.isOptimising()) {
				try {
					// return new LNGSchedulerRunAllSimilarityJobControl(descriptor);
					return (T) new LNGSchedulerRunMultipleSeedsJobControl(descriptor);
				} catch (final IOException e) {
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
		}
		return (T) null;

	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IJobControl.class };
	}
}
