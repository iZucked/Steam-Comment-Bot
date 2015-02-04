/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario.internal;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.models.migration.scenario.FinalClientMigratedVersionMismatchException;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.rcp.common.editors.IReasonProvider;

public class ScenarioMigrationExceptionReasonProviderAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(final Object adaptableObject, final Class adapterType) {

		if (adaptableObject instanceof FinalClientMigratedVersionMismatchException) {
			final FinalClientMigratedVersionMismatchException exception = (FinalClientMigratedVersionMismatchException) adaptableObject;
			return new IReasonProvider() {

				@Override
				public String getTitle() {
					return String.format("Unable to migrate client custom data model between version %d and %d", exception.getActualVersion(), exception.getExpectedVersion());
				}

				@Override
				public Throwable getThrowable() {
					return exception;
				}

				@Override
				public String getResolutionSteps() {
					return "Please contact Minimax Labs with this scenario and error message.";
				}

				@Override
				public String getDescription() {
					return "There was an internal error migrating the client specific data model.";
				}
			};
		} else if (adaptableObject instanceof ScenarioMigrationException) {

			final ScenarioMigrationException exception = (ScenarioMigrationException) adaptableObject;
			return new IReasonProvider() {

				@Override
				public String getTitle() {
					return "Unable to migrate scenario data model.";
				}

				@Override
				public Throwable getThrowable() {
					return exception;
				}

				@Override
				public String getResolutionSteps() {
					return "Please contact Minimax Labs with this scenario and error message.";
				}

				@Override
				public String getDescription() {
					return "There was an internal error migrating the scenario to the latest data model.";
				}
			};
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
