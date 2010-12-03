/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core;

import java.util.List;

public interface IJobManager {

	List<IManagedJob> getJobs();

	void addJob(IManagedJob job);

	void removeJob(IManagedJob job);

	void addJobManagerListener(IJobManagerListener jobManagerListener);

	void removeJobManagerListener(IJobManagerListener jobManagerListener);

	void toggleJobSelection(IManagedJob job);

	List<IManagedJob> getSelectedJobs();

}