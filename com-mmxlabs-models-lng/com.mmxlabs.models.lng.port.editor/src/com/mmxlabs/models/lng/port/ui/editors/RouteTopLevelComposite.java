/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.ui.distanceeditor.DistanceEditorComposite;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the {@link VesselAvailability} editor to keep the start and end heel options in one column. Note this assumes start and end heel are the only contained children.
 * 
 * @author Simon Goodall
 * 
 */
public class RouteTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the heel editors
	 */
	private Composite right;
	// private IDisplayComposite ballastBonusComposite;

	public RouteTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		if (object instanceof Route) {

			Route route = (Route) object;
			final EClass eClass = object.eClass();
			Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
			containerComposite.setLayout(new GridLayout(2, true));

			IDisplayComposite entryAComposite = createChildArea(root, route, containerComposite, PortPackage.Literals.ROUTE__ENTRY_A, route.getEntryA());
			IDisplayComposite entryBComposite = createChildArea(root, route, containerComposite, PortPackage.Literals.ROUTE__ENTRY_B, route.getEntryB());

			// // START CUSTOM SECTION
			//// // Initialise right composite
			//// right = toolkit.createComposite(containerComposite);
			//// createChildComposites(root, object, eClass, right);
			// // Single column
			// final GridLayout layout = GridLayoutFactory.swtDefaults() //
			// .numColumns(1) //
			// .equalWidth(true) //
			// .margins(0, 0) //
			// .extendedMargins(0, 0, 0, 0) //
			// .create();
			// right.setLayout(layout);
			// right.setLayoutData(new GridData(GridData.FILL_BOTH));
			// right.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));


			final Iterator<IDisplayComposite> children = childComposites.iterator();
			final Iterator<EObject> childObjectsItr = childObjects.iterator();

			while (childObjectsItr.hasNext()) {
				EObject next = childObjectsItr.next();
				if (!(next instanceof BallastBonusContract)) {
					children.next().display(dialogContext, root, next, range, dbc);
				}
			}
			if (entryAComposite != null) {
				entryAComposite.display(dialogContext, root, route.getEntryA(), range, dbc);
			}
			if (entryBComposite != null) {
				entryAComposite.display(dialogContext, root, route.getEntryB(), range, dbc);
			}
			// createChildArea(root, route, containerComposite, ref, (EObject) o);
			//
			 final Group g = new Group(containerComposite, SWT.NONE);
			 toolkit.adapt(g);
			//
			 g.setText(EditorUtils.unmangle(eClass.getName()));
			 g.setLayout(new FillLayout());
			 g.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			 g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			
			 // topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
			 topLevel = new DistanceEditorComposite(g, SWT.NONE, toolkit);
			 topLevel.setCommandHandler(commandHandler);
			 topLevel.setEditorWrapper(editorWrapper);
			// Overrides default layout factory so we get two columns columns
//			containerComposite.setLayout(new GridLayout(2, true));
//
			 topLevel.display(dialogContext, root, object, range, dbc);
//			Composite bottomComposite = toolkit.createComposite(this, SWT.NONE);
//			bottomComposite.setLayout(new FillLayout());
//			bottomComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//			final Group g2 = new Group(bottomComposite, SWT.NONE);
//			toolkit.adapt(g2);
//			g2.setText("Distances");
//			g2.setLayout(new GridLayout());
//			g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
//			//
			// ballastBonusComposite = new BallastBonusContractDetailComposite(g2, SWT.NONE, toolkit, () -> {
			// if (!RouteTopLevelComposite.this.isDisposed()) {
			// RouteTopLevelComposite.this.layout(true, true);
			// }
			// });
			// ballastBonusComposite.setCommandHandler(commandHandler);
			// ballastBonusComposite.display(dialogContext, root, object, range, dbc);

			this.setLayout(new GridLayout(1, true));
		} else {
			assert false;
			super.display(dialogContext, root, object, range, dbc);
		}
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		// ballastBonusComposite.displayValidationStatus(status);
	}

	// @Override
	// protected boolean shouldDisplay(final EReference ref) {
	//// return ref.isContainment() && !ref.isMany() && ref != CargoPackage.eINSTANCE.getVesselAvailability_BallastBonusContract();
	// }

}
