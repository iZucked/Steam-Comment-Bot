/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
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
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.TimePeriod;
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
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.ecore.SafeEContentAdapter;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ContractPage extends ADPComposite {

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private EmbeddedDetailComposite detailComposite;
	private ComboViewer objectSelector;
	private Runnable releaseAdaptersRunnable = null;

	private ScenarioTableViewer previewViewer;

	private IActionBars actionBars;

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
			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
			rhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setContent(rhsComposite);

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
			}
		}

	}

	private ScenarioTableViewer constructPreviewViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer localPreviewViewer = new ScenarioTableViewer(previewGroup, SWT.V_SCROLL | SWT.MULTI, editorData);
		localPreviewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference());
		localPreviewViewer.setContentProvider(new ContentProvider());
		GridViewerHelper.configureLookAndFeel(localPreviewViewer);

		localPreviewViewer.setStatusProvider(editorData.getStatusProvider());

		localPreviewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(localPreviewViewer);

		localPreviewViewer.getGrid().setHeaderVisible(true);

		localPreviewViewer.addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getEditingDomain()));
		localPreviewViewer.addTypicalColumn("Port",
				new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), editorData.getReferenceValueProviderCache(), editorData.getEditingDomain()));

		// TODO: Groups
		final GridColumnGroup windowGroup = new GridColumnGroup(localPreviewViewer.getGrid(), SWT.NONE);
		windowGroup.setText("Window");
		GridViewerHelper.configureLookAndFeel(windowGroup);
		localPreviewViewer.addTypicalColumn("Date", windowGroup, new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editorData.getEditingDomain()));
		localPreviewViewer.addTypicalColumn("Time", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editorData.getEditingDomain()));
		localPreviewViewer.addTypicalColumn("Size", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editorData.getEditingDomain()));
		localPreviewViewer.addTypicalColumn("Units", windowGroup,
				new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(), editorData.getEditingDomain(), e -> mapName((TimePeriod) e)));
		localPreviewViewer.addTypicalColumn("Duration", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), editorData.getEditingDomain()));

		// TODO: Groups
		final GridColumnGroup quantityGroup = new GridColumnGroup(localPreviewViewer.getGrid(), SWT.NONE);
		quantityGroup.setText("Quantity");
		GridViewerHelper.configureLookAndFeel(quantityGroup);
		localPreviewViewer.addTypicalColumn("Min", quantityGroup, new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), editorData.getEditingDomain()));
		localPreviewViewer.addTypicalColumn("Max", quantityGroup, new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editorData.getEditingDomain()));
		localPreviewViewer.addTypicalColumn("Units", quantityGroup,
				new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(), editorData.getEditingDomain(), e -> mapName((VolumeUnits) e)));

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
	
	private Command populateModelFromMultipleInventories(@NonNull final EditingDomain editingDomain, @NonNull final LNGScenarioModel sm, final ADPModel adpModel, @NonNull final MullProfile profile) {
		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");
		
		final IModelFactoryRegistry modelFactoryRegistry = Activator.getDefault().getModelFactoryRegistry();
		if (modelFactoryRegistry == null) {
			throw new IllegalStateException("Factory registry must not be null");
		}
		
		final CargoEditingCommands cec = new CargoEditingCommands(editingDomain, sm, ScenarioModelUtil.getCargoModel(sm), ScenarioModelUtil.getCommercialModel(sm), modelFactoryRegistry);
		
		final int loadWindow = profile.getWindowSize();
		final int volumeFlex = profile.getVolumeFlex();
		final int fullCargoLotValue = profile.getFullCargoLotValue();
		// Default value shouldn't be used
		final int cargoVolume = 160_000;
		
		final int loadWindowHours = loadWindow*24;
		
		final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime = new HashMap<>();

		final Set<BaseLegalEntity> firstPartyEntities = ScenarioModelUtil.getCommercialModel(sm).getEntities().stream() //
				.filter(e -> !e.isThirdParty()) //
				.collect(Collectors.toSet());
		
		final LocalDate dayBeforeADPStart = adpModel.getYearStart().atDay(1).minusDays(1);
		final LocalDateTime dateTimeBeforeADPStart = dayBeforeADPStart.atStartOfDay();
		
		final Map<Inventory, MULLContainer> mullContainers = new HashMap<>();
		
		for (MullSubprofile subprofile : profile.getInventories()) {
			final Inventory currentInventory = subprofile.getInventory();
			mullContainers.put(currentInventory, new MULLContainer(subprofile, fullCargoLotValue));

			for (MullEntityRow row : subprofile.getEntityTable()) {
				Stream.concat(
						row.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						row.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream)
				).forEach(vessel -> vesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}
		}
		
		final Set<Vessel> firstPartyVessels = mullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.filter(mudContainer -> firstPartyEntities.contains(mudContainer.getEntity()))
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.flatMap(allocationTracker -> allocationTracker.getVessels().stream()) //
				.collect(Collectors.toSet());
		
		mullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(firstPartyVessels));

		final Set<Vessel> expectedVessels = profile.getInventories().stream() //
				.flatMap(sProfile -> sProfile.getEntityTable().stream() //
						.flatMap(row -> Stream.concat(
								row.getDesSalesMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), 
								row.getSalesContractAllocationRows().stream().flatMap(con -> con.getVessels().stream())
						)) //
				).collect(Collectors.toSet());
		final Map<Vessel, VesselAvailability> vessToVA = sm.getCargoModel().getVesselAvailabilities().stream().filter(va -> expectedVessels.contains(va.getVessel())).collect(Collectors.toMap(VesselAvailability::getVessel, Function.identity()));

		final LocalDate startDate = adpModel.getYearStart().atDay(1);
		
		final Map<Inventory, TreeMap<LocalDateTime, InventoryDateTimeEvent>> inventoryHourlyInsAndOuts = new HashMap<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			inventoryHourlyInsAndOuts.put(inventory, getInventoryInsAndOutsHourly(inventory, sm));
		}
		
		final LocalDateTime startDateTime = startDate.atStartOfDay();
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
		
		Map<Inventory, Set<LocalDate>> inventoryFlaggedDates = new HashMap<>();
		
		final LocalDate dayBeforeStart = startDate.minusDays(1);
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Port inventoryPort = sProfile.getInventory().getPort();
			final Set<LocalDate> flaggedDates = new HashSet<>();
			List<LoadSlot> existingLoads = sm.getCargoModel().getLoadSlots().stream() //
					.filter(s -> s.getPort().equals(inventoryPort) && dayBeforeStart.isBefore(s.getWindowStart())) //
					.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())) //
					.collect(Collectors.toList());
			existingLoads.stream().map(LoadSlot::getWindowStart).forEach(flaggedDates::add);
			inventoryFlaggedDates.put(sProfile.getInventory(), flaggedDates);
		}
		
		final Map<Inventory, LinkedList<CargoBlueprint>> cargoBlueprintsToGenerate = new HashMap<>();
		
		final Map<Inventory, Integer> inventorySlotCounters = new HashMap<>();

		final List<PurchaseContract> pcs = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> newIterSwap = new LinkedList<>();
		
		final Map<Inventory, Integer> inventoryRunningVolume = new HashMap<>();
		final Map<Inventory, PurchaseContract> inventoryPurchaseContracts = new HashMap<>();
		final Map<Inventory, RollingLoadWindow> inventoryRollingWindows = new HashMap<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = inventoryHourlyInsAndOuts.get(inventory).entrySet().iterator();
			newIterSwap.add(Pair.of(inventory, hourlyIter));
			inventoryPurchaseContracts.put(
					sProfile.getInventory(),
					pcs.stream().filter(c -> sProfile.getInventory().getPort().equals(c.getPreferredPort())).findFirst().get()
			);
			inventoryRunningVolume.put(sProfile.getInventory(), 0);
			inventorySlotCounters.put(sProfile.getInventory(), 0);
			cargoBlueprintsToGenerate.put(sProfile.getInventory(), new LinkedList<>());
			inventoryRollingWindows.put(sProfile.getInventory(), new RollingLoadWindow(sProfile.getInventory().getPort().getLoadDuration(), hourlyIter));
		}
		
		while (true) {
			boolean enteredInnerLoop = false;
			for (Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> curr : newIterSwap) {
				final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIter = curr.getSecond();
				if (!currentIter.hasNext()) {
					continue;
				}
				enteredInnerLoop = true;
				
				final Inventory currentInventory = curr.getFirst();
				final Entry<LocalDateTime, InventoryDateTimeEvent> newEndWindowEntry = currentIter.next();
				final RollingLoadWindow currentLoadWindow = inventoryRollingWindows.get(currentInventory);
				final InventoryDateTimeEvent currentEvent = currentLoadWindow.getCurrentEvent();
				final InventoryDateTimeEvent newEndWindowEvent = newEndWindowEntry.getValue();
				
				final MULLContainer currentMULLContainer = mullContainers.get(currentInventory);
				currentMULLContainer.updateRunningAllocation(currentEvent.getNetVolumeIn());
				
				final LocalDateTime currentDateTime = currentLoadWindow.getStartDateTime();
				
				if (currentDateTime.getDayOfMonth() == 1 && currentDateTime.getHour() == 0) {
					final YearMonth currentYM = YearMonth.from(currentDateTime);
					final int monthIn = inventoryHourlyInsAndOuts.get(currentInventory).entrySet().stream() //
							.filter(p -> YearMonth.from(p.getKey()).equals(currentYM)) //
							.mapToInt(p -> p.getValue().getNetVolumeIn()) //
							.sum();
					currentMULLContainer.updateCurrentMonthAbsoluteEntitlement2(monthIn);
				}
				if (!currentLoadWindow.isLoading()) {
					final MUDContainer mullMUDContainer = currentMULLContainer.calculateMULL(vesselToMostRecentUseDateTime, cargoVolume);
					final AllocationTracker mudAllocationTracker = mullMUDContainer.calculateMUDAllocationTracker();
					final List<Vessel> mudVesselRestrictions = mudAllocationTracker.getVessels();
					final int currentAllocationDrop;
					final Vessel assignedVessel;
					if (!mudVesselRestrictions.isEmpty()) {
						assignedVessel = mudVesselRestrictions.stream().min((v1, v2) -> vesselToMostRecentUseDateTime.get(v1).compareTo(vesselToMostRecentUseDateTime.get(v2))).get();
						currentAllocationDrop = mudAllocationTracker.calculateExpectedAllocationDrop(assignedVessel, currentInventory.getPort().getLoadDuration());
					} else {
						assignedVessel = null;
						currentAllocationDrop = cargoVolume;
					}
					if (currentLoadWindow.canLift(currentAllocationDrop)) {
						currentLoadWindow.startLoad(currentAllocationDrop);
						mullMUDContainer.dropAllocation(currentAllocationDrop);
						mudAllocationTracker.dropAllocation(currentAllocationDrop);
						
						final int nextLoadCount = inventorySlotCounters.get(currentInventory);
						final CargoBlueprint currentCargoBlueprint = new CargoBlueprint(currentInventory, inventoryPurchaseContracts.get(currentInventory), nextLoadCount, assignedVessel, currentLoadWindow.getStartDateTime(), loadWindowHours, mudAllocationTracker, currentAllocationDrop, mullMUDContainer.getEntity());
						if (!cargoBlueprintsToGenerate.get(currentInventory).isEmpty()) {
							final CargoBlueprint previousCargoBlueprint = cargoBlueprintsToGenerate.get(currentInventory).getLast();
							final LocalDateTime earliestPreviousStart = currentLoadWindow.getStartDateTime().minusHours(currentInventory.getPort().getLoadDuration());
							final int newWindowHours = Hours.between(previousCargoBlueprint.getWindowStart(), earliestPreviousStart);
							previousCargoBlueprint.updateWindowSize(newWindowHours);
						}
						cargoBlueprintsToGenerate.get(currentInventory).add(currentCargoBlueprint);
						
						inventorySlotCounters.put(currentInventory, nextLoadCount+1);
						if (assignedVessel != null) {
							vesselToMostRecentUseDateTime.put(assignedVessel, currentCargoBlueprint.getWindowStart());
						}
					}
				}
				currentLoadWindow.stepForward(newEndWindowEvent);
			}
			if (!enteredInnerLoop) {
				break;
			}
		}

		if (cargoBlueprintsToGenerate.values().stream().anyMatch(c -> !c.isEmpty())) {
			final IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
			cargoBlueprintsToGenerate.values().stream() //
					.flatMap(List::stream) //
					.forEach(cargoBlueprint -> cargoBlueprint.constructCargoModelPopulationCommands(cargoModel, cec, editingDomain, volumeFlex, sdp, vessToVA, cmd));
			editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
		} else if (cmd.isEmpty()) {
			return IdentityCommand.INSTANCE;
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
			previewGroup.layout();
			rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		final List<Object> objects = new LinkedList<>();
		if (scenarioModel != null && adpModel != null) {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
			commercialModel.eAdapters().add(commercialModelAdapter);

			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			cargoModel.eAdapters().add(cargoModelAdapter);

			releaseAdaptersRunnable = () -> {
				commercialModel.eAdapters().remove(commercialModelAdapter);
				cargoModel.eAdapters().remove(cargoModelAdapter);
			};
			
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
				MullProfile profile = adpModel.getMullProfile();
				if (profile == null) {
					profile = ADPFactory.eINSTANCE.createMullProfile();
				}
				objects.add(profile);
			}
			objects.addAll(commercialModel.getPurchaseContracts());
			objects.addAll(commercialModel.getSalesContracts());
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
				} else if (target instanceof SalesContractProfile) {
					final SalesContractProfile salesContractProfile = (SalesContractProfile) target;
					final List<Object> o = new LinkedList<>();
					for (DischargeSlot s : editorData.getScenarioModel().getCargoModel().getDischargeSlots()) {
						if (s.getContract() == salesContractProfile.getContract()) {
							o.add(s);
						}
					}
					previewViewer.setInput(o);
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
	
	private TreeMap<LocalDateTime, InventoryDateTimeEvent> getInventoryInsAndOutsHourly(final Inventory inventory, final LNGScenarioModel scenarioModel) {
		final List<InventoryCapacityRow> capacities = inventory.getCapacities();
		final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap = capacities.stream() //
				.collect(Collectors.toMap(
						InventoryCapacityRow::getDate,
						c -> c,
						(oldVal, newVal) -> newVal,
						TreeMap::new));
		final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts = new TreeMap<>();
		addHourlyNetVolumes(inventory.getFeeds(), capacityTreeMap, insAndOuts, IntUnaryOperator.identity());
		addHourlyNetVolumes(inventory.getOfftakes(), capacityTreeMap, insAndOuts, a -> -a);
		
		final Port inventoryPort = inventory.getPort();
		if (inventoryPort != null) {
			final int loadDuration = inventoryPort.getLoadDuration();
			if (loadDuration > 0) {
				scenarioModel.getCargoModel().getLoadSlots().stream() //
				.filter(loadSlot -> loadSlot.getPort().equals(inventoryPort))
				.forEach(loadSlot -> {
					final int expectedLiftVolume = loadSlot.getMaxQuantity();
					final int currentDuration = loadSlot.isSetDuration() ? loadSlot.getDuration() : loadDuration;
					final int hourlyLift = expectedLiftVolume / currentDuration;
					final int firstLift = hourlyLift + expectedLiftVolume % currentDuration;
					final LocalDateTime loadStartDateTime = LocalDateTime.of(
							loadSlot.getWindowStart(), 
							loadSlot.isSetWindowStartTime() ? LocalTime.of(loadSlot.getWindowStartTime(), 0) : LocalTime.of(0, 0)
					);
					addSingleVolume(capacityTreeMap, insAndOuts, loadStartDateTime, -firstLift);
					final LocalDateTime endLoad = loadStartDateTime.plusHours(currentDuration);
					for (LocalDateTime dateTimeCounter = loadStartDateTime.plusHours(1L); dateTimeCounter.isBefore(endLoad); dateTimeCounter = loadStartDateTime.plusHours(1L)) {
						addSingleVolume(capacityTreeMap, insAndOuts, dateTimeCounter, -hourlyLift);
					}
				});
			}
		}
		return insAndOuts;
	}

	private void addHourlyNetVolumes(final List<InventoryEventRow> events, final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final IntUnaryOperator volumeFunction) {
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
					for (int hour = 1; hour < 24; ++ hour) {
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

	private void addSingleVolume(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final IntUnaryOperator volumeFunction, final LocalDateTime expectedDateTime, final int absoluteVolume) {
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

	private void addSingleVolume(final TreeMap<LocalDate, InventoryCapacityRow> capacityTreeMap, final TreeMap<LocalDateTime, InventoryDateTimeEvent> insAndOuts, final LocalDateTime expectedDateTime, final int volume) {
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
}
