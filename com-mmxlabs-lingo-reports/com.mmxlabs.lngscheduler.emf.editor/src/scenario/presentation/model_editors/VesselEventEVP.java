/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.fleet.FleetPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.BasicAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.DateManipulator;
import com.mmxlabs.shiplingo.ui.tableview.MultipleReferenceManipulator;
import com.mmxlabs.shiplingo.ui.tableview.NonEditableColumn;
import com.mmxlabs.shiplingo.ui.tableview.NumericAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.SingleReferenceManipulator;

/**
 * EVP for vessel events
 * 
 * @author Tom Hinton
 * 
 */
public class VesselEventEVP extends ScenarioObjectEditorViewerPane {
	public VesselEventEVP(final IWorkbenchPage page, final ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		// TODO Auto-generated method stub
		super.init(path, adapterFactory);
		final FleetPackage fp = FleetPackage.eINSTANCE;

		final NonEditableColumn type = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof EObject) {
					return ((EObject) object).eClass().getName();
				} else {
					return object.getClass().getSimpleName();
				}
			}
		};

		addTypicalColumn("Event Type", type);

		final BasicAttributeManipulator id = new BasicAttributeManipulator(fp.getVesselEvent_Id(), part.getEditingDomain());

		addColumn("ID", id, id);

		final DateManipulator start = new DateManipulator(fp.getVesselEvent_StartDate(), part.getEditingDomain());
		addColumn("Start Date", start, start);

		final DateManipulator end = new DateManipulator(fp.getVesselEvent_EndDate(), part.getEditingDomain());
		addColumn("End Date", end, end);

		final NumericAttributeManipulator duration = new NumericAttributeManipulator(fp.getVesselEvent_Duration(), part.getEditingDomain());
		addColumn("Duration (days)", duration, duration);

		final SingleReferenceManipulator port = new SingleReferenceManipulator(fp.getVesselEvent_StartPort(), part.getPortProvider(), part.getEditingDomain());
		addColumn("Start Port", port, port);

		final MultipleReferenceManipulator vessels = new MultipleReferenceManipulator(fp.getVesselEvent_Vessels(), part.getEditingDomain(), part.getVesselProvider(), namedObjectName);

		addColumn("Vessels", vessels, vessels);

		final MultipleReferenceManipulator classes = new MultipleReferenceManipulator(fp.getVesselEvent_VesselClasses(), part.getEditingDomain(), part.getVesselClassProvider(), namedObjectName);

		addColumn("Classes", classes, classes);
	}

}
