package com.mmxlabs.jobcontroller.core;



public interface IManagedJob {
	
	String getName();
	
	public static int NO_PROGRESS = -1;
	
	public enum JobState {
		UNKNOWN, INITIALISED, RUNNING, PAUSING, PAUSED, RESUMING, COMPLETED, ABORTED
	}
	
	/**
	 * Initialise job - ensure {@link #getTotalProgress()} will return a valid
	 * value. Prepare job prior to a call to {@link #start()}. This is expected
	 * to be a quickly executed method and any lengthy processing should be
	 * performed in the {@link #start()} method rather than here.
	 */
	void init();

	void start();

	void stop();

	void pause();

	void resume();

	/**
	 * Returns current progress of job. Returns {@link #NO_PROGRESS} when there is no progress to report.
	 * @return
	 */
	int getProgress();

	int getTotalProgress();

	
	
	Object getSchedule();

	void addManagedJobListener(IManagedJobListener managedJobListener);
	void removeManagedJobListener(IManagedJobListener managedJobListener);
	
	JobState getJobState();
}
