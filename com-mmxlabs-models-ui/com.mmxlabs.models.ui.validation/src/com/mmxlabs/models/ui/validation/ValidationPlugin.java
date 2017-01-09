/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A base class for validation plugins, which provides the method {@link #getExtraValidationContext()}
 * 
 * @author hinton
 * 
 */
public abstract class ValidationPlugin extends AbstractUIPlugin {
	private ServiceTracker<IValidationService, IValidationService> validationDataTracker;

	public IExtraValidationContext getExtraValidationContext() {
		final IValidationService service = validationDataTracker.getService();
		if (service == null)
			return null;
		return service.getExtraContext();
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		validationDataTracker = new ServiceTracker<IValidationService, IValidationService>(context, IValidationService.class, null);
		validationDataTracker.open();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		validationDataTracker.close();
		validationDataTracker = null;
		super.stop(context);
	}
}
