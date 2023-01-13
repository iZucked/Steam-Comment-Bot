/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.internal;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil.EncryptedScenarioException;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;
import com.mmxlabs.scenario.service.model.util.encryption.UnknownEncryptionKeyException;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;

public class ScenarioEncryptionExceptionReasonProviderApaterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof ScenarioEncryptionException) {
			final ScenarioEncryptionException exception = (ScenarioEncryptionException) adaptableObject;

			return adapterType.cast(new IReasonProvider() {

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
			});
		}
		if (adaptableObject instanceof EncryptedScenarioException) {
			final EncryptedScenarioException exception = (EncryptedScenarioException) adaptableObject;

			return adapterType.cast(new IReasonProvider() {

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
			});
		}
		if (adaptableObject instanceof UnknownEncryptionKeyException) {
			final UnknownEncryptionKeyException exception = (UnknownEncryptionKeyException) adaptableObject;

			return adapterType.cast(new IReasonProvider() {

				@Override
				public String getTitle() {
					return "Unknown encryption key";
				}

				@Override
				public Throwable getThrowable() {
					return null;
				}

				@Override
				public String getResolutionSteps() {
					return "Contact Minimax Labs to resolve this issue.";
				}

				@Override
				public String getDescription() {
					return "The scenario was encrypted with an unknown key. Expected a key ID of " + KeyFileUtil.byteToString(exception.getKey(), ":");
				}
			});
		}
		return adapterType.cast(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IReasonProvider.class };
	}

}
