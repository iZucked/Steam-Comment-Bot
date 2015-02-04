/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.internal;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;

import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.ReloadScenarioException;

public class ReloadScenarioExceptionReasonProviderApaterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof ReloadScenarioException) {
			ReloadScenarioException reloadScenarioException = (ReloadScenarioException) adaptableObject;
			return Platform.getAdapterManager().loadAdapter(reloadScenarioException.getCause(), IReasonProvider.class.getCanonicalName());
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
