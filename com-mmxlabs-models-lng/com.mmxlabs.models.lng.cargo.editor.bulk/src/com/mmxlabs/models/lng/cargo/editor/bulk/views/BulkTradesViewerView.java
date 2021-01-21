/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Arrays;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorFactory;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorPackage;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
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
			Table table = CargoBulkEditorFactory.eINSTANCE.createTable();
			pane.setTable(table);
			pane.setlngScenarioModel((LNGScenarioModel) getRootObject());
			pane.init(Arrays.asList(new EReference[] {CargoBulkEditorPackage.eINSTANCE.getTable_Rows()}), null, getModelReference());
			pane.setCargoes(table, (LNGScenarioModel) getRootObject());
			pane.setlngScenarioModel(((LNGScenarioModel) getRootObject()));
			pane.getViewer().setInput(table);
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
}