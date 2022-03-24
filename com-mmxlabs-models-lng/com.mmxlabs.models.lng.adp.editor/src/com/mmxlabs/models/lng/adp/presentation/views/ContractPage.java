/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Triple;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.lng.adp.mull.FilterToVesselsAction;
import com.mmxlabs.models.lng.adp.mull.MullSolver;
import com.mmxlabs.models.lng.adp.rateability.export.FeasibleSolverResult;
import com.mmxlabs.models.lng.adp.rateability.export.Infeasible;
import com.mmxlabs.models.lng.adp.rateability.export.SpacingRateabilitySolverResult;
import com.mmxlabs.models.lng.adp.rateability.spacing.CPBasedSolver;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualSingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.ecore.SafeEContentAdapter;

public class ContractPage extends ADPComposite {

	private static final String UNSPECIFIED_NAME = "<Not specified>";

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private Composite mullComposite;
	private EmbeddedDetailComposite detailComposite;
	private ComboViewer objectSelector;
	private Runnable releaseAdaptersRunnable = null;

	private ScenarioTableViewer previewViewer;
	private GridTreeViewer mullSummaryViewer;
	private int mullSummaryExpandLevel = 1;

	FilterToVesselsAction filterAction;

	private IActionBars actionBars;

	private Adapter mullSummaryAdapter;

