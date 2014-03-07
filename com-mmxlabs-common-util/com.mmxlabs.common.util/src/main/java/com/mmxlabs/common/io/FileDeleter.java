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
 * @see http://www.javafaq.nu/java-example-code-1121.html
 * @author sg
 *
 */
public final class FileDeleter {

	public static void delete(File file) throws IOException, FileNotFoundException {

		if (!file.exists()) {
			throw new IllegalStateException("File must exist");
		}

		if (file.isDirectory()) {
			throw new IllegalStateException("File must be a regular file");
		}

		SecureRandom random = new SecureRandom();
		RandomAccessFile raf = new RandomAccessFile(file, "rws");
		try {
			// // reset buffer
			// {
			// long length = raf.length();
			// raf.seek(0);
			// byte[] bytes = new byte[4096];
			// int pos = 0;
			// while (pos < length) {
			// int len = (int)Math.min(length - pos, bytes.length);
			// random.nextBytes(bytes);
			//
			// raf.write(bytes, pos, len);
			// pos += bytes.length;
			// }
			// }

			FileChannel channel = raf.getChannel();
			try {
				FileLock lock = channel.lock();
				try {
					MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, raf.length());
					byte[] bytes = new byte[4096];

					Arrays.fill(bytes, (byte)0x00);
					while (buffer.hasRemaining()) {
						buffer.put(bytes, 0, Math.min(buffer.remaining(), bytes.length));
					}
					buffer.force();
					buffer.rewind();
					Arrays.fill(bytes, (byte)0xFF);
					while (buffer.hasRemaining()) {
						buffer.put(bytes, 0, Math.min(buffer.remaining(), bytes.length));
					}
					buffer.force();
					buffer.rewind();

					Arrays.fill(bytes, (byte)0xF0);
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

	public static void main(String arg[]) throws Exception {

		File f = new File("C:/temp/test.file.txt");

		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for (int i = 0; i < 1000; ++i) {
			bw.write("Hello!");
		}
		bw.close();

		FileDeleter.delete(f);
	}
}
