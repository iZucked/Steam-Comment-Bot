/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.commons.http;

public interface IProgressListener {
	void update(long bytesRead, long contentLength, boolean done);
}