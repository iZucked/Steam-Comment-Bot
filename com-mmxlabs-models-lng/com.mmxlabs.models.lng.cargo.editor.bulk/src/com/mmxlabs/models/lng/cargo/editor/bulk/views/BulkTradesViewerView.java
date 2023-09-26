/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Arrays;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;


import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelFactory;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class BulkTradesViewerView extends ScenarioTableViewerView<BulkTradesTablePane> {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.views.CargoView";

	public BulkTradesViewerView() {

	}

	public void buildModel() {
		
	}
	@Override
	protected BulkTradesTablePane createViewerPane() {
		return new BulkTradesTablePane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final BulkTradesTablePane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			TradesTableRoot tradesTableRoot = CargoEditorModelFactory.eINSTANCE.createTradesTableRoot();
			pane.setTradesTableRoot(tradesTableRoot);
			pane.setlngScenarioModel((LNGScenarioModel) getRootObject());
			pane.init(Arrays.asList(new EReference[] {CargoEditorModelPackage.eINSTANCE.getTradesTableRoot_TradesRows()}), null, getModelReference());
			pane.setCargoes(tradesTableRoot, (LNGScenarioModel) getRootObject());
			pane.setlngScenarioModel(((LNGScenarioModel) getRootObject()));
			pane.getViewer().setInput(tradesTableRoot);
		}
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator dcsd) {
			Port port = null;
			if (dcsd.getTarget() instanceof Port) {
				port = (Port) dcsd.getTarget();
			} else if (dcsd.getTarget() instanceof Location location) {
				port = (Port) location.eContainer();
			}
			if (port != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(port), true);
			}
		}
	}
}