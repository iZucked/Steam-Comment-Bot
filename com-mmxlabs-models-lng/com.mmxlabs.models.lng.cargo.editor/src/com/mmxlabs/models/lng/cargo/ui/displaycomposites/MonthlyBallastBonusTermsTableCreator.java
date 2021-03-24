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
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.PriceAttributeManipulator;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.EnumAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.YearMonthAttributeManipulator;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

public class MonthlyBallastBonusTermsTableCreator {	
	public static EObjectTableViewer createMonthlyBallastBonusTable(final Composite parent, final FormToolkit toolkit, final IDialogEditingContext dialogContext, final ICommandHandler commandHandler,
			final GenericCharterContract charterContract, final IStatusProvider statusProvider, final Runnable sizeChangedAction) {
		
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		
		final Label label = toolkit.createLabel(parent, "Ballast Bonus");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		final EObjectTableViewer eViewer = new EObjectTableViewer(parent, SWT.FULL_SELECTION);
		eViewer.setStatusProvider(statusProvider);
		eViewer.setAutoResizeable(false);
		eViewer.setSorter(null);

		eViewer.addTypicalColumn("Month", new YearMonthAttributeManipulator(CommercialPackage.eINSTANCE.getMonthlyBallastBonusTerm_Month(), 
				commandHandler) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				super.runSetCommand(object, value);

				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});
		
		eViewer.addTypicalColumn("Redelivery ports", new MultiplePortReferenceManipulator(CommercialPackage.eINSTANCE.getBallastBonusTerm_RedeliveryPorts(), sel.getReferenceValueProviderCache(),
				commandHandler, MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				super.runSetCommand(object, value);

				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		});

		eViewer.addTypicalColumn("Lump sum ($)", new PriceAttributeManipulator(
				CommercialPackage.eINSTANCE.getLumpSumTerm_PriceExpression(),
				commandHandler) {

			protected EAttribute getAttribute(Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return CommercialPackage.eINSTANCE.getNotionalJourneyTerm_LumpSumPriceExpression();
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
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.render(object);
				}
				else {
					return "-";
				}
			}
		});

		eViewer.addTypicalColumn("Speed", new NumericAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_Speed(), commandHandler) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.getValue(object);
				} else {
					return null;
				}
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.getComparable(object);
				}
				return -Integer.MAX_VALUE;
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});

		eViewer.addTypicalColumn("Ballast To", new EnumAttributeManipulator(CommercialPackage.eINSTANCE.getMonthlyBallastBonusTerm_BallastBonusTo(),
				commandHandler) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof MonthlyBallastBonusTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof MonthlyBallastBonusTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof MonthlyBallastBonusTerm) {
					return super.getValue(object);
				} else {
					return null;
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof MonthlyBallastBonusTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});

		eViewer.addTypicalColumn("% Fuel",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getMonthlyBallastBonusTerm_BallastBonusPctFuel(), commandHandler) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof MonthlyBallastBonusTerm) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof MonthlyBallastBonusTerm) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof MonthlyBallastBonusTerm) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof MonthlyBallastBonusTerm) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		
		eViewer.addTypicalColumn("Fuel Cost ($/MT)",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_FuelPriceExpression(), commandHandler) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		eViewer.addTypicalColumn("% Hire Cost",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getMonthlyBallastBonusTerm_BallastBonusPctCharter(), commandHandler) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof MonthlyBallastBonusTerm) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof MonthlyBallastBonusTerm) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof MonthlyBallastBonusTerm) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof MonthlyBallastBonusTerm) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});
		
		eViewer.addTypicalColumn("Hire Cost ($/day)",
				new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_HirePriceExpression(), commandHandler) {

					@Override
					public void runSetCommand(final Object object, final Object value) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							super.runSetCommand(object, value);

							dialogContext.getDialogController().validate();
							eViewer.refresh();
						}
					}

					@Override
					public boolean canEdit(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							return super.canEdit(object);
						} else {
							return false;
						}
					}

					@Override
					public Object getValue(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							return super.getValue(object);
						} else {
							return null;
						}
					}

					@Override
					public @Nullable String render(final Object object) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							return super.render(object);
						} else {
							return "-";
						}
					}

				});

		eViewer.addTypicalColumn("Include canal fees", new BooleanAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_IncludeCanal(), commandHandler) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.getValue(object);
				} else {
					return "";
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.render(object);
				} else {
					return "-";
				}
			}

		});


		eViewer.addTypicalColumn("Include canal time", new BooleanAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_IncludeCanalTime(), commandHandler) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					super.runSetCommand(object, value);

					dialogContext.getDialogController().validate();
					eViewer.refresh();
				}
			}

			@Override
			public boolean canEdit(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.canEdit(object);
				} else {
					return false;
				}
			}

			@Override
			public Object getValue(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.getValue(object);
				} else {
					return "";
				}
			}

			@Override
			public @Nullable String render(final Object object) {
				if (object instanceof NotionalJourneyBallastBonusTerm) {
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

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), CommercialPackage.eINSTANCE.getMonthlyBallastBonusContainer_Terms());
		eViewer.setInput(getBallastBonusContainer(commandHandler, charterContract));

		final GridData gridData = GridDataFactory.fillDefaults().grab(true, true).create();
		gridData.minimumHeight = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final Composite buttons = toolkit.createComposite(parent);
		final GridData bottomOneGridData = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).create();
		buttons.setLayoutData(bottomOneGridData);
		final GridLayout buttonLayout = new GridLayout(3, false);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		
		final Label labelRule = toolkit.createLabel(buttons, "Add rule: ");
		labelRule.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Button addMonthlyRuleBtn = toolkit.createButton(buttons, "Monthly", SWT.NONE);
		addMonthlyRuleBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addMonthlyRuleBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IBallastBonus ballastBonus = getBallastBonusContainer(commandHandler, charterContract);
				final MonthlyBallastBonusTerm newLine = CommercialFactory.eINSTANCE.createMonthlyBallastBonusTerm();
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), ballastBonus, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS, newLine),
						ballastBonus, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);
				eViewer.setSelection(new StructuredSelection(newLine));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});

		
		final Button remove = toolkit.createButton(buttons, "Remove", SWT.NONE);
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = eViewer.getSelection();
				if (sel.isEmpty()) {
					return;
				}
				if (sel instanceof IStructuredSelection) {
					final Object selection = ((IStructuredSelection) sel).getFirstElement();
					if (selection instanceof BallastBonusTerm) {
						
						final BallastBonusTerm ballastBonusTerm = (BallastBonusTerm) selection;
						final IBallastBonus ballastBonus = (IBallastBonus) ballastBonusTerm.eContainer();
						commandHandler.handleCommand(
								RemoveCommand.create(commandHandler.getEditingDomain(), ballastBonus, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS, selection),
								ballastBonus, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_CONTAINER__TERMS);
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
	
	private static IBallastBonus getBallastBonusContainer(final ICommandHandler commandHandler, final GenericCharterContract charterContract) {
		IBallastBonus ballastBonus = charterContract.getBallastBonusTerms();
		if (ballastBonus == null) {
			ballastBonus = CommercialFactory.eINSTANCE.createMonthlyBallastBonusContainer();
			commandHandler.handleCommand(
					AddCommand.create(commandHandler.getEditingDomain(), charterContract, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS, ballastBonus),
					charterContract, CommercialPackage.Literals.GENERIC_CHARTER_CONTRACT__BALLAST_BONUS_TERMS);
		}
		return ballastBonus;
	}
}
