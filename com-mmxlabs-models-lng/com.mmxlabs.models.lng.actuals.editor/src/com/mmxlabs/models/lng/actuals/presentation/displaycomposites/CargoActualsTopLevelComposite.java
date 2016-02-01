/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.presentation.displaycomposites;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the cargo editor; because the slots are not contained in the cargo any more we need some special-case editing behaviour.
 * 
 * We also have to signify to containers that we will be editing some non-contained objects, so that they can be duplicated.
 * 
 * This {@link DefaultTopLevelComposite} implementation also splits the top level attributes into two groups rather than the default single group.
 * 
 * @author hinton, Simon Goodall
 * 
 */
public class CargoActualsTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;

	public CargoActualsTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		toolkit.adapt(this);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		final String groupName = EditorUtils.unmangle(eClass.getName());

		g.setText(groupName);
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.adapt(g);

		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);

		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		// Initialise middle composite
		middle = toolkit.createComposite(this);

		createChildComposites(root, object, eClass, middle);
		// We know there are n slots, so n columns
		middle.setLayout(new GridLayout(childObjects.size(), true));
		middle.setLayoutData(new GridData(GridData.FILL_BOTH));
		middle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel.display(dialogContext, root, object, range, dbc);

		final Iterator<IDisplayComposite> children = childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(1, true));
	}

	@Override
	protected void createChildArea(final MMXRootObject root, final EObject object, final Composite parent, final EReference ref, final EObject value) {
		if (value != null) {
			final Group g2 = new Group(parent, SWT.NONE);
			if (value instanceof SlotActuals) {
				if (value instanceof LoadActuals) {
					g2.setText("Load Actuals");
				} else if (value instanceof DischargeActuals) {
					g2.setText("Discharge Actuals");
				} else {
					final String groupName = EditorUtils.unmangle(value.eClass().getName());
					g2.setText(groupName);
				}
			} else {
				g2.setText(EditorUtils.unmangle(ref.getName()));
			}
			g2.setLayout(new FillLayout());
			g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));
			g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			toolkit.adapt(g2);

			final IDisplayComposite sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(value.eClass())
					.createSublevelComposite(g2, value.eClass(), dialogContext, toolkit);

			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);
			childReferences.add(ref);
			childComposites.add(sub);
			childObjects.add(value);
		}
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return ref == ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS || super.shouldDisplay(ref);
	}
}
