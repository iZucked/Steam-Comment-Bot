/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
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
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Objects;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.mull.scenariocombine.BlueSkyPADPWizard;
import com.mmxlabs.models.lng.adp.mull.scenariocombine.CombinePADPWizard;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.tabular.ScenarioViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.date.YearMonthTextFormatter;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusDialog;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ADPEditorViewerPane extends ScenarioViewerPane {

	private Label errorLabel;

	private ADPEditorData editorData;

	private final List<ADPComposite> pages = new LinkedList<>();
	private FormattedText startEditor;
	private FormattedText endEditor;

	public ADPEditorViewerPane(IWorkbenchPage page, JointModelEditorPart editorPart, IActionBars actionBars) {
		super(page, editorPart, editorPart, actionBars);
	}

	@Override
	public Viewer createViewer(final Composite parent) {
		imgError = CommonImages.getImageDescriptor(IconPaths.Error, IconMode.Enabled).createImage();

		editorData = new ADPEditorData(this.scenarioEditingLocation);

		parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).create());

		// Top Toolbar
		{
			final Composite toolbarComposite = new Composite(parent, SWT.NONE);
			final int additionalColumns = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION) ? 2 : 0;
			final int numColumns = additionalColumns + 7;
			toolbarComposite.setLayout(GridLayoutFactory.fillDefaults() //
					.numColumns(numColumns) //
					.equalWidth(false) //
					.create());
			toolbarComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

			{
				final Label lbl = new Label(toolbarComposite, SWT.NONE);
				lbl.setText("ADP");

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

				{
					deleteScenarioButton = new Button(toolbarComposite, SWT.PUSH);
					// deleteScenarioButton.setText("X");

					final Image img = CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled).createImage();
					deleteScenarioButton.addDisposeListener(e -> img.dispose());
					deleteScenarioButton.setImage(img);
					deleteScenarioButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).create());

					deleteScenarioButton.setToolTipText("Delete current ADP");
					deleteScenarioButton.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(final SelectionEvent e) {
							if (editorData.adpModel != null) {
								if (MessageDialog.openConfirm(getScenarioEditingLocation().getShell(), "Delete ADP", "Are you sure you want to delete the current ADP model?")) {

									final CompoundCommand cmd = new CompoundCommand("Delete current ADP");
									cmd.append(DeleteCommand.create(getEditingDomain(), editorData.adpModel));
									getEditingDomain().getCommandStack().execute(cmd);
									doDisplayScenarioInstance(getScenarioEditingLocation().getScenarioInstance(), getScenarioEditingLocation().getRootObject(), null);
								}
							}
						}
					});
				}

				final Button changeADPYearButton = new Button(toolbarComposite, SWT.PUSH);
				changeADPYearButton.setText("Shift Year");
				changeADPYearButton.setToolTipText("Requests a new year for the ADP start and updates all profile constraints to align with the new year.");
				changeADPYearButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).create());
				changeADPYearButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						if (editorData.adpModel != null) {
							final Display display = PlatformUI.getWorkbench().getDisplay();
							final InputDialog dialog = new InputDialog(display.getActiveShell(), "Shift ADP Year", "Enter a new ADP Start Year", "", newText -> {
								if (newText == null) {
									return "Please enter a new start year";
								}
								if (!newText.matches("^\\d+$")) {
									return "Please enter a valid year";
								}
								return null;
							});
							if (dialog.open() == Window.OK) {
								final String year = dialog.getValue();
								final int startYear = Integer.parseInt(year);
								final Command cmd = ADPModelUtil.constructShiftAdpYearCommand(getEditingDomain(), editorData.getAdpModel(), startYear);
								if (cmd != null) {
									getEditingDomain().getCommandStack().execute(cmd);
									refresh();
								}
							}
						}
					}
				});

				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
					final Button buildBlueSkyButton = new Button(toolbarComposite, SWT.PUSH);
					buildBlueSkyButton.setText("Build blue sky");
					buildBlueSkyButton.setToolTipText("Builds blue sky model where current scenario is the demand side");
					buildBlueSkyButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).create());
					buildBlueSkyButton.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							ScenarioInstance demandSideScenarioInstance = editorData.getScenarioInstance();
							if (demandSideScenarioInstance != null) {
								final BlueSkyPADPWizard wizard = new BlueSkyPADPWizard(demandSideScenarioInstance);
								wizard.init(page.getWorkbenchWindow().getWorkbench(), null);
								Shell parent = page.getWorkbenchWindow().getShell();
								WizardDialog dialog = new WizardDialog(parent, wizard);
								dialog.create();
								dialog.open();
							}
						}
					});

					final Button buildCombinedModelButton = new Button(toolbarComposite, SWT.PUSH);
					buildCombinedModelButton.setText("Build combined");
					buildCombinedModelButton.setToolTipText("Builds combined model where current scenario is the demand side");
					buildCombinedModelButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER).create());
					buildCombinedModelButton.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							ScenarioInstance demandSideScenarioInstance = editorData.getScenarioInstance();
							if (demandSideScenarioInstance != null) {
								final CombinePADPWizard wizard = new CombinePADPWizard(demandSideScenarioInstance);
								wizard.init(page.getWorkbenchWindow().getWorkbench(), null);
								Shell parent = page.getWorkbenchWindow().getShell();
								WizardDialog dialog = new WizardDialog(parent, wizard);
								dialog.create();
								dialog.open();
							}
						}
					});
				}
			}
		}
		folder = new CTabFolder(parent, SWT.BOTTOM);
		folder.setLayout(new GridLayout(1, true));
		folder.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Contracts");
			final ContractPage page = new ContractPage(folder, SWT.NONE, editorData, actionBars);
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

		// makeUndoActions();

		// listenToScenarioSelection();
		doDisplayScenarioInstance(getScenarioEditingLocation().getScenarioInstance(), getScenarioEditingLocation().getRootObject(), null);

		// Create dummy viewer instance
		return new Viewer() {

			@Override
			public void setSelection(ISelection selection, boolean reveal) {
				// TODO Auto-generated method stub

			}

			@Override
			public void setInput(Object input) {
				// TODO Auto-generated method stub
				doDisplayScenarioInstance(getScenarioEditingLocation().getScenarioInstance(), getScenarioEditingLocation().getRootObject(), null);
			}

			@Override
			public void refresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public ISelection getSelection() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getInput() {
				// TODO Auto-generated method stub
				return editorData.adpModel;
			}

			@Override
			public Control getControl() {
				// TODO Auto-generated method stub
				return folder;
			}
		};
	}

	private CTabFolder folder;

	private Button deleteScenarioButton;

	private Runnable releaseAdaptersRunnable;

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

	private void refresh() {
		final ADPModel adpModel = this.editorData.adpModel;
		if (adpModel == null) {
			return;
		}
		startEditor.setValue(adpModel.getYearStart());
		endEditor.setValue(adpModel.getYearEnd());
		pages.stream().forEach(ADPComposite::refresh);
	}

	private void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable final ADPModel adpModel) {
		final boolean changed = this.editorData.adpModel != adpModel;
		if (!changed) {
			return;
		}

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

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

		if (editorData.adpModel != null) {
			editorData.adpModel = null;
		}
		if (editorData.scenarioModel != null) {
			editorData.scenarioModel = null;
		}

		super.dispose();
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
			return helper.runValidation(validator, extraContext, rootObject, null);
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

	public void setValidationTarget(EObject target) {
		// Note: These indicies need to be kept in sync with page.
		if (target instanceof FleetProfile) {
			folder.setSelection(1);
		} else if (target instanceof ContractProfile<?, ?>) {
			folder.setSelection(0);
			((ContractPage) pages.get(0)).setSelectedProfile((ContractProfile<?, ?>) target);
		}
	}

	@Override
	public void updateActionBars() {
		// TODO Auto-generated method stub
		super.updateActionBars();
	}
}
