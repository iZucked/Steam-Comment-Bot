package com.mmxlabs.lingo.reports.diff.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.BaseSelectionListenerAction;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class AbstractDiffAction extends BaseSelectionListenerAction {

	protected EObject target;

	protected AbstractDiffAction(final String text) {
		super(text);
	}

	@Override
	protected boolean updateSelection(final IStructuredSelection selection) {

		final Object firstElement = selection.getFirstElement();
		if (firstElement instanceof UserGroup || firstElement instanceof CycleGroup || firstElement instanceof Row) {
			target = (EObject) firstElement;
			return true;
		}
		return false;
	}
}
