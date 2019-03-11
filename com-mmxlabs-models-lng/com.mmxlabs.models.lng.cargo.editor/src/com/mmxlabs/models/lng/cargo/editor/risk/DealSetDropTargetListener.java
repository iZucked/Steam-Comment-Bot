/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import java.util.Iterator;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class DealSetDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private @NonNull final GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	public DealSetDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(e -> menuHelper.dispose());
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {
	}

	@Override
	public void drop(final DropTargetEvent event) {
		if (event.item == null) return;
		if (event.item.getData() == null) return;
		final DealSet ds;
		if (event.item.getData() instanceof DealSet) {
			ds = (DealSet) event.item.getData();
		} else if (event.item.getData() instanceof Slot || event.item.getData() instanceof PaperDeal) {
			GridItem item = (GridItem) event.item;
			ds = (DealSet) item.getParentItem().getData();
		} else {
			return;
		}
		
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (!selection.isEmpty()) {
				final CompoundCommand cmd = new CompoundCommand();
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object o = itr.next();
					if (o instanceof Slot) {
						final Slot slot = (Slot) o;
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ds, CargoPackage.Literals.DEAL_SET__SLOTS, slot));
					} else if (o instanceof PaperDeal) {
						final PaperDeal pd = (PaperDeal) o;
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), ds, CargoPackage.Literals.DEAL_SET__PAPER_DEALS, pd));
					}
				}
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				}
				viewer.refresh();
			}
		}
	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection == null) return;
			if (!selection.isEmpty()) {
				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final Object o = itr.next();
					if (o instanceof Slot || o instanceof PaperDeal) {
						// Found a valid source in the selection.
						event.operations = DND.DROP_MOVE;
						return;
					}
				}
			}
		}
		event.operations = DND.DROP_NONE;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
	}
}
