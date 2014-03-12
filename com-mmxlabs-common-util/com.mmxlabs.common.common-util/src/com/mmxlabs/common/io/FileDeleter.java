package com.mmxlabs.common.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
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
	public static void delete(final File file) throws IOException, FileNotFoundException {

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
					final MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, raf.length());
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
				} finally {
					lock.release();
				}
			} finally {
				channel.close();
			}
		} finally {
			raf.close();
		}

		file.delete();
		System.gc();
	}

	public static void main(final String arg[]) throws Exception {

		final File f = new File("C:/temp/test.file.txt");

		final BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (int i = 0; i < 1000; ++i) {
			bw.write("Hello!");
		}
		bw.close();

		FileDeleter.delete(f);
	}
}
