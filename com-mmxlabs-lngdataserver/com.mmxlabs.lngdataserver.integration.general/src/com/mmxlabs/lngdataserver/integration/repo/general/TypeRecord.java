/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

import java.io.IOException;
import java.io.OutputStream;

public interface TypeRecord {

	String getType();

	String getListURL();

	String getUploadURL();

	String getDownloadURL(String uuid);

	String getDeleteURL(String uuid);

	Class<?> getMixin();

	void writeHeader(OutputStream os) throws IOException;

}
