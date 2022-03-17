/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.ui.distanceeditor.DistanceEditorComposite;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class RouteTopLevelComposite extends DefaultTopLevelComposite {

	public RouteTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		if (object instanceof Route) {
			this.setLayout(new GridLayout(1, true));
			this.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));

			final Route route = (Route) object;
			final EClass eClass = object.eClass();
			final Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
			containerComposite.setLayout(new GridLayout(2, true));
			containerComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));
			ChildCompositeContainer childReferences = new ChildCompositeContainer();
			childCompositeContainers.add(childReferences);

			IDisplayComposite northEntranceComposite = null;
			IDisplayComposite southEntranceComposite = null;
			if (route.getRouteOption() != RouteOption.DIRECT) {
				northEntranceComposite = createChildArea(childReferences, root, route, containerComposite, PortPackage.Literals.ROUTE__NORTH_ENTRANCE, route.getNorthEntrance(), "Northern Entrance");
				southEntranceComposite = createChildArea(childReferences, root, route, containerComposite, PortPackage.Literals.ROUTE__SOUTH_ENTRANCE, route.getSouthEntrance(), "Southern Entrance");
			}

			if (route.getRouteOption() != RouteOption.DIRECT) {
				if (northEntranceComposite != null) {
					northEntranceComposite.display(dialogContext, root, route.getNorthEntrance(), range, dbc);
				}
				if (southEntranceComposite != null) {
					southEntranceComposite.display(dialogContext, root, route.getSouthEntrance(), range, dbc);
				}
			}

			final Group g = new Group(containerComposite, SWT.NONE);
			toolkit.adapt(g);

			g.setText(EditorUtils.unmangle(eClass.getName()));
			g.setLayout(new FillLayout());
			g.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			topLevel = new DistanceEditorComposite(g, SWT.NONE, toolkit);
			topLevel.setCommandHandler(commandHandler);
			topLevel.setEditorWrapper(editorWrapper);

			topLevel.display(dialogContext, root, object, range, dbc);

		} else {
			assert false;
			super.display(dialogContext, root, object, range, dbc);
		}
	}

	protected IDisplayComposite createChildArea(ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final EObject value, String groupName) {
		if (value != null) {
			final Group g2 = new Group(parent, SWT.NONE);
			toolkit.adapt(g2);
			g2.setText(groupName);
			g2.setLayout(new FillLayout());
			g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));

			final IDisplayComposite sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(value.eClass()).createSublevelComposite(g2, value.eClass(),
					dialogContext, toolkit);

			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);
			childCompositeContainer.childReferences.add(ref);
			childCompositeContainer.childComposites.add(sub);
			childCompositeContainer.childObjects.add(value);

			return sub;
		}
		return null;
	}
}
