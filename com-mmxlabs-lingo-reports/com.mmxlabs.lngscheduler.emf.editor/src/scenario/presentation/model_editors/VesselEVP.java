/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;

import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.detailview.containers.DetailCompositeDialog;
import com.mmxlabs.shiplingo.ui.tableview.DialogFeatureManipulator;
import com.mmxlabs.shiplingo.ui.tableview.SingleReferenceManipulator;

/**
 * @author Tom Hinton
 * 
 */
public class VesselEVP extends NamedObjectEVP {
	public VesselEVP(final IWorkbenchPage page, final ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		// TODO Auto-generated method stub
		super.init(path, adapterFactory);

		{
			final SingleReferenceManipulator vclass = new SingleReferenceManipulator(FleetPackage.eINSTANCE.getVessel_Class(), part.getVesselClassProvider(), part.getEditingDomain());
			addColumn("Class", vclass, vclass);
		}

		{
			class RequirementFeatureManipulator extends DialogFeatureManipulator {

				public RequirementFeatureManipulator(final EStructuralFeature field, final EditingDomain editingDomain) {
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
						zone = TimeZone.getTimeZone(pat.getPort().getTimeZone());
					} else {
						zone = TimeZone.getTimeZone("GMT");
						port = "Anywhere";
					}

					final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
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
				protected Object openDialogBox(final Control cellEditorWindow, final Object object) {

					final DetailCompositeDialog dcd = new DetailCompositeDialog(cellEditorWindow.getShell(), part, part.getEditingDomain());

					final int result = dcd.open(Collections.singletonList((EObject) getValue(object)));
					if (result == Window.OK) {
						return object;
					} else {
						return null;
					}
				}
			}
			final DialogFeatureManipulator startRequirement = new RequirementFeatureManipulator(FleetPackage.eINSTANCE.getVessel_StartRequirement(), part.getEditingDomain());

			addColumn("Start constraint", startRequirement, startRequirement);

			final DialogFeatureManipulator endRequirement = new RequirementFeatureManipulator(FleetPackage.eINSTANCE.getVessel_EndRequirement(), part.getEditingDomain());

			addColumn("End constraint", endRequirement, endRequirement);
		}

	}
}
