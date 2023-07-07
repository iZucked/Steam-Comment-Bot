/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
	 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
	 * All rights reserved.
	 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

/**
 * Abstract parent for sandbox drop listeners. Holds shared checks on locked status. Adds a tooltip to the draged item.
 * 
 * @author Simon Goodall
 *
 */
public abstract class AbstractDropTargetListener implements DropTargetListener {

	protected final @NonNull IScenarioEditingLocation scenarioEditingLocation;
	protected OptionAnalysisModel optionAnalysisModel;
	protected final @NonNull LocalMenuHelper menuHelper;
	protected final @NonNull GridTreeViewer viewer;

	private int x = 0; // Last drag x pos
	private int y = 0; // Last drag y pos
	private Label lbl; // Tooltip label while dragging

	protected AbstractDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, @NonNull final GridTreeViewer viewer) {
		this.scenarioEditingLocation = scenarioEditingLocation;
		this.optionAnalysisModel = optionAnalysisModel;
		this.viewer = viewer;

		menuHelper = new LocalMenuHelper(scenarioEditingLocation.getShell());
		viewer.getControl().addDisposeListener(e -> menuHelper.dispose());

		lbl = new Label(viewer.getControl().getParent(), SWT.BORDER);
		lbl.setText("");
		lbl.setBackground(lbl.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		lbl.setLayoutData(GridDataFactory.swtDefaults().exclude(true).create());
		// Make sure we are on top of the table viewer
		lbl.moveAbove(viewer.getControl());
		// Hide until we are ready to show
		lbl.setVisible(false);

	}

	protected AbstractDropTargetListener(final @NonNull IScenarioEditingLocation scenarioEditingLocation, @NonNull final GridTreeViewer viewer) {
		this(scenarioEditingLocation, null, viewer);
	}

	@Override
	public final void dropAccept(final DropTargetEvent event) {
		lbl.setVisible(false);
		if (scenarioEditingLocation.isLocked()) {
			event.detail = DND.DROP_NONE;
			return;
		}
		final OptionAnalysisModel optionAnalysisModel = this.optionAnalysisModel;
		if (optionAnalysisModel == null) {
			event.detail = DND.DROP_NONE;
			return;
		}
		doDropAccept(event);
	}

	protected abstract void doDropAccept(final DropTargetEvent event);

	@Override
	public final void drop(final DropTargetEvent event) {
		lbl.setVisible(false);

		if (scenarioEditingLocation.isLocked()) {
			event.detail = DND.DROP_NONE;
			return;
		}

		if (this.optionAnalysisModel == null) {
			event.detail = DND.DROP_NONE;
			return;
		}

		doDrop(event);
	}

	protected abstract void doDrop(final DropTargetEvent event);

	protected final void setDragMessage(String message) {
		if (message == null || message.isBlank()) {
			if (this.lbl.isVisible()) {
				this.lbl.setVisible(false);
			}
			return;
		}

		if (!Objects.equals(this.lbl.getText(), message)) {
			this.lbl.setText(message);
			this.lbl.pack();

		}
		if (!this.lbl.isVisible()) {
			this.lbl.setVisible(true);
		}
	}

	@Override
	public final void dragOver(final DropTargetEvent event) {
		if (event.x != x || event.y != y) {
			x = event.x;
			y = event.y;
			this.lbl.setLocation(viewer.getControl().toControl(event.x + 0, event.y + 50));
		}
		doDragOver(event);
	}

	protected abstract void doDragOver(final DropTargetEvent event);

	@Override
	public void dragOperationChanged(final DropTargetEvent event) {

	}

	@Override
	public final void dragLeave(final DropTargetEvent event) {
		this.lbl.setVisible(false);
	}

	@Override
	public void dragEnter(final DropTargetEvent event) {
	}

	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setOptionAnalysisModel(final OptionAnalysisModel optionAnalysisModel) {
		this.optionAnalysisModel = optionAnalysisModel;
	}
}
