/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to persist the {@link JobStateMode#LIMITED} jobs as they are created. For a given depth in the search, an instance is shared for all search evolutions, saving batches of
 * {@link JobState}s as they are created rather than allowing potentially 100,000s of objects to be returned, then serialised as this can lead to memory issues. This class does not take ownership of
 * the temporary files and they must be cleaned up separately.
 * 
 * @author Simon Goodall
 *
 */
public final class JobStore {
	private boolean foundBranch = false;
	private final int depth;
	private final List<JobState> pendingJobs = new LinkedList<>();
	private final List<File> files = new LinkedList<>();
	private int count = 0;

	// Use a Semaphore to limit the number of jobs kept in memory while we wait for I/O to catch up.
	private Semaphore s = new Semaphore(20);
	private ExecutorService backgroundSaver;

	public JobStore(final int depth) {
		this.depth = depth;
	}

	Random r = new Random(0);

	public void store(final JobState jobState) {

		if (foundBranch) {
			return;
		}

		// Store 1 in a 100 job states
		if (r.nextInt(100) != 0) {
			return;
		}

		// Add current file to list
		pendingJobs.add(jobState);

		// Hit size limit? Flush to disk
		if (pendingJobs.size() >= 10000) {
			finishFile();
		}
	}

	private void finishFile() {
		if (backgroundSaver == null) {
			backgroundSaver = Executors.newSingleThreadExecutor();
		}

		final List<JobState> jobs = new LinkedList<>(pendingJobs);
		count += pendingJobs.size();

		try {
			// Use a semaphore to limit the amount of pending jobs. Slow I/O could result in a lot of pending saves leading to heap issues.
			s.acquire();
			// Push save into a separate thread to avoid I/O blocks in main search.
			backgroundSaver.submit(new Runnable() {
				@Override
				public void run() {
					try {
						if (foundBranch) {
							return;
						}
						final File f = File.createTempFile(String.format("breadth-%02d-", depth), ".dat");
						JobStateSerialiser.save(jobs, f);
						jobs.clear();
						files.add(f);
					} catch (final Exception e) {
						throw new RuntimeException(e);
					} finally {
						s.release();
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		pendingJobs.clear();

	}

	/**
	 * No more results are expected to be added to this {@link JobStore}, flush any remaining files to disk and return the list of files.
	 * 
	 * @return
	 */
	public List<File> getFiles() {
		// Clear existing pending jobs
		if (!foundBranch && pendingJobs.size() > 0) {
			finishFile();
		}

		if (backgroundSaver != null) {
			// Block until all jobs complete as we are nearly finished with this object and need all the results persisted.
			backgroundSaver.shutdown();
			try {
				while (!backgroundSaver.awaitTermination(100, TimeUnit.MILLISECONDS))
					;
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			backgroundSaver = null;
		}

		return files;
	}

	public int getPersistedStateCount() {
		return count;
	}

	public void setFoundBranch() {
		foundBranch = true;
		pendingJobs.clear();
		if (backgroundSaver != null) {
			backgroundSaver.shutdownNow();
			backgroundSaver = null;
		}
	}
}