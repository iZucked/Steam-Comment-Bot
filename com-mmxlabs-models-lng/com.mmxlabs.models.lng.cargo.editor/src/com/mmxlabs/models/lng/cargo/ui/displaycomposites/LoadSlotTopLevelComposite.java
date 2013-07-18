/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the LoadSlot editor to customise string name
 * 
 * @author Simon Goodall
 * 
 */
public class LoadSlotTopLevelComposite extends DefaultTopLevelComposite {

	public LoadSlotTopLevelComposite(final Composite parent, final int style, final IScenarioEditingLocation location) {
		super(parent, style, location);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);
		String groupName = EditorUtils.unmangle(eClass.getName());
		if (object instanceof LoadSlot) {
			LoadSlot loadSlot = (LoadSlot) object;

			if (loadSlot.getTransferFrom() != null) {
				groupName = "STS: Load";
			} else {
				groupName = "Load";
			}
		}

		g.setText(groupName);
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, location);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		createChildComposites(root, object, eClass, this, toolkit);

		topLevel.display(location, root, object, range, dbc, toolkit);
		final Iterator<IDisplayComposite> children = childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(location, root, childObjectsItr.next(), range, dbc, toolkit);
		}

		setLayout(layoutProvider.createTopLevelLayout(root, object, childComposites.size() + 1));
	}
}
