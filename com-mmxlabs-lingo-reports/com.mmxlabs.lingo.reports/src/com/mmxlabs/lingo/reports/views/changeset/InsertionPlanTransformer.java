/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.analytics.SlotInsertionOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class InsertionPlanTransformer {

	public ChangeSetRoot createDataModel(final ScenarioInstance instance, final SlotInsertionOptions plan, final IProgressMonitor monitor) {

		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();

		final List<ScenarioResult> stages = new LinkedList<>();

		// Assuming first option is the base.
		for (final SlotInsertionOption option : plan.getInsertionOptions()) {
			stages.add(new ScenarioResult(instance, option.getScheduleModel()));
		}

		try {
			final ScheduleResultListTransformer transformer = new ScheduleResultListTransformer();
			monitor.beginTask("Opening insertion plans", stages.size());
			ScenarioResult base = null;
			for (final ScenarioResult current : stages) {
				if (base != null) {
					root.getChangeSets().add(transformer.buildChangeSet(stages.get(0), base, current));
				}
				if (base == null) {
					base = current;
				}
				monitor.worked(1);

			}
		} finally {
			monitor.done();
		}

		return root;

	}

}