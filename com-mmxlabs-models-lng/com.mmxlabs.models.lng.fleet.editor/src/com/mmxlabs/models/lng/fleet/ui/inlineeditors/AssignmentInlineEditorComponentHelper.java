/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * A component helper which adds an assignment editor to the cargo view.
 * 
 * @author hinton
 * 
 */
public class AssignmentInlineEditorComponentHelper extends BaseComponentHelper {
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		final IInlineEditor assignmentEditor = new AssignmentInlineEditor();
		detailComposite.addInlineEditor(assignmentEditor);
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {
		addEditorsToComposite(detailComposite);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		return super.getExternalEditingRange(root, value);
	}

	@Override
	public int getDisplayPriority() {
		return 1;
	}
}
