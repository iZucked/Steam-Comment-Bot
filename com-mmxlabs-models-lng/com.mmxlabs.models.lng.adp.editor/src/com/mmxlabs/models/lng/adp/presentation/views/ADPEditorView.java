package com.mmxlabs.models.lng.adp.presentation.views;

import java.lang.reflect.InvocationTargetException;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Objects;
import com.mmxlabs.models.datetime.ui.formatters.YearMonthTextFormatter;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPModelResult;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.services.IADPScenarioEvaluator;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceViewWithUndoSupport;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ClonedScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.OpenScenarioUtils;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class ADPEditorView extends ScenarioInstanceViewWithUndoSupport {

	private ComboViewer scenarioSelector;

	private Label errorLabel;

	private ADPEditorData editorData;

	private final List<ADPComposite> pages = new LinkedList<>();
	FormattedText startEditor;
	FormattedText endEditor;

	@Override
	public void createPartControl(final Composite parent) {
		editorData = new ADPEditorData(this);

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());

		// Top Toolbar
		{
			final Composite toolbarComposite = new Composite(parent, SWT.NONE);
			toolbarComposite.setLayout(GridLayoutFactory.fillDefaults() //
					.numColumns(10) //
					.equalWidth(false) //
					.create());
			{
				final Label lbl = new Label(toolbarComposite, SWT.NONE);
				lbl.setText("ADP");
				scenarioSelector = new ComboViewer(toolbarComposite, SWT.DROP_DOWN);
				scenarioSelector.setContentProvider(new ArrayContentProvider());
				scenarioSelector.setLabelProvider(new LabelProvider() {
					@Override
					public String getText(final Object element) {
						if (element instanceof ADPModel) {
							final ADPModel adpModel = (ADPModel) element;
							return String.format("%s -> %s", adpModel.getYearStart(), adpModel.getYearEnd());
						}
						return super.getText(element);
					}
				});
				scenarioSelector.setInput(Collections.emptyList());
				scenarioSelector.getCombo().setLayoutData(GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).create());
				scenarioSelector.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						ADPModel adpModel = null;
						if (event.getSelection() instanceof IStructuredSelection) {
							final IStructuredSelection iStructuredSelection = (IStructuredSelection) event.getSelection();
							final Object firstElement = iStructuredSelection.getFirstElement();
							if (firstElement instanceof ADPModel) {
								adpModel = (ADPModel) firstElement;
							}
						}
						updateRootModel(editorData.scenarioModel, adpModel);
					}
				});
				{
					final Button btn = new Button(toolbarComposite, SWT.PUSH);
					// btn.setText("+");
					btn.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD));

					btn.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(final SelectionEvent e) {

							final LNGScenarioModel scenarioModel = (LNGScenarioModel) getRootObject();
							final ADPModel model = ADPModelUtil.createADPModel(scenarioModel);

							ADPModelUtil.populateModel(scenarioModel, model);

							final CompoundCommand cmd = new CompoundCommand("Create ADP");
							cmd.append(AddCommand.create(getEditingDomain(), scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModels(), Collections.singletonList(model)));
							getEditingDomain().getCommandStack().execute(cmd);

							doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), model);
						}
					});
				}
				{
					deleteScenarioButton = new Button(toolbarComposite, SWT.PUSH);
					// deleteScenarioButton.setText("X");
					deleteScenarioButton.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_DELETE));

					deleteScenarioButton.setToolTipText("Delete current ADP");
					deleteScenarioButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(final SelectionEvent e) {
							if (editorData.adpModel != null) {
								if (MessageDialog.openConfirm(getShell(), "Delete ADP", "Are you sure you want to delete the current ADP model?")) {

									final CompoundCommand cmd = new CompoundCommand("Delete current ADP");
									cmd.append(DeleteCommand.create(getEditingDomain(), editorData.adpModel));
									getEditingDomain().getCommandStack().execute(cmd);
									doDisplayScenarioInstance(getScenarioInstance(), getRootObject(), null);
								}
							}
						}
					});
				}

				{
					final Label l = new Label(toolbarComposite, SWT.NONE);
					l.setText("Start: ");
					startEditor = createYearMonthEditor(toolbarComposite, ADPPackage.Literals.ADP_MODEL__YEAR_START);
					startEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(80, SWT.DEFAULT).create());
				}
				{
					final Label l = new Label(toolbarComposite, SWT.NONE);
					l.setText("End before: ");
					endEditor = createYearMonthEditor(toolbarComposite, ADPPackage.Literals.ADP_MODEL__YEAR_END);
					endEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(80, SWT.DEFAULT).create());
				}
			}
		}
		folder = new CTabFolder(parent, SWT.BOTTOM);
		folder.setLayout(new GridLayout(1, true));
		folder.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Contracts");
			final ContractPage page = new ContractPage(folder, SWT.NONE, editorData);
			page.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			pages.add(page);
			tabItem.setControl(page);
		}
		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Fleet");
			final FleetPage page = new FleetPage(folder, SWT.NONE, editorData);
			page.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			pages.add(page);
			tabItem.setControl(page);
		}
		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Markets");
			final MarketsPage page = new MarketsPage(folder, SWT.NONE, editorData);
			page.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			pages.add(page);
			tabItem.setControl(page);
		}
		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Summary");
			final SummaryPage page = new SummaryPage(folder, SWT.NONE, editorData);
			page.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

			pages.add(page);
			tabItem.setControl(page);
		}
		folder.setSelection(0);

		final Composite bottomBar = new Composite(parent, SWT.NONE);
		bottomBar.setLayout(GridLayoutFactory.fillDefaults().numColumns(10).create());
		{
			btn_exportScenario = new Button(bottomBar, SWT.PUSH);
			btn_exportScenario.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			btn_exportScenario.setText("Export");
			btn_exportScenario.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					exportScenario();
				}

			});
		}

		{
			btn_optimise = new Button(bottomBar, SWT.PUSH);
			btn_optimise.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			btn_optimise.setText("Optimise");
			btn_optimise.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					optimiseScenario();
				}

			});
		}

		{
			btn_exportResult = new Button(bottomBar, SWT.PUSH);
			btn_exportResult.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			btn_exportResult.setText("Export result");
			btn_exportResult.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					exportScenarioResult();
				}

			});
		}
		{
			btn_displayResult = new Button(bottomBar, SWT.PUSH);
			btn_displayResult.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
			btn_displayResult.setText("Display result");
			btn_displayResult.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					final ADPModelResult result = editorData.adpModel.getResult();
					if (result != null) {
						final ScenarioResult scenarioResult = new ScenarioResult(getScenarioInstance(), result.getScheduleModel());
						ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, //
								provider -> {
									provider.deselectAll();
									provider.select(scenarioResult);

								});
					}
				}

			});
		}

		makeUndoActions();

		listenToScenarioSelection();
	}

	private final IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {
			RunnerHelper.runNowOrAsync(() -> refreshValidation(status));
		}
	};

	private CTabFolder folder;

	private Button deleteScenarioButton;

	private Button btn_exportScenario;

	private Button btn_optimise;

	private Button btn_exportResult;

	private Button btn_displayResult;

	private Runnable releaseAdaptersRunnable;

	private void refreshValidation(final IStatus status) {
		editorData.validationErrors.clear();
		DialogValidationSupport.processStatus(status, editorData.validationErrors);
		for (final ADPComposite page : pages) {
			page.refresh();
		}
	}

	@Override
	protected void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject) {
		doDisplayScenarioInstance(scenarioInstance, rootObject, null);
		final IStatusProvider statusProvider = getStatusProvider();
		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
			statusProvider.addStatusChangedListener(statusChangedListener);
			refreshValidation(statusProvider.getStatus());
		}
		updateUndoActions(getEditingDomain());
	}

	void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable ADPModel model) {

		if (errorLabel != null) {
			errorLabel.dispose();
			errorLabel = null;
		}

		// Some slightly hacky code to hide the editor if there is no scenario open
		if (scenarioInstance != null && rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

			// folder.setVisible(true);
			// folder.getParent().layout(true);

			if (model == null && !scenarioModel.getAdpModels().isEmpty()) {
				model = scenarioModel.getAdpModels().get(0);
			}
			updateRootModel(scenarioModel, model);
		} else {
			errorLabel = new Label(folder.getParent(), SWT.NONE);
			errorLabel.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			errorLabel.setText("No scenario selected");

			// folder.setVisible(false);
			// folder.getParent().layout(true);
			updateRootModel(null, null);
		}
	}

	private void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable final ADPModel adpModel) {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

		final boolean changed = this.editorData.adpModel != adpModel;

		this.editorData.adpModel = adpModel;
		this.editorData.scenarioModel = scenarioModel;

		if (adpModel != null) {
			startEditor.setValue(adpModel.getYearStart());
			endEditor.setValue(adpModel.getYearEnd());
		} else {
			startEditor.setValue(YearMonth.now());
			endEditor.setValue(YearMonth.now());
		}

		for (final ADPComposite page : pages) {
			page.updateRootModel(scenarioModel, adpModel);
		}

		final List<Object> objects = new LinkedList<Object>();
		if (scenarioModel != null) {
			objects.addAll(scenarioModel.getAdpModels());
		}
		if (changed || (scenarioSelector.getControl() instanceof Combo && scenarioSelector.getCombo().getItemCount() != objects.size())) {
			scenarioSelector.setInput(objects);
			scenarioSelector.setSelection(adpModel == null ? StructuredSelection.EMPTY : new StructuredSelection(adpModel));
		}

		deleteScenarioButton.setEnabled(adpModel != null);
		startEditor.getControl().setEnabled(adpModel != null);
		endEditor.getControl().setEnabled(adpModel != null);

		btn_exportScenario.setEnabled(adpModel != null);
		btn_optimise.setEnabled(adpModel != null);
		btn_exportResult.setEnabled(adpModel != null && adpModel.getResult() != null);
		btn_displayResult.setEnabled(adpModel != null && adpModel.getResult() != null);

		if (adpModel != null) {
			adpModel.eAdapters().add(adpModelAdapter);
			releaseAdaptersRunnable = () -> adpModel.eAdapters().remove(adpModelAdapter);
		}
	}

	private final AdapterImpl adpModelAdapter = new AdapterImpl() {
		public void notifyChanged(org.eclipse.emf.common.notify.Notification msg) {
			if (msg.isTouch()) {
				return;
			}
			if (msg.getFeature() == ADPPackage.Literals.ADP_MODEL__RESULT) {
				btn_exportResult.setEnabled(msg.getNewValue() != null);
				btn_displayResult.setEnabled(msg.getNewValue() != null);
			}
		};
	};

	@Override
	public void dispose() {
		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		final IStatusProvider statusProvider = getStatusProvider();
		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
		}

		if (editorData.adpModel != null) {
			editorData.adpModel = null;
		}
		if (editorData.scenarioModel != null) {
			editorData.scenarioModel = null;
		}

		super.dispose();
	}

	private void exportScenario() {
		final ScenarioInstance fork[] = new ScenarioInstance[1];
		final String name = ScenarioServiceModelUtils.openNewNameForForkPrompt("ADP", "ADP", Collections.emptySet());
		if (name == null) {
			return;
		}
		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		try {
			dialog.run(true, false, monitor -> {
				monitor.beginTask("Export solution", IProgressMonitor.UNKNOWN);

				final Copier copier = new Copier();
				final LNGScenarioModel copy = (LNGScenarioModel) copier.copy(editorData.getScenarioModel());
				copier.copyReferences();

				// LNGScenarioModel copy = EcoreUtil.copy(rootObject);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(copy);
				// Probably more?
				cargoModel.getCargoes().clear();
				cargoModel.getVesselEvents().clear();
				cargoModel.getLoadSlots().clear();
				cargoModel.getDischargeSlots().clear();
				cargoModel.getVesselAvailabilities().clear();

				final AnalyticsModel analyticsModel = ScenarioModelUtil.getAnalyticsModel(copy);
				analyticsModel.getOptimisations().clear();
				analyticsModel.getOptionModels().clear();
				final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(copy);
				scheduleModel.setSchedule(null);

				copy.getAdpModels().clear();

				final ADPModel copyADP = (ADPModel) copier.get(editorData.getAdpModel());
				for (final PurchaseContractProfile profile : copyADP.getPurchaseContractProfiles()) {
					if (profile.isEnabled()) {
						for (final SubContractProfile<LoadSlot> subProfile : profile.getSubProfiles()) {
							cargoModel.getLoadSlots().addAll(subProfile.getSlots());
						}
					}
				}
				for (final SalesContractProfile profile : copyADP.getSalesContractProfiles()) {
					if (profile.isEnabled()) {

						for (final SubContractProfile<DischargeSlot> subProfile : profile.getSubProfiles()) {
							cargoModel.getDischargeSlots().addAll(subProfile.getSlots());
						}
					}
				}
				cargoModel.getVesselAvailabilities().addAll(copyADP.getFleetProfile().getVesselAvailabilities());

				SpotMarketsModel marketsModel = ScenarioModelUtil.getSpotMarketsModel(copy);
				for (SpotMarket m : new LinkedList<>(copyADP.getSpotMarketsProfile().getSpotMarkets())) {
					if (m instanceof DESSalesMarket) {
						marketsModel.getDesSalesSpotMarket().getMarkets().add(m);
					} else if (m instanceof DESPurchaseMarket) {
						marketsModel.getDesPurchaseSpotMarket().getMarkets().add(m);
					} else if (m instanceof FOBPurchasesMarket) {
						marketsModel.getFobPurchasesSpotMarket().getMarkets().add(m);
					} else if (m instanceof FOBSalesMarket) {
						marketsModel.getFobSalesSpotMarket().getMarkets().add(m);
					}
				}

				final ClonedScenarioDataProvider scenarioDataProvider = ClonedScenarioDataProvider.make(copy, getScenarioDataProvider());

				final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(getScenarioInstance());
				if (scenarioService != null) {
					try {
						final ScenarioInstance theFork = scenarioService.copyInto(getScenarioInstance(), scenarioDataProvider, name, new NullProgressMonitor());

						// if (openScenario) {
						OpenScenarioUtils.openScenarioInstance(theFork);
					} catch (final Exception se) {
						// LOG.error(e.getMessage(), e);
					}
				}

			});
		} catch (final Exception es) {
			// LOGGER.error(e.getMessage(), e);
		}
	}

	private void exportScenarioResult() {

		final String name = ScenarioServiceModelUtils.openNewNameForForkPrompt("AD ForkP", "AD Fork", Collections.emptySet());
		if (name == null) {
			return;
		}
		final ScenarioResult scenarioResult = new ScenarioResult(getScenarioInstance(), editorData.adpModel.getResult().getScheduleModel());
		final ScenarioInstance fork = ServiceHelper.withService(IADPScenarioEvaluator.class, //
				evaluator -> {
					try {
						return evaluator.export(scenarioResult, name);
					} catch (final Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				});

	}

	private void optimiseScenario() {

		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
		try {
			dialog.run(true, true, (monitor) -> {

				monitor.beginTask("Generate ADP", 1000);
				try {
					final ADPModelResult result = ServiceHelper.withService(IADPScenarioEvaluator.class, //
							evaluator -> evaluator.runADPModel(getScenarioInstance(), //
									getScenarioDataProvider(), //
									editorData.adpModel, //
									new SubProgressMonitor(monitor, 1000)));

					final Command command = SetCommand.create(getEditingDomain(), editorData.adpModel, ADPPackage.Literals.ADP_MODEL__RESULT, result);
					RunnerHelper.syncExecDisplayOptional(() -> getDefaultCommandHandler().handleCommand(command, editorData.adpModel, ADPPackage.Literals.ADP_MODEL__RESULT));

					final ScenarioResult scenarioResult = new ScenarioResult(getScenarioInstance(), result.getScheduleModel());
					ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, //
							provider -> {
								provider.deselectAll();
								provider.select(scenarioResult);

							});
					// Assume valid result and scenario has not changed....
					RunnerHelper.runNowOrAsync(() -> {
						btn_exportResult.setEnabled(true);
						btn_displayResult.setEnabled(true);
					});
				} finally {
					monitor.done();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private FormattedText createYearMonthEditor(final Composite parent, final EAttribute attribute) {

		final YearMonthTextFormatter dateFormatter = new YearMonthTextFormatter();
		final FormattedText formattedText = new FormattedText(parent);
		formattedText.setFormatter(dateFormatter);
		formattedText.getControl().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				final ADPModel m = editorData.adpModel;
				if (m != null && formattedText.isValid()) {
					Object v = (YearMonth) formattedText.getValue();
					final Object eGet = m.eGet(attribute);
					// Only execute on change
					if (!Objects.equal(v, eGet)) {
						if (v == null) {
							v = SetCommand.UNSET_VALUE;
						}
						getEditingDomain().getCommandStack().execute(SetCommand.create(getEditingDomain(), m, attribute, v));
						// Refresh combo labels
						scenarioSelector.refresh();
					}
				}
			}
		});

		return formattedText;
	}
}
