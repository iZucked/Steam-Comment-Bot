/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ui.contingencyeditor.ContingencyMatrixEditorComposite;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class ContingencyMatrixLevelComposite extends DefaultTopLevelComposite {

	public ContingencyMatrixLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		if (object instanceof ContingencyMatrix) {
			this.setLayout(new GridLayout(1, true));
			this.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));

			final ContingencyMatrix matrix = (ContingencyMatrix) object;
			final EClass eClass = object.eClass();
			final Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
			containerComposite.setLayout(new GridLayout(2, true));
			containerComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));

			final Group g = new Group(containerComposite, SWT.NONE);
			toolkit.adapt(g);

			g.setText(EditorUtils.unmangle(eClass.getName()));
			g.setLayout(new FillLayout());
			g.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			topLevel = new ContingencyMatrixEditorComposite(g, SWT.NONE, toolkit);
			topLevel.setCommandHandler(commandHandler);
			topLevel.setEditorWrapper(editorWrapper);

			topLevel.display(dialogContext, root, object, range, dbc);

		} else {
			assert false;
			super.display(dialogContext, root, object, range, dbc);
		}
	}
 
}
