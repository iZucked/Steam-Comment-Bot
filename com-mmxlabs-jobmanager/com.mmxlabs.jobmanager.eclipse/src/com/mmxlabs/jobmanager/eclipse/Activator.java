/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.eclipse;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.mmxlabs.jobmanager.eclipse.jobs.impl.AdapterFactoryJobControlFactory;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.jobmanager.manager.impl.JobManager;
import com.mmxlabs.jobmanager.manager.impl.JobManagerDescriptor;

public class Activator implements BundleActivator {

	private JobManager localJobManager = null;
	private ServiceRegistration<IJobManager> localJobManagerService;

	@Override
	public void start(final BundleContext context) throws Exception {

		localJobManager = new JobManager();
		localJobManager.setJobControlFactory(new AdapterFactoryJobControlFactory());

		final JobManagerDescriptor jobManagerDescriptor = new JobManagerDescriptor("Local");
		jobManagerDescriptor.setDescription("Internal job manager");

		localJobManager.setJobManagerDescriptor(jobManagerDescriptor);

		// localJobManager.restoreState();

		localJobManagerService = context.registerService(IJobManager.class, localJobManager, null);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

		localJobManagerService.unregister();

		// TODO: What about existing jobs?
		// localJobManager.saveState();

		localJobManager.dispose();
	}
}
