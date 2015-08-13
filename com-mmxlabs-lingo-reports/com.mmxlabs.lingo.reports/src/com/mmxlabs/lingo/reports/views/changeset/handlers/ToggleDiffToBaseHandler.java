
package com.mmxlabs.lingo.reports.views.changeset.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ToggleDiffToBaseHandler {

	@Inject
	private IEventBroker eventBroker;

	// @Inject
	// private ESelectionService selectionService;

	// @CanExecute
	// public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_PART) MPart part) {
	// if (part == null) {
	// return false;
	// }
	// Object selection = selectionService.getSelection(part.getElementId());
	// if (selection instanceof IStructuredSelection) {
	// IStructuredSelection ss = (IStructuredSelection) selection;
	// if (ss.size() == 1) {
	// Object o = ss.getFirstElement();
	// if (o instanceof ScenarioInstance) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	@Execute
	public void execute() {
		eventBroker.post("toggle-diff-to-base", null);
	}

}