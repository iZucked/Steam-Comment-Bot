package com.mmxlabs.jobcontroller.jobs.eclipse.impl;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import com.mmxlabs.jobcontroller.jobs.IJobControl;
import com.mmxlabs.jobcontroller.jobs.IJobControlFactory;
import com.mmxlabs.jobcontroller.jobs.IJobDescriptor;

/**
 * Implementation of {@link IJobControlFactory} which uses the Eclipse {@link IActivityManager} to obtain a {@link IJobControl}.
 * 
 * @author Simon Goodall
 * 
 */
public final class AdapterFactoryJobControlFactory implements IJobControlFactory {

	private final IAdapterManager adapterManager;

	/**
	 * Default constructor which uses the {@link IAdapterManager} instance returned by {@link Platform#getAdapterManager()}.
	 */
	public AdapterFactoryJobControlFactory() {
		this(Platform.getAdapterManager());
	}

	public AdapterFactoryJobControlFactory(final IAdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}

	@Override
	public IJobControl createJobControl(final IJobDescriptor job) {

		return (IJobControl) adapterManager.getAdapter(job, IJobControl.class);
	}	
}
