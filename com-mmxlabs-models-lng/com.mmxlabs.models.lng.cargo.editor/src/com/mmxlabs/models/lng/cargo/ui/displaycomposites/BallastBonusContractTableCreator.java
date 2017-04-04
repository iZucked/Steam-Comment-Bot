/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
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

import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

public class BallastBonusContractTableCreator {
	public static EObjectTableViewer createBallastBonusTable(final Composite parent, final FormToolkit toolkit, final IDialogEditingContext dialogContext, final ICommandHandler commandHandler,
			final RuleBasedBallastBonusContract ruleBasedBallastBonusContract, final IStatusProvider statusProvider, final Runnable sizeChangedAction) {
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		final EObjectTableViewer eViewer = new EObjectTableViewer(parent, SWT.FULL_SELECTION);
		eViewer.setStatusProvider(statusProvider);
		eViewer.setAutoResizeable(false);
		eViewer.setSorter(null);

		eViewer.addTypicalColumn("Redelivery ports", new MultipleReferenceManipulator(CommercialPackage.eINSTANCE.getBallastBonusContractLine_RedeliveryPorts(), sel.getReferenceValueProviderCache(),
				sel.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				super.runSetCommand(object, value);

				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});

		eViewer.addTypicalColumn("Lump sum ($)", new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getLumpSumBallastBonusContractLine_PriceExpression(), sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof LumpSumBallastBonusContractLine) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof LumpSumBallastBonusContractLine) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof LumpSumBallastBonusContractLine) {
					return super.render(object);
				} else {
					return "-";
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof LumpSumBallastBonusContractLine) {
					return super.getValue(object);
				} else {
					return null;
				}
			}

		});

		eViewer.addTypicalColumn("Speed", new NumericAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyBallastBonusContractLine_Speed(), sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.getValue(object);
				} else {
					return null;
				}
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.getComparable(object);
				}
				return -Integer.MAX_VALUE;
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});

		eViewer.addTypicalColumn("Notional return ports", new MultipleReferenceManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyBallastBonusContractLine_ReturnPorts(),
				sel.getReferenceValueProviderCache(), sel.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.getValue(object);
				} else {
					return null;
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});

		eViewer.addTypicalColumn("Fuel Cost ($/MT)",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyBallastBonusContractLine_FuelPriceExpression(), sel.getEditingDomain()) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		eViewer.addTypicalColumn("Hire Cost ($/day)",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyBallastBonusContractLine_HirePriceExpression(), sel.getEditingDomain()) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusContractLine) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		eViewer.addTypicalColumn("Include canal fees", new BooleanAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyBallastBonusContractLine_IncludeCanal(), sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.getValue(object);
				} else {
					return "";
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});

		eViewer.addTypicalColumn("Type", new BasicAttributeManipulator(null, sel.getEditingDomain()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
			}

			@Override
			public boolean canEdit(final Object object) {
				return false;
			}

			@Override
			public Object getValue(final Object object) {
				return null;
			}

			@Override
			public boolean isValueUnset(final Object object) {
				return true;
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusContractLine) {
					return "Journey";
				} else if (object instanceof LumpSumBallastBonusContractLine) {
					return "Lump sum";
				} else {
					return "";
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

		eViewer.init(sel.getAdapterFactory(), sel.getEditingDomain().getCommandStack(), CommercialPackage.eINSTANCE.getRuleBasedBallastBonusContract_Rules());

		eViewer.setInput(ruleBasedBallastBonusContract);

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

		final Button addLumpSum = toolkit.createButton(buttons, "Add lump sum rule", SWT.NONE);
		addLumpSum.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addLumpSum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final LumpSumBallastBonusContractLine newLine = CommercialFactory.eINSTANCE.createLumpSumBallastBonusContractLine();
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), ruleBasedBallastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES, newLine),
						ruleBasedBallastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
				eViewer.setSelection(new StructuredSelection(newLine));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});

		final Button addNotional = toolkit.createButton(buttons, "Add notional journey rule", SWT.NONE);
		addNotional.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addNotional.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final NotionalJourneyBallastBonusContractLine newLine = CommercialFactory.eINSTANCE.createNotionalJourneyBallastBonusContractLine();
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), ruleBasedBallastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES, newLine),
						ruleBasedBallastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
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
					if (selection instanceof BallastBonusContractLine) {
						commandHandler.handleCommand(
								RemoveCommand.create(commandHandler.getEditingDomain(), ruleBasedBallastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES, selection),
								ruleBasedBallastBonusContract, CommercialPackage.Literals.RULE_BASED_BALLAST_BONUS_CONTRACT__RULES);
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
}
