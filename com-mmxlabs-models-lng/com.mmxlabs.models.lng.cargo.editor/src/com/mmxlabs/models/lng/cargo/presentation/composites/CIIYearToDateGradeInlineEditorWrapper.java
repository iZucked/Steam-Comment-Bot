package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ReadOnlyInlineEditorWrapper;

public class CIIYearToDateGradeInlineEditorWrapper extends ReadOnlyInlineEditorWrapper {
	
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;

	protected CIIYearToDateGradeInlineEditorWrapper(final @NonNull IInlineEditor wrapped) {
		super(wrapped);
	}
	
	@Override
	protected boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject scenario, EObject object, Collection<EObject> range) {
		
		this.dialogContext = dialogContext;
		this.scenario = scenario;
		this.range = range;

		enabled = true;
		if (object instanceof final CIIStartOptions options && options.eContainer() instanceof final VesselCharter vesselCharter) {
			
			if (wrapped instanceof CIIYearToDateGradeInlineEditor editor) {
				editor.setGrade("Hello Andrey! The vessel charter is now available for your disposal");
			}
		}
		super.display(dialogContext, scenario, object, range);
	}
}
