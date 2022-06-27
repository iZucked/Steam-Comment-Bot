package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public interface ISolutionSetTransformer<T extends AbstractSolutionSet> {

	@NonNull
	ChangeSetRoot createDataModel(ScenarioInstance scenarioInstance, //
			@Nullable ScenarioModelRecord modelRecord, //
			@NonNull T solutionSet, //
			// boolean preferSolutionBase, //
			@NonNull IProgressMonitor monitor, //
			@Nullable NamedObject target);

}
