/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.inlineeditors;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;

public final class AssignmentInlineEditor extends ReferenceInlineEditor {

	public AssignmentInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

		final Control c = super.createControl(parent, dbc, toolkit);

		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (AssignmentInlineEditor.this.input instanceof AssignableElement) {
					final AssignableElement elementAssignment = (AssignableElement) AssignmentInlineEditor.this.input;

					// apply change
					final int index = combo.getSelectionIndex();
					if (index >= 0) {
						final EObject vessel = valueList.get(index);
						// Uh oh.....
						if (elementAssignment != null) {
							if (vessel == null) {
								commandHandler.handleCommand(
										SetCommand.create(commandHandler.getEditingDomain(), elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT, SetCommand.UNSET_VALUE),
										elementAssignment, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
							} else if (vessel instanceof VesselClass) {
								// assign to a new spot
								if (rootObject instanceof LNGScenarioModel) {

									final CargoModel cargoModel = ((LNGScenarioModel) rootObject).getPortfolioModel().getCargoModel();
									final int maxSpot = AssignmentEditorHelper.getMaxSpot(cargoModel) + 1;

									commandHandler.handleCommand(AssignmentEditorHelper.reassignElement(commandHandler.getEditingDomain(), (AVesselSet<Vessel>) vessel, elementAssignment, maxSpot),
											elementAssignment, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
									return;
								}
							} else if (vessel instanceof Vessel) {
								commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), elementAssignment, CargoPackage.eINSTANCE.getAssignableElement_Assignment(), vessel),
										elementAssignment, CargoPackage.eINSTANCE.getAssignableElement_Assignment());
							} else {
								throw new RuntimeException("Unexpected vessel assignment type");
							}
						}
					}
				}
			}
		});

		return c;
	}
}