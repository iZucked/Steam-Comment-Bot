package scenario.presentation.cargoeditor.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import scenario.presentation.LngEditorPlugin;

public class SwapDischargeSlotsAction extends Action implements
		ISelectionChangedListener {

	final SwapDischargeHandler handler = new SwapDischargeHandler();

	public SwapDischargeSlotsAction() {
		setImageDescriptor(LngEditorPlugin.Implementation
				.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin()
						.getSymbolicName(), "/icons/swap.gif"));
		setToolTipText("Swap discharge slots");
		setText("Swap discharge slots");
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
			setEnabled(ss.size() == 2);
		} else {
			setEnabled(false);
		}
	}
}
