package com.mmxlabs.lngdataserver.commons.http;

public interface IProgressListener {
	void update(long bytesRead, long contentLength, boolean done);
}