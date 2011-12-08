/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import scenario.presentation.LngEditorPlugin;

/**
 * Action for replicating a template cargo over a range of times
 * 
 * @author hinton
 * 
 */
public class ReplicateCargoAction extends Action implements ISelectionChangedListener {
	final ReplicateCargoHandler handler = new ReplicateCargoHandler();

	public ReplicateCargoAction() {
		setImageDescriptor(LngEditorPlugin.Implementation.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin().getSymbolicName(), "/icons/repeat.gif"));
		setToolTipText("Generate cargoes by replicating the selected cargoes a given number of times at a specified interval");
		setText("Replicate cargoes");
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
