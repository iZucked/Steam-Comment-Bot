package com.mmxlabs.models.lng.ui.actions;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Superclass for actions which will do something to a scenario based on a selection
 * 
 * @author hinton
 *
 */
public abstract class ScenarioModifyingAction extends LockableAction implements ISelectionChangedListener {
	public ScenarioModifyingAction() {
		super();
	}

	public ScenarioModifyingAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public ScenarioModifyingAction(String text, int style) {
		super(text, style);
	}

	public ScenarioModifyingAction(String text) {
		super(text);
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		setEnabled(isApplicableToSelection(event.getSelection()));
		lastSelection = event.getSelection();
	}
	
	protected ISelection getLastSelection() {
		return lastSelection;
	}

	private ISelection lastSelection;
	
	protected abstract boolean isApplicableToSelection(ISelection selection);
	
	protected EditingDomain getEditingDomain(final IWorkbenchWindow wbw) {
		if (wbw == null || wbw.getActivePage() == null) {
			return null;
		}
		IWorkbenchPart part = wbw.getActivePage().getActivePart();
		return (part instanceof IEditingDomainProvider ? ((IEditingDomainProvider) part)
				.getEditingDomain() : null);
	}
}