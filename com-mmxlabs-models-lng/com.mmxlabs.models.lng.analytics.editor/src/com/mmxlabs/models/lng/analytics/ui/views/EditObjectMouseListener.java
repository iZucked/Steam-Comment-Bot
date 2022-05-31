/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;

/**
 * Mouse listener to handle the editing dialog for the
 * {@link OptionModellerView} viewer cells.
 * 
 * Note -- hardcoded column indicies!
 * 
 * @author Simon Goodall
 *
 */
public class EditObjectMouseListener extends MouseAdapter {

	private final @NonNull GridTreeViewer viewer;
	private final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	public EditObjectMouseListener(@NonNull final GridTreeViewer viewer, @NonNull final IScenarioEditingLocation scenarioEditingLocation) {
		this.viewer = viewer;
		this.scenarioEditingLocation = scenarioEditingLocation;
	}

	@Override
	public void mouseDoubleClick(final MouseEvent e) {

		final ViewerCell cell = viewer.getCell(new Point(e.x, e.y));
		if (cell != null) {
			final Object element = cell.getElement();
			EObject target = null;
			if (element instanceof final BaseCaseRow row) {
				if (cell.getColumnIndex() == 1) {
					target = row.getBuyOption();
				} else if (cell.getColumnIndex() == 2) {
					// Wiring
				} else if (cell.getColumnIndex() == 3) {
					target = row.getSellOption();
				} else if (cell.getColumnIndex() == 4) {
					target = row.getShipping();
				} else if (cell.getColumnIndex() == 0) {
					final Command c = SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__OPTIONISE, !row.isOptionise());
					DetailCompositeDialogUtil.editInlock(scenarioEditingLocation, () -> {
						final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
						commandStack.execute(c);
						return Window.OK;
					});
					return;
				} else if (cell.getColumnIndex() == 5) {
					if (row.getOptions() == null) {
						final BaseCaseRowOptions options = AnalyticsFactory.eINSTANCE.createBaseCaseRowOptions();
						final Command c = SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__OPTIONS, options);
						DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, options, c);
					} else {
						DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row.getOptions());
					}
					viewer.refresh(row);
					return;
				} else if (cell.getColumnIndex() == 6) {
					final Command c = SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__FREEZE, !row.isFreeze());
					DetailCompositeDialogUtil.editInlock(scenarioEditingLocation, () -> {
						final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
						commandStack.execute(c);
						return Window.OK;
					});
					return;
				}

				if (target != null) {
					DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, target);
				}

			} else if (element instanceof final PartialCaseRow row) {
				if (cell.getColumnIndex() == 0) {
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(row.getBuyOptions()));
				} else if (cell.getColumnIndex() == 1) {
					// Wiring
				} else if (cell.getColumnIndex() == 2) {
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(row.getSellOptions()));
				} else if (cell.getColumnIndex() == 3) {
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(row.getShipping()));
				} else if (cell.getColumnIndex() == 4) {
					if (row.getOptions() == null) {
						final PartialCaseRowOptions options = AnalyticsFactory.eINSTANCE.createPartialCaseRowOptions();
						final Command c = SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__OPTIONS, options);
						DetailCompositeDialogUtil.editNewObjectWithUndoOnCancel(scenarioEditingLocation, options, c);
					} else {
						DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, row.getOptions());
					}
					viewer.refresh(row);
				}
			} else if (element instanceof final BreakEvenAnalysisRow row) {
				// Do not edit
			} else if (element instanceof final EObject eObj) {
				DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, eObj);
			}
		}
	}
}
