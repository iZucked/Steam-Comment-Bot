package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionRule;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellOption;
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
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

			buyOptionsViewer = createBuyOptionsViewer(buyComposite);
			buyOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

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
						refreshAll();
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
					BaseCaseEvaluator.evaluate(OptionModellerView.this, model, model.getBaseCase());
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
			wrapInExpandable(composite, mainComposite, "What if?", p -> createPartialCaseViewer(p), expandableCompo -> {
				final Button textClient = new Button(expandableCompo, SWT.PUSH);
				textClient.setText("+");
				expandableCompo.setTextClient(textClient);
				textClient.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
						row.setShipping(AnalyticsFactory.eINSTANCE.createRoundTripShippingOption());
						getDefaultCommandHandler().handleCommand(AddCommand.create(getEditingDomain(), model.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE, row),
								model.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE);
						refreshAll();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			});
			//

			hookOpenEditor(partialCaseViewer);

			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			partialCaseViewer.addDropSupport(DND.DROP_MOVE, types, new PartialCaseDropTargetListener(OptionModellerView.this, model, () -> refreshAll()));
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

			}
		});

		wrapInExpandable(composite, mainComposite, "Results", p -> createResultsViewer(p));

		{
			final Composite sellComposite = new Composite(mainComposite, SWT.NONE);
			sellComposite.setLayoutData(GridDataFactory.fillDefaults().create());
			sellComposite.setLayout(new GridLayout(1, true));

			sellOptionsViewer = createSellOptionsViewer(sellComposite);

			sellOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

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

		refreshAll();
		listenToScenarioSelection();
		packAll(mainComposite);
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

			// Coarse grained refresh method..
			refreshAll();
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

	private GridTreeViewer createBuyOptionsViewer(final Composite buyComposite) {
		final GridTreeViewer buyOptionsViewer = new GridTreeViewer(buyComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridViewerHelper.configureLookAndFeel(buyOptionsViewer);
		buyOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(buyOptionsViewer, "Buy", new BuyOptionDescriptionFormatter());

		buyOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS));
		hookOpenEditor(buyOptionsViewer);

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
		GridViewerHelper.configureLookAndFeel(sellOptionsViewer);
		sellOptionsViewer.getGrid().setHeaderVisible(true);

		createColumn(sellOptionsViewer, "Sell", new SellOptionDescriptionFormatter());

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);
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
				refreshAll();
			}
		});

		if (customiser != null) {
			customiser.accept(expandableCompo);
		}
	}

	private Control createBaseCaseViewer(final Composite parent) {
		baseCaseViewer = new GridTreeViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setHeaderVisible(true);

		createColumn(baseCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
		createColumn(baseCaseViewer, "Sell", new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
		createColumn(baseCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

		baseCaseViewer.getGrid().setCellSelectionEnabled(true);

		baseCaseViewer.setContentProvider(new BaseCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		baseCaseViewer.getGrid().addMenuDetectListener(new BaseCaseContextMenuManager(baseCaseViewer, OptionModellerView.this, mgr));

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

		partialCaseViewer.getGrid().addMenuDetectListener(new PartialCaseContextMenuManager(partialCaseViewer, OptionModellerView.this, mgr));

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
		gvc.setLabelProvider(new CellFormatterLabelProvider(renderer, pathObjects));
		return gvc;
	}

	private void hookOpenEditor(final @NonNull GridTreeViewer viewer) {

		viewer.getGrid().addMouseListener(new EditObjectMouseListener(viewer, OptionModellerView.this));
	}

	private void refreshAll() {
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
		packAll(mainComposite);
	}

	public void packAll(final Control c) {
		if ((c instanceof Composite)) {
			final Composite composite = (Composite) c;
			for (final Control child : composite.getChildren()) {
				packAll(child);
			}
		}
		c.pack();
	}
}
