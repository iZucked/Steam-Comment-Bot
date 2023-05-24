/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.PreferredFormulaeWrapper;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.ExpressionAnnotationConstants;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.autocomplete.AutoCompleteHelper;
import com.mmxlabs.models.ui.editors.autocomplete.IMMXContentProposalProvider;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class PreferredFormulaeTableCreator {

	public static EObjectTableViewer createPrefferedFormulaeTable(final Composite parent, final FormToolkit toolkit, final IDialogEditingContext dialogContext, final ICommandHandler commandHandler,
			final EObject input, final EReference reference, final IStatusProvider statusProvider, final Runnable sizeChangedAction) {
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();

		final Label label = toolkit.createLabel(parent, "Preferred formulae");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

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
		// ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
				ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		eViewer.addTypicalColumn("       Name       ",
				new StringAttributeManipulator(MMXCorePackage.Literals.NAMED_OBJECT__NAME, commandHandler) {
			@Override
			protected CellEditor createCellEditor(final Composite c, final Object object) {
				final ComboBoxViewerCellEditor editor = new ComboBoxViewerCellEditor(c, SWT.FLAT | SWT.BORDER) {

					private IMMXContentProposalProvider proposalHelper;

					@Override
					protected Control createControl(Composite parent) {
						Control control = super.createControl(parent);
						this.proposalHelper = AutoCompleteHelper.createControlProposalAdapter(control, ExpressionAnnotationConstants.TYPE_COMMODITY);
						EditingDomain editingDomain = commandHandler.getEditingDomain();
						for (Resource r : editingDomain.getResourceSet().getResources()) {
							for (EObject o : r.getContents()) {
								if (o instanceof MMXRootObject mmxo) {
									this.proposalHelper.setRootObject(mmxo);
									if (getViewer() != null) {
										setContentProvider(new ArrayContentProvider());
										getViewer().setInput(getInputs(mmxo));
									}
								}
							}
						}
						return control;
					}

					/**
					 * Override doGetValue to also return the custom string if a valid selection has
					 * not been made.
					 */
					@Override
					protected Object doGetValue() {
						final Object value = super.doGetValue();
						if (value == null) {
							return ((CCombo) getControl()).getText();
						}
						return value;
					}
					
					@Override
					protected void doSetValue(Object value) {
						if (value instanceof NamedObject no) {
							super.doSetValue(no.getName());
						} else {
							super.doSetValue(value);
						}
					}
				};
				editor.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(final Object element) {

						// Is the element missing?
						if (element == null || "".equals(element)) {
							return "";
						}

						if (element instanceof NamedObject namedObject) {
							return namedObject.getName();
						}
						return super.getText(element);
					}
				});
				editor.setValue(object);
				return editor;
			}
		});
		

		final WrappingColumnHeaderRenderer chr = new WrappingColumnHeaderRenderer();
		chr.setWordWrap(true);
		for (final GridColumn gridColumn : eViewer.getGrid().getColumns()) {
			eViewer.getSortingSupport().removeSortableColumn(gridColumn);
			gridColumn.setSort(SWT.NONE);
			gridColumn.setHeaderRenderer(chr);
			gridColumn.setHeaderWordWrap(true);
			//gridColumn.setWidth(85);
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

		final Button addPPB = toolkit.createButton(bottomOne, "Add", SWT.NONE);
		addPPB.setText("");
		addPPB.setImage(CommonImages.getImage(IconPaths.Plus, IconMode.Enabled));
		addPPB.setLayoutData(buttonsGridData);

		addPPB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {

				final PreferredFormulaeWrapper newPFW = CommercialFactory.eINSTANCE.createPreferredFormulaeWrapper();
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), input,  //
						reference, newPFW)//
						, input,  reference);
				eViewer.setSelection(new StructuredSelection(newPFW));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
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
				if (sel instanceof IStructuredSelection) {
					final Object selection = ((IStructuredSelection) sel).getFirstElement();
					if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
						commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), input, //
								reference, ss.getFirstElement()), //
								input, reference);
					}
				}
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});

		eViewer.refresh();
		sizeChangedAction.run();
		return eViewer;
	}
	
	@NonNull
	private static List<String> getInputs(final @NonNull MMXRootObject mmxo){
		if (mmxo instanceof LNGScenarioModel scenarioModel // 
				&& scenarioModel.getReferenceModel() != null //
				&& scenarioModel.getReferenceModel().getPricingModel() != null) {
			final List<CommodityCurve> pb = scenarioModel.getReferenceModel().getPricingModel().getFormulaeCurves();
			if (pb != null) {
				return pb.stream().map(NamedObject::getName).toList();
			}

		}
		return Collections.emptyList();
	}
}
