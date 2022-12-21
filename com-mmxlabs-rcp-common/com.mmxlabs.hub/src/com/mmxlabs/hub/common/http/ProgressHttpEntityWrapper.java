package com.mmxlabs.hub.common.http;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.eclipse.jdt.annotation.Nullable;

/**
 * See also https://www.baeldung.com/httpclient-post-http-request
 * 
 * @author sg
 *
 */
public class ProgressHttpEntityWrapper extends HttpEntityWrapper {

	private final @Nullable IProgressListener progressListener;

	public ProgressHttpEntityWrapper(final HttpEntity entity, final @Nullable IProgressListener progressListener) {
		super(entity);
		this.progressListener = progressListener;
	}

	@Override
	public void writeTo(final OutputStream out) throws IOException {
		if (progressListener != null) {
			final long contentLength = getContentLength();
			final FilterOutputStream fos = new FilterOutputStream(out) {

				@Override
				public void write(final int b) throws IOException {
					out.write(b);
					final int bytesRead = 1;
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
				}

				@Override
				public void write(final byte[] b, final int off, final int len) throws IOException {
					out.write(b, off, len);
					final int bytesRead = len;
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
				}

				@Override
				public void write(final byte[] b) throws IOException {
					out.write(b);
					final int bytesRead = b.length;
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);

				}
			};
			super.wrappedEntity.writeTo(fos);
		} else {
			super.wrappedEntity.writeTo(out);
		}
	}

	@Override
	public InputStream getContent() throws IOException {
		if (progressListener != null) {
			final long contentLength = getContentLength();
			InputStream in = super.getContent();
			return new FilterInputStream(in) {

				@Override
				public int read() throws IOException {
					final int bytesRead = 1;
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
					return in.read();
				}

				@Override
				public int read(byte[] b) throws IOException {
					final int bytesRead = in.read(b);
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
					return bytesRead;
				}

				@Override
				public int read(byte[] b, int off, int len) throws IOException {
					final int bytesRead = in.read(b, off, len);
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
					return bytesRead;
				}

				@Override
				public byte[] readAllBytes() throws IOException {
					byte[] b = in.readAllBytes();
					final int bytesRead = b.length;
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
					return b;
				}

				@Override
				public int readNBytes(byte[] b, int off, int len) throws IOException {
					final int bytesRead = in.readNBytes(b, off, len);
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);
					return bytesRead;
				}

				@Override
				public byte[] readNBytes(int len) throws IOException {
					byte[] b = in.readNBytes(len);
					final int bytesRead = b.length;
					progressListener.update(bytesRead != -1 ? bytesRead : 0, contentLength, bytesRead == -1);

					return b;
				}
			};
		} else {
			return super.wrappedEntity.getContent();
		}
	}
}