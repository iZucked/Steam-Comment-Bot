/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the LoadSlot editor to customise string name
 * 
 * @author Simon Goodall
 * 
 */
public class DischargeSlotTopLevelComposite extends DefaultTopLevelComposite {

	public DischargeSlotTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		String groupName = EditorUtils.unmangle(eClass.getName());
		if (object instanceof DischargeSlot dischargeSlot) {

			if (dischargeSlot.getTransferTo() != null) {
				groupName = "STS: Discharge";
			} else if (dischargeSlot.isFOBSale()) {
				groupName = "FOB Sale";
			} else {
				groupName = "DES Sale";
			}
		}

		g.setText(groupName);
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);

		int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, this);
		setLayout(layoutProvider.createTopLevelLayout(root, object, numChildren + 1));
	}
}
