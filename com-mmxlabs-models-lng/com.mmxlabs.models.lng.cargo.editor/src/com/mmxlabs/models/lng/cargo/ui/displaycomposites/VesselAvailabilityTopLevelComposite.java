/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
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
public class VesselAvailabilityTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the heel editors
	 */
	private Composite right;

	public VesselAvailabilityTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		g.setText(EditorUtils.unmangle(eClass.getName()));
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		// // START CUSTOM SECTION
		// Initialise right composite
		right = toolkit.createComposite(this);
		createChildComposites(root, object, eClass, right);
		// Single column
		right.setLayout(new GridLayout(1, true));
		right.setLayoutData(new GridData(GridData.FILL_BOTH));
		right.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		// // END CUSTOM SECTION

		topLevel.display(dialogContext, root, object, range, dbc);

		final Iterator<IDisplayComposite> children = childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		// Overrides default layout factory so we get two columns columns
		this.setLayout(new GridLayout(2, true));
	}
}
