/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo;

import java.io.IOException;
import java.io.OutputStream;

public interface TypeRecord {

	String getType();

	String getListURL();

	String getUploadURL();

	String getDownloadURL(String uuid);

	String getDeleteURL(String uuid);

	String getVersionNotificationEndpoint();

	Class<?> getMixin();

	void writeHeader(OutputStream os) throws IOException;

}
