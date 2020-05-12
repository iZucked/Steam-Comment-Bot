/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.hub.common.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * From https://stackoverflow.com/questions/35528751/okhttp-3-tracking-multipart-upload-progress
 * 
 *
 */
public class ProgressRequestBody extends RequestBody {

	protected RequestBody mDelegate;
	protected IProgressListener mListener;
	protected CountingSink mCountingSink;

	public ProgressRequestBody(RequestBody delegate, IProgressListener listener) {
		mDelegate = delegate;
		mListener = listener;
	}

	@Override
	public MediaType contentType() {
		return mDelegate.contentType();
	}

	@Override
	public long contentLength() {
		try {
			return mDelegate.contentLength();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void writeTo(BufferedSink sink) throws IOException {
		mCountingSink = new CountingSink(sink);
		BufferedSink bufferedSink = Okio.buffer(mCountingSink);
		mDelegate.writeTo(bufferedSink);
		bufferedSink.flush();
	}

	protected final class CountingSink extends ForwardingSink {
		private long bytesWritten = 0;

		public CountingSink(Sink delegate) {
			super(delegate);
		}

		@Override
		public void write(Buffer source, long byteCount) throws IOException {
			super.write(source, byteCount);
			bytesWritten += byteCount;
			mListener.update(byteCount, contentLength(), bytesWritten == contentLength());
		}
	}

	public interface Listener {
		void onProgress(int progress);
	}
}