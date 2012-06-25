package com.mmxlabs.models.lng.ui.tabular;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioTableViewer extends EObjectTableViewer  {
	private IScenarioEditingLocation editorPart;

	public ScenarioTableViewer(final Composite parent, final int style, final IScenarioEditingLocation part) {
		super(parent, style);
		this.editorPart = part;
	}

	@Override
	protected boolean claimValidationLock() {
		ScenarioLock lock = editorPart.getScenarioInstance().getLock(ScenarioLock.VALIDATION);
		return lock.claim();
	}

	@Override
	protected void releaseValidationLock() {
		ScenarioLock lock = editorPart.getScenarioInstance().getLock(ScenarioLock.VALIDATION);
		lock.release();
	}
}
