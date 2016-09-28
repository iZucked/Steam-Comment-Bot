/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class OptionModellerView extends ScenarioInstanceView implements CommandStackListener {

	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	private OptionAnalysisModel model;

	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	private DialogValidationSupport validationSupport;

	// Callbacks for objects that need the current input
	private final List<Consumer<OptionAnalysisModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;
	private Link createNewLink;

	private ScrolledComposite centralScrolledComposite;
	private ScrolledComposite rhsScrolledComposite;
	private ShippingOptionsComponent shippingOptionsComponent;
	private OptionModelsComponent optionsModelComponent;

	private ICommandHandler commandHandler;

	private Composite rhsComposite;
	private ScrolledComposite lhsScrolledComposite;
	private BuyOptionsComponent buyComponent;
	private SellOptionsComponent sellComponent;
	private VesselsComponent vesselsComponent;
	private VesselClassesComponent vesselClassesComponent;

	private BaseCaseComponent baseCaseComponent;
	private ResultsComponent resultsComponent;
	private PartialCaseCompoment partialCaseComponent;

	private EmbeddedReportComponent econsComponent;
	private EmbeddedReportComponent pnlDetailsComponent;
	private WeakHashMap<OptionAnalysisModel, WeakReference<OptionAnalysisModel>> navigationHistory = new WeakHashMap<>();
	private WeakReference<OptionAnalysisModel> currentRoot = null;
	
	@Override
	public void createPartControl(final Composite parent) {

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getRootObject(), false));
		validationSupport.setValidationTargets(Collections.singleton(getModel()));
		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());
//		Composite dummy = new Composite(parent, SWT.BORDER);
//		dummy.setLayoutData(GridDataFactory.swtDefaults()//
//				.grab(false, true)//
//				.span(0,1)
//				.align(SWT.CENTER,SWT.TOP)
//				.create());
		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainComposite.setLayoutData(GridDataFactory.swtDefaults()//
				.grab(true, true)//
				.align(SWT.CENTER,SWT.TOP)
				.span(1,1)
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(false) //
				.numColumns(5) //
				.spacing(0, 0) //
//				.margins(100, 0)//
				.create());

		{
			lhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.V_SCROLL);
			lhsScrolledComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(3, 1) //
					.align(SWT.FILL, SWT.FILL).create());
			lhsScrolledComposite.setLayout(new GridLayout());
			lhsScrolledComposite.setExpandHorizontal(true);
			lhsScrolledComposite.setExpandVertical(true);
			lhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			lhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			// lhsScrolledComposite.setMinSize(400, 400);
			lhsComposite = new Composite(lhsScrolledComposite, SWT.NONE);
			lhsScrolledComposite.setContent(lhsComposite);
			lhsComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());
			lhsComposite.setLayout(GridLayoutFactory.fillDefaults()//
					.equalWidth(false) //
					.numColumns(1) //
					.spacing(0, 20) //
					.margins(0, 0)//
					.create());
			final IExpansionListener lhsExpansionListener = new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					lhsComposite.layout(true);
					lhsScrolledComposite.setMinSize(lhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

				}
			};

			final BiConsumer<AbstractSandboxComponent, Boolean> hook = (component, expanded) -> {
				component.createControls(lhsComposite, expanded, lhsExpansionListener, OptionModellerView.this);
				inputWants.addAll(component.getInputWants());
				disposables.add(() -> component.dispose());
			};

			{
				optionsModelComponent = new OptionModelsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(optionsModelComponent, true);
			}
			{
				buyComponent = new BuyOptionsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(buyComponent, true);
			}

			{
				sellComponent = new SellOptionsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(sellComponent, true);
			}

			{
				shippingOptionsComponent = new ShippingOptionsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(shippingOptionsComponent, true);
			}
			{
				vesselsComponent = new VesselsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(vesselsComponent, true);
			}
			{
				vesselClassesComponent = new VesselClassesComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(vesselClassesComponent, true);
			}
		}

		{
			centralScrolledComposite = new ScrolledComposite(mainComposite, SWT.H_SCROLL | SWT.V_SCROLL);
			centralScrolledComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.align(SWT.FILL, SWT.FILL) //
					.span(1, 1) //
					.create());
