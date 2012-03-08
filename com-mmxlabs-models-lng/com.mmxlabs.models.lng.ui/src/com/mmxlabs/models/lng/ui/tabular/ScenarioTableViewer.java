package com.mmxlabs.models.lng.ui.tabular;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;

public class ScenarioTableViewer extends EObjectTableViewer  {
	private JointModelEditorPart editorPart;

	public ScenarioTableViewer(final Composite parent, final int style, final JointModelEditorPart editorPart) {
		super(parent, style);
		this.editorPart = editorPart;
	}

	@Override
	public void refresh() {
		if (editorPart.isSaving()) return;
		super.refresh();
	}
}
