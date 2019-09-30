package com.mmxlabs.lingo.reports.modelbased;

import java.util.function.UnaryOperator;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

public final class WrappedSelectionProvider implements ISelectionProvider {
	private final ListenerList<ISelectionChangedListener> selectionChangedListeners = new ListenerList<>();
	private ISelection selection = StructuredSelection.EMPTY;
	private final UnaryOperator<ISelection> adapterFunction;

	public WrappedSelectionProvider(@Nullable final UnaryOperator<ISelection> adapterFunction) {
		this.adapterFunction = adapterFunction;

	}

	@Override
	public void setSelection(final ISelection sel) {
		this.selection = adapterFunction.apply(sel);
		fireSelectionChanged(new SelectionChangedEvent(this, selection));

	}

	@Override
	public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	@Override
	public ISelection getSelection() {
		return this.selection;
	}

	@Override
	public void addSelectionChangedListener(final ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);

	}

	/**
	 * Notifies any selection changed listeners that the viewer's selection has changed. Only listeners registered at the time this method is called are notified.
	 *
	 * @param event
	 *            a selection changed event
	 *
	 * @see ISelectionChangedListener#selectionChanged
	 */
	protected void fireSelectionChanged(final SelectionChangedEvent event) {
		for (final ISelectionChangedListener l : selectionChangedListeners) {
			SafeRunnable.run(new SafeRunnable() {
				@Override
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}
}