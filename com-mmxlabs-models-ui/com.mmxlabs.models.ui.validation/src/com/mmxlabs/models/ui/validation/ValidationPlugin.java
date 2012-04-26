/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
	private ServiceTracker<IValidationInputService, IValidationInputService> validationDataTracker;
	public IExtraValidationContext getExtraValidationContext() {
		final IValidationInputService service = validationDataTracker.getService();
		if (service == null) return null;
		return service.getExtraContext();
	}
	
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		validationDataTracker = new ServiceTracker<IValidationInputService, IValidationInputService>(context, IValidationInputService.class, null);
		validationDataTracker.open();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		validationDataTracker.close();
		super.stop(context);
	}
}
