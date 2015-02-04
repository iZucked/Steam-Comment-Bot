/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.dirscan.DirScanException;

public class DirScanExceptionReasonProviderAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, @SuppressWarnings("rawtypes") final Class adapterType) {
		if (adaptableObject instanceof DirScanException) {

			final DirScanException exception = (DirScanException) adaptableObject;
			return new IReasonProvider() {

				@Override
				public String getTitle() {
					return "Unable to open shared scenario.";
				}

				@Override
				public Throwable getThrowable() {
					return exception;
				}

				@Override
				public String getResolutionSteps() {
					return "Please copy the scenario to \"My Scenarios\" and re-open.";
				}

				@Override
				public String getDescription() {
					return "There was an error loading the scenario from \"" + exception.getServiceName()
							+ "\". This is often because a scenario needs to be migrated to the latest data model version.";
				}
			};
		}

		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
