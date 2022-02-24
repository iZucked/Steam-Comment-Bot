/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;

public class DistributionModelTopLevelCompositeFactory extends DefaultDisplayCompositeFactory {

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass, IDialogEditingContext dialogContext, FormToolkit toolkit) {
		final DefaultDetailComposite s = (DefaultDetailComposite) super.createSublevelComposite(parent, eClass, dialogContext, toolkit);
		s.setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {

			@Override
			public Layout createDetailLayout(@NonNull MMXRootObject root, EObject value) {
				return GridLayoutFactory.swtDefaults().numColumns(3).create();
			}

			@Override
			public Object createEditorLayoutData(final MMXRootObject root, final EObject value, final IInlineEditor editor, final Control control) {
				return GridDataFactory.swtDefaults().span(2, 1).grab(true, false).create();
			}
		});
		return s;
	}

}
