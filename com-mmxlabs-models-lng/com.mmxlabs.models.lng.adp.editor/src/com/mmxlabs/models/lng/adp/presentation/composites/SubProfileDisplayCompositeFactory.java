/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.CustomSubProfileAttributes;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

public class SubProfileDisplayCompositeFactory implements IDisplayCompositeFactory {
	final IDisplayCompositeFactory defaultFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(EcorePackage.eINSTANCE.getEObject());

	@Override
	public IDisplayComposite createToplevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		// return defaultFactory.createToplevelComposite(composite, eClass, dialogContext, toolkit);
		return new DefaultTopLevelComposite(parent, SWT.NONE, dialogContext, toolkit) {
			protected IDisplayComposite createChildArea(MMXRootObject root, EObject object, Composite parent, org.eclipse.emf.ecore.EReference ref, EObject value) {
				if (value != null) {
					final Group g2 = new Group(parent, SWT.NONE);
					toolkit.adapt(g2);
					if (object instanceof CustomSubProfileAttributes) {
						g2.setText("Options");
					} else {
						g2.setText(EditorUtils.unmangle(ref.getName()));
					}
					g2.setLayout(new FillLayout());
					g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));

					final IDisplayComposite sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(value.eClass()).createSublevelComposite(g2, value.eClass(),
							dialogContext, toolkit);

					sub.setCommandHandler(commandHandler);
					sub.setEditorWrapper(editorWrapper);
					childReferences.add(ref);
					childComposites.add(sub);
					childObjects.add(value);

					return sub;
				}
				return null;
			}
		};
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new SubProfileDetailComposite(composite, SWT.NONE, toolkit);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		return defaultFactory.getExternalEditingRange(root, value);
	}
}
