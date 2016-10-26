package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collections;
import java.util.EnumSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionRule;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.WhatIfEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.OptionTreeViewerFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ResultDetailsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.BaseCaseContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.CellFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.OptionsTreeViewerContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.OptionsViewerContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.PartialCaseContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsViewerContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ShippingOptionsContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselClassContentProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.VesselContentProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;
import com.mmxlabs.models.ui.properties.views.Options;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.application.BindSelectionListener;
import com.mmxlabs.rcp.common.application.IInjectableE4ComponentFactory;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class OptionModellerView extends ScenarioInstanceView implements CommandStackListener {

	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	private GridTreeViewer baseCaseViewer;
	private GridTreeViewer partialCaseViewer;
	private GridTreeViewer buyOptionsViewer;
	private GridTreeViewer sellOptionsViewer;
	// private GridTreeViewer rulesViewer;
	private GridTreeViewer resultsViewer;
	private GridTreeViewer vesselViewer;
	private GridTreeViewer vesselClassViewer;
	private GridTreeViewer shippingOptionsViewer;
	private GridTreeViewer optionsTreeViewer;

	private OptionAnalysisModel model;

	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	private DialogValidationSupport validationSupport;

	// Callbacks for objects that need the current input
	private final List<Consumer<OptionAnalysisModel>> inputWants = new LinkedList<>();
	private Label errorLabel;

	private Image image_calculate;
	private Image image_grey_calculate;
	private Image image_generate;
	private Image image_grey_generate;
	private Image image_grey_add;

	private IEclipseContext pnlReportContext;
	private Object pnlReport;
	private IEclipseContext econsReportContext;
	private Object econsReport;
	private NumberInlineEditor numberInlineEditor;
	private Control inputPNL;

	@Override
	public void createPartControl(final Composite parent) {

		final ImageDescriptor calc_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_calc.gif");
		image_calculate = calc_desc.createImage();
		image_grey_calculate = ImageDescriptor.createWithFlags(calc_desc, SWT.IMAGE_GRAY).createImage();

		final ImageDescriptor generate_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_generate.gif");
		image_generate = generate_desc.createImage();
		image_grey_generate = ImageDescriptor.createWithFlags(generate_desc, SWT.IMAGE_GRAY).createImage();

		final ImageDescriptor baseAdd = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD);
		image_grey_add = ImageDescriptor.createWithFlags(baseAdd, SWT.IMAGE_GRAY).createImage();

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getRootObject(), false));
		validationSupport.setValidationTargets(Collections.singleton(getModel()));

		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainComposite.setLayoutData(GridDataFactory.swtDefaults()//
				.grab(true, true)//
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(false) //
				.numColumns(5) //
				.spacing(0, 0) //
				.margins(0, 0)//
				.create());

		{
			buyComposite = new Composite(mainComposite, SWT.NONE);
			buyComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(3, 1) //
					.align(SWT.FILL, SWT.FILL).create());
			buyComposite.setLayout(new GridLayout(1, true));

			{

				wrapInExpandable(buyComposite, "Options history", p -> createOptionsTreeViewer(p), expandableCompo -> {

					final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
					final OptionsTreeViewerDropTargetListener listener = new OptionsTreeViewerDropTargetListener(OptionModellerView.this, optionsTreeViewer);
					final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE);
					dropTarget.setTransfer(types);
					dropTarget.addDropListener(listener);
				});

				optionsTreeViewer.getGrid().setCellSelectionEnabled(true);

				{
					final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
					final OptionsTreeViewerDropTargetListener listener = new OptionsTreeViewerDropTargetListener(OptionModellerView.this, optionsTreeViewer);
					optionsTreeViewer.addDropSupport(DND.DROP_MOVE, types, listener);
				}

			}

			wrapInExpandable(buyComposite, "Buys", p -> createBuyOptionsViewer(p), expandableComposite -> {

				{
					final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
					final BuysDropTargetListener listener = new BuysDropTargetListener(OptionModellerView.this, buyOptionsViewer);
					inputWants.add(model -> listener.setOptionAnalysisModel(model));
					// Control control = getControl();
					final DropTarget dropTarget = new DropTarget(expandableComposite, DND.DROP_MOVE);
					dropTarget.setTransfer(types);
					dropTarget.addDropListener(listener);
				}

				final Label c = new Label(expandableComposite, SWT.NONE);
				expandableComposite.setTextClient(c);
				c.setImage(image_grey_add);
				c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(true, false).create());
				c.addMouseTrackListener(new MouseTrackListener() {

					@Override
					public void mouseHover(final MouseEvent e) {

					}

					@Override
					public void mouseExit(final MouseEvent e) {
						c.setImage(image_grey_add);
					}

					@Override
					public void mouseEnter(final MouseEvent e) {
						c.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
					}
				});
				c.addMouseListener(OptionMenuHelper.createNewBuyOptionMenuListener(c.getParent(), OptionModellerView.this, () -> getModel()));
			});
			buyOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(false, true).hint(SWT.DEFAULT, 400).create());
			hookDragSource(buyOptionsViewer);

		}

		{
			centralComposite = new Composite(mainComposite, SWT.NONE);
			// sc.setContent(centralComposite);
			centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			centralComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(true, true)//
					.align(SWT.FILL, SWT.FILL) //
					.span(1, 1) //
					.create());

			centralComposite.setLayout(new GridLayout(1, true));
			{
				wrapInExpandable(centralComposite, "Target", p -> createBaseCaseViewer(p), expandableCompo -> {

					final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
					final BaseCaseDropTargetListener listener = new BaseCaseDropTargetListener(OptionModellerView.this, baseCaseViewer);
					inputWants.add(model -> listener.setOptionAnalysisModel(model));
					// Control control = getControl();
					final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE);
					dropTarget.setTransfer(types);
					dropTarget.addDropListener(listener);
				});

				final Composite c = new Composite(centralComposite, SWT.NONE);
				GridDataFactory.generate(c, 1, 1);
				c.setLayout(new GridLayout(5, false));
				baseCaseProftLabel = new Label(c, SWT.NONE);
				GridDataFactory.generate(baseCaseProftLabel, 1, 1);
				baseCaseProftLabel.setText("Base P&&L: $");
				inputPNL = createInputTargetPNL(c);
				inputPNL.setLayoutData(new GridData(100, SWT.DEFAULT));

				final Label baseCaseCalculator = new Label(c, SWT.NONE);
				// baseCaseCalculator.setText("Calc."); --cogs
				baseCaseCalculator.setImage(image_grey_calculate);
				GridDataFactory.generate(baseCaseCalculator, 1, 1);
				baseCaseCalculator.addMouseTrackListener(new MouseTrackListener() {

					@Override
					public void mouseHover(final MouseEvent e) {
					}

					@Override
					public void mouseExit(final MouseEvent e) {
						baseCaseCalculator.setImage(image_grey_calculate);
					}

					@Override
					public void mouseEnter(final MouseEvent e) {
						baseCaseCalculator.setImage(image_calculate);
					}
				});

				baseCaseCalculator.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseDown(final MouseEvent e) {

						if (getModel() != null) {
							BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(),
									() -> BaseCaseEvaluator.evaluate(OptionModellerView.this, getModel(), getModel().getBaseCase(), true, "Base Case"));
						}
					}

				});
				/*
				 * toggle for target pnl
				 */
				final Composite targetPNLToggle = createUseTargetPNLToggleComposite(c);
				GridDataFactory.generate(targetPNLToggle, 1, 1);

				hookOpenEditor(baseCaseViewer);

				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final BaseCaseDropTargetListener listener = new BaseCaseDropTargetListener(OptionModellerView.this, baseCaseViewer);
				inputWants.add(model -> listener.setOptionAnalysisModel(model));
				baseCaseViewer.addDropSupport(DND.DROP_MOVE, types, listener);
			}

			{
				wrapInExpandable(centralComposite, "Options", p -> createPartialCaseViewer(p), expandableCompo -> {

					final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
					final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(OptionModellerView.this, partialCaseViewer);
					inputWants.add(model -> listener.setOptionAnalysisModel(model));
					// Control control = getControl();
					final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE | DND.DROP_LINK);
					dropTarget.setTransfer(types);
					dropTarget.addDropListener(listener);
				});

				hookOpenEditor(partialCaseViewer);

				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(OptionModellerView.this, partialCaseViewer);
				inputWants.add(model -> listener.setOptionAnalysisModel(model));
				partialCaseViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_LINK, types, listener);
			}

			{
				final Composite generateComposite = new Composite(centralComposite, SWT.NONE);
				GridDataFactory.generate(generateComposite, 2, 1);

				generateComposite.setLayout(new GridLayout(1, true));

				final Label generateButton = new Label(generateComposite, SWT.NONE);
				generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).create());
				generateButton.setImage(image_grey_generate);
				generateButton.addMouseListener(new MouseListener() {

					@Override
					public void mouseDown(final MouseEvent e) {

						if (getModel() != null) {
							BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> WhatIfEvaluator.evaluate(OptionModellerView.this, getModel()));
						}
					}

					@Override
					public void mouseDoubleClick(final MouseEvent e) {

					}

					@Override
					public void mouseUp(final MouseEvent e) {

					}
				});
				generateButton.addMouseTrackListener(new MouseTrackListener() {

					@Override
					public void mouseHover(final MouseEvent e) {
					}

					@Override
					public void mouseExit(final MouseEvent e) {
						generateButton.setImage(image_grey_generate);
					}

					@Override
					public void mouseEnter(final MouseEvent e) {
						generateButton.setImage(image_generate);
					}
				});
			}

			wrapInExpandable(centralComposite, "Results", p -> createResultsViewer(p));

			{
				wrapInExpandable(buyComposite, "Sells", p -> createSellOptionsViewer(p), expandableComposite -> {

					{
						final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
						final SellsDropTargetListener listener = new SellsDropTargetListener(OptionModellerView.this, sellOptionsViewer);
						inputWants.add(model -> listener.setOptionAnalysisModel(model));
						// Control control = getControl();
						final DropTarget dropTarget = new DropTarget(expandableComposite, DND.DROP_MOVE);
						dropTarget.setTransfer(types);
						dropTarget.addDropListener(listener);
						// expandableComposite.addDropSupport(DND.DROP_MOVE, types, listener);
					}

					final Label addSellButton = new Label(expandableComposite, SWT.NONE);
					addSellButton.setImage(image_grey_add);
					expandableComposite.setTextClient(addSellButton);
					addSellButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).grab(true, false).create());
					addSellButton.addMouseTrackListener(new MouseTrackListener() {

						@Override
						public void mouseHover(final MouseEvent e) {

						}

						@Override
						public void mouseExit(final MouseEvent e) {
							addSellButton.setImage(image_grey_add);
						}

						@Override
						public void mouseEnter(final MouseEvent e) {
							addSellButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
						}
					});
					addSellButton.addMouseListener(OptionMenuHelper.createNewSellOptionMenuListener(addSellButton.getParent(), OptionModellerView.this, () -> getModel()));

				});
				sellOptionsViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 400).create());

				hookDragSource(sellOptionsViewer);

			}

			{
				{

					wrapInExpandable(buyComposite, "Shipping", p -> createShippingOptionsViewer(p).getGrid(), expandableCompo -> {

						{
							final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
							final ShippingOptionsDropTargetListener listener = new ShippingOptionsDropTargetListener(OptionModellerView.this, shippingOptionsViewer);
							inputWants.add(model -> listener.setOptionAnalysisModel(model));
							// Control control = getControl();
							final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE);
							dropTarget.setTransfer(types);
							dropTarget.addDropListener(listener);
						}

						final Label addShipping = new Label(expandableCompo, SWT.NONE);
						expandableCompo.setTextClient(addShipping);
						addShipping.setImage(image_grey_add);

						addShipping.setLayoutData(GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BOTTOM).grab(true, false).create());
						addShipping.addMouseTrackListener(new MouseTrackListener() {

							@Override
							public void mouseHover(final MouseEvent e) {

							}

							@Override
							public void mouseExit(final MouseEvent e) {
								addShipping.setImage(image_grey_add);
							}

							@Override
							public void mouseEnter(final MouseEvent e) {
								addShipping.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
							}
						});

						addShipping.addMouseListener(new MouseListener() {

							LocalMenuHelper helper = new LocalMenuHelper(addShipping.getParent());
							{
								helper.addAction(new RunnableAction("Nominated vessel", () -> {
									final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();

									OptionModellerView.this.getDefaultCommandHandler().handleCommand(
											AddCommand.create(OptionModellerView.this.getEditingDomain(), getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
											getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

									DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(opt));
								}));
								helper.addAction(new RunnableAction("Round trip vessel", () -> {
									final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();

									OptionModellerView.this.getDefaultCommandHandler().handleCommand(
											AddCommand.create(OptionModellerView.this.getEditingDomain(), getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
											getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

									DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(opt));
								}));
								helper.addAction(new RunnableAction("Fleet vessel", () -> {
									final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
									AnalyticsBuilder.setDefaultEntity(OptionModellerView.this, opt);
									OptionModellerView.this.getDefaultCommandHandler().handleCommand(
											AddCommand.create(OptionModellerView.this.getEditingDomain(), getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt),
											getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);

									DetailCompositeDialogUtil.editSelection(OptionModellerView.this, new StructuredSelection(opt));
								}));
							}

							@Override
							public void mouseDoubleClick(final MouseEvent e) {

							}

							@Override
							public void mouseDown(final MouseEvent e) {
								helper.open();

							}

							@Override
							public void mouseUp(final MouseEvent e) {

							}
						});
					});
					// Failed attempt to set a minimum size on the table
					shippingOptionsViewer.getGrid().setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 150).create());

					wrapInExpandable(buyComposite, "Vessels", p -> createVesselOptionsViewer(p).getGrid()).setExpanded(false);
					wrapInExpandable(buyComposite, "Vessel Classes", p -> createVesselClassOptionsViewer(p).getGrid()).setExpanded(false);
				}
			}

		}

		rhsComposite = new Composite(mainComposite, SWT.NONE);
		rhsComposite.setLayout(new GridLayout(1, true));
		rhsComposite.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				.hint(200, SWT.DEFAULT) //
				// .span(1, 1) //
				.align(SWT.FILL, SWT.FILL).create());

		{
			final Pair<Object, IEclipseContext> p = createReportControl("com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport", "Econs", rhsComposite, childContext -> {
			});
			this.econsReport = p.getFirst();
			this.econsReportContext = p.getSecond();
		}
		{
			final Pair<Object, IEclipseContext> p = createReportControl("com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport", "P&&L", rhsComposite, childContext -> {
				final Options options = new Options("pnl", null, false);
				childContext.set(Options.class, options);
			});
			this.pnlReport = p.getFirst();
			this.pnlReportContext = p.getSecond();
		}

		listenToScenarioSelection();

		packAll(mainComposite);

		final IActionBars actionBars = getViewSite().getActionBars();

		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		updateActions(getEditingDomain());

	}

	private Control createInputTargetPNL(final Composite composite) {
		numberInlineEditor = new NumberInlineEditor(AnalyticsPackage.Literals.BASE_CASE__PROFIT_AND_LOSS);
		numberInlineEditor.setCommandHandler(getDefaultCommandHandler());
		final Control control = numberInlineEditor.createControl(composite, null, new FormToolkit(Display.getDefault()));

		return control;
	}

	private Composite createUseTargetPNLToggleComposite(final Composite composite) {
		final Composite matching = new Composite(composite, SWT.ALL);
		final GridLayout gridLayoutRadiosMatching = new GridLayout(3, false);
		matching.setLayout(gridLayoutRadiosMatching);
		final GridData gdM = new GridData(SWT.LEFT, SWT.BEGINNING, false, false);
		gdM.horizontalSpan = 2;
		matching.setLayoutData(gdM);
		new Label(matching, SWT.NONE).setText("B/E with target P&&L");
		final Button matchingButton = new Button(matching, SWT.CHECK | SWT.LEFT);
		matchingButton.setSelection(false);
		matchingButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				getDefaultCommandHandler().handleCommand(
						SetCommand.create(getEditingDomain(), getModel(), AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, matchingButton.getSelection()), getModel(),
						AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});

		// final Button matchingYesButton = new Button(matching, SWT.RADIO | SWT.LEFT);
		// matchingYesButton.setText("Yes");
		// matchingYesButton.setSelection(false);
		// matchingYesButton.addSelectionListener(new SelectionListener() {
		//
		// @Override
		// public void widgetSelected(final SelectionEvent e) {
		// getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, Boolean.TRUE), model,
		// AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
		// }
		//
		// @Override
		// public void widgetDefaultSelected(final SelectionEvent e) {
		// }
		// });

		// FIXME: This control does not respond to e.g. Undo() calls.
		// Need to hook up explicitly to the refresh adapter

		inputWants.add(model -> {
			if (model != null) {
				matchingButton.setSelection(model.isUseTargetPNL());
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

		// Some slightly hacky code to hide the editor if there is no scenario open
		if (scenarioInstance == null) {

			if (errorLabel == null) {
				errorLabel = new Label(mainComposite.getParent(), SWT.NONE);
				errorLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
				errorLabel.setText("No scenario selected");
			}

			mainComposite.setVisible(false);
			mainComposite.getParent().layout(true);
			setInput(null);
		} else {
			mainComposite.setVisible(true);
			if (errorLabel != null) {
				errorLabel.dispose();
				errorLabel = null;
			}
			mainComposite.getParent().layout(true);

			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				if (lngScenarioModel.getOptionModels().isEmpty()) {
					setModel(createDemoModel1());
					lngScenarioModel.getOptionModels().add(getModel());
				} else {
					setModel(lngScenarioModel.getOptionModels().get(0));
				}
			}
			setInput(getModel());
		}

	}

	private final EContentAdapter historyRenameAdaptor = new EContentAdapter() {
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.getFeature() == MMXCorePackage.Literals.NAMED_OBJECT__NAME && notification.getNotifier() instanceof OptionAnalysisModel) {
				refreshSections(false, EnumSet.of(SectionType.BUYS));
			}
		}
	};

	private final MMXContentAdapter refreshAdapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			doValidate();
			final Pair<Boolean, EnumSet<SectionType>> p = processNotification(notification);
			if (p != null) {
				refreshSections(p.getFirst(), p.getSecond());
				if (p.getFirst()) {
					refreshSections(p.getFirst(), p.getSecond());
				}
			}
		}

		/**
		 * @since 2.2
		 */
		protected void missedNotifications(final List<Notification> missed) {
			doValidate();

			boolean refreshSections = false;
			final EnumSet<SectionType> sections = EnumSet.noneOf(SectionType.class);
			for (final Notification n : missed) {
				final Pair<Boolean, EnumSet<SectionType>> p = processNotification(n);
				if (p != null) {
					refreshSections |= p.getFirst();
					sections.addAll(p.getSecond());
				}
			}
			if (!sections.isEmpty()) {
				refreshSections(refreshSections, sections);
				if (refreshSections) {
					refreshSections(refreshSections, sections);
				}
			}
		}

		//
		private @Nullable Pair<Boolean, EnumSet<SectionType>> processNotification(final Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return null;
			}

			if (notification.getNotifier() == getModel() && notification.getFeature() == MMXCorePackage.Literals.NAMED_OBJECT__NAME) {
				setPartName("Sandbox " + getModel().getName());
				return null;
			}

			if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS) {
				return new Pair<>(true, EnumSet.of(SectionType.BUYS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS) {
				return new Pair<>(true, EnumSet.of(SectionType.SELLS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES) {
				return new Pair<>(true, EnumSet.of(SectionType.VESSEL));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.BASE_CASE__BASE_CASE) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.BASE_CASE__PROFIT_AND_LOSS) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.PARTIAL_CASE__PARTIAL_CASE) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof BaseCaseRow) {
				return new Pair<>(false, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof PartialCaseRow) {
				return new Pair<>(false, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof BuyOption) {
				return new Pair<>(false, EnumSet.of(SectionType.BUYS, SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__CHILDREN) {
				return new Pair<>(true, EnumSet.of(SectionType.BUYS));
			} else if (notification.getFeature() == MMXCorePackage.Literals.NAMED_OBJECT__NAME && notification.getNotifier() instanceof OptionAnalysisModel) {
				return new Pair<>(false, EnumSet.of(SectionType.BUYS));
			} else if (notification.getNotifier() instanceof SellOption) {
				return new Pair<>(false, EnumSet.of(SectionType.SELLS, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof ShippingOption) {
				return new Pair<>(false, EnumSet.of(SectionType.VESSEL, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof ResultSet) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof AnalysisResultRow) {
				return new Pair<>(false, EnumSet.of(SectionType.MIDDLE));
			}

			return null;
		}
	};
	private Label baseCaseProftLabel;
	private Composite mainComposite;
	private Composite buyComposite;
	private Composite centralComposite;
	// private Composite vesselComposite;
	// private Composite sellComposite;
	private OptionAnalysisModel rootModel;
	private BaseCaseWiringDiagram baseCaseDiagram;
	private PartialCaseWiringDiagram partialCaseDiagram;
	private ResultsSetWiringDiagram resultsDiagram;

	public void setInput(final @Nullable OptionAnalysisModel model) {
		if (this.getModel() != null) {
			if (this.getModel().eAdapters().contains(refreshAdapter)) {
				this.getModel().eAdapters().remove(refreshAdapter);
			}
		}

		this.setModel(model);

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getRootObject(), false));
		if (model != null) {
			validationSupport.setValidationTargets(Collections.singleton(model));
		} else {
			validationSupport.setValidationTargets(Collections.emptySet());
		}

		doValidate();

		// control
		numberInlineEditor.display(new IDialogEditingContext() {

			@Override
			public void registerEditorControl(final EObject target, final EStructuralFeature feature, final Control control) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isNewEdit() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isMultiEdit() {
				return false;
			}
			// TODO Auto-generated method stub

			@Override
			public IScenarioEditingLocation getScenarioEditingLocation() {
				return OptionModellerView.this;
			}

			@Override
			public List<Control> getEditorControls(final EObject target, final EStructuralFeature feature) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IDialogController getDialogController() {
				// TODO Auto-generated method stub
				return null;
			}
		}, this.getRootObject(), model != null ? model.getBaseCase() : null, model != null ? Lists.newArrayList(model.getBaseCase()) : Lists.newArrayList());
		inputPNL.redraw();
		baseCaseViewer.setInput(model);
		if (model != null) {
			baseCaseDiagram.setBaseCase(model.getBaseCase());
		} else {
			baseCaseDiagram.setBaseCase(null);
		}
		partialCaseViewer.setInput(model);
		if (model != null) {
			partialCaseDiagram.setRoot(model);
		} else {
			partialCaseDiagram.setRoot(null);
		}
		buyOptionsViewer.setInput(model);
		sellOptionsViewer.setInput(model);
		// rulesViewer.setInput(model);
		resultsViewer.setInput(model);
		if (model != null) {
			resultsDiagram.setRoot(model);
		} else {
			resultsDiagram.setRoot(null);
		}
		vesselViewer.setInput(this);
		vesselClassViewer.setInput(this);
		vesselViewer.expandAll();
		shippingOptionsViewer.setInput(model);

		if (rootModel != null) {
			rootModel.eAdapters().remove(historyRenameAdaptor);
		}
		rootModel = getRootOptionsModel(model);
		if (rootModel != null) {
			rootModel.eAdapters().add(historyRenameAdaptor);
			optionsTreeViewer.setInput(Collections.singleton(rootModel));
		}

		if (model != null) {
			setPartName("Sandbox " + model.getName());

			if (!model.eAdapters().contains(refreshAdapter)) {
				model.eAdapters().add(refreshAdapter);
			}
		} else {
			setPartName("Sandbox");
		}
		for (final Consumer<OptionAnalysisModel> want : inputWants) {
			want.accept(model);
		}

		refreshSections(true, EnumSet.allOf(SectionType.class));

	}

	private OptionAnalysisModel getRootOptionsModel(@Nullable final OptionAnalysisModel optionModel) {
		OptionAnalysisModel optionAnalysisModel = optionModel;
		while (optionAnalysisModel != null && optionAnalysisModel.eContainer() != null && optionAnalysisModel.eContainer() instanceof OptionAnalysisModel) {
			optionAnalysisModel = (OptionAnalysisModel) optionAnalysisModel.eContainer();
		}
		if (optionAnalysisModel != null && optionAnalysisModel.getName() == null) {
			optionAnalysisModel.setName("root");
		}
		return optionAnalysisModel;
	}

	private Control createBuyOptionsViewer(final Composite buyComposite) {

		buyOptionsViewer = new GridTreeViewer(buyComposite, SWT.NONE | SWT.MULTI | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(buyOptionsViewer);

		GridViewerHelper.configureLookAndFeel(buyOptionsViewer);
		buyOptionsViewer.getGrid().setHeaderVisible(false);

		createColumn(buyOptionsViewer, "Buy", new BuyOptionDescriptionFormatter(), false);

		buyOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS));
		hookOpenEditor(buyOptionsViewer);
		{
			final MenuManager mgr = new MenuManager();
			final BuyOptionsContextMenuManager listener = new BuyOptionsContextMenuManager(buyOptionsViewer, OptionModellerView.this, mgr);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			buyOptionsViewer.getGrid().addMenuDetectListener(listener);
		}
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final BuysDropTargetListener listener = new BuysDropTargetListener(OptionModellerView.this, buyOptionsViewer);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			buyOptionsViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}

		return buyOptionsViewer.getControl();
	}

	private Control createSellOptionsViewer(final Composite sellComposite) {

		sellOptionsViewer = new GridTreeViewer(sellComposite, SWT.NONE | SWT.MULTI);
		ColumnViewerToolTipSupport.enableFor(sellOptionsViewer);

		GridViewerHelper.configureLookAndFeel(sellOptionsViewer);
		sellOptionsViewer.getGrid().setHeaderVisible(false);

		createColumn(sellOptionsViewer, "Sell", new SellOptionDescriptionFormatter(), false);

		sellOptionsViewer.setContentProvider(new OptionsViewerContentProvider(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS));
		hookOpenEditor(sellOptionsViewer);
		{
			final MenuManager mgr = new MenuManager();
			final SellOptionsContextMenuManager listener = new SellOptionsContextMenuManager(sellOptionsViewer, OptionModellerView.this, mgr);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			sellOptionsViewer.getGrid().addMenuDetectListener(listener);
		}
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final SellsDropTargetListener listener = new SellsDropTargetListener(OptionModellerView.this, sellOptionsViewer);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
			sellOptionsViewer.addDropSupport(DND.DROP_MOVE, types, listener);
		}
		return sellOptionsViewer.getControl();
	}

	private GridTreeViewer createShippingOptionsViewer(final Composite vesselComposite) {

		shippingOptionsViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(shippingOptionsViewer);

		shippingOptionsViewer.getGrid().setHeaderVisible(false);

		createColumn(shippingOptionsViewer, "Templates", new ShippingOptionDescriptionFormatter(), false);
		shippingOptionsViewer.setContentProvider(new ShippingOptionsContentProvider(this));
		hookOpenEditor(shippingOptionsViewer);

		{
			final Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final ShippingOptionsDropTargetListener listener = new ShippingOptionsDropTargetListener(OptionModellerView.this, shippingOptionsViewer);
			shippingOptionsViewer.addDropSupport(DND.DROP_MOVE, transferTypes, listener);
			inputWants.add(model -> listener.setOptionAnalysisModel(model));
		}

		final MenuManager mgr = new MenuManager();
		final ShippingOptionsContextMenuManager listener = new ShippingOptionsContextMenuManager(shippingOptionsViewer, OptionModellerView.this, mgr);
		shippingOptionsViewer.getGrid().addMenuDetectListener(listener);

		hookDragSource(shippingOptionsViewer);

		return shippingOptionsViewer;
	}

	private GridTreeViewer createVesselOptionsViewer(final Composite vesselComposite) {
		vesselViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(vesselViewer);
		vesselViewer.getGrid().setHeaderVisible(false);

		// vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselViewer, "Vessels", new VesselDescriptionFormatter(), false);
		vesselViewer.setContentProvider(new VesselContentProvider(this));
		hookDragSource(vesselViewer);
		return vesselViewer;
	}

	private GridTreeViewer createVesselClassOptionsViewer(final Composite vesselComposite) {
		vesselClassViewer = new GridTreeViewer(vesselComposite, SWT.NONE | SWT.MULTI);

		GridViewerHelper.configureLookAndFeel(vesselClassViewer);
		vesselClassViewer.getGrid().setHeaderVisible(false);

		// vesselViewer.getGrid().setHeaderVisible(true);

		createColumn(vesselClassViewer, "Vessel Classes", new VesselDescriptionFormatter(), false);
		vesselClassViewer.setContentProvider(new VesselClassContentProvider(this));
		hookDragSource(vesselClassViewer);
		return vesselClassViewer;
	}

	// private void createRulesViewer(final Composite parent) {
	// rulesViewer = new GridTreeViewer(parent, SWT.NONE);
	// GridViewerHelper.configureLookAndFeel(rulesViewer);
	// rulesViewer.getGrid().setHeaderVisible(true);
	//
	// createColumn(rulesViewer, "Rule", new RuleDescriptionFormatter(), false);
	//
	// rulesViewer.setContentProvider(new RulesViewerContentProvider());
	// hookOpenEditor(rulesViewer);
	// }

	private ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s) {
		return wrapInExpandable(composite, name, s, null);
	}

	private ExpandableComposite wrapInExpandable(final Composite composite, final String name, final Function<Composite, Control> s, @Nullable final Consumer<ExpandableComposite> customiser) {
		final ExpandableComposite expandableCompo = new ExpandableComposite(composite, SWT.NONE);
		expandableCompo.setExpanded(true);
		expandableCompo.setText(name);
		expandableCompo.setLayout(new GridLayout(1, false));

		final Control client = s.apply(expandableCompo);
		GridDataFactory.generate(expandableCompo, 2, 2);

		expandableCompo.setClient(client);
		expandableCompo.addExpansionListener(new IExpansionListener() {

			@Override
			public void expansionStateChanging(final ExpansionEvent e) {

			}

			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				composite.layout(true);
			}
		});

		if (customiser != null) {
			customiser.accept(expandableCompo);
		}
		return expandableCompo;
	}

	private Control createBaseCaseViewer(final Composite parent) {
		baseCaseViewer = new GridTreeViewer(parent, SWT.NONE | SWT.SINGLE);
		ColumnViewerToolTipSupport.enableFor(baseCaseViewer);

		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setHeaderVisible(true);
		baseCaseViewer.getGrid().setRowHeaderVisible(true);

		createColumn(baseCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__BUY_OPTION);
		{
			final GridViewerColumn gvc = new GridViewerColumn(baseCaseViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(final ViewerCell cell) {
					// TODO Auto-generated method stub

				}
			});
			this.baseCaseDiagram = new BaseCaseWiringDiagram(baseCaseViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(baseCaseViewer, "Sell", new SellOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__SELL_OPTION);
		createColumn(baseCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING);

		baseCaseViewer.getGrid().setCellSelectionEnabled(true);

		baseCaseViewer.setContentProvider(new BaseCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final BaseCaseContextMenuManager listener = new BaseCaseContextMenuManager(baseCaseViewer, OptionModellerView.this, mgr);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));
		baseCaseViewer.getGrid().addMenuDetectListener(listener);

		return baseCaseViewer.getGrid();
	}

	private Control createPartialCaseViewer(final Composite parent) {
		partialCaseViewer = new GridTreeViewer(parent, SWT.NONE | SWT.WRAP);
		ColumnViewerToolTipSupport.enableFor(partialCaseViewer);

		GridViewerHelper.configureLookAndFeel(partialCaseViewer);
		partialCaseViewer.getGrid().setHeaderVisible(true);
		partialCaseViewer.getGrid().setCellSelectionEnabled(true);

		partialCaseViewer.getGrid().setAutoHeight(true);
		partialCaseViewer.getGrid().setRowHeaderVisible(true);

		createColumn(partialCaseViewer, "Buy", new BuyOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__BUY_OPTIONS).getColumn().setWordWrap(true);

		{
			final GridViewerColumn gvc = new GridViewerColumn(partialCaseViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(final ViewerCell cell) {

				}
			});
			this.partialCaseDiagram = new PartialCaseWiringDiagram(partialCaseViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(partialCaseViewer, "Sell", new SellOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SELL_OPTIONS).getColumn().setWordWrap(true);
		createColumn(partialCaseViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING).getColumn().setWordWrap(true);

		partialCaseViewer.setContentProvider(new PartialCaseContentProvider());

		final MenuManager mgr = new MenuManager();

		final PartialCaseContextMenuManager listener = new PartialCaseContextMenuManager(partialCaseViewer, OptionModellerView.this, mgr);
		partialCaseViewer.getGrid().addMenuDetectListener(listener);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));

		hookDragSource(partialCaseViewer);
		return partialCaseViewer.getGrid();
	}

	private Control createResultsViewer(final Composite parent) {

		resultsViewer = new GridTreeViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(resultsViewer);
		GridViewerHelper.configureLookAndFeel(resultsViewer);
		resultsViewer.getGrid().setHeaderVisible(true);
		resultsViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		createColumn(resultsViewer, "Buy", new ResultsFormatterLabelProvider(new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION);
		{
			final GridViewerColumn gvc = new GridViewerColumn(resultsViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(final ViewerCell cell) {
					// TODO Auto-generated method stub

				}
			});
			this.resultsDiagram = new ResultsSetWiringDiagram(resultsViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(resultsViewer, "Sell", new ResultsFormatterLabelProvider(new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION);
		createColumn(resultsViewer, "Shipping", new ResultsFormatterLabelProvider(new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SHIPPING), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SHIPPING);
		createColumn(resultsViewer, "Details", new ResultsFormatterLabelProvider(new ResultDetailsDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL);

		final MenuManager mgr = new MenuManager();

		final ResultsContextMenuManager listener = new ResultsContextMenuManager(resultsViewer, baseCaseViewer, OptionModellerView.this, OptionModellerView.this, mgr);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));
		resultsViewer.getGrid().addMenuDetectListener(listener);

		resultsViewer.setContentProvider(new ResultsViewerContentProvider());
		final ESelectionService selectionService = getSite().getService(ESelectionService.class);
		resultsViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final List<Object> selection = new LinkedList<>();
				final ISelection s = resultsViewer.getSelection();
				if (s instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) s;
					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof AnalysisResultRow) {
							final AnalysisResultRow analysisResultRow = (AnalysisResultRow) o;
							final ResultContainer t = analysisResultRow.getResultDetails();
							if (t != null) {
								if (t.getCargoAllocation() != null) {
									selection.add(t.getCargoAllocation());
								}
								selection.addAll(t.getSlotAllocations());
								selection.addAll(t.getOpenSlotAllocations());
							}
						}
					}
				}
				if (selection != null) {
					selectionService.setPostSelection(new StructuredSelection(selection));
					packAll(rhsComposite);
				}
			}

		});

		return resultsViewer.getControl();
	}

	private Control createOptionsTreeViewer(final Composite parent) {

		optionsTreeViewer = new GridTreeViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(optionsTreeViewer);
		GridViewerHelper.configureLookAndFeel(optionsTreeViewer);
		// optionsTreeViewer.getGrid().setHeaderVisible(true);
		optionsTreeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		createColumn(optionsTreeViewer, "Option Models", new OptionTreeViewerFormatter(this), true);

		final MenuManager mgr = new MenuManager();

		final OptionsTreeViewerContextMenuManager listener = new OptionsTreeViewerContextMenuManager(optionsTreeViewer, OptionModellerView.this, OptionModellerView.this, mgr);
		// inputWants.add(model -> listener.setOptionAnalysisModel(model));
		optionsTreeViewer.getGrid().addMenuDetectListener(listener);

		optionsTreeViewer.setContentProvider(new OptionsTreeViewerContentProvider());
		optionsTreeViewer.addOpenListener(new OptionsTreeViewerOpenListener(this));
		return optionsTreeViewer.getControl();
	}

	private OptionAnalysisModel createDemoModel1() {

		final OptionAnalysisModel model = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();

		model.setName("Demo1");

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

		updateActions(getEditingDomain());
	}

	private GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final ICellRenderer renderer, final boolean isTree, final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
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
				super.dispose();
			}

		});
		return gvc;
	}

	private GridViewerColumn createColumn(final GridTreeViewer viewer, final String name, final CellLabelProvider labelProvider, final boolean isTree, final ETypedElement... pathObjects) {

		final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		gvc.getColumn().setTree(isTree);
		GridViewerHelper.configureLookAndFeel(gvc);
		gvc.getColumn().setText(name);
		gvc.getColumn().setWidth(200);
		gvc.getColumn().setDetail(true);
		gvc.getColumn().setSummary(true);
		gvc.setLabelProvider(labelProvider);
		return gvc;
	}

	private void hookOpenEditor(final @NonNull GridTreeViewer viewer) {

		viewer.getGrid().addMouseListener(new EditObjectMouseListener(viewer, OptionModellerView.this));
	}

	private enum SectionType {
		BUYS, MIDDLE, SELLS, VESSEL
	};

	private void refreshSections(final boolean layout, final EnumSet<SectionType> sections) {

		RunnerHelper.syncExec(() -> {

			// Coarse grained refresh method..
			if (sections.contains(SectionType.BUYS) || sections.contains(SectionType.SELLS) || sections.contains(SectionType.VESSEL)) {
				buyOptionsViewer.refresh();
				sellOptionsViewer.refresh();
				optionsTreeViewer.refresh();
				optionsTreeViewer.expandAll();
				shippingOptionsViewer.refresh();
				vesselViewer.refresh();
				vesselClassViewer.refresh();
				if (layout) {
					// packAll(vesselComposite);
					packAll(buyComposite);
				}
			}
			if (sections.contains(SectionType.MIDDLE)) {
				baseCaseViewer.refresh();
				if (getModel() != null) {
					baseCaseProftLabel.setText("Base P&&L: $");
				} else {
					baseCaseProftLabel.setText(String.format("Base P&&L: $---,---,---.--"));
				}
				partialCaseViewer.refresh();
				GridViewerHelper.recalculateRowHeights(partialCaseViewer.getGrid());
				resultsViewer.refresh();
				resultsViewer.expandAll();
				if (layout) {
					packAll(centralComposite);
				}
			}
			// if (sections.contains(SectionType.SELLS)) {
			// sellOptionsViewer.refresh();
			// if (layout) {
			// packAll(sellComposite);
			// }
			// }
			// if () {
			//
			// }
		});
	}

	public void packAll(final Control c) {
		if ((c instanceof Composite)) {
			final Composite composite = (Composite) c;
			((Composite) c).layout(true);
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

	private void updateActions(final EditingDomain editingDomain) {

		if (currentCommandStack != null) {
			currentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}
		if (undoAction != null) {
			undoAction.setEditingDomain(editingDomain);
			undoAction.setEnabled(editingDomain != null && editingDomain.getCommandStack().canUndo());
		}
		if (redoAction != null) {
			redoAction.setEditingDomain(editingDomain);
			redoAction.setEnabled(editingDomain != null && editingDomain.getCommandStack().canRedo());
		}
		if (editingDomain != null) {
			currentCommandStack = editingDomain.getCommandStack();
			currentCommandStack.addCommandStackListener(this);

			undoAction.update();
			redoAction.update();
		}
	}

	@Override
	public void commandStackChanged(final EventObject event) {
		if (undoAction != null) {
			undoAction.update();
		}
		if (redoAction != null) {
			redoAction.update();
		}
	}

	@Override
	public void dispose() {
		if (image_grey_add != null) {
			image_grey_add.dispose();
		}
		if (image_calculate != null) {
			image_calculate.dispose();
		}
		if (image_generate != null) {
			image_generate.dispose();
		}
		if (image_grey_calculate != null) {
			image_grey_calculate.dispose();
		}
		if (image_grey_generate != null) {
			image_grey_generate.dispose();
		}

		if (getModel() != null) {
			getModel().eAdapters().remove(refreshAdapter);
			getModel().eAdapters().remove(historyRenameAdaptor);
			setModel(null);
		}

		final CommandStack pCurrentCommandStack = currentCommandStack;
		if (pCurrentCommandStack != null) {
			pCurrentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
		}

		if (econsReport != null) {
			ContextInjectionFactory.invoke(econsReport, PreDestroy.class, econsReportContext);
			econsReport = null;
		}
		if (econsReportContext != null) {
			econsReportContext.dispose();
			econsReportContext = null;
		}
		if (pnlReport != null) {
			ContextInjectionFactory.invoke(pnlReport, PreDestroy.class, pnlReportContext);
			pnlReport = null;
		}
		if (pnlReportContext != null) {
			pnlReportContext.dispose();
			pnlReportContext = null;
		}

		super.dispose();
	}

	@Override
	public void setLocked(final boolean locked) {

		// Disable while locked.
		if (locked) {
			updateActions(null);
		} else {
			updateActions(getEditingDomain());
		}

		super.setLocked(locked);
	}

	private void createResultSetFork(final ResultSet rs) {
		final BaseCase bc = AnalyticsFactory.eINSTANCE.createBaseCase();
		final EList<BaseCaseRow> baseCase = bc.getBaseCase();
		for (final AnalysisResultRow analysisResultRow : rs.getRows()) {
			final BaseCaseRow bcr = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
			bcr.setBuyOption(analysisResultRow.getBuyOption());
			bcr.setSellOption(analysisResultRow.getSellOption());
			bcr.setShipping(analysisResultRow.getShipping());
		}
	}

	public OptionAnalysisModel getModel() {
		return model;
	}

	public void setModel(final OptionAnalysisModel model) {
		this.model = model;
	}

	private ICommandHandler commandHandler;

	private Composite rhsComposite;

	@Override
	public synchronized ICommandHandler getDefaultCommandHandler() {

		if (commandHandler == null) {
			final ICommandHandler superHandler = super.getDefaultCommandHandler();

			final EditingDomain domain = super.getEditingDomain();

			commandHandler = new ICommandHandler() {

				@Override
				public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {

					if (domain instanceof CommandProviderAwareEditingDomain) {
						final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) domain;
						commandProviderAwareEditingDomain.disableAdapters(model);
					}
					superHandler.handleCommand(command, target, feature);
					if (domain instanceof CommandProviderAwareEditingDomain) {
						final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) domain;
						commandProviderAwareEditingDomain.enableAdapters(model, false);
					}

				}

				@Override
				public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
					// TODO Auto-generated method stub
					return superHandler.getReferenceValueProviderProvider();
				}

				@Override
				public EditingDomain getEditingDomain() {
					// TODO Auto-generated method stub
					return superHandler.getEditingDomain();
				}
			};
		}

		return commandHandler;

	}

	private Pair<Object, IEclipseContext> createReportControl(final String componentId, final String title, final Composite parent, final Consumer<IEclipseContext> contextConsumer) {

		final Pair<Object, IEclipseContext> result = new Pair<>();

		final String filter = String.format("(partId=%s)", componentId);
		ServiceHelper.withAllServices(IInjectableE4ComponentFactory.class, filter, service -> {
			if (service != null) {
				final IEclipseContext ctx = getSite().getService(IEclipseContext.class);

				final ExpandableComposite expandable = wrapInExpandable(parent, title, (p) -> {
					final Composite componentComposite = new Composite(p, SWT.NONE);
					componentComposite.setLayout(new GridLayout(1, true));
					componentComposite.setLayoutData(GridDataFactory.fillDefaults()//
							.grab(true, true)//
							.hint(200, SWT.DEFAULT) //
							// .span(1, 1) //
							.align(SWT.FILL, SWT.FILL).create());

					final IEclipseContext componentContext = ctx.createChild();
					componentContext.set(Composite.class, componentComposite);

					contextConsumer.accept(componentContext);

					final Object component = ContextInjectionFactory.make(service.getComponentClass(), componentContext);

					componentContext.set(String.class, getViewSite().getId());
					ContextInjectionFactory.invoke(component, BindSelectionListener.class, componentContext);

					result.setBoth(component, componentContext);

					return componentComposite;
				});
				expandable.setExpanded(false);
				return false;
			}
			return true;
		});

		if (result.getFirst() != null) {
			return result;
		}
		return null;
	}
}