	public ContractPage(final Composite parent, final int style, final ADPEditorData editorData, IActionBars actionBars) {
		super(parent, style);
		this.editorData = editorData;
		this.actionBars = actionBars;
		this.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).create());

		// Top Toolbar
		{
			final Composite toolbarComposite = new Composite(this, SWT.NONE);
			toolbarComposite.setLayout(GridLayoutFactory.fillDefaults() //
					.numColumns(5) //
					.equalWidth(false) //
					.create());
			{
				final Label lbl = new Label(toolbarComposite, SWT.NONE);
				lbl.setText("Contract");
				objectSelector = new ComboViewer(toolbarComposite, SWT.DROP_DOWN);
				objectSelector.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).create());
				objectSelector.setContentProvider(new ArrayContentProvider());
				objectSelector.setLabelProvider(new LabelProvider() {

					@Override
					public String getText(final Object element) {
						if (element instanceof MullProfile) {
							return "MULL Generation";
						} else if (element instanceof SpacingProfile) {
							return "Spacing Rateability";
						} else if (element instanceof final PurchaseContract profile) {
							return String.format("%s (Purchase)", profile.getName());
						} else if (element instanceof final SalesContract profile) {
							return String.format("%s (Sale)", profile.getName());
						}
						return super.getText(element);
					}
				});
				objectSelector.setInput(Collections.emptyList());
				objectSelector.addSelectionChangedListener(event -> {
					final ISelection selection = event.getSelection();
					EObject target = null;
					generateButton.setEnabled(false);
					if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
						vanillaBuildButton.setEnabled(false);
					}
					if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_SPACING_RATEABILITY)) {
						cpSolveButton.setEnabled(false);
					}
					if (selection instanceof final IStructuredSelection iStructuredSelection) {
						target = (EObject) iStructuredSelection.getFirstElement();
						generateButton.setEnabled(target instanceof Contract || target instanceof Inventory || target instanceof MullProfile);
						if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
							vanillaBuildButton.setEnabled(target instanceof MullProfile);
						}
						if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_SPACING_RATEABILITY)) {
							cpSolveButton.setEnabled(target instanceof SpacingProfile);
						}
					}
					// generateButton.setEnabled(target != null);
					updateDetailPaneInput(target);
				});
			}
			{
				generateButton = new Button(toolbarComposite, SWT.PUSH);
				generateButton.setText("Re-generate slots");
				generateButton.setEnabled(false);
				generateButton.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						EObject input = detailComposite.getInput();
						if (input instanceof final PurchaseContractProfile profile) {
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");

							final Command populateModelCommand = ADPModelUtil.populateModel(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							if (populateModelCommand != null) {
								cmd.append(populateModelCommand);
								editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
								input = profile.getContract();
							}
						} else if (input instanceof final SalesContractProfile profile) {
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");

							final Command populateModelCommand = ADPModelUtil.populateModel(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							if (populateModelCommand != null) {
								cmd.append(populateModelCommand);
								editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
								input = profile.getContract();
							}
						} else if (input instanceof final MullProfile profile) {
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");
							if (editorData != null) {
								final Command populateModelCommand = MullSolver.populateModelFromMultipleInventories(editorData, profile);
								cmd.append(populateModelCommand);
							}
						}
						updateDetailPaneInput(input);
					}
				});
			}
			{
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
					vanillaBuildButton = new Button(toolbarComposite, SWT.PUSH);
					vanillaBuildButton.setText("Generate vanilla");
					vanillaBuildButton.setEnabled(true);
					vanillaBuildButton.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {

							EObject input = detailComposite.getInput();
							if (input instanceof final MullProfile profile) {
								final CompoundCommand cmd = new CompoundCommand("Generate vanilla ADP Slots");
								if (editorData != null) {
									final Command populateModelCommand = MullSolver.populateVanillaModel(editorData, profile);
									cmd.append(populateModelCommand);
								}

							}
							updateDetailPaneInput(input);
						}
					});
				}
			}
			{
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_SPACING_RATEABILITY)) {
					cpSolveButton = new Button(toolbarComposite, SWT.PUSH);
					cpSolveButton.setText("Run rateability solver");
					cpSolveButton.setEnabled(true);
					cpSolveButton.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							final CPBasedSolver cpBasedSolver = new CPBasedSolver();

							final EObject input = detailComposite.getInput();
							if (input instanceof @NonNull final SpacingProfile spacingProfile) {
								final SpacingRateabilitySolverResult result = cpBasedSolver.runCpSolver(spacingProfile, editorData.scenarioModel, editorData.getEditingDomain(),
										editorData.getScenarioDataProvider());
								if (result instanceof final FeasibleSolverResult feasibleResult) {
									final CompoundCommand modelPopulationCommand = (CompoundCommand) feasibleResult.getModelPopulationCommand();
									if (!modelPopulationCommand.isEmpty()) {
										editorData.getDefaultCommandHandler().handleCommand(modelPopulationCommand);
									}
								} else {
									final String message;
									if (result instanceof Infeasible) {
										message = "Rateability model is infeasible";
									} else {
										message = "Rateability solver could not find a solution";
									}
									if (result != null) {
										MessageDialog.openInformation(getShell(), "No solution found", message);
									}
								}
							}

						}
					});
				}
			}
		}

		mainComposite = new SashForm(this, SWT.HORIZONTAL);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY));
		mainComposite.setSashWidth(5);

		mainComposite.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				// .align(SWT.CENTER, SWT.TOP)//
				// .span(1, 1) //
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(true) //
				.numColumns(2) //
				.spacing(0, 0) //
				.create());

		{
			detailComposite = new EmbeddedDetailComposite(mainComposite, editorData);
		}
		{
			rhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL);
			rhsScrolledComposite.setLayoutData(GridDataFactory.fillDefaults()//
					.grab(false, true)//
					.hint(200, SWT.DEFAULT) //
					.align(SWT.FILL, SWT.FILL).create());

			rhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			rhsScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			rhsScrolledComposite.setExpandHorizontal(true);
			rhsScrolledComposite.setExpandVertical(true);
			rhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);

			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
			// rhsScrolledComposite.setContent(rhsComposite);

			rhsComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			{
				// Preview Table with generated options
				{
					previewGroup = new Group(rhsComposite, SWT.NONE);
					previewGroup.setLayout(new GridLayout(1, false));
					previewGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

					previewGroup.setText("Contract positions");

					final DetailToolbarManager removeButtonManager = new DetailToolbarManager(previewGroup, SWT.TOP);

					deleteSlotAction = new Action("Delete") {

						@Override
						public void run() {
							if (previewViewer != null) {
								final ISelection selection = previewViewer.getSelection();
								if (selection instanceof final IStructuredSelection iStructuredSelection) {
									final CompoundCommand cc = new CompoundCommand();
									final Iterator<?> itr = iStructuredSelection.iterator();
									List<Object> objectsToDelete = Lists.newArrayList(itr);
									final List<Object> extraObjects = new LinkedList<>();
									for (final Object o : objectsToDelete) {
										Cargo c = null;
										if (o instanceof Slot<?>) {
											c = ((Slot<?>) o).getCargo();
											extraObjects.add(c);
										}
										if (c != null) {
											for (final Slot<?> s : c.getSlots()) {
												if (s instanceof SpotSlot) {
													extraObjects.add(s);
												}
											}
										}
									}
									objectsToDelete.addAll(extraObjects);
									cc.append(DeleteCommand.create(editorData.getEditingDomain(), objectsToDelete));
									editorData.getEditingDomain().getCommandStack().execute(cc);
									updatePreviewPaneInput(detailComposite.getInput());
								}
							}
							;

						}
					};
					deleteSlotAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled));
					if (actionBars != null) {
						actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteSlotAction);
					}
					removeButtonManager.getToolbarManager().add(deleteSlotAction);
					{

						Action packAction = new Action("Pack") {
							@Override
							public void run() {

								if (previewViewer != null && !previewViewer.getControl().isDisposed()) {
									final GridColumn[] columns = previewViewer.getGrid().getColumns();
									for (final GridColumn c : columns) {
										if (c.getResizeable()) {
											c.pack();
										}
									}
								}
							}
						};

						packAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Pack, IconMode.Enabled));

						removeButtonManager.getToolbarManager().add(packAction);
					}

					removeButtonManager.getToolbarManager().update(true);
				}
				{
					if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
						mullComposite = new Composite(rhsScrolledComposite, SWT.NONE);
						mullComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
						mullSummaryGroup = new Group(mullComposite, SWT.NONE);
						mullSummaryGroup.setLayout(new GridLayout(1, false));
						mullSummaryGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

						mullSummaryGroup.setText("Mull Summary");
						final DetailToolbarManager toolbarManager = new DetailToolbarManager(mullSummaryGroup, SWT.TOP);

						filterAction = new FilterToVesselsAction(filterToVessels -> {
							if (mullSummaryViewer != null) {
								final IContentProvider contentProvider = getMullSummaryContentProvider(filterToVessels);
								mullSummaryViewer.setContentProvider(contentProvider);
								mullSummaryViewer.refresh();
							}
						});
						toolbarManager.getToolbarManager().add(filterAction);

						mullSummaryViewer = constructMullSummaryViewer(editorData, mullSummaryGroup);

						Action packAction = new Action("Pack") {
							@Override
							public void run() {

								if (mullSummaryViewer != null && !mullSummaryViewer.getControl().isDisposed()) {
									final GridColumn[] columns = mullSummaryViewer.getGrid().getColumns();
									for (final GridColumn c : columns) {
										if (c.getResizeable()) {
											c.pack();
										}
									}
								}
							}
						};

						packAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Pack, IconMode.Enabled));

						toolbarManager.getToolbarManager().add(packAction);

						final CopyGridToClipboardAction mullSummaryTableCopyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(mullSummaryViewer);
						toolbarManager.getToolbarManager().add(mullSummaryTableCopyAction);

						mullSummaryExpandLevel = 1;
						final Action collapseOneLevel = new Action("Collapse All") {
							@Override
							public void run() {
								mullSummaryViewer.collapseAll();
								mullSummaryExpandLevel = 1;
							}
						};
						final Action expandOneLevel = new Action("Expand one Level") {
							@Override
							public void run() {
								mullSummaryViewer.expandToLevel(++mullSummaryExpandLevel);
							}
						};

						CommonImages.setImageDescriptors(collapseOneLevel, IconPaths.CollapseAll);
						CommonImages.setImageDescriptors(expandOneLevel, IconPaths.ExpandAll);

						toolbarManager.getToolbarManager().add(collapseOneLevel);
						toolbarManager.getToolbarManager().add(expandOneLevel);
						toolbarManager.getToolbarManager().update(true);

						mullSummaryAdapter = new EContentAdapter() {
							@Override
							public void notifyChanged(final Notification notification) {
								super.notifyChanged(notification);
								ViewerHelper.refresh(mullSummaryViewer, false);
							}

							@Override
							protected void setTarget(final EObject target) {
								basicSetTarget(target);
								if (target instanceof @NonNull final ADPModel adpModel) {
									basicSetTarget(target);
									addAdapter(target);
									final MullProfile mullProfile = adpModel.getMullProfile();
									if (mullProfile != null) {
										addAdapter(mullProfile);
										for (final MullSubprofile mullSubprofile : mullProfile.getInventories()) {
											addAdapter(mullSubprofile);
											for (final MullEntityRow mullEntityRow : mullSubprofile.getEntityTable()) {
												addAdapter(mullEntityRow);
												for (final SalesContractAllocationRow salesContractAllocationRow : mullEntityRow.getSalesContractAllocationRows()) {
													addAdapter(salesContractAllocationRow);
												}
												for (final DESSalesMarketAllocationRow desSalesMarketAllocationRow : mullEntityRow.getDesSalesMarketAllocationRows()) {
													addAdapter(desSalesMarketAllocationRow);
												}
											}
										}
									}
								}
							}

							@Override
							protected void unsetTarget(final EObject target) {
								basicUnsetTarget(target);
								if (target instanceof @NonNull final ADPModel adpModel) {
									removeAdapter(target, false, true);
									final MullProfile mullProfile = adpModel.getMullProfile();
									if (mullProfile != null) {
										removeAdapter(mullProfile, false, true);
										for (final MullSubprofile mullSubprofile : mullProfile.getInventories()) {
											removeAdapter(mullSubprofile, false, true);
											for (final MullEntityRow mullEntityRow : mullSubprofile.getEntityTable()) {
												removeAdapter(mullEntityRow, false, true);
												for (final SalesContractAllocationRow salesContractAllocationRow : mullEntityRow.getSalesContractAllocationRows()) {
													removeAdapter(salesContractAllocationRow, false, true);
												}
												for (final DESSalesMarketAllocationRow desSalesMarketAllocationRow : mullEntityRow.getDesSalesMarketAllocationRows()) {
													removeAdapter(desSalesMarketAllocationRow, false, true);
												}
											}
										}
									}
								}
							}
						};
					}
				}
			}
		}

	}

	private ScenarioTableViewer constructPreviewViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer localPreviewViewer = new ScenarioTableViewer(previewGroup, SWT.MULTI, editorData);
		localPreviewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference());
		localPreviewViewer.setContentProvider(new ContentProvider());
		GridViewerHelper.configureLookAndFeel(localPreviewViewer);

		localPreviewViewer.setStatusProvider(editorData.getStatusProvider());

		localPreviewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(localPreviewViewer);

		localPreviewViewer.getGrid().setHeaderVisible(true);

		localPreviewViewer.addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getDefaultCommandHandler()));
		localPreviewViewer.addTypicalColumn("Port",
				new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), editorData.getReferenceValueProviderCache(), editorData.getDefaultCommandHandler()));

		// TODO: Groups
		final GridColumnGroup windowGroup = new GridColumnGroup(localPreviewViewer.getGrid(), SWT.NONE);
		windowGroup.setText("Window");
		GridViewerHelper.configureLookAndFeel(windowGroup);
		localPreviewViewer.addTypicalColumn("Date", windowGroup, new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editorData.getDefaultCommandHandler()));
		localPreviewViewer.addTypicalColumn("Time", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editorData.getDefaultCommandHandler()));
		localPreviewViewer.addTypicalColumn("Size", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editorData.getDefaultCommandHandler()));
		localPreviewViewer.addTypicalColumn("Units", windowGroup,
				new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(), editorData.getDefaultCommandHandler(), e -> mapName((TimePeriod) e)));
		localPreviewViewer.addTypicalColumn("Duration", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), editorData.getDefaultCommandHandler()));

		// TODO: Groups
		final GridColumnGroup quantityGroup = new GridColumnGroup(localPreviewViewer.getGrid(), SWT.NONE);
		quantityGroup.setText("Quantity");
		GridViewerHelper.configureLookAndFeel(quantityGroup);
		localPreviewViewer.addTypicalColumn("Min", quantityGroup, new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), editorData.getDefaultCommandHandler()));
		localPreviewViewer.addTypicalColumn("Max", quantityGroup, new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editorData.getDefaultCommandHandler()));
		localPreviewViewer.addTypicalColumn("Units", quantityGroup,
				new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(), editorData.getDefaultCommandHandler(), e -> mapName((VolumeUnits) e)));

		localPreviewViewer.addDoubleClickListener(event -> {
			final ISelection selection = localPreviewViewer.getSelection();
			if (selection instanceof final IStructuredSelection ss) {
				DetailCompositeDialogUtil.editSelection(editorData, ss);
				localPreviewViewer.refresh();
			}
		});

		localPreviewViewer.addSelectionChangedListener(event -> {
			// Update global action on selection change.
			if (actionBars != null) {
				actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteSlotAction);
			}
			deleteSlotAction.setEnabled(!event.getSelection().isEmpty());
		});
		deleteSlotAction.setEnabled(false);

		return localPreviewViewer;
	}

	private GridTreeViewer constructMullSummaryViewer(final ADPEditorData editorData, final Group group) {
		final GridTreeViewer localMullSummaryViewer = new GridTreeViewer(group, SWT.V_SCROLL | SWT.MULTI);
		// localMullSummaryViewer.init(editorData.getAdapterFactory(), editorData.getModelReference());

		final IContentProvider contentProvider = getMullSummaryContentProvider(filterAction.isShowingVessels());
		localMullSummaryViewer.setContentProvider(contentProvider);
		localMullSummaryViewer.getGrid().setHeaderVisible(true);
		GridViewerHelper.configureLookAndFeel(localMullSummaryViewer);

		// localMullSummaryViewer.setStatusProvider(editorData.getStatusProvider());

		localMullSummaryViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		// ColumnViewerToolTipSupport.enableFor(localMullSummaryViewer);

		// localMullSummaryViewer.getGrid().setHeaderVisible(true);

		final GridViewerColumn attributeGvc = new GridViewerColumn(localMullSummaryViewer, SWT.NONE);
		attributeGvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		attributeGvc.getColumn().setText("Attribute");
		attributeGvc.getColumn().setTree(true);
		attributeGvc.getColumn().setWidth(50);
		attributeGvc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof final Triple<?, ?, ?> triple) {
					return (String) triple.getFirst();
				} else if (element instanceof final MullSubprofile mullSubprofile) {
					return ScenarioElementNameHelper.getName(mullSubprofile.getInventory(), UNSPECIFIED_NAME);
				} else if (element instanceof final MullEntityRow mullEntityRow) {
					return ScenarioElementNameHelper.getName(mullEntityRow.getEntity(), UNSPECIFIED_NAME);
				} else if (element instanceof final SalesContractAllocationRow salesContractAllocationRow) {
					return ScenarioElementNameHelper.getName(salesContractAllocationRow.getContract(), UNSPECIFIED_NAME);
				} else if (element instanceof final DESSalesMarketAllocationRow desSalesMarketAllocationRow) {
					return ScenarioElementNameHelper.getName(desSalesMarketAllocationRow.getDesSalesMarket(), UNSPECIFIED_NAME);
				}
				return null;
			}
		});

		final GridViewerColumn valueGvc = new GridViewerColumn(localMullSummaryViewer, SWT.NONE);
		valueGvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		valueGvc.getColumn().setText("Value");
		valueGvc.getColumn().setWidth(50);
		valueGvc.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Triple) {
					final Object secondElement = ((Triple<?, ?, ?>) element).getSecond();
					if (secondElement instanceof final Vessel vessel) {
						return ScenarioElementNameHelper.getName(vessel, UNSPECIFIED_NAME);
					} else if (secondElement instanceof final MullCargoWrapper mullCargoWrapper) {
						final String entityName = ScenarioElementNameHelper.getName(mullCargoWrapper.getLoadSlot().getEntity(), UNSPECIFIED_NAME);
						final PurchaseContract loadContract = mullCargoWrapper.getLoadSlot().getContract();
						final String loadContractName = loadContract != null ? ScenarioElementNameHelper.getName(loadContract, UNSPECIFIED_NAME) : UNSPECIFIED_NAME;
						final DischargeSlot dischargeSlot = mullCargoWrapper.getDischargeSlot();
						final String dischargeName;
						if (dischargeSlot instanceof final SpotDischargeSlot spotDischargeSlot) {
							if (spotDischargeSlot.getMarket() != null) {
								dischargeName = ScenarioElementNameHelper.getName(spotDischargeSlot.getMarket(), UNSPECIFIED_NAME);
							} else {
								dischargeName = spotDischargeSlot.getPriceExpression() != null ? spotDischargeSlot.getPriceExpression() : UNSPECIFIED_NAME;
							}
						} else {
							if (dischargeSlot.getContract() != null) {
								dischargeName = ScenarioElementNameHelper.getName(dischargeSlot.getContract(), UNSPECIFIED_NAME);
							} else {
								dischargeName = dischargeSlot.getPriceExpression() != null ? dischargeSlot.getPriceExpression() : UNSPECIFIED_NAME;
							}
						}
						final LocalDate loadDate = mullCargoWrapper.getLoadSlot().getWindowStart();
						final String loadDateString = loadDate != null ? String.format("%01d/%01d/%d", loadDate.getDayOfMonth(), loadDate.getMonthValue(), loadDate.getYear()) : UNSPECIFIED_NAME;
						return String.format("%s--%s(%s)--%s", entityName, loadContractName, loadDateString, dischargeName);
					} else if (secondElement instanceof Integer) {
						return String.format("%,d", (int) secondElement);
					} else if (secondElement instanceof Double) {
						return String.format("%,.5f", (double) secondElement);
					}
				} else if (element instanceof MullSubprofile) {
					return "";
				} else if (element instanceof MullEntityRow) {
					return "";
				} else if (element instanceof SalesContractAllocationRow) {
					return "";
				} else if (element instanceof DESSalesMarketAllocationRow) {
					return "";
				}
				return null;
			}
		});

		return localMullSummaryViewer;
	}

	@Override
	public void refresh() {
		ViewerHelper.refresh(previewViewer, true);
	}

	@Override
	public synchronized void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable final ADPModel adpModel) {
		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

		if (previewViewer != null) {
			previewViewer.getControl().dispose();
			previewViewer.dispose();
			previewViewer = null;
		}
		if (scenarioModel != null) {
			previewViewer = constructPreviewViewer(editorData, previewGroup);
		}
		final List<Object> objects = new LinkedList<>();
		if (scenarioModel != null && adpModel != null) {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
			commercialModel.eAdapters().add(commercialModelAdapter);

			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			cargoModel.eAdapters().add(cargoModelAdapter);

			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
				MullProfile profile = adpModel.getMullProfile();
				if (profile == null) {
					profile = ADPFactory.eINSTANCE.createMullProfile();
				}
				objects.add(profile);
				mullSummaryAdapter.setTarget(adpModel);
				releaseAdaptersRunnable = () -> {
					commercialModel.eAdapters().remove(commercialModelAdapter);
					cargoModel.eAdapters().remove(cargoModelAdapter);
					adpModel.eAdapters().remove(mullSummaryAdapter);
				};
			} else if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_SPACING_RATEABILITY)) {
				SpacingProfile profile = adpModel.getSpacingProfile();
				if (profile == null) {
					profile = ADPFactory.eINSTANCE.createSpacingProfile();
				}
				objects.add(profile);
				releaseAdaptersRunnable = () -> {
					commercialModel.eAdapters().remove(commercialModelAdapter);
					cargoModel.eAdapters().remove(cargoModelAdapter);
				};
			} else {
				releaseAdaptersRunnable = () -> {
					commercialModel.eAdapters().remove(commercialModelAdapter);
					cargoModel.eAdapters().remove(cargoModelAdapter);
				};
			}
			final List<PurchaseContract> sortedPurchaseContracts = new ArrayList<>(commercialModel.getPurchaseContracts());
			sortedPurchaseContracts.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
			objects.addAll(sortedPurchaseContracts);
			final List<SalesContract> sortedSalesContracts = new ArrayList<>(commercialModel.getSalesContracts());
			sortedSalesContracts.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
			objects.addAll(sortedSalesContracts);
		}
		// Try to retain current selection
		final ISelection selection = new StructuredSelection(objectSelector.getSelection());
		objectSelector.setInput(objects);
		objectSelector.setSelection(selection);

		detailComposite.getComposite().setEnabled(adpModel != null);
		mainComposite.setVisible(adpModel != null);
		objectSelector.getControl().setEnabled(adpModel != null);
		if (adpModel == null) {
			updateDetailPaneInput(null);
		}

	}

	@Override
	public void dispose() {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		if (filterAction != null) {
			filterAction.dispose();
			filterAction = null;
		}
		super.dispose();
	}

	private void updateDetailPaneInput(final EObject object) {
		EObject target = null;
		if (object instanceof final PurchaseContract contract) {
			for (final PurchaseContractProfile profile : editorData.adpModel.getPurchaseContractProfiles()) {
				if (profile.getContract() == contract) {
					target = profile;
				}
			}
			if (target == null) {
				target = ADPModelUtil.createProfile(editorData.scenarioModel, editorData.adpModel, contract);
				if (target != null) {
					final CompoundCommand cmd = new CompoundCommand("Create ADP Profile");
					cmd.append(AddCommand.create(editorData.getEditingDomain(), editorData.adpModel, ADPPackage.eINSTANCE.getADPModel_PurchaseContractProfiles(), Collections.singletonList(target)));
					editorData.getEditingDomain().getCommandStack().execute(cmd);
				}
			}
		} else if (object instanceof final SalesContract contract) {
			for (final SalesContractProfile profile : editorData.adpModel.getSalesContractProfiles()) {
				if (profile.getContract() == contract) {
					target = profile;
				}
			}
			if (target == null) {
				target = ADPModelUtil.createProfile(editorData.scenarioModel, editorData.adpModel, contract);
				if (target != null) {
					final CompoundCommand cmd = new CompoundCommand("Create ADP Profile");
					cmd.append(AddCommand.create(editorData.getEditingDomain(), editorData.adpModel, ADPPackage.eINSTANCE.getADPModel_SalesContractProfiles(), Collections.singletonList(target)));
					editorData.getEditingDomain().getCommandStack().execute(cmd);
				}
			}
		} else if (object instanceof final MullProfile mullProfile) {
			if (editorData.adpModel.getMullProfile() == null) {
				editorData.adpModel.setMullProfile(mullProfile);
			}
			target = object;
		} else if (object instanceof final SpacingProfile spacingProfile) {
			if (editorData.adpModel.getSpacingProfile() == null) {
				editorData.adpModel.setSpacingProfile(spacingProfile);
			}
			target = object;
		}

		detailComposite.setInput(target);
		updatePreviewPaneInput(target);
	}

	private void updatePreviewPaneInput(final EObject target) {
		if (editorData != null && editorData.getScenarioModel() != null) {
			if (previewViewer != null) {
				if (target instanceof final PurchaseContractProfile purchaseContractProfile) {
					final List<Object> o = new LinkedList<>();
					for (LoadSlot s : editorData.getScenarioModel().getCargoModel().getLoadSlots()) {
						if (s.getContract() == purchaseContractProfile.getContract()) {
							o.add(s);
						}
					}
					previewViewer.setInput(o);
					// previewGroup.setVisible(true);
					if (rhsScrolledComposite.getContent() != rhsComposite) {
						rhsScrolledComposite.setContent(rhsComposite);
					}
					previewGroup.layout();
					rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					rhsScrolledComposite.requestLayout();
					// rhsScrolledComposite.layout();
				} else if (target instanceof final SalesContractProfile salesContractProfile) {
					final List<Object> o = new LinkedList<>();
					for (DischargeSlot s : editorData.getScenarioModel().getCargoModel().getDischargeSlots()) {
						if (s.getContract() == salesContractProfile.getContract()) {
							o.add(s);
						}
					}
					previewViewer.setInput(o);
					// previewGroup.setVisible(true);
					if (rhsScrolledComposite.getContent() != rhsComposite) {
						rhsScrolledComposite.setContent(rhsComposite);
					}
					previewGroup.layout();
					rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					rhsScrolledComposite.requestLayout();
					// rhsScrolledComposite.layout();
				} else if (target instanceof MullProfile) {
					// previewGroup.setVisible(false);
					mullSummaryViewer.setInput(target);
					if (rhsScrolledComposite.getContent() != mullComposite) {
						rhsScrolledComposite.setContent(mullComposite);
					}
					mullSummaryGroup.layout();
					// rhsScrolledComposite.setMinSize(mullComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					// rhsScrolledComposite.layout();
					rhsScrolledComposite.requestLayout();
				} else {
					rhsScrolledComposite.setContent(null);
					// rhsScrolledComposite.requestLayout();
					// rhsScrolledComposite.layout();
				}
			}
		}
	}

	private final AdapterImpl commercialModelAdapter = new SafeEContentAdapter() {
		@Override
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {

			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS //
					|| msg.getFeature() == CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS) {
				final CommercialModel commercialModel = (CommercialModel) msg.getNotifier();
				final List<Object> objects = new LinkedList<>();

				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
					MullProfile profile = editorData.adpModel.getMullProfile();
					if (profile == null) {
						profile = ADPFactory.eINSTANCE.createMullProfile();
					}
					objects.add(profile);
				}
				if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_ADP_SPACING_RATEABILITY)) {
					SpacingProfile profile = editorData.adpModel.getSpacingProfile();
					if (profile == null) {
						profile = ADPFactory.eINSTANCE.createSpacingProfile();
					}
					objects.add(profile);
				}

				objects.addAll(commercialModel.getPurchaseContracts());
				objects.addAll(commercialModel.getSalesContracts());

				RunnerHelper.runNowOrAsync(() -> {
					if (objectSelector.getControl().isDisposed()) {
						return;
					}
					Object selected = objectSelector.getStructuredSelection().getFirstElement();
					objectSelector.setInput(objects);
					if (selected != null && !objects.contains(selected)) {
						if (objects.isEmpty()) {
							objectSelector.setSelection(StructuredSelection.EMPTY);
						} else {
							objectSelector.setSelection(new StructuredSelection(objects.get(0)));
						}
						objectSelector.refresh(true);
					}
				});
			} else if (msg.getNotifier() instanceof Contract) {
				RunnerHelper.asyncExec(() -> {
					if (!objectSelector.getControl().isDisposed()) {
						objectSelector.refresh(true);
					}
				});
			}
		}
	};

	private final AdapterImpl cargoModelAdapter = new SafeEContentAdapter() {
		@Override
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {

			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS //
					|| msg.getFeature() == CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS //
					|| msg.getNotifier() instanceof Slot<?>) {

				RunnerHelper.asyncExec(() -> updatePreviewPaneInput(detailComposite.getInput()));
			}
		}
	};

	private Button generateButton;

	private Button cpSolveButton;

	private Button vanillaBuildButton;

	private Group previewGroup;

	private Group mullSummaryGroup;

	private Action deleteSlotAction;

	private static String mapName(final VolumeUnits units) {

		switch (units) {
		case M3:
			return "m³";
		case MMBTU:
			return "mmBtu";
		}
		return units.getName();
	}

	private static String mapName(final TimePeriod units) {

		switch (units) {
		case DAYS:
			return "Days";
		case HOURS:
			return "Hours";
		case MONTHS:
			return "Months";
		default:
			break;

		}
		return units.getName();
	}

	private class ContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(final Object parentElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getParent(final Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(final Object element) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public void setSelectedProfile(ContractProfile<?, ?> p) {
		objectSelector.setSelection(new StructuredSelection(p.getContract()));
	}

	private IContentProvider getMullSummaryContentProvider(final boolean filterToVessels) {
		if (filterToVessels) {
			return new ITreeContentProvider() {

				@Override
				public boolean hasChildren(Object element) {
					return element instanceof MullSubprofile || element instanceof MullEntityRow || element instanceof MullAllocationRow;
				}

				@Override
				public Object getParent(Object element) {
					if (element instanceof final Triple<?, ?, ?> triple) {
						return triple.getThird();
					} else if (element instanceof final EObject eObject) {
						return eObject.eContainer();
					}
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof final MullProfile mullProfile) {
						return mullProfile.getInventories().toArray();
					}
					return null;
				}

				@Override
				public Object[] getChildren(Object parentElement) {
					if (parentElement instanceof final MullSubprofile mullSubprofile) {
						return mullSubprofile.getEntityTable().stream().sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getEntity(), b.getEntity(), UNSPECIFIED_NAME)).toArray();
					} else if (parentElement instanceof final MullEntityRow mullEntityRow) {
						final List<Object> elements = new ArrayList<>();
						elements.addAll(mullEntityRow.getSalesContractAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getContract(), b.getContract(), UNSPECIFIED_NAME)).toList());
						elements.addAll(mullEntityRow.getDesSalesMarketAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getDesSalesMarket(), b.getDesSalesMarket(), UNSPECIFIED_NAME)).toList());
						return elements.toArray();
					} else if (parentElement instanceof final MullAllocationRow mullAllocationRow) {
						final List<Object> elements = new ArrayList<>();
						final List<Vessel> sortedVessels = mullAllocationRow.getVessels().stream().sorted((v1, v2) -> ScenarioElementNameHelper.safeCompareNamedObjects(v1, v2, UNSPECIFIED_NAME))
								.toList();
						for (final Vessel vessel : sortedVessels) {
							elements.add(Triple.of("", vessel, parentElement));
						}
						return elements.toArray();
					}
					return null;
				}
			};
		} else {
			return new ITreeContentProvider() {

				@Override
				public boolean hasChildren(Object element) {
					return (element instanceof final Triple<?, ?, ?> triple && triple.getSecond() instanceof Collection) //
							|| element instanceof MullSubprofile //
							|| element instanceof MullEntityRow //
							|| element instanceof MullAllocationRow;
				}

				@Override
				public Object getParent(Object element) {
					if (element instanceof final Triple<?, ?, ?> triple) {
						return triple.getThird();
					} else if (element instanceof final EObject eObject) {
						return eObject.eContainer();
					}
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof final MullProfile mullProfile) {
						final List<Object> elements = new ArrayList<>();
						elements.add(Triple.of("Full cargo lot value", mullProfile.getFullCargoLotValue(), inputElement));
						elements.add(Triple.of("Cargoes to keep", mullProfile.getCargoesToKeep(), inputElement));
						elements.add(Triple.of("Volume flex", mullProfile.getVolumeFlex(), inputElement));
						elements.add(Triple.of("Window size", mullProfile.getWindowSize(), inputElement));
						for (final MullSubprofile mullSubprofile : mullProfile.getInventories()) {
							elements.add(mullSubprofile);
						}
						return elements.toArray();
					}
					return null;
				}

				@Override
				public Object[] getChildren(Object parentElement) {
					if (parentElement instanceof final Triple<?, ?, ?> triple) {
						if (!(triple.getSecond() instanceof final Collection<?> collectedObjects)) {
							throw new IllegalStateException("Provided triple must have second element as collection");
						}
						if (triple.getFirst().equals("Vessels")) {
							final Object[] elements = new Object[collectedObjects.size()];
							final Iterator<?> iter = collectedObjects.iterator();
							for (int i = 0; i < elements.length; ++i) {
								elements[i] = Triple.of("", iter.next(), triple.getThird());
							}
							return elements;
						} else if (triple.getFirst().equals("Cargoes to keep")) {
							final Object[] elements = new Object[collectedObjects.size()];
							final Iterator<?> iter = collectedObjects.iterator();
							for (int i = 0; i < elements.length; ++i) {
								elements[i] = Triple.of("", iter.next(), triple.getThird());
							}
							return elements;
						} else {
							return collectedObjects.toArray();
						}

					} else if (parentElement instanceof final MullSubprofile mullSubprofile) {
						return mullSubprofile.getEntityTable().stream().sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getEntity(), b.getEntity(), UNSPECIFIED_NAME)).toArray();
					} else if (parentElement instanceof final MullEntityRow mullEntityRow) {
						final List<Object> elements = new ArrayList<>();
						elements.add(Triple.of("Initial allocation", mullEntityRow.getInitialAllocation(), parentElement));
						elements.add(Triple.of("Reference entitlement", mullEntityRow.getRelativeEntitlement(), parentElement));
						elements.addAll(mullEntityRow.getSalesContractAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getContract(), b.getContract(), UNSPECIFIED_NAME)).toList());
						elements.addAll(mullEntityRow.getDesSalesMarketAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getDesSalesMarket(), b.getDesSalesMarket(), UNSPECIFIED_NAME)).toList());
						return elements.toArray();
					} else if (parentElement instanceof final MullAllocationRow mullAllocationRow) {
						final List<Object> elements = new ArrayList<>();
						elements.add(Triple.of("AACQ", mullAllocationRow.getWeight(), parentElement));
						final List<Vessel> sortedVessels = mullAllocationRow.getVessels().stream().sorted((v1, v2) -> ScenarioElementNameHelper.safeCompareNamedObjects(v1, v2, UNSPECIFIED_NAME))
								.toList();
						elements.add(Triple.of("Vessels", sortedVessels, parentElement));
						return elements.toArray();
					}
					return null;
				}
			};
		}
	}
}
