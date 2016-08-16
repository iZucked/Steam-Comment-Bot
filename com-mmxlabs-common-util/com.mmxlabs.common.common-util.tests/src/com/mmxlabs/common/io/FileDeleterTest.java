/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.io;

import java.io.File;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class FileDeleterTest {

	/**
	 * A Unit test to try and trigger the deadlock behaviour observed running the ITS on occasions. This test does *NOT* trigger the deadlock.
	 * 
	 * The deadlock occurs in {@link System#runFinalization()} where the secondary thread is already terminated, but the current thread is waiting on the terminated state change.
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void test() throws Exception {

		ExecutorService executor = Executors.newFixedThreadPool(4);

		for (int i = 0; i < 20000; ++i) {

			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						File f = File.createTempFile("junit", "dat");
						try (PrintWriter writer = new PrintWriter(f)) {
							for (int j = 0; j < 10_000_000; ++j) {
								writer.println("test data");
							}
							writer.flush();
						}

						FileDeleter.delete(f, true);

						Assert.assertFalse(f.exists());
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			};
			executor.submit(r);
		}

		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.DAYS);
	}

}
