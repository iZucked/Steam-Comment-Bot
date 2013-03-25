/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
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
public class CargoTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;
	/**
	 * {@link IDisplayComposite} to contain elements for the bottom of the editor
	 */
	protected IDisplayComposite bottomLevel = null;

	public CargoTopLevelComposite(final Composite parent, final int style, final IScenarioEditingLocation location) {
		super(parent, style, location);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);

		String groupName = EditorUtils.unmangle(eClass.getName());
		if (object instanceof Cargo) {
			CargoType cargoType = ((Cargo) object).getCargoType();
			groupName += " Type: " + cargoType.getName();
		}

		g.setText(groupName);
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = new CargoDetailComposite(g, SWT.NONE, true);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		// Initialise middle composite
		middle = new Composite(this, SWT.NONE);
		// We know there is only the load and discharge slot, so two columns
		middle.setLayout(new GridLayout(2, true));

		createChildComposites(root, object, eClass, middle);

		// Additional Group for the bottom section
		final Group g2 = new Group(this, SWT.NONE);
		// g2.setText(EditorUtils.unmangle(eClass.getName()));
		g2.setLayout(new FillLayout());
		g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		// Create the directly rather than go through the registry. True indicates this is the bottom section.
		bottomLevel = new CargoDetailComposite(g2, SWT.NONE, false);
		bottomLevel.setCommandHandler(commandHandler);
		bottomLevel.setEditorWrapper(editorWrapper);

		topLevel.display(location, root, object, range);
		bottomLevel.display(location, root, object, range);

		final Iterator<EReference> refs = childReferences.iterator();
		final Iterator<IDisplayComposite> children = childComposites.iterator();

		while (refs.hasNext()) {
			children.next().display(location, root, (EObject) object.eGet(refs.next()), range);
		}

		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(1, true));
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) || ref == CargoPackage.eINSTANCE.getCargo_Slots();
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		bottomLevel.displayValidationStatus(status);
		super.displayValidationStatus(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		if (bottomLevel != null) {
			bottomLevel.setEditorWrapper(wrapper);
		}
		super.setEditorWrapper(wrapper);
	}
}
