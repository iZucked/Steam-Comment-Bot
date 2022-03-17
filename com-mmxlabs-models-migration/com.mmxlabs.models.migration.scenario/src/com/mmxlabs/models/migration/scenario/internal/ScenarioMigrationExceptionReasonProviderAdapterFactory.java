/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.models.migration.scenario.FinalClientMigratedVersionMismatchException;
import com.mmxlabs.models.migration.scenario.ScenarioMigrationException;
import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.model.manager.MigrationForbiddenException;

public class ScenarioMigrationExceptionReasonProviderAdapterFactory implements IAdapterFactory {

	@SuppressWarnings({ "null", "unchecked" })
	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {

		if (adaptableObject instanceof FinalClientMigratedVersionMismatchException) {
			final FinalClientMigratedVersionMismatchException exception = (FinalClientMigratedVersionMismatchException) adaptableObject;
			return (T) new IReasonProvider() {

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
			return (T) new IReasonProvider() {

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
		} else if (adaptableObject instanceof MigrationForbiddenException) {
			final MigrationForbiddenException exception = (MigrationForbiddenException) adaptableObject;
			return (T) new IReasonProvider() {

				@Override
				public String getTitle() {
					return "Unable to migrate scenario.";
				}

				@Override
				public Throwable getThrowable() {
					return null;// No need for details...
				}

				@Override
				public String getResolutionSteps() {
					return "Copy scenario to \"My Scenarios\" and re-open.";
				}

				@Override
				public String getDescription() {
					return "Shared scenario folders such as \"Team\" cannot migrate a scenario. Copy the scenario into the \"My Scenarios\" area and open again.";
				}
			};
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
