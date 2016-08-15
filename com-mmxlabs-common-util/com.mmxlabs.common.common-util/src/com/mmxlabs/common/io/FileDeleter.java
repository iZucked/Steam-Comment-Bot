/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * A class to *more* securely erase files over {@link File}{@link #delete(File)}. Note the underlying filesystem is outside of the control of Java and may negate anything done here.
 * 
 * @see http://www.javafaq.nu/java-example-code-1121.html
 * @author Simon Goodall
 * 
 */
public final class FileDeleter {

	/**
	 * Overwrite the contents of the file a few times with zeros, ones and random data before deleting.
	 * 
	 * @param file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void delete(final File file, boolean secureDelete) throws IOException, FileNotFoundException {

		if (!secureDelete) {
			file.delete();
		}
		
		if (!file.exists()) {
			throw new IllegalStateException("File must exist");
		}

		if (file.isDirectory()) {
			throw new IllegalStateException("File must be a regular file");
		}

		final SecureRandom random = new SecureRandom();
		final RandomAccessFile raf = new RandomAccessFile(file, "rws");
		try {

			final FileChannel channel = raf.getChannel();
			try {
				final FileLock lock = channel.lock();
				try {
					// Garbage collect is the only way to free this thing!
					MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, raf.length());
					final byte[] bytes = new byte[4096];

					Arrays.fill(bytes, (byte) 0x00);
					while (buffer.hasRemaining()) {
						buffer.put(bytes, 0, Math.min(buffer.remaining(), bytes.length));
					}
					buffer.force();
					buffer.rewind();
					Arrays.fill(bytes, (byte) 0xFF);
					while (buffer.hasRemaining()) {
						buffer.put(bytes, 0, Math.min(buffer.remaining(), bytes.length));
					}
					buffer.force();
					buffer.rewind();

					Arrays.fill(bytes, (byte) 0xF0);
					while (buffer.hasRemaining()) {
						buffer.put(bytes, 0, Math.min(buffer.remaining(), bytes.length));
					}
					buffer.force();
					buffer.rewind();

					while (buffer.hasRemaining()) {
						random.nextBytes(bytes);
						buffer.put(bytes, 0, Math.min(buffer.remaining(), bytes.length));
					}
					buffer.force();
					buffer.clear();
					buffer = null;
				} finally {
					lock.release();
				}
			} finally {
				channel.close();
			}
		} finally {
			raf.close();
		}

		// We can only delete the file once the memory mapped buffer has been released. This is done when the buffer is garbage collected and finalised. This loop runs the gc() and attempts to delete
		// the file. If it is not deleted, loop again until the limit has been reached.
		// See http://stackoverflow.com/questions/2972986/how-to-unmap-a-file-from-memory-mapped-using-filechannel-in-java
		for (int i = 0; i < 100; ++i) {
			System.gc();
			System.runFinalization();
			try {
				Files.delete(file.toPath());
				break;
			} catch (Exception e) {
			}
		}
		// If we reached the limit, then the file still exists. Invoke delete one more time to propogate the exception.
		if (file.exists()) {
			Files.delete(file.toPath());
		}
	}
}
