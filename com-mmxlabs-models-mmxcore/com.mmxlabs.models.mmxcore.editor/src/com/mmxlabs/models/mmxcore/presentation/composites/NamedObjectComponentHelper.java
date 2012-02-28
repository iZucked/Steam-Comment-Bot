/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.presentation.composites;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

public class NamedObjectComponentHelper implements IComponentHelper {
	@Override
	public void addEditorsToComposite(IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, MMXCorePackage.eINSTANCE.getNamedObject());
	}

	@Override
	public void addEditorsToComposite(IInlineEditorContainer detailComposite,
			EClass displayedClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(displayedClass, MMXCorePackage.eINSTANCE.getNamedObject_Name()));
	}
}
