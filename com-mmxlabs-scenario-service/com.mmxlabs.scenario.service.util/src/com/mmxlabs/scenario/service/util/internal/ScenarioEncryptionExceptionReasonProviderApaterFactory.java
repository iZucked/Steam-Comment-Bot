/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.util.encryption.ScenarioEncryptionException;

public class ScenarioEncryptionExceptionReasonProviderApaterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof ScenarioEncryptionException) {
			final ScenarioEncryptionException exception = (ScenarioEncryptionException) adaptableObject;

			return (T) new IReasonProvider() {

				@Override
				public String getTitle() {
					return "Scenario Encryption Fault";
				}

				@Override
				public Throwable getThrowable() {
					return exception.getCause();
				}

				@Override
				public String getResolutionSteps() {
					return "Contact Minimax Labs to resolve this issue.";
				}

				@Override
				public String getDescription() {
					return "An error occurred decrypting your scenario";
				}
			};
		}
		return (T) null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
