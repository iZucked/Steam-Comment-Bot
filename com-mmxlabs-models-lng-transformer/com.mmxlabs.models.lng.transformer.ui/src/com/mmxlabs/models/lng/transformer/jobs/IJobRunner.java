package com.mmxlabs.models.lng.transformer.jobs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

@NonNullByDefault
public interface IJobRunner {

	void withParams(File file) throws IOException;

	void withParams(String json) throws IOException;

	void withParams(InputStream is) throws IOException;

	void withLogging(Object machineInfo);

	void withScenario(IScenarioDataProvider sdp);

	default AbstractSolutionSet run(IProgressMonitor monitor) {
		// 0 Means query system for available threads.
		return run(0, monitor);
	}

	AbstractSolutionSet run(int threadsAvailable, IProgressMonitor monitor);

	void saveLogs(File file) throws IOException;

	void saveLogs(OutputStream os) throws IOException;

	String saveLogsAsString() throws IOException;

}
