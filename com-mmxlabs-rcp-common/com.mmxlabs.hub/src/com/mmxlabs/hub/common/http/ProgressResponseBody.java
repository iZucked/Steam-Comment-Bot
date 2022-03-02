/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * From https://github.com/square/okhttp/blob/master/samples/guide/src/main/java/okhttp3/recipes/Progress.java
 */
public class ProgressResponseBody extends ResponseBody {

	private final ResponseBody responseBody;
	private final IProgressListener progressListener;
	private BufferedSource bufferedSource;

	public ProgressResponseBody(ResponseBody responseBody, IProgressListener progressListener) {
		this.responseBody = responseBody;
		this.progressListener = progressListener;
	}

	@Override
	public MediaType contentType() {
		return responseBody.contentType();
	}

	@Override
	public long contentLength() {
		return responseBody.contentLength();
	}

	@Override
	public BufferedSource source() {
		if (bufferedSource == null) {
			bufferedSource = Okio.buffer(source(responseBody.source()));
		}
		return bufferedSource;
	}

	private Source source(Source source) {
		return new ForwardingSource(source) {
			long totalBytesRead = 0L;

			@Override
			public long read(Buffer sink, long byteCount) throws IOException {
				long bytesRead = super.read(sink, byteCount);
				// read() returns the number of bytes read, or -1 if this source is exhausted.
				totalBytesRead += bytesRead != -1 ? bytesRead : 0;
				progressListener.update(bytesRead != -1 ? bytesRead : 0, responseBody.contentLength(), bytesRead == -1);
				return bytesRead;
			}
		};
	}
}