/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

public interface IProgressListener {
	void update(long bytesRead, long contentLength, boolean done);
}