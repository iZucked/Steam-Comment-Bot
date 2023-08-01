package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.manipulators.IdentityInlineEditorWrapper;

public class CIIYearToDateGradeInlineEditorWrapper extends IdentityInlineEditorWrapper {

	protected CIIYearToDateGradeInlineEditorWrapper(final @NonNull IInlineEditor wrapped) {
		super(wrapped);
	}
	
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
		super.display(dialogContext, scenario, object, range);
	}
}
