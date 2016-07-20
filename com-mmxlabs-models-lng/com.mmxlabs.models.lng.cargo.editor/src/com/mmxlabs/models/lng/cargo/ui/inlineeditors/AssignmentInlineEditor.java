/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.edit.utils.AssignableElementCommandHelper;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
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
								commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE,
										SetCommand.UNSET_VALUE), elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
							} else if (vessel instanceof CharterInMarket) {
								// assign to a new spot
								if (rootObject instanceof LNGScenarioModel) {

									// Always assign to first element
//									final CargoModel cargoModel = ((LNGScenarioModel) rootObject).getCargoModel();
									final int maxSpot = 0;//AssignmentEditorHelper.getMaxSpot(cargoModel) + 1;

									commandHandler.handleCommand(
											AssignableElementCommandHelper.reassignElement(commandHandler.getEditingDomain(), (VesselAssignmentType) vessel, elementAssignment, maxSpot),
											elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
									return;
								}
							} else if (vessel instanceof VesselAvailability) {
								commandHandler.handleCommand(
										SetCommand.create(commandHandler.getEditingDomain(), elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, vessel),
										elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
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