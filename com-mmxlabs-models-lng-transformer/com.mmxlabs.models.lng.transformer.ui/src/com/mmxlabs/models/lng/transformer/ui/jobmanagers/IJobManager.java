package com.mmxlabs.models.lng.transformer.ui.jobmanagers;

import java.util.Collection;
import java.util.function.BiConsumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.IProgressProvider;

@NonNullByDefault
public interface IJobManager extends IProgressProvider {

	Collection<Task> getTasks();

	void submit(String taskName, JobDataRecord job, @Nullable CheckedFunction<IScenarioDataProvider, @Nullable String, Exception> parametersFactory,
			@Nullable ToBooleanFunction<IScenarioDataProvider> validationFactory, BiConsumer<IScenarioDataProvider, @Nullable AbstractSolutionSet> applyFunction);

	void cancel(Task task);

	void cancelAll(String scenarioUUID);

	void remove(Task task);

	void removeAll(String scenarioUUID);

	boolean hasActiveJob(String uuid);

}
