/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortEditorPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortView extends ScenarioTableViewerView<PortEditorPane> {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortView";

	public PortView() {

	}

	@Override
	protected PortEditorPane createViewerPane() {
		return new PortEditorPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final PortEditorPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ReferenceModel(), LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_PortModel(),
					PortPackage.eINSTANCE.getPortModel_Ports() }), null, getModelReference());
			pane.getViewer().setInput(getRootObject());

			pane.getToolBarManager().update(true);
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
			Port port = null;
			if (dcsd.getTarget() instanceof Port) {
				port = (Port) dcsd.getTarget();
			} else if (dcsd.getTarget() instanceof Location) {
				final Location location = (Location) dcsd.getTarget();
				port = (Port) location.eContainer();
			}
			if (port != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(port), true);
			}
		}
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IScenarioEditingLocation.class.isAssignableFrom(adapter)) {
			return (T) this;
		}
		return super.getAdapter(adapter);
	}
}