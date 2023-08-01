package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ReadOnlyInlineEditorWrapper;

public class CIIYearToDateGradeInlineEditorWrapper extends ReadOnlyInlineEditorWrapper {

	protected CIIYearToDateGradeInlineEditorWrapper(final @NonNull IInlineEditor wrapped) {
		super(wrapped);
	}
	
	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject scenario, EObject object, Collection<EObject> range) {
		super.display(dialogContext, scenario, object, range);
	}
}
