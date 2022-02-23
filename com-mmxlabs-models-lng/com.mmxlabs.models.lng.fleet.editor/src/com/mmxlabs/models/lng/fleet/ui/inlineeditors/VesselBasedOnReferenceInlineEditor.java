/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;

public class VesselBasedOnReferenceInlineEditor extends ReferenceInlineEditor {

	private Button copyButton;

	public VesselBasedOnReferenceInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	
	@Override
	protected boolean updateOnChangeToFeature(Object changedFeature) {
		
		if (changedFeature == FleetPackage.Literals.VESSEL__REFERENCE) {
			copyButton.setEnabled(((Vessel)input).getReference() != null);
		}
		// TODO Auto-generated method stub
		return super.updateOnChangeToFeature(changedFeature);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

		Composite newParent = new Composite(parent, SWT.NONE);
		newParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).margins(0, 0).create());

		final Control control = super.createControl(newParent, dbc, toolkit);

		copyButton = new Button(newParent, SWT.PUSH);
		copyButton.setText("Copy");
		copyButton.setToolTipText("Copy parameters to this vessel");
		copyButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (input instanceof Vessel) {
					Vessel vessel = (Vessel) input;
					Vessel reference = vessel.getReference();
					if (reference != null) {
						CompoundCommand cmd = new CompoundCommand();
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__BASE_FUEL);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__CAPACITY);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__FILL_CAPACITY);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__SCNT);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__WARMING_TIME);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__PURGE_TIME);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__COOLING_VOLUME);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__MAX_SPEED);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__MIN_SPEED);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__SAFETY_HEEL);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__MIN_BASE_FUEL_CONSUMPTION);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__PILOT_LIGHT_RATE);

						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES);
						copyFeature(cmd, reference, vessel, FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS);
						{
							VesselStateAttributes dest = vessel.getBallastAttributes();
							VesselStateAttributes source = reference.getBallastAttributes();
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED);
						}
						{
							VesselStateAttributes dest = vessel.getLadenAttributes();
							VesselStateAttributes source = reference.getLadenAttributes();
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_BASE_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_BASE_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__IN_PORT_NBO_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__NBO_RATE);
							copyFeature(cmd, source, dest, FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES__SERVICE_SPEED);
						}
						if (!cmd.isEmpty()) {
							commandHandler.handleCommand(cmd, vessel, null);

						}
					}
				}

			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		return control;
	}

	protected void copyFeature(CompoundCommand cmd, EObject source, EObject destination, EStructuralFeature feature) {
		final EditingDomain ed = commandHandler.getEditingDomain();
		if (!destination.eIsSet(feature)) {
			if (feature.isMany()) {
				if (feature instanceof EReference && ((EReference) feature).isContainment()) {
					cmd.append(SetCommand.create(ed, destination, feature, EcoreUtil.copyAll(((Collection<EObject>) source.eGet(feature)))));
				} else {
					cmd.append(SetCommand.create(ed, destination, feature, new LinkedList<>(((Collection<EObject>) source.eGet(feature)))));
				}

			} else {

				if (feature instanceof EReference && ((EReference) feature).isContainment()) {
					cmd.append(SetCommand.create(ed, destination, feature, source.eGet(feature)));
				} else {
					cmd.append(SetCommand.create(ed, destination, feature, source.eGet(feature)));
				}
			}
			EStructuralFeature feature2 = destination.eClass().getEStructuralFeature(feature.getName() + "Override");
			if (feature2 != null) {
				cmd.append(SetCommand.create(ed, destination, feature2, Boolean.TRUE));
			}
		}
	}
}
