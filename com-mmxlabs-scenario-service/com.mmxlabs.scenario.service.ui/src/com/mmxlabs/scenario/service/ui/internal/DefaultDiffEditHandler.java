/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IDiffEditHandler;

public class DefaultDiffEditHandler implements IDiffEditHandler {

	private final ScenarioInstance parent;
	private final ScenarioInstance child;

	private boolean cleanUpOnDispose = true;

	public DefaultDiffEditHandler(final ScenarioInstance child, final ScenarioInstance parent) {
		this.child = child;
		this.parent = parent;
	}

	@Override
	public void onPreEditorCancel() {
		cleanUpOnDispose = false;

	}

	@Override
	public void onEditorCancel() {

		final IScenarioService scenarioService = child.getScenarioService();
		scenarioService.delete(child);
	}

	@Override
	public void onEditorDisposed() {

		// TODO: Prompt user to apply changes?
		if (cleanUpOnDispose) {
			cleanUp();
		}
	}

	@Override
	public void onPreEditorApply() {
		cleanUpOnDispose = false;
	}

	@Override
	public void onEditorApply() {

		final MMXRootObject childRoot = (MMXRootObject) child.getInstance();
		final MMXRootObject parentRoot = (MMXRootObject) parent.getInstance();

		final EList<MMXSubModel> childSubModels = childRoot.getSubModels();
		final EList<MMXSubModel> parentSubModels = parentRoot.getSubModels();
		final CommandProviderAwareEditingDomain editingDomain = (CommandProviderAwareEditingDomain) parent.getAdapters().get(EditingDomain.class);

		final CompoundCommand cmd = new CompoundCommand("Update parent");
		try {

			editingDomain.setCommandProvidersDisabled(true);
			editingDomain.setAdaptersEnabled(false);
			for (int i = 0; i < childSubModels.size(); ++i) {

				final MMXSubModel leftSub = childSubModels.get(i);
				final MMXSubModel rightSub = parentSubModels.get(i);

				final UUIDObject left = leftSub.getSubModelInstance();
				final UUIDObject right = rightSub.getSubModelInstance();

				if (left.eClass() != right.eClass()) {
					throw new RuntimeException("OOPS");
				}
				for (final EStructuralFeature feature : left.eClass().getEAllStructuralFeatures()) {
					if (feature.isMany() && left.eIsSet(feature)) {
						// Passing in lists breaks undo()/redo() as the list contents change
						cmd.append(SetCommand.create(editingDomain, right, feature, new ArrayList((List<?>) left.eGet(feature))));
					} else {
						cmd.append(SetCommand.create(editingDomain, right, feature, left.eGet(feature)));
					}
				}
				/**
				 * // FIXME: Does not work when the schedule model changes - issues matching elements in containment lists. Might be possible to use a more specialised matcher but no luck so far...
				 * try { MatchModel match = MatchService.doMatch(left, right, null); final DiffModel diff = DiffService.doDiff(match); for (DiffElement e : diff.getOwnedElements()) {
				 * MergeService.merge(e, true); } } catch (InterruptedException e) { // TODO Auto-generated catch block e.printStackTrace(); }
				 */
			}
			editingDomain.getCommandStack().execute(cmd);
		} finally {
			editingDomain.setAdaptersEnabled(true, false);
			editingDomain.setCommandProvidersDisabled(false);
		}

		cleanUp();
	}

	private void cleanUp() {
		final IScenarioService scenarioService = child.getScenarioService();
		scenarioService.delete(child);
		
		// Unset pin 
		
		// Deselect entry
	}

}
