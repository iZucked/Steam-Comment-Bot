/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.time.YearMonth;
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
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.RedoAction;
import org.eclipse.emf.edit.ui.action.UndoAction;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.SwapValueMatrixSandboxEvaluator;
import com.mmxlabs.models.lng.cargo.VesselCharter;
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

public class ValueMatrixModellerView extends ScenarioInstanceView implements CommandStackListener {

	public static final String ID = "com.mmxlabs.models.lng.analytics.ui.views.valuematrix.ValueMatrixModellerView";

	private UndoAction undoAction;
	private RedoAction redoAction;
	private HighlightAction highlightAction;
	private CommandStack currentCommandStack;

	private SwapValueMatrixModel currentModel;
	private final Map<Object, Collection<IStatus>> validationErrors = new HashMap<>();

	private DialogValidationSupport validationSupport;

	// Callbacks for objects that need the current input
	private final List<Consumer<SwapValueMatrixModel>> inputWants = new LinkedList<>();
	private final List<Runnable> disposables = new LinkedList<>();
	private Label errorLabel;
	private Link createNewLink;

	private ScrolledComposite centralScrolledComposite;

	private ICommandHandler commandHandler;

	private ValueMatrixDataComponent valueMatrixDataComponent;
	private ValueMatrixResultsComponent valueMatrixResultsComponent;

	protected Collection<Consumer<Boolean>> lockedListeners = Sets.newConcurrentHashSet();

