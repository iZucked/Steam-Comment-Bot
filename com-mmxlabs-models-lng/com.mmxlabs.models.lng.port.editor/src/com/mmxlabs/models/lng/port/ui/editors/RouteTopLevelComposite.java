/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.ui.distanceeditor.DistanceEditorComposite;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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

			final Route route = (Route) object;
			final EClass eClass = object.eClass();
			final Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
			containerComposite.setLayout(new GridLayout(2, true));

			IDisplayComposite entryAComposite = null;
			IDisplayComposite entryBComposite = null;
			if (route.isCanal()) {
				entryAComposite = createChildArea(root, route, containerComposite, PortPackage.Literals.ROUTE__ENTRY_A, route.getEntryA());
				entryBComposite = createChildArea(root, route, containerComposite, PortPackage.Literals.ROUTE__ENTRY_B, route.getEntryB());
			}

			if (route.isCanal()) {
				if (entryAComposite != null) {
					entryAComposite.display(dialogContext, root, route.getEntryA(), range, dbc);
				}
				if (entryBComposite != null) {
					entryBComposite.display(dialogContext, root, route.getEntryB(), range, dbc);
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

			this.setLayout(new GridLayout(1, true));
		} else {
			assert false;
			super.display(dialogContext, root, object, range, dbc);
		}
	}
}
