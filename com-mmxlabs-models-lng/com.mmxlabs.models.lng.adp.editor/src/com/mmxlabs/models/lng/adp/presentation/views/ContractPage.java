/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.mull.AllocationTracker;
import com.mmxlabs.models.lng.adp.mull.CargoBlueprint;
import com.mmxlabs.models.lng.adp.mull.DESMarketTracker;
import com.mmxlabs.models.lng.adp.mull.InventoryDateTimeEvent;
import com.mmxlabs.models.lng.adp.mull.MUDContainer;
import com.mmxlabs.models.lng.adp.mull.MULLContainer;
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
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
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

		localPreviewViewer.addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getDefaultCommandHandler() ));
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

		final LocalDate startDate = adpModel.getYearStart().atDay(1);
		final LocalDateTime startDateTime = startDate.atStartOfDay();
		final LocalDateTime endDateTimeExclusive = adpModel.getYearEnd().atDay(1).atStartOfDay();

		final boolean debugFlex = false;

		final int loadWindowHours = loadWindow * 24;

		final Map<Vessel, LocalDateTime> vesselToMostRecentUseDateTime = new HashMap<>();
		final Map<Vessel, LocalDateTime> newVesselToMostRecentUseDateTime = new HashMap<>();

		final Set<BaseLegalEntity> firstPartyEntities = ScenarioModelUtil.getCommercialModel(sm).getEntities().stream() //
				.filter(e -> !e.isThirdParty()) //
				.collect(Collectors.toSet());

		final LocalDate dayBeforeADPStart = adpModel.getYearStart().atDay(1).minusDays(1);
		final LocalDateTime dateTimeBeforeADPStart = dayBeforeADPStart.atStartOfDay();

		final Map<Inventory, MULLContainer> mullContainers = new HashMap<>();
		final Map<Inventory, Integer> yearlyProductions = new HashMap<>();

		for (MullSubprofile subprofile : profile.getInventories()) {
			final Inventory currentInventory = subprofile.getInventory();
			mullContainers.put(currentInventory, new MULLContainer(subprofile, fullCargoLotValue));

			for (MullEntityRow row : subprofile.getEntityTable()) {
				Stream.concat(row.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						row.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream))
						.forEach(vessel -> vesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}
			yearlyProductions.put(currentInventory, getYearlyProduction(currentInventory, sm, startDateTime, endDateTimeExclusive));
		}

		final Set<Vessel> firstPartyVessels = mullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.filter(mudContainer -> firstPartyEntities.contains(mudContainer.getEntity())).flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.flatMap(allocationTracker -> allocationTracker.getVessels().stream()) //
				.collect(Collectors.toSet());

		mullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(firstPartyVessels));

		final MullProfile newProfile = ADPFactory.eINSTANCE.createMullProfile();
		newProfile.setFullCargoLotValue(profile.getFullCargoLotValue());
		newProfile.setVolumeFlex(profile.getVolumeFlex());
		newProfile.setWindowSize(profile.getWindowSize());

		final Map<Inventory, Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>>> rearrangedProfiles = new HashMap<>();

		for (final MULLContainer mullContainer : mullContainers.values()) {
			// Expecting one first party MUD container - unknown behaviour for more than one
			// or none first party
			final Optional<MUDContainer> optMudContainer = mullContainer.getMUDContainers().stream().filter(m -> firstPartyEntities.contains(m.getEntity())).findAny();
			if (optMudContainer.isEmpty()) {
				throw new IllegalStateException("MULL profile should contain a first party entry.");
			}
			final MUDContainer firstPartyMudContainer = optMudContainer.get();
			final List<Triple<MUDContainer, List<Pair<AllocationTracker, AllocationTracker>>, List<AllocationTracker>>> l = new LinkedList<>();
			for (final MUDContainer currentMUDContainer : mullContainer.getMUDContainers()) {
				if (currentMUDContainer.getEntity().isThirdParty()) {
					final List<Pair<AllocationTracker, AllocationTracker>> allocationTrackersToCombine = new LinkedList<>();
					final List<AllocationTracker> allocationTrackersToRetain = new LinkedList<>();
					for (final AllocationTracker allocationTracker : currentMUDContainer.getAllocationTrackers()) {
						if (allocationTracker.isSharingVessels()) {
							final Optional<AllocationTracker> optAllocationTracker = firstPartyMudContainer.getAllocationTrackers().stream() //
									.filter(a -> {
										final HashSet<Vessel> firstVessSet = new HashSet<>(a.getVessels());
										return allocationTracker.getVessels().stream().allMatch(firstVessSet::contains);
									}).findAny();
							if (optAllocationTracker.isEmpty()) {
								throw new IllegalStateException("Shared vessels should be entirely shared.");
							}
							allocationTrackersToCombine.add(Pair.of(allocationTracker, optAllocationTracker.get()));
						} else {
							allocationTrackersToRetain.add(allocationTracker);
						}
					}
					l.add(Triple.of(currentMUDContainer, allocationTrackersToCombine, allocationTrackersToRetain));
				}
			}

			// Map of <entity, allocation tracker pair> to List of <entity, allocation
			// tracker> that it combines with (using first party as sink)
			final Map<Pair<MUDContainer, AllocationTracker>, List<Pair<MUDContainer, AllocationTracker>>> newCombinations = new HashMap<>();
			firstPartyMudContainer.getAllocationTrackers().stream().map(a -> Pair.of(firstPartyMudContainer, a)).forEach(p -> newCombinations.put(p, new LinkedList<>(Collections.singleton(p))));
			for (final MUDContainer currentMUDContainer : mullContainer.getMUDContainers()) {
				if (currentMUDContainer.getEntity().isThirdParty()) {
					for (final AllocationTracker allocationTracker : currentMUDContainer.getAllocationTrackers()) {
						if (allocationTracker.isSharingVessels()) {
							final Optional<AllocationTracker> optAllocationTracker = firstPartyMudContainer.getAllocationTrackers().stream() //
									.filter(a -> {
										final HashSet<Vessel> firstVessSet = new HashSet<>(a.getVessels());
										return allocationTracker.getVessels().stream().allMatch(firstVessSet::contains);
									}).findAny();
							if (optAllocationTracker.isEmpty()) {
								throw new IllegalStateException("Shared vessels should be entirely shared.");
							}
							newCombinations.get(Pair.of(firstPartyMudContainer, optAllocationTracker.get())).add(Pair.of(currentMUDContainer, allocationTracker));
						} else {
							final Pair<MUDContainer, AllocationTracker> p = Pair.of(currentMUDContainer, allocationTracker);
							newCombinations.put(p, new LinkedList<>(Collections.singleton(p)));
						}
					}
				}
			}

			// Maps to tie allocation tracker and entitlements to entities
			final Map<MUDContainer, Set<AllocationTracker>> rearrangedContainment = new HashMap<>();
			final Map<MUDContainer, Double> runningRelativeEntitlement = new HashMap<>();
			final Map<MUDContainer, Long> runningInitialAllocation = new HashMap<>();
			for (final MUDContainer currentMUDContainer : mullContainer.getMUDContainers()) {
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
			for (final MUDContainer currentMUDContainer : mullContainer.getMUDContainers()) {
				rearrangedProfile.put(currentMUDContainer, Pair.of(new LinkedList<>(), new HashMap<>()));
			}

			// Populate the rearranged profile
			nonMovingElements.forEach(p -> rearrangedProfile.get(p.getFirst()).getFirst().add(p.getSecond()));
			for (final Entry<Pair<MUDContainer, AllocationTracker>, List<Pair<MUDContainer, AllocationTracker>>> entry : movingElements.entrySet()) {
				final Pair<MUDContainer, AllocationTracker> minorLifter = entry.getValue().stream()
						.min((p1, p2) -> Double.compare(p1.getFirst().getRelativeEntitlement(), p2.getFirst().getRelativeEntitlement())).get();
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
									.mapToInt(v -> allocToAbsorb.calculateExpectedAllocationDrop(v, mullContainer.getInventory().getPort().getLoadDuration())) //
									.sum() / allocToAbsorb.getVessels().size();
							final int totalProd = yearlyProductions.get(mullContainer.getInventory());
							final double allocatedProduction = totalProd * lifterToAbsorb.getFirst().getRelativeEntitlement();
							final int aacqLiftedVolume = allocToAbsorb.getAACQ() * averageVolumeLifted;
							final double localPercentage = aacqLiftedVolume / allocatedProduction;
							final double reShift = mudToAbsorb.getRelativeEntitlement() * localPercentage;
							final long initialAllocationShift = (long) (mudToAbsorb.getInitialAllocation() * localPercentage);
							runningRelativeEntitlement.put(minorLifter.getFirst(), runningRelativeEntitlement.get(minorLifter.getFirst()) + reShift);
							runningRelativeEntitlement.put(mudToAbsorb, runningRelativeEntitlement.get(mudToAbsorb) - reShift);
							runningInitialAllocation.put(minorLifter.getFirst(), runningInitialAllocation.get(minorLifter.getFirst()) + initialAllocationShift);
							runningInitialAllocation.put(mudToAbsorb, runningInitialAllocation.put(mudToAbsorb, runningInitialAllocation.get(mudToAbsorb) - initialAllocationShift));
						}
					}
					rearrangedProfile.get(minorLifter.getFirst()).getSecond().put(minorLifter.getSecond(), sharedCombinations);
				}
			}

			rearrangedProfiles.put(mullContainer.getInventory(), rearrangedProfile);

			final double sumNewRelativeEntitlements = runningRelativeEntitlement.values().stream().mapToDouble(v -> v).sum();
			assert sumNewRelativeEntitlements == 1;

			final long sumInitialAllocationsBefore = mullContainer.getMUDContainers().stream().mapToLong(MUDContainer::getInitialAllocation).sum();
			final long sumInitialAllocationsAfter = runningInitialAllocation.values().stream().mapToLong(v -> v).sum();
			assert sumInitialAllocationsBefore == sumInitialAllocationsAfter;

			// Convert rearranged profile to EMF MULL Subprofile (to use existing MULL
			// objects)
			final MullSubprofile newSubprofile = ADPFactory.eINSTANCE.createMullSubprofile();
			newSubprofile.setInventory(mullContainer.getInventory());
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
					newSubprofile.getEntityTable().add(tempRow);
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

			final int aacqSumBefore = mullContainer.getMUDContainers().stream() //
					.mapToInt(mc -> mc.getAllocationTrackers().stream().mapToInt(AllocationTracker::getAACQ).sum()) //
					.sum();
			final int aacqSumAfter = newSubprofile.getEntityTable().stream() //
					.mapToInt(row -> row.getSalesContractAllocationRows().stream().mapToInt(MullAllocationRow::getWeight).sum()
							+ row.getDesSalesMarketAllocationRows().stream().mapToInt(MullAllocationRow::getWeight).sum()) //
					.sum();
			assert aacqSumBefore == aacqSumAfter;
			newProfile.getInventories().add(newSubprofile);
		}

		final Map<Inventory, MULLContainer> newMullContainers = new HashMap<>();
		for (MullSubprofile subprofile : newProfile.getInventories()) {
			final Inventory currentInventory = subprofile.getInventory();
			newMullContainers.put(currentInventory, new MULLContainer(subprofile, fullCargoLotValue));

			for (MullEntityRow row : subprofile.getEntityTable()) {
				Stream.concat(row.getSalesContractAllocationRows().stream().map(SalesContractAllocationRow::getVessels).flatMap(List::stream),
						row.getDesSalesMarketAllocationRows().stream().map(DESSalesMarketAllocationRow::getVessels).flatMap(List::stream))
						.forEach(vessel -> newVesselToMostRecentUseDateTime.put(vessel, dateTimeBeforeADPStart));
			}
		}

		final Set<Vessel> newFirstPartyVessels = mullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.filter(mudContainer -> firstPartyEntities.contains(mudContainer.getEntity())) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.flatMap(allocationTracker -> allocationTracker.getVessels().stream()) //
				.collect(Collectors.toSet());

		newMullContainers.entrySet().stream() //
				.flatMap(e -> e.getValue().getMUDContainers().stream()) //
				.flatMap(mudContainer -> mudContainer.getAllocationTrackers().stream()) //
				.forEach(allocationTracker -> allocationTracker.setVesselSharing(firstPartyVessels));

		// Map <entity, contract/market> to rearranged profile allocation trackers
		final Map<Inventory, Map<Pair<BaseLegalEntity, SalesContract>, AllocationTracker>> salesContractMap = new HashMap<>();
		final Map<Inventory, Map<Pair<BaseLegalEntity, DESSalesMarket>, AllocationTracker>> desMarketMap = new HashMap<>();
		for (MULLContainer mullContainer : newMullContainers.values()) {
			final Map<Pair<BaseLegalEntity, SalesContract>, AllocationTracker> currentSalesContractMap = new HashMap<>();
			final Map<Pair<BaseLegalEntity, DESSalesMarket>, AllocationTracker> currentDESMarketMap = new HashMap<>();
			salesContractMap.put(mullContainer.getInventory(), currentSalesContractMap);
			desMarketMap.put(mullContainer.getInventory(), currentDESMarketMap);
			for (MUDContainer mudContainer : mullContainer.getMUDContainers()) {
				for (AllocationTracker allocationTracker : mudContainer.getAllocationTrackers()) {
					if (allocationTracker instanceof SalesContractTracker) {
						currentSalesContractMap.put(Pair.of(mudContainer.getEntity(), ((SalesContractTracker) allocationTracker).getSalesContract()), allocationTracker);
					} else if (allocationTracker instanceof DESMarketTracker) {
						currentDESMarketMap.put(Pair.of(mudContainer.getEntity(), ((DESMarketTracker) allocationTracker).getDESSalesMarket()), allocationTracker);
					}
				}
			}
		}
		final Map<Inventory, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>> newAllocationTrackerToCombinedMapMap = new HashMap<>();
		for (Entry<Inventory, Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>>> entry : rearrangedProfiles.entrySet()) {
			final Inventory inventory = entry.getKey();
			final Map<Pair<BaseLegalEntity, SalesContract>, AllocationTracker> currentSalesContractMap = salesContractMap.get(inventory);
			final Map<Pair<BaseLegalEntity, DESSalesMarket>, AllocationTracker> currentDESContractMap = desMarketMap.get(inventory);
			final Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> currentCombinedMap = new HashMap<>();
			newAllocationTrackerToCombinedMapMap.put(inventory, currentCombinedMap);
			final Map<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>> currentRearrangement = entry.getValue();
			for (final Entry<MUDContainer, Pair<List<AllocationTracker>, Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>>>> remappingEntry : currentRearrangement.entrySet()) {
				final BaseLegalEntity currentRemappingEntity = remappingEntry.getKey().getEntity();
				final List<AllocationTracker> directMappings = remappingEntry.getValue().getFirst();
				for (final AllocationTracker allocationTracker : directMappings) {
					if (allocationTracker instanceof SalesContractTracker) {
						final SalesContract salesContract = ((SalesContractTracker) allocationTracker).getSalesContract();
						final AllocationTracker replacementTracker = currentSalesContractMap.get(Pair.of(currentRemappingEntity, salesContract));
						currentCombinedMap.put(replacementTracker, new LinkedList<>(Collections.singleton(Pair.of(remappingEntry.getKey(), allocationTracker))));
					} else if (allocationTracker instanceof DESMarketTracker) {
						final DESSalesMarket desSalesMarket = ((DESMarketTracker) allocationTracker).getDESSalesMarket();
						final AllocationTracker replacementTracker = currentDESContractMap.get(Pair.of(currentRemappingEntity, desSalesMarket));
						currentCombinedMap.put(replacementTracker, new LinkedList<>(Collections.singleton(Pair.of(remappingEntry.getKey(), allocationTracker))));
					}
				}
				final Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> combinedMappings = remappingEntry.getValue().getSecond();
				for (final Entry<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> entry2 : combinedMappings.entrySet()) {
					final AllocationTracker allocationTracker = entry2.getKey();
					assert !entry2.getValue().isEmpty();
					final List<Pair<MUDContainer, AllocationTracker>> outputList = new LinkedList<>(entry2.getValue());
					outputList.add(Pair.of(remappingEntry.getKey(), allocationTracker));
					if (allocationTracker instanceof SalesContractTracker) {
						final SalesContract salesContract = ((SalesContractTracker) allocationTracker).getSalesContract();
						final AllocationTracker replacementTracker = currentSalesContractMap.get(Pair.of(currentRemappingEntity, salesContract));
						// remapping shares vessels with first party - following needed for correct
						// volume calculation
//						replacementTracker.setVesselSharing(true);
						currentCombinedMap.put(replacementTracker, outputList);
					} else if (allocationTracker instanceof DESMarketTracker) {
						final DESSalesMarket desSalesMarket = ((DESMarketTracker) allocationTracker).getDESSalesMarket();
						final AllocationTracker replacementTracker = currentDESContractMap.get(Pair.of(currentRemappingEntity, desSalesMarket));
						// remapping shares vessels with first party - following needed for correct
						// volume calculation
//						replacementTracker.setVesselSharing(true);
						currentCombinedMap.put(replacementTracker, outputList);
					}
				}
			}
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

		for (final MullSubprofile sProfile : newProfile.getInventories()) {
			int totalInventoryVolume = 0;
			final Inventory currentInventory = sProfile.getInventory();
			final TreeMap<LocalDateTime, InventoryDateTimeEvent> currentInsAndOuts = newInventoryHourlyInsAndOuts.get(currentInventory);
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

		Map<Inventory, Set<LocalDate>> newInventoryFlaggedDates = new HashMap<>();

		for (final MullSubprofile sProfile : newProfile.getInventories()) {
			final Port inventoryPort = sProfile.getInventory().getPort();
			final Set<LocalDate> flaggedDates = new HashSet<>();
			List<LoadSlot> existingLoads = sm.getCargoModel().getLoadSlots().stream() //
					.filter(s -> s.getPort().equals(inventoryPort) && dayBeforeStart.isBefore(s.getWindowStart())) //
					.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())) //
					.collect(Collectors.toList());
			existingLoads.stream().map(LoadSlot::getWindowStart).forEach(flaggedDates::add);
			newInventoryFlaggedDates.put(sProfile.getInventory(), flaggedDates);
		}

		final Map<Inventory, LinkedList<CargoBlueprint>> cargoBlueprintsToGenerate = new HashMap<>();
		final Map<Inventory, Integer> inventorySlotCounters = new HashMap<>();

		final Map<Inventory, LinkedList<CargoBlueprint>> newCargoBlueprintsToGenerate = new HashMap<>();
		final Map<Inventory, Integer> newInventorySlotCounters = new HashMap<>();

		final List<PurchaseContract> pcs = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> newIterSwap = new LinkedList<>();

		final List<PurchaseContract> newPcs = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts();
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> newNewIterSwap = new LinkedList<>();

		final Map<Inventory, Integer> inventoryRunningVolume = new HashMap<>();
		final Map<Inventory, PurchaseContract> inventoryPurchaseContracts = new HashMap<>();
		final Map<Inventory, RollingLoadWindow> inventoryRollingWindows = new HashMap<>();
		final Map<Inventory, Iterator<Entry<LocalDateTime, Cargo>>> inventoryExistingCargoes = new HashMap<>();
		final Map<Inventory, Set<String>> inventoryKnownLoadIDs = new HashMap<>();
		final Map<Inventory, Entry<LocalDateTime, Cargo>> nextFixedCargoes = new HashMap<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = inventoryHourlyInsAndOuts.get(inventory).entrySet().iterator();
			newIterSwap.add(Pair.of(inventory, hourlyIter));

			inventoryPurchaseContracts.put(sProfile.getInventory(), pcs.stream().filter(c -> sProfile.getInventory().getPort().equals(c.getPreferredPort())).findFirst().get());
			inventoryRunningVolume.put(sProfile.getInventory(), 0);
			inventorySlotCounters.put(sProfile.getInventory(), 0);
			cargoBlueprintsToGenerate.put(sProfile.getInventory(), new LinkedList<>());

			final Port inventoryPort = inventory.getPort();
			final TreeMap<LocalDateTime, Cargo> existingCargoes = new TreeMap<>();
			final Set<String> knownLoadIDs = new HashSet<>();
			inventoryKnownLoadIDs.put(inventory, knownLoadIDs);
			sm.getCargoModel().getCargoes().stream() //
					.filter(c -> c.getSlots().get(0).getPort().equals(inventoryPort)) //
					.forEach(c -> {
						Slot<?> loadSlot = c.getSlots().get(0);
						final LocalDateTime loadStart = loadSlot.isSetWindowStartTime() ? loadSlot.getWindowStart().atTime(LocalTime.of(loadSlot.getWindowStartTime(), 0))
								: loadSlot.getWindowStart().atStartOfDay();
						knownLoadIDs.add(loadSlot.getName());
						existingCargoes.put(loadStart, c);
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
							vesselToMostRecentUseDateTime.put(vessel, currentFirst);
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
		final Map<Inventory, Iterator<Entry<LocalDateTime, Cargo>>> newInventoryExistingCargoes = new HashMap<>();
		final Map<Inventory, Set<String>> newInventoryKnownLoadIDs = new HashMap<>();
		final Map<Inventory, Entry<LocalDateTime, Cargo>> newNextFixedCargoes = new HashMap<>();
		for (final MullSubprofile sProfile : newProfile.getInventories()) {
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
			sm.getCargoModel().getCargoes().stream() //
					.filter(c -> c.getSlots().get(0).getPort().equals(inventoryPort)) //
					.forEach(c -> {
						Slot<?> loadSlot = c.getSlots().get(0);
						final LocalDateTime loadStart = loadSlot.isSetWindowStartTime() ? loadSlot.getWindowStart().atTime(LocalTime.of(loadSlot.getWindowStartTime(), 0))
								: loadSlot.getWindowStart().atStartOfDay();
						knownLoadIDs.add(loadSlot.getName());
						existingCargoes.put(loadStart, c);
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
							newVesselToMostRecentUseDateTime.put(vessel, currentFirst);
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
		}

		final CharterInMarket adpNominalMarket = adpModel.getFleetProfile().getDefaultNominalMarket();
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
				final MULLContainer currentMULLContainer = newMullContainers.get(currentInventory);
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
				}
				final Entry<LocalDateTime, Cargo> nextFixedCargo = newNextFixedCargoes.get(currentInventory);
				if (nextFixedCargo != null && nextFixedCargo.getKey().equals(currentDateTime)) {
					final LinkedList<CargoBlueprint> currentCargoBlueprintsToGenerate = newCargoBlueprintsToGenerate.get(currentInventory);
					// Start fixed cargo load on rolling window return any undos on generated
					// cargoes
					final List<CargoBlueprint> cargoBlueprintsToUndo = currentLoadWindow.startFixedLoad(nextFixedCargo.getValue(), currentCargoBlueprintsToGenerate);
					currentMULLContainer.dropFixedLoad(nextFixedCargo.getValue());
					final Set<Vessel> vesselsToUndo = new HashSet<>();
					for (final CargoBlueprint cargoBlueprint : cargoBlueprintsToUndo) {
						currentMULLContainer.undo(cargoBlueprint);
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
							newVesselToMostRecentUseDateTime.put(assignedVessel, cargoBlueprint.getWindowStart());
							vesselsToUndo.remove(assignedVessel);
						}
					}
					if (currentCargoBlueprintsToGenerate.isEmpty()) {
						newInventorySlotCounters.put(currentInventory, 0);
					} else {
						newInventorySlotCounters.put(currentInventory, currentCargoBlueprintsToGenerate.getLast().getLoadCounter() + 1);
					}
					for (final Vessel vesselToUndo : vesselsToUndo) {
						newVesselToMostRecentUseDateTime.put(vesselToUndo, dateTimeBeforeADPStart);
					}
					final Iterator<Entry<LocalDateTime, Cargo>> nextFixedCargoesIter = newInventoryExistingCargoes.get(currentInventory);
					if (nextFixedCargoesIter.hasNext()) {
						nextFixedCargoes.put(currentInventory, nextFixedCargoesIter.next());
					} else {
						nextFixedCargoes.remove(currentInventory);
					}
				}
				if (!currentLoadWindow.isLoading()) {
					final MUDContainer mullMUDContainer = currentMULLContainer.calculateMULL(newVesselToMostRecentUseDateTime, cargoVolume);
					final AllocationTracker mudAllocationTracker = mullMUDContainer.calculateMUDAllocationTracker();
					final List<Vessel> mudVesselRestrictions = mudAllocationTracker.getVessels();
					final int currentAllocationDrop;
					final Vessel assignedVessel;
					if (!mudVesselRestrictions.isEmpty()) {
						assignedVessel = mudVesselRestrictions.stream().min((v1, v2) -> newVesselToMostRecentUseDateTime.get(v1).compareTo(newVesselToMostRecentUseDateTime.get(v2))).get();
						currentAllocationDrop = mudAllocationTracker.calculateExpectedAllocationDrop(assignedVessel, currentInventory.getPort().getLoadDuration());
					} else {
						assignedVessel = null;
						currentAllocationDrop = cargoVolume;
					}
					if (currentLoadWindow.canLift(currentAllocationDrop)) {
						final Set<String> currentKnownLoadIDs = newInventoryKnownLoadIDs.get(currentInventory);
						currentLoadWindow.startLoad(currentAllocationDrop);
						mullMUDContainer.dropAllocation(currentAllocationDrop);
						mudAllocationTracker.dropAllocation(currentAllocationDrop);

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
							newVesselToMostRecentUseDateTime.put(assignedVessel, currentCargoBlueprint.getWindowStart());
						}
					}
				}
				currentLoadWindow.stepForward(newEndWindowEvent);
			}
		}

		// Do second pass on cargoes to generate
		final List<Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>>> secondPassIterSwap = new LinkedList<>();
		for (final MullSubprofile sProfile : profile.getInventories()) {
			final Inventory inventory = sProfile.getInventory();
			final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> hourlyIter = inventoryHourlyInsAndOuts.get(inventory).entrySet().iterator();
			secondPassIterSwap.add(Pair.of(inventory, hourlyIter));
		}
		final Map<Inventory, CargoBlueprint[]> secondPassCargoBlueprints = new HashMap<>();
		final Map<Inventory, Integer> secondPassCargoBlueprintIndex = new HashMap<>();
		final Map<Inventory, CargoBlueprint> secondPassNextCargoBlueprints = new HashMap<>();
		final Map<Inventory, Iterator<CargoBlueprint>> secondPassCargoBlueprintIters = new HashMap<>();
		for (final Entry<Inventory, LinkedList<CargoBlueprint>> entry : newCargoBlueprintsToGenerate.entrySet()) {
			secondPassCargoBlueprints.put(entry.getKey(), new CargoBlueprint[entry.getValue().size()]);
			secondPassCargoBlueprintIndex.put(entry.getKey(), 0);
			final Iterator<CargoBlueprint> cargoBlueprintIter = entry.getValue().iterator();
			secondPassNextCargoBlueprints.put(entry.getKey(), cargoBlueprintIter.hasNext() ? cargoBlueprintIter.next() : null);
			secondPassCargoBlueprintIters.put(entry.getKey(), cargoBlueprintIter);
		}
		newAllocationTrackerToCombinedMapMap.size();
		for (LocalDateTime currentDateTime2 = startDateTime; currentDateTime2.isBefore(endDateTimeExclusive); currentDateTime2 = currentDateTime2.plusHours(1)) {
			for (Pair<Inventory, Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>>> curr : secondPassIterSwap) {
				final Inventory currentInventory = curr.getFirst();
				final Iterator<Entry<LocalDateTime, InventoryDateTimeEvent>> currentIter = curr.getSecond();
				final Entry<LocalDateTime, InventoryDateTimeEvent> currentEntry = currentIter.next();
				final InventoryDateTimeEvent currentEvent = currentEntry.getValue();

				final MULLContainer currentMULLContainer = mullContainers.get(currentInventory);
				currentMULLContainer.updateRunningAllocation(currentEvent.getNetVolumeIn());

				assert currentDateTime2.equals(currentEntry.getKey());
				if (isAtStartHourOfMonth(currentDateTime2)) {
					final YearMonth currentYearMonth = YearMonth.from(currentDateTime2);
					final int monthIn = newInventoryHourlyInsAndOuts.get(currentInventory).entrySet().stream() //
							.filter(p -> YearMonth.from(p.getKey()).equals(currentYearMonth)) //
							.mapToInt(p -> p.getValue().getNetVolumeIn()) //
							.sum();
					currentMULLContainer.updateCurrentMonthAbsoluteEntitlement(monthIn);
				}

				final CargoBlueprint currentCargoBlueprint = secondPassNextCargoBlueprints.get(currentInventory);
				if (currentCargoBlueprint != null && currentCargoBlueprint.getWindowStart().equals(currentDateTime2)) {
					final Iterator<CargoBlueprint> currentCargoBlueprintIter = secondPassCargoBlueprintIters.get(currentInventory);
					if (currentCargoBlueprintIter.hasNext()) {
						secondPassNextCargoBlueprints.put(currentInventory, currentCargoBlueprintIter.next());
					} else {
						secondPassNextCargoBlueprints.put(currentInventory, null);
					}

					final Map<AllocationTracker, List<Pair<MUDContainer, AllocationTracker>>> currentAllocationTrackerToCombinedMap = newAllocationTrackerToCombinedMapMap.get(currentInventory);
					final AllocationTracker allocationTrackerToReplace = currentCargoBlueprint.getAllocationTracker();
					final List<Pair<MUDContainer, AllocationTracker>> combinedTrackers = currentAllocationTrackerToCombinedMap.get(allocationTrackerToReplace);
					if (combinedTrackers.size() == 1) {
						final Pair<MUDContainer, AllocationTracker> replacementPair = combinedTrackers.get(0);

						final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(), replacementPair.getFirst().getEntity());

						final CargoBlueprint[] newCargoBlueprints = secondPassCargoBlueprints.get(currentInventory);
						final int currentCargoBlueprintIndex = secondPassCargoBlueprintIndex.get(currentInventory);
						assert newCargoBlueprints[currentCargoBlueprintIndex] == null;
						newCargoBlueprints[currentCargoBlueprintIndex] = replacementCargoBlueprint;

						replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
						replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
						secondPassCargoBlueprintIndex.put(currentInventory, currentCargoBlueprintIndex + 1);
					} else {
						final int allocationDrop = currentCargoBlueprint.getAllocatedVolume();
						final Set<MUDContainer> uniqueMudContainers = new HashSet<>();
						for (final Pair<MUDContainer, AllocationTracker> pair : combinedTrackers) {
							uniqueMudContainers.add(pair.getFirst());
						}
						final MUDContainer mostEntitledMUDContainer = uniqueMudContainers.stream().max((Comparator<MUDContainer>) (mc0, mc1) -> {
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
						final List<AllocationTracker> potentialAllocationTrackers = combinedTrackers.stream().filter(p -> p.getFirst() == mostEntitledMUDContainer).map(Pair::getSecond)
								.collect(Collectors.toList());
						if (potentialAllocationTrackers.size() == 1) {
							final Pair<MUDContainer, AllocationTracker> replacementPair = Pair.of(mostEntitledMUDContainer, potentialAllocationTrackers.get(0));

							final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(), replacementPair.getFirst().getEntity());

							final CargoBlueprint[] newCargoBlueprints = secondPassCargoBlueprints.get(currentInventory);
							final int currentCargoBlueprintIndex = secondPassCargoBlueprintIndex.get(currentInventory);
							assert newCargoBlueprints[currentCargoBlueprintIndex] == null;
							newCargoBlueprints[currentCargoBlueprintIndex] = replacementCargoBlueprint;

							replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							secondPassCargoBlueprintIndex.put(currentInventory, currentCargoBlueprintIndex + 1);
						} else {
							final AllocationTracker mudAllocationTracker = potentialAllocationTrackers.stream().max((t1, t2) -> Long.compare(t1.getRunningAllocation(), t2.getRunningAllocation()))
									.get();
							final Pair<MUDContainer, AllocationTracker> replacementPair = Pair.of(mostEntitledMUDContainer, mudAllocationTracker);

							final CargoBlueprint replacementCargoBlueprint = currentCargoBlueprint.getPostHarmonisationReplacement(replacementPair.getSecond(), replacementPair.getFirst().getEntity());

							final CargoBlueprint[] newCargoBlueprints = secondPassCargoBlueprints.get(currentInventory);
							final int currentCargoBlueprintIndex = secondPassCargoBlueprintIndex.get(currentInventory);
							assert newCargoBlueprints[currentCargoBlueprintIndex] == null;
							newCargoBlueprints[currentCargoBlueprintIndex] = replacementCargoBlueprint;

							replacementPair.getFirst().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							replacementPair.getSecond().dropAllocation(replacementCargoBlueprint.getAllocatedVolume());
							secondPassCargoBlueprintIndex.put(currentInventory, currentCargoBlueprintIndex + 1);
						}
					}
				}
			}
		}

		if (secondPassCargoBlueprints.values().stream().anyMatch(arr -> arr.length > 0)) {
			final IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
			secondPassCargoBlueprints.values().forEach(arr -> {
				for (final CargoBlueprint cargoBlueprint : arr) {
					cargoBlueprint.constructCargoModelPopulationCommands(sm, cargoModel, cec, editingDomain, volumeFlex, sdp, vessToVA, adpNominalMarket, cmd);
				}
			});
			editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
		} else {
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
					rhsScrolledComposite.setVisible(true);
					final PurchaseContractProfile purchaseContractProfile = (PurchaseContractProfile) target;
					final List<Object> o = new LinkedList<>();
					for (LoadSlot s : editorData.getScenarioModel().getCargoModel().getLoadSlots()) {
						if (s.getContract() == purchaseContractProfile.getContract()) {
							o.add(s);
						}
					}
					previewViewer.setInput(o);
					previewGroup.setVisible(true);
				} else if (target instanceof SalesContractProfile) {
					rhsScrolledComposite.setVisible(true);
					final SalesContractProfile salesContractProfile = (SalesContractProfile) target;
					final List<Object> o = new LinkedList<>();
					for (DischargeSlot s : editorData.getScenarioModel().getCargoModel().getDischargeSlots()) {
						if (s.getContract() == salesContractProfile.getContract()) {
							o.add(s);
						}
					}
					previewViewer.setInput(o);
					previewGroup.setVisible(true);
				} else if (target instanceof MullProfile) {
					previewGroup.setVisible(false);
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
//		final List<InventoryEventRow> filteredFeeds = inventory.getFeeds().stream() //
//				.filter(f -> !f.getStartDate().isBefore(startDateTimeInclusive.toLocalDate()) || f.getPeriod() == InventoryFrequency.LEVEL) //
//				.collect(Collectors.toList());
		addHourlyNetVolumes(inventory.getFeeds(), capacityTreeMap, insAndOuts, IntUnaryOperator.identity());
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

	private boolean isAtStartHourOfMonth(final LocalDateTime localDateTime) {
		return localDateTime.getDayOfMonth() == 1 && localDateTime.getHour() == 0;
	}
}
