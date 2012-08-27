/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoModelEditorContribution extends BaseJointModelEditorContribution<CargoModel> {
	private int cargoPageNumber = -1;
	private int loadSlotPageNumber = -1;
	private int dischargeSlotPageNumber = -1;
	private int wiringPageNumber = -1;;
	private CargoModelViewer cargoViewerPane;
	private LoadSlotViewer loadSlotViewerPane;
	private DischargeSlotViewer dischargeSlotViewerPane;
	private CargoWiringViewer wiringViewer;

	// Temp flag to turn on/off load/discharge slots during development
	private static final boolean showSlotTabs = true;

	@Override
	public void addPages(final Composite parent) {
		{
			this.cargoViewerPane = new CargoModelViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			cargoViewerPane.createControl(parent);
			cargoViewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_Cargoes()), editorPart.getAdapterFactory());
			cargoViewerPane.getViewer().setInput(modelObject);
			cargoPageNumber = editorPart.addPage(cargoViewerPane.getControl());
			editorPart.setPageText(cargoPageNumber, "Cargoes");
		}
		if (showSlotTabs) {
			this.loadSlotViewerPane = new LoadSlotViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			loadSlotViewerPane.createControl(parent);
			loadSlotViewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_LoadSlots()), editorPart.getAdapterFactory());
			loadSlotViewerPane.getViewer().setInput(modelObject);
			loadSlotPageNumber = editorPart.addPage(loadSlotViewerPane.getControl());
			editorPart.setPageText(loadSlotPageNumber, "Load Slots");
		}
		if (showSlotTabs) {
			this.dischargeSlotViewerPane = new DischargeSlotViewer(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			dischargeSlotViewerPane.createControl(parent);
			dischargeSlotViewerPane.init(Collections.singletonList(CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()), editorPart.getAdapterFactory());
			dischargeSlotViewerPane.getViewer().setInput(modelObject);
			dischargeSlotPageNumber = editorPart.addPage(dischargeSlotViewerPane.getControl());
			editorPart.setPageText(dischargeSlotPageNumber, "Discharge Slots");
		}
		
		wiringViewer = new CargoWiringViewer(parent, editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		wiringPageNumber = editorPart.addPage(wiringViewer);
		editorPart.setPageText(wiringPageNumber, "Wiring");
	}

	@Override
	public void setLocked(final boolean locked) {
		cargoViewerPane.setLocked(locked);
		if (showSlotTabs) {
			loadSlotViewerPane.setLocked(locked);
			dischargeSlotViewerPane.setLocked(locked);
		}
		wiringViewer.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof Cargo) {
				return true;
			} else if (dcsd.getTarget() instanceof LoadSlot) {
				return true;
			} else if (dcsd.getTarget() instanceof DischargeSlot) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			Cargo cargo = null;
			if (dcsd.getTarget() instanceof Cargo) {
				cargo = (Cargo) dcsd.getTarget();
			} else if (dcsd.getTarget() instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) dcsd.getTarget();
				cargo = loadSlot.getCargo();
			} else if (dcsd.getTarget() instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) dcsd.getTarget();
				cargo = dischargeSlot.getCargo();
			}
			editorPart.setActivePage(cargoPageNumber);
			if (cargo != null) {
				cargoViewerPane.getScenarioViewer().setSelection(new StructuredSelection(cargo), true);
			}
			// TODO: Handle load/discharge slots better - e.g. if there is no cargo
		}
	}
}
