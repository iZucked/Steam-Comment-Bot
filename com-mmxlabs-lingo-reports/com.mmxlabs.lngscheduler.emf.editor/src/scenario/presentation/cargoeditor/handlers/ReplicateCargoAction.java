/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import scenario.presentation.LngEditorPlugin;
import scenario.presentation.cargoeditor.LockableAction;

/**
 * Action for replicating a template cargo over a range of times
 * 
 * @author hinton
 * 
 */
public class ReplicateCargoAction extends LockableAction implements ISelectionChangedListener {
	final ReplicateCargoHandler handler = new ReplicateCargoHandler();

	public ReplicateCargoAction() {
		setImageDescriptor(LngEditorPlugin.Implementation.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin().getSymbolicName(), "/icons/repeat.gif"));
		setToolTipText("Offset cargoes by a given time interval, or repeat cargoes with a given time interval");
		setText("Replicate/offset cargoes");
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	@Override
	public void run() {
		try {
			handler.execute(null);
		} catch (final ExecutionException e) {
			// display exception
		}
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			setEnabled(ss.size() > 0);
		} else {
			setEnabled(false);
		}
	}
}
