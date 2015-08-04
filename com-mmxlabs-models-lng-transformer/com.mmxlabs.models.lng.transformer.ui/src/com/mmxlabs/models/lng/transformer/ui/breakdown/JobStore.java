package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mmxlabs.models.lng.transformer.ui.BreadthOptimiser.JobState;

/**
 * This class is used to persist the {@link JobStateMode#LIMITED} jobs as they are created. For a given depth in the search, an instance is shared for all search evolutions, saving batches of
 * {@link JobState}s as they are created rather than allowing potentially 100,000s of objects to be returned, then serialised as this can lead to memory issues. This class does not take ownership of
 * the temporary files and they must be cleaned up separately.
 * 
 * @author Simon Goodall
 *
 */
public final class JobStore {

	private final int depth;
	private final List<JobState> pendingJobs = new LinkedList<>();
	private final List<File> files = new LinkedList<>();
	private int count = 0;

	ExecutorService backgroundSaver;

	public JobStore(final int depth) {
		this.depth = depth;
	}

	public void store(final JobState jobState) {
		// Add current file to list
		pendingJobs.add(jobState);

		// Hit size limit? Flush to disk
		if (pendingJobs.size() > 10000) {
			finishFile();
		}
	}

	private void finishFile() {
		if (backgroundSaver == null) {
			backgroundSaver = Executors.newSingleThreadExecutor();
		}

		final List<JobState> jobs = new LinkedList<>(pendingJobs);
		count += pendingJobs.size();
		// Push save into a separate thread to avoid I/O blocks in main search.
		backgroundSaver.submit(new Runnable() {
			@Override
			public void run() {
				try {
					final File f = File.createTempFile(String.format("breadth-%02d-", depth), ".dat");
					JobStateSerialiser.save(jobs, f);
					jobs.clear();
					files.add(f);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		pendingJobs.clear();
	}

	/**
	 * No more results are expected to be added to this {@link JobStore}, flush any remaining files to disk and return the list of files.
	 * 
	 * @return
	 */
	public List<File> getFiles() {
		// Clear existing pending jobs
		if (pendingJobs.size() > 0) {
			finishFile();
		}

		if (backgroundSaver != null) {
			// Block until all jobs complete as we are nearly finished with this object and need all the results persisted.
			backgroundSaver.shutdown();
			try {
				while (!backgroundSaver.awaitTermination(100, TimeUnit.MILLISECONDS))
					;
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			backgroundSaver = null;
		}

		return files;
	}

	public int getPersistedStateCount() {
		return count;
	}
}