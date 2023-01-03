/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

public interface IProgressListener {
	void update(long bytesRead, long contentLength, boolean done);
}