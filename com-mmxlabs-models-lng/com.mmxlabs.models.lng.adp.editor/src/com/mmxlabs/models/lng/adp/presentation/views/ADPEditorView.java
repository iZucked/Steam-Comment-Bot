/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.lang.reflect.InvocationTargetException;
import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.google.common.base.Objects;
import com.mmxlabs.models.datetime.ui.formatters.YearMonthTextFormatter;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.services.IADPScenarioEvaluator;
import com.mmxlabs.models.lng.scenario.LNGScenarioModelValidationTransformer;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceStatusProvider;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceViewWithUndoSupport;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.models.ui.validation.impl.Multi;
import com.mmxlabs.optimiser.core.exceptions.InfeasibleSolutionException;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ADPEditorView extends ScenarioInstanceViewWithUndoSupport {

	private Label errorLabel;

	private ADPEditorData editorData;

	private final List<ADPComposite> pages = new LinkedList<>();
	FormattedText startEditor;
	FormattedText endEditor;

	@Override
	public void createPartControl(final Composite parent) {
		imgError = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.validation", "/icons/error.gif").createImage();

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
			btn_optimise = new Button(bottomBar, SWT.PUSH);
			btn_optimise.setLayoutData(GridDataFactory.fillDefaults() //
					.span(2, 1) //
					.hint(100, SWT.DEFAULT) //
					.create());
			btn_optimise.setText("Optimise");
			btn_optimise.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					optimiseScenario();
				}

			});
		}

		makeUndoActions();

		listenToScenarioSelection();
	}

	private final IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {
			RunnerHelper.syncExec(() -> {
				btn_optimise.setImage(status.getSeverity() < IStatus.ERROR ? null : imgError);
				btn_optimise.setEnabled(status.getSeverity() < IStatus.ERROR);
				// Large tooltip covers the button and stop user being able to press it.
				if (status.isOK()) {
					// btn_optimise.setToolTipText(null);
				} else {
					// btn_optimise.setToolTipText(DialogValidationSupport.processMessages(status));
				}
			});
			RunnerHelper.runNowOrAsync(() -> refreshValidation(status));
		}
	};

	private CTabFolder folder;

	private Button deleteScenarioButton;

	private Button btn_optimise;

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

			if (model == null) {
				model = scenarioModel.getAdpModel();
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
		if (scenarioModel != null && scenarioModel.getAdpModel() != null) {
			objects.add(scenarioModel.getAdpModel());
		}

		deleteScenarioButton.setEnabled(adpModel != null);
		startEditor.getControl().setEnabled(adpModel != null);
		endEditor.getControl().setEnabled(adpModel != null);

		btn_optimise.setEnabled(adpModel != null);

		final IStatusProvider statusProvider = getStatusProvider();
		if (statusProvider != null) {
			statusChangedListener.onStatusChanged(statusProvider, statusProvider.getStatus());
		}
	}

	private Image imgError;

	@Override
	public void dispose() {

		if (imgError != null) {
			imgError.dispose();
			imgError = null;
		}

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

	private void optimiseScenario() {

		if (!validateScenario(getScenarioDataProvider(), editorData.adpModel)) {
			return;
		}

		final ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
		try {
			dialog.run(true, true, (monitor) -> {

				monitor.beginTask("Generate ADP", 1000);
				try {
					ServiceHelper.withService(IADPScenarioEvaluator.class, //
							evaluator -> evaluator.runADPModel(getScenarioInstance(), //
									getScenarioDataProvider(), //
									editorData.adpModel, //
									new SubProgressMonitor(monitor, 1000)));

					// final Command command = SetCommand.create(getEditingDomain(), editorData.adpModel, ADPPackage.Literals.ADP_MODEL__RESULT, result);
					// RunnerHelper.syncExecDisplayOptional(() -> getDefaultCommandHandler().handleCommand(command, editorData.adpModel, ADPPackage.Literals.ADP_MODEL__RESULT));

					final ScenarioResult scenarioResult = new ScenarioResult(getScenarioInstance(), ScenarioModelUtil.getScheduleModel(getScenarioDataProvider()));
					ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, //
							provider -> {
								provider.deselectAll();
								provider.select(scenarioResult);

							});

				} finally {
					monitor.done();
				}
			});
		} catch (final InvocationTargetException e) {
			if (e.getCause() instanceof InfeasibleSolutionException || e.getTargetException() instanceof InfeasibleSolutionException) {
				MessageDialog.openError(getShell(), "Unable to generate solution", "Unable to generate a feasible schedule with the given constraints.");
			} else {
				e.printStackTrace();
			}
		} catch (final InterruptedException e) {

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
					}
				}
			}
		});

		return formattedText;
	}

	public static boolean validateScenario(final IScenarioDataProvider scenarioDataProvider, final ADPModel adpModel) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().endsWith(".base")) {
						return true;
					} else if (cat.getId().endsWith(".optimisation")) {
						return true;
					} else if (cat.getId().endsWith(".adp")) {
						return true;
					}
				}

				return false;
			}
		});
		final MMXRootObject rootObject = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, false);
			return helper.runValidation(validator, extraContext, new LNGScenarioModelValidationTransformer(), rootObject, adpModel);
		});

		if (status == null) {
			return false;
		}

		if (status.isOK() == false) {

			// See if this command was executed in the UI thread - if so fire up the dialog box.
			if (Display.getCurrent() != null) {

				final ValidationStatusDialog dialog = new ValidationStatusDialog(Display.getCurrent().getActiveShell(), status, status.getSeverity() != IStatus.ERROR);

				// Wait for use to press a button before continuing.
				dialog.setBlockOnOpen(true);

				if (dialog.open() == Window.CANCEL) {
					return false;
				}
			}
		}

		if (status.getSeverity() == IStatus.ERROR) {
			return false;
		}

		return true;
	}

	@Override
	protected ScenarioInstanceStatusProvider createScenarioValidationProvider(final ScenarioInstance instance) {
		return new ScenarioInstanceStatusProvider(instance) {
			@Override
			public void fireStatusChanged(IStatus status) {
				if (!(status instanceof Multi)) {
					if (editorData.adpModel != null) {
						status = ADPEditorValidatorUtil.mergeWithADPValidation(status, editorData.getScenarioDataProvider(), editorData.adpModel);
					}
				}
				super.fireStatusChanged(status);
			}

			@Override
			public IStatus getStatus() {
				IStatus status = super.getStatus();

				if (!(status instanceof Multi)) {
					if (editorData.adpModel != null) {
						status = ADPEditorValidatorUtil.mergeWithADPValidation(status, editorData.getScenarioDataProvider(), editorData.adpModel);
					}
				}
				return status;
			}
		};
	}
}
