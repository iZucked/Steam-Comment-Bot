/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse.jobs.impl;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlFactory;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;

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
