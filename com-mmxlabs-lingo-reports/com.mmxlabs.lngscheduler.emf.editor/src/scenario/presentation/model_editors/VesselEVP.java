/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;

import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.DialogFeatureManipulator;
import scenario.presentation.cargoeditor.EObjectEditorViewerPane;
import scenario.presentation.cargoeditor.SingleReferenceManipulator;
import scenario.presentation.cargoeditor.detailview.EObjectDetailDialog;
import scenario.presentation.cargoeditor.dialogs.PortAndTimeDialog;

/**
 * @author Tom Hinton
 * 
 */
public class VesselEVP extends NamedObjectEVP {
	public VesselEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		// TODO Auto-generated method stub
		super.init(path, adapterFactory);

		{
			final SingleReferenceManipulator vclass = new SingleReferenceManipulator(
					FleetPackage.eINSTANCE.getVessel_Class(),
					part.getVesselClassProvider(), part.getEditingDomain());
			addColumn("Class", vclass, vclass);
		}

		{
			class RequirementFeatureManipulator extends
					DialogFeatureManipulator {

				public RequirementFeatureManipulator(
						final EStructuralFeature field,
						final EditingDomain editingDomain) {
					super(field, editingDomain);
				}

				@Override
				protected String renderValue(final Object value) {
					final PortAndTime pat = (PortAndTime) value;
					if (pat == null) {
						return "No constraint";
					}

					final String port;
					final String startTime;
					final String endTime;

					final TimeZone zone;
					if (pat.isSetPort()) {
						port = pat.getPort().getName();
						zone = TimeZone.getTimeZone(pat.getPort()
								.getTimeZone());
					} else {
						zone = TimeZone.getTimeZone("GMT");
						port = "Anywhere";
					}

					final DateFormat df = DateFormat.getDateTimeInstance(
							DateFormat.SHORT, DateFormat.SHORT);
					df.setTimeZone(zone);

					if (pat.isSetStartTime()) {
						startTime = df.format(pat.getStartTime());
					} else {
						startTime = "any time";
					}

					if (pat.isSetEndTime()) {
						endTime = df.format(pat.getEndTime());
					} else {
						endTime = "any time";
					}

					return port + " from " + startTime + " to " + endTime;
				}

				@Override
				protected Object openDialogBox(
						final Control cellEditorWindow, final Object object) {
					
					final EObjectDetailDialog eodd = 
							new EObjectDetailDialog(cellEditorWindow.getShell(), ((SWT.DIALOG_TRIM & ~SWT.CLOSE)
									| SWT.APPLICATION_MODAL), part.getEditingDomain());
					
					
					part.setupDetailViewContainer(eodd);
					
					final Collection<EObject> result = eodd.open(Collections.singletonList((EObject) getValue(object)));
					if (result.isEmpty()) return null;
					else return result.iterator().next();
				}
			}
			final DialogFeatureManipulator startRequirement = new RequirementFeatureManipulator(
					FleetPackage.eINSTANCE.getVessel_StartRequirement(),
					part.getEditingDomain());

			addColumn("Start constraint", startRequirement,
					startRequirement);

			final DialogFeatureManipulator endRequirement = new RequirementFeatureManipulator(
					FleetPackage.eINSTANCE.getVessel_EndRequirement(),
					part.getEditingDomain());

			addColumn("End constraint", endRequirement,
					endRequirement);
		}

	}
}
