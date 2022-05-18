package com.mmxlabs.models.lng.adp.presentation.customisation;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;

@NonNullByDefault
public interface IAdpToolbarCustomiser {

	void customiseToolbar(Composite toolbarComposite, ADPEditorData editorData, IWorkbenchPage page);

	int columnsRequired();
}
