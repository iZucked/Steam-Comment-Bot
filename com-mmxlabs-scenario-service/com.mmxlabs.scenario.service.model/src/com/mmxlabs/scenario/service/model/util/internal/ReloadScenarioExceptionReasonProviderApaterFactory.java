/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.internal;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;

import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.ReloadScenarioException;

public class ReloadScenarioExceptionReasonProviderApaterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof ReloadScenarioException) {
			ReloadScenarioException reloadScenarioException = (ReloadScenarioException) adaptableObject;
			return adapterType.cast(Platform.getAdapterManager().loadAdapter(reloadScenarioException.getCause(), IReasonProvider.class.getCanonicalName()));
		}
		return adapterType.cast(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
