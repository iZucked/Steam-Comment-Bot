/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.impl;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IKeyFile {

	byte[] getKeyUUID();

	InputStream decrypt(InputStream inputStream) throws Exception;

	void finish(InputStream in) throws Exception;

	OutputStream encrypt(OutputStream outputStream) throws Exception;

	void finish(OutputStream os) throws Exception;

}
