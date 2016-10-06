package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionRule;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ResultSet;
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
import com.mmxlabs.models.lng.analytics.ui.views.providers.ShippingOptionsContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselAndClassContentProvider;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.rcp.common.LocalMenuHelper;
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
	private GridTreeViewer shippingOptionsViewer;
	private OptionAnalysisModel model;

	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	private DialogValidationSupport validationSupport;

	@Override
	public void createPartControl(final Composite parent) {
		model = createDemoModel1();

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getRootObject(), false));
		validationSupport.setValidationTargets(Collections.singleton(model));

		// parent.setLayout(new FillLayout());

		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayoutData(GridDataFactory.swtDefaults()//
				.grab(true, true)//
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(false) //
				.numColumns(6) //
				.spacing(0, 0) //
				.margins(0, 0)//
				.create());

		{
			buyComposite = new Composite(mainComposite, SWT.NONE);
			buyComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.align(SWT.FILL, SWT.FILL).create());
			buyComposite.setLayout(new GridLayout(1, true));

			buyOptionsViewer = createBuyOptionsViewer(buyComposite);
			buyOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

			// GridDataFactory.generate(addBuy, 1, 1);
			hookDragSource(buyOptionsViewer);

		}

		// final ScrolledComposite sc = new ScrolledComposite(mainComposite, SWT.BORDER | SWT.V_SCROLL);
		// sc.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		// sc.setExpandHorizontal(true);
		// sc.setExpandVertical(true);
		// // sc.getHorizontalBar().setPageIncrement(100);
		// sc.getVerticalBar().setPageIncrement(100);

		centralComposite = new Composite(mainComposite, SWT.NONE);
		// sc.setContent(centralComposite);
		centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		centralComposite.setLayoutData(GridDataFactory.swtDefaults()//
				.grab(true, true)//
				.align(SWT.FILL, SWT.FILL).span(1, 1) //
				.create());
		// sc.setLayoutData(GridDataFactory.swtDefaults()//
		// .grab(true, true)//
		// .align(SWT.FILL, SWT.FILL).span(3, 1) //
		// .create());
		// sc.setLayout(new GridLayout(1, true));
		//
		// sc.addListener(SWT.Resize, new Listener() {
		//
		// public void handleEvent(final Event event) {
		// sc.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// }
		//
		// });
		centralComposite.setLayout(new GridLayout(1, true));
		{
			// createBaseCaseViewer(centralComposite);
			wrapInExpandable(centralComposite, "Base Case", p -> createBaseCaseViewer(p), expandableCompo -> {
				// final Button textClient = new Button(expandableCompo, SWT.PUSH);
				// textClient.setText("+");
				// expandableCompo.setTextClient(textClient);
				// textClient.addSelectionListener(new SelectionListener() {
				//
				// @Override
				// public void widgetSelected(final SelectionEvent e) {
				// final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
				// row.setShipping(AnalyticsFactory.eINSTANCE.createRoundTripShippingOption());
				// getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__BASE_CASE, row), model.getBaseCase(),
				// AnalyticsPackage.Literals.BASE_CASE__BASE_CASE);
				// refreshAll();
				// }
				//
				// @Override
				// public void widgetDefaultSelected(final SelectionEvent e) {
				//
				// }
				// });
			});

			baseCaseProftLabel = new Label(centralComposite, SWT.NONE);
			GridDataFactory.generate(baseCaseProftLabel, 2, 1);
			baseCaseProftLabel.setText("Base P&&L: <not calculated>");

			final Button baseCaseCalculator = new Button(centralComposite, SWT.FLAT);
			baseCaseCalculator.setText("Calc.");
			baseCaseCalculator.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {

					BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> BaseCaseEvaluator.evaluate(OptionModellerView.this, model, model.getBaseCase()));
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {

				}
			});

			hookOpenEditor(baseCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			baseCaseViewer.addDropSupport(DND.DROP_MOVE, types, new BaseCaseDropTargetListener(OptionModellerView.this, model, () -> refreshAll(), baseCaseViewer));
		}

		{
			wrapInExpandable(centralComposite, "What if?", p -> createPartialCaseViewer(p), expandableCompo -> {
				// final Button textClient = new Button(expandableCompo, SWT.PUSH);
				// textClient.setText("+");
				// expandableCompo.setTextClient(textClient);
				// textClient.addSelectionListener(new SelectionListener() {
				//
				// @Override
				// public void widgetSelected(final SelectionEvent e) {
				// final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
				// row.setShipping(AnalyticsFactory.eINSTANCE.createRoundTripShippingOption());
				// getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row),
				// model.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE);
				// refreshAll();
				// }
				//
				// @Override
				// public void widgetDefaultSelected(final SelectionEvent e) {
				//
				// }
				// });
			});

			hookOpenEditor(partialCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			partialCaseViewer.addDropSupport(DND.DROP_MOVE, types, new PartialCaseDropTargetListener(OptionModellerView.this, model, () -> refreshAll(), partialCaseViewer));
		}

		createRulesViewer(centralComposite);
		GridDataFactory.generate(rulesViewer.getGrid(), 2, 1);

		/*
		 * toggle for target pnl
		 */
		final Composite targetPNLToggle = createUseTargetPNLToggleComposite(centralComposite);
		GridDataFactory.generate(targetPNLToggle, 2, 1);

		final Button generateButton = new Button(centralComposite, SWT.PUSH);
		generateButton.setText("Generate");
		GridDataFactory.generate(generateButton, 2, 1);

		generateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> WhatIfEvaluator.evaluate(OptionModellerView.this, model));
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		wrapInExpandable(centralComposite, "Results", p -> createResultsViewer(p));

		{
			sellComposite = new Composite(mainComposite, SWT.NONE);
			sellComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.align(SWT.FILL, SWT.FILL).create());
			sellComposite.setLayout(new GridLayout(1, true));

			sellOptionsViewer = createSellOptionsViewer(sellComposite);
			sellOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

			hookDragSource(sellOptionsViewer);

		}

		{
			{
				vesselComposite = new Composite(mainComposite, SWT.NONE);
				vesselComposite.setLayoutData(GridDataFactory.swtDefaults()//
						.grab(true, true)//
						.align(SWT.FILL, SWT.FILL).create());
				vesselComposite.setLayout(new GridLayout(1, true));
				// vesselComposite.setExpanded(true);
				// final Composite c = new Composite(vesselComposite, SWT.NONE);

				wrapInExpandable(vesselComposite, "Shipping ", p -> createShippingOptionsViewer(p).getGrid());
				wrapInExpandable(vesselComposite, "Vessels", p -> createVesselOptionsViewer(p).getGrid());
				//
				// shippingOptionsViewer = createShippingOptionsViewer(c);
				// c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
				// shippingOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
				//
				// hookDragSource(shippingOptionsViewer);
				//
				// // final ExpandItem item2 = new ExpandItem(vesselComposite, SWT.BORDER);
				// item2.setText("Shipping");
				// item2.setControl(c);
				// item2.setHeight(c.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			}
			// {
			// final ExpandableComposite vesselComposite = new ExpandableComposite(mainComposite, SWT.BORDER);
			// vesselComposite.setLayoutData(GridDataFactory.swtDefaults()//
			// .grab(false, true)//
			// .align(SWT.FILL, SWT.FILL).create());
			// vesselComposite.setLayout(new GridLayout(1, true));
			// vesselComposite.setExpanded(true);
			// final Composite c = new Composite(vesselComposite, SWT.NONE);
			// c.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			// vesselViewer = createVesselOptionsViewer(c);
			// vesselViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			// hookDragSource(vesselViewer);
			// // final ExpandItem item1 = new ExpandItem(vesselComposite, SWT.BORDER);
			// // item1.setText("Vessels");
			// // item1.setControl(c);
			// // item1.setHeight(c.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			//
			// }
		}

		refreshAll();
		listenToScenarioSelection();
		packAll(mainComposite);
	}

	private Composite createUseTargetPNLToggleComposite(final Composite composite) {
		model.setUseTargetPNL(false);
		final Composite matching = new Composite(composite, SWT.ALL);
		final GridLayout gridLayoutRadiosMatching = new GridLayout(3, false);
		matching.setLayout(gridLayoutRadiosMatching);
		final GridData gdM = new GridData(SWT.LEFT, SWT.BEGINNING, false, false);
		gdM.horizontalSpan = 2;
		matching.setLayoutData(gdM);
		new Label(matching, SWT.NONE).setText("Use target pnl from base case");
		final Button matchingNoButton = new Button(matching, SWT.RADIO | SWT.LEFT);
		matchingNoButton.setSelection(true);
		matchingNoButton.setText("No");
		matchingNoButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				model.setUseTargetPNL(false);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});

		final Button matchingYesButton = new Button(matching, SWT.RADIO | SWT.LEFT);
		matchingYesButton.setText("Yes");
		matchingYesButton.setSelection(false);
		matchingYesButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				model.setUseTargetPNL(true);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});
		return matching;
	}

	private void hookDragSource(final GridTreeViewer viewer) {

		final DragSource source = new DragSource(viewer.getGrid(), DND.DROP_MOVE);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer));

	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {

		setInput(model);
	}

	private final EContentAdapter refreshAdapter = new EContentAdapter() {
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);

			doValidate();

			if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS) {
				refreshSections(true, EnumSet.of(SectionType.BUYS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS) {
				refreshSections(true, EnumSet.of(SectionType.SELLS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES) {
				refreshSections(true, EnumSet.of(SectionType.VESSEL));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS) {
				refreshSections(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.BASE_CASE__BASE_CASE) {
				refreshSections(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE) {
				refreshSections(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof BaseCaseRow) {
				refreshSections(false, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof PartialCaseRow) {
				refreshSections(false, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof BuyOption) {
				refreshSections(false, EnumSet.of(SectionType.BUYS, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof SellOption) {
				refreshSections(false, EnumSet.of(SectionType.SELLS, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof ShippingOption) {
				refreshSections(false, EnumSet.of(SectionType.VESSEL, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof ResultSet) {
				refreshSections(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof AnalysisResultRow) {
				refreshSections(false, EnumSet.of(SectionType.MIDDLE));
			}
			// noti
			// if (.etFeature) {
			// // Coarse grained refresh method..
			// refreshAll();
			// }
		}
	};
	private Label baseCaseProftLabel;
	private Composite mainComposite;
	private Composite buyComposite;
	private Composite centralComposite;
	private Composite vesselComposite;
	private Composite sellComposite;

	private void setInput(final OptionAnalysisModel model) {
		baseCaseViewer.setInput(model);
		partialCaseViewer.setInput(model);
		buyOptionsViewer.setInput(model);
		sellOptionsViewer.setInput(model);
		rulesViewer.setInput(model);
		resultsViewer.setInput(model);
		vesselViewer.setInput(this);
		vesselViewer.expandAll();
		shippingOptionsViewer.setInput(model);
		if (!model.eAdapters().contains(refreshAdapter)) {
			model.eAdapters().add(refreshAdapter);
		}

	}

	private GridTreeViewer createBuyOptionsViewer(final Composite buyComposite) {
		final GridTreeViewer buyOptionsViewer = new GridTreeViewer(buyComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(buyOptionsViewer);

		GridViewerHelper.configureLookAndFeel(buyOptionsViewer);
		buyOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(buyOptionsViewer, "Buy", new BuyOptionDescriptionFormatter());

		buyOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS));
		hookOpenEditor(buyOptionsViewer);

		final MenuManager mgr = new MenuManager();
		final BuyOptionsContextMenuManager listener = new BuyOptionsContextMenuManager(buyOptionsViewer, OptionModellerView.this, mgr, () -> refreshAll());
		listener.setOptionAnalysisModel(model);
		buyOptionsViewer.getGrid().addMenuDetectListener(listener);

		// Create buttons
		{
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
						refreshAll();
						editObject(row);
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

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
						refreshAll();
						editObject(row);
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

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
						refreshAll();
						editObject(row);
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}
		}

		return buyOptionsViewer;
	}

	private GridTreeViewer createSellOptionsViewer(final Composite sellComposite) {
		final GridTreeViewer sellOptionsViewer = new GridTreeViewer(sellComposite, SWT.BORDER | SWT.MULTI);
		ColumnViewerToolTipSupport.enableFor(sellOptionsViewer);

		GridViewerHelper.configureLookAndFeel(sellOptionsViewer);
		sellOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(sellOptionsViewer, "Sell", new SellOptionDescriptionFormatter());

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);

		final MenuManager mgr = new MenuManager();
		final SellOptionsContextMenuManager listener = new SellOptionsContextMenuManager(sellOptionsViewer, OptionModellerView.this, mgr, () -> refreshAll());
		listener.setOptionAnalysisModel(model);
		sellOptionsViewer.getGrid().addMenuDetectListener(listener);

		{
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
						refreshAll();
						editObject(row);
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

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
						refreshAll();
						editObject(row);
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

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
						refreshAll();
						editObject(row);
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}
		}

		return sellOptionsViewer;
	}

	private GridTreeViewer createShippingOptionsViewer(final Composite vesselComposite) {

		shippingOptionsViewer = new GridTreeViewer(vesselComposite, SWT.BORDER | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(shippingOptionsViewer);

		shippingOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(shippingOptionsViewer, "Shipping", new ShippingOptionDescriptionFormatter());
		shippingOptionsViewer.setContentProvider(new ShippingOptionsContentProvider(this));
		hookOpenEditor(shippingOptionsViewer);

		Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		shippingOptionsViewer.addDropSupport(DND.DROP_MOVE, transferTypes, new ShippingOptionsDropTargetListener(OptionModellerView.this, model, () -> refreshAll(), shippingOptionsViewer));

		final MenuManager mgr = new MenuManager();
		final ShippingOptionsContextMenuManager listener = new ShippingOptionsContextMenuManager(shippingOptionsViewer, OptionModellerView.this, mgr, () -> refreshAll());
		listener.setOptionAnalysisModel(model);
		shippingOptionsViewer.getGrid().addMenuDetectListener(listener);

		{
			{
				final Button addShipping = new Button(vesselComposite, SWT.PUSH);
				addShipping.setText("Add...");
				addShipping.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
				addShipping.addSelectionListener(new SelectionListener() {

					LocalMenuHelper helper = new LocalMenuHelper(addShipping.getParent());
					{
						helper.addAction(new RunnableAction("Nominated vessel", () -> {
							final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();

							OptionModellerView.this.getDefaultCommandHandler().handleCommand(
									AddCommand.create(OptionModellerView.this.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt), model,
									AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(opt));
							refreshAll();
						}));
						helper.addAction(new RunnableAction("Round trip vessel", () -> {
							final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();

							OptionModellerView.this.getDefaultCommandHandler().handleCommand(
									AddCommand.create(OptionModellerView.this.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt), model,
									AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(opt));
							refreshAll();
						}));
						helper.addAction(new RunnableAction("Fleet vessel", () -> {
							final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();

							OptionModellerView.this.getDefaultCommandHandler().handleCommand(
									AddCommand.create(OptionModellerView.this.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt), model,
									AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

							DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(opt));
							refreshAll();
						}));
					}

					@Override
					public void widgetSelected(final SelectionEvent e) {
						helper.open();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}
		}
		hookDragSource(shippingOptionsViewer);
		return shippingOptionsViewer;
	}

	private GridTreeViewer createVesselOptionsViewer(final Composite vesselComposite) {
		vesselViewer = new GridTreeViewer(vesselComposite, SWT.BORDER | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(vesselViewer);

		vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselViewer, "Vessels", new VesselDescriptionFormatter());
		vesselViewer.setContentProvider(new VesselAndClassContentProvider(this));
		hookOpenEditor(vesselViewer);
		hookDragSource(vesselViewer);
		return vesselViewer;
	}

	private void createRulesViewer(final Composite parent) {
		rulesViewer = new GridTreeViewer(parent, SWT.BORDER);
		GridViewerHelper.configureLookAndFeel(rulesViewer);
		rulesViewer.getGrid().setHeaderVisible(true);

		createColumn(rulesViewer, "Rule", new RuleDescriptionFormatter());

		rulesViewer.setContentProvider(new RulesViewerContentProvider());
		hookOpenEditor(rulesViewer);
	}

	private void wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s) {
		wrapInExpandable(composite, name, s, null);
	}

	private void wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s, @Nullable final Consumer<ExpandableComposite> customiser) {
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
				composite.layout();
			}
		});

		if (customiser != null) {
			customiser.accept(expandableCompo);
		}
	}

	private Control createBaseCaseViewer(final Composite parent) {
		baseCaseViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.SINGLE);
		ColumnViewerToolTipSupport.enableFor(baseCaseViewer);

		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setHeaderVisible(true);
		baseCaseViewer.getGrid().setRowHeaderVisible(true);

		createColumn(baseCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
		createColumn(baseCaseViewer, "Sell", new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
		createColumn(baseCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

		baseCaseViewer.getGrid().setCellSelectionEnabled(true);

		baseCaseViewer.setContentProvider(new BaseCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final BaseCaseContextMenuManager listener = new BaseCaseContextMenuManager(baseCaseViewer, OptionModellerView.this, mgr, () -> refreshAll());
		listener.setOptionAnalysisModel(model);
		baseCaseViewer.getGrid().addMenuDetectListener(listener);

		return baseCaseViewer.getGrid();
	}

	private Control createPartialCaseViewer(final Composite parent) {
		partialCaseViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.WRAP);
		ColumnViewerToolTipSupport.enableFor(partialCaseViewer);

		GridViewerHelper.configureLookAndFeel(partialCaseViewer);
		partialCaseViewer.getGrid().setHeaderVisible(true);
		partialCaseViewer.getGrid().setAutoHeight(true);
		partialCaseViewer.getGrid().setCellSelectionEnabled(true);

		partialCaseViewer.getGrid().setRowHeaderVisible(true);

		createColumn(partialCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS).getColumn().setWordWrap(true);
		createColumn(partialCaseViewer, "Sell", new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS).getColumn().setWordWrap(true);
		createColumn(partialCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING).getColumn().setWordWrap(true);

		partialCaseViewer.setContentProvider(new PartialCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final PartialCaseContextMenuManager listener = new PartialCaseContextMenuManager(partialCaseViewer, OptionModellerView.this, mgr, () -> refreshAll());
		partialCaseViewer.getGrid().addMenuDetectListener(listener);
		listener.setOptionAnalysisModel(model);

		return partialCaseViewer.getGrid();
	}

	private Control createResultsViewer(final Composite parent) {

		resultsViewer = new GridTreeViewer(parent, SWT.BORDER);
		ColumnViewerToolTipSupport.enableFor(resultsViewer);
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

		{
			final OptionRule rule = AnalyticsFactory.eINSTANCE.createModeOptionRule();
			rule.setName("Mode: Break-evens");
			model.getRules().add(rule);
		}

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
		gvc.setLabelProvider(new CellFormatterLabelProvider(renderer, pathObjects) {

			Image imgError = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/error.gif").createImage();
			Image imgWarn = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/warning.gif").createImage();
			Image imgInfo = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/information.gif").createImage();

			@Override
			protected @Nullable Image getImage(@NonNull final ViewerCell cell, @Nullable final Object element) {

				if (validationErrors.containsKey(element)) {
					final IStatus status = validationErrors.get(element);
					if (!status.isOK()) {
						if (status.matches(IStatus.ERROR)) {
							return imgError;
						}
						if (status.matches(IStatus.WARNING)) {
							return imgWarn;
						}
						if (status.matches(IStatus.INFO)) {
							return imgWarn;
						}
					}
				} else {

				}
				return null;
			}

			@Override
			public String getToolTipText(final Object element) {

				final Set<Object> targetElements = new HashSet<>();
				targetElements.add(element);
				// FIXME: Hacky!
				if (element instanceof BaseCaseRow) {
					final BaseCaseRow baseCaseRow = (BaseCaseRow) element;
					if ("Buy".equals(name)) {
						targetElements.add(baseCaseRow.getBuyOption());
					}
					if ("Sell".equals(name)) {
						targetElements.add(baseCaseRow.getSellOption());
					}
					if ("Shipping".equals(name)) {
						targetElements.add(baseCaseRow.getShipping());
					}
				}
				if (element instanceof PartialCaseRow) {
					final PartialCaseRow row = (PartialCaseRow) element;
					if ("Buy".equals(name)) {
						targetElements.addAll(row.getBuyOptions());
					}
					if ("Sell".equals(name)) {
						targetElements.addAll(row.getSellOptions());
					}
					if ("Shipping".equals(name)) {
						targetElements.add(row.getShipping());
					}
				}
				targetElements.remove(null);

				final StringBuilder sb = new StringBuilder();
				for (final Object target : targetElements) {
					if (validationErrors.containsKey(target)) {
						final IStatus status = validationErrors.get(target);
						if (!status.isOK()) {
							if (sb.length() > 0) {
								sb.append("\n");
							}
							sb.append(status.getMessage());
						}
					}
				}
				if (sb.length() > 0) {
					return sb.toString();
				}
				return super.getToolTipText(element);
			}

			@Override
			public void update(final ViewerCell cell) {
				super.update(cell);

				final GridItem item = (GridItem) cell.getItem();
				item.setHeaderText("");
				item.setHeaderImage(null);

				final Object element = cell.getElement();
				if (element instanceof BaseCaseRow || element instanceof PartialCaseRow) {
					if (validationErrors.containsKey(element)) {
						final IStatus status = validationErrors.get(element);
						if (!status.isOK()) {
							if (status.matches(IStatus.ERROR)) {
								item.setHeaderImage(imgError);
							}
							if (status.matches(IStatus.WARNING)) {
								item.setHeaderImage(imgWarn);
							}
							if (status.matches(IStatus.INFO)) {
								item.setHeaderImage(imgWarn);
							}
						}
					}
				}
			}

			@Override
			public void dispose() {
				imgError.dispose();
				imgWarn.dispose();
				imgInfo.dispose();
			}

		});
		return gvc;
	}

	private void hookOpenEditor(final @NonNull GridTreeViewer viewer) {

		viewer.getGrid().addMouseListener(new EditObjectMouseListener(viewer, OptionModellerView.this));
	}

	private enum SectionType {
		BUYS, MIDDLE, SELLS, VESSEL
	};

	private void refreshAll() {

	}

	private void refreshSections(boolean layout, EnumSet<SectionType> sections) {
		// Coarse grained refresh method..
		if (sections.contains(SectionType.BUYS)) {
			buyOptionsViewer.refresh();
			if (layout) {
				packAll(buyComposite);
			}
		}
		if (sections.contains(SectionType.MIDDLE)) {
			baseCaseViewer.refresh();
			baseCaseProftLabel.setText(String.format("Base P&&L: $%,d", model.getBaseCase().getProfitAndLoss()));
			partialCaseViewer.refresh();
			rulesViewer.refresh();
			resultsViewer.refresh();
			resultsViewer.expandAll();
			if (layout) {
				packAll(centralComposite);
			}
		}
		if (sections.contains(SectionType.SELLS)) {
			sellOptionsViewer.refresh();
			if (layout) {
				packAll(sellComposite);
			}
		}
		if (sections.contains(SectionType.VESSEL)) {
			vesselViewer.refresh();
			shippingOptionsViewer.refresh();
			vesselViewer.expandAll();
			if (layout) {
				packAll(vesselComposite);
			}
		}
		// mainComposite.pack(true);
	}

	public void packAll(final Control c) {
		if ((c instanceof Composite)) {
			final Composite composite = (Composite) c;
			for (final Control child : composite.getChildren()) {
				packAll(child);
			}
			((Composite) c).layout(true);
		}
		// c.layout (true);
	}

	private void doValidate() {
		try {
			pushExtraValidationContext(validationSupport.getValidationContext());
			final IStatus status = validationSupport.validate();

			validationErrors.clear();
			validationSupport.processStatus(status, validationErrors);

		} finally {

			popExtraValidationContext();

		}
	}
}
