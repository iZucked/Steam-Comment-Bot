package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionRule;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.WhatIfEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ResultDetailsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.RuleDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.BaseCaseContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.OptionsViewerContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.PartialCaseContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsViewerContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.RulesViewerContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselAndClassContentProvider;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class OptionModellerView extends ScenarioInstanceView {

	private GridTreeViewer baseCaseViewer;
	private GridTreeViewer partialCaseViewer;
	private GridTreeViewer buyOptionsViewer;
	private GridTreeViewer sellOptionsViewer;
	private GridTreeViewer rulesViewer;
	private GridTreeViewer resultsViewer;
	private GridTreeViewer vesselViewer;
	private OptionAnalysisModel model;

	@Override
	public void createPartControl(final Composite parent) {
		model = createDemoModel1();

		parent.setLayout(new FillLayout());

		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(4, false));

		{
			final Composite buyComposite = new Composite(mainComposite, SWT.NONE);
			buyComposite.setLayoutData(GridDataFactory.fillDefaults().create());
			buyComposite.setLayout(new GridLayout(1, true));

			createBuyOptionsViewer(buyComposite);
			// GridDataFactory.generate(buyOptionsViewer.getGrid(), 1, 1);
			buyOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			// buyOptionsViewer.addFilter(new ViewerFilter() {
			//
			// @Override
			// public boolean select(Viewer viewer, Object parentElement, Object element) {
			// if (element instanceof BuyOption) {
			// BuyOption buyOption = (BuyOption) element;
			// if (buyOption.getBaseCase().isEmpty() || buyOption.getPartialCase().isEmpty()) {
			// return true;
			// }
			// }
			//
			// return false;
			// }
			// });
			{
				final Button addBuy = new Button(buyComposite, SWT.PUSH);
				addBuy.setText("Add existing");
				addBuy.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addBuy.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final BuyOption row = AnalyticsFactory.eINSTANCE.createBuyReference();
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, row), model,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
						buyOptionsViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}
			{
				final Button addBuy = new Button(buyComposite, SWT.PUSH);
				addBuy.setText("Add option");
				addBuy.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addBuy.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final BuyOption row = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, row), model,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
						buyOptionsViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}
			{
				final Button addBuy = new Button(buyComposite, SWT.PUSH);
				addBuy.setText("Add market");
				addBuy.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addBuy.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final BuyMarket row = AnalyticsFactory.eINSTANCE.createBuyMarket();
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, row), model,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
						buyOptionsViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}

			// GridDataFactory.generate(addBuy, 1, 1);
			hookDragSource(buyOptionsViewer);

		}

		final Composite composite = new Composite(mainComposite, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		{

			wrapInExpandable(composite, mainComposite, "Base Case", p -> createBaseCaseViewer(p), expandableCompo -> {
				final Button textClient = new Button(expandableCompo, SWT.PUSH);
				textClient.setText("+");
				expandableCompo.setTextClient(textClient);
				textClient.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub
						final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
						row.setShipping(AnalyticsFactory.eINSTANCE.createRoundTripShippingOption());
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row), model.getBaseCase(),
								AnalyticsPackage.Literals.BASE_CASE__BASE_CASE);
						baseCaseViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			});

			baseCaseProftLabel = new Label(composite, SWT.NONE);
			GridDataFactory.generate(baseCaseProftLabel, 2, 1);
			baseCaseProftLabel.setText("Base P&&L: <not calculated>");

			final Button baseCaseCalculator = new Button(composite, SWT.FLAT);
			baseCaseCalculator.setText("Calc.");
			baseCaseCalculator.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					// TODO Auto-generated method stub
					BaseCaseEvaluator.evaluate(OptionModellerView.this, model, model.getBaseCase());
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
					// TODO Auto-generated method stub

				}
			});

			hookOpenEditor(baseCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			baseCaseViewer.addDropSupport(DND.DROP_MOVE, types, new DropTargetListener() {

				@Override
				public void dropAccept(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void drop(final DropTargetEvent event) {
					// TODO Auto-generated method stub
					if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
						final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
						if (selection.size() == 1) {
							final Object o = selection.getFirstElement();

							BaseCaseRow existing = null;
							if (event.item instanceof GridItem) {
								final GridItem gridItem = (GridItem) event.item;
								final Point cell = baseCaseViewer.getGrid().getCell(baseCaseViewer.getGrid().toControl(event.x, event.y));

								final Object d = gridItem.getData();
								if (d instanceof BaseCaseRow) {
									existing = (BaseCaseRow) d;
								}
							}

							if (o instanceof BuyOption) {
								final BuyOption buyOption = (BuyOption) o;
								if (existing != null) {
									getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption), existing,
											AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
								} else {
									final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
									final CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
									cmd.append(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION, buyOption));
									getDefaultCommandHandler().handleCommand(cmd, model, null);

								}
								baseCaseViewer.refresh();
								mainComposite.pack();
							} else if (o instanceof SellOption) {
								final SellOption sellOption = (SellOption) o;
								if (existing != null) {
									getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption),
											existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
								} else {
									final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
									final CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
									cmd.append(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION, sellOption));
									getDefaultCommandHandler().handleCommand(cmd, model, null);
								}
								baseCaseViewer.refresh();
								mainComposite.pack();
							} else if (o instanceof Vessel) {
								NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
								opt.setNominatedVessel((Vessel) o);
								if (existing != null) {
									getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
											AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
								} else {
									final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
									final CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(getEditingDomain(), model.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
									cmd.append(SetCommand.create(getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt));
									getDefaultCommandHandler().handleCommand(cmd, model.getBaseCase(), null);
								}
								baseCaseViewer.refresh();
								mainComposite.pack();
							} else if (o instanceof VesselClass) {
								RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
//								opt.setNominatedVessel((VesselClass) o);
								if (existing != null) {
									getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt), existing,
											AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
								} else {
									final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
									final CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(getEditingDomain(), model.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row));
									cmd.append(SetCommand.create(getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt));
									getDefaultCommandHandler().handleCommand(cmd, model.getBaseCase(), null);
								}
								baseCaseViewer.refresh();
								mainComposite.pack();
							}
						}
					}
				}

				@Override
				public void dragOver(final DropTargetEvent event) {
					if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
						final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
						if (selection.size() == 1) {
							final Object o = selection.getFirstElement();
							if (o instanceof BuyOption || o instanceof SellOption) {
								event.operations = DND.DROP_MOVE;
								return;
							}
							if (o instanceof Vessel || o instanceof VesselClass) {
								event.operations = DND.DROP_MOVE;
								return;
							}
						}
					}
					event.operations = DND.DROP_NONE;
				}

				@Override
				public void dragOperationChanged(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void dragLeave(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void dragEnter(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}
			});
		}

		{
			wrapInExpandable(composite, mainComposite, "What if?", p -> createPartialCaseViewer(p), expandableCompo -> {
				final Button textClient = new Button(expandableCompo, SWT.PUSH);
				textClient.setText("+");
				expandableCompo.setTextClient(textClient);
				textClient.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						row.setShipping(AnalyticsFactory.eINSTANCE.createRoundTripShippingOption());
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row),
								model.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE);
						partialCaseViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			});
			//

			hookOpenEditor(partialCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			partialCaseViewer.addDropSupport(DND.DROP_MOVE, types, new DropTargetListener() {

				@Override
				public void dropAccept(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void drop(final DropTargetEvent event) {
					// TODO Auto-generated method stub
					if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)) {
						final IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().nativeToJava(event.currentDataType);
						Object o = null;
						Collection<EObject> collection = null;
						if (selection.size() > 1) {
							final Object f = selection.getFirstElement();

							if (f instanceof BuyOption) {
								collection = new LinkedList<>();
								// final SellGroup g = AnalyticsFactory.eINSTANCE.createSellGroup();
								final Iterator<?> itr = selection.iterator();
								while (itr.hasNext()) {
									final BuyOption next = (BuyOption) itr.next();
									if (o == null) {
										o = next;
									}
									collection.add(next);
								}
							} else if (f instanceof SellOption) {
								collection = new LinkedList<>();
								// final SellGroup g = AnalyticsFactory.eINSTANCE.createSellGroup();
								final Iterator<?> itr = selection.iterator();
								while (itr.hasNext()) {
									final SellOption next = (SellOption) itr.next();
									if (o == null) {
										o = next;
									}
									collection.add(next);
								}
							}

						} else if (selection.size() == 1) {
							o = selection.getFirstElement();
							collection = Collections.singletonList((EObject) o);
						}

						if (o != null) {

							PartialCaseRow existing = null;
							if (event.item instanceof GridItem) {
								final GridItem gridItem = (GridItem) event.item;
								final Object d = gridItem.getData();
								if (d instanceof PartialCaseRow) {
									existing = (PartialCaseRow) d;
								}
							}

							if (o instanceof BuyOption) {
								final BuyOption buyOption = (BuyOption) o;
								if (existing != null) {
									getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection),
											existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS);
								} else {
									final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
									final CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
									cmd.append(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS, collection));
									getDefaultCommandHandler().handleCommand(cmd, model, null);

								}
								partialCaseViewer.refresh();
								mainComposite.pack();
							} else if (o instanceof SellOption) {
								final SellOption sellOption = (SellOption) o;
								if (existing != null) {
									getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, collection),
											existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS);
								} else {
									final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
									final CompoundCommand cmd = new CompoundCommand();
									cmd.append(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row));
									cmd.append(SetCommand.create(getEditingDomain(), existing, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS, collection));
									getDefaultCommandHandler().handleCommand(cmd, model, null);
								}
								partialCaseViewer.refresh();
								mainComposite.pack();
							}
						}
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
					// TODO Auto-generated method stub

				}

				@Override
				public void dragLeave(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}

				@Override
				public void dragEnter(final DropTargetEvent event) {
					// TODO Auto-generated method stub

				}
			});
		}

		createRulesViewer(composite);
		GridDataFactory.generate(rulesViewer.getGrid(), 2, 1);

		final Button generateButton = new Button(composite, SWT.PUSH);
		generateButton.setText("Generate");
		GridDataFactory.generate(generateButton, 2, 1);

		generateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				WhatIfEvaluator.evaluate(OptionModellerView.this, model);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		wrapInExpandable(composite, mainComposite, "Results", p -> createResultsViewer(p));

		// setInput(model);
		{
			final Composite sellComposite = new Composite(mainComposite, SWT.NONE);
			sellComposite.setLayoutData(GridDataFactory.fillDefaults().create());
			sellComposite.setLayout(new GridLayout(1, true));

			createSellOptionsViewer(sellComposite);

			sellOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			// sellOptionsViewer.addFilter(new ViewerFilter() {
			//
			// @Override
			// public boolean select(Viewer viewer, Object parentElement, Object element) {
			// if (element instanceof SellOption) {
			// SellOption sellOption = (SellOption) element;
			// if (sellOption.getBaseCase().isEmpty() || sellOption.getPartialCase().isEmpty()) {
			// return true;
			// }
			// }
			//
			// return false;
			// }
			// });
			{
				final Button addSell = new Button(sellComposite, SWT.PUSH);
				addSell.setText("Add existing");
				addSell.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addSell.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final SellOption row = AnalyticsFactory.eINSTANCE.createSellReference();
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS, row), model,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS);
						sellOptionsViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}
			{
				final Button addSell = new Button(sellComposite, SWT.PUSH);
				addSell.setText("Add option");
				addSell.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addSell.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final SellOption row = AnalyticsFactory.eINSTANCE.createSellOpportunity();
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS, row), model,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS);
						sellOptionsViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}
			{
				final Button addSell = new Button(sellComposite, SWT.PUSH);
				addSell.setText("Add market");
				addSell.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addSell.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final SellOption row = AnalyticsFactory.eINSTANCE.createSellMarket();
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS, row), model,
								AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS);
						sellOptionsViewer.refresh();
						mainComposite.pack();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
			}
			hookDragSource(sellOptionsViewer);

		}

		{
			final Composite vesselComposite = new Composite(mainComposite, SWT.NONE);
			vesselComposite.setLayoutData(GridDataFactory.fillDefaults().create());
			vesselComposite.setLayout(new GridLayout(1, true));

			createVesselOptionsViewer(vesselComposite);

			vesselViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			hookDragSource(vesselViewer);

		}

		mainComposite.pack();

		listenToScenarioSelection();
	}

	private void hookDragSource(final GridTreeViewer viewer) {

		final DragSource source = new DragSource(viewer.getGrid(), DND.DROP_MOVE);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {

			@Override
			public void dragStart(final DragSourceEvent event) {

			}

			@Override
			public void dragSetData(final DragSourceEvent event) {
				// TODO Auto-generated method stub
				final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

				final LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
				if (transfer.isSupportedType(event.dataType)) {
					transfer.setSelection(selection);
					transfer.setSelectionSetTime(event.time & 0xFFFF);
				}
			}

			@Override
			public void dragFinished(final DragSourceEvent event) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {

		setInput(model);
	}

	private final EContentAdapter refreshAdapter = new EContentAdapter() {
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);

			// Coarse grained refresh method..
			baseCaseViewer.refresh();
			partialCaseViewer.refresh();
			buyOptionsViewer.refresh();
			sellOptionsViewer.refresh();
			rulesViewer.refresh();
			resultsViewer.refresh();
			resultsViewer.expandAll();
			vesselViewer.refresh();
			vesselViewer.expandAll();
			baseCaseProftLabel.setText(String.format("Base P&&L: $%,d", model.getBaseCase().getProfitAndLoss()));

			mainComposite.pack();

		}
	};
	private Label baseCaseProftLabel;
	private Composite mainComposite;

	private void setInput(final OptionAnalysisModel model) {
		baseCaseViewer.setInput(model);
		partialCaseViewer.setInput(model);
		buyOptionsViewer.setInput(model);
		sellOptionsViewer.setInput(model);
		rulesViewer.setInput(model);
		resultsViewer.setInput(model);
		vesselViewer.setInput(this);
		if (!model.eAdapters().contains(refreshAdapter)) {
			model.eAdapters().add(refreshAdapter);
		}

	}

	private void createBuyOptionsViewer(final Composite parent) {
		buyOptionsViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.MULTI);
		GridViewerHelper.configureLookAndFeel(buyOptionsViewer);
		buyOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(buyOptionsViewer, "Buy", new BuyOptionDescriptionFormatter());

		buyOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS));
		hookOpenEditor(buyOptionsViewer);
	}

	private void createSellOptionsViewer(final Composite parent) {
		sellOptionsViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.MULTI);
		GridViewerHelper.configureLookAndFeel(sellOptionsViewer);
		sellOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(sellOptionsViewer, "Sell", new SellOptionDescriptionFormatter());

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);
	}

	private void createVesselOptionsViewer(final Composite parent) {
		vesselViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.MULTI);
		GridViewerHelper.configureLookAndFeel(vesselViewer);
		vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselViewer, "Vessels", new VesselDescriptionFormatter());

		vesselViewer.setContentProvider(new VesselAndClassContentProvider(this));
		hookOpenEditor(vesselViewer);
	}

	private void createRulesViewer(final Composite parent) {
		rulesViewer = new GridTreeViewer(parent, SWT.BORDER);
		GridViewerHelper.configureLookAndFeel(rulesViewer);
		rulesViewer.getGrid().setHeaderVisible(true);

		createColumn(rulesViewer, "Rule", new RuleDescriptionFormatter());

		rulesViewer.setContentProvider(new RulesViewerContentProvider());
		hookOpenEditor(rulesViewer);
	}

	private void wrapInExpandable(final Composite composite, final Composite mainComposite, final String name, final Function<Composite, Control> s) {
		wrapInExpandable(composite, mainComposite, name, s, null);
	}

	private void wrapInExpandable(final Composite composite, final Composite mainComposite, final String name, final Function<Composite, Control> s,
			@Nullable final Consumer<ExpandableComposite> customiser) {
		final ExpandableComposite expandableCompo = new ExpandableComposite(composite, SWT.NONE);
		expandableCompo.setExpanded(true);
		expandableCompo.setText(name);
		expandableCompo.setLayout(new FillLayout());

		final Control client = s.apply(expandableCompo);
		GridDataFactory.generate(expandableCompo, 2, 2);

		expandableCompo.setClient(client);
		expandableCompo.addExpansionListener(new IExpansionListener() {

			@Override
			public void expansionStateChanging(final ExpansionEvent e) {
			}

			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				mainComposite.pack();
			}
		});

		if (customiser != null) {
			customiser.accept(expandableCompo);
		}
	}

	private Control createBaseCaseViewer(final Composite parent) {
		baseCaseViewer = new GridTreeViewer(parent, SWT.BORDER);
		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setHeaderVisible(true);

		createColumn(baseCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
		createColumn(baseCaseViewer, "Sell", new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
		createColumn(baseCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

		baseCaseViewer.getGrid().setCellSelectionEnabled(true);

		baseCaseViewer.setContentProvider(new BaseCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		baseCaseViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				final Grid grid = baseCaseViewer.getGrid();
				if (menu == null) {
					menu = mgr.createContextMenu(grid);
				}
				mgr.removeAll();

				final Point mousePoint = grid.toControl(new Point(e.x, e.y));
				final GridColumn column = grid.getColumn(mousePoint);

				final IStructuredSelection selection = (IStructuredSelection) baseCaseViewer.getSelection();
				final GridItem[] items = grid.getSelection();
				if (items.length == 1) {
					mgr.add(new RunnableAction("Delete", () -> {
						final Collection<EObject> c = new LinkedList<>();
						selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

						OptionModellerView.this.getDefaultCommandHandler().handleCommand(DeleteCommand.create(OptionModellerView.this.getEditingDomain(), c), null, null);

					}));
					// BAD!
					if (column.getText().equals("Shipping")) {
						Object ed = items[0].getData();
						BaseCaseRow row = (BaseCaseRow) ed;
						ShippingOption opt = row.getShipping();
						// if (opt == null)
						{
							mgr.add(new RunnableAction("Create RT", () -> {
								final Collection<EObject> c = new LinkedList<>();
								RoundTripShippingOption o = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
								OptionModellerView.this.getDefaultCommandHandler().handleCommand(
										SetCommand.create(OptionModellerView.this.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
										AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

							}));
							mgr.add(new RunnableAction("Create Nominated", () -> {
								final Collection<EObject> c = new LinkedList<>();
								NominatedShippingOption o = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
								OptionModellerView.this.getDefaultCommandHandler().handleCommand(
										SetCommand.create(OptionModellerView.this.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, o), row,
										AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

							}));
						}
						//
						// -- filter! no nominated if shipped, etc
						// ADD Change to:
						// * RT ->
						// RT ON Small , Medium, Large
						// * Nominated on small/medium/large
					}

				}
				menu.setVisible(true);
			}
		});

		return baseCaseViewer.getGrid();
	}

	private Control createPartialCaseViewer(final Composite parent) {
		partialCaseViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.WRAP);
		GridViewerHelper.configureLookAndFeel(partialCaseViewer);
		partialCaseViewer.getGrid().setHeaderVisible(true);
		partialCaseViewer.getGrid().setAutoHeight(true);
		partialCaseViewer.getGrid().setCellSelectionEnabled(true);

		createColumn(partialCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS).getColumn().setWordWrap(true);
		;
		createColumn(partialCaseViewer, "Sell", new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS).getColumn().setWordWrap(true);
		createColumn(partialCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING).getColumn().setWordWrap(true);

		partialCaseViewer.setContentProvider(new PartialCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		partialCaseViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				final Grid grid = partialCaseViewer.getGrid();
				if (menu == null) {
					menu = mgr.createContextMenu(grid);
				}
				mgr.removeAll();

				final Point mousePoint = grid.toControl(new Point(e.x, e.y));
				final GridColumn column = grid.getColumn(mousePoint);

				final IStructuredSelection selection = (IStructuredSelection) partialCaseViewer.getSelection();
				final GridItem[] items = grid.getSelection();
				if (items.length == 1) {
					mgr.add(new RunnableAction("Delete", () -> {
						final Collection<EObject> c = new LinkedList<>();
						selection.iterator().forEachRemaining(ee -> c.add((EObject) ee));

						OptionModellerView.this.getDefaultCommandHandler().handleCommand(DeleteCommand.create(OptionModellerView.this.getEditingDomain(), c), null, null);

					}));
				}
				menu.setVisible(true);
			}
		});

		return partialCaseViewer.getGrid();
	}

	private Control createResultsViewer(final Composite parent) {
		resultsViewer = new GridTreeViewer(parent, SWT.BORDER);
		GridViewerHelper.configureLookAndFeel(resultsViewer);
		resultsViewer.getGrid().setHeaderVisible(true);
		resultsViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		createColumn(resultsViewer, "Buy", new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION);
		createColumn(resultsViewer, "Sell", new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION);
		createColumn(resultsViewer, "Details", new ResultDetailsDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL);

		resultsViewer.setContentProvider(new ResultsViewerContentProvider());
		return resultsViewer.getControl();
	}

	private OptionAnalysisModel createDemoModel1() {

		final OptionAnalysisModel model = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
		model.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
		model.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

		// final Port buyPort = PortFactory.eINSTANCE.createPort();
		// buyPort.setName("Sabine Pass");
		//
		// final Port sellPort_india = PortFactory.eINSTANCE.createPort();
		// sellPort_india.setName("India");
		//
		// final Port sellPort_japan = PortFactory.eINSTANCE.createPort();
		// sellPort_japan.setName("Japan");
		//
		// final Port sellPort_egypt = PortFactory.eINSTANCE.createPort();
		// sellPort_egypt.setName("Egypt");
		//
		// final BuyOpportunity buy = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
		// buy.setDate(LocalDate.of(2016, 9, 29));
		// buy.setPort(buyPort);
		// buy.setPriceExpression("115%HH");
		// model.getBuys().add(buy);
		//
		// final BuyOpportunity buy_bf = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
		// buy_bf.setDate(LocalDate.of(2016, 9, 29));
		// buy_bf.setPort(sellPort_japan);
		// buy_bf.setPriceExpression("115%HH");
		// model.getBuys().add(buy_bf);
		//
		// final SellOpportunity sell_egypt = AnalyticsFactory.eINSTANCE.createSellOpportunity();
		// sell_egypt.setDate(LocalDate.of(2016, 10, 29));
		// sell_egypt.setPort(sellPort_egypt);
		// sell_egypt.setPriceExpression("?");
		// model.getSells().add(sell_egypt);
		//
		// final SellOpportunity sell_india = AnalyticsFactory.eINSTANCE.createSellOpportunity();
		// sell_india.setDate(LocalDate.of(2016, 10, 29));
		// sell_india.setPort(sellPort_india);
		// sell_india.setPriceExpression("?");
		// model.getSells().add(sell_india);
		//
		// final SellOpportunity sell_japan = AnalyticsFactory.eINSTANCE.createSellOpportunity();
		// sell_japan.setDate(LocalDate.of(2016, 10, 29));
		// sell_japan.setPort(sellPort_japan);
		// sell_japan.setPriceExpression("JCC");
		// model.getSells().add(sell_japan);
		// {
		// final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
		// row.setBuyOption(buy);
		// row.setSellOption(sell_japan);
		//
		// final ShippingOption shipping = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
		// row.setShipping(shipping);
		//
		// model.getBaseCase().getBaseCase().add(row);
		// }
		//
		// {
		// final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
		// row.setBuyOption(buy_bf);
		// row.setSellOption(sell_japan);
		//
		// final ShippingOption shipping = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
		// row.setShipping(shipping);
		//
		// model.getPartialCase().getPartialCase().add(row);
		// }
		// {
		// final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
		// row.setBuyOption(buy);
		//
		// final SellGroup sellGroup = AnalyticsFactory.eINSTANCE.createSellGroup();
		// sellGroup.getOptions().add(sell_india);
		// sellGroup.getOptions().add(sell_egypt);
		// row.setSellOption(sellGroup);
		//
		// final ShippingOption shipping = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
		// row.setShipping(shipping);
		//
		// model.getPartialCase().getPartialCase().add(row);
		// }
		// ResultSet resultSet = AnalyticsFactory.eINSTANCE.createResultSet();
		// {
		// final AnalysisResultRow row = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
		// row.setBuyOption(buy_bf);
		// row.setSellOption(sell_japan);
		//
		// final ProfitAndLossResult result = AnalyticsFactory.eINSTANCE.createProfitAndLossResult();
		// result.setValue(5000000);
		//
		// row.setResultDetail(result);
		// resultSet.getRows().add(row);
		// }
		// {
		// final AnalysisResultRow row = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
		// row.setBuyOption(buy);
		// row.setSellOption(sell_egypt);
		//
		// final BreakEvenResult result = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
		// result.setPrice(3.5);
		//
		// row.setResultDetail(result);
		// resultSet.getRows().add(row);
		// }
		// {
		// final AnalysisResultRow row = AnalyticsFactory.eINSTANCE.createAnalysisResultRow();
		// row.setBuyOption(buy);
		// row.setSellOption(sell_india);
		//
		// final BreakEvenResult result = AnalyticsFactory.eINSTANCE.createBreakEvenResult();
		// result.setPrice(3.0);
		//
		// row.setResultDetail(result);
		//
		// resultSet.getRows().add(row);
		// }
		// model.getResultSets().add(resultSet);
		{
			final OptionRule rule = AnalyticsFactory.eINSTANCE.createModeOptionRule();
			rule.setName("Mode: Break-evens");
			model.getRules().add(rule);
		}

		// {
		// final OptionRule rule = AnalyticsFactory.eINSTANCE.createMatchOptionRule();
		// rule.setName("Match: One-to-Many");
		// model.getRules().add(rule);
		// }
		return model;
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(resultsViewer);
	}

	private GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final ICellRenderer renderer, final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(250);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(new CellFormatterLabelProvider(renderer, pathObjects));
		return gvc;
	}

	private void hookOpenEditor(final GridTreeViewer viewer) {

		viewer.getGrid().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(final MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDown(final MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				// TODO Auto-generated method stub
				final ViewerCell cell = viewer.getCell(new Point(e.x, e.y));
				if (cell != null) {
					final Object element = cell.getElement();
					EObject target = null;
					if (element instanceof BaseCaseRow) {
						final BaseCaseRow row = (BaseCaseRow) element;
						if (cell.getColumnIndex() == 0) {
							target = row.getBuyOption();
						}
						if (cell.getColumnIndex() == 1) {
							target = row.getSellOption();
						}
						if (cell.getColumnIndex() == 2) {
							target = row.getShipping();
						}
						if (target != null) {
							DetailCompositeDialogUtil.editSingleObject(OptionModellerView.this, target);
						}
					} else if (element instanceof PartialCaseRow) {
						final PartialCaseRow row = (PartialCaseRow) element;
						if (cell.getColumnIndex() == 0) {
							DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(row.getBuyOptions()));
						}
						if (cell.getColumnIndex() == 1) {
							DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(row.getSellOptions()));
						}
						if (cell.getColumnIndex() == 2) {
							target = row.getShipping();
							if (target != null) {
								DetailCompositeDialogUtil.editSingleObject(OptionModellerView.this, target);
							}
						}
					} else if (element instanceof EObject) {
						DetailCompositeDialogUtil.editSingleObject(OptionModellerView.this, (EObject) element);
					}
				}
			}
		});
	}
}
