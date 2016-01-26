/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.mmxcore;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;

public class NamedObjectComponentHelper extends BaseComponentHelper {

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, MMXCorePackage.eINSTANCE.getNamedObject());
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(displayedClass, MMXCorePackage.eINSTANCE.getNamedObject_Name()));
	}
}
