/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

/**
 * An action for opening the @link {@link WiringDialog}
 * 
 * @author Tom Hinton
 *
 */
public class WiringDialogAction extends Action implements
		ISelectionChangedListener {
	public WiringDialogAction() {
		super("Redirection Wizard");
	}

	@Override
	public void run() {
		super.run();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub

	}

}
