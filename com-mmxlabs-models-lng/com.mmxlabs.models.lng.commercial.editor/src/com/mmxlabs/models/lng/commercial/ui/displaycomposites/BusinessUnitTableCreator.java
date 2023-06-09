/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class BusinessUnitTableCreator {

	public static EObjectTableViewer createBusinessUnitsTable(final Composite parent, final FormToolkit toolkit, final IDialogEditingContext dialogContext, final ICommandHandler commandHandler,
			final EObject input, final EReference reference, final IStatusProvider statusProvider, final Runnable sizeChangedAction) {
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		final EObjectTableViewer eViewer = new EObjectTableViewer(parent, SWT.FULL_SELECTION);
		eViewer.setStatusProvider(statusProvider);
		eViewer.setAutoResizeable(false);
		eViewer.setSorter(null);

		final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(eViewer) {
			@Override
			protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};

		GridViewerEditor.create(eViewer, actSupport, ColumnViewerEditor.KEYBOARD_ACTIVATION | GridViewerEditor.SELECTION_FOLLOWS_EDITOR |
				ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		eViewer.addTypicalColumn("       Name       ",
				new StringAttributeManipulator(MMXCorePackage.Literals.NAMED_OBJECT__NAME, commandHandler));

		eViewer.addTypicalColumn("  Description  ",
				new StringAttributeManipulator(CommercialPackage.Literals.BUSINESS_UNIT__DESCRIPTION, commandHandler));

		eViewer.addTypicalColumn("Default",
				new BooleanFlagAttributeManipulator(CommercialPackage.Literals.BUSINESS_UNIT__DEFAULT, commandHandler));

		final WrappingColumnHeaderRenderer chr = new WrappingColumnHeaderRenderer();
		chr.setWordWrap(true);
		for (final GridColumn gridColumn : eViewer.getGrid().getColumns()) {
			eViewer.getSortingSupport().removeSortableColumn(gridColumn);
			gridColumn.setSort(SWT.NONE);
			gridColumn.setHeaderRenderer(chr);
			gridColumn.setHeaderWordWrap(true);
			gridColumn.setWidth(180);
		}
		eViewer.getSortingSupport().clearColumnSortOrder();
		eViewer.getGrid().recalculateHeader();

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), reference);
		eViewer.setInput(input);

		final GridData gridData = GridDataFactory.fillDefaults().grab(true, true).create();
		gridData.minimumHeight = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final Composite bottomOne = toolkit.createComposite(parent);
		final GridData bottomOneGridData = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).create();
		bottomOne.setLayoutData(bottomOneGridData);
		final GridLayout bottomOneLayout = new GridLayout(2, false);
		bottomOne.setLayout(bottomOneLayout);
		bottomOneLayout.marginHeight = 0;
		bottomOneLayout.marginWidth = 0;

		final GridData buttonsGridData = GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).grab(false, false).create();

		final Button addBU = toolkit.createButton(bottomOne, "Add", SWT.NONE);
		addBU.setText("");
		addBU.setImage(CommonImages.getImage(IconPaths.Plus, IconMode.Enabled));
		addBU.setLayoutData(buttonsGridData);

		addBU.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (input instanceof final BaseLegalEntity entity) {
					final BusinessUnit newBusinessUnit = CommercialFactory.eINSTANCE.createBusinessUnit();
					commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), entity, //
							CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS, newBusinessUnit), //
							entity,  CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);

					eViewer.setSelection(new StructuredSelection(newBusinessUnit));
					eViewer.refresh();
					RunnerHelper.asyncExec(sizeChangedAction);
				}
			}
		});

		final Button remove = toolkit.createButton(bottomOne, "Remove", SWT.NONE);
		remove.setText("");
		remove.setImage(CommonImages.getImage(IconPaths.Delete, IconMode.Enabled));
		remove.setLayoutData(buttonsGridData);
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = eViewer.getSelection();
				if (sel.isEmpty()) {
					return;
				}
				if (input instanceof final BaseLegalEntity entity) {
					if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
						Command c = RemoveCommand.create(commandHandler.getEditingDomain(), entity, //
								CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS, ss.toList());
						commandHandler.handleCommand(c, entity, CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);

					}
				}
				//}
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});

		eViewer.refresh();
		sizeChangedAction.run();
		return eViewer;
	}
}
