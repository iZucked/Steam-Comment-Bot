package com.mmxlabs.models.lng.ui.tabular;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;

public class ScenarioTableViewer extends EObjectTableViewer  {
	private IScenarioEditingLocation editorPart;

	public ScenarioTableViewer(final Composite parent, final int style, final IScenarioEditingLocation part) {
		super(parent, style);
		this.editorPart = part;
	}

//
//	@Override
//	public void refresh() {
//		if (editorPart.isSaving()) return;
//		super.refresh();
//	}
}
