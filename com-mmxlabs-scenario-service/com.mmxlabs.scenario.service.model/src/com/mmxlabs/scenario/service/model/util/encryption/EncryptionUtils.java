/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.rcp.common.ServiceHelper;

public class EncryptionUtils {
	public static void encrypt(final OutputStream os, final CheckedConsumer<OutputStream, Exception> action) throws Exception {
		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, p -> {
			if (p == null) {
				action.accept(os);
			} else {
				final Cipher sharedCipher = p.getSharedCipher();
				if (sharedCipher != null) {
					try (OutputStream eos = sharedCipher.encrypt(os)) {
						try {
							action.accept(eos);
						} finally {
							sharedCipher.finish(eos);
						}
					}
				} else {
					action.accept(os);
				}
			}
		});
	}

	public static <T> T decrypt(final InputStream is, final CheckedFunction<InputStream, T, Exception> action) throws Exception {
		return ServiceHelper.withCheckedOptionalService(IScenarioCipherProvider.class, p -> {
			if (p == null) {
				return action.apply(is);
			} else {
				final Cipher sharedCipher = p.getSharedCipher();
				if (sharedCipher != null) {
					try (InputStream eis = sharedCipher.decrypt(is)) {
						try {
							return action.apply(eis);
						} finally {
							sharedCipher.finish(eis);
						}
					}
				} else {
					return action.apply(is);
				}
			}
		});
	}
}
