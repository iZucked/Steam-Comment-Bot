/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.impl;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;

/**
 * A {@link Cipher} which does nothing. The given {@link InputStream} and {@link OutputStream} are returned unmodified.
 * 
 * @author Simon Goodall
 * 
 */
public class PassthoughCipher implements Cipher {

	@Override
	public void finish(final InputStream inputStream) throws Exception {

	}

	@Override
	public void finish(final OutputStream outputStream) throws Exception {

	}

	@Override
	public OutputStream encrypt(final OutputStream outputStream) throws Exception {
		return outputStream;
	}

	@Override
	public InputStream decrypt(final InputStream inputStream) throws Exception {
		return inputStream;
	}
}
