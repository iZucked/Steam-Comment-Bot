/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IDiffEditHandler;

public class DefaultDiffEditHandler implements IDiffEditHandler {

	private final ScenarioInstance parent;
	private final ScenarioInstance child;
	private final ModelReference parentModelReference;
	private final ModelReference childModelReference;

	private boolean cleanUpOnDispose = true;

	public DefaultDiffEditHandler(@NonNull final ScenarioInstance child, @NonNull final ScenarioInstance parent) {
		this.child = child;
		this.childModelReference = child.getReference("DefaultDiffEditHandler:1");
		this.parent = parent;
		this.parentModelReference = parent.getReference("DefaultDiffEditHandler:2");
	}

	@Override
	public void onPreEditorCancel() {
		cleanUpOnDispose = false;

	}

	@Override
	public void onEditorCancel() {
		assert child != null;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onEditorApply() {

		// FIXME: Note tested after API Changes
		final MMXRootObject leftRoot = (MMXRootObject) childModelReference.getInstance();
		final MMXRootObject rightRoot = (MMXRootObject) parentModelReference.getInstance();

		final CommandProviderAwareEditingDomain editingDomain = (CommandProviderAwareEditingDomain) parent.getAdapters().get(EditingDomain.class);

		final CompoundCommand cmd = new CompoundCommand("Update parent");
		try {

			editingDomain.setCommandProvidersDisabled(true);
			editingDomain.setAdaptersEnabled(false);

			for (final EStructuralFeature feature : leftRoot.eClass().getEAllStructuralFeatures()) {
				if (feature.isMany() && leftRoot.eIsSet(feature)) {
					// Passing in raw lists breaks undo()/redo() as the list contents change - so take a copy instead
					cmd.append(SetCommand.create(editingDomain, rightRoot, feature, new ArrayList((List) leftRoot.eGet(feature))));
				} else {
					cmd.append(SetCommand.create(editingDomain, rightRoot, feature, leftRoot.eGet(feature)));
				}
			}

			editingDomain.getCommandStack().execute(cmd);
		} finally {
			editingDomain.setAdaptersEnabled(true, false);
			editingDomain.setCommandProvidersDisabled(false);
		}

		cleanUp();
	}

	private void cleanUp() {

		childModelReference.close();
		parentModelReference.close();

		final IScenarioService scenarioService = child.getScenarioService();
		assert scenarioService != null;
		scenarioService.delete(child);

		// Unset pin

		// Deselect entry
	}

}
