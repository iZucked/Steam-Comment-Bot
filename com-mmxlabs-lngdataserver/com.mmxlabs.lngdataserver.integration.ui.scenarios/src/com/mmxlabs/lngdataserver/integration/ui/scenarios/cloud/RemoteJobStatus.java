package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

public enum RemoteJobStatus {
	SUBMITTED, // Job submitted to executor
	RUNNING, // Job is running
	FINISHED, // Job is complete, but not yet processed
	DOWNLOADING_RESULT, // Result is being downloaded
	DOWNLOADED_RESULT, // Result has been downloaded, but not merged in
	MERGING_RESULT, // Starting to merge result into scenario
	MERGED_RESULT, // Result is merged in. Job is finished
	FAILED, // Job failed somewhere along the line
	UNKNOWN // Job state is unknown.
}
