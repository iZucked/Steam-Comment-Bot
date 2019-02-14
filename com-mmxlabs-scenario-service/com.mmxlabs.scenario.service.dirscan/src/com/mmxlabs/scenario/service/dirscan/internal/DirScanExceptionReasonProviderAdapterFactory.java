/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.dirscan.DirScanException;
import com.mmxlabs.scenario.service.model.manager.MigrationForbiddenException;

public class DirScanExceptionReasonProviderAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {
		if (adaptableObject instanceof DirScanException || adaptableObject instanceof MigrationForbiddenException) {

			final Exception exception = (Exception) adaptableObject;
			return adapterType.cast(new IReasonProvider() {

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
					if (exception instanceof DirScanException) {
						return "There was an error loading the scenario from \"" + ((DirScanException) exception).getServiceName()
								+ "\". This is often because a scenario needs to be migrated to the latest data model version.";
					} else {
						return "There was an error loading the scenario . This is often because a scenario needs to be migrated to the latest data model version.";
					}
				}
			});
		}

		return adapterType.cast(null);
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
