/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
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

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class OptionsTreeViewerDropTargetListener implements DropTargetListener {

	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	private @NonNull final GridTreeViewer viewer;

	private final LocalMenuHelper menuHelper;

	public OptionsTreeViewerDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.viewer = viewer;
		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				menuHelper.dispose();
			}
		});
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {

	}

	@Override
	public void drop(final DropTargetEvent event) {
		if (event.item instanceof GridItem) {
			final GridItem gridItem = (GridItem) event.item;
			final Object d = gridItem.getData();
			OptionAnalysisModel existing = null;
			if (d instanceof OptionAnalysisModel) {
				existing = (OptionAnalysisModel) d;
			}
			if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
				final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
				if (selection.size() >= 1) {
					List<BuyOption> buys = new LinkedList<>();
					List<SellOption> sells = new LinkedList<>();
					for (Object selectionObject : selection.toArray()) {
						if (selectionObject instanceof BuyOption) {
							buys.add((BuyOption) selectionObject);
						} else if (selectionObject instanceof SellOption) {
							sells.add((SellOption) selectionObject);
						}
					}
					if (existing != null) {
						if (!buys.isEmpty()) {
							processBuys(existing, buys);
						}
						if (!sells.isEmpty()) {
							processSells(existing, sells);
						}
					}
				}
			}
		}
	}

	private void processBuys(OptionAnalysisModel existing, List<BuyOption> buys) {
		for (BuyOption buy : buys) {
			existing.getBuys().add(EcoreUtil.copy(buy));
		}
	}

	private void processSells(OptionAnalysisModel existing, List<SellOption> sells) {
		for (SellOption sell : sells) {
			existing.getSells().add(EcoreUtil.copy(sell));
		}
	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
			final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
			if (selection.size() > 0) {
				final Object o = selection.getFirstElement();
				if (o instanceof BuyOption || o instanceof SellOption) {
					event.operations = DND.DROP_MOVE;
					return;
				}
			}
		}
		event.operations = DND.DROP_NONE;
	}

	@Override
	public void dragOperationChanged(final DropTargetEvent event) {

	}

	@Override
	public void dragLeave(final DropTargetEvent event) {

	}

	@Override
	public void dragEnter(final DropTargetEvent event) {

	}
}
