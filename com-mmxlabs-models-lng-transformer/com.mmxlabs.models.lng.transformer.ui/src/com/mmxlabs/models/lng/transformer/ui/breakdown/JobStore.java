package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
		try {
			final File f = File.createTempFile(String.format("breadth-%02d-", depth), ".dat");
			count += pendingJobs.size();
			JobStateSerialiser.save(pendingJobs, f);
			files.add(f);
			pendingJobs.clear();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<File> getFiles() {
		// Clear exising pending jobs
		if (pendingJobs.size() > 0) {
			finishFile();
		}
		return files;
	}

	public int getPersistedStateCount() {
		return count;
	}
}