//			centralScrolledComposite.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 20).create());
			centralScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			centralScrolledComposite.setLayout(new GridLayout());
			centralScrolledComposite.setExpandHorizontal(true);
			centralScrolledComposite.setExpandVertical(true);
			// lhsScrolledComposite.setMinSize(400, 400);
			centralComposite = new Composite(centralScrolledComposite, SWT.NONE);
			// centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			// centralComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			centralScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			centralScrolledComposite.setContent(centralComposite);
			centralComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());

			centralComposite.setLayout(GridLayoutFactory.fillDefaults()
					.equalWidth(true) //
					.numColumns(1) //
					.spacing(0, 20) //
//					.margins(100, 0)//
					.create());
			
			final IExpansionListener centralExpansionListener = new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					centralComposite.layout(true);
					centralScrolledComposite.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			};

//			centralComposite.setLayout(new GridLayout(1, true));
			final BiConsumer<AbstractSandboxComponent, Boolean> hook = (component, expand) -> {
				component.createControls(centralComposite, expand, centralExpansionListener, OptionModellerView.this);
				inputWants.addAll(component.getInputWants());
				disposables.add(() -> component.dispose());
			};
			{
				baseCaseComponent = new BaseCaseComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(baseCaseComponent, true);
			}
			{
				partialCaseComponent = new PartialCaseCompoment(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(partialCaseComponent, true);
			}
			{
				resultsComponent = new ResultsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(resultsComponent, true);
			}
		}
		{

			rhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.V_SCROLL);
			rhsScrolledComposite.setLayoutData(GridDataFactory.fillDefaults()//
					.grab(false, true)//
					.hint(200, SWT.DEFAULT) //
					// .span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());

			rhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			rhsScrolledComposite.setLayout(new GridLayout());
			rhsScrolledComposite.setExpandHorizontal(true);
			rhsScrolledComposite.setExpandVertical(true);
			// lhsScrolledComposite.setMinSize(400, 400);
			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
			// centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			// centralComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setContent(rhsComposite);

			rhsComposite.setLayout(new GridLayout(1, true));
			final IExpansionListener rhsExpansionListener = new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					rhsComposite.layout(true);
					rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			};

			rhsComposite.setLayout(new GridLayout(1, true));

			{
				econsComponent = new EmbeddedReportComponent(OptionModellerView.this, validationErrors, () -> getModel(), "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport", "Econs",
						getViewSite(), childContext -> {
						});
				econsComponent.createControls(rhsComposite, true, rhsExpansionListener, OptionModellerView.this);
				inputWants.addAll(econsComponent.getInputWants());
				disposables.add(() -> econsComponent.dispose());

			}
			{

//				pnlDetailsComponent = new EmbeddedReportComponent(OptionModellerView.this, validationErrors, () -> getModel(), "com.mmxlabs.shiplingo.platform.reports.views.PNLDetailsReport", "P&&L",
//						getViewSite(), childContext -> {
//							final Options options = new Options("pnl", null, false);
//							childContext.set(Options.class, options);
//						});
//				pnlDetailsComponent.createControls(rhsComposite, false, rhsExpansionListener, OptionModellerView.this);
//				inputWants.addAll(pnlDetailsComponent.getInputWants());
//				disposables.add(() -> pnlDetailsComponent.dispose());

			}
		}
	

		final IActionBars actionBars = getViewSite().getActionBars();

		final ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		undoAction = new UndoAction();
		undoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		redoAction = new RedoAction();
		redoAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		updateActions(getEditingDomain());
		
		listenToScenarioSelection();

		packAll(mainComposite);

	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {
		doDisplayScenarioInstance(scenarioInstance, rootObject, null);

		updateActions(getEditingDomain());
	}

	void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable OptionAnalysisModel model) {

		if (errorLabel != null) {
			errorLabel.dispose();
			errorLabel = null;
		}
		if (createNewLink != null) {
			createNewLink.dispose();
			createNewLink = null;
		}

		// Some slightly hacky code to hide the editor if there is no scenario open
		if (scenarioInstance == null) {

			errorLabel = new Label(mainComposite.getParent(), SWT.NONE);
			errorLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			errorLabel.setText("No scenario selected");

			mainComposite.setVisible(false);
			mainComposite.getParent().layout(true);
			setInput(null);
		} else {
			mainComposite.setVisible(true);

			mainComposite.getParent().layout(true);

			if (model != null) {
				setModel(model);
			} else if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);

				if (analyticsModel.getOptionModels().isEmpty()) {
					setModel(null);
					createNewLink = new Link(mainComposite.getParent(), SWT.NONE);
					createNewLink.addListener(SWT.Selection, new Listener() {
					      public void handleEvent(Event event) {
							final OptionAnalysisModel model = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();

							model.setName("New sandbox");

							model.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
							model.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

							final CompoundCommand cmd = new CompoundCommand("Create sandbox");
							cmd.append(AddCommand.create(getEditingDomain(), analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), Collections.singletonList(model)));
							getEditingDomain().getCommandStack().execute(cmd);

							doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), model);
							
						}
					});
