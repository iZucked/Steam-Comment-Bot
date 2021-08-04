/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.ResourceLocator;
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.time.Hours;
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
import com.mmxlabs.models.lng.adp.mull.AllocationDropType;
import com.mmxlabs.models.lng.adp.mull.AllocationTracker;
import com.mmxlabs.models.lng.adp.mull.CargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.CombinedCargoBlueprintIterator;
import com.mmxlabs.models.lng.adp.mull.DESMarketTracker;
import com.mmxlabs.models.lng.adp.mull.FilterToVesselsAction;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.MUDContainer;
import com.mmxlabs.models.lng.adp.mull.MULLContainer;
import com.mmxlabs.models.lng.adp.mull.Phase1AComparator;
import com.mmxlabs.models.lng.adp.mull.Phase1AComparator2;
import com.mmxlabs.models.lng.adp.mull.RollingLoadWindow;
import com.mmxlabs.models.lng.adp.mull.SalesContractTracker;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.InventoryFrequency;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ui.commands.ScheduleModelInvalidateCommandProvider;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.registries.IModelFactoryRegistry;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualSingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.ecore.SafeEContentAdapter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ContractPage extends ADPComposite {

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
						} else if (element instanceof PurchaseContract) {
							final PurchaseContract profile = (PurchaseContract) element;
							return String.format("%s (Purchase)", profile.getName());
						} else if (element instanceof SalesContract) {
							final SalesContract profile = (SalesContract) element;
							return String.format("%s (Sale)", profile.getName());
						}
						return super.getText(element);
					}
				});
				objectSelector.setInput(Collections.emptyList());
				objectSelector.addSelectionChangedListener(event -> {
					final ISelection selection = event.getSelection();
					EObject target = null;
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
						target = (EObject) iStructuredSelection.getFirstElement();
						generateButton.setEnabled(target instanceof Contract || target instanceof Inventory || target instanceof MullProfile);
					}
					generateButton.setEnabled(target != null);
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
						if (input instanceof PurchaseContractProfile) {
							final PurchaseContractProfile profile = (PurchaseContractProfile) input;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");

							final Command populateModelCommand = ADPModelUtil.populateModel(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							if (populateModelCommand != null) {
								cmd.append(populateModelCommand);
								editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
								input = profile.getContract();
							}
						} else if (input instanceof SalesContractProfile) {
							final SalesContractProfile profile = (SalesContractProfile) input;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");

							final Command populateModelCommand = ADPModelUtil.populateModel(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							if (populateModelCommand != null) {
								cmd.append(populateModelCommand);
								editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
								input = profile.getContract();
							}
						} else if (input instanceof MullProfile) {
							final MullProfile profile = (MullProfile) input;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");
							final Command populateModelCommand = populateModelFromMultipleInventories(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							cmd.append(populateModelCommand);
						}
						updateDetailPaneInput(input);
					}
				});
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
								if (selection instanceof IStructuredSelection) {
									final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
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
					deleteSlotAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
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

						ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);

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

						ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.gif").ifPresent(packAction::setImageDescriptor);

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
						ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/collapseall.gif").ifPresent(collapseOneLevel::setImageDescriptor);
						ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/expandall.gif").ifPresent(expandOneLevel::setImageDescriptor);
						// collapseOneLevel.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/collapseall.gif"));
						// expandOneLevel.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/expandall.gif"));

						toolbarManager.getToolbarManager().add(collapseOneLevel);
						toolbarManager.getToolbarManager().add(expandOneLevel);
						toolbarManager.getToolbarManager().update(true);

						mullSummaryAdapter = new EContentAdapter() {
							@Override
							public void notifyChanged(final Notification notification) {
								super.notifyChanged(notification);
								final Object notifier = notification.getNotifier();
								ViewerHelper.refresh(mullSummaryViewer, false);
							}

							@Override
							protected void setTarget(final EObject target) {
								basicSetTarget(target);
								if (target instanceof ADPModel) {
									basicSetTarget(target);
									addAdapter(target);
									final MullProfile mullProfile = ((ADPModel) target).getMullProfile();
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
								if (target instanceof ADPModel) {
									removeAdapter(target, false, true);
									final MullProfile mullProfile = ((ADPModel) target).getMullProfile();
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
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
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
				if (element instanceof Triple) {
					return ((Triple<String, ?, ?>) element).getFirst();
				} else if (element instanceof MullSubprofile) {
					return ScenarioElementNameHelper.getName(((MullSubprofile) element).getInventory(), "<Not specified>");
				} else if (element instanceof MullEntityRow) {
					return ScenarioElementNameHelper.getName(((MullEntityRow) element).getEntity(), "<Not specified>");
				} else if (element instanceof SalesContractAllocationRow) {
					return ScenarioElementNameHelper.getName(((SalesContractAllocationRow) element).getContract(), "<Not specified>");
				} else if (element instanceof DESSalesMarketAllocationRow) {
					return ScenarioElementNameHelper.getName(((DESSalesMarketAllocationRow) element).getDesSalesMarket(), "<Not specified>");
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
					if (secondElement instanceof Vessel) {
						return ScenarioElementNameHelper.getName((Vessel) secondElement, "<Not specified>");
					} else if (secondElement instanceof MullCargoWrapper) {
						final MullCargoWrapper mullCargoWrapper = (MullCargoWrapper) secondElement;
						final String entityName = ScenarioElementNameHelper.getName(mullCargoWrapper.getLoadSlot().getEntity(), "<Not specified>");
						final PurchaseContract loadContract = mullCargoWrapper.getLoadSlot().getContract();
						final String loadContractName = loadContract != null ? ScenarioElementNameHelper.getName(loadContract, "<Not specified") : "<Not specified>";
						final DischargeSlot dischargeSlot = mullCargoWrapper.getDischargeSlot();
						final String dischargeName;
						if (dischargeSlot instanceof SpotDischargeSlot) {
							final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) dischargeSlot;
							if (spotDischargeSlot.getMarket() != null) {
								dischargeName = ScenarioElementNameHelper.getName(spotDischargeSlot.getMarket(), "<Not specified>");
							} else {
								dischargeName = spotDischargeSlot.getPriceExpression() != null ? spotDischargeSlot.getPriceExpression() : "<Not specified>";
							}
						} else {
							if (dischargeSlot.getContract() != null) {
								dischargeName = ScenarioElementNameHelper.getName(dischargeSlot.getContract(), "<Not specified>");
							} else {
								dischargeName = dischargeSlot.getPriceExpression() != null ? dischargeSlot.getPriceExpression() : "<Not specified>";
							}
						}
						final LocalDate loadDate = mullCargoWrapper.getLoadSlot().getWindowStart();
						final String loadDateString = loadDate != null ? String.format("%01d/%01d/%d", loadDate.getDayOfMonth(), loadDate.getMonthValue(), loadDate.getYear()) : "<Not specified>";
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

	private Command populateModelFromMultipleInventories(@NonNull final EditingDomain editingDomain, @NonNull final LNGScenarioModel sm, final ADPModel adpModel,
			@NonNull final MullProfile originalProfile) {
		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");

		final IModelFactoryRegistry modelFactoryRegistry = Activator.getDefault().getModelFactoryRegistry();
		if (modelFactoryRegistry == null) {
			throw new IllegalStateException("Factory registry must not be null");
		}

		final CargoEditingCommands cec = new CargoEditingCommands(editingDomain, sm, ScenarioModelUtil.getCargoModel(sm), ScenarioModelUtil.getCommercialModel(sm), modelFactoryRegistry);

		final MullProfile profile = trimZeroesFromMullProfile(originalProfile);

		final int loadWindow = profile.getWindowSize();
		final int volumeFlex = profile.getVolumeFlex();
		final int fullCargoLotValue = profile.getFullCargoLotValue();
		// Default value shouldn't be used
		final int cargoVolume = 160_000;
		final List<MullCargoWrapper> cargoesToKeep = new ArrayList<>(originalProfile.getCargoesToKeep());

		final LocalDate startDate = adpModel.getYearStart().atDay(1);
		final LocalDateTime startDateTime = startDate.atStartOfDay();
		final LocalDateTime endDateTimeExclusive = adpModel.getYearEnd().atDay(1).atStartOfDay();

		final boolean debugFlex = false;

		final int loadWindowHours = loadWindow * 24;

		// Reused objects
		final Set<BaseLegalEntity> firstPartyEntities = ScenarioModelUtil.getCommercialModel(sm).getEntities().stream() //
				.filter(e -> !e.isThirdParty()) //
				.collect(Collectors.toSet());
		final LocalDate dayBeforeADPStart = adpModel.getYearStart().atDay(1).minusDays(1);
		final LocalDateTime dateTimeBeforeADPStart = dayBeforeADPStart.atStartOfDay();
		final Map<Inventory, Integer> yearlyProductions = new HashMap<>();
		final Map<Inventory, PurchaseContract> inventoryPurchaseContracts = new HashMap<>();
		final Map<PurchaseContract, Inventory> purchaseContractToInventory = new HashMap<>();

		for (final MullSubprofile mullSubprofile : profile.getInventories()) {
			final Inventory inventory = mullSubprofile.getInventory();
			final PurchaseContract purchaseContract = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts().stream().filter(c -> inventory.getPort().equals(c.getPreferredPort())).findFirst()
					.get();
			inventoryPurchaseContracts.put(inventory, purchaseContract);
			purchaseContractToInventory.put(purchaseContract, inventory);
		}
		final CharterInMarket adpNominalMarket = adpModel.getFleetProfile().getDefaultNominalMarket();
		// End of shared objects

		// Phase 1 - Generate cargoes favouring sales contracts - count of ship use
		// should stay similar

		// Start of phase 1
		final Map<Vessel, LocalDateTime> phase1VesselToMostRecentUseDateTime = new HashMap<>();
		final Map<Inventory, MULLContainer> phase1MullContainers = new HashMap<>();
		final Map<Inventory, TreeMap<LocalDateTime, InventoryDateTimeEvent>> phase1InventoryHourlyInsAndOuts = new HashMap<>();
		final Map<Inventory, LinkedList<CargoBlueprint>> phase1CargoBlueprintsToGenerate = new HashMap<>();
		final Map<Inventory, Integer> phase1InventorySlotCounters = new HashMap<>();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> phase1IterSwap = new LinkedList<>();
		final Map<Inventory, RollingLoadWindow> phase1InventoryRollingWindows = new HashMap<>();

		for (final MullSubprofile subprofile : profile.getInventories()) {
			final Inventory currentInventory = subprofile.getInventory();
			phase1MullContainers.put(currentInventory, new MULLContainer(subprofile, fullCargoLotValue));

			for (MullEntityRow row : subprofile.getEntityTable()) {
				Stream.concat(row.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						row.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream))
						.forEach(vessel -> phase1VesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}

			// Build ins and outs
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> currentInsAndOuts = getInventoryInsAndOutsHourly(currentInventory, sm, startDateTime, endDateTimeExclusive);
			int totalInventoryVolume = 0;
			while (currentInsAndOuts.firstKey().isBefore(startDateTime)) {
				final InventoryDateTimeEvent event = currentInsAndOuts.remove(currentInsAndOuts.firstKey());
				totalInventoryVolume += event.getNetVolumeIn();
			}
			currentInsAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
			phase1InventoryHourlyInsAndOuts.put(currentInventory, currentInsAndOuts);

			// Flagged existing cargo dates
			final Port inventoryPort = currentInventory.getPort();
			final LocalDate dayBeforeStart = startDate.minusDays(1);

			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = currentInsAndOuts.entrySet().iterator();
			phase1IterSwap.add(Pair.of(currentInventory, hourlyIter));

			phase1InventorySlotCounters.put(currentInventory, 0);

			phase1CargoBlueprintsToGenerate.put(currentInventory, new LinkedList<>());

			// Phase 1 should not use fixed cargoes to get an accurate picture of expected numbers for the year in question.
			final int inventoryLoadDuration = inventoryPort.getLoadDuration();
			phase1InventoryRollingWindows.put(currentInventory, new RollingLoadWindow(inventoryLoadDuration, hourlyIter, inventoryLoadDuration));
		}
		final Set<Vessel> phase1FirstPartyVessels = phase1MullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.filter(mudContainer -> firstPartyEntities.contains(mudContainer.getEntity())).flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.flatMap(allocationTracker -> allocationTracker.getVessels().stream()) //
				.collect(Collectors.toSet());
		phase1MullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(phase1FirstPartyVessels));

		// Phase 1 doesn't use a fixed cargoes so this empty map behaves as a null object - this should be refactored so that we don't need this
		final Map<Vessel, LocalDateTime> phase1ForwardLookahead = new HashMap<>();

		// End of phase 1 initialisation
		// Start of phase 1 cargo generation
		for (LocalDateTime phase1DateTime = startDateTime; phase1DateTime.isBefore(endDateTimeExclusive); phase1DateTime = phase1DateTime.plusHours(1)) {
			for (Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> phase1Curr : phase1IterSwap) {
				final Inventory currentInventory = phase1Curr.getFirst();
				final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIter = phase1Curr.getSecond();

				final RollingLoadWindow currentLoadWindow = phase1InventoryRollingWindows.get(currentInventory);
				final InventoryDateTimeEvent newEndWindowEvent;
				if (currentIter.hasNext()) {
					newEndWindowEvent = currentIter.next().getValue();
				} else {
					final InventoryDateTimeEvent previousDateTimeEvent = currentLoadWindow.getLastEvent();
					newEndWindowEvent = new InventoryDateTimeEvent(phase1DateTime, 0, previousDateTimeEvent.minVolume, previousDateTimeEvent.maxVolume);
				}

				final InventoryDateTimeEvent currentEvent = currentLoadWindow.getCurrentEvent();
				final MULLContainer currentMULLContainer = phase1MullContainers.get(currentInventory);
				currentMULLContainer.updateRunningAllocation(currentEvent.getNetVolumeIn());

				final LocalDateTime currentDateTime = currentLoadWindow.getStartDateTime();
				assert currentDateTime.equals(phase1DateTime);

				if (isAtStartHourOfMonth(phase1DateTime)) {
					final YearMonth currentYM = YearMonth.from(phase1DateTime);
					final int monthIn = phase1InventoryHourlyInsAndOuts.get(currentInventory).entrySet().stream() //
							.filter(p -> YearMonth.from(p.getKey()).equals(currentYM)) //
							.mapToInt(p -> p.getValue().getNetVolumeIn()) //
							.sum();
					currentMULLContainer.updateCurrentMonthAbsoluteEntitlement(monthIn);
				}
				if (!currentLoadWindow.isLoading()) {
					final MUDContainer mullMUDContainer = currentMULLContainer.phase1CalculateMULL(phase1VesselToMostRecentUseDateTime, phase1ForwardLookahead, cargoVolume, phase1FirstPartyVessels,
							currentDateTime);
					final AllocationTracker mudAllocationTracker = mullMUDContainer.phase1CalculateMUDAllocationTracker();
					final List<Vessel> mudVesselRestrictions = mudAllocationTracker.getVessels();
					final int currentAllocationDrop;
					final Vessel assignedVessel;
					if (!mudVesselRestrictions.isEmpty()) {
						assignedVessel = mudVesselRestrictions.stream().min((v1, v2) -> phase1VesselToMostRecentUseDateTime.get(v1).compareTo(phase1VesselToMostRecentUseDateTime.get(v2))).get();
						currentAllocationDrop = mudAllocationTracker.calculateExpectedAllocationDrop(assignedVessel, currentInventory.getPort().getLoadDuration(),
								phase1FirstPartyVessels.contains(assignedVessel));
					} else {
						assignedVessel = null;
						currentAllocationDrop = cargoVolume;
					}
					if (currentLoadWindow.canLift(currentAllocationDrop)) {
						currentLoadWindow.startLoad(currentAllocationDrop);
						mullMUDContainer.dropAllocation(currentAllocationDrop);
						mudAllocationTracker.phase1DropAllocation(currentAllocationDrop);
						mullMUDContainer.reassessAACQSatisfaction();

						int nextLoadCount = phase1InventorySlotCounters.get(currentInventory);

						final int volumeHigh;
						final int volumeLow;
						if (debugFlex) {
							final int flexDifference = currentLoadWindow.getEndWindowVolume() - currentAllocationDrop - currentLoadWindow.getEndWindowTankMin();
							assert flexDifference >= 0;
							volumeHigh = currentAllocationDrop;
							volumeLow = currentAllocationDrop - volumeFlex;
						} else {
							volumeHigh = currentAllocationDrop + volumeFlex;
							volumeLow = currentAllocationDrop - volumeFlex;
						}
						final CargoBlueprint currentCargoBlueprint = new CargoBlueprint(currentInventory, inventoryPurchaseContracts.get(currentInventory), nextLoadCount, assignedVessel,
								currentLoadWindow.getStartDateTime(), loadWindowHours, mudAllocationTracker, currentAllocationDrop, mullMUDContainer.getEntity(), volumeHigh, volumeLow);
						if (!phase1CargoBlueprintsToGenerate.get(currentInventory).isEmpty()) {
							final CargoBlueprint previousCargoBlueprint = phase1CargoBlueprintsToGenerate.get(currentInventory).getLast();
							final LocalDateTime earliestPreviousStart = currentLoadWindow.getStartDateTime().minusHours(currentInventory.getPort().getLoadDuration());
							final int newWindowHours = Hours.between(previousCargoBlueprint.getWindowStart(), earliestPreviousStart);
							previousCargoBlueprint.updateWindowSize(newWindowHours);
						}
						phase1CargoBlueprintsToGenerate.get(currentInventory).add(currentCargoBlueprint);
						phase1InventorySlotCounters.put(currentInventory, nextLoadCount + 1);
						if (assignedVessel != null) {
							phase1VesselToMostRecentUseDateTime.put(assignedVessel, currentCargoBlueprint.getWindowStart());
						}
					}
				}
				currentLoadWindow.stepForward(newEndWindowEvent);
			}
		}
		// End of phase 1 cargo generation

		final Map<Inventory, Map<BaseLegalEntity, Map<SalesContractTracker, Integer>>> salesContractInspect = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Map<DESMarketTracker, Integer>>> desMarketInspect = new HashMap<>();
		for (final Entry<Inventory, LinkedList<CargoBlueprint>> entry : phase1CargoBlueprintsToGenerate.entrySet()) {
			salesContractInspect.put(entry.getKey(), new HashMap<>());
			desMarketInspect.put(entry.getKey(), new HashMap<>());
			for (final CargoBlueprint cargoBlueprint : entry.getValue()) {
				if (cargoBlueprint.getAllocationTracker() instanceof SalesContractTracker) {
					final Map<SalesContractTracker, Integer> scMap = salesContractInspect.get(entry.getKey()).computeIfAbsent(cargoBlueprint.getEntity(), k -> new HashMap<>());
					scMap.compute((SalesContractTracker) cargoBlueprint.getAllocationTracker(), (k, v) -> v == null ? 1 : v + 1);
				} else {
					final Map<DESMarketTracker, Integer> dsmMap = desMarketInspect.get(entry.getKey()).computeIfAbsent(cargoBlueprint.getEntity(), k -> new HashMap<>());
					dsmMap.compute((DESMarketTracker) cargoBlueprint.getAllocationTracker(), (k, v) -> v == null ? 1 : v + 1);
				}
			}
		}

		for (final Inventory inventory : phase1CargoBlueprintsToGenerate.keySet()) {
			for (final MUDContainer mudContainer : phase1MullContainers.get(inventory).getMUDContainers()) {
				final BaseLegalEntity entity = mudContainer.getEntity();
				final Map<BaseLegalEntity, Map<DESMarketTracker, Integer>> desTrackers = desMarketInspect.get(inventory);
				if (!desTrackers.containsKey(entity)) {
					final HashMap<DESMarketTracker, Integer> map = new HashMap<>();
					mudContainer.getAllocationTrackers().stream().filter(DESMarketTracker.class::isInstance).map(DESMarketTracker.class::cast).forEach(tracker -> map.put(tracker, 0));
					desTrackers.put(entity, map);
				}
			}
		}

		// Phase 1 debug check
		// for (final Inventory inventory : phase1CargoBlueprintsToGenerate.keySet()) {
		// System.out.println(String.format("For inventory %s", inventory.getName()));
		// for (final MUDContainer mudContainer : phase1MullContainers.get(inventory).getMUDContainers()) {
		// final BaseLegalEntity entity = mudContainer.getEntity();
		// System.out.println(String.format("\tFor %s", entity.getName()));
		// final Map<SalesContractTracker, Integer> currSCMap = salesContractInspect.get(inventory).get(entity);
		// if (currSCMap != null) {
		// for (Entry<SalesContractTracker, Integer> entry : currSCMap.entrySet()) {
		// System.out.println(String.format("\t\tSC:%s. Expected: %d. Actual: %d", entry.getKey().getSalesContract().getName(), entry.getKey().getAACQ(), entry.getValue()));
		// }
		// }
		// for (Entry<DESMarketTracker, Integer> entry : desMarketInspect.get(inventory).computeIfAbsent(entity, k -> {
		// final HashMap<DESMarketTracker, Integer> map = new HashMap<>();
		// mudContainer.getAllocationTrackers().stream().filter(DESMarketTracker.class::isInstance).map(DESMarketTracker.class::cast).forEach(tracker -> map.put(tracker, 0));
		// return map;
		// }).entrySet()) {
		// System.out.println(String.format("\t\tDSM:%s. Expected: %d. Actual: %d", entry.getKey().getDESSalesMarket().getName(), entry.getKey().getAACQ(), entry.getValue()));
		// }
		// }
		// }
		// end of phase 1 debug check

		// End of phase 1

		// Phase 2 - Pull AACQs derived from phase 1 and use this to generate a
		// harmonised schedule. Generate cargoes favouring sales contracts
		// Start of phase 2

		// Start of phase 1 to phase 2 transistion
		final MullProfile phase1RevisedProfile = ADPFactory.eINSTANCE.createMullProfile();
		phase1RevisedProfile.setFullCargoLotValue(profile.getFullCargoLotValue());
		phase1RevisedProfile.setVolumeFlex(profile.getVolumeFlex());
		phase1RevisedProfile.setWindowSize(profile.getWindowSize());
		for (final MullSubprofile mullSubprofile : profile.getInventories()) {
			final MullSubprofile replacementSubprofile = ADPFactory.eINSTANCE.createMullSubprofile();
			replacementSubprofile.setInventory(mullSubprofile.getInventory());
			for (final MullEntityRow mullEntityRow : mullSubprofile.getEntityTable()) {
				final MullEntityRow replacementEntityRow = ADPFactory.eINSTANCE.createMullEntityRow();
				replacementEntityRow.setEntity(mullEntityRow.getEntity());
				replacementEntityRow.setInitialAllocation(mullEntityRow.getInitialAllocation());
				replacementEntityRow.setRelativeEntitlement(mullEntityRow.getRelativeEntitlement());
				for (final SalesContractAllocationRow salesContractAllocationRow : mullEntityRow.getSalesContractAllocationRows()) {
					final SalesContractAllocationRow replacementSalesContractAllocationRow = ADPFactory.eINSTANCE.createSalesContractAllocationRow();
					replacementSalesContractAllocationRow.setContract(salesContractAllocationRow.getContract());
					replacementSalesContractAllocationRow.setWeight(salesContractAllocationRow.getWeight());
					replacementSalesContractAllocationRow.getVessels().addAll(salesContractAllocationRow.getVessels());
					replacementEntityRow.getSalesContractAllocationRows().add(replacementSalesContractAllocationRow);
				}
				for (final DESSalesMarketAllocationRow desSalesMarketAllocationRow : mullEntityRow.getDesSalesMarketAllocationRows()) {
					final DESSalesMarketAllocationRow replacementDesSalesMarketAllocationRow = ADPFactory.eINSTANCE.createDESSalesMarketAllocationRow();
					replacementDesSalesMarketAllocationRow.setDesSalesMarket(desSalesMarketAllocationRow.getDesSalesMarket());
					replacementDesSalesMarketAllocationRow.setWeight(desMarketInspect.get(mullSubprofile.getInventory()).get(mullEntityRow.getEntity()).entrySet().stream()
							.filter(entry -> entry.getKey().getDESSalesMarket().equals(desSalesMarketAllocationRow.getDesSalesMarket())).mapToInt(Entry::getValue).findFirst().getAsInt());
					replacementDesSalesMarketAllocationRow.getVessels().addAll(desSalesMarketAllocationRow.getVessels());
					replacementEntityRow.getDesSalesMarketAllocationRows().add(replacementDesSalesMarketAllocationRow);
				}
				replacementSubprofile.getEntityTable().add(replacementEntityRow);
			}
			phase1RevisedProfile.getInventories().add(replacementSubprofile);
		}

		// Start of phase 1a
		final Map<Vessel, LocalDateTime> phase1aVesselToMostRecentUseDateTime = new HashMap<>();
		final Map<Inventory, MULLContainer> phase1aMullContainers = new HashMap<>();
		final Map<Inventory, TreeMap<LocalDateTime, InventoryDateTimeEvent>> phase1aInventoryHourlyInsAndOuts = new HashMap<>();
		final Map<Inventory, LinkedList<CargoBlueprint>> phase1aCargoBluePrintsToGenerate = new HashMap<>();
		final Map<Inventory, Integer> phase1aInventorySlotCounters = new HashMap<>();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> phase1aIterSwap = new LinkedList<>();
		final Map<Inventory, RollingLoadWindow> phase1aInventoryRollingWindows = new HashMap<>();

		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Long>>> phase1aMonthEndEntitlements = new HashMap<>();
		final Map<Inventory, Map<AllocationTracker, Map<YearMonth, Long>>> phase1aMonthEndAllocationTrackerEntitlements = new HashMap<>();
		for (final MullSubprofile subprofile : phase1RevisedProfile.getInventories()) {
			final Inventory currentInventory = subprofile.getInventory();
			phase1aMullContainers.put(currentInventory, new MULLContainer(subprofile, fullCargoLotValue));

			for (MullEntityRow row : subprofile.getEntityTable()) {
				Stream.concat(row.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						row.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream))
						.forEach(vessel -> phase1aVesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> currentInsAndOuts = getInventoryInsAndOutsHourly(currentInventory, sm, startDateTime, endDateTimeExclusive);
			int totalInventoryVolume = 0;
			while (currentInsAndOuts.firstKey().isBefore(startDateTime)) {
				final InventoryDateTimeEvent event = currentInsAndOuts.remove(currentInsAndOuts.firstKey());
				totalInventoryVolume += event.getNetVolumeIn();
			}
			currentInsAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
			phase1aInventoryHourlyInsAndOuts.put(currentInventory, currentInsAndOuts);

			final Port inventoryPort = currentInventory.getPort();
			final LocalDate dayBeforeStart = startDate.minusDays(1);

			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = currentInsAndOuts.entrySet().iterator();
			phase1aIterSwap.add(Pair.of(currentInventory, hourlyIter));
			phase1aInventorySlotCounters.put(currentInventory, 0);
			phase1aCargoBluePrintsToGenerate.put(currentInventory, new LinkedList<>());

			final int inventoryLoadDuration = inventoryPort.getLoadDuration();
			phase1aInventoryRollingWindows.put(currentInventory, new RollingLoadWindow(inventoryLoadDuration, hourlyIter, inventoryLoadDuration));
		}
		for (final Entry<Inventory, MULLContainer> entry : phase1aMullContainers.entrySet()) {
			final Inventory inventory = entry.getKey();
			phase1aMonthEndEntitlements.put(inventory, new HashMap<>());
			phase1aMonthEndAllocationTrackerEntitlements.put(inventory, new HashMap<>());
			for (final MUDContainer mudContainer : entry.getValue().getMUDContainers()) {
				phase1aMonthEndEntitlements.get(inventory).put(mudContainer.getEntity(), new HashMap<>());
				for (final AllocationTracker alloc : mudContainer.getAllocationTrackers()) {
					phase1aMonthEndAllocationTrackerEntitlements.get(inventory).put(alloc, new HashMap<>());
				}
			}
		}
		phase1aMullContainers.entrySet().stream().flatMap(e -> e.getValue().getMUDContainers().stream()).flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream())
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(phase1FirstPartyVessels));

		// phase 1a doesn't use the lookahead so object below behaves as a null object - this should be refactored so that this object doesn't need to exist
		final Map<Vessel, LocalDateTime> phase1aForwardLookahead = new HashMap<>();

		for (LocalDateTime phase1aDateTime = startDateTime; phase1aDateTime.isBefore(endDateTimeExclusive); phase1aDateTime = phase1aDateTime.plusHours(1)) {
			for (Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> phase1aCurr : phase1aIterSwap) {
				final Inventory currentInventory = phase1aCurr.getFirst();
				final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIter = phase1aCurr.getSecond();

				final RollingLoadWindow currentLoadWindow = phase1aInventoryRollingWindows.get(currentInventory);
				final InventoryDateTimeEvent newEndWindowEvent;
				if (currentIter.hasNext()) {
					newEndWindowEvent = currentIter.next().getValue();
				} else {
					final InventoryDateTimeEvent previousDateTimeEvent = currentLoadWindow.getLastEvent();
					newEndWindowEvent = new InventoryDateTimeEvent(phase1aDateTime, 0, previousDateTimeEvent.minVolume, previousDateTimeEvent.maxVolume);
				}

				final InventoryDateTimeEvent currentEvent = currentLoadWindow.getCurrentEvent();
				final MULLContainer currentMULLContainer = phase1aMullContainers.get(currentInventory);
				currentMULLContainer.updateRunningAllocation(currentEvent.getNetVolumeIn());

				final LocalDateTime currentDateTime = currentLoadWindow.getStartDateTime();
				assert currentDateTime.equals(phase1aDateTime);

				if (isAtStartHourOfMonth(phase1aDateTime)) {
					final YearMonth currentYM = YearMonth.from(phase1aDateTime);
					final int monthIn = phase1aInventoryHourlyInsAndOuts.get(currentInventory).entrySet().stream().filter(p -> YearMonth.from(p.getKey()).equals(currentYM))
							.mapToInt(p -> p.getValue().getNetVolumeIn()).sum();
					currentMULLContainer.updateCurrentMonthAbsoluteEntitlement(monthIn);
				}
				if (!currentLoadWindow.isLoading()) {
					final MUDContainer mullMUDContainer = currentMULLContainer.calculateMULL(phase1aVesselToMostRecentUseDateTime, phase1aForwardLookahead, cargoVolume, phase1FirstPartyVessels,
							currentDateTime);
					final AllocationTracker mudAllocationTracker = mullMUDContainer.calculateMUDAllocationTracker();
					final List<Vessel> mudVesselRestrictions = mudAllocationTracker.getVessels();
					final int currentAllocationDrop;
					final Vessel assignedVessel;
					if (!mudVesselRestrictions.isEmpty()) {
						assignedVessel = mudVesselRestrictions.stream().min((v1, v2) -> phase1aVesselToMostRecentUseDateTime.get(v1).compareTo(phase1aVesselToMostRecentUseDateTime.get(v2))).get();
						currentAllocationDrop = mudAllocationTracker.calculateExpectedAllocationDrop(assignedVessel, currentInventory.getPort().getLoadDuration(),
								phase1FirstPartyVessels.contains(assignedVessel));
					} else {
						assignedVessel = null;
						currentAllocationDrop = cargoVolume;
					}
					if (currentLoadWindow.canLift(currentAllocationDrop)) {
						currentLoadWindow.startLoad(currentAllocationDrop);
						mullMUDContainer.dropAllocation(currentAllocationDrop);
						mudAllocationTracker.phase2DropAllocation(currentAllocationDrop);

						int nextLoadCount = phase1aInventorySlotCounters.get(currentInventory);

						final int volumeHigh;
						final int volumeLow;
						if (debugFlex) {
							final int flexDifference = currentLoadWindow.getEndWindowVolume() - currentAllocationDrop - currentLoadWindow.getEndWindowTankMin();
							assert flexDifference >= 0;
							volumeHigh = currentAllocationDrop;
							volumeLow = currentAllocationDrop - volumeFlex;
						} else {
							volumeHigh = currentAllocationDrop + volumeFlex;
							volumeLow = currentAllocationDrop - volumeFlex;
						}
						final CargoBlueprint currentCargoBlueprint = new CargoBlueprint(currentInventory, inventoryPurchaseContracts.get(currentInventory), nextLoadCount, assignedVessel,
								currentLoadWindow.getStartDateTime(), loadWindowHours, mudAllocationTracker, currentAllocationDrop, mullMUDContainer.getEntity(), volumeHigh, volumeLow);
						if (!phase1aCargoBluePrintsToGenerate.get(currentInventory).isEmpty()) {
							final CargoBlueprint previousCargoBlueprint = phase1aCargoBluePrintsToGenerate.get(currentInventory).getLast();
							final LocalDateTime earliestPreviousStart = currentLoadWindow.getStartDateTime().minusHours(currentInventory.getPort().getLoadDuration());
							final int newWindowHours = Hours.between(previousCargoBlueprint.getWindowStart(), earliestPreviousStart);
							previousCargoBlueprint.updateWindowSize(newWindowHours);
						}
						phase1aCargoBluePrintsToGenerate.get(currentInventory).add(currentCargoBlueprint);
						phase1aInventorySlotCounters.put(currentInventory, nextLoadCount + 1);
						if (assignedVessel != null) {
							phase1aVesselToMostRecentUseDateTime.put(assignedVessel, currentCargoBlueprint.getWindowStart());
						}
					}
				}
				// Update here since we might lift a cargo
				if (isAtEndHourOfMonth(phase1aDateTime)) {
					final YearMonth currentYM = YearMonth.from(phase1aDateTime);
					final Map<BaseLegalEntity, Map<YearMonth, Long>> currentEntityMonthEndEntitlements = phase1aMonthEndEntitlements.get(currentInventory);
					final Map<AllocationTracker, Map<YearMonth, Long>> currentAllocationTrackerMonthEndEntitlements = phase1aMonthEndAllocationTrackerEntitlements.get(currentInventory);
					for (final MUDContainer mudContainer : currentMULLContainer.getMUDContainers()) {
						currentEntityMonthEndEntitlements.get(mudContainer.getEntity()).put(currentYM, mudContainer.getRunningAllocation());
						for (final AllocationTracker alloc : mudContainer.getAllocationTrackers()) {
							currentAllocationTrackerMonthEndEntitlements.get(alloc).put(currentYM, alloc.getRunningAllocation());
						}
					}
				}
				currentLoadWindow.stepForward(newEndWindowEvent);
			}
		}

		// Phase 1a monthly cargo count
		// Find fixed cargoes that are closest to the fixed cargoes - greedy approach

		final Set<CargoBlueprint> sameMonthShiftCargoBlueprints = new HashSet<>();
		final List<Pair<CargoBlueprint, LocalDateTime>> differentMonthShiftCargoBlueprints = new ArrayList<>();
		final Set<CargoBlueprint> usedCargoBlueprints = new HashSet<>();
		for (final MullCargoWrapper fixedCargoWrapper : cargoesToKeep) {
			final BaseLegalEntity entity = fixedCargoWrapper.getLoadSlot().getEntity();
			final DischargeSlot dischargeSlot = fixedCargoWrapper.getDischargeSlot();
			final Inventory inventory = purchaseContractToInventory.get(fixedCargoWrapper.getLoadSlot().getContract());
			if (inventory == null) {
				continue;
			}

			AllocationTracker allocationTracker = null;
			for (MUDContainer mudContainer : phase1aMullContainers.get(inventory).getMUDContainers()) {
				if (mudContainer.getEntity().equals(entity)) {
					if (dischargeSlot.getContract() != null) {
						final SalesContract expectedContract = dischargeSlot.getContract();
						final Optional<SalesContractTracker> optSct = mudContainer.getAllocationTrackers().stream().filter(SalesContractTracker.class::isInstance).map(SalesContractTracker.class::cast)
								.filter(sct -> expectedContract.equals(sct.getSalesContract())).findAny();
						if (optSct.isPresent()) {
							allocationTracker = optSct.get();
						}
					} else if (dischargeSlot.isFOBSale()) {
						allocationTracker = mudContainer.getAllocationTrackers().stream().filter(DESMarketTracker.class::isInstance).findAny().get();
					} else if (dischargeSlot instanceof SpotDischargeSlot) {
						final Port expectedPort = dischargeSlot.getPort();
						final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) dischargeSlot;
						final SpotMarket spotMarket = spotDischargeSlot.getMarket();
						if (spotMarket instanceof DESSalesMarket) {
							final DESSalesMarket expectedMarket = (DESSalesMarket) spotMarket;
							final Optional<DESMarketTracker> optDmt = mudContainer.getAllocationTrackers().stream().filter(DESMarketTracker.class::isInstance).map(DESMarketTracker.class::cast)
									.filter(dmt -> expectedMarket.equals(dmt.getDESSalesMarket())).findAny();
							if (optDmt.isPresent()) {
								allocationTracker = optDmt.get();
							}
						}
					}
				}
			}

			if (allocationTracker != null) {
				final LocalDateTime liftingDateTime = LocalDateTime.of(fixedCargoWrapper.getLoadSlot().getWindowStart(), LocalTime.of(fixedCargoWrapper.getLoadSlot().getWindowStartTime(), 0));
				CargoBlueprint previousCargoBlueprint = null;
				boolean foundMatch = false;
				for (final CargoBlueprint cargoBlueprint : phase1aCargoBluePrintsToGenerate.get(inventory)) {
					if (!cargoBlueprint.getEntity().equals(entity) || !cargoBlueprint.getAllocationTracker().equals(allocationTracker) || usedCargoBlueprints.contains(cargoBlueprint)) {
						continue;
					}
					final LocalDateTime currentDateTime = cargoBlueprint.getWindowStart();
					if (currentDateTime.isBefore(liftingDateTime)) {
						previousCargoBlueprint = cargoBlueprint;
					} else if (currentDateTime.equals(liftingDateTime)) {
						sameMonthShiftCargoBlueprints.add(cargoBlueprint);
						usedCargoBlueprints.add(cargoBlueprint);
						foundMatch = true;
						break;
					} else if (currentDateTime.isAfter(liftingDateTime)) {
						if (previousCargoBlueprint == null) {
							final YearMonth liftingYM = YearMonth.from(liftingDateTime);
							final YearMonth cargoYM = YearMonth.from(currentDateTime);
							if (liftingYM.equals(cargoYM)) {
								sameMonthShiftCargoBlueprints.add(cargoBlueprint);
							} else {
								differentMonthShiftCargoBlueprints.add(Pair.of(cargoBlueprint, liftingDateTime));
							}
							usedCargoBlueprints.add(cargoBlueprint);
							foundMatch = true;
						} else {
							final int hoursDifferencePrevious = Hours.between(previousCargoBlueprint.getWindowStart(), liftingDateTime);
							final int hoursDifferenceLater = Hours.between(liftingDateTime, currentDateTime);
							final CargoBlueprint selectedCargoBlueprint;
							if (hoursDifferencePrevious <= hoursDifferenceLater) {
								selectedCargoBlueprint = previousCargoBlueprint;
							} else {
								selectedCargoBlueprint = cargoBlueprint;
							}
							final YearMonth liftingYM = YearMonth.from(liftingDateTime);
							final YearMonth cargoYM = YearMonth.from(selectedCargoBlueprint.getWindowStart());
							if (liftingYM.equals(cargoYM)) {
								// no shifting of cargoes between months, keep entitlements the same
								sameMonthShiftCargoBlueprints.add(selectedCargoBlueprint);
							} else {
								// shifting a cargo elsewhere, have to account for changes in entitlement
								differentMonthShiftCargoBlueprints.add(Pair.of(selectedCargoBlueprint, liftingDateTime));
							}
							usedCargoBlueprints.add(selectedCargoBlueprint);
							foundMatch = true;
						}
						break;
					}
				}
				if (!foundMatch && previousCargoBlueprint != null) {
					final YearMonth liftingYM = YearMonth.from(liftingDateTime);
					final YearMonth cargoYM = YearMonth.from(previousCargoBlueprint.getWindowStart());
					if (liftingYM.equals(cargoYM)) {
						sameMonthShiftCargoBlueprints.add(previousCargoBlueprint);
					} else {
						differentMonthShiftCargoBlueprints.add(Pair.of(previousCargoBlueprint, liftingDateTime));
					}
					usedCargoBlueprints.add(previousCargoBlueprint);
				}
			}
		}

		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Set<CargoBlueprint>>>> monthMappedEntityCargoBlueprints = new HashMap<>();
		final Map<Inventory, Map<YearMonth, Set<CargoBlueprint>>> monthMappedCargoBlueprints = new HashMap<>();
		final Map<Inventory, Map<AllocationTracker, Map<YearMonth, Set<CargoBlueprint>>>> monthMappedAllocationTrackerCargoBlueprints = new HashMap<>();
		for (final Entry<Inventory, LinkedList<CargoBlueprint>> entry : phase1aCargoBluePrintsToGenerate.entrySet()) {
			final Inventory inventory = entry.getKey();
			monthMappedEntityCargoBlueprints.put(inventory, new HashMap<>());
			monthMappedCargoBlueprints.put(inventory, new HashMap<>());
			monthMappedAllocationTrackerCargoBlueprints.put(inventory, new HashMap<>());
			for (CargoBlueprint cb : entry.getValue()) {
				if (usedCargoBlueprints.contains(cb)) {
					continue;
				}
				final YearMonth cbYM = YearMonth.from(cb.getWindowStart());
				monthMappedEntityCargoBlueprints.get(inventory).computeIfAbsent(cb.getEntity(), k -> new HashMap<>()).computeIfAbsent(cbYM, k -> new HashSet<>()).add(cb);
				monthMappedCargoBlueprints.get(inventory).computeIfAbsent(cbYM, k -> new HashSet<>()).add(cb);
				monthMappedAllocationTrackerCargoBlueprints.get(inventory).computeIfAbsent(cb.getAllocationTracker(), k -> new HashMap<>()).computeIfAbsent(cbYM, k -> new HashSet<>()).add(cb);
			}
		}

		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>>> phase1aMonthlySalesContract = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<DESSalesMarket, Integer>>>> phase1aMonthlyDesSalesMarket = new HashMap<>();
		phase1aMullContainers.entrySet().forEach(mullContainerEntry -> {
			final Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> currentSalesContractMapLevel2 = new HashMap<>();
			final Map<BaseLegalEntity, Map<YearMonth, Map<DESSalesMarket, Integer>>> currentDesSalesMarketLevel2 = new HashMap<>();
			phase1aMonthlySalesContract.put(mullContainerEntry.getKey(), currentSalesContractMapLevel2);
			phase1aMonthlyDesSalesMarket.put(mullContainerEntry.getKey(), currentDesSalesMarketLevel2);
			mullContainerEntry.getValue().getMUDContainers().forEach(mudContainer -> {
				final Map<YearMonth, Map<SalesContract, Integer>> currentSalesContractMapLevel3 = new TreeMap<>();
				final Map<YearMonth, Map<DESSalesMarket, Integer>> currentDesSalesMarketMapLevel3 = new TreeMap<>();
				currentSalesContractMapLevel2.put(mudContainer.getEntity(), currentSalesContractMapLevel3);
				currentDesSalesMarketLevel2.put(mudContainer.getEntity(), currentDesSalesMarketMapLevel3);
				for (YearMonth ym = adpModel.getYearStart(); ym.isBefore(adpModel.getYearEnd()); ym = ym.plusMonths(1)) {
					final YearMonth currentYM = ym;
					final Map<SalesContract, Integer> currentSalesContractMapLevel4 = new HashMap<>();
					final Map<DESSalesMarket, Integer> currentDesSalesMarketMapLevel4 = new HashMap<>();
					currentSalesContractMapLevel3.put(currentYM, currentSalesContractMapLevel4);
					currentDesSalesMarketMapLevel3.put(ym, currentDesSalesMarketMapLevel4);
					mudContainer.getAllocationTrackers().forEach(at -> {
						if (at instanceof SalesContractTracker) {
							currentSalesContractMapLevel4.put(((SalesContractTracker) at).getSalesContract(), 0);
						} else {
							currentDesSalesMarketMapLevel4.put(((DESMarketTracker) at).getDESSalesMarket(), 0);
						}
					});
				}
			});
		});

		phase1aCargoBluePrintsToGenerate.entrySet().forEach(entry -> {
			final Inventory currentInventory = entry.getKey();
			entry.getValue().forEach(cb -> {
				final BaseLegalEntity currentEntity = cb.getEntity();
				final YearMonth currentYM = YearMonth.from(cb.getWindowStart());
				if (cb.getAllocationTracker() instanceof SalesContractTracker) {
					final SalesContract salesContract = ((SalesContractTracker) cb.getAllocationTracker()).getSalesContract();
					final Map<SalesContract, Integer> scMap = phase1aMonthlySalesContract.get(currentInventory).get(currentEntity).get(currentYM);
					final int currentCount = scMap.get(salesContract);
					scMap.put(salesContract, currentCount + 1);
				} else {
					final DESSalesMarket desMarket = ((DESMarketTracker) cb.getAllocationTracker()).getDESSalesMarket();
					final Map<DESSalesMarket, Integer> dsmMap = phase1aMonthlyDesSalesMarket.get(currentInventory).get(currentEntity).get(currentYM);
					final int currentCount = dsmMap.get(desMarket);
					dsmMap.put(desMarket, currentCount + 1);
				}
			});
		});

		for (final CargoBlueprint cargoBlueprint : usedCargoBlueprints) {
			final Inventory inventory = purchaseContractToInventory.get(cargoBlueprint.getPurchaseContract());
			final BaseLegalEntity entity = cargoBlueprint.getEntity();
			final YearMonth ym = YearMonth.from(cargoBlueprint.getWindowStart());
			final AllocationTracker alloc = cargoBlueprint.getAllocationTracker();
			if (alloc instanceof SalesContractTracker) {
				final SalesContract salesContract = ((SalesContractTracker) alloc).getSalesContract();
				final int count = phase1aMonthlySalesContract.get(inventory).get(entity).get(ym).get(salesContract);
				assert count > 0;
				phase1aMonthlySalesContract.get(inventory).get(entity).get(ym).put(salesContract, count - 1);
			} else {
				final DESSalesMarket desSalesMarket = ((DESMarketTracker) alloc).getDESSalesMarket();
				final int count = phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(ym).get(desSalesMarket);
				assert count > 0;
				phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(ym).put(desSalesMarket, count - 1);
			}
		}

		for (final Pair<CargoBlueprint, LocalDateTime> pair : differentMonthShiftCargoBlueprints) {
			final CargoBlueprint fixedCargoMatchedBlueprint = pair.getFirst();
			final LocalDateTime movedFromDate = fixedCargoMatchedBlueprint.getWindowStart();
			final LocalDateTime movedToDate = pair.getSecond();
			final Inventory inventory = purchaseContractToInventory.get(fixedCargoMatchedBlueprint.getPurchaseContract());
			final YearMonth fromYM = YearMonth.from(movedFromDate);
			final YearMonth toYM = YearMonth.from(movedToDate);
			assert !fromYM.equals(toYM);
			if (fromYM.isBefore(toYM)) {
				// is there a same entity cargoBlueprint?
				final BaseLegalEntity entity = fixedCargoMatchedBlueprint.getEntity();
				Map<YearMonth, Set<CargoBlueprint>> ymMap = monthMappedEntityCargoBlueprints.get(inventory).get(entity);
				if (ymMap != null) {
					final Set<CargoBlueprint> cbSet = ymMap.get(toYM);
					if (cbSet != null && !cbSet.isEmpty()) {
						// find cargo that has a similar lifting

						final int movedAllocatedVolume = fixedCargoMatchedBlueprint.getAllocatedVolume();
						final CargoBlueprint selectedCargoblueprint = cbSet.stream()
								.min((cb1, cb2) -> Integer.compare(Math.abs(movedAllocatedVolume - cb1.getAllocatedVolume()), Math.abs(movedAllocatedVolume - cb2.getAllocatedVolume()))).get();
						selectedCargoblueprint.setWindowStart(fixedCargoMatchedBlueprint.getWindowStart());
						monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoblueprint.getAllocationTracker()).get(toYM).remove(selectedCargoblueprint);
						monthMappedCargoBlueprints.get(inventory).get(toYM).remove(selectedCargoblueprint);
						monthMappedEntityCargoBlueprints.get(inventory).get(entity).get(toYM).remove(selectedCargoblueprint);
						monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoblueprint.getAllocationTracker()).computeIfAbsent(fromYM, k -> new HashSet<>())
								.add(selectedCargoblueprint);
						monthMappedCargoBlueprints.get(inventory).computeIfAbsent(fromYM, k -> new HashSet<>()).add(selectedCargoblueprint);
						monthMappedEntityCargoBlueprints.get(inventory).get(entity).computeIfAbsent(fromYM, k -> new HashSet<>()).add(selectedCargoblueprint);
						// We've done a one for one swap on the same entity, assume that the changes in entitlement aren't that different - hence no updating of entitlements.
						// This might be a bad assumption
						final AllocationTracker alloc = selectedCargoblueprint.getAllocationTracker();
						if (alloc instanceof SalesContractTracker) {
							final SalesContract salesContract = ((SalesContractTracker) alloc).getSalesContract();
							final int toMonthCount = phase1aMonthlySalesContract.get(inventory).get(entity).get(toYM).get(salesContract);
							final int fromMonthCount = phase1aMonthlySalesContract.get(inventory).get(entity).get(fromYM).get(salesContract);
							phase1aMonthlySalesContract.get(inventory).get(entity).get(fromYM).put(salesContract, fromMonthCount + 1);
							phase1aMonthlySalesContract.get(inventory).get(entity).get(toYM).put(salesContract, toMonthCount - 1);
						} else {
							final DESSalesMarket desSalesMarket = ((DESMarketTracker) alloc).getDESSalesMarket();
							final int toMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(toYM).get(desSalesMarket);
							final int fromMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(fromYM).get(desSalesMarket);
							phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(fromYM).put(desSalesMarket, fromMonthCount + 1);
							phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(toYM).put(desSalesMarket, toMonthCount - 1);
						}
						// Made switch so continue
						continue;
					}
				}

				final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements = phase1aMonthEndEntitlements.get(inventory);
				final Phase1AComparator violationComparator = new Phase1AComparator(fromYM, toYM, monthEndEntitlements, fullCargoLotValue);
				// If we get here then we could not find an alternative entity to switch cargoes on
				// Find entity that causes minimal violation on either side if brought forward

				// Switch cargoes on a case-by-case basis
				CargoBlueprint transferredCargoBlueprint = null;

				YearMonth releasingYearMonth = toYM;
				YearMonth acceptingYearMonth = null;
				for (YearMonth currentYm = toYM.minusMonths(1); !fromYM.isAfter(currentYm); currentYm = currentYm.minusMonths(1)) {
					final Set<CargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
					if (cbSet != null && !cbSet.isEmpty()) {
						acceptingYearMonth = currentYm;
						break;
					}
				}

				while (!fromYM.isAfter(releasingYearMonth)) {
					final YearMonth finalReleasingYm = releasingYearMonth;
					final CargoBlueprint selectedCargoBlueprint;
					if (transferredCargoBlueprint == null) {
						violationComparator.init(acceptingYearMonth, releasingYearMonth);
						selectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream().map(Entry::getValue).filter(currentYmMap -> {
							final Set<CargoBlueprint> set = currentYmMap.get(finalReleasingYm);
							return set != null && !set.isEmpty();
						}).flatMap(currentYmMap -> currentYmMap.get(finalReleasingYm).stream()).min(violationComparator).get();
					} else {
						final CargoBlueprint finalTransferredCargoBlueprint = transferredCargoBlueprint;
						violationComparator.init(acceptingYearMonth, releasingYearMonth);
						final AllocationTracker transferenceAllocationTracker = transferredCargoBlueprint.getAllocationTracker();
						Optional<CargoBlueprint> optSelectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream().filter(entry -> {
							final Set<CargoBlueprint> set = entry.getValue().get(finalReleasingYm);
							return set != null && !set.isEmpty();
						}).flatMap(entry -> {
							final Stream<CargoBlueprint> stream = entry.getValue().get(finalReleasingYm).stream();
							if (entry.getKey() == transferenceAllocationTracker) {
								return stream.filter(cb -> cb != finalTransferredCargoBlueprint);
							} else {
								return stream;
							}
						}).min(violationComparator);
						if (optSelectedCargoBlueprint.isPresent()) {
							selectedCargoBlueprint = optSelectedCargoBlueprint.get();
						} else {
							selectedCargoBlueprint = transferredCargoBlueprint;
						}
					}
					final BaseLegalEntity selectedEntity = selectedCargoBlueprint.getEntity();
					monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).get(releasingYearMonth).remove(selectedCargoBlueprint);
					monthMappedCargoBlueprints.get(inventory).get(releasingYearMonth).remove(selectedCargoBlueprint);
					monthMappedEntityCargoBlueprints.get(inventory).get(selectedEntity).get(releasingYearMonth).remove(selectedCargoBlueprint);
					monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).computeIfAbsent(acceptingYearMonth, k -> new HashSet<>())
							.add(selectedCargoBlueprint);
					monthMappedCargoBlueprints.get(inventory).computeIfAbsent(fromYM, k -> new HashSet<>()).add(selectedCargoBlueprint);
					monthMappedEntityCargoBlueprints.get(inventory).get(selectedEntity).computeIfAbsent(fromYM, k -> new HashSet<>()).add(selectedCargoBlueprint);
					// We've moved cargoes for a different entity and the current entity have to update their reference entitlements to accomodate this.
					// Using matched cargoblueprint for volume - this might be a bad assumption
					for (YearMonth ym = acceptingYearMonth; ym.isBefore(finalReleasingYm); ym = ym.plusMonths(1)) {
						final long newEntitlement = phase1aMonthEndEntitlements.get(inventory).get(selectedEntity).get(ym).longValue() - selectedCargoBlueprint.getAllocatedVolume();
						phase1aMonthEndEntitlements.get(inventory).get(selectedEntity).put(ym, newEntitlement);
						final long newEntitlementForFixedCargo = phase1aMonthEndEntitlements.get(inventory).get(entity).get(ym).longValue() + fixedCargoMatchedBlueprint.getAllocatedVolume();
						phase1aMonthEndEntitlements.get(inventory).get(entity).put(ym, newEntitlementForFixedCargo);
					}
					final AllocationTracker alloc = selectedCargoBlueprint.getAllocationTracker();
					if (alloc instanceof SalesContractTracker) {
						final SalesContract salesContract = ((SalesContractTracker) alloc).getSalesContract();
						final int toMonthCount = phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(releasingYearMonth).get(salesContract);
						final int fromMonthCount = phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(acceptingYearMonth).get(salesContract);
						phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(releasingYearMonth).put(salesContract, toMonthCount - 1);
						phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(acceptingYearMonth).put(salesContract, fromMonthCount + 1);
					} else {
						final DESSalesMarket desSalesMarket = ((DESMarketTracker) alloc).getDESSalesMarket();
						final int toMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(releasingYearMonth).get(desSalesMarket);
						final int fromMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(acceptingYearMonth).get(desSalesMarket);
						phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(releasingYearMonth).put(desSalesMarket, toMonthCount - 1);
						phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(acceptingYearMonth).put(desSalesMarket, fromMonthCount + 1);
					}

					transferredCargoBlueprint = selectedCargoBlueprint;
					releasingYearMonth = acceptingYearMonth;
					acceptingYearMonth = null;
					for (YearMonth currentYm = releasingYearMonth.minusMonths(1); !fromYM.isAfter(currentYm); currentYm = currentYm.minusMonths(1)) {
						final Set<CargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
						if (cbSet != null && !cbSet.isEmpty()) {
							acceptingYearMonth = currentYm;
							break;
						}
					}
					if (acceptingYearMonth == null) {
						break;
					}
				}

			} else {
				final BaseLegalEntity entity = fixedCargoMatchedBlueprint.getEntity();
				Map<YearMonth, Set<CargoBlueprint>> ymMap = monthMappedEntityCargoBlueprints.get(inventory).get(entity);
				if (ymMap != null) {
					final Set<CargoBlueprint> cbSet = ymMap.get(toYM);
					if (cbSet != null && !cbSet.isEmpty()) {
						final int movedAllocatedVolume = fixedCargoMatchedBlueprint.getAllocatedVolume();
						final CargoBlueprint selectedCargoBlueprint = cbSet.stream()
								.min((cb1, cb2) -> Integer.compare(Math.abs(movedAllocatedVolume - cb1.getAllocatedVolume()), Math.abs(movedAllocatedVolume - cb2.getAllocatedVolume()))).get();
						selectedCargoBlueprint.setWindowStart(fixedCargoMatchedBlueprint.getWindowStart());
						monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).get(toYM).remove(selectedCargoBlueprint);
						monthMappedCargoBlueprints.get(inventory).get(toYM).remove(selectedCargoBlueprint);
						monthMappedEntityCargoBlueprints.get(inventory).get(entity).get(toYM).remove(selectedCargoBlueprint);
						monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).computeIfAbsent(fromYM, k -> new HashSet<>())
								.add(selectedCargoBlueprint);
						monthMappedCargoBlueprints.get(inventory).computeIfAbsent(fromYM, k -> new HashSet<>()).add(selectedCargoBlueprint);
						monthMappedEntityCargoBlueprints.get(inventory).get(entity).computeIfAbsent(fromYM, k -> new HashSet<>()).add(selectedCargoBlueprint);
						final AllocationTracker alloc = selectedCargoBlueprint.getAllocationTracker();
						if (alloc instanceof SalesContractTracker) {
							final SalesContract salesContract = ((SalesContractTracker) alloc).getSalesContract();
							final int toMonthCount = phase1aMonthlySalesContract.get(inventory).get(entity).get(toYM).get(salesContract);
							final int fromMonthCount = phase1aMonthlySalesContract.get(inventory).get(entity).get(fromYM).get(salesContract);
							phase1aMonthlySalesContract.get(inventory).get(entity).get(fromYM).put(salesContract, fromMonthCount + 1);
							phase1aMonthlySalesContract.get(inventory).get(entity).get(toYM).put(salesContract, toMonthCount - 1);
						} else {
							final DESSalesMarket desSalesMarket = ((DESMarketTracker) alloc).getDESSalesMarket();
							final int toMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(toYM).get(desSalesMarket);
							final int fromMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(fromYM).get(desSalesMarket);
							phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(fromYM).put(desSalesMarket, fromMonthCount + 1);
							phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(toYM).put(desSalesMarket, toMonthCount - 1);
						}
						// Made switch so continue
						continue;
					}
				}
				final Map<BaseLegalEntity, Map<YearMonth, Long>> monthEndEntitlements = phase1aMonthEndEntitlements.get(inventory);
				final Phase1AComparator2 violationComparator = new Phase1AComparator2(fromYM, toYM, monthEndEntitlements, fullCargoLotValue);
				// If we get here then we could not find an alternative entity to switch cargoes on
				// Find entity that causes minimal violation on either side if brought forward

				// Switch cargoes on a case-by-case basis
				CargoBlueprint transferredCargoBlueprint = null;

				YearMonth releasingYearMonth = toYM;
				YearMonth acceptingYearMonth = null;
				for (YearMonth currentYm = toYM.plusMonths(1); !fromYM.isBefore(currentYm); currentYm = currentYm.plusMonths(1)) {
					final Set<CargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
					if (cbSet != null && !cbSet.isEmpty()) {
						acceptingYearMonth = currentYm;
						break;
					}
				}

				while (!fromYM.isBefore(releasingYearMonth)) {
					final YearMonth finalReleasingYm = releasingYearMonth;
					final CargoBlueprint selectedCargoBlueprint;
					if (transferredCargoBlueprint == null) {
						violationComparator.init(acceptingYearMonth, releasingYearMonth);
						selectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream().map(Entry::getValue).filter(map -> {
							final Set<CargoBlueprint> cbSet = map.get(finalReleasingYm);
							return cbSet != null && !cbSet.isEmpty();
						}).flatMap(map -> map.get(finalReleasingYm).stream()).min(violationComparator).get();
					} else {
						final CargoBlueprint finalTransferredCargoBlueprint = transferredCargoBlueprint;
						violationComparator.init(acceptingYearMonth, releasingYearMonth);
						final AllocationTracker transferenceAllocationTracker = transferredCargoBlueprint.getAllocationTracker();
						Optional<CargoBlueprint> optSelectedCargoBlueprint = monthMappedAllocationTrackerCargoBlueprints.get(inventory).entrySet().stream().filter(entry -> {
							final Set<CargoBlueprint> set = entry.getValue().get(finalReleasingYm);
							return set != null && !set.isEmpty();
						}).flatMap(entry -> {
							final Stream<CargoBlueprint> stream = entry.getValue().get(finalReleasingYm).stream();
							if (entry.getKey() == transferenceAllocationTracker) {
								return stream.filter(cb -> cb != finalTransferredCargoBlueprint);
							} else {
								return stream;
							}
						}).min(violationComparator);
						selectedCargoBlueprint = optSelectedCargoBlueprint.isPresent() ? optSelectedCargoBlueprint.get() : transferredCargoBlueprint;
						// We've moved cargoes for a different entity and the current entity have to update their reference entitlements to accomodate this.
						// Using matched cargoblueprint for volume - this might be a bad assumption
					}
					final BaseLegalEntity selectedEntity = selectedCargoBlueprint.getEntity();
					monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).get(releasingYearMonth).remove(selectedCargoBlueprint);
					monthMappedCargoBlueprints.get(inventory).get(releasingYearMonth).remove(selectedCargoBlueprint);
					monthMappedEntityCargoBlueprints.get(inventory).get(selectedEntity).get(releasingYearMonth).remove(selectedCargoBlueprint);
					monthMappedAllocationTrackerCargoBlueprints.get(inventory).get(selectedCargoBlueprint.getAllocationTracker()).computeIfAbsent(acceptingYearMonth, k -> new HashSet<>())
							.add(selectedCargoBlueprint);
					monthMappedCargoBlueprints.get(inventory).computeIfAbsent(acceptingYearMonth, k -> new HashSet<>()).add(selectedCargoBlueprint);
					monthMappedEntityCargoBlueprints.get(inventory).get(selectedEntity).computeIfAbsent(acceptingYearMonth, k -> new HashSet<>()).add(selectedCargoBlueprint);

					for (YearMonth ym = acceptingYearMonth; ym.isAfter(finalReleasingYm); ym = ym.minusMonths(1)) {
						final long newEntitlement = phase1aMonthEndEntitlements.get(inventory).get(selectedEntity).get(ym).longValue() + selectedCargoBlueprint.getAllocatedVolume();
						phase1aMonthEndEntitlements.get(inventory).get(selectedEntity).put(ym, newEntitlement);
						final long newEntitlementForFixedCargo = phase1aMonthEndEntitlements.get(inventory).get(entity).get(ym).longValue() - fixedCargoMatchedBlueprint.getAllocatedVolume();
						phase1aMonthEndEntitlements.get(inventory).get(entity).put(ym, newEntitlementForFixedCargo);
					}
					final AllocationTracker alloc = selectedCargoBlueprint.getAllocationTracker();
					if (alloc instanceof SalesContractTracker) {
						final SalesContract salesContract = ((SalesContractTracker) alloc).getSalesContract();
						final int toMonthCount = phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(releasingYearMonth).get(salesContract);
						final int fromMonthCount = phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(acceptingYearMonth).get(salesContract);
						phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(releasingYearMonth).put(salesContract, toMonthCount - 1);
						phase1aMonthlySalesContract.get(inventory).get(selectedEntity).get(acceptingYearMonth).put(salesContract, fromMonthCount + 1);
					} else {
						final DESSalesMarket desSalesMarket = ((DESMarketTracker) alloc).getDESSalesMarket();
						final int toMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(releasingYearMonth).get(desSalesMarket);
						final int fromMonthCount = phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(acceptingYearMonth).get(desSalesMarket);
						phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(releasingYearMonth).put(desSalesMarket, toMonthCount - 1);
						phase1aMonthlyDesSalesMarket.get(inventory).get(selectedEntity).get(acceptingYearMonth).put(desSalesMarket, fromMonthCount + 1);
					}
					transferredCargoBlueprint = selectedCargoBlueprint;
					releasingYearMonth = acceptingYearMonth;
					acceptingYearMonth = null;
					for (YearMonth currentYm = releasingYearMonth.plusMonths(1); !fromYM.isBefore(currentYm); currentYm = currentYm.plusMonths(1)) {
						final Set<CargoBlueprint> cbSet = monthMappedCargoBlueprints.get(inventory).get(currentYm);
						if (cbSet != null && !cbSet.isEmpty()) {
							acceptingYearMonth = currentYm;
							break;
						}
					}
					if (acceptingYearMonth == null) {
						break;
					}
				}

			}
		}

		// for (final Entry<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>>> entry : phase1aMonthlySalesContract.entrySet()) {
		// System.out.println(String.format("Inventory: %s", entry.getKey().getName()));
		// final Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> mapScA = entry.getValue();
		// final Map<BaseLegalEntity, Map<YearMonth, Map<DESSalesMarket, Integer>>> mapDsmA = phase1aMonthlyDesSalesMarket.get(entry.getKey());
		// for (Entry<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> entryB : mapScA.entrySet()) {
		// final Map<YearMonth, Map<SalesContract, Integer>> mapScB = entryB.getValue();
		// final Map<YearMonth, Map<DESSalesMarket, Integer>> mapDsmB = mapDsmA.get(entryB.getKey());
		// System.out.println(String.format("\tEntity: %s", entryB.getKey().getName()));
		// for (YearMonth ym = adpModel.getYearStart(); ym.isBefore(adpModel.getYearEnd()); ym = ym.plusMonths(1)) {
		// System.out.println(String.format("\t\t%s:", ym.toString()));
		// for (Entry<SalesContract, Integer> entryC : mapScB.get(ym).entrySet()) {
		// System.out.println(String.format("\t\t\t%s: %d", entryC.getKey().getName(), entryC.getValue().intValue()));
		// }
		// for (Entry<DESSalesMarket, Integer> entryC : mapDsmB.get(ym).entrySet()) {
		// System.out.println(String.format("\t\t\t%s: %d", entryC.getKey().getName(), entryC.getValue().intValue()));
		// }
		// }
		// }
		// }
		// for (final Entry<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>>> entry : phase1aMonthlySalesContract.entrySet()) {
		// System.out.println(String.format("Inventory: %s", entry.getKey().getName()));
		// final Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> mapScA = entry.getValue();
		// final Map<BaseLegalEntity, Map<YearMonth, Map<DESSalesMarket, Integer>>> mapDsmA = phase1aMonthlyDesSalesMarket.get(entry.getKey());
		// for (Entry<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> entryB : mapScA.entrySet()) {
		// final List<SalesContract> salesContracts = entryB.getValue().entrySet().stream().findAny().get().getValue().keySet().stream()
		// .sorted((sc1, sc2) -> sc1.getName().compareTo(sc2.getName())).collect(Collectors.toList());
		// final Map<YearMonth, Map<DESSalesMarket, Integer>> mapDsmB = mapDsmA.get(entryB.getKey());
		// final List<DESSalesMarket> desSalesMarkets = mapDsmB.entrySet().stream().findAny().get().getValue().keySet().stream().sorted((dsm1, dsm2) -> dsm1.getName().compareTo(dsm2.getName()))
		// .collect(Collectors.toList());
		// final int totalCount = entryB.getValue().entrySet().stream().map(Entry::getValue).flatMap(m -> m.entrySet().stream()).mapToInt(Entry::getValue).sum()
		// + mapDsmB.entrySet().stream().map(Entry::getValue).flatMap(m -> m.entrySet().stream()).mapToInt(Entry::getValue).sum();
		// System.out.println(String.format("\t%s: %d", entryB.getKey().getName(), totalCount));
		// for (final SalesContract sc : salesContracts) {
		// final int count = entryB.getValue().entrySet().stream().map(Entry::getValue).mapToInt(m -> m.get(sc).intValue()).sum();
		// System.out.println(String.format("\t\tSC:%s. Got: %d", sc.getName(), count));
		// }
		// for (final DESSalesMarket dsm : desSalesMarkets) {
		// final int count = mapDsmB.entrySet().stream().map(Entry::getValue).mapToInt(m -> m.get(dsm).intValue()).sum();
		// System.out.println(String.format("\t\tDSM:%s. Got: %d", dsm.getName(), count));
		// }
		// }
		// }
		// for (final Entry<Inventory, Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>>> entry : phase1aMonthlySalesContract.entrySet()) {
		// System.out.println(String.format("Inventory: %s", entry.getKey().getName()));
		// final Map<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> mapScA = entry.getValue();
		// final Map<BaseLegalEntity, Map<YearMonth, Map<DESSalesMarket, Integer>>> mapDsmA = phase1aMonthlyDesSalesMarket.get(entry.getKey());
		// for (YearMonth ym = adpModel.getYearStart(); ym.isBefore(adpModel.getYearEnd()); ym = ym.plusMonths(1)) {
		// System.out.println(String.format("\t%s:", ym.toString()));
		// for (Entry<BaseLegalEntity, Map<YearMonth, Map<SalesContract, Integer>>> entryB : mapScA.entrySet()) {
		// final Map<YearMonth, Map<SalesContract, Integer>> mapScB = entryB.getValue();
		// final Map<YearMonth, Map<DESSalesMarket, Integer>> mapDsmB = mapDsmA.get(entryB.getKey());
		// final int totalCount = mapScB.get(ym).entrySet().stream().mapToInt(Entry::getValue).sum() + mapDsmB.get(ym).entrySet().stream().mapToInt(Entry::getValue).sum();
		// for (Entry<SalesContract, Integer> entryC : mapScB.get(ym).entrySet()) {
		// System.out.println(String.format("\t\t%s (%d) -- %s: %d", entryB.getKey().getName(), totalCount, entryC.getKey().getName(), entryC.getValue().intValue()));
		// }
		// for (Entry<DESSalesMarket, Integer> entryC : mapDsmB.get(ym).entrySet()) {
		// System.out.println(String.format("\t\t%s (%d) -- %s: %d", entryB.getKey().getName(), totalCount, entryC.getKey().getName(), entryC.getValue().intValue()));
		// }
		// }
		// }
		// }
		// int j = 0;
		// End of phase 1 to phase 2 transition

		// Phase 2 initialisation
		final Map<Vessel, LocalDateTime> phase2VesselToMostRecentUseDateTime = new HashMap<>();
		final Map<Vessel, LocalDateTime> finalPhaseVesselToMostRecentUseDateTime = new HashMap<>();

		final Map<Inventory, MULLContainer> finalPhaseMullContainers = new HashMap<>();
		for (final MullSubprofile phase1RevisedSubprofile : phase1RevisedProfile.getInventories()) {
			final Inventory inventory = phase1RevisedSubprofile.getInventory();
			finalPhaseMullContainers.put(inventory, new MULLContainer(phase1RevisedSubprofile, fullCargoLotValue));
			for (final MullEntityRow mullEntityRow : phase1RevisedSubprofile.getEntityTable()) {
				Stream.concat(mullEntityRow.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						mullEntityRow.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream))
						.forEach(vessel -> finalPhaseVesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}
			yearlyProductions.put(inventory, getYearlyProduction(inventory, sm, startDateTime, endDateTimeExclusive));
		}

		final Set<Vessel> firstPartyVessels = finalPhaseMullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.filter(mudContainer -> firstPartyEntities.contains(mudContainer.getEntity())).flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.flatMap(allocationTracker -> allocationTracker.getVessels().stream()) //
				.collect(Collectors.toSet());

		finalPhaseMullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(firstPartyVessels));

		final MullProfile harmonisationMullProfile = ADPFactory.eINSTANCE.createMullProfile();
		harmonisationMullProfile.setFullCargoLotValue(phase1RevisedProfile.getFullCargoLotValue());
		harmonisationMullProfile.setVolumeFlex(phase1RevisedProfile.getVolumeFlex());
		harmonisationMullProfile.setWindowSize(phase1RevisedProfile.getWindowSize());

		final Map<Inventory, Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>>> rearrangedProfiles = new HashMap<>();
		for (final MULLContainer finalPhaseMullContainer : finalPhaseMullContainers.values()) {
			// Expecting one first party MUD container - unknown behaviour for more than one
			// or none first party
			final Optional<MUDContainer> optMudContainer = finalPhaseMullContainer.getMUDContainers().stream().filter(m -> firstPartyEntities.contains(m.getEntity())).findAny();
			if (optMudContainer.isEmpty()) {
				throw new IllegalStateException("MULL profile should contain a first party entry.");
			}
			final MUDContainer firstPartyMudContainer = optMudContainer.get();

			// Map of <entity, allocation tracker pair> to List of <entity, allocation
			// tracker> that it combines with (using first party as sink)
			final Map<Pair<MUDContainer, AllocationTracker>, List<Pair<MUDContainer, AllocationTracker>>> newCombinations = new HashMap<>();
			firstPartyMudContainer.getAllocationTrackers().stream().map(a -> Pair.of(firstPartyMudContainer, a)).forEach(p -> newCombinations.put(p, new LinkedList<>(Collections.singleton(p))));
			for (final MUDContainer thirdPartyMudContainer : finalPhaseMullContainer.getMUDContainers()) {
				if (thirdPartyMudContainer.getEntity().isThirdParty()) {
					for (final AllocationTracker thirdPartyAllocationTracker : thirdPartyMudContainer.getAllocationTrackers()) {
						if (thirdPartyAllocationTracker.isSharingVessels()) {
							final Optional<AllocationTracker> optAllocationTracker = firstPartyMudContainer.getAllocationTrackers().stream() //
									.filter(a -> {
										final HashSet<Vessel> firstVessSet = new HashSet<>(a.getVessels());
										final List<Vessel> thirdPartyVessels = thirdPartyAllocationTracker.getVessels();
										return firstVessSet.size() == thirdPartyVessels.size() && thirdPartyVessels.stream().allMatch(firstVessSet::contains);
									}).findAny();
							if (optAllocationTracker.isEmpty()) {
								// Shares vessels but first party does not have a tracker with the same vessel
								// set (not all vessels are shared)
								final Pair<MUDContainer, AllocationTracker> p = Pair.of(thirdPartyMudContainer, thirdPartyAllocationTracker);
								newCombinations.put(p, new LinkedList<>(Collections.singleton(p)));
							} else {
								newCombinations.get(Pair.of(firstPartyMudContainer, optAllocationTracker.get())).add(Pair.of(thirdPartyMudContainer, thirdPartyAllocationTracker));
							}
						} else {
							final Pair<MUDContainer, AllocationTracker> p = Pair.of(thirdPartyMudContainer, thirdPartyAllocationTracker);
							newCombinations.put(p, new LinkedList<>(Collections.singleton(p)));
						}
					}
				}
			}

			// Maps to tie allocation tracker and entitlements to entities
			final Map<MUDContainer, Set<AllocationTracker>> rearrangedContainment = new HashMap<>();
			final Map<MUDContainer, Double> runningRelativeEntitlement = new HashMap<>();
			final Map<MUDContainer, Long> runningInitialAllocation = new HashMap<>();
			for (final MUDContainer currentMUDContainer : finalPhaseMullContainer.getMUDContainers()) {
				rearrangedContainment.put(currentMUDContainer, new HashSet<>(currentMUDContainer.getAllocationTrackers()));
				runningRelativeEntitlement.put(currentMUDContainer, currentMUDContainer.getRelativeEntitlement());
				runningInitialAllocation.put(currentMUDContainer, currentMUDContainer.getInitialAllocation());
			}

			// <MUD, allocation tracker> pairs that do not move
			final List<Pair<MUDContainer, AllocationTracker>> nonMovingElements = newCombinations.entrySet().stream().filter(entry -> entry.getValue().size() == 1).map(Entry::getKey)
					.collect(Collectors.toList());
			// <MUD, allocation tracker> pairs that combine with another
			final Map<Pair<MUDContainer, AllocationTracker>, List<Pair<MUDContainer, AllocationTracker>>> movingElements = new HashMap<>();
			for (final Entry<Pair<MUDContainer, AllocationTracker>, List<Pair<MUDContainer, AllocationTracker>>> entry : newCombinations.entrySet()) {
				if (entry.getValue().size() > 1) {
					movingElements.put(entry.getKey(), entry.getValue());
				}
			}

			// Map of MUD to pair of <allocation trackers that are from the original setup
			// and map from an allocation tracker to a list of <MUD, allocation tracker>
			// that the current allocation tracker absorbs
			final Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>> rearrangedProfile = new HashMap<>();
			// Populate with original MUD containers
			for (final MUDContainer currentMUDContainer : finalPhaseMullContainer.getMUDContainers()) {
				rearrangedProfile.put(currentMUDContainer, Pair.of(new LinkedList<>(), new HashMap<>()));
			}

			// Populate the rearranged profile
			nonMovingElements.forEach(p -> rearrangedProfile.get(p.getFirst()).getFirst().add(p.getSecond()));
			for (final Entry<Pair<MUDContainer, AllocationTracker>, List<Pair<MUDContainer, AllocationTracker>>> entry : movingElements.entrySet()) {
				final MUDContainer minorMud = entry.getValue().stream().map(Pair::getFirst).min((mud1, mud2) -> Double.compare(mud1.getRelativeEntitlement(), mud2.getRelativeEntitlement())).get();

				// If we have the option of using DESMarketTracker as sink, use it (otherwise
				// throws an IllegalStateException later on)
				final List<Pair<MUDContainer, AllocationTracker>> minorLifterPairCandidates = entry.getValue().stream().filter(p -> p.getFirst() == minorMud).collect(Collectors.toList());
				final Optional<Pair<MUDContainer, AllocationTracker>> optDesMinorLifterPair = minorLifterPairCandidates.stream().filter(p -> p.getSecond() instanceof DESMarketTracker).findAny();
				final Pair<MUDContainer, AllocationTracker> minorLifter = optDesMinorLifterPair.isPresent() ? optDesMinorLifterPair.get() : minorLifterPairCandidates.get(0);

				final List<Pair<MUDContainer, AllocationTracker>> sharedCombinations = entry.getValue().stream().filter(p -> p != minorLifter).collect(Collectors.toList());
				for (final Pair<MUDContainer, AllocationTracker> lifterToAbsorb : sharedCombinations) {
					if (lifterToAbsorb.getFirst() != minorLifter.getFirst()) {
						final MUDContainer mudToAbsorb = lifterToAbsorb.getFirst();
						final AllocationTracker allocToAbsorb = lifterToAbsorb.getSecond();
						rearrangedContainment.get(mudToAbsorb).remove(allocToAbsorb);
						rearrangedContainment.get(minorLifter.getFirst()).add(allocToAbsorb);
						if (rearrangedContainment.get(mudToAbsorb).isEmpty()) {
							runningRelativeEntitlement.put(minorLifter.getFirst(), runningRelativeEntitlement.get(minorLifter.getFirst()) + runningRelativeEntitlement.get(mudToAbsorb));
							runningRelativeEntitlement.put(mudToAbsorb, 0.0);
							runningInitialAllocation.put(minorLifter.getFirst(), runningInitialAllocation.get(minorLifter.getFirst()) + runningInitialAllocation.get(mudToAbsorb));
							runningInitialAllocation.put(mudToAbsorb, 0L);
						} else {
							final int averageVolumeLifted = allocToAbsorb.getVessels().stream() //
									.mapToInt(v -> allocToAbsorb.calculateExpectedAllocationDrop(v, finalPhaseMullContainer.getInventory().getPort().getLoadDuration(), true)) //
									.sum() / allocToAbsorb.getVessels().size();
							final int totalProd = yearlyProductions.get(finalPhaseMullContainer.getInventory());
							final double allocatedProduction = totalProd * lifterToAbsorb.getFirst().getRelativeEntitlement();
							final int aacqLiftedVolume = allocToAbsorb.getAACQ() * averageVolumeLifted;
							final double localPercentage = aacqLiftedVolume / allocatedProduction;
							final double reShift = mudToAbsorb.getRelativeEntitlement() * localPercentage;
							final long initialAllocationShift = (long) (mudToAbsorb.getInitialAllocation() * localPercentage);
							runningRelativeEntitlement.put(minorLifter.getFirst(), runningRelativeEntitlement.get(minorLifter.getFirst()) + reShift);
							runningRelativeEntitlement.put(mudToAbsorb, runningRelativeEntitlement.get(mudToAbsorb) - reShift);
							runningInitialAllocation.put(minorLifter.getFirst(), runningInitialAllocation.get(minorLifter.getFirst()) + initialAllocationShift);
							runningInitialAllocation.put(mudToAbsorb, runningInitialAllocation.get(mudToAbsorb) - initialAllocationShift);
						}
					}
					rearrangedProfile.get(minorLifter.getFirst()).getSecond().put(minorLifter.getSecond(), sharedCombinations);
				}
			}

			rearrangedProfiles.put(finalPhaseMullContainer.getInventory(), rearrangedProfile);

			final double sumNewRelativeEntitlements = runningRelativeEntitlement.values().stream().mapToDouble(v -> v).sum();
			// assert sumNewRelativeEntitlements == 1.0;

			final long sumInitialAllocationsBefore = finalPhaseMullContainer.getMUDContainers().stream().mapToLong(MUDContainer::getInitialAllocation).sum();
			final long sumInitialAllocationsAfter = runningInitialAllocation.values().stream().mapToLong(v -> v).sum();
			// assert sumInitialAllocationsBefore == sumInitialAllocationsAfter;

			// Convert rearranged profile to EMF MULL Subprofile (to use existing MULL
			// objects)
			final MullSubprofile harmonisationMullSubprofile = ADPFactory.eINSTANCE.createMullSubprofile();
			harmonisationMullSubprofile.setInventory(finalPhaseMullContainer.getInventory());
			rearrangedProfile.entrySet().forEach(entry -> {
				final MUDContainer currentMudContainer = entry.getKey();
				final Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>> pair = entry.getValue();
				if (rearrangedContainment.get(currentMudContainer).isEmpty()) {
					assert runningRelativeEntitlement.get(currentMudContainer) == 0.0;
					assert runningInitialAllocation.get(currentMudContainer) == 0L;
					assert pair.getFirst().isEmpty() && pair.getSecond().isEmpty();
				} else {
					assert !pair.getFirst().isEmpty() || !pair.getSecond().isEmpty();
					final MullEntityRow tempRow = ADPFactory.eINSTANCE.createMullEntityRow();
					tempRow.setEntity(currentMudContainer.getEntity());
					tempRow.setRelativeEntitlement(runningRelativeEntitlement.get(currentMudContainer));
					tempRow.setInitialAllocation(Long.toString(runningInitialAllocation.get(currentMudContainer)));
					harmonisationMullSubprofile.getEntityTable().add(tempRow);
					for (final AllocationTracker allocationTracker : pair.getFirst()) {
						if (allocationTracker instanceof SalesContractTracker) {
							final SalesContractAllocationRow tempAllocRow = ADPFactory.eINSTANCE.createSalesContractAllocationRow();
							tempAllocRow.setContract(((SalesContractTracker) allocationTracker).getSalesContract());
							tempAllocRow.setWeight(allocationTracker.getAACQ());
							tempAllocRow.getVessels().addAll(allocationTracker.getVessels());
							tempRow.getSalesContractAllocationRows().add(tempAllocRow);
						} else if (allocationTracker instanceof DESMarketTracker) {
							final DESSalesMarketAllocationRow tempAllocRow = ADPFactory.eINSTANCE.createDESSalesMarketAllocationRow();
							tempAllocRow.setDesSalesMarket(((DESMarketTracker) allocationTracker).getDESSalesMarket());
							tempAllocRow.setWeight(allocationTracker.getAACQ());
							tempAllocRow.getVessels().addAll(allocationTracker.getVessels());
							tempRow.getDesSalesMarketAllocationRows().add(tempAllocRow);
						}
					}
					for (Entry<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> entry2 : pair.getSecond().entrySet()) {
						final AllocationTracker absorbingAllocationTracker = entry2.getKey();
						final List<Pair<MUDContainer, AllocationTracker>> allocationTrackersToAbsorb = entry2.getValue();
						if (absorbingAllocationTracker instanceof SalesContractTracker) {
							final SalesContractAllocationRow tempAllocRow = ADPFactory.eINSTANCE.createSalesContractAllocationRow();
							tempAllocRow.setContract(((SalesContractTracker) absorbingAllocationTracker).getSalesContract());
							final int newAACQ = absorbingAllocationTracker.getAACQ() + allocationTrackersToAbsorb.stream().map(Pair::getSecond).mapToInt(AllocationTracker::getAACQ).sum();
							tempAllocRow.setWeight(newAACQ);
							tempAllocRow.getVessels().addAll(absorbingAllocationTracker.getVessels());
							tempRow.getSalesContractAllocationRows().add(tempAllocRow);
						} else if (absorbingAllocationTracker instanceof DESMarketTracker) {
							final DESSalesMarketAllocationRow tempAllocRow = ADPFactory.eINSTANCE.createDESSalesMarketAllocationRow();
							final int newAACQ = absorbingAllocationTracker.getAACQ() + allocationTrackersToAbsorb.stream().map(Pair::getSecond).mapToInt(AllocationTracker::getAACQ).sum();
							tempAllocRow.setDesSalesMarket(((DESMarketTracker) absorbingAllocationTracker).getDESSalesMarket());
							tempAllocRow.setWeight(newAACQ);
							tempAllocRow.getVessels().addAll(absorbingAllocationTracker.getVessels());
							tempRow.getDesSalesMarketAllocationRows().add(tempAllocRow);
						}
					}
				}
			});

			final int aacqSumBefore = finalPhaseMullContainer.getMUDContainers().stream() //
					.mapToInt(mc -> mc.getAllocationTrackers().stream().mapToInt(AllocationTracker::getAACQ).sum()) //
					.sum();
			final int aacqSumAfter = harmonisationMullSubprofile.getEntityTable().stream() //
					.mapToInt(row -> row.getSalesContractAllocationRows().stream().mapToInt(MullAllocationRow::getWeight).sum()
							+ row.getDesSalesMarketAllocationRows().stream().mapToInt(MullAllocationRow::getWeight).sum()) //
					.sum();
			assert aacqSumBefore == aacqSumAfter;
			harmonisationMullProfile.getInventories().add(harmonisationMullSubprofile);
		}

		final Map<Inventory, Map<Pair<BaseLegalEntity, SalesContract>, Integer>> salesContractAacqSatisfiedByFixedCargoes = new HashMap<>();
		final Map<Inventory, Map<Pair<BaseLegalEntity, DESSalesMarket>, Integer>> desSalesMarketAacqSatisfiedByFixedCargoes = new HashMap<>();
		for (final MullSubprofile phase1RevisedSubprofile : phase1RevisedProfile.getInventories()) {
			final Map<Pair<BaseLegalEntity, SalesContract>, Integer> salesContractAacqMap = new HashMap<>();
			final Map<Pair<BaseLegalEntity, DESSalesMarket>, Integer> desMarketAacqMap = new HashMap<>();
			salesContractAacqSatisfiedByFixedCargoes.put(phase1RevisedSubprofile.getInventory(), salesContractAacqMap);
			desSalesMarketAacqSatisfiedByFixedCargoes.put(phase1RevisedSubprofile.getInventory(), desMarketAacqMap);
			for (final MullEntityRow entityRow : phase1RevisedSubprofile.getEntityTable()) {
				for (final SalesContractAllocationRow salesContractAllocationRow : entityRow.getSalesContractAllocationRows()) {
					salesContractAacqMap.put(Pair.of(entityRow.getEntity(), salesContractAllocationRow.getContract()), 0);
				}
				for (final DESSalesMarketAllocationRow desMarketAllocationRow : entityRow.getDesSalesMarketAllocationRows()) {
					desMarketAacqMap.put(Pair.of(entityRow.getEntity(), desMarketAllocationRow.getDesSalesMarket()), 0);
				}
			}
		}

		final Map<Port, Inventory> portToInventoryMap = phase1RevisedProfile.getInventories().stream().map(MullSubprofile::getInventory)
				.collect(Collectors.toMap(Inventory::getPort, Function.identity()));
		cargoesToKeep.stream().forEach(cargoWrapper -> {
			final LoadSlot loadSlot = cargoWrapper.getLoadSlot();
			final DischargeSlot dischargeSlot = cargoWrapper.getDischargeSlot();
			final BaseLegalEntity entity = loadSlot.getEntity();
			if (entity == null) {
				return;
			}
			final Inventory inventory = portToInventoryMap.get(loadSlot.getPort());
			if (inventory == null) {
				return;
			}
			if (dischargeSlot instanceof SpotDischargeSlot) {
				final SpotDischargeSlot spotDischargeSlot = (SpotDischargeSlot) dischargeSlot;
				final Pair<BaseLegalEntity, DESSalesMarket> pair = Pair.of(entity, (DESSalesMarket) spotDischargeSlot.getMarket());
				final Map<Pair<BaseLegalEntity, DESSalesMarket>, Integer> map = desSalesMarketAacqSatisfiedByFixedCargoes.get(inventory);
				final Integer currentCount = map.get(pair);
				// The DES market is not part of the MULL data - ignore
				if (currentCount != null) {
					map.put(pair, currentCount + 1);
				}
			} else {
				final Pair<BaseLegalEntity, SalesContract> pair = Pair.of(entity, dischargeSlot.getContract());
				final Map<Pair<BaseLegalEntity, SalesContract>, Integer> map = salesContractAacqSatisfiedByFixedCargoes.get(inventory);
				final Integer currentCount = map.get(pair);
				// The sales contract is not part of the MULL data - ignore
				if (currentCount != null) {
					map.put(pair, currentCount + 1);
				}
			}
		});

		for (final Entry<Inventory, MULLContainer> entry : finalPhaseMullContainers.entrySet()) {
			final Inventory inventory = entry.getKey();
			final MULLContainer mullContainer = entry.getValue();
			for (MUDContainer mudContainer : mullContainer.getMUDContainers()) {
				final BaseLegalEntity entity = mudContainer.getEntity();
				for (final AllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					if (allocationTracker instanceof SalesContractTracker) {
						final SalesContractTracker salesContractTracker = (SalesContractTracker) allocationTracker;
						salesContractTracker.setAllocatedAacq(salesContractAacqSatisfiedByFixedCargoes.get(inventory).get(Pair.of(entity, salesContractTracker.getSalesContract())));
					} else if (allocationTracker instanceof DESMarketTracker) {
						final DESMarketTracker desMarketTracker = (DESMarketTracker) allocationTracker;
						desMarketTracker.setAllocatedAacq(desSalesMarketAacqSatisfiedByFixedCargoes.get(inventory).get(Pair.of(entity, desMarketTracker.getDESSalesMarket())));
					}
				}
			}
		}

		final Map<Inventory, MULLContainer> harmonisationMullContainers = new HashMap<>();
		for (final MullSubprofile harmonisationSubprofile : harmonisationMullProfile.getInventories()) {
			final Inventory currentInventory = harmonisationSubprofile.getInventory();
			harmonisationMullContainers.put(currentInventory, new MULLContainer(harmonisationSubprofile, fullCargoLotValue));

			for (MullEntityRow row : harmonisationSubprofile.getEntityTable()) {
				Stream.concat(row.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						row.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream))
						.forEach(vessel -> phase2VesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}
		}

		harmonisationMullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(firstPartyVessels));

		// Map <entity, contract/market> to rearranged profile allocation trackers
		// Contains harmonisation profile sales contracts mapped to their harmonisation allocation tracker
		final Map<Inventory, Map<Pair<BaseLegalEntity, SalesContract>, AllocationTracker>> salesContractMap = new HashMap<>();
		// Contains harmonisation profile des sales markets mapped to harmonisation their allocation tracker
		final Map<Inventory, Map<Pair<BaseLegalEntity, DESSalesMarket>, AllocationTracker>> desMarketMap = new HashMap<>();
		for (final MULLContainer harmonisationMullContainer : harmonisationMullContainers.values()) {
			final Map<Pair<BaseLegalEntity, SalesContract>, AllocationTracker> currentSalesContractMap = new HashMap<>();
			final Map<Pair<BaseLegalEntity, DESSalesMarket>, AllocationTracker> currentDESMarketMap = new HashMap<>();
			salesContractMap.put(harmonisationMullContainer.getInventory(), currentSalesContractMap);
			desMarketMap.put(harmonisationMullContainer.getInventory(), currentDESMarketMap);
			for (final MUDContainer harmonisationMudContainer : harmonisationMullContainer.getMUDContainers()) {
				for (final AllocationTracker harmonisationAllocationTracker : harmonisationMudContainer.getAllocationTrackers()) {
					if (harmonisationAllocationTracker instanceof SalesContractTracker) {
						currentSalesContractMap.put(Pair.of(harmonisationMudContainer.getEntity(), ((SalesContractTracker) harmonisationAllocationTracker).getSalesContract()),
								harmonisationAllocationTracker);
					} else if (harmonisationAllocationTracker instanceof DESMarketTracker) {
						currentDESMarketMap.put(Pair.of(harmonisationMudContainer.getEntity(), ((DESMarketTracker) harmonisationAllocationTracker).getDESSalesMarket()),
								harmonisationAllocationTracker);
					}
				}
			}
		}
		final Map<Inventory, Map<AllocationTracker, Map<YearMonth, Integer>>> harmonisedMonthlyCountsMap = new HashMap<>();
		final Map<Inventory, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>> harmonisationAllocationTrackerToCombinedMapMap = new HashMap<>();
		final Map<Inventory, Map<Pair<BaseLegalEntity, SalesContract>, Pair<MUDContainer, AllocationTracker>>> originalSalesContractPairToNewAllocationPairMapMap = new HashMap<>();
		final Map<Inventory, Map<Pair<BaseLegalEntity, DESSalesMarket>, Pair<MUDContainer, AllocationTracker>>> originalDESMarketPairToNewAllocationPairMapMap = new HashMap<>();
		for (final Entry<Inventory, Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>>> rearrangedProfileEntry : rearrangedProfiles
				.entrySet()) {
			final Map<AllocationTracker, Map<YearMonth, Integer>> harmonisedMonthlyCounts = new HashMap<>();
			final Inventory inventory = rearrangedProfileEntry.getKey();
			final Map<Pair<BaseLegalEntity, SalesContract>, AllocationTracker> currentSalesContractMap = salesContractMap.get(inventory);
			final Map<Pair<BaseLegalEntity, DESSalesMarket>, AllocationTracker> currentDESContractMap = desMarketMap.get(inventory);
			final Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> currentCombinedMap = new HashMap<>();
			final Map<Pair<BaseLegalEntity, SalesContract>, Pair<MUDContainer, AllocationTracker>> currentSalesContractToNewAllocationPairMap = new HashMap<>();
			final Map<Pair<BaseLegalEntity, DESSalesMarket>, Pair<MUDContainer, AllocationTracker>> currentDesMarketToNewAllocationPairMap = new HashMap<>();
			harmonisationAllocationTrackerToCombinedMapMap.put(inventory, currentCombinedMap);
			final Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>> currentRearrangement = rearrangedProfileEntry.getValue();
			for (final Entry<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>> remappingEntry : currentRearrangement.entrySet()) {
				final BaseLegalEntity currentRemappingEntity = remappingEntry.getKey().getEntity();
				final MUDContainer replacementMUDContainer = harmonisationMullContainers.get(inventory).getMUDContainers().stream().filter(m -> m.getEntity().equals(currentRemappingEntity))
						.findFirst().get();
				final List<AllocationTracker> directMappings = remappingEntry.getValue().getFirst();
				for (final AllocationTracker allocationTracker : directMappings) {
					if (allocationTracker instanceof SalesContractTracker) {
						final SalesContract salesContract = ((SalesContractTracker) allocationTracker).getSalesContract();
						final AllocationTracker replacementTracker = currentSalesContractMap.get(Pair.of(currentRemappingEntity, salesContract));
						currentCombinedMap.put(replacementTracker, new LinkedList<>(Collections.singleton(Pair.of(remappingEntry.getKey(), allocationTracker))));
						currentSalesContractToNewAllocationPairMap.put(Pair.of(currentRemappingEntity, salesContract), Pair.of(replacementMUDContainer, replacementTracker));
						replacementTracker.setAllocatedAacq(salesContractAacqSatisfiedByFixedCargoes.get(inventory).get(Pair.of(currentRemappingEntity, salesContract)));
						final Map<YearMonth, Integer> monthlyCounts = new HashMap<>();
						harmonisedMonthlyCounts.put(replacementTracker, monthlyCounts);
						phase1aMonthlySalesContract.get(inventory).get(currentRemappingEntity).entrySet().forEach(entry -> monthlyCounts.put(entry.getKey(), entry.getValue().get(salesContract)));
						replacementTracker.setMonthlyAllocations(monthlyCounts);
					} else if (allocationTracker instanceof DESMarketTracker) {
						final DESSalesMarket desSalesMarket = ((DESMarketTracker) allocationTracker).getDESSalesMarket();
						final AllocationTracker replacementTracker = currentDESContractMap.get(Pair.of(currentRemappingEntity, desSalesMarket));
						currentCombinedMap.put(replacementTracker, new LinkedList<>(Collections.singleton(Pair.of(remappingEntry.getKey(), allocationTracker))));
						currentDesMarketToNewAllocationPairMap.put(Pair.of(currentRemappingEntity, desSalesMarket), Pair.of(replacementMUDContainer, replacementTracker));
						replacementTracker.setAllocatedAacq(desSalesMarketAacqSatisfiedByFixedCargoes.get(inventory).get(Pair.of(currentRemappingEntity, desSalesMarket)));
						final Map<YearMonth, Integer> monthlyCounts = new HashMap<>();
						harmonisedMonthlyCounts.put(replacementTracker, monthlyCounts);
						phase1aMonthlyDesSalesMarket.get(inventory).get(currentRemappingEntity).entrySet().forEach(entry -> monthlyCounts.put(entry.getKey(), entry.getValue().get(desSalesMarket)));
						replacementTracker.setMonthlyAllocations(monthlyCounts);
					}
				}
				final Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> combinedMappings = remappingEntry.getValue().getSecond();
				for (final Entry<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> entry2 : combinedMappings.entrySet()) {
					final AllocationTracker allocationTracker = entry2.getKey();
					assert !entry2.getValue().isEmpty();
					final List<Pair<MUDContainer, AllocationTracker>> outputList = new LinkedList<>(entry2.getValue());
					outputList.add(Pair.of(remappingEntry.getKey(), allocationTracker));
					final AllocationTracker replacementTracker;
					if (allocationTracker instanceof SalesContractTracker) {
						final SalesContract salesContract = ((SalesContractTracker) allocationTracker).getSalesContract();
						replacementTracker = currentSalesContractMap.get(Pair.of(currentRemappingEntity, salesContract));
						final Pair<MUDContainer, AllocationTracker> replacementPair = Pair.of(replacementMUDContainer, replacementTracker);
						// remapping shares vessels with first party - following needed for correct
						// volume calculation
						// replacementTracker.setVesselSharing(true);
						currentCombinedMap.put(replacementTracker, outputList);
						outputList.forEach(p -> {
							final BaseLegalEntity currentEntity = p.getFirst().getEntity();
							final AllocationTracker currentAllocationTracker = p.getSecond();
							if (currentAllocationTracker instanceof SalesContractTracker) {
								final Pair<BaseLegalEntity, SalesContract> originalPair = Pair.of(currentEntity, ((SalesContractTracker) currentAllocationTracker).getSalesContract());
								currentSalesContractToNewAllocationPairMap.put(originalPair, replacementPair);
							} else if (currentAllocationTracker instanceof DESMarketTracker) {
								final Pair<BaseLegalEntity, DESSalesMarket> originalPair = Pair.of(currentEntity, ((DESMarketTracker) currentAllocationTracker).getDESSalesMarket());
								currentDesMarketToNewAllocationPairMap.put(originalPair, replacementPair);
							}
						});
						replacementTracker.setAllocatedAacq(outputList.stream().mapToInt(p -> {
							final BaseLegalEntity currentEntity = p.getFirst().getEntity();
							final AllocationTracker currentAllocationTracker = p.getSecond();
							if (currentAllocationTracker instanceof SalesContractTracker) {
								final Pair<BaseLegalEntity, SalesContract> pairToLookup = Pair.of(currentEntity, ((SalesContractTracker) currentAllocationTracker).getSalesContract());
								return salesContractAacqSatisfiedByFixedCargoes.get(inventory).get(pairToLookup);
							} else if (currentAllocationTracker instanceof DESMarketTracker) {
								final Pair<BaseLegalEntity, DESSalesMarket> pairToLookup = Pair.of(currentEntity, ((DESMarketTracker) currentAllocationTracker).getDESSalesMarket());
								return desSalesMarketAacqSatisfiedByFixedCargoes.get(inventory).get(pairToLookup);
							}
							return 0;
						}).sum());
						final Map<YearMonth, Integer> monthlyCounts = new HashMap<>();
						harmonisedMonthlyCounts.put(replacementTracker, monthlyCounts);
						for (YearMonth ym = adpModel.getYearStart(); ym.isBefore(adpModel.getYearEnd()); ym = ym.plusMonths(1)) {
							final YearMonth currentYm = ym;
							final int count = outputList.stream().mapToInt(pair -> {
								final BaseLegalEntity entity = pair.getFirst().getEntity();
								final AllocationTracker alloc = pair.getSecond();
								if (alloc instanceof SalesContractTracker) {
									return phase1aMonthlySalesContract.get(inventory).get(entity).get(currentYm).get(((SalesContractTracker) alloc).getSalesContract());
								} else {
									return phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(currentYm).get(((DESMarketTracker) alloc).getDESSalesMarket());
								}
							}).sum();
							monthlyCounts.put(ym, count);

						}
						replacementTracker.setMonthlyAllocations(monthlyCounts);
					} else if (allocationTracker instanceof DESMarketTracker) {
						final DESSalesMarket desSalesMarket = ((DESMarketTracker) allocationTracker).getDESSalesMarket();
						replacementTracker = currentDESContractMap.get(Pair.of(currentRemappingEntity, desSalesMarket));
						final Pair<MUDContainer, AllocationTracker> replacementPair = Pair.of(replacementMUDContainer, replacementTracker);
						// remapping shares vessels with first party - following needed for correct
						// volume calculation
						// replacementTracker.setVesselSharing(true);
						currentCombinedMap.put(replacementTracker, outputList);
						outputList.forEach(p -> {
							final BaseLegalEntity currentEntity = p.getFirst().getEntity();
							final AllocationTracker currentAllocationTracker = p.getSecond();
							if (currentAllocationTracker instanceof SalesContractTracker) {
								final Pair<BaseLegalEntity, SalesContract> originalPair = Pair.of(currentEntity, ((SalesContractTracker) currentAllocationTracker).getSalesContract());
								currentSalesContractToNewAllocationPairMap.put(originalPair, replacementPair);
							} else if (currentAllocationTracker instanceof DESMarketTracker) {
								final Pair<BaseLegalEntity, DESSalesMarket> originalPair = Pair.of(currentEntity, ((DESMarketTracker) currentAllocationTracker).getDESSalesMarket());
								currentDesMarketToNewAllocationPairMap.put(originalPair, replacementPair);
							}
						});
						replacementTracker.setAllocatedAacq(outputList.stream().mapToInt(p -> {
							final BaseLegalEntity currentEntity = p.getFirst().getEntity();
							final AllocationTracker currentAllocationTracker = p.getSecond();
							if (currentAllocationTracker instanceof SalesContractTracker) {
								final Pair<BaseLegalEntity, SalesContract> pairToLookup = Pair.of(currentEntity, ((SalesContractTracker) currentAllocationTracker).getSalesContract());
								return salesContractAacqSatisfiedByFixedCargoes.get(inventory).get(pairToLookup);
							} else if (currentAllocationTracker instanceof DESMarketTracker) {
								final Pair<BaseLegalEntity, DESSalesMarket> pairToLookup = Pair.of(currentEntity, ((DESMarketTracker) currentAllocationTracker).getDESSalesMarket());
								return desSalesMarketAacqSatisfiedByFixedCargoes.get(inventory).get(pairToLookup);
							}
							return 0;
						}).sum());
						final Map<YearMonth, Integer> monthlyCounts = new HashMap<>();
						harmonisedMonthlyCounts.put(replacementTracker, monthlyCounts);
						for (YearMonth ym = adpModel.getYearStart(); ym.isBefore(adpModel.getYearEnd()); ym = ym.plusMonths(1)) {
							final YearMonth currentYm = ym;
							final int count = outputList.stream().mapToInt(pair -> {
								final BaseLegalEntity entity = pair.getFirst().getEntity();
								final AllocationTracker alloc = pair.getSecond();
								if (alloc instanceof SalesContractTracker) {
									return phase1aMonthlySalesContract.get(inventory).get(entity).get(currentYm).get(((SalesContractTracker) alloc).getSalesContract());
								} else {
									return phase1aMonthlyDesSalesMarket.get(inventory).get(entity).get(currentYm).get(((DESMarketTracker) alloc).getDESSalesMarket());
								}
							}).sum();
							monthlyCounts.put(ym, count);
						}
						replacementTracker.setMonthlyAllocations(monthlyCounts);
					}
				}
			}
			originalSalesContractPairToNewAllocationPairMapMap.put(inventory, currentSalesContractToNewAllocationPairMap);
			originalDESMarketPairToNewAllocationPairMapMap.put(inventory, currentDesMarketToNewAllocationPairMap);
		}

		final Set<Vessel> expectedVessels = profile.getInventories().stream() //
				.flatMap(sProfile -> sProfile.getEntityTable().stream() //
						.flatMap(row -> Stream.concat(row.getDesSalesMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()),
								row.getSalesContractAllocationRows().stream().flatMap(con -> con.getVessels().stream()))) //
				).collect(Collectors.toSet());
		final Map<Vessel, VesselAvailability> vessToVA = sm.getCargoModel().getVesselAvailabilities().stream().filter(va -> expectedVessels.contains(va.getVessel()))
				.collect(Collectors.toMap(VesselAvailability::getVessel, Function.identity()));

		final Map<Inventory, TreeMap<LocalDateTime, InventoryDateTimeEvent>> inventoryHourlyInsAndOuts = new HashMap<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			inventoryHourlyInsAndOuts.put(inventory, getInventoryInsAndOutsHourly(inventory, sm, startDateTime, endDateTimeExclusive));
		}

		for (final MullSubprofile sProfile : profile.getInventories()) {
			int totalInventoryVolume = 0;
			final Inventory currentInventory = sProfile.getInventory();
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> currentInsAndOuts = inventoryHourlyInsAndOuts.get(currentInventory);
			while (currentInsAndOuts.firstKey().isBefore(startDateTime)) {
				final InventoryDateTimeEvent event = currentInsAndOuts.remove(currentInsAndOuts.firstKey());
				totalInventoryVolume += event.getNetVolumeIn();
			}
			currentInsAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
		}

		final Map<Inventory, TreeMap<LocalDateTime, InventoryDateTimeEvent>> newInventoryHourlyInsAndOuts = new HashMap<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			newInventoryHourlyInsAndOuts.put(inventory, getInventoryInsAndOutsHourly(inventory, sm, startDateTime, endDateTimeExclusive));
		}

		for (final MullSubprofile sProfile : harmonisationMullProfile.getInventories()) {
			int totalInventoryVolume = 0;
			final Inventory currentInventory = sProfile.getInventory();
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> currentInsAndOuts = newInventoryHourlyInsAndOuts.get(currentInventory);
			while (currentInsAndOuts.firstKey().isBefore(startDateTime)) {
				final InventoryDateTimeEvent event = currentInsAndOuts.remove(currentInsAndOuts.firstKey());
				totalInventoryVolume += event.getNetVolumeIn();
			}
			currentInsAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
		}

		final LocalDate dayBeforeStart = startDate.minusDays(1);

		final Map<Inventory, LinkedList<CargoBlueprint>> cargoBlueprintsToGenerate = new HashMap<>();
		final Map<Inventory, Integer> inventorySlotCounters = new HashMap<>();

		final Map<Inventory, LinkedList<CargoBlueprint>> newCargoBlueprintsToGenerate = new HashMap<>();
		final Map<Inventory, Integer> newInventorySlotCounters = new HashMap<>();

		final List<PurchaseContract> pcs = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> newIterSwap = new LinkedList<>();

		final List<PurchaseContract> newPcs = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> newNewIterSwap = new LinkedList<>();

		final Map<Inventory, RollingLoadWindow> inventoryRollingWindows = new HashMap<>();
		final Map<Inventory, Iterator<Entry<LocalDateTime, Cargo>>> inventoryExistingCargoes = new HashMap<>();
		final Map<Inventory, Set<String>> inventoryKnownLoadIDs = new HashMap<>();
		final Map<Inventory, Entry<LocalDateTime, Cargo>> nextFixedCargoes = new HashMap<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = inventoryHourlyInsAndOuts.get(inventory).entrySet().iterator();
			newIterSwap.add(Pair.of(inventory, hourlyIter));

			inventorySlotCounters.put(sProfile.getInventory(), 0);
			cargoBlueprintsToGenerate.put(sProfile.getInventory(), new LinkedList<>());

			final Port inventoryPort = inventory.getPort();
			final TreeMap<LocalDateTime, Cargo> existingCargoes = new TreeMap<>();
			final Set<String> knownLoadIDs = new HashSet<>();
			inventoryKnownLoadIDs.put(inventory, knownLoadIDs);
			cargoesToKeep.stream().filter(cargoWrapper -> cargoWrapper.getLoadSlot().getPort().equals(inventoryPort)).forEach(cargoWrapper -> {
				final LoadSlot loadSlot = cargoWrapper.getLoadSlot();
				final LocalDateTime loadStart = loadSlot.isSetWindowStartTime() ? loadSlot.getWindowStart().atTime(LocalTime.of(loadSlot.getWindowStartTime(), 00))
						: loadSlot.getWindowStart().atStartOfDay();
				knownLoadIDs.add(loadSlot.getName());
				assert loadSlot.getCargo() != null;
				existingCargoes.put(loadStart, loadSlot.getCargo());
			});
			if (!existingCargoes.isEmpty()) {
				LocalDateTime currentFirst = existingCargoes.firstKey();
				while (currentFirst.isBefore(startDateTime)) {
					final Cargo earliestCargo = existingCargoes.remove(currentFirst);
					final Slot<?> dSlot = earliestCargo.getSlots().get(1);
					if (dSlot instanceof DischargeSlot) {
						final DischargeSlot dischargeSlot = (DischargeSlot) dSlot;
						Vessel vessel = null;
						if (dischargeSlot.isFOBSale()) {
							vessel = dischargeSlot.getNominatedVessel();
						} else {
							final VesselAssignmentType vat = earliestCargo.getVesselAssignmentType();
							if (vat instanceof VesselAvailability) {
								vessel = ((VesselAvailability) vat).getVessel();
							}
						}
						if (vessel != null) {
							finalPhaseVesselToMostRecentUseDateTime.put(vessel, currentFirst);
						}
					}
					if (existingCargoes.isEmpty()) {
						break;
					}
					currentFirst = existingCargoes.firstKey();
				}
			}

			final Iterator<Entry<LocalDateTime, Cargo>> existingCargoesIter = existingCargoes.entrySet().iterator();
			inventoryExistingCargoes.put(inventory, existingCargoesIter);
			if (existingCargoesIter.hasNext()) {
				nextFixedCargoes.put(inventory, existingCargoesIter.next());
			}
			final int inventoryLoadDuration = inventoryPort.getLoadDuration();
			final OptionalInt optMaxLoadSlotDuration = existingCargoes.entrySet().stream().map(Entry::getValue).map(c -> c.getSlots().get(0))
					.mapToInt(loadSlot -> loadSlot.isSetDuration() ? loadSlot.getDuration() : inventoryLoadDuration).max();
			if (optMaxLoadSlotDuration.isPresent()) {
				inventoryRollingWindows.put(sProfile.getInventory(), new RollingLoadWindow(inventoryLoadDuration, hourlyIter, Math.max(inventoryLoadDuration, optMaxLoadSlotDuration.getAsInt())));
			} else {
				inventoryRollingWindows.put(sProfile.getInventory(), new RollingLoadWindow(inventoryLoadDuration, hourlyIter, inventoryLoadDuration));
			}
		}

		final Map<Inventory, Integer> newInventoryRunningVolume = new HashMap<>();
		final Map<Inventory, PurchaseContract> newInventoryPurchaseContracts = new HashMap<>();
		final Map<Inventory, RollingLoadWindow> newInventoryRollingWindows = new HashMap<>();
		final Map<Inventory, TreeMap<LocalDateTime, Cargo>> existingCargoesContainer = new HashMap<>();
		final Map<Inventory, Iterator<Entry<LocalDateTime, Cargo>>> newInventoryExistingCargoes = new HashMap<>();
		final Map<Inventory, Set<String>> newInventoryKnownLoadIDs = new HashMap<>();
		final Map<Inventory, Entry<LocalDateTime, Cargo>> newNextFixedCargoes = new HashMap<>();
		for (final MullSubprofile sProfile : harmonisationMullProfile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = newInventoryHourlyInsAndOuts.get(inventory).entrySet().iterator();
			newNewIterSwap.add(Pair.of(inventory, hourlyIter));

			newInventoryPurchaseContracts.put(sProfile.getInventory(), newPcs.stream().filter(c -> sProfile.getInventory().getPort().equals(c.getPreferredPort())).findFirst().get());
			newInventoryRunningVolume.put(sProfile.getInventory(), 0);
			newInventorySlotCounters.put(sProfile.getInventory(), 0);
			newCargoBlueprintsToGenerate.put(sProfile.getInventory(), new LinkedList<>());

			final Port inventoryPort = inventory.getPort();
			final TreeMap<LocalDateTime, Cargo> existingCargoes = new TreeMap<>();
			final Set<String> knownLoadIDs = new HashSet<>();
			newInventoryKnownLoadIDs.put(inventory, knownLoadIDs);

			cargoesToKeep.stream().filter(cargoWrapper -> cargoWrapper.getLoadSlot().getPort().equals(inventoryPort)).forEach(cargoWrapper -> {
				final LoadSlot loadSlot = cargoWrapper.getLoadSlot();
				final LocalDateTime loadStart = loadSlot.isSetWindowStartTime() ? loadSlot.getWindowStart().atTime(LocalTime.of(loadSlot.getWindowStartTime(), 00))
						: loadSlot.getWindowStart().atStartOfDay();
				knownLoadIDs.add(loadSlot.getName());
				assert loadSlot.getCargo() != null;
				existingCargoes.put(loadStart, loadSlot.getCargo());
			});
			if (!existingCargoes.isEmpty()) {
				LocalDateTime currentFirst = existingCargoes.firstKey();
				while (currentFirst.isBefore(startDateTime)) {
					final Cargo earliestCargo = existingCargoes.remove(currentFirst);
					final Slot<?> dSlot = earliestCargo.getSlots().get(1);
					if (dSlot instanceof DischargeSlot) {
						final DischargeSlot dischargeSlot = (DischargeSlot) dSlot;
						Vessel vessel = null;
						if (dischargeSlot.isFOBSale()) {
							vessel = dischargeSlot.getNominatedVessel();
						} else {
							final VesselAssignmentType vat = earliestCargo.getVesselAssignmentType();
							if (vat instanceof VesselAvailability) {
								vessel = ((VesselAvailability) vat).getVessel();
							}
						}
						if (vessel != null) {
							phase2VesselToMostRecentUseDateTime.put(vessel, currentFirst);
						}
					}
					if (existingCargoes.isEmpty()) {
						break;
					}
					currentFirst = existingCargoes.firstKey();
				}
			}

			final Iterator<Entry<LocalDateTime, Cargo>> existingCargoesIter = existingCargoes.entrySet().iterator();
			newInventoryExistingCargoes.put(inventory, existingCargoesIter);
			if (existingCargoesIter.hasNext()) {
				newNextFixedCargoes.put(inventory, existingCargoesIter.next());
			}
			final int inventoryLoadDuration = inventoryPort.getLoadDuration();
			final OptionalInt optMaxLoadSlotDuration = existingCargoes.entrySet().stream().map(Entry::getValue).map(c -> c.getSlots().get(0))
					.mapToInt(loadSlot -> loadSlot.isSetDuration() ? loadSlot.getDuration() : inventoryLoadDuration).max();
			if (optMaxLoadSlotDuration.isPresent()) {
				newInventoryRollingWindows.put(sProfile.getInventory(), new RollingLoadWindow(inventoryLoadDuration, hourlyIter, Math.max(inventoryLoadDuration, optMaxLoadSlotDuration.getAsInt())));
			} else {
				newInventoryRollingWindows.put(sProfile.getInventory(), new RollingLoadWindow(inventoryLoadDuration, hourlyIter, inventoryLoadDuration));
			}
			existingCargoesContainer.put(inventory, existingCargoes);
		}
		// final Map<Vessel, LinkedList<LocalDateTime>> harmonisationPhaseVesselUseLookahead = new HashMap<>();
		final List<MullCargoWrapper> sortedCargoesToKeep = cargoesToKeep.stream()
				.sorted((m1, m2) -> LocalDateTime.of(m1.getLoadSlot().getWindowStart(), LocalTime.of(m1.getLoadSlot().getWindowStartTime(), 0))
						.compareTo(LocalDateTime.of(m2.getLoadSlot().getWindowStart(), LocalTime.of(m2.getLoadSlot().getWindowStartTime(), 0))))
				.collect(Collectors.toList());
		final Map<Vessel, List<LocalDateTime>> vesselToForwardUseTime = new HashMap<>();
		sortedCargoesToKeep.forEach(cargoWrapper -> {
			final LocalDateTime localDateTime = LocalDateTime.of(cargoWrapper.getLoadSlot().getWindowStart(), LocalTime.of(cargoWrapper.getLoadSlot().getWindowStartTime(), 0));
			final DischargeSlot dischargeSlot = cargoWrapper.getDischargeSlot();
			if (dischargeSlot instanceof SpotDischargeSlot) {
				final VesselAssignmentType vat = cargoWrapper.getLoadSlot().getCargo().getVesselAssignmentType();
				if (vat instanceof VesselAvailability) {
					final VesselAvailability va = (VesselAvailability) vat;
					vesselToForwardUseTime.computeIfAbsent(va.getVessel(), k -> new LinkedList<>()).add(localDateTime);
				}
			} else {
				if (dischargeSlot.isFOBSale()) {
					vesselToForwardUseTime.computeIfAbsent(dischargeSlot.getNominatedVessel(), k -> new LinkedList<>()).add(localDateTime);
				} else {
					final VesselAssignmentType vat = cargoWrapper.getLoadSlot().getCargo().getVesselAssignmentType();
					if (vat instanceof VesselAvailability) {
						final VesselAvailability va = (VesselAvailability) vat;
						vesselToForwardUseTime.computeIfAbsent(va.getVessel(), k -> new LinkedList<>()).add(localDateTime);
					}
				}
			}
		});
		final Map<Vessel, LocalDateTime> harmonisationVesselToNextForwardUseTime = new HashMap<>();
		final Map<Vessel, Iterator<LocalDateTime>> harmonisationVesselToForwardUseIterators = new HashMap<>();
		for (Entry<Vessel, List<LocalDateTime>> entry : vesselToForwardUseTime.entrySet()) {
			assert !entry.getValue().isEmpty();
			final Iterator<LocalDateTime> currentUseTimeIter = entry.getValue().iterator();
			harmonisationVesselToForwardUseIterators.put(entry.getKey(), currentUseTimeIter);
			harmonisationVesselToNextForwardUseTime.put(entry.getKey(), currentUseTimeIter.next());
		}

		for (LocalDateTime currentDateTime2 = startDateTime; currentDateTime2.isBefore(endDateTimeExclusive); currentDateTime2 = currentDateTime2.plusHours(1)) {
			for (Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> curr : newNewIterSwap) {
				final Inventory currentInventory = curr.getFirst();
				final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIter = curr.getSecond();

				final RollingLoadWindow currentLoadWindow = newInventoryRollingWindows.get(currentInventory);
				final InventoryDateTimeEvent newEndWindowEvent;
				if (currentIter.hasNext()) {
					newEndWindowEvent = currentIter.next().getValue();
				} else {
					final InventoryDateTimeEvent previousDateTimeEvent = currentLoadWindow.getLastEvent();
					newEndWindowEvent = new InventoryDateTimeEvent(currentDateTime2, 0, previousDateTimeEvent.minVolume, previousDateTimeEvent.maxVolume);
				}

				final InventoryDateTimeEvent currentEvent = currentLoadWindow.getCurrentEvent();
				final MULLContainer currentMULLContainer = harmonisationMullContainers.get(currentInventory);
				currentMULLContainer.updateRunningAllocation(currentEvent.getNetVolumeIn());

				final LocalDateTime currentDateTime = currentLoadWindow.getStartDateTime();
				assert currentDateTime.equals(currentDateTime2);

				if (isAtStartHourOfMonth(currentDateTime)) {
					final YearMonth currentYM = YearMonth.from(currentDateTime);
					final int monthIn = newInventoryHourlyInsAndOuts.get(currentInventory).entrySet().stream() //
							.filter(p -> YearMonth.from(p.getKey()).equals(currentYM)) //
							.mapToInt(p -> p.getValue().getNetVolumeIn()) //
							.sum();
					currentMULLContainer.updateCurrentMonthAbsoluteEntitlement(monthIn);
					currentMULLContainer.updateCurrentMonthAllocations(currentYM);
				}
				final Entry<LocalDateTime, Cargo> nextFixedCargo = newNextFixedCargoes.get(currentInventory);
				if (nextFixedCargo != null && nextFixedCargo.getKey().equals(currentDateTime)) {
					final LinkedList<CargoBlueprint> currentCargoBlueprintsToGenerate = newCargoBlueprintsToGenerate.get(currentInventory);
					// Start fixed cargo load on rolling window return any undos on generated
					// cargoes
					final List<CargoBlueprint> cargoBlueprintsToUndo = currentLoadWindow.startFixedLoad(nextFixedCargo.getValue(), currentCargoBlueprintsToGenerate);

					final LoadSlot loadSlot = (LoadSlot) nextFixedCargo.getValue().getSlots().get(0);
					final DischargeSlot dischargeSlot = (DischargeSlot) nextFixedCargo.getValue().getSlots().get(1);
					Vessel fixedCargoAssignedVessel = null;

					if (dischargeSlot instanceof SpotSlot) {
						// originalSalesContractPairToNewAllocationPairMapMap
						final Map<Pair<BaseLegalEntity, DESSalesMarket>, Pair<MUDContainer, AllocationTracker>> currentContainer = originalDESMarketPairToNewAllocationPairMapMap.get(currentInventory);
						final Pair<MUDContainer, AllocationTracker> currentPair = currentContainer.get(Pair.of(loadSlot.getEntity(), (DESSalesMarket) ((SpotDischargeSlot) dischargeSlot).getMarket()));
						if (currentPair != null) {
							final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
							assert currentMULLContainer.getMUDContainers().contains(currentPair.getFirst());
							currentPair.getFirst().dropFixedLoad(expectedVolumeLoaded);
							currentPair.getSecond().dropFixedLoad(expectedVolumeLoaded);
							final VesselAssignmentType vat = nextFixedCargo.getValue().getVesselAssignmentType();
							if (vat instanceof VesselAvailability) {
								final VesselAvailability va = (VesselAvailability) vat;
								final Vessel vess = va.getVessel();
								if (currentPair.getSecond().getVessels().contains(vess)) {
									fixedCargoAssignedVessel = vess;
								}
							}
						}
					} else {
						if (dischargeSlot.isFOBSale()) {
							Optional<MUDContainer> optMudContainer = currentMULLContainer.getMUDContainers().stream().filter(m -> m.getEntity().equals(loadSlot.getEntity())).findFirst();
							if (optMudContainer.isPresent()) {
								final MUDContainer mudContainer = optMudContainer.get();
								final Optional<DESMarketTracker> optDesMarketTracker = mudContainer.getAllocationTrackers().stream().filter(DESMarketTracker.class::isInstance)
										.map(DESMarketTracker.class::cast).findFirst();
								if (optDesMarketTracker.isPresent()) {
									final DESMarketTracker desMarketTracker = optDesMarketTracker.get();
									final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
									mudContainer.dropFixedLoad(expectedVolumeLoaded);
									desMarketTracker.dropFixedLoad(expectedVolumeLoaded);
									final Vessel vess = dischargeSlot.getNominatedVessel();
									if (desMarketTracker.getVessels().contains(vess)) {
										fixedCargoAssignedVessel = vess;
									}
								}
							}
						} else {
							final Map<Pair<BaseLegalEntity, SalesContract>, Pair<MUDContainer, AllocationTracker>> currentContainer = originalSalesContractPairToNewAllocationPairMapMap
									.get(currentInventory);
							final Pair<MUDContainer, AllocationTracker> currentPair = currentContainer.get(Pair.of(loadSlot.getEntity(), dischargeSlot.getContract()));
							if (currentPair != null) {
								final int expectedVolumeLoaded = loadSlot.getSlotOrDelegateMaxQuantity();
								assert currentMULLContainer.getMUDContainers().contains(currentPair.getFirst());
								currentPair.getFirst().dropFixedLoad(expectedVolumeLoaded);
								currentPair.getSecond().dropFixedLoad(expectedVolumeLoaded);
								final VesselAssignmentType vat = nextFixedCargo.getValue().getVesselAssignmentType();
								if (vat instanceof VesselAvailability) {
									final VesselAvailability va = (VesselAvailability) vat;
									final Vessel vess = va.getVessel();
									if (currentPair.getSecond().getVessels().contains(vess)) {
										fixedCargoAssignedVessel = vess;
									}
								}
							}
						}
					}

					final Set<Vessel> vesselsToUndo = new HashSet<>();
					for (final CargoBlueprint cargoBlueprint : cargoBlueprintsToUndo) {
						// currentMULLContainer.undo(cargoBlueprint);
						currentMULLContainer.harmonisationPhaseUndo(cargoBlueprint);
						final Vessel undoneVessel = cargoBlueprint.getAssignedVessel();
						if (undoneVessel != null) {
							vesselsToUndo.add(undoneVessel);
						}
					}
					final Iterator<CargoBlueprint> reverseCargoBlueprints = currentCargoBlueprintsToGenerate.descendingIterator();
					while (!vesselsToUndo.isEmpty()) {
						if (!reverseCargoBlueprints.hasNext()) {
							break;
						}
						final CargoBlueprint cargoBlueprint = reverseCargoBlueprints.next();
						final Vessel assignedVessel = cargoBlueprint.getAssignedVessel();
						if (vesselsToUndo.contains(assignedVessel)) {
							phase2VesselToMostRecentUseDateTime.put(assignedVessel, cargoBlueprint.getWindowStart());
							vesselsToUndo.remove(assignedVessel);
						}
					}
					if (currentCargoBlueprintsToGenerate.isEmpty()) {
						newInventorySlotCounters.put(currentInventory, 0);
					} else {
						newInventorySlotCounters.put(currentInventory, currentCargoBlueprintsToGenerate.getLast().getLoadCounter() + 1);
					}
					for (final Vessel vesselToUndo : vesselsToUndo) {
						phase2VesselToMostRecentUseDateTime.put(vesselToUndo, dateTimeBeforeADPStart);
					}
					if (fixedCargoAssignedVessel != null) {
						phase2VesselToMostRecentUseDateTime.put(fixedCargoAssignedVessel, currentDateTime2);
						final Iterator<LocalDateTime> iter = harmonisationVesselToForwardUseIterators.get(fixedCargoAssignedVessel);
						if (iter.hasNext()) {
							harmonisationVesselToNextForwardUseTime.put(fixedCargoAssignedVessel, iter.next());
						} else {
							harmonisationVesselToNextForwardUseTime.remove(fixedCargoAssignedVessel);
						}
					}

					final Iterator<Entry<LocalDateTime, Cargo>> nextFixedCargoesIter = newInventoryExistingCargoes.get(currentInventory);
					if (nextFixedCargoesIter.hasNext()) {
						newNextFixedCargoes.put(currentInventory, nextFixedCargoesIter.next());
					} else {
						newNextFixedCargoes.remove(currentInventory);
					}
				}
				if (!currentLoadWindow.isLoading()) {
					final MUDContainer mullMUDContainer = currentMULLContainer.harmonisationPhaseCalculateMULL(phase2VesselToMostRecentUseDateTime, harmonisationVesselToNextForwardUseTime,
							cargoVolume, firstPartyVessels, currentDateTime);
					final AllocationTracker mudAllocationTracker = mullMUDContainer.harmonisationPhaseCalculateMUDAllocationTracker();
					final List<Vessel> mudVesselRestrictions = mudAllocationTracker.getVessels();
					final int currentAllocationDrop;
					final Vessel assignedVessel;
					if (!mudVesselRestrictions.isEmpty()) {
						assignedVessel = mudVesselRestrictions.stream().max((v1, v2) -> {
							final LocalDateTime lookahead1 = harmonisationVesselToNextForwardUseTime.get(v1);
							final LocalDateTime lookahead2 = harmonisationVesselToNextForwardUseTime.get(v2);
							final LocalDateTime lookback1 = phase2VesselToMostRecentUseDateTime.get(v1);
							final LocalDateTime lookback2 = phase2VesselToMostRecentUseDateTime.get(v2);
							// Get minimum difference in hours
							final int hoursDiff1;
							final int hoursDiff2;
							if (lookahead1 != null) {
								hoursDiff1 = Math.min(Hours.between(lookback1, currentDateTime), Hours.between(currentDateTime, lookahead1));
							} else {
								hoursDiff1 = Hours.between(lookback1, currentDateTime);
							}
							if (lookahead2 != null) {
								hoursDiff2 = Math.min(Hours.between(lookback2, currentDateTime), Hours.between(currentDateTime, lookahead2));
							} else {
								hoursDiff2 = Hours.between(lookback2, currentDateTime);
							}
							// Compare hour differences - largest is vessel furthest from being used
							return Integer.compare(hoursDiff1, hoursDiff2);
						}).get();
						currentAllocationDrop = mudAllocationTracker.calculateExpectedAllocationDrop(assignedVessel, currentInventory.getPort().getLoadDuration(),
								firstPartyVessels.contains(assignedVessel));
					} else {
						assignedVessel = null;
						currentAllocationDrop = cargoVolume;
					}
					if (currentLoadWindow.canLift(currentAllocationDrop)) {
						final Set<String> currentKnownLoadIDs = newInventoryKnownLoadIDs.get(currentInventory);
						currentLoadWindow.startLoad(currentAllocationDrop);
						mullMUDContainer.dropAllocation(currentAllocationDrop);
						mudAllocationTracker.harmonisationPhaseDropAllocation(currentAllocationDrop);

						int nextLoadCount = newInventorySlotCounters.get(currentInventory);
						String expectedLoadName = String.format("%s-%03d", currentInventory.getName(), nextLoadCount);
						while (currentKnownLoadIDs.contains(expectedLoadName)) {
							++nextLoadCount;
							expectedLoadName = String.format("%s-%03d", currentInventory.getName(), nextLoadCount);
						}

						final int volumeHigh;
						final int volumeLow;
						if (debugFlex) {
							final int flexDifference = currentLoadWindow.getEndWindowVolume() - currentAllocationDrop - currentLoadWindow.getEndWindowTankMin();
							assert flexDifference >= 0;
							volumeHigh = currentAllocationDrop;
							volumeLow = currentAllocationDrop - volumeFlex;
						} else {
							volumeHigh = currentAllocationDrop + volumeFlex;
							volumeLow = currentAllocationDrop - volumeFlex;
						}
						final CargoBlueprint currentCargoBlueprint = new CargoBlueprint(currentInventory, newInventoryPurchaseContracts.get(currentInventory), nextLoadCount, assignedVessel,
								currentLoadWindow.getStartDateTime(), loadWindowHours, mudAllocationTracker, currentAllocationDrop, mullMUDContainer.getEntity(), volumeHigh, volumeLow);
						if (!newCargoBlueprintsToGenerate.get(currentInventory).isEmpty()) {
							final CargoBlueprint previousCargoBlueprint = newCargoBlueprintsToGenerate.get(currentInventory).getLast();
							final LocalDateTime earliestPreviousStart = currentLoadWindow.getStartDateTime().minusHours(currentInventory.getPort().getLoadDuration());
							final int newWindowHours = Hours.between(previousCargoBlueprint.getWindowStart(), earliestPreviousStart);
							previousCargoBlueprint.updateWindowSize(newWindowHours);
						}
						newCargoBlueprintsToGenerate.get(currentInventory).add(currentCargoBlueprint);
						newInventorySlotCounters.put(currentInventory, nextLoadCount + 1);
						if (assignedVessel != null) {
							phase2VesselToMostRecentUseDateTime.put(assignedVessel, currentCargoBlueprint.getWindowStart());
						}
					}
				}
				currentLoadWindow.stepForward(newEndWindowEvent);
			}
		}

		// Do second pass on cargoes to generate
		final Map<Inventory, Map<YearMonth, Map<AllocationTracker, Integer>>> finalPhaseMonthlyAllocations = new HashMap<>();
		for (final Entry<Inventory, MULLContainer> entryA : finalPhaseMullContainers.entrySet()) {
			final Map<YearMonth, Map<AllocationTracker, Integer>> mapB = new HashMap<>();
			finalPhaseMonthlyAllocations.put(entryA.getKey(), mapB);
			for (YearMonth ym = adpModel.getYearStart(); ym.isBefore(adpModel.getYearEnd()); ym = ym.plusMonths(1)) {
				final Map<AllocationTracker, Integer> mapC = new HashMap<>();
				mapB.put(ym, mapC);
				for (final MUDContainer mudContainer : entryA.getValue().getMUDContainers()) {
					final BaseLegalEntity entity = mudContainer.getEntity();
					for (final AllocationTracker alloc : mudContainer.getAllocationTrackers()) {
						if (alloc instanceof SalesContractTracker) {
							final SalesContractTracker sct = (SalesContractTracker) alloc;
							final int count = phase1aMonthlySalesContract.get(entryA.getKey()).get(entity).get(ym).get(sct.getSalesContract());
							mapC.put(alloc, count);
						} else {
							final DESMarketTracker dmt = (DESMarketTracker) alloc;
							final int count = phase1aMonthlyDesSalesMarket.get(entryA.getKey()).get(entity).get(ym).get(dmt.getDESSalesMarket());
							mapC.put(alloc, count);
						}
					}
				}
			}
		}

		final Map<Inventory, Map<YearMonth, List<Cargo>>> monthMappedExistingCargoes = new HashMap<>();
		for (final Entry<Inventory, TreeMap<LocalDateTime, Cargo>> entry1 : existingCargoesContainer.entrySet()) {
			final Map<YearMonth, List<Cargo>> map1 = new HashMap<>();
			for (final Entry<LocalDateTime, Cargo> entry2 : entry1.getValue().entrySet()) {
				map1.computeIfAbsent(YearMonth.from(entry2.getKey()), k -> new LinkedList<>()).add(entry2.getValue());
			}
			monthMappedExistingCargoes.put(entry1.getKey(), map1);
		}
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> secondPassIterSwap = new LinkedList<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = inventoryHourlyInsAndOuts.get(inventory).entrySet().iterator();
			secondPassIterSwap.add(Pair.of(inventory, hourlyIter));
		}
		final Map<Inventory, CargoBlueprint[]> secondPassCargoBlueprints = new HashMap<>();
		final Map<Inventory, Integer> secondPassCargoBlueprintIndex = new HashMap<>();
		final Map<Inventory, Integer> secondPassOldCargoBlueprintsIndexStart = new HashMap<>();
		final Map<Inventory, CargoBlueprint> secondPassNextCargoBlueprints = new HashMap<>();
		final Map<Inventory, Iterator<CargoBlueprint>> secondPassCargoBlueprintIters = new HashMap<>();
		for (final Entry<Inventory, LinkedList<CargoBlueprint>> entry : newCargoBlueprintsToGenerate.entrySet()) {
			secondPassCargoBlueprints.put(entry.getKey(), new CargoBlueprint[entry.getValue().size()]);
			secondPassCargoBlueprintIndex.put(entry.getKey(), 0);
			secondPassOldCargoBlueprintsIndexStart.put(entry.getKey(), 0);
			final Iterator<CargoBlueprint> cargoBlueprintIter = entry.getValue().iterator();
			secondPassNextCargoBlueprints.put(entry.getKey(), cargoBlueprintIter.hasNext() ? cargoBlueprintIter.next() : null);
			secondPassCargoBlueprintIters.put(entry.getKey(), cargoBlueprintIter);
		}

		for (YearMonth currentYearMonth = adpModel.getYearStart(); currentYearMonth.isBefore(adpModel.getYearEnd()); currentYearMonth = currentYearMonth.plusMonths(1)) {
			final Map<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> persistedInventoryEventsContainer = new HashMap<>();
			final Map<Inventory, List<CargoBlueprint>> persistedInventoryOldCargoBlueprintsContainer = new HashMap<>();
			final Map<Inventory, List<Integer>> persistedInventoryOldCargoBlueprintsIndicesContainer = new HashMap<>();
			final Set<Integer> deferredIndices = new HashSet<>();
			// Persist iterator data
			for (Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> curr : secondPassIterSwap) {
				final List<Entry<LocalDateTime, InventoryDateTimeEvent>> currentPersistedInventoryEvents = new LinkedList<>();
				final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIterator = curr.getSecond();
				assert currentIterator.hasNext();
				Entry<LocalDateTime, InventoryDateTimeEvent> currentEntry = currentIterator.next();
				assert YearMonth.from(currentEntry.getKey()).equals(currentYearMonth);
				final LocalDateTime lastExpectedDateTime = currentYearMonth.atEndOfMonth().atTime(23, 00);
				while (currentEntry.getKey().isBefore(lastExpectedDateTime)) {
					currentPersistedInventoryEvents.add(currentEntry);
					assert currentIterator.hasNext();
					currentEntry = currentIterator.next();
				}
				assert currentEntry.getKey().equals(lastExpectedDateTime);
				currentPersistedInventoryEvents.add(currentEntry);
				persistedInventoryEventsContainer.put(curr.getFirst(), currentPersistedInventoryEvents);

				final List<CargoBlueprint> currentPersistedInventoryOldCargoBlueprints = new LinkedList<>();
				final List<Integer> currentPersistedInventoryOldCargoBlueprintsIndices = new LinkedList<>();
				final Iterator<CargoBlueprint> oldCargoBlueprintsIter = secondPassCargoBlueprintIters.get(curr.getFirst());
				CargoBlueprint c = secondPassNextCargoBlueprints.get(curr.getFirst());
				if (c != null) {
					while (c != null && YearMonth.from(c.getWindowStart()).equals(currentYearMonth)) {
						currentPersistedInventoryOldCargoBlueprints.add(c);
						if (oldCargoBlueprintsIter.hasNext()) {
							c = oldCargoBlueprintsIter.next();
						} else {
							c = null;
						}
					}
					secondPassNextCargoBlueprints.put(curr.getFirst(), c);
				}
				persistedInventoryOldCargoBlueprintsContainer.put(curr.getFirst(), currentPersistedInventoryOldCargoBlueprints);
				final int indexStart = secondPassOldCargoBlueprintsIndexStart.get(curr.getFirst());
				for (int i = 0; i < currentPersistedInventoryOldCargoBlueprints.size(); ++i) {
					currentPersistedInventoryOldCargoBlueprintsIndices.add(indexStart + i);
				}
				persistedInventoryOldCargoBlueprintsIndicesContainer.put(curr.getFirst(), currentPersistedInventoryOldCargoBlueprintsIndices);
				secondPassOldCargoBlueprintsIndexStart.put(curr.getFirst(), indexStart + currentPersistedInventoryOldCargoBlueprints.size());
			}

			for (final Entry<Inventory, List<Entry<LocalDateTime, InventoryDateTimeEvent>>> currentPersistedContainerEntry : persistedInventoryEventsContainer.entrySet()) {

				final Map<LocalDateTime, CargoBlueprint> entitySyncedCargoBlueprints = new TreeMap<>();
				final Inventory currentInventory = currentPersistedContainerEntry.getKey();
				final MULLContainer currentMULLContainer = finalPhaseMullContainers.get(currentInventory);
				final List<Entry<LocalDateTime, InventoryDateTimeEvent>> currentPersisitedEventList = currentPersistedContainerEntry.getValue();

				final Map<AllocationTracker, Integer> monthlyAllocations = finalPhaseMonthlyAllocations.get(currentInventory).get(currentYearMonth);

				assert isAtStartHourOfMonth(currentPersisitedEventList.get(0).getKey());
				final int monthIn = currentPersisitedEventList.stream().map(Entry::getValue).mapToInt(InventoryDateTimeEvent::getNetVolumeIn).sum();
				currentMULLContainer.updateCurrentMonthAbsoluteEntitlement(monthIn);

				currentMULLContainer.getMUDContainers().get(0).getCurrentMonthAbsoluteEntitlement();
				final Map<BaseLegalEntity, Integer> temporaryMonthAbsoluteEntitlement = new HashMap<>();
				currentMULLContainer.getMUDContainers().stream()
						.forEach(mudContainer -> temporaryMonthAbsoluteEntitlement.put(mudContainer.getEntity(), mudContainer.getCurrentMonthAbsoluteEntitlement()));
				final Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> currentAllocationTrackerToCombinedMap = harmonisationAllocationTrackerToCombinedMapMap.get(currentInventory);

				Iterator<CargoBlueprint> iterCargoBlueprint = persistedInventoryOldCargoBlueprintsContainer.get(currentInventory).iterator();
				Iterator<Integer> iterCargoBlueprintIndices = persistedInventoryOldCargoBlueprintsIndicesContainer.get(currentInventory).iterator();

				// Update status of fixed cargoes
				final Map<YearMonth, List<Cargo>> currentMonthMappedFixedCargoes = monthMappedExistingCargoes.get(currentInventory);
				final List<Cargo> currentCargoes = currentMonthMappedFixedCargoes.get(currentYearMonth);
				if (currentCargoes != null) {
					currentCargoes.forEach(currentMULLContainer::dropFixedLoad);
				}

				// Find obvious cargoes to map
				while (iterCargoBlueprint.hasNext()) {
					final CargoBlueprint currentCargoBlueprint = iterCargoBlueprint.next();
					final int currentCargoBlueprintIndex = iterCargoBlueprintIndices.next();
					final AllocationTracker allocationTrackerToReplace = currentCargoBlueprint.getAllocationTracker();
					final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);

					if (combinedTrackers.size() == 1) {
						final Pair<MUDContainer, AllocationTracker> replacementPair = combinedTrackers.get(0);
						monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocations.get(replacementPair.getSecond()) - 1);

						final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(), replacementPair.getFirst().getEntity());
						entitySyncedCargoBlueprints.put(replacementCargoBlueprint.getWindowStart(), replacementCargoBlueprint);
						iterCargoBlueprint.remove();
						iterCargoBlueprintIndices.remove();
						secondPassCargoBlueprints.get(currentInventory)[currentCargoBlueprintIndex] = replacementCargoBlueprint;

						replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
						replacementPair.getSecond().finalPhaseDropAllocation(replacementCargoBlueprint.getAllocatedVolume());
					}
				}

				// Find cargoes where monthly allocation leaves an obvious choice
				iterCargoBlueprint = persistedInventoryOldCargoBlueprintsContainer.get(currentInventory).iterator();
				iterCargoBlueprintIndices = persistedInventoryOldCargoBlueprintsIndicesContainer.get(currentInventory).iterator();
				while (iterCargoBlueprint.hasNext()) {
					final CargoBlueprint currentCargoBlueprint = iterCargoBlueprint.next();
					final int currentCargoBlueprintIndex = iterCargoBlueprintIndices.next();
					final AllocationTracker allocationTrackerToReplace = currentCargoBlueprint.getAllocationTracker();
					final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);

					final List<Pair<MUDContainer, AllocationTracker>> monthAllocatedTrackers = combinedTrackers.stream().filter(pair -> monthlyAllocations.get(pair.getSecond()) > 0)
							.collect(Collectors.toList());
					if (monthAllocatedTrackers.size() == 1) {
						final Pair<MUDContainer, AllocationTracker> replacementPair = monthAllocatedTrackers.get(0);
						monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocations.get(replacementPair.getSecond()) - 1);

						final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(), replacementPair.getFirst().getEntity());
						entitySyncedCargoBlueprints.put(replacementCargoBlueprint.getWindowStart(), replacementCargoBlueprint);
						iterCargoBlueprint.remove();
						iterCargoBlueprintIndices.remove();
						secondPassCargoBlueprints.get(currentInventory)[currentCargoBlueprintIndex] = replacementCargoBlueprint;

						replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
						replacementPair.getSecond().finalPhaseDropAllocation(replacementCargoBlueprint.getAllocatedVolume());
					}
				}

				// Find cargoes where AACQ satisfaction leaves an obvious entity or cargo choice, e.g. minor lifter has met their AACQ
				final List<Pair<Integer, CargoBlueprint>> deferredAACQSatisfactionCargoBlueprints = new LinkedList<>();
				iterCargoBlueprint = persistedInventoryOldCargoBlueprintsContainer.get(currentInventory).iterator();
				iterCargoBlueprintIndices = persistedInventoryOldCargoBlueprintsIndicesContainer.get(currentInventory).iterator();
				while (iterCargoBlueprint.hasNext()) {
					final CargoBlueprint currentCargoBlueprint = iterCargoBlueprint.next();
					final int currentCargoBlueprintIndex = iterCargoBlueprintIndices.next();
					final AllocationTracker allocationTrackerToReplace = currentCargoBlueprint.getAllocationTracker();
					final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);

					final List<Pair<MUDContainer, AllocationTracker>> nonAACQSatisfiedTrackers = combinedTrackers.stream().filter(p -> !p.getSecond().finalPhaseSatisfiedAACQ())
							.collect(Collectors.toList());
					if (nonAACQSatisfiedTrackers.size() == 1) {
						final Pair<MUDContainer, AllocationTracker> replacementPair = nonAACQSatisfiedTrackers.get(0);
						monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocations.get(replacementPair.getSecond()) - 1);
						final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(), replacementPair.getFirst().getEntity());
						entitySyncedCargoBlueprints.put(replacementCargoBlueprint.getWindowStart(), replacementCargoBlueprint);
						iterCargoBlueprint.remove();
						iterCargoBlueprintIndices.remove();
						secondPassCargoBlueprints.get(currentInventory)[currentCargoBlueprintIndex] = replacementCargoBlueprint;

						replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
						replacementPair.getSecond().finalPhaseDropAllocation(replacementCargoBlueprint.getAllocatedVolume());
					} else {
						final Set<BaseLegalEntity> entitledEntities = new HashSet<>();
						nonAACQSatisfiedTrackers.stream().map(Pair::getFirst).map(MUDContainer::getEntity).forEach(entitledEntities::add);
						if (entitledEntities.size() == 1) {
							deferredAACQSatisfactionCargoBlueprints.add(Pair.of(currentCargoBlueprintIndex, currentCargoBlueprint));
							deferredIndices.add(currentCargoBlueprintIndex);
							iterCargoBlueprint.remove();
							iterCargoBlueprintIndices.remove();

							nonAACQSatisfiedTrackers.get(0).getFirst().dropAllocation(currentCargoBlueprint.getAllocatedVolume());
						}
					}
				}

				// Find cargoes that can be deferred (have to decide for same entity-multiple
				// discharge option [not tracking discharge tracker allocation yet])
				final List<Pair<Integer, CargoBlueprint>> deferredCargoBlueprints = new LinkedList<>();
				iterCargoBlueprint = persistedInventoryOldCargoBlueprintsContainer.get(currentInventory).iterator();
				iterCargoBlueprintIndices = persistedInventoryOldCargoBlueprintsIndicesContainer.get(currentInventory).iterator();
				while (iterCargoBlueprint.hasNext()) {
					final CargoBlueprint currentCargoBlueprint = iterCargoBlueprint.next();
					final int currentCargoBlueprintIndex = iterCargoBlueprintIndices.next();
					final AllocationTracker allocationTrackerToReplace = currentCargoBlueprint.getAllocationTracker();
					final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);

					final Set<BaseLegalEntity> entitledEntities = new HashSet<>();
					assert combinedTrackers.size() > 1;
					combinedTrackers.stream().forEach(p -> entitledEntities.add(p.getFirst().getEntity()));
					if (entitledEntities.size() == 1) {
						deferredCargoBlueprints.add(Pair.of(currentCargoBlueprintIndex, currentCargoBlueprint));
						deferredIndices.add(currentCargoBlueprintIndex);
						iterCargoBlueprint.remove();
						iterCargoBlueprintIndices.remove();

						// MUD container is the same so just update the first option
						combinedTrackers.get(0).getFirst().dropAllocation(currentCargoBlueprint.getAllocatedVolume());
					}
				}

				// Allocate trackers that have an obvious FCL violation (i.e. only one of the
				// trackers is above the FCL upper limit)
				boolean madeChanges = true;
				while (madeChanges) {
					madeChanges = false;
					iterCargoBlueprint = persistedInventoryOldCargoBlueprintsContainer.get(currentInventory).iterator();
					iterCargoBlueprintIndices = persistedInventoryOldCargoBlueprintsIndicesContainer.get(currentInventory).iterator();
					while (iterCargoBlueprint.hasNext()) {
						final CargoBlueprint currentCargoBlueprint = iterCargoBlueprint.next();
						final int currentCargoBlueprintIndex = iterCargoBlueprintIndices.next();
						final AllocationTracker allocationTrackerToReplace = currentCargoBlueprint.getAllocationTracker();
						final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);

						final int allocationDrop = currentCargoBlueprint.getAllocatedVolume();
						final Set<MUDContainer> uniqueMudContainers = new HashSet<>();
						for (final Pair<MUDContainer, AllocationTracker> pair : combinedTrackers) {
							uniqueMudContainers.add(pair.getFirst());
						}
						List<AllocationDropType> mappedToAllocationTypes = uniqueMudContainers.stream().map(p -> {
							final int currentMonthEntitlement = p.getCurrentMonthAbsoluteEntitlement();
							final int afterDrop = currentMonthEntitlement - allocationDrop;
							if (currentMonthEntitlement > fullCargoLotValue) {
								if (afterDrop > fullCargoLotValue) {
									return AllocationDropType.ABOVE_ABOVE;
								} else if (afterDrop >= -fullCargoLotValue) {
									return AllocationDropType.ABOVE_IN;
								} else {
									return AllocationDropType.ABOVE_BELOW;
								}
							} else if (currentMonthEntitlement > -fullCargoLotValue) {
								return afterDrop >= -fullCargoLotValue ? AllocationDropType.IN_IN : AllocationDropType.IN_BELOW;
							} else {
								return AllocationDropType.BELOW_BELOW;
							}
						}).collect(Collectors.toList());
						final long countAboveAbove = mappedToAllocationTypes.stream().filter(v -> v == AllocationDropType.ABOVE_ABOVE).count();
						if (countAboveAbove > 1) {
							// wait cannot decide
						} else {
							final long countNotEndingBelow = mappedToAllocationTypes.stream()
									.filter(v -> v != AllocationDropType.ABOVE_BELOW && v != AllocationDropType.IN_BELOW && v != AllocationDropType.BELOW_BELOW).count();
							if (countNotEndingBelow == 1L) {
								// Only one entity does not violate lower FCL bound - assign this one
								final Iterator<AllocationDropType> dropTypeIter = mappedToAllocationTypes.iterator();
								final Iterator<MUDContainer> containersIter = uniqueMudContainers.iterator();
								AllocationDropType currentDropType;
								MUDContainer currentMudContainer;
								while (true) {
									currentDropType = dropTypeIter.next();
									currentMudContainer = containersIter.next();
									if (currentDropType != AllocationDropType.ABOVE_BELOW && currentDropType != AllocationDropType.IN_BELOW && currentDropType != AllocationDropType.BELOW_BELOW) {
										break;
									}
								}
								final MUDContainer selectedMudContainer = currentMudContainer;
								List<Pair<MUDContainer, AllocationTracker>> trackersToAssign = combinedTrackers.stream().filter(p -> p.getFirst() == selectedMudContainer).collect(Collectors.toList());
								if (trackersToAssign.size() == 1) {
									final Pair<MUDContainer, AllocationTracker> replacementPair = trackersToAssign.get(0);
									monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocations.get(replacementPair.getSecond()) - 1);

									final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(),
											replacementPair.getFirst().getEntity());
									entitySyncedCargoBlueprints.put(replacementCargoBlueprint.getWindowStart(), replacementCargoBlueprint);
									iterCargoBlueprint.remove();
									iterCargoBlueprintIndices.remove();
									secondPassCargoBlueprints.get(currentInventory)[currentCargoBlueprintIndex] = replacementCargoBlueprint;

									replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
									replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
								} else {
									deferredCargoBlueprints.add(Pair.of(currentCargoBlueprintIndex, currentCargoBlueprint));
									deferredIndices.add(currentCargoBlueprintIndex);
									iterCargoBlueprint.remove();
									iterCargoBlueprintIndices.remove();

									// MUD container is the same so just update the first option [get(0)] (first element of pair is MUD container [getFirst()])
									combinedTrackers.get(0).getFirst().dropAllocation(currentCargoBlueprint.getAllocatedVolume());
								}
								madeChanges = true;
							}
						}
					}
				}

				// At this point all obvious assignments should be made (and deferred cargoes
				// should be accounted for) default to MULL comparison
				iterCargoBlueprint = persistedInventoryOldCargoBlueprintsContainer.get(currentInventory).iterator();
				iterCargoBlueprintIndices = persistedInventoryOldCargoBlueprintsIndicesContainer.get(currentInventory).iterator();
				final List<Pair<Integer, CargoBlueprint>> persistedPairList = new ArrayList<>();
				while (iterCargoBlueprint.hasNext()) {
					persistedPairList.add(Pair.of(iterCargoBlueprintIndices.next(), iterCargoBlueprint.next()));
				}

				// final Iterator<Pair<Integer, CargoBlueprint>> iter = deferredCargoBlueprints.iterator();

				final Iterator<Pair<Integer, CargoBlueprint>> combinedIter = new CombinedCargoBlueprintIterator(persistedPairList, deferredCargoBlueprints, deferredAACQSatisfactionCargoBlueprints);

				Pair<Integer, CargoBlueprint> nextPair = combinedIter.hasNext() ? combinedIter.next() : null;

				if (nextPair != null) {
					CargoBlueprint nextCargoBlueprint = nextPair.getSecond();
					int nextIndex = nextPair.getFirst();
					for (Entry<LocalDateTime, InventoryDateTimeEvent> entry : currentPersistedContainerEntry.getValue()) {
						final InventoryDateTimeEvent currentEvent = entry.getValue();
						currentMULLContainer.updateRunningAllocation(currentEvent.getNetVolumeIn());

						assert entry.getKey().equals(currentEvent.getDateTime());

						if (entry.getKey().equals(nextCargoBlueprint.getWindowStart())) {
							final AllocationTracker allocationTrackerToReplace = nextCargoBlueprint.getAllocationTracker();
							final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);

							// Check if monthly allocations leave an obvious choice
							final List<Pair<MUDContainer, AllocationTracker>> monthAllocatedTrackers = combinedTrackers.stream().filter(pair -> monthlyAllocations.get(pair.getSecond()) > 0)
									.collect(Collectors.toList());
							if (monthAllocatedTrackers.size() == 1) {
								final Pair<MUDContainer, AllocationTracker> replacementPair = monthAllocatedTrackers.get(0);
								monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocations.get(replacementPair.getSecond()) - 1);

								final CargoBlueprint replacementCargoBlueprint = nextCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(),
										replacementPair.getFirst().getEntity());
								entitySyncedCargoBlueprints.put(replacementCargoBlueprint.getWindowStart(), replacementCargoBlueprint);
								// iterCargoBlueprint.remove();
								// iterCargoBlueprintIndices.remove();
								secondPassCargoBlueprints.get(currentInventory)[nextIndex] = replacementCargoBlueprint;

								if (!deferredIndices.contains(nextIndex)) {
									replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
								}
								// replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
								replacementPair.getSecond().finalPhaseDropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							} else {

								final int allocationDrop = nextCargoBlueprint.getAllocatedVolume();
								final Set<MUDContainer> mudChoices = new HashSet<>();
								boolean dumpIntoDES = false;
								for (final Pair<MUDContainer, AllocationTracker> pair : combinedTrackers) {
									if (!pair.getSecond().finalPhaseSatisfiedAACQ()) {
										mudChoices.add(pair.getFirst());
									}
								}
								if (mudChoices.isEmpty()) {
									dumpIntoDES = true;
									for (final Pair<MUDContainer, AllocationTracker> pair : combinedTrackers) {
										if (pair.getSecond() instanceof DESMarketTracker) {
											mudChoices.add(pair.getFirst());
										}
									}
									if (mudChoices.isEmpty()) {
										throw new IllegalStateException("No DES Sales Market option found");
									}
								}
								assert !mudChoices.isEmpty();
								final MUDContainer mostEntitledMUDContainer = mudChoices.stream().max((Comparator<MUDContainer>) (mc0, mc1) -> {
									final Long allocation0 = mc0.getRunningAllocation();
									final Long allocation1 = mc1.getRunningAllocation();
									final int expectedAllocationDrop0 = allocationDrop;
									final int expectedAllocationDrop1 = allocationDrop;
									final int beforeDrop0 = mc0.getCurrentMonthAbsoluteEntitlement();
									final int beforeDrop1 = mc1.getCurrentMonthAbsoluteEntitlement();
									final int afterDrop0 = beforeDrop0 - expectedAllocationDrop0;
									final int afterDrop1 = beforeDrop1 - expectedAllocationDrop1;

									final boolean belowLower0 = afterDrop0 < -fullCargoLotValue;
									final boolean belowLower1 = afterDrop1 < -fullCargoLotValue;
									final boolean aboveUpper0 = afterDrop0 > fullCargoLotValue;
									final boolean aboveUpper1 = afterDrop1 > fullCargoLotValue;
									if (belowLower0) {
										if (belowLower1) {
											if (afterDrop0 < afterDrop1) {
												return -1;
											} else {
												return 1;
											}
										} else {
											return -1;
										}
									} else {
										if (belowLower1) {
											return 1;
										} else {
											if (aboveUpper0) {
												if (aboveUpper1) {
													if (allocation0 > allocation1) {
														return 1;
													} else {
														return -1;
													}
												} else {
													return 1;
												}
											} else {
												if (aboveUpper1) {
													return -1;
												} else {
													if (beforeDrop0 > fullCargoLotValue) {
														if (beforeDrop1 > beforeDrop0) {
															return -1;
														} else {
															return 1;
														}
													} else {
														if (beforeDrop1 > fullCargoLotValue) {
															return -1;
														} else {
															if (allocation0 > allocation1) {
																return 1;
															} else {
																return -1;
															}
														}
													}
												}
											}
										}
									}
								}).get();
								final List<AllocationTracker> potentialAllocationTrackers;
								if (dumpIntoDES) {
									potentialAllocationTrackers = combinedTrackers.stream() //
											.filter(p -> p.getFirst() == mostEntitledMUDContainer) //
											.map(Pair::getSecond) //
											.filter(DESMarketTracker.class::isInstance) //
											.collect(Collectors.toList());
								} else {
									potentialAllocationTrackers = combinedTrackers.stream() //
											.filter(p -> p.getFirst() == mostEntitledMUDContainer) //
											.map(Pair::getSecond) //
											.filter(at -> !at.finalPhaseSatisfiedAACQ()) //
											.collect(Collectors.toList());
								}
								final CargoBlueprint[] newCargoBlueprints = secondPassCargoBlueprints.get(currentInventory);
								if (potentialAllocationTrackers.size() == 1) {
									final Pair<MUDContainer, AllocationTracker> replacementPair = Pair.of(mostEntitledMUDContainer, potentialAllocationTrackers.get(0));

									final CargoBlueprint replacementCargoBlueprint = nextCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(),
											replacementPair.getFirst().getEntity());

									assert newCargoBlueprints[nextIndex] == null;
									newCargoBlueprints[nextIndex] = replacementCargoBlueprint;

									if (!deferredIndices.contains(nextIndex)) {
										replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
									}
									replacementPair.getSecond().finalPhaseDropAllocation(replacementCargoBlueprint.getAllocatedVolume());

									final int monthlyAllocation = monthlyAllocations.get(replacementPair.getSecond());
									if (monthlyAllocation > 0) {
										monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocation - 1);
									}
								} else {
									final AllocationTracker mudAllocationTracker = potentialAllocationTrackers.stream()
											.max((t1, t2) -> Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation())).get();
									final Pair<MUDContainer, AllocationTracker> replacementPair = Pair.of(mostEntitledMUDContainer, mudAllocationTracker);

									final CargoBlueprint replacementCargoBlueprint = nextCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(),
											replacementPair.getFirst().getEntity());

									assert newCargoBlueprints[nextIndex] == null;
									newCargoBlueprints[nextIndex] = replacementCargoBlueprint;

									if (!deferredIndices.contains(nextIndex)) {
										// MUD Container already had allocation dropped
										replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
									}
									replacementPair.getSecond().finalPhaseDropAllocation(replacementCargoBlueprint.getAllocatedVolume());
									final int monthlyAllocation = monthlyAllocations.get(replacementPair.getSecond());
									if (monthlyAllocation > 0) {
										monthlyAllocations.put(replacementPair.getSecond(), monthlyAllocation - 1);
									}
								}
							}

							if (combinedIter.hasNext()) {
								nextPair = combinedIter.next();
								nextCargoBlueprint = nextPair.getSecond();
								nextIndex = nextPair.getFirst();
							} else {
								break;
							}
						}
					}
				}
			}
		}

		// Sanity check that all cargoes are accounted for
		for (CargoBlueprint[] cargoBlueprints : secondPassCargoBlueprints.values()) {
			for (CargoBlueprint cargoBlueprint : cargoBlueprints) {
				assert cargoBlueprint != null;
			}
		}

		// final Map<Inventory, Map<BaseLegalEntity, Map<SalesContractTracker, Integer>>> finalPhaseSalesContractInspect = new HashMap<>();
		// final Map<Inventory, Map<BaseLegalEntity, Map<DESMarketTracker, Integer>>> finalPhaseDesMarketInspect = new HashMap<>();
		// for (final Entry<Inventory, CargoBlueprint[]> entry : secondPassCargoBlueprints.entrySet()) {
		// salesContractInspect.put(entry.getKey(), new HashMap<>());
		// desMarketInspect.put(entry.getKey(), new HashMap<>());
		// for (final CargoBlueprint cargoBlueprint : entry.getValue()) {
		// if (cargoBlueprint.getAllocationTracker() instanceof SalesContractTracker) {
		// final Map<SalesContractTracker, Integer> scMap = salesContractInspect.get(entry.getKey()).computeIfAbsent(cargoBlueprint.getEntity(), k -> new HashMap<>());
		// scMap.compute((SalesContractTracker) cargoBlueprint.getAllocationTracker(), (k, v) -> v == null ? 1 : v + 1);
		// } else {
		// final Map<DESMarketTracker, Integer> dsmMap = desMarketInspect.get(entry.getKey()).computeIfAbsent(cargoBlueprint.getEntity(), k -> new HashMap<>());
		// dsmMap.compute((DESMarketTracker) cargoBlueprint.getAllocationTracker(), (k, v) -> v == null ? 1 : v + 1);
		// }
		// }
		// }

		// System.out.println("==================");
		// System.out.println("Final phase");
		// for (final Inventory inventory : phase1CargoBlueprintsToGenerate.keySet()) {
		// System.out.println(String.format("For inventory %s", inventory.getName()));
		// for (final MUDContainer mudContainer : finalPhaseMullContainers.get(inventory).getMUDContainers()) {
		// final BaseLegalEntity entity = mudContainer.getEntity();
		// System.out.println(String.format("\tFor %s", entity.getName()));
		// final Map<SalesContractTracker, Integer> currSCMap = salesContractInspect.get(inventory).get(entity);
		// if (currSCMap != null) {
		// for (Entry<SalesContractTracker, Integer> entry : currSCMap.entrySet()) {
		// System.out.println(String.format("\t\tSC:%s. Expected: %d. Actual: %d", entry.getKey().getSalesContract().getName(), entry.getKey().getAACQ(), entry.getValue()));
		// }
		// }
		// for (Entry<DESMarketTracker, Integer> entry : desMarketInspect.get(inventory).computeIfAbsent(entity, k -> {
		// final HashMap<DESMarketTracker, Integer> map = new HashMap<>();
		// mudContainer.getAllocationTrackers().stream().filter(DESMarketTracker.class::isInstance).map(DESMarketTracker.class::cast).forEach(tracker -> map.put(tracker, 0));
		// return map;
		// }).entrySet()) {
		// System.out.println(String.format("\t\tDSM:%s. Expected: %d. Actual: %d", entry.getKey().getDESSalesMarket().getName(), entry.getKey().getAACQ(), entry.getValue()));
		// }
		// }
		// }

		final IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
		final Command deleteModelsCommand = ScheduleModelInvalidateCommandProvider.createClearModelsCommand(editingDomain, sm, ScenarioModelUtil.getAnalyticsModel(sm));

		final List<Cargo> cargoesToDelete;
		final List<LoadSlot> loadSlotsToDelete;
		final List<DischargeSlot> dischargeSlotsToDelete;
		if (cargoesToKeep.isEmpty()) {
			cargoesToDelete = ScenarioModelUtil.getCargoModel(sdp).getCargoes().stream().filter(c -> {
				final YearMonth loadYM = YearMonth.from(((LoadSlot) c.getSlots().get(0)).getWindowStart());
				return !loadYM.isBefore(adpModel.getYearStart()) && loadYM.isBefore(adpModel.getYearEnd());
			}).collect(Collectors.toList());
			loadSlotsToDelete = cargoesToDelete.stream().map(c -> (LoadSlot) c.getSlots().get(0)).collect(Collectors.toList());
			dischargeSlotsToDelete = cargoesToDelete.stream().map(c -> (DischargeSlot) c.getSlots().get(1)).collect(Collectors.toList());
		} else {
			final Set<Cargo> cargoesToKeepSet = cargoesToKeep.stream().map(wrapper -> wrapper.getLoadSlot().getCargo()).collect(Collectors.toSet());
			final int numSlotsToDelete = ScenarioModelUtil.getCargoModel(sdp).getCargoes().size() - cargoesToKeepSet.size();
			loadSlotsToDelete = new ArrayList<>(numSlotsToDelete);
			dischargeSlotsToDelete = new ArrayList<>(numSlotsToDelete);
			cargoesToDelete = new ArrayList<>(numSlotsToDelete);
			for (final Cargo cargo : ScenarioModelUtil.getCargoModel(sdp).getCargoes()) {
				final LoadSlot loadSlot = (LoadSlot) cargo.getSlots().get(0);
				final YearMonth loadYM = YearMonth.from(loadSlot.getWindowStart());
				if (!cargoesToKeepSet.contains(cargo) && (!loadYM.isBefore(adpModel.getYearStart()) && loadYM.isBefore(adpModel.getYearEnd()))) {
					cargoesToDelete.add(cargo);
					loadSlotsToDelete.add(loadSlot);
					dischargeSlotsToDelete.add((DischargeSlot) cargo.getSlots().get(1));
				}
			}
		}
		if (!cargoesToDelete.isEmpty()) {
			final List<EObject> objectsToDelete = new ArrayList<>(cargoesToDelete.size() + loadSlotsToDelete.size() + dischargeSlotsToDelete.size());
			objectsToDelete.addAll(cargoesToDelete);
			objectsToDelete.addAll(loadSlotsToDelete);
			objectsToDelete.addAll(dischargeSlotsToDelete);
			cmd.append(DeleteCommand.create(editingDomain, objectsToDelete));
		}

		if (secondPassCargoBlueprints.values().stream().anyMatch(arr -> arr.length > 0)) {
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
			secondPassCargoBlueprints.values().forEach(arr -> {
				for (final CargoBlueprint cargoBlueprint : arr) {
					cargoBlueprint.constructCargoModelPopulationCommands(sm, cargoModel, cec, editingDomain, volumeFlex, sdp, vessToVA, adpNominalMarket, cmd, firstPartyVessels);
				}
			});
		}
		if (cmd.isEmpty()) {
			return IdentityCommand.INSTANCE;
		} else {
			if (deleteModelsCommand != null) {
				editorData.getDefaultCommandHandler().handleCommand(deleteModelsCommand);
			}
			try {
				editorData.setDisableUpdates(true);
				editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
			} finally {
				editorData.setDisableUpdates(false);
			}
		}
		return cmd;
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
		if (object instanceof PurchaseContract) {
			final PurchaseContract contract = (PurchaseContract) object;
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
		} else if (object instanceof SalesContract) {
			final SalesContract contract = (SalesContract) object;
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
		} else if (object instanceof MullProfile) {
			if (editorData.adpModel.getMullProfile() == null) {
				editorData.adpModel.setMullProfile((MullProfile) object);
			}

			target = object;
		}

		detailComposite.setInput(target);
		updatePreviewPaneInput(target);
	}

	private void updatePreviewPaneInput(final EObject target) {
		if (editorData != null && editorData.getScenarioModel() != null) {
			if (previewViewer != null) {
				if (target instanceof PurchaseContractProfile) {
					final PurchaseContractProfile purchaseContractProfile = (PurchaseContractProfile) target;
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
				} else if (target instanceof SalesContractProfile) {
					final SalesContractProfile salesContractProfile = (SalesContractProfile) target;
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

	private TreeMap<LocalDateTime, InventoryDateTimeEvent> getInventoryInsAndOutsHourly(final Inventory inventory, final LNGScenarioModel scenarioModel, final LocalDateTime startDateTimeInclusive,
			final LocalDateTime endDateTimeExclusive) {
		final List<InventoryCapacityRow> capacities = inventory.getCapacities();
		final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap = capacities.stream() //
				.collect(Collectors.toMap(InventoryCapacityRow::getDate, c -> c, (oldVal, newVal) -> newVal, TreeMap::new));
		final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts = new TreeMap<>();

		final InventoryEventRow lastPreAdpLevel = getLastPreAdpLevelFeed(inventory.getFeeds(), startDateTimeInclusive);
		final Stream<InventoryEventRow> inAdpYearFeedsStream = inventory.getFeeds().stream().filter(f -> !f.getStartDate().isBefore(startDateTimeInclusive.toLocalDate()));
		final Stream<InventoryEventRow> feedsOfInterest = lastPreAdpLevel == null ? inAdpYearFeedsStream : Stream.concat(Stream.of(lastPreAdpLevel), inAdpYearFeedsStream);
		final List<InventoryEventRow> filteredFeeds = feedsOfInterest.collect(Collectors.toList());

		addHourlyNetVolumes(filteredFeeds, capacityTreeMap, insAndOuts, IntUnaryOperator.identity());
		addHourlyNetVolumes(inventory.getOfftakes(), capacityTreeMap, insAndOuts, a -> -a);

		for (LocalDateTime currentDateTime = startDateTimeInclusive; currentDateTime.isBefore(endDateTimeExclusive); currentDateTime = currentDateTime.plusHours(1)) {
			if (!insAndOuts.containsKey(currentDateTime)) {
				InventoryCapacityRow capacityRow = capacityTreeMap.get(currentDateTime.toLocalDate());
				if (capacityRow == null) {
					capacityRow = capacityTreeMap.lowerEntry(currentDateTime.toLocalDate()).getValue();
				}
				final InventoryDateTimeEvent inventoryDateTimeEvent = new InventoryDateTimeEvent(currentDateTime, 0, capacityRow.getMinVolume(), capacityRow.getMaxVolume());
				insAndOuts.put(currentDateTime, inventoryDateTimeEvent);
			}
		}
		return insAndOuts;
	}

	private InventoryEventRow getLastPreAdpLevelFeed(final List<InventoryEventRow> feeds, final LocalDateTime startDateTimeInclusive) {
		final LocalDate startLocalDateInclusive = startDateTimeInclusive.toLocalDate();
		final Iterator<InventoryEventRow> levelFeedsBeforeAdpStartIter = feeds.stream().filter(f -> f.getPeriod() == InventoryFrequency.LEVEL && f.getStartDate().isBefore(startLocalDateInclusive))
				.iterator();

		if (!levelFeedsBeforeAdpStartIter.hasNext()) {
			return null;
		}
		InventoryEventRow lastPreAdpLevel = levelFeedsBeforeAdpStartIter.next();
		while (levelFeedsBeforeAdpStartIter.hasNext()) {
			final InventoryEventRow feed = levelFeedsBeforeAdpStartIter.next();
			if (lastPreAdpLevel.getStartDate().isBefore(feed.getStartDate())) {
				lastPreAdpLevel = feed;
			}
		}
		return lastPreAdpLevel;
	}

	private int getYearlyProduction(final Inventory inventory, final LNGScenarioModel scenarioModel, final LocalDateTime startTimeInclusive, final LocalDateTime endTimeExclusive) {
		int production = 0;
		for (final InventoryEventRow eventRow : inventory.getFeeds()) {
			final LocalDate eventStart = eventRow.getStartDate();
			if (eventStart != null && !eventStart.isBefore(startTimeInclusive.toLocalDate()) && eventStart.isBefore(endTimeExclusive.toLocalDate())) {
				if (eventRow.getPeriod() == InventoryFrequency.LEVEL) {
					production += eventRow.getReliableVolume();
				} else if (eventRow.getPeriod() == InventoryFrequency.DAILY) {
					final LocalDate eventEnd = eventRow.getEndDate();
					for (LocalDate currentDate = eventStart; !currentDate.isAfter(eventEnd); currentDate = currentDate.plusDays(1)) {
						production += eventRow.getReliableVolume();
					}
				} else if (eventRow.getPeriod() == InventoryFrequency.HOURLY) {
					final LocalDate eventEnd = eventRow.getEndDate();
					final int delta = 24 * eventRow.getReliableVolume();
					for (LocalDate currentDate = eventStart; !currentDate.isAfter(eventEnd); currentDate = currentDate.plusDays(1)) {
						production += delta;
					}
				}
			}
		}
		return production;
	}

	private void addHourlyNetVolumes(final List<InventoryEventRow> events, final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap,
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final IntUnaryOperator volumeFunction) {
		for (final InventoryEventRow inventoryEventRow : events) {
			final LocalDate eventStart = inventoryEventRow.getStartDate();
			if (eventStart != null) {
				if (inventoryEventRow.getPeriod() == InventoryFrequency.LEVEL) {
					final LocalDateTime expectedDateTime = LocalDateTime.of(eventStart, LocalTime.of(0, 0));
					addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, expectedDateTime, inventoryEventRow.getReliableVolume());
				} else if (inventoryEventRow.getPeriod() == InventoryFrequency.DAILY) {
					final int delta = inventoryEventRow.getReliableVolume() / 24;
					final int firstAmount = delta + inventoryEventRow.getReliableVolume() % 24;
					final LocalDateTime firstDateTime = LocalDateTime.of(eventStart, LocalTime.of(0, 0));
					addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, firstDateTime, firstAmount);
					for (int hour = 1; hour < 24; ++hour) {
						final LocalDateTime expectedDateTime = LocalDateTime.of(eventStart, LocalTime.of(hour, 0));
						addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, expectedDateTime, delta);
					}
				} else if (inventoryEventRow.getPeriod() == InventoryFrequency.HOURLY) {
					final LocalDate eventStop = inventoryEventRow.getEndDate();
					if (eventStop != null && eventStart.isBefore(eventStop)) {
						final int volume = inventoryEventRow.getReliableVolume();
						final LocalDateTime endDateTime = LocalDateTime.of(eventStop, LocalTime.of(0, 0));
						LocalDateTime dateTimeCounter;
						for (dateTimeCounter = LocalDateTime.of(eventStart, LocalTime.of(0, 0)); dateTimeCounter.isBefore(endDateTime); dateTimeCounter = dateTimeCounter.plusHours(1L)) {
							addSingleVolume(capacityTreeMap, insAndOuts, volumeFunction, dateTimeCounter, volume);
						}
					}
				}
			}
		}
	}

	private void addSingleVolume(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final IntUnaryOperator volumeFunction,
			final LocalDateTime expectedDateTime, final int absoluteVolume) {
		InventoryDateTimeEvent inventoryDateTimeEvent = insAndOuts.get(expectedDateTime);
		if (inventoryDateTimeEvent == null) {
			InventoryCapacityRow capacityRow = capacityTreeMap.get(expectedDateTime.toLocalDate());
			if (capacityRow == null) {
				capacityRow = capacityTreeMap.lowerEntry(expectedDateTime.toLocalDate()).getValue();
			}
			final int minVolume = capacityRow.getMinVolume();
			final int maxVolume = capacityRow.getMaxVolume();
			inventoryDateTimeEvent = new InventoryDateTimeEvent(expectedDateTime, volumeFunction.applyAsInt(absoluteVolume), minVolume, maxVolume);
			insAndOuts.put(expectedDateTime, inventoryDateTimeEvent);
		} else {
			inventoryDateTimeEvent.addVolume(volumeFunction.applyAsInt(absoluteVolume));
		}
	}

	private void addSingleVolume(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final LocalDateTime expectedDateTime,
			final int volume) {
		InventoryDateTimeEvent inventoryDateTimeEvent = insAndOuts.get(expectedDateTime);
		if (inventoryDateTimeEvent == null) {
			InventoryCapacityRow capacityRow = capacityTreeMap.get(expectedDateTime.toLocalDate());
			if (capacityRow == null) {
				capacityRow = capacityTreeMap.lowerEntry(expectedDateTime.toLocalDate()).getValue();
			}
			final int minVolume = capacityRow.getMinVolume();
			final int maxVolume = capacityRow.getMaxVolume();
			inventoryDateTimeEvent = new InventoryDateTimeEvent(expectedDateTime, volume, minVolume, maxVolume);
			insAndOuts.put(expectedDateTime, inventoryDateTimeEvent);
		} else {
			inventoryDateTimeEvent.addVolume(volume);
		}
	}

	private boolean isAtEndHourOfMonth(final LocalDateTime localDateTime) {
		final YearMonth ym = YearMonth.from(localDateTime);
		final LocalDate lastDateOfMonth = ym.atEndOfMonth();
		return localDateTime.getDayOfMonth() == lastDateOfMonth.getDayOfMonth() && localDateTime.getHour() == 23;
	}

	private boolean isAtStartHourOfMonth(final LocalDateTime localDateTime) {
		return localDateTime.getDayOfMonth() == 1 && localDateTime.getHour() == 0;
	}

	private MullProfile trimZeroesFromMullProfile(final MullProfile profile) {
		final MullProfile trimmedProfile = ADPFactory.eINSTANCE.createMullProfile();
		trimmedProfile.setFullCargoLotValue(profile.getFullCargoLotValue());
		trimmedProfile.setVolumeFlex(profile.getVolumeFlex());
		trimmedProfile.setWindowSize(profile.getWindowSize());
		// Don't add cargoes to keep - or clone if needed

		for (final MullSubprofile mullSubprofile : profile.getInventories()) {
			final MullSubprofile trimmedMullSubprofile = ADPFactory.eINSTANCE.createMullSubprofile();
			trimmedMullSubprofile.setInventory(mullSubprofile.getInventory());
			for (final MullEntityRow mullEntityRow : mullSubprofile.getEntityTable()) {
				if (mullEntityRow.getRelativeEntitlement() > 0.0) {
					final MullEntityRow trimmedMullEntityRow = ADPFactory.eINSTANCE.createMullEntityRow();
					trimmedMullEntityRow.setEntity(mullEntityRow.getEntity());
					trimmedMullEntityRow.setInitialAllocation(mullEntityRow.getInitialAllocation());
					trimmedMullEntityRow.setRelativeEntitlement(mullEntityRow.getRelativeEntitlement());
					for (final SalesContractAllocationRow salesContractAllocationRow : mullEntityRow.getSalesContractAllocationRows()) {
						if (salesContractAllocationRow.getWeight() != 0) {
							final SalesContractAllocationRow rowCopy = ADPFactory.eINSTANCE.createSalesContractAllocationRow();
							rowCopy.setContract(salesContractAllocationRow.getContract());
							rowCopy.setWeight(salesContractAllocationRow.getWeight());
							rowCopy.getVessels().addAll(salesContractAllocationRow.getVessels());
							trimmedMullEntityRow.getSalesContractAllocationRows().add(rowCopy);
						}
					}
					for (final DESSalesMarketAllocationRow desSalesMarketAllocationRow : mullEntityRow.getDesSalesMarketAllocationRows()) {
						final DESSalesMarketAllocationRow rowCopy = ADPFactory.eINSTANCE.createDESSalesMarketAllocationRow();
						rowCopy.setDesSalesMarket(desSalesMarketAllocationRow.getDesSalesMarket());
						rowCopy.setWeight(desSalesMarketAllocationRow.getWeight());
						rowCopy.getVessels().addAll(desSalesMarketAllocationRow.getVessels());
						trimmedMullEntityRow.getDesSalesMarketAllocationRows().add(rowCopy);
					}
					trimmedMullSubprofile.getEntityTable().add(trimmedMullEntityRow);
				}
			}
			trimmedProfile.getInventories().add(trimmedMullSubprofile);
		}
		return trimmedProfile;
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
					if (element instanceof Triple) {
						return ((Triple<?, ?, ?>) element).getThird();
					} else if (element instanceof EObject) {
						return ((EObject) element).eContainer();
					}
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof MullProfile) {
						final MullProfile mullProfile = (MullProfile) inputElement;
						return mullProfile.getInventories().toArray();
					}
					return null;
				}

				@Override
				public Object[] getChildren(Object parentElement) {
					if (parentElement instanceof MullSubprofile) {
						final MullSubprofile mullSubprofile = (MullSubprofile) parentElement;
						return mullSubprofile.getEntityTable().stream().sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getEntity(), b.getEntity(), "<Not specified>")).toArray();
					} else if (parentElement instanceof MullEntityRow) {
						final MullEntityRow mullEntityRow = (MullEntityRow) parentElement;
						final List<Object> elements = new ArrayList<>();
						elements.addAll(mullEntityRow.getSalesContractAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getContract(), b.getContract(), "<Not specified>")).collect(Collectors.toList()));
						elements.addAll(mullEntityRow.getDesSalesMarketAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getDesSalesMarket(), b.getDesSalesMarket(), "<Not specified>")).collect(Collectors.toList()));
						return elements.toArray();
					} else if (parentElement instanceof MullAllocationRow) {
						final MullAllocationRow mullAllocationRow = (MullAllocationRow) parentElement;
						final List<Object> elements = new ArrayList<>();
						final List<Vessel> sortedVessels = mullAllocationRow.getVessels().stream().sorted((v1, v2) -> ScenarioElementNameHelper.safeCompareNamedObjects(v1, v2, "<Not specified>"))
								.collect(Collectors.toList());
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
					return (element instanceof Triple && ((Triple<Object, Object, Object>) element).getSecond() instanceof Collection) || element instanceof MullSubprofile
							|| element instanceof MullEntityRow || element instanceof MullAllocationRow;
				}

				@Override
				public Object getParent(Object element) {
					if (element instanceof Triple) {
						return ((Triple<?, ?, ?>) element).getThird();
					} else if (element instanceof EObject) {
						return ((EObject) element).eContainer();
					}
					return null;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof MullProfile) {
						final MullProfile mullProfile = (MullProfile) inputElement;
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
					if (parentElement instanceof Triple) {
						final Triple<Object, Object, Object> triple = (Triple<Object, Object, Object>) parentElement;
						if (!(triple.getSecond() instanceof Collection)) {
							throw new IllegalStateException("Provided triple must have second element as collection");
						}
						final Collection<Object> collectedObjects = (Collection<Object>) triple.getSecond();
						if (triple.getFirst().equals("Vessels")) {
							final Object[] elements = new Object[collectedObjects.size()];
							final Iterator<Object> iter = collectedObjects.iterator();
							for (int i = 0; i < elements.length; ++i) {
								elements[i] = Triple.of("", iter.next(), triple.getThird());
							}
							return elements;
						} else if (triple.getFirst().equals("Cargoes to keep")) {
							final Object[] elements = new Object[collectedObjects.size()];
							final Iterator<Object> iter = collectedObjects.iterator();
							for (int i = 0; i < elements.length; ++i) {
								elements[i] = Triple.of("", iter.next(), triple.getThird());
							}
							return elements;
						} else {
							return collectedObjects.toArray();
						}
					} else if (parentElement instanceof MullSubprofile) {
						final MullSubprofile mullSubprofile = (MullSubprofile) parentElement;
						return mullSubprofile.getEntityTable().stream().sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getEntity(), b.getEntity(), "<Not specified>")).toArray();
					} else if (parentElement instanceof MullEntityRow) {
						final MullEntityRow mullEntityRow = (MullEntityRow) parentElement;
						final List<Object> elements = new ArrayList<>();
						elements.add(Triple.of("Initial allocation", mullEntityRow.getInitialAllocation(), parentElement));
						elements.add(Triple.of("Reference entitlement", mullEntityRow.getRelativeEntitlement(), parentElement));
						elements.addAll(mullEntityRow.getSalesContractAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getContract(), b.getContract(), "<Not specified>")).collect(Collectors.toList()));
						elements.addAll(mullEntityRow.getDesSalesMarketAllocationRows().stream()
								.sorted((a, b) -> ScenarioElementNameHelper.safeCompareNamedObjects(a.getDesSalesMarket(), b.getDesSalesMarket(), "<Not specified>")).collect(Collectors.toList()));
						return elements.toArray();
					} else if (parentElement instanceof MullAllocationRow) {
						final MullAllocationRow mullAllocationRow = (MullAllocationRow) parentElement;
						final List<Object> elements = new ArrayList<>();
						elements.add(Triple.of("AACQ", mullAllocationRow.getWeight(), parentElement));
						final List<Vessel> sortedVessels = mullAllocationRow.getVessels().stream().sorted((v1, v2) -> ScenarioElementNameHelper.safeCompareNamedObjects(v1, v2, "<Not specified>"))
								.collect(Collectors.toList());
						elements.add(Triple.of("Vessels", sortedVessels, parentElement));
						return elements.toArray();
					}
					return null;
				}
			};
		}
	}
}
