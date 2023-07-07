/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.internal;

import java.util.List;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil.EncryptedScenarioException;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.ScenarioEncryptionException;
import com.mmxlabs.scenario.service.model.util.encryption.UnknownEncryptionKeyException;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;
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
		if (adaptableObject instanceof UnknownEncryptionKeyException exception) {

			final boolean[] keysPresent = new boolean[] { false };
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, p -> {
				if (p != null) {
					final Cipher sharedCipher = p.getSharedCipher();
					if (sharedCipher instanceof final DelegatingEMFCipher delegatingEMFCipher) {
						final List<byte[]> keyfiles = delegatingEMFCipher.listKeys();
						keysPresent[0] = !keyfiles.isEmpty();
					}
				}
			});

			return adapterType.cast(new IReasonProvider() {

				@Override
				public String getTitle() {
					if (keysPresent[0]) {
						return "Unknown encryption key";
					} else {
						return "Encrypted scenario";
					}
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
					if (keysPresent[0]) {
						return "The scenario was encrypted with an unknown key. Expected a key ID of " + KeyFileUtil.byteToString(exception.getKey(), ":");
					} else {
						return "The scenario is encrypted and there are no encryption keys installed";
					}
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
