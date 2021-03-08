/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortSingleReferenceManipulatorExtension;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.PriceAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

public class RepositioningFeeTermsTableCreator {
	public static EObjectTableViewer createRepositioningFeeTable(final Composite parent, final FormToolkit toolkit, final IDialogEditingContext dialogContext, final ICommandHandler commandHandler,
			final GenericCharterContract charterContract, final IStatusProvider statusProvider, final Runnable sizeChangedAction) {
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
		// ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
				ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		eViewer.addTypicalColumn("Notional origin port", new TextualPortSingleReferenceManipulatorExtension(CommercialPackage.eINSTANCE.getRepositioningFeeTerm_OriginPort(), sel.getReferenceValueProviderCache(),
				sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
			
			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.render(object);
				} else if (object instanceof LumpSumRepositioningFeeTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}
		});

		eViewer.addTypicalColumn("Lump sum ($)", new PriceAttributeManipulator(
				CommercialPackage.eINSTANCE.getNotionalJourneyTerm_LumpSumPriceExpression(),
				sel.getEditingDomain()) {

			protected EAttribute getAttribute(Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return CommercialPackage.eINSTANCE.getNotionalJourneyTerm_LumpSumPriceExpression();
				} else if (object instanceof LumpSumRepositioningFeeTerm) {
					return CommercialPackage.eINSTANCE.getLumpSumTerm_PriceExpression();
				}
				return super.getAttribute(object);
			}
			
			@Override
			public void runSetCommand(final Object object, final String value) {
				super.runSetCommand(object, value);
				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.render(object);
				} else if (object instanceof LumpSumRepositioningFeeTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}
		});

		eViewer.addTypicalColumn("Speed", new NumericAttributeManipulator(
				CommercialPackage.eINSTANCE.getNotionalJourneyTerm_Speed(), sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.getValue(object);
				} else {
					return null;
				}
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.getComparable(object);
				}
				return -Integer.MAX_VALUE;
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});

		eViewer.addTypicalColumn("Fuel Cost ($/MT)",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_FuelPriceExpression(), sel.getEditingDomain()) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		eViewer.addTypicalColumn("Hire Cost ($/day)",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_HirePriceExpression(), sel.getEditingDomain()) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof OriginPortRepositioningFeeTerm) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		eViewer.addTypicalColumn("Include canal fees", new BooleanAttributeManipulator(
				CommercialPackage.eINSTANCE.getNotionalJourneyTerm_IncludeCanal(), sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.getValue(object);
				} else {
					return "";
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});


		eViewer.addTypicalColumn("Include canal time", new BooleanAttributeManipulator(
				CommercialPackage.eINSTANCE.getNotionalJourneyTerm_IncludeCanalTime(), sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.getValue(object);
				} else {
					return "";
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof OriginPortRepositioningFeeTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}
		});

		final WrappingColumnHeaderRenderer chr = new WrappingColumnHeaderRenderer();
		chr.setWordWrap(true);
		for (final GridColumn gridColumn : eViewer.getGrid().getColumns()) {
			eViewer.getSortingSupport().removeSortableColumn(gridColumn);
			gridColumn.setSort(SWT.NONE);
			gridColumn.setHeaderRenderer(chr);
			gridColumn.setHeaderWordWrap(true);
			gridColumn.setWidth(85);
		}
		eViewer.getSortingSupport().clearColumnSortOrder();
		eViewer.getGrid().recalculateHeader();

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), CommercialPackage.eINSTANCE.getSimpleRepositioningFeeContainer_Terms());
		eViewer.setInput(getRepositioningFeeContainer(commandHandler, charterContract));

		final GridData gridData = GridDataFactory.fillDefaults().grab(true, true).create();
		gridData.minimumHeight = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final Composite buttons = toolkit.createComposite(parent);

		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false));
		final GridLayout buttonLayout = new GridLayout(3, false);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;

		final Button addLumpSumRule = toolkit.createButton(buttons, "Add lump sume rule", SWT.NONE);
		addLumpSumRule.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addLumpSumRule.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IRepositioningFee repositioningFee = getRepositioningFeeContainer(commandHandler, charterContract);
				
				final LumpSumRepositioningFeeTerm newLine = CommercialFactory.eINSTANCE.createLumpSumRepositioningFeeTerm();
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), repositioningFee, CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS, newLine),
						repositioningFee, CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS);
				eViewer.setSelection(new StructuredSelection(newLine));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});
		
		final Button addOriginPortRule = toolkit.createButton(buttons, "Add origin port rule", SWT.NONE);
		addOriginPortRule.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addOriginPortRule.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IRepositioningFee repositioningFee = getRepositioningFeeContainer(commandHandler, charterContract);
				final OriginPortRepositioningFeeTerm newLine = CommercialFactory.eINSTANCE.createOriginPortRepositioningFeeTerm();
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), repositioningFee, CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS, newLine),
						repositioningFee, CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS);
				eViewer.setSelection(new StructuredSelection(newLine));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});
		
		final Button remove = toolkit.createButton(buttons, "Remove", SWT.NONE);
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = eViewer.getSelection();
				if (sel.isEmpty()) {
					return;
				}
				if (sel instanceof IStructuredSelection) {
					final Object selection = ((IStructuredSelection) sel).getFirstElement();
					if (selection instanceof RepositioningFeeTerm) {
						RepositioningFeeTerm repositioningFeeTerm = (RepositioningFeeTerm) selection;
						IRepositioningFee repositioningFee = (IRepositioningFee) repositioningFeeTerm.eContainer();
						commandHandler.handleCommand(
								RemoveCommand.create(commandHandler.getEditingDomain(), repositioningFee, CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS, selection),
								repositioningFee, CommercialPackage.Literals.SIMPLE_REPOSITIONING_FEE_CONTAINER__TERMS);
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
	
	private static IRepositioningFee getRepositioningFeeContainer(final ICommandHandler commandHandler, final GenericCharterContract charterContract) {
		if (charterContract.getRepositioningFeeTerms() == null) {
			charterContract.setRepositioningFeeTerms(CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer());
		}
		return charterContract.getRepositioningFeeTerms();
	}
}
