/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.FrameworkUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ui.analytics.EvaluateSolutionSetHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.EMFCopier;
import com.mmxlabs.rcp.common.json.JSONReference;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.dnd.IScenarioFragmentCopyHandler;

public class FragmentCopyHandler implements IScenarioFragmentCopyHandler {

	@Override
	public boolean copy(@NonNull final ScenarioFragment scenarioFragment, @NonNull final ScenarioInstance target) {

		final ScenarioInstance source = scenarioFragment.getScenarioInstance();
		final ScenarioModelRecord sourceRecord = SSDataManager.Instance.getModelRecord(source);

		try (IScenarioDataProvider sourceSDP = sourceRecord.aquireScenarioDataProvider("FragmentCopyHandler:1")) {
			final EObject fragment = scenarioFragment.getFragment();
			if (fragment instanceof OptionAnalysisModel) {
				final OptionAnalysisModel sourceModel = (OptionAnalysisModel) fragment;

				if (target == source) {
					final ModelReference sourceReference = sourceSDP.getModelReference();
					final OptionAnalysisModel copyModel = EMFCopier.copy(sourceModel);
					final LNGScenarioModel targetModel = (LNGScenarioModel) sourceReference.getInstance();
					final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(targetModel);
					analyticsModel.getOptionModels().add(copyModel);
					sourceReference.setDirty();
					return true;
				} else {
					final ScenarioModelRecord targetRecord = SSDataManager.Instance.getModelRecord(target);
					try (IScenarioDataProvider targetSDP = targetRecord.aquireScenarioDataProvider("FragmentCopyHandler:2")) {

						try {
							final OptionAnalysisModel model = copySandboxModelWithJSON(sourceSDP, sourceModel, targetSDP);
							if (model != null) {
								EvaluateSolutionSetHelper.recomputeSolution(targetRecord, targetRecord.getScenarioInstance(), model.getResults(), true, true);
								return true;
							}
						} catch (final JsonProcessingException e) {
							e.printStackTrace();
						}
						return false;
					}
				}
			}
		}

		return false;

	}

	public static OptionAnalysisModel copySandboxModelWithJSON(final IScenarioDataProvider sourceSDP, final OptionAnalysisModel sourceModel, final IScenarioDataProvider targetSDP)
			throws JsonProcessingException {
		final String json = SharedScenarioDataUtils.createSandboxJSON(sourceSDP, sourceModel);

		final List<Pair<JSONReference, String>> missingReferences = new LinkedList<>();
		// Reference will be populated on successful command execution.
		final OptionAnalysisModel[] modelRef = new OptionAnalysisModel[1];
		final BiConsumer<CompoundCommand, IScenarioDataProvider> updater = SharedScenarioDataUtils.createSandboxUpdater(json, (ref, lbl) -> {
			missingReferences.add(Pair.of(ref, lbl));
			return false;
		}, modelRef);

		final CompoundCommand cmd = new CompoundCommand("Copy sandbox");
		missingReferences.clear();
		updater.accept(cmd, targetSDP);

		final EditingDomain domain = targetSDP.getEditingDomain();

		if (cmd.canExecute()) {
			domain.getCommandStack().execute(cmd);

			if (!missingReferences.isEmpty()) {
				// Revert change if there are missing references

				domain.getCommandStack().undo();
				if (System.getProperty("lingo.suppress.dialogs") == null) {
					final String msg = "Unable to find all data references";

					final List<Status> ss = new LinkedList<>();
					final Set<String> messages = new HashSet<>();
					for (final Pair<JSONReference, String> p : missingReferences) {
						final JSONReference ref = p.getFirst();
						String lbl = p.getSecond();
						if (lbl == null) {
							final int idx = ref.getClassType().lastIndexOf('/');
							lbl = (idx > 0) ? ref.getClassType().substring(idx + 1) : ref.getClassType();
						}
						messages.add(String.format("Missing %s: %s", lbl, ref.getName()));
					}
					final String pluginId = FrameworkUtil.getBundle(FragmentCopyHandler.class).getSymbolicName();
					messages.forEach(m -> ss.add(new Status(IStatus.ERROR, pluginId, m)));

					RunnerHelper.syncExecDisplayOptional(() -> ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error copying sandbox", msg,
							new MultiStatus(pluginId, IStatus.ERROR, ss.toArray(new IStatus[0]), "Unable to find all data references", null)));

					// LiNGO failure exit point
					return null;
				} else {
					// ITS failure exit point
					throw new IllegalStateException();
				}
			}
			return modelRef[0];
		} else {
			throw new RuntimeException("Unable to execute update command");
		}
	}
}
