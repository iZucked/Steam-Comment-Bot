/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.pricing.ui.autocomplete.PriceAttributeManipulator;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.renderers.WrappingColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.rcp.common.RunnerHelper;

public class BallastBonusTermsTableCreator {
	public static EObjectTableViewer createBallastBonusTable(final Composite parent, final FormToolkit toolkit, final IDialogEditingContext dialogContext, final ICommandHandler commandHandler,
			final GenericCharterContract charterContract, final IStatusProvider statusProvider, final Runnable sizeChangedAction) {
		final IScenarioEditingLocation sel = dialogContext.getScenarioEditingLocation();
		
		final EObjectTableViewer eViewer = new EObjectTableViewer(parent, SWT.FULL_SELECTION);
		eViewer.setStatusProvider(statusProvider);
		eViewer.setAutoResizeable(false);
		eViewer.setSorter(null);
		
		eViewer.addTypicalColumn("Redelivery ports", new MultiplePortReferenceManipulator(CommercialPackage.eINSTANCE.getBallastBonusTerm_RedeliveryPorts(), sel.getReferenceValueProviderCache(),
				commandHandler, MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

			@Override
			public void runSetCommand(final Object object, final Object value) {
				super.runSetCommand(object, value);

				dialogContext.getDialogController().validate();
				eViewer.refresh();
			}
		}).getColumn().setHeaderTooltip("The end at ports used to match against this line. A blank entry means any port. Rules are checked in order and the first match is applied.");

		eViewer.addTypicalColumn("Lump sum ($)", new PriceAttributeManipulator(
				CommercialPackage.eINSTANCE.getLumpSumTerm_PriceExpression(),
				commandHandler) {

			protected EAttribute getAttribute(Object object) {
				if (object instanceof LumpSumBallastBonusTerm) {
					return CommercialPackage.eINSTANCE.getLumpSumTerm_PriceExpression();
				}
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
				if (object instanceof LumpSumBallastBonusTerm) {
					return super.render(object);
				} 
				else if (object instanceof NotionalJourneyBallastBonusTerm) {
					return super.render(object);
				}
				else {
					return "-";
				}
			}
		}).getColumn().setHeaderTooltip("A lumpsum value to be added to the total cost.");

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

		eViewer.addTypicalColumn("Notional return ports", new MultipleReferenceManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyBallastBonusTerm_ReturnPorts(),
				sel.getReferenceValueProviderCache(), commandHandler, MMXCorePackage.eINSTANCE.getNamedObject_Name()) {

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

		}).getColumn().setHeaderTooltip("The set of ports used for the notional ballast calcuations. The port resulting in the lowest cost will be used.");

		var fuelCostCol = eViewer.addTypicalColumn("Fuel Cost ($/MT)",
				new FuelCostManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_FuelPriceExpression(), commandHandler) {

					@Override
					public void doSetValue(final Object object, final Object value) {
						if (object instanceof NotionalJourneyBallastBonusTerm) {
							super.doSetValue(object, value);

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

				})
				;
		fuelCostCol.getColumn().setHeaderTooltip("Voyage fuel consumption is based on consumption at the notional speed. This can be bunkers only at the given price or base-fuel equivalent of LNG at the last sales price.");

		eViewer.addTypicalColumn("Hire Cost ($/day)",
				new PriceAttributeManipulator(CommercialPackage.eINSTANCE.getNotionalJourneyTerm_HirePriceExpression(), commandHandler) {

					@Override
					public void runSetCommand(final Object object, final String value) {
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

		}).getColumn().setHeaderTooltip("Include canal transit fees");

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
		}).getColumn().setHeaderTooltip("Include canal transit time in hire cost and fuel consumptions. Panama waiting days are not included.");
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

		eViewer.init(sel.getAdapterFactory(), sel.getModelReference(), CommercialPackage.eINSTANCE.getSimpleBallastBonusContainer_Terms());
		eViewer.setInput(getBallastBonusContainer(commandHandler, charterContract));

		final GridData gridData = GridDataFactory.fillDefaults().grab(true, true).create();
		gridData.minimumHeight = 150;
		final Grid table = eViewer.getGrid();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final Composite bottomOne = toolkit.createComposite(parent);
		final GridData bottomOneGridData = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).create();
		bottomOne.setLayoutData(bottomOneGridData);
		final GridLayout bottomOneLayout = new GridLayout(4, false);
		bottomOne.setLayout(bottomOneLayout);
		bottomOneLayout.marginHeight = 0;
		bottomOneLayout.marginWidth = 0;
		
		final Label labelRule = toolkit.createLabel(bottomOne, "Add rule: ");
		labelRule.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Button addLumpSum = toolkit.createButton(bottomOne, "Lump sum", SWT.NONE);
		addLumpSum.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addLumpSum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IBallastBonus ballastBonus = getBallastBonusContainer(commandHandler, charterContract);
				
				final LumpSumBallastBonusTerm newLine = CommercialFactory.eINSTANCE.createLumpSumBallastBonusTerm();
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), ballastBonus, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS, newLine),
						ballastBonus, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS);
				eViewer.setSelection(new StructuredSelection(newLine));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});

		final Button addNotional = toolkit.createButton(bottomOne, "Notional journey", SWT.NONE);
		addNotional.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		addNotional.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				IBallastBonus ballastBonus = getBallastBonusContainer(commandHandler, charterContract);
				final NotionalJourneyBallastBonusTerm newLine = CommercialFactory.eINSTANCE.createNotionalJourneyBallastBonusTerm();
				
				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), ballastBonus, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS, newLine),
						ballastBonus, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS);
				eViewer.setSelection(new StructuredSelection(newLine));
				eViewer.refresh();
				RunnerHelper.asyncExec(sizeChangedAction);
			}
		});
		
		final Button remove = toolkit.createButton(bottomOne, "Remove", SWT.NONE);
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
								RemoveCommand.create(commandHandler.getEditingDomain(), ballastBonus, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS, selection),
								ballastBonus, CommercialPackage.Literals.SIMPLE_BALLAST_BONUS_CONTAINER__TERMS);
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
		if (charterContract.getBallastBonusTerms() == null) {
			charterContract.setBallastBonusTerms(CommercialFactory.eINSTANCE.createSimpleBallastBonusContainer());
		}
		return charterContract.getBallastBonusTerms();
	}
}