	public ValueMatrixModellerView() {
		super();
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
				.span(1, 1).create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(false) //
				.numColumns(1) //
				.spacing(10, 10) //
				.create());
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
					.margins(10, 10) //
					.spacing(20, 20) //
					.create());
			final Runnable recalculateScrollbar = () -> {
				centralComposite.layout(true);
				centralScrolledComposite.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			};

			final IExpansionListener centralExpansionListener = new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					centralComposite.layout(true);
					centralScrolledComposite.setMinSize(centralComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			};

			final BiConsumer<AbstractValueMatrixComponent, Boolean> hook = (component, expand) -> {
				component.createControls(centralComposite, expand, centralExpansionListener, ValueMatrixModellerView.this);
				inputWants.addAll(component.getInputWants());
				disposables.add(component::dispose);
			};

			{
				valueMatrixDataComponent = new ValueMatrixDataComponent(ValueMatrixModellerView.this, validationErrors, this::getModel);
				hook.accept(valueMatrixDataComponent, true);
				lockedListeners.add(locked -> valueMatrixDataComponent.setEditingLock(locked));
			}
			{
				valueMatrixResultsComponent = new ValueMatrixResultsComponent(ValueMatrixModellerView.this, this::getModel, recalculateScrollbar);
				hook.accept(valueMatrixResultsComponent, true);
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

		{
			final Action runAction = createRunAction();
			runAction.setEnabled(true);
			getViewSite().getActionBars().getToolBarManager().add(runAction);

			final Action deleteResultsAction = createDeleteResultsAction(centralComposite);
			getViewSite().getActionBars().getToolBarManager().add(deleteResultsAction);

			highlightAction = new HighlightAction(valueMatrixResultsComponent);
			getViewSite().getActionBars().getToolBarManager().add(highlightAction);

			final Action packAction = createPackAction();
			packAction.setEnabled(true);
			getViewSite().getActionBars().getToolBarManager().add(packAction);

			lockedListeners.add(locked -> RunnerHelper.runNowOrAsync(() -> {
				final IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
				final boolean actionState = currentModel != null && !locked;
				runAction.setEnabled(actionState);
				deleteResultsAction.setEnabled(actionState);
				toolbarManager.update(true);
			}));
			getViewSite().getActionBars().getToolBarManager().update(true);

		}

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
			if (mr != null) {
				try (IScenarioDataProvider sdp = mr.aquireScenarioDataProvider("ScenarioInstanceView:1")) {
					final MMXRootObject ro = sdp.getTypedScenario(MMXRootObject.class);

					if (Objects.equal(instance, this.scenarioInstance)) {
						return;
					}

					displayScenarioInstance(instance, ro, null);
				}
			}
		}
	}

	public synchronized void openValueMatrixScenario(@Nullable final ValueMatrixScenario valueMatrixScenario) {
		displayScenarioInstance(valueMatrixScenario.getScenarioInstance(), valueMatrixScenario.getRootObject(), valueMatrixScenario.getValueMatrixModel());
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

			SwapValueMatrixModel newModel = null;

			if (target instanceof @NonNull final SwapValueMatrixModel swapValueMatrixModel) {
				newModel = swapValueMatrixModel;
			} else if (rootObject instanceof @NonNull final LNGScenarioModel lngScenarioModel) {
				@NonNull
				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(lngScenarioModel);
				if (analyticsModel.getSwapValueMatrixModels().isEmpty()) {
					createNewLink = new Link(mainComposite.getParent(), SWT.NONE);
					createNewLink.addListener(SWT.Selection, event -> {
						final SwapValueMatrixModel tmpNewModel = AnalyticsFactory.eINSTANCE.createSwapValueMatrixModel();

						tmpNewModel.setName("New value matrix");

						final CompoundCommand cmd = new CompoundCommand("Create value matrix");
						cmd.append(AddCommand.create(getEditingDomain(), analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_SwapValueMatrixModels(), Collections.singletonList(tmpNewModel)));
						getEditingDomain().getCommandStack().execute(cmd);

						doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), tmpNewModel);
					});
					createNewLink.setText("<A>Create new value matrix</A>");
					createNewLink.setToolTipText("Create new value matrix");

					mainComposite.setVisible(false);
					mainComposite.getParent().layout(true);
					setInput(null);
					return;
				} else {
					// Is the current model part of the current scenario model?
					if (currentModel != null && ScenarioModelUtil.findScenarioModel(currentModel) == target) {
						newModel = currentModel;
					} else {
						newModel = analyticsModel.getSwapValueMatrixModels().get(0);
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
				setPartName(getModel().getName());
				return null;
			}
			if (notification.getNotifier() == getModel() && notification.getFeature() == AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT) {
				return new Pair<>(true, EnumSet.of(SectionType.MIDDLE));
			}

			if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS) {
				return new Pair<>(true, EnumSet.of(SectionType.BUYS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS) {
				return new Pair<>(true, EnumSet.of(SectionType.SELLS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS) {
				return new Pair<>(true, EnumSet.of(SectionType.EVENTS));
			} else if (notification.getFeature() == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES) {
				return new Pair<>(true, EnumSet.of(SectionType.VESSEL));
			} else if (notification.getNotifier() instanceof ShippingOption || notification.getNotifier() instanceof VesselCharter) {
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

	private Button portfolioLinkBtn;

	public void setInput(@Nullable final SwapValueMatrixModel model) {

		if (currentModel != null) {
			if (currentModel.eAdapters().contains(refreshAdapter)) {
				currentModel.eAdapters().remove(refreshAdapter);
			}
			currentModel.eAdapters().remove(historyRenameAdaptor);
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

		rootObject = getRootObject();
		if (rootObject != null) {
			rootObject.eAdapters().add(deletedOptionModelAdapter);
		}

		if (model != null) {
			setPartName(model.getName());

			if (!model.eAdapters().contains(refreshAdapter)) {
				model.eAdapters().add(refreshAdapter);
			}
			if (!model.eAdapters().contains(historyRenameAdaptor)) {
				model.eAdapters().add(historyRenameAdaptor);
			}
		} else {
			setPartName("Value matrix");
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
//				buyComponent.refresh();
//				sellComponent.refresh();
//				eventsComponent.refresh();

//				optionsModelComponent.refresh();

//				shippingOptionsComponent.refresh();
//				if (layout) {
//					packAll(lhsComposite);
//					lhsScrolledComposite.setMinSize(lhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//				}
			}
			if (sections.contains(SectionType.MIDDLE)) {
				valueMatrixDataComponent.refresh();
				valueMatrixResultsComponent.refresh();
//				baseCaseComponent.refresh();
//				partialCaseComponent.refresh();
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
			validationSupport.processStatus(validationErrors, status);

			if (currentModel != null) {
				boolean l_partialCaseValid = true;
//				final PartialCase partialCase = currentModel.getPartialCase();
//				if (!checker.accept(partialCase)) {
//					l_partialCaseValid = false;
//				} else {
//					for (final PartialCaseRow row : partialCase.getPartialCase()) {
//						if (!checker.accept(row)) {
//							l_partialCaseValid = false;
//						} else {
//							for (final BuyOption b : row.getBuyOptions()) {
//								l_partialCaseValid &= checker.accept(b);
//							}
//
//							for (final SellOption s : row.getSellOptions()) {
//								l_partialCaseValid &= checker.accept(s);
//							}
//
//							for (final ShippingOption s : row.getShipping()) {
//								l_partialCaseValid &= checker.accept(s);
//							}
//						}
//						if (!l_partialCaseValid) {
//							break;
//						}
//					}
//				}
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

		final boolean thisLocked = locked;

		// Disable while locked.
		if (thisLocked) {
			updateActions(null);
		} else {
			updateActions(getEditingDomain());
		}

		lockedListeners.forEach(e -> e.accept(thisLocked));

		super.setLocked(locked);
	}

	public SwapValueMatrixModel getModel() {
		return currentModel;
	}

	@Override
	public synchronized ICommandHandler getDefaultCommandHandler() {

		if (commandHandler == null) {
			final ICommandHandler superHandler = super.getDefaultCommandHandler();

			final EditingDomain domain = super.getEditingDomain();

			commandHandler = new ICommandHandler() {

				@Override
				public void handleCommand(final Command command, final EObject target, final ETypedElement feature) {
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

	private Action createRunAction() {
		final ImageDescriptor genDesc = CommonImages.getImageDescriptor(IconPaths.Play_16, IconMode.Enabled);
		final Action runValueMatrixAnalysisAction = new Action() {
			@Override
			public void run() {
				if (isLocked()) {
					return;
				}
				final ADPModel adpModel = ScenarioModelUtil.getADPModel(getScenarioDataProvider());
				if (adpModel != null) {
					MessageDialog.openError(getShell(), "Unable to perform value matrix analysis", "Value matrix analysis cannot be performed on ADP scenario");
					return;
				}
				final SwapValueMatrixModel m = currentModel;
				if (m != null) {
					final SellReference sellReference = m.getParameters().getBaseDischarge();
					if (sellReference.getSlot() == null) {
						throw new IllegalStateException("No discharge slot selected");
					}
					final BuyMarket buyMarket = m.getParameters().getSwapLoadMarket();
					if (buyMarket.getMarket() == null) {
						throw new IllegalStateException("No buy market selected");
					}
					final SellMarket sellMarket = m.getParameters().getSwapDischargeMarket();
					if (sellMarket.getMarket() == null) {
						throw new IllegalStateException("No sell market selected");
					}
					final YearMonth dischargeMonth = YearMonth.from(sellReference.getSlot().getWindowStart());
					buyMarket.setMonth(dischargeMonth);
					sellMarket.setMonth(dischargeMonth);
					SwapValueMatrixSandboxEvaluator.doValueMatrixSandbox(ValueMatrixModellerView.this, m);
					valueMatrixResultsComponent.refresh();
				}
			}
		};
		runValueMatrixAnalysisAction.setImageDescriptor(genDesc);
		runValueMatrixAnalysisAction.setToolTipText("Run value matrix analysis");

		lockedListeners.add(locked -> RunnerHelper.runNowOrAsync(() -> runValueMatrixAnalysisAction.setEnabled(currentModel != null && !locked)));
		inputWants.add(m -> RunnerHelper.runNowOrAsync(() -> runValueMatrixAnalysisAction.setEnabled(m != null && !isLocked())));
		return runValueMatrixAnalysisAction;
	}

	private Action createDeleteResultsAction(final Composite parent) {
		final ImageDescriptor genDesc = CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled);
		final Action deleteResultsAction = new Action() {
			@Override
			public void run() {
				if (isLocked()) {
					return;
				}
				final SwapValueMatrixModel m = currentModel;
				if (m != null) {
					BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> {
						if (m != null && m.getSwapValueMatrixResult() != null) {

							final ScenarioResult sr = new ScenarioResultImpl(getScenarioInstance());
							ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, sp -> sp.deselect(sr, false));

							final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
							eventBroker.post(ScenarioServiceUtils.EVENT_CLOSING_ANALYTICS_SOLUTION, new AnalyticsSolution(getScenarioInstance(), m.getSwapValueMatrixResult(), "dummy-title"));

							final CompoundCommand cmd = new CompoundCommand("Delete sandbox results");
							cmd.append(DeleteCommand.create(getEditingDomain(), m.getSwapValueMatrixResult()));
							getEditingDomain().getCommandStack().execute(cmd);
							valueMatrixResultsComponent.refresh();
							parent.redraw();
							parent.requestLayout();
							parent.layout();
						}
					});
				}
			}
		};
		deleteResultsAction.setImageDescriptor(genDesc);
		deleteResultsAction.setToolTipText("Delete value matrix results");

		lockedListeners.add(locked -> RunnerHelper.runNowOrAsync(() -> deleteResultsAction.setEnabled(currentModel != null && !locked)));
		inputWants.add(m -> RunnerHelper.runNowOrAsync(() -> deleteResultsAction.setEnabled(m != null && !isLocked())));
		return deleteResultsAction;
	}

	private Action createPackAction() {
		final ImageDescriptor genDesc = CommonImages.getImageDescriptor(IconPaths.Pack, IconMode.Enabled);
		final Action packAction = new Action() {
			@Override
			public void run() {
				valueMatrixDataComponent.pack();
			}
		};
		packAction.setImageDescriptor(genDesc);
		packAction.setToolTipText("Pack parameters");

		inputWants.add(m -> RunnerHelper.runNowOrAsync(() -> packAction.setEnabled(m != null && !isLocked())));
		return packAction;
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof @NonNull final DetailConstraintStatusDecorator dcsd) {
			final SwapValueMatrixModel model = findMatrixModelInObjectTree(dcsd.getTarget());
			if (model != null //
					&& currentModel != model //
					&& (currentModel.eContainer() instanceof @NonNull final AnalyticsModel analyticsModel) //
					&& analyticsModel.getSwapValueMatrixModels().contains(model)) {
				displayScenarioInstance(this.scenarioInstance, this.rootObject, model);
			}
		}
	}

	private @Nullable SwapValueMatrixModel findMatrixModelInObjectTree(final @Nullable EObject node) {
		if (node instanceof SwapValueMatrixModel swapValueMatrixModel) {
			return swapValueMatrixModel;
		}
		if (node == null) {
			return null;
		}

		return findMatrixModelInObjectTree(node.eContainer());
	}

}
