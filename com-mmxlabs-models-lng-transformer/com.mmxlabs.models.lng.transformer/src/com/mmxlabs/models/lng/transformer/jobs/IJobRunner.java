/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.jobs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public interface IJobRunner {

	/**
	 * When running a batch of jobs, this is the individual job id used to e.g.
	 * select the correct parameter set
	 * 
	 * @param subJobId
	 */
	void withSubJobId(int subJobId);

	void withParams(File file) throws IOException;

	void withParams(String json) throws IOException;

	void withParams(InputStream is) throws IOException;

	void withLogging(Object machineInfo);

	void withScenario(IScenarioDataProvider sdp);

	/**
	 * Returns null if cancelled.
	 * 
	 * @param threadsAvailable
	 * @param monitor
	 * @return
	 */
	@Nullable
	AbstractSolutionSet run(int threadsAvailable, IProgressMonitor monitor);

	default @Nullable AbstractSolutionSet run(IProgressMonitor monitor) {
		// 0 Means query system for available threads.
		return run(0, monitor);
	}

	void saveLogs(File file) throws IOException;

	void saveLogs(OutputStream os) throws IOException;

	String saveLogsAsString() throws IOException;

	void withScenarioInstance(ScenarioInstance scenarioInstance);

}
