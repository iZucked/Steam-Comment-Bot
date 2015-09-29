/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.adapterfactories;

import java.io.IOException;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGRunAllSimilarityJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerEvaluationJobControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerJobDescriptor;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerManyJobsControl;
import com.mmxlabs.models.lng.transformer.ui.LNGSchedulerOptimiserJobControl;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

/**
 * {@link IAdapterFactory} to convert a {@link LNGSchedulerJobDescriptor} into an {@link IJobControl} - specifically a {@link LNGSchedulerJobUtils}.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGJobControlAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		if (adaptableObject instanceof LNGRunAllSimilarityJobDescriptor) {

			final LNGRunAllSimilarityJobDescriptor descriptor = (LNGRunAllSimilarityJobDescriptor) adaptableObject;
			if (descriptor.isOptimising()) {
				try {
//					return new LNGSchedulerRunAllSimilarityJobControl(descriptor);
					return new LNGSchedulerManyJobsControl(descriptor);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		} else if (adaptableObject instanceof LNGSchedulerJobDescriptor) {

			final LNGSchedulerJobDescriptor descriptor = (LNGSchedulerJobDescriptor) adaptableObject;
			if (descriptor.isOptimising()) {
				return new LNGSchedulerOptimiserJobControl(descriptor);
			} else {
				return new LNGSchedulerEvaluationJobControl(descriptor);
			}
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IJobControl.class };
	}
}
