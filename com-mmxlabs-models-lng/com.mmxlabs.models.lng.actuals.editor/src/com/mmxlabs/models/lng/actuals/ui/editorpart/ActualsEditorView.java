/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ActualsEditorView extends ScenarioTableViewerView<ActualsTableViewerPane> {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.actuals.editor.views.ActualsEditorView";

	public ActualsEditorView() {

	}

	@Override
	protected ActualsTableViewerPane createViewerPane() {
		return new ActualsTableViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final ActualsTableViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			final MMXRootObject rootObject = getRootObject();
			ActualsModel actualsModel = null;
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				actualsModel = lngScenarioModel.getActualsModel();
				if (actualsModel == null) {
					actualsModel = ActualsFactory.eINSTANCE.createActualsModel();
					domain.getCommandStack().execute(SetCommand.create(domain, lngScenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ActualsModel(), actualsModel));
				}
			}
			pane.init(Arrays.asList(new EReference[] { ActualsPackage.eINSTANCE.getActualsModel_CargoActuals() }), null, domain.getCommandStack());
			pane.getViewer().setInput(actualsModel);
			//
			// // Add action to create and edit cargo groups
			// pane.getToolBarManager().appendToGroup("edit", new MergePorts(this, pane.getScenarioViewer()));
			// pane.getToolBarManager().update(true);
		}
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final EObject target = dcsd.getTarget();
			if (target instanceof CargoActuals || target instanceof LoadActuals || target instanceof DischargeActuals) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
	}
}