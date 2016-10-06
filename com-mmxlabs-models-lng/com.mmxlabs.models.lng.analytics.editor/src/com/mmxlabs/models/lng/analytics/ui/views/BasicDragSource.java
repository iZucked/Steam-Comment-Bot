package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;

public class BasicDragSource implements DragSourceListener {

	private final @NonNull GridTreeViewer viewer;
	private IStructuredSelection selection;

	public BasicDragSource(@NonNull final GridTreeViewer viewer) {
		this.viewer = viewer;

	}

	@Override
	public void dragStart(final DragSourceEvent event) {
		// Grab selection now as the viewer selection can change (especially if it is also a drop target)
		selection = (IStructuredSelection) viewer.getSelection();

	}

	@Override
	public void dragSetData(final DragSourceEvent event) {

		final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
		if (transfer.isSupportedType(event.dataType)) {
			transfer.setSelection(selection);
			transfer.setSelectionSetTime(event.time & 0xFFFF);
		}
	}

	@Override
	public void dragFinished(final DragSourceEvent event) {
		// Clear reference
		selection = null;
	}

}