//					createNewLink.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
//					createNewLink.setText("No sandbox selected");
					createNewLink.setText("<A>Create new sandbox</A>");
					createNewLink.setToolTipText("Create new sandbox");
//					createNewLink.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));
//
//					createNewLink.addMouseListener(new MouseAdapter() {
//						@Override
//						public void mouseDown(MouseEvent e) {
//
//						
//						}
//					});

					mainComposite.setVisible(false);
					mainComposite.getParent().layout(true);
					setInput(null);
					return;
				} else {
					WeakReference<OptionAnalysisModel> root = getCurrentRoot();
					if (root == null || (root != null && root.get() == null)) {
						setCurrentRoot(new WeakReference<OptionAnalysisModel>(analyticsModel.getOptionModels().get(0)));
					}
					WeakReference<OptionAnalysisModel> modelToUse = navigationHistory.get(getCurrentRoot().get());
					setModel(modelToUse == null || (modelToUse != null && modelToUse.get() == null) ? rootOptionsModel : modelToUse.get());
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

	/**
	 * If the current model is deleted, then clear the input
	 */
	private final EContentAdapter deletedOptionModelAdapter = new EContentAdapter() {
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels()) {
				if (notification.getEventType() == Notification.REMOVE) {
					if (model != null && notification.getOldValue() == model) {
						displayScenarioInstance(getScenarioInstance());
						if (getCurrentRoot() == model) {
							setCurrentRoot(null);
						}
						navigationHistory.remove(model);
					} else if (rootOptionsModel != null && notification.getOldValue() == rootOptionsModel) {
						displayScenarioInstance(getScenarioInstance());
						if (getCurrentRoot() == rootOptionsModel) {
							setCurrentRoot(null);
						}
						navigationHistory.remove(rootOptionsModel);
					}
				}
			}
			if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getOptionAnalysisModel_Children()) {
				if (notification.getEventType() == Notification.REMOVE) {
					if (model != null && notification.getOldValue() == model) {
						displayScenarioInstance(getScenarioInstance());
					} else if (rootOptionsModel != null && notification.getOldValue() == rootOptionsModel) {
						displayScenarioInstance(getScenarioInstance());
					}
				}
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
			} else if (notification.getNotifier() instanceof ResultSet) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof AnalysisResultRow) {
				return new Pair<>(false, EnumSet.of(SectionType.MIDDLE));
			}

			return null;
		}
	};
	private Composite mainComposite;
	private Composite lhsComposite;
	private Composite centralComposite;
	private OptionAnalysisModel rootOptionsModel;
	private MMXRootObject rootObject;

	public void setInput(final @Nullable OptionAnalysisModel model) {
		if (this.getModel() != null) {
			if (this.getModel().eAdapters().contains(refreshAdapter)) {
				this.getModel().eAdapters().remove(refreshAdapter);
			}
		}
		if (rootOptionsModel != null) {
			rootOptionsModel.eAdapters().remove(deletedOptionModelAdapter);
			rootOptionsModel.eAdapters().remove(historyRenameAdaptor);
			optionsModelComponent.setInput(Collections.emptySet());
			rootOptionsModel = null;
		}
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
			rootObject = null;
		}

		this.setModel(model);

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getRootObject(), false));
		if (model != null) {
			validationSupport.setValidationTargets(Collections.singleton(model));
		} else {
			validationSupport.setValidationTargets(Collections.emptySet());
		}

		doValidate();

		inputWants.forEach(want -> want.accept(model));

		vesselsComponent.setInput(this);
		vesselClassesComponent.setInput(this);

		rootOptionsModel = getRootOptionsModel(model);
		if (rootOptionsModel != null) {
			rootOptionsModel.eAdapters().add(historyRenameAdaptor);
			optionsModelComponent.setInput(Collections.singleton(rootOptionsModel));
			// create a weak reference to avoid memory leaks
			WeakReference<OptionAnalysisModel> weakReferenceToModel = new WeakReference<>(model);
			navigationHistory.put(rootOptionsModel, weakReferenceToModel);
		}

		rootObject = getRootObject();
		if (rootObject != null) {
			rootObject.eAdapters().add(deletedOptionModelAdapter);
		}

		if (model != null) {
			setPartName("Sandbox " + model.getName());

			if (!model.eAdapters().contains(refreshAdapter)) {
				model.eAdapters().add(refreshAdapter);
			}
		} else {
			setPartName("Sandbox");
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

	@Override
	public void setFocus() {
		// ViewerHelper.setFocus(resultsViewer);

		updateActions(getEditingDomain());
	}

	public enum SectionType {
		BUYS, MIDDLE, SELLS, VESSEL
	};

	public void refreshSections(final boolean layout, final EnumSet<SectionType> sections) {

		RunnerHelper.syncExec(() -> {

			// Coarse grained refresh method..
			if (sections.contains(SectionType.BUYS) || sections.contains(SectionType.SELLS) || sections.contains(SectionType.VESSEL)) {
				buyComponent.refresh();
				sellComponent.refresh();

				optionsModelComponent.refresh();

				shippingOptionsComponent.refresh();
				vesselsComponent.refresh();
				vesselClassesComponent.refresh();
				if (layout) {
					// packAll(vesselComposite);
					packAll(lhsComposite);
					lhsScrolledComposite.setMinSize(lhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			}
			if (sections.contains(SectionType.MIDDLE)) {
				baseCaseComponent.refresh();
				partialCaseComponent.refresh();
				resultsComponent.refresh();
				if (layout) {
					packAll(centralComposite);
					centralScrolledComposite.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			}

			packAll(rhsComposite);
			rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		});
	}

	public void repackResults() {
		packAll(rhsComposite);
		rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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

			final Function<EObject, Boolean> checker = o -> {
				if (o == null) {
					return true;
				}
				if (validationErrors.containsKey(o)) {
					final IStatus s = validationErrors.get(o);
					if (s != null && s.getSeverity() >= IStatus.ERROR) {
						return false;
					}
				}
				return true;
			};
			if (model != null) {
				{
					boolean baseCaseValid = true;
					final BaseCase baseCase = model.getBaseCase();
					if (!checker.apply(baseCase)) {
						baseCaseValid = false;
					} else {
						for (final BaseCaseRow row : baseCase.getBaseCase()) {
							if (!checker.apply(row)) {
								baseCaseValid = false;
							} else {
								baseCaseValid &= checker.apply(row.getBuyOption());
								baseCaseValid &= checker.apply(row.getSellOption());
								baseCaseValid &= checker.apply(row.getShipping());
							}
							if (!baseCaseValid) {
								break;
							}
						}
					}
					baseCaseComponent.setBaseCaseValid(baseCaseValid);
				}
				{
					boolean partialCaseValid = true;
					final PartialCase partialCase = model.getPartialCase();
					if (!checker.apply(partialCase)) {
						partialCaseValid = false;
					} else {
						for (final PartialCaseRow row : partialCase.getPartialCase()) {
							if (!checker.apply(row)) {
								partialCaseValid = false;
							} else {
								for (final BuyOption b : row.getBuyOptions()) {
									partialCaseValid &= checker.apply(b);
								}

								for (final SellOption s : row.getSellOptions()) {
									partialCaseValid &= checker.apply(s);
								}

								for (final ShippingOption s : row.getShipping()) {
									partialCaseValid &= checker.apply(s);
								}
							}
							if (!partialCaseValid) {
								break;
							}
						}
					}
					partialCaseComponent.setPartialCaseValid(partialCaseValid);
				}
			}

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

		disposables.forEach(r -> r.run());

		if (model != null) {
			model.eAdapters().remove(refreshAdapter);
			model = null;
		}
		if (rootOptionsModel != null) {
			rootOptionsModel.eAdapters().remove(historyRenameAdaptor);
			rootOptionsModel = null;
		}
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
			rootObject = null;
		}

		final CommandStack pCurrentCommandStack = currentCommandStack;
		if (pCurrentCommandStack != null) {
			pCurrentCommandStack.removeCommandStackListener(this);
			currentCommandStack = null;
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

	public OptionAnalysisModel getModel() {
		return model;
	}

	public void setModel(final OptionAnalysisModel model) {
		this.model = model;
	}

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
					return superHandler.getReferenceValueProviderProvider();
				}

				@Override
				public EditingDomain getEditingDomain() {
					return superHandler.getEditingDomain();
				}

				@Override
				public ModelReference getModelReference() {
					return superHandler.getModelReference();
				}
			};
		}

		return commandHandler;

	}

	public WeakReference<OptionAnalysisModel> getCurrentRoot() {
		return currentRoot;
	}

	public void setCurrentRoot(WeakReference<OptionAnalysisModel> currentRoot) {
		this.currentRoot = currentRoot;
	}
	
//	public WeakReference<OptionAnalysisModel> findReferenceToRoot(OptionAnalysisModel root) {
//		for (OptionAnalysisModel optionAnalysisModel : navigationHistory.) {
//			if (optionAnalysisModel == root) {
//		}
//	}

}
