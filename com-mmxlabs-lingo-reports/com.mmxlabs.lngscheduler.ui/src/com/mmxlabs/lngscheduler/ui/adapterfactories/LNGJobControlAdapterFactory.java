/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.ui.adapterfactories;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobControl;
import com.mmxlabs.lngscheduler.ui.LNGSchedulerJobDescriptor;

/**
 * {@link IAdapterFactory} to convert a {@link LNGSchedulerJobDescriptor} into an {@link IJobControl} - specifically a {@link LNGSchedulerJobControl}.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGJobControlAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {

		if (adaptableObject instanceof LNGSchedulerJobDescriptor) {

			final LNGSchedulerJobDescriptor descriptor = (LNGSchedulerJobDescriptor) adaptableObject;
			return new LNGSchedulerJobControl(descriptor);
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IJobControl.class };
	}
}
