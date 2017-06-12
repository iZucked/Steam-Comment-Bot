/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.routeslots;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class RouteOptionsEditorView extends ScenarioTableViewerView<RouteOptionEditorPane> {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.cargo.editor.views.RouteOptionsEditorView";

	public RouteOptionsEditorView() {

	}

	@Override
	protected RouteOptionEditorPane createViewerPane() {
		return new RouteOptionEditorPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final RouteOptionEditorPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			final MMXRootObject rootObject = getRootObject();
			CanalBookings canalBookingsModel = null;
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				@NonNull
				CargoModel cargoModel = ScenarioModelUtil.getCargoModel(lngScenarioModel);
				canalBookingsModel = cargoModel.getCanalBookings();
				if (canalBookingsModel == null) {
					canalBookingsModel = CargoFactory.eINSTANCE.createCanalBookings();
					domain.getCommandStack().execute(SetCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_CanalBookings(), canalBookingsModel));
				}
			}
			pane.init(Arrays.asList(new EReference[] { CargoPackage.eINSTANCE.getCanalBookings_CanalBookingSlots() }), null, domain.getCommandStack());
			pane.getViewer().setInput(canalBookingsModel);
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
			if (target instanceof CanalBookingSlot) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
	}
}