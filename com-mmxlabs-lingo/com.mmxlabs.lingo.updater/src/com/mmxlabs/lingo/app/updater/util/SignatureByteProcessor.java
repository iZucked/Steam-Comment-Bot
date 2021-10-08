package com.mmxlabs.lingo.app.updater.util;

import java.io.IOException;
import java.security.Signature;

import com.google.common.io.ByteProcessor;

/**
 * An adapter implementation of {@link ByteProcessor} to update a
 * {@link Signature} from an input stream.
 * 
 * @author Simon Goodall
 *
 */
public class SignatureByteProcessor implements ByteProcessor<Void> {

	private Signature signature;

	public SignatureByteProcessor(Signature signature) {
		this.signature = signature;
	}

	@Override
	public Void getResult() {
		return null;
	}

	public boolean processBytes(byte[] buf, int off, int len) throws IOException {
		try {
			signature.update(buf, off, len);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
