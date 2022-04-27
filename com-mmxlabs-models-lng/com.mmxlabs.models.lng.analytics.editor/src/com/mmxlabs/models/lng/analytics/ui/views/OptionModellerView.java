/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.util.ToBooleanFunction;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SandboxResult;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.services.IAnalyticsPushToCloudRunner;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.WhatIfEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.AbstractSandboxComponent;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.BuyOptionsComponent;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SellOptionsComponent;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.ShippingOptionsComponent;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.VesselEventOptionsComponent;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.SafeMMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceUtils;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public class OptionModellerView extends ScenarioInstanceView implements CommandStackListener {

	public static final String ID = "com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView";

	private UndoAction undoAction;
	private RedoAction redoAction;
	private CommandStack currentCommandStack;

	private OptionAnalysisModel currentModel;
	private final Map<Object, IStatus> validationErrors = new HashMap<>();

	private DialogValidationSupport validationSupport;

	// Callbacks for objects that need the current input
	private final List<Consumer<AbstractAnalysisModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;
	private Link createNewLink;

	private ScrolledComposite centralScrolledComposite;
	private ShippingOptionsComponent shippingOptionsComponent;
	private OptionModelsComponent optionsModelComponent;

	private ICommandHandler commandHandler;

	private ScrolledComposite lhsScrolledComposite;
	private BuyOptionsComponent buyComponent;
	private SellOptionsComponent sellComponent;
	private VesselEventOptionsComponent eventsComponent;

	private BaseCaseComponent baseCaseComponent;
	private PartialCaseCompoment partialCaseComponent;

	protected Collection<Consumer<Boolean>> lockedListeners = Sets.newConcurrentHashSet();

	private IAnalyticsPushToCloudRunner pushToCloudRunner;

	public OptionModellerView() {
		super();
		this.pushToCloudRunner = PlatformUI.getWorkbench().getService(IAnalyticsPushToCloudRunner.class);
	}

	@Override
	public void createPartControl(final Composite parent) {

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getScenarioDataProvider(), false, false));
		validationSupport.setValidationTargets(Collections.singleton(getModel()));
		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());

		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

		mainComposite.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				// .align(SWT.LEFT, SWT.TOP)
				.span(1, 1).create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(false) //
				.numColumns(5) //
				.spacing(0, 0) //
				.create());

		{
			lhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.H_SCROLL | SWT.V_SCROLL);
			lhsScrolledComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(3, 1) //
					.align(SWT.LEFT, SWT.FILL).create());
			lhsScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().create());
			lhsScrolledComposite.setExpandHorizontal(true);
			lhsScrolledComposite.setExpandVertical(true);
			lhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			lhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			lhsComposite = new Composite(lhsScrolledComposite, SWT.NONE);
			lhsScrolledComposite.setContent(lhsComposite);
			lhsComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(1, 1) //
					.align(SWT.LEFT, SWT.FILL).create());
			lhsComposite.setLayout(GridLayoutFactory.fillDefaults()//
					.equalWidth(false) //
					.numColumns(1) //
					.spacing(0, 20) //
					.margins(3, 3)//
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
				disposables.add(component::dispose);
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
				eventsComponent = new VesselEventOptionsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(eventsComponent, true);
			}

			{
				shippingOptionsComponent = new ShippingOptionsComponent(OptionModellerView.this, validationErrors, () -> getModel());
				hook.accept(shippingOptionsComponent, true);
			}
		}

		{
			centralScrolledComposite = new ScrolledComposite(mainComposite, SWT.H_SCROLL | SWT.V_SCROLL);
			centralScrolledComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(true, true)//
					.align(SWT.FILL, SWT.FILL) //
					.span(1, 1) //
					.create());
			centralScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			centralScrolledComposite.setLayout(new GridLayout());
			centralScrolledComposite.setExpandHorizontal(true);
			centralScrolledComposite.setExpandVertical(true);
			centralComposite = new Composite(centralScrolledComposite, SWT.NONE);
			centralScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			centralScrolledComposite.setContent(centralComposite);
			centralComposite.setLayoutData(GridDataFactory.swtDefaults()//
					.grab(false, true)//
					.span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());

			centralComposite.setLayout(GridLayoutFactory.fillDefaults().equalWidth(true) //
					.numColumns(1) //
					.spacing(0, 20) //
					.create());

			final IExpansionListener centralExpansionListener = new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					centralComposite.layout(true);
					centralScrolledComposite.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			};

			final BiConsumer<AbstractSandboxComponent, Boolean> hook = (component, expand) -> {
				component.createControls(centralComposite, expand, centralExpansionListener, OptionModellerView.this);
				inputWants.addAll(component.getInputWants());
				disposables.add(component::dispose);
			};

			{
				final Composite c = new Composite(centralComposite, SWT.NONE);
				GridDataFactory.generate(c, 1, 1);
				c.setLayout(new GridLayout(7, false));

				/*
				 * toggle for portfolio mode
				 */
				final Composite portfolioModeToggle = createPortfolioToggleComposite(c);
				GridDataFactory.defaultsFor(portfolioModeToggle).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(portfolioModeToggle);

				final Composite sandboxModeSelector = createSandboxModeComposite(c);
				GridDataFactory.defaultsFor(sandboxModeSelector).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(sandboxModeSelector);

				final Composite generateButton = createRunButton(c);
				GridDataFactory.defaultsFor(generateButton).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(generateButton);

				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CLOUD_OPTIMISATION) && pushToCloudRunner != null) {
					final Composite cloudRunButton = createCloudRunButton(c);
					GridDataFactory.defaultsFor(sandboxModeSelector).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(cloudRunButton);
				}

				final Composite displayButton = createDisplayButton(c);
				GridDataFactory.defaultsFor(displayButton).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(displayButton);

				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_SANDBOX)) {
					beModeToggle = createUseTargetPNLToggleComposite(c);
					GridDataFactory.defaultsFor(beModeToggle).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(beModeToggle);
				}
				final Composite deleteButton = createDeleteResultButton(c);
				GridDataFactory.defaultsFor(deleteButton).span(1, 1).align(SWT.LEFT, SWT.CENTER).applyTo(deleteButton);

			}

			{
				baseCaseComponent = new BaseCaseComponent(OptionModellerView.this, validationErrors, this::getModel);
				hook.accept(baseCaseComponent, true);
			}
			{
				partialCaseComponent = new PartialCaseCompoment(OptionModellerView.this, validationErrors, this::getModel);
				hook.accept(partialCaseComponent, true);
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
	protected void activeEditorChange(final ScenarioInstance instance) {
		if (instance == null) {
			displayScenarioInstance(null, null, null);

		} else {
			final ScenarioModelRecord mr = SSDataManager.Instance.getModelRecord(instance);
			try (IScenarioDataProvider sdp = mr.aquireScenarioDataProvider("ScenarioInstanceView:1")) {
				final MMXRootObject ro = sdp.getTypedScenario(MMXRootObject.class);

				if (Objects.equal(instance, this.scenarioInstance)) {
					return;
				}

				displayScenarioInstance(instance, ro, null);
			}
		}
	}

	public synchronized void openSandboxScenario(@Nullable final SandboxScenario sandboxScenario, boolean showResult) {
		displayScenarioInstance(sandboxScenario.getScenarioInstance(), sandboxScenario.getRootObject(), sandboxScenario.getSandboxModel());

		if (showResult) {
			OptionAnalysisModel m = sandboxScenario.getSandboxModel();
			BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
				if (m != null && m.getResults() != null) {
					final AnalyticsSolution data = new AnalyticsSolution(getScenarioInstance(), m.getResults(), m.getName());
					data.open();
				}
			});
		}
	}

	@Override
	protected synchronized void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable final Object target) {

		if (target != null && ((EObject) target).eContainer() != null && currentModel == target) {
			return;
		}
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

			OptionAnalysisModel newModel = null;

			if (target instanceof OptionAnalysisModel) {
				newModel = (OptionAnalysisModel) target;
			} else if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final @NonNull AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);

				if (analyticsModel.getOptionModels().isEmpty()) {
					createNewLink = new Link(mainComposite.getParent(), SWT.NONE);
					createNewLink.addListener(SWT.Selection, event -> {
						final OptionAnalysisModel tmpNewModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();

						tmpNewModel.setName("New sandbox");

						tmpNewModel.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
						tmpNewModel.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

						final CompoundCommand cmd = new CompoundCommand("Create sandbox");
						cmd.append(AddCommand.create(getEditingDomain(), analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels(), Collections.singletonList(tmpNewModel)));
						getEditingDomain().getCommandStack().execute(cmd);

						doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), tmpNewModel);
					});
					createNewLink.setText("<A>Create new sandbox</A>");
					createNewLink.setToolTipText("Create new sandbox");

					mainComposite.setVisible(false);
					mainComposite.getParent().layout(true);
					setInput(null);
					return;
				} else {
					// Is the current model part of the current scenario model?
					if (currentModel != null && ScenarioModelUtil.findScenarioModel(currentModel) == target) {
						newModel = currentModel;
					} else {
						newModel = analyticsModel.getOptionModels().get(0);
					}
				}
			}
			setInput(newModel);
		}

		updateActions(getEditingDomain());
	}

	private final @NonNull EContentAdapter historyRenameAdaptor = new EContentAdapter() {
		@Override
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
	private final @NonNull EContentAdapter deletedOptionModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.isTouch()) {
				return;
			}
			// Sneak a rename detector
			if (notification.getFeature() == MMXCorePackage.Literals.NAMED_OBJECT__NAME) {
				refreshSections(true, EnumSet.allOf(SectionType.class));
			} else if (notification.getFeature() == AnalyticsPackage.eINSTANCE.getAnalyticsModel_OptionModels()) {
				if (notification.getEventType() == Notification.REMOVE) {
					if (currentModel != null && notification.getOldValue() == currentModel) {
						displayScenarioInstance(getScenarioInstance(), getRootObject(), null);
					}
				}
			}
		}
	};

	private final @NonNull SafeMMXContentAdapter refreshAdapter = new SafeMMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			if (notification.isTouch()) {
				return;
			}
			doValidate();
			final Pair<Boolean, EnumSet<SectionType>> p = processNotification(notification);
			if (p != null) {
				refreshSections(p.getFirst(), p.getSecond());
				if (p.getFirst()) {
					refreshSections(p.getFirst(), p.getSecond());
				}
			}
		}

		@Override
		protected synchronized void missedNotifications(final List<Notification> missed) {
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

		private @Nullable Pair<Boolean, EnumSet<SectionType>> processNotification(final Notification notification) {
			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return null;
			}

			if (notification.getNotifier() == getModel() && notification.getFeature() == MMXCorePackage.Literals.NAMED_OBJECT__NAME) {
				setPartName("Sandbox " + getModel().getName());
				return null;
			}

			if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS) {
				return new Pair<>(true, EnumSet.of(SectionType.BUYS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS) {
				return new Pair<>(true, EnumSet.of(SectionType.SELLS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS) {
				return new Pair<>(true, EnumSet.of(SectionType.EVENTS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES) {
				return new Pair<>(true, EnumSet.of(SectionType.VESSEL));
			} else if (notification.getNotifier() instanceof ShippingOption || notification.getNotifier() instanceof VesselAvailability) {
				return new Pair<>(true, EnumSet.of(SectionType.VESSEL, SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULTS) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__OPTIONS) {
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
			} else if (notification.getFeature() == MMXCorePackage.Literals.NAMED_OBJECT__NAME && notification.getNotifier() instanceof OptionAnalysisModel) {
				return new Pair<>(false, EnumSet.of(SectionType.BUYS));
			} else if (notification.getNotifier() instanceof VesselEventOption) {
				return new Pair<>(false, EnumSet.of(SectionType.EVENTS, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof SellOption) {
				return new Pair<>(false, EnumSet.of(SectionType.SELLS, SectionType.MIDDLE));
			} else if (notification.getNotifier() instanceof ShippingOption) {
				return new Pair<>(false, EnumSet.of(SectionType.VESSEL, SectionType.MIDDLE));
			}

			return null;
		}
	};
	private Composite mainComposite;
	private Composite lhsComposite;
	private Composite centralComposite;

	private boolean partialCaseValid;

	private Composite beModeToggle;

	public void setInput(final @Nullable OptionAnalysisModel model) {

		if (currentModel != null) {
			if (currentModel.eAdapters().contains(refreshAdapter)) {
				currentModel.eAdapters().remove(refreshAdapter);
			}
			currentModel.eAdapters().remove(historyRenameAdaptor);
			optionsModelComponent.setInput(Collections.emptySet());
			currentModel = null;
		}
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
		}

		this.currentModel = model;
		this.targetObject = model;

		validationSupport = new DialogValidationSupport(new DefaultExtraValidationContext(getScenarioDataProvider(), false, false));
		if (model != null) {
			validationSupport.setValidationTargets(Collections.singleton(model));
		} else {
			validationSupport.setValidationTargets(Collections.emptySet());
		}

		doValidate();

		inputWants.forEach(want -> want.accept(model));

		// Disable auto-open results. use the display button!
		// if (model != null && model.getResults() != null) {
		// final AnalyticsSolution data = new AnalyticsSolution(getScenarioInstance(),
		// model.getResults(), model.getName());
		// data.open();
		// }

		if (currentModel != null) {
			optionsModelComponent.setInput(Collections.singleton(currentModel));
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
			if (!model.eAdapters().contains(historyRenameAdaptor)) {
				model.eAdapters().add(historyRenameAdaptor);
			}
		} else {
			setPartName("Sandbox");
		}

		refreshSections(true, EnumSet.allOf(SectionType.class));

	}

	@Override
	public void setFocus() {
		updateActions(getEditingDomain());
	}

	public enum SectionType {
		BUYS, MIDDLE, SELLS, VESSEL, EVENTS
	};

	public void refreshSections(final boolean layout, final EnumSet<SectionType> sections) {

		RunnerHelper.syncExec(() -> {

			// Coarse grained refresh method..
			if (sections.contains(SectionType.BUYS) || sections.contains(SectionType.EVENTS) || sections.contains(SectionType.SELLS) || sections.contains(SectionType.VESSEL)) {
				buyComponent.refresh();
				sellComponent.refresh();
				eventsComponent.refresh();

				optionsModelComponent.refresh();

				shippingOptionsComponent.refresh();
				if (layout) {
					packAll(lhsComposite);
					lhsScrolledComposite.setMinSize(lhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			}
			if (sections.contains(SectionType.MIDDLE)) {
				baseCaseComponent.refresh();
				partialCaseComponent.refresh();
				if (layout) {
					packAll(centralComposite);
					centralScrolledComposite.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			}
		});
	}

	public void packAll(final Control c) {
		if ((c instanceof Composite composite)) {
			composite.layout(true);
			for (final Control child : composite.getChildren()) {
				packAll(child);
			}
			composite.layout(true);
		}
	}

	private void doValidate() {
		try {
			pushExtraValidationContext(validationSupport.getValidationContext());
			final IStatus status = validationSupport.validate();

			validationErrors.clear();
			validationSupport.processStatus(status, validationErrors);

			final ToBooleanFunction<EObject> checker = o -> {
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
			if (currentModel != null) {
				boolean l_partialCaseValid = true;
				final PartialCase partialCase = currentModel.getPartialCase();
				if (!checker.accept(partialCase)) {
					l_partialCaseValid = false;
				} else {
					for (final PartialCaseRow row : partialCase.getPartialCase()) {
						if (!checker.accept(row)) {
							l_partialCaseValid = false;
						} else {
							for (final BuyOption b : row.getBuyOptions()) {
								l_partialCaseValid &= checker.accept(b);
							}

							for (final SellOption s : row.getSellOptions()) {
								l_partialCaseValid &= checker.accept(s);
							}

							for (final ShippingOption s : row.getShipping()) {
								l_partialCaseValid &= checker.accept(s);
							}
						}
						if (!l_partialCaseValid) {
							break;
						}
					}
				}
				this.partialCaseValid = l_partialCaseValid;
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

			if (undoAction != null) {
				undoAction.update();
			}
			if (redoAction != null) {
				redoAction.update();
			}
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

		disposables.forEach(Runnable::run);

		if (currentModel != null) {
			currentModel.eAdapters().remove(refreshAdapter);
			currentModel.eAdapters().remove(historyRenameAdaptor);
			currentModel = null;
		}
		if (rootObject != null) {
			rootObject.eAdapters().remove(deletedOptionModelAdapter);
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

		final boolean thisLocked = locked || (scenarioInstance != null && scenarioInstance.isCloudLocked());

		// Disable while locked.
		if (thisLocked) {
			updateActions(null);
		} else {
			updateActions(getEditingDomain());
		}

		baseCaseComponent.setLocked(thisLocked);
		partialCaseComponent.setLocked(thisLocked);
		buyComponent.setLocked(locked);
		sellComponent.setLocked(thisLocked);
		eventsComponent.setLocked(thisLocked);
		shippingOptionsComponent.setLocked(thisLocked);

		lockedListeners.forEach(e -> e.accept(thisLocked));

		super.setLocked(locked);
	}

	public OptionAnalysisModel getModel() {
		return currentModel;
	}

	@Override
	public synchronized ICommandHandler getDefaultCommandHandler() {

		if (commandHandler == null) {
			final ICommandHandler superHandler = super.getDefaultCommandHandler();

			final EditingDomain domain = super.getEditingDomain();

			commandHandler = new ICommandHandler() {

				@Override
				public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
					CommandProviderAwareEditingDomain.withAdaptersDisabled(domain, currentModel, () -> {
						superHandler.handleCommand(command, target, feature);
					});
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

	private Composite createRunButton(final Composite parent) {
		//

		final Composite generateComposite = new Composite(parent, SWT.NONE);
		GridDataFactory.generate(generateComposite, 2, 1);

		generateComposite.setLayout(new GridLayout(1, true));

		final Label generateButton = new Label(generateComposite, SWT.NONE);
		generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(false, false).create());
		generateButton.setImage(CommonImages.getImage(IconPaths.Play, IconMode.Enabled));
		generateButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				if (isLocked()) {
					return;
				}

				{
					final ADPModel adpModel = ScenarioModelUtil.getADPModel(getScenarioDataProvider());
					if (adpModel != null) {
						MessageDialog.openError(getShell(), "Unable to evaluate", "Sandbox scenarios cannot be used with ADP scenarios");
						// Cannot use sandbox with ADP
						return;
					}

				}

				final OptionAnalysisModel m = currentModel;
				if (m != null) {
					final int mode = m.getMode();
					if (mode == SandboxModeConstants.MODE_OPTIMISE && !m.getBaseCase().isKeepExistingScenario()) {
						// BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(),
						MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Error running sandbox", "Optimise mode needs portfolio link enabled");
					} else {

						if (mode != SandboxModeConstants.MODE_DERIVE || partialCaseValid) {
							BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
//<<<<<<< HEAD
//								switch (mode) {
//								case SandboxModeConstants.MODE_OPTIONISE:
//									WhatIfEvaluator.doOptimise(OptionModellerView.this, m, true);
//									break;
//								case SandboxModeConstants.MODE_OPTIMISE:
//									WhatIfEvaluator.doOptimise(OptionModellerView.this, m, false);
//									break;
//								case SandboxModeConstants.MODE_DERIVE:
//								default:
//									if (m.getCommodityCurves().isEmpty()) {
//										WhatIfEvaluator.evaluate(OptionModellerView.this, m);
//									} else {
//										WhatIfEvaluator.doPriceSensitivity(OptionModellerView.this, m);
//									}
//									break;
//								}
								WhatIfEvaluator.runSandbox(OptionModellerView.this, m);
								if (m != null && m.getResults() != null) {
									final AnalyticsSolution data = new AnalyticsSolution(getScenarioInstance(), m.getResults(), m.getName());
									data.open();
								}
							});
						}
					}
				}
			}
		});

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(generateButton, btn -> btn.setEnabled(currentModel != null && !locked)));

		inputWants.add(m -> generateButton.setEnabled(m != null && !isLocked()));

		return generateComposite;
	}

	private Composite createCloudRunButton(final Composite parent) {
		//
		final Composite generateComposite = new Composite(parent, SWT.NONE);
		GridDataFactory.generate(generateComposite, 2, 1);

		generateComposite.setLayout(new GridLayout(1, true));

		final Label generateButton = new Label(generateComposite, SWT.NONE);
		generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(false, false).create());
		generateButton.setImage(CommonImages.getImage(IconPaths.CloudPlay_24, IconMode.Enabled));
		generateButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				if (isLocked()) {
					return;
				}

				{
					final ADPModel adpModel = ScenarioModelUtil.getADPModel(getScenarioDataProvider());
					if (adpModel != null) {
						MessageDialog.openError(getShell(), "Unable to evaluate", "Sandbox scenarios cannot be used with ADP scenarios");
						// Cannot use sandbox with ADP
						return;
					}

				}

				final OptionAnalysisModel m = currentModel;
				if (m != null) {
					final int mode = m.getMode();
					if (mode == SandboxModeConstants.MODE_OPTIMISE && !m.getBaseCase().isKeepExistingScenario()) {
						// BusyIndicator.showWhgile(PlatformUI.getWorkbench().getDisplay(),
						MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Error running sandbox", "Optimise mode needs portfolio link enabled");
					} else {

						if (mode != SandboxModeConstants.MODE_DERIVE || partialCaseValid) {
							if (pushToCloudRunner != null) {
								pushToCloudRunner.run(scenarioInstance, m);
							}
							// BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
							// switch (mode) {
							// case SandboxModeConstants.MODE_OPTIONISE:
							// WhatIfEvaluator.doOptimise(OptionModellerView.this, m, true);
							// break;
							// case SandboxModeConstants.MODE_OPTIMISE:
							// WhatIfEvaluator.doOptimise(OptionModellerView.this, m, false);
							// break;
							// case SandboxModeConstants.MODE_DERIVE:
							// default:
							// WhatIfEvaluator.evaluate(OptionModellerView.this, m);
							// break;
							// }
							// if (m != null && m.getResults() != null) {
							// final AnalyticsSolution data = new AnalyticsSolution(getScenarioInstance(),
							// m.getResults(), m.getName());
							// data.open();
							// }
							// });
						}
					}
				}
			}
		});

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(generateButton, btn -> btn.setEnabled(currentModel != null && !locked)));

		inputWants.add(m -> generateButton.setEnabled(m != null && !isLocked()));

		return generateComposite;
	}

	private Composite createDisplayButton(final Composite parent) {
		//
		final Composite generateComposite = new Composite(parent, SWT.NONE);
		GridDataFactory.generate(generateComposite, 2, 1);

		generateComposite.setLayout(new GridLayout(1, true));

		final Label generateButton = new Label(generateComposite, SWT.NONE);
		generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(false, false).create());
		generateButton.setImage(CommonImages.getImage(IconPaths.Console, IconMode.Enabled));

		generateButton.setToolTipText("Display results");

		generateButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				if (isLocked()) {
					return;
				}
				final OptionAnalysisModel m = currentModel;
				if (m != null) {
					BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
						if (m != null) {
							if (m.getResults() != null) {
								final AnalyticsSolution data = new AnalyticsSolution(getScenarioInstance(), m.getResults(), m.getName());
								data.open();
							} else {
								// No results, so show a blank case
								final SandboxResult emptyResult = AnalyticsFactory.eINSTANCE.createSandboxResult();
								emptyResult.setName(m.getName());
								emptyResult.setUseScenarioBase(false);
								final AnalyticsSolution data = new AnalyticsSolution(getScenarioInstance(), emptyResult, m.getName());
								data.open();
							}
						}
					});
				}
			}
		});

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(generateButton, btn -> btn.setEnabled(currentModel != null && !locked)));

		inputWants.add(m -> generateButton.setEnabled(m != null && !isLocked()));

		return generateComposite;
	}

	private Composite createDeleteResultButton(final Composite parent) {
		//
		final Composite generateComposite = new Composite(parent, SWT.NONE);
		GridDataFactory.generate(generateComposite, 1, 1);
		generateComposite.setLayout(new GridLayout(1, true));

		final Label generateButton = new Label(generateComposite, SWT.NONE);
		generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.CENTER).grab(false, false).create());

		generateButton.setImage(CommonImages.getImage(IconPaths.Delete, IconMode.Enabled));

		generateButton.setToolTipText("Delete current results");

		generateButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(final MouseEvent e) {
				if (isLocked()) {
					return;
				}
				final OptionAnalysisModel m = currentModel;
				if (m != null) {
					BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
						if (m != null && m.getResults() != null) {

							final ScenarioResult sr = new ScenarioResultImpl(getScenarioInstance());
							ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, sp -> sp.deselect(sr, false));

							final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
							eventBroker.post(ScenarioServiceUtils.EVENT_CLOSING_ANALYTICS_SOLUTION, new AnalyticsSolution(getScenarioInstance(), m.getResults(), "dummy-title"));

							final CompoundCommand cmd = new CompoundCommand("Delete sandbox results");
							cmd.append(DeleteCommand.create(getEditingDomain(), m.getResults()));
							getEditingDomain().getCommandStack().execute(cmd);
						}
					});
				}
			}
		});

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(generateButton, btn -> btn.setEnabled(currentModel != null && !locked)));

		inputWants.add(m -> generateButton.setEnabled(m != null && !isLocked()));

		return generateComposite;
	}

	private Composite createSandboxModeComposite(final Composite composite) {
		final Composite matching = new Composite(composite, SWT.ALL);
		final GridLayout gridLayoutRadiosMatching = new GridLayout(3, false);
		matching.setLayout(gridLayoutRadiosMatching);
		final GridData gdM = new GridData(SWT.LEFT, SWT.BEGINNING, false, false);
		gdM.horizontalSpan = 2;
		matching.setLayoutData(gdM);
		new Label(matching, SWT.NONE).setText("Mode:");
		final Combo combo = new Combo(matching, SWT.DROP_DOWN);
		// final Button matchingButton = new Button(matching, SWT.CHECK | SWT.LEFT);
		combo.setItems("Define", "Optimise", "Optionise");

		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (isLocked()) {
					return;
				}
				final OptionAnalysisModel m = currentModel;
				if (m != null) {
					final int mode = combo.getSelectionIndex();
					baseCaseComponent.setMode(mode);
					partialCaseComponent.setVisible(mode == 0);
					if (beModeToggle != null) {
						beModeToggle.setVisible(mode != 1);
					}
					getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), m, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__MODE, mode), m,
							AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__MODE);
					refreshSections(true, EnumSet.of(SectionType.MIDDLE));
				} else {
					baseCaseComponent.setMode(-1);
				}

			}
		});

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(combo, c -> c.setEnabled(!locked)));

		inputWants.add(m -> matching.setEnabled(m != null));

		// FIXME: This control does not respond to e.g. Undo() calls.
		// Need to hook up explicitly to the refresh adapter

		inputWants.add(am -> {
			if (am instanceof OptionAnalysisModel) {
				final OptionAnalysisModel optionAnalysisModel = (OptionAnalysisModel) am;
				final int mode = optionAnalysisModel.getMode();
				combo.select(mode);
				partialCaseComponent.setVisible(mode == SandboxModeConstants.MODE_DERIVE);
				if (beModeToggle != null) {
					beModeToggle.setVisible(mode != SandboxModeConstants.MODE_OPTIMISE);
				}
				baseCaseComponent.setMode(mode);
				refreshSections(true, EnumSet.of(SectionType.MIDDLE));
			} else {
				baseCaseComponent.setMode(-1);
			}
		});

		return matching;
	}

	private Composite createPortfolioToggleComposite(final Composite composite) {
		final Composite matching = new Composite(composite, SWT.ALL);
		final GridLayout gridLayoutRadiosMatching = new GridLayout(3, false);
		matching.setLayout(gridLayoutRadiosMatching);
		final GridData gdM = new GridData(SWT.LEFT, SWT.BEGINNING, false, false);
		gdM.horizontalSpan = 2;
		matching.setLayoutData(gdM);
		new Label(matching, SWT.NONE).setText("Portfolio link");
		final Button matchingButton = new Button(matching, SWT.CHECK | SWT.LEFT);
		matchingButton.setSelection(false);
		matchingButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (isLocked()) {
					return;
				}
				final OptionAnalysisModel m = currentModel;
				if (m != null) {

					final CompoundCommand cmd = new CompoundCommand();

					cmd.append(SetCommand.create(getEditingDomain(), m.getBaseCase(), AnalyticsPackage.Literals.BASE_CASE__KEEP_EXISTING_SCENARIO, matchingButton.getSelection()));
					cmd.append(SetCommand.create(getEditingDomain(), m.getPartialCase(), AnalyticsPackage.Literals.PARTIAL_CASE__KEEP_EXISTING_SCENARIO, matchingButton.getSelection()));

					getDefaultCommandHandler().handleCommand(cmd, null, null);
					refreshSections(false, EnumSet.of(SectionType.MIDDLE));
				}
			}
		});
		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(matchingButton, btn -> btn.setEnabled(!locked)));
		inputWants.add(m -> matching.setEnabled(m != null));

		inputWants.add(am -> {
			if (am instanceof OptionAnalysisModel) {
				final OptionAnalysisModel optionAnalysisModel = (OptionAnalysisModel) am;
				matchingButton.setSelection(optionAnalysisModel.getBaseCase().isKeepExistingScenario());
			}
		});
		return matching;
	}

	private Composite createUseTargetPNLToggleComposite(final Composite composite) {
		final Composite matching = new Composite(composite, SWT.ALL);
		final GridLayout gridLayoutRadiosMatching = new GridLayout(3, false);
		matching.setLayout(gridLayoutRadiosMatching);
		final GridData gdM = new GridData(SWT.LEFT, SWT.BEGINNING, false, false);
		gdM.horizontalSpan = 2;
		matching.setLayoutData(gdM);
		final Label l = new Label(matching, SWT.NONE);
		l.setText("Starting point P&&L B/E");
		l.setToolTipText(
				"When checked use the portfolio P&&L from the starting point scenario to calculate the B/E prices. Otherwise a point-to-point B/E is calculated. Does not apply when optimising.");
		final Button matchingButton = new Button(matching, SWT.CHECK | SWT.LEFT);
		matchingButton.setSelection(false);
		matchingButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (isLocked()) {
					return;
				}
				final OptionAnalysisModel m = currentModel;
				if (currentModel != null) {
					getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), m, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL, matchingButton.getSelection()),
							m, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL);
				}
			}

		});

		lockedListeners.add(locked -> RunnerHelper.runAsyncIfControlValid(matchingButton, btn -> btn.setEnabled(!locked)));
		inputWants.add(m -> matching.setEnabled(m != null));

		// FIXME: This control does not respond to e.g. Undo() calls.
		// Need to hook up explicitly to the refresh adapter

		inputWants.add(am -> {
			if (am instanceof OptionAnalysisModel) {
				final OptionAnalysisModel optionAnalysisModel = (OptionAnalysisModel) am;
				matchingButton.setSelection(optionAnalysisModel.isUseTargetPNL());
			}
		});
		return matching;
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final OptionAnalysisModel model = findSandboxInObjectTree(dcsd.getTarget());
			if (model != null) {
				if (currentModel != model && currentModel.eContainer() instanceof AnalyticsModel && ((AnalyticsModel) currentModel.eContainer()).getOptionModels().contains(model)) {
					displayScenarioInstance(this.scenarioInstance, this.rootObject, model);
				}
			}
		}
	}

	private @Nullable OptionAnalysisModel findSandboxInObjectTree(final @Nullable EObject node) {

		if (node instanceof OptionAnalysisModel) {
			return (OptionAnalysisModel) node;
		}
		if (node == null) {
			return null;
		}

		return findSandboxInObjectTree(node.eContainer());
	}

}
