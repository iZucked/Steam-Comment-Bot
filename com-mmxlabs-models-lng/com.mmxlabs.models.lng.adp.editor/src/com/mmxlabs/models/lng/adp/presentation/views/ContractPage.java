/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import com.mmxlabs.common.time.Days;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractAllocationRow;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventoryProfile;
import com.mmxlabs.models.lng.adp.InventorySubprofile;
import com.mmxlabs.models.lng.adp.MarketAllocationRow;
import com.mmxlabs.models.lng.adp.MultipleInventoryProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryCapacityRow;
import com.mmxlabs.models.lng.cargo.InventoryEventRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SchedulingTimeWindow;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
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
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
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
	
	private Button generateFromInventoryButton;

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
				
				ComboViewer objSel = objectSelector;
				
				objectSelector.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).create());
				final ArrayContentProvider a = new ArrayContentProvider();
				objectSelector.setContentProvider(new ArrayContentProvider());
				objectSelector.setLabelProvider(new LabelProvider() {

					@Override
					public String getText(final Object element) {
						ComboViewer objSell = objSel;
						if (element instanceof MultipleInventoryProfile) {
							return "Multiple inventories";
						} else if (element instanceof Inventory) {
							final Inventory profile = (Inventory) element;
							return String.format("%s (Inventory)", profile.getName());
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
						
						generateButton.setEnabled(target instanceof Contract || target instanceof Inventory || target instanceof MultipleInventoryProfile);
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
						} else if (input instanceof InventoryProfile) {
							final InventoryProfile profile = (InventoryProfile) input;
							int i = 0;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");
							final Command populateModelCommand = populateModelFromInventory(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							cmd.append(populateModelCommand);
							// editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
							input = profile.getInventory();
						} else if (input instanceof MultipleInventoryProfile) {
							final MultipleInventoryProfile profile = (MultipleInventoryProfile) input;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");
							final Command populateModelCommand = populateModelFromMultipleInventories(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							cmd.append(populateModelCommand);
							// editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
							input = profile;
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
	
	private Command populateModelFromInventory(EditingDomain ed, LNGScenarioModel sm, ADPModel adpModel, InventoryProfile profile) {
		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");
		
		CargoEditingCommands cec = new CargoEditingCommands(ed, sm, ScenarioModelUtil.getCargoModel(sm), ScenarioModelUtil.getCommercialModel(sm),
				Activator.getDefault().getModelFactoryRegistry());
		
//		List<LoadSlot> slotsToRemove = profile.getGeneratedSlots();
//		if (!slotsToRemove.isEmpty()) {
//			cmd.append(DeleteCommand.create(ed, slotsToRemove));
//		}
		final int loadWindow = profile.getWindowSize();
		
		final List<Triple<LoadSlot, DESSalesMarket, Vessel>> slotToMarketVessels = new LinkedList<>();
		final List<Triple<LoadSlot, SalesContract, Vessel>> slotToSalesContract = new LinkedList<>();
		final List<Triple<LoadSlot, DESSalesMarket, Vessel>> slotToFOBSaleVessels = new LinkedList<>();
		final List<Triple<LoadSlot, DESSalesMarket, Vessel>> capltaplSlotToMarket = new LinkedList<>();
		final Map<Vessel, LocalDate> vesselToMostRecentUseDate = new HashMap<>();
		
		final BaseLegalEntity capltaplEntity = ScenarioModelUtil.getCommercialModel(sm).getEntities().stream().filter(e -> e.getName().equalsIgnoreCase("capl/tapl")).findAny().get();
		
		final Map<BaseLegalEntity,Integer> thirdPartyVesselIndex = new HashMap<>();
		final Map<BaseLegalEntity, List<Vessel>> thirdPartyVesselList = new HashMap<>();
		final Map<BaseLegalEntity, List<MarketAllocationRow>> thirdPartyMarketAllocs = new HashMap<>();
		// int capltaplContractIndex = 0;
		SalesContract[] capltaplSalesContracts = null;
		DESSalesMarket[] capltaplMarkets = null;
		final Map<SalesContract, Integer> capltaplSalesContractVesselIndices = new HashMap<>();
		
		final List<Pair<SalesContract, Double>> capltaplSalesContractRelativeEntitlements = new LinkedList<>();
		final Map<SalesContract, Long> capltaplSalesContractRunningAllocation = new HashMap<>();
		final List<Pair<DESSalesMarket, Double>> capltaplMarketRelativeEntitlements = new LinkedList<>();
		final Map<DESSalesMarket, Long> capltaplMarketRunningAllocation = new HashMap<>();
		
		final Map<SalesContract, List<Vessel>> capltaplContractVesselLists = new HashMap<>();
		final Map<DESSalesMarket, List<Vessel>> capltaplMarketVesselLists = new HashMap<>();
		final Set<Vessel> capltaplVessels = new HashSet<>();
		final List<Pair<BaseLegalEntity, Double>> relativeEntitlements = new ArrayList<>();
		final Map<BaseLegalEntity, Long> runningAllocation = new HashMap<>();
		
		LocalDate dayBeforeADPStart = adpModel.getYearStart().atDay(1).minusDays(1);
		for (InventoryADPEntityRow row : profile.getEntityTable()) {
			final BaseLegalEntity currentEntity = row.getEntity();
			if (currentEntity.equals(capltaplEntity)) {
				capltaplSalesContracts = new SalesContract[row.getContractAllocationRows().size()];
				capltaplMarkets = new DESSalesMarket[row.getMarketAllocationRows().size()];
				int i = 0;
				double totalWeight = IntStream.concat(
						row.getContractAllocationRows().stream().mapToInt(ContractAllocationRow::getWeight),
						row.getMarketAllocationRows().stream().mapToInt(MarketAllocationRow::getWeight)
						).sum();
				
				for (ContractAllocationRow contractAllocationRow : row.getContractAllocationRows()) {
					final SalesContract currentSalesContract = contractAllocationRow.getContract();
					capltaplSalesContracts[i] = currentSalesContract;
					final List<Vessel> currentVessels = contractAllocationRow.getVessels();
					currentVessels.stream().forEach(v -> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
					capltaplVessels.addAll(currentVessels);
					capltaplContractVesselLists.put(currentSalesContract, currentVessels);
					capltaplSalesContractVesselIndices.put(currentSalesContract, 0);
					final double relEntitle = contractAllocationRow.getWeight()/totalWeight;
					capltaplSalesContractRelativeEntitlements.add(new Pair<>(currentSalesContract, relEntitle));
					capltaplSalesContractRunningAllocation.put(currentSalesContract, 0L);
					++i;
				}
				i = 0;
				for (MarketAllocationRow marAllocationRow : row.getMarketAllocationRows()) {
					final DESSalesMarket currentSalesMarket = marAllocationRow.getMarket();
					capltaplMarkets[i] = currentSalesMarket;
					final List<Vessel> currentVessels = marAllocationRow.getVessels();
					currentVessels.stream().forEach(v -> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
					capltaplVessels.addAll(currentVessels);
					capltaplMarketVesselLists.put(currentSalesMarket, currentVessels);
					final double relEntitle = marAllocationRow.getWeight()/totalWeight;
					capltaplMarketRelativeEntitlements.add(new Pair<>(currentSalesMarket, relEntitle));
					capltaplMarketRunningAllocation.put(currentSalesMarket, 0L);
					++i;
				}
			} else {
				thirdPartyVesselIndex.put(currentEntity, 0);
				thirdPartyVesselList.put(currentEntity, row.getMarketAllocationRows().get(0).getVessels());
				thirdPartyMarketAllocs.put(currentEntity, row.getMarketAllocationRows());
				row.getMarketAllocationRows().get(0).getVessels().stream().forEach(v -> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
			}
			relativeEntitlements.add(new Pair<>(currentEntity, row.getRelativeEntitlement()));
			runningAllocation.put(currentEntity, Long.parseLong(row.getInitialAllocation()));
		}
		
		Set<BaseLegalEntity> nonCaplTaplVesselSharingEntities = new HashSet<>();
		for (InventoryADPEntityRow row : profile.getEntityTable()) {
			if (!row.getEntity().equals(capltaplEntity) && Stream.concat(row.getMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), row.getContractAllocationRows().stream().flatMap(con -> con.getVessels().stream())).noneMatch(capltaplVessels::contains)){
				nonCaplTaplVesselSharingEntities.add(row.getEntity());
			}
		}
		
		final Set<Vessel> expectedVessels = profile.getEntityTable().stream() //
				.flatMap(row -> Stream.concat(row.getMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), row.getContractAllocationRows().stream().flatMap(con -> con.getVessels().stream()))) //
//				.flatMap(row -> row.getMarketAllocationRows().stream()) //
//				.flatMap(mar -> mar.getVessels().stream()) //
				.collect(Collectors.toSet());
		final Map<Vessel, VesselAvailability> vessToVA = sm.getCargoModel().getVesselAvailabilities().stream().filter(va -> expectedVessels.contains(va.getVessel())).collect(Collectors.toMap(VesselAvailability::getVessel, Function.identity()));

		for (Vessel v : expectedVessels) {
			if (!vessToVA.containsKey(v)) {
				System.out.println("No availability for "+ v.getName());
			}
		}
		
		final Inventory inventory = profile.getInventory();
		final List<LoadSlot> newLoadSlots = new LinkedList<>();
		final Optional<PurchaseContract> pcFetch = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts().stream() //
				.filter(c -> inventory.getPort().equals(c.getPreferredPort())) //
				.findFirst();
		assert pcFetch.isPresent();
		final PurchaseContract chosenPurchaseContract = pcFetch.get();
		final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = getInventoryInsAndOuts(inventory, sm);
		int totalInventoryVolume = 0;
		final int cargoVolume = profile.getVolume();
		final LocalDate startDate = adpModel.getYearStart().atDay(1);
		
		
		
		while (insAndOuts.firstKey().isBefore(startDate)) {
			final InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
			totalInventoryVolume += event.netVolumeIn;
		}
		insAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
		
		final Port inventoryPort = inventory.getPort();
		final Set<LocalDate> flaggedDates = new HashSet<>();
		final LocalDate dayBeforeStart = startDate.minusDays(1);
		List<LoadSlot> existingLoads = sm.getCargoModel().getLoadSlots().stream() //
				.filter(s -> s.getPort().equals(inventoryPort) && dayBeforeStart.isBefore(s.getWindowStart())) //
				.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())) //
				.collect(Collectors.toList());
		existingLoads.stream().map(LoadSlot::getWindowStart).forEach(flaggedDates::add);
		
		int runningVolume = 0;
		int idx = 0;
		
		Iterator<Entry<LocalDate, InventoryDailyEvent>> iterInsAndOuts = insAndOuts.entrySet().iterator();
		Iterator<LoadSlot> iterExistingLoads = existingLoads.iterator();
		
		Entry<LocalDate, InventoryDailyEvent> currentEntry = null;
		LoadSlot currentExistingSlot = null;
		
		if (iterExistingLoads.hasNext()) {
			currentExistingSlot = iterExistingLoads.next();
		}
		
		Stack<LoadSlot> tempSlots = new Stack<>();
		for (final Entry<LocalDate, InventoryDailyEvent> entry : insAndOuts.entrySet()) {
			final InventoryDailyEvent dailyEvent = entry.getValue();
			runningVolume += dailyEvent.netVolumeIn;
			for (final Pair<BaseLegalEntity, Double> p : relativeEntitlements) {
				final Long additionalAllocation = ((Double) (dailyEvent.netVolumeIn*p.getSecond())).longValue();
				runningAllocation.compute(p.getFirst(), (k, v) -> {
					return v + additionalAllocation;
				});
				if (p.getFirst().equals(capltaplEntity)) {
					for (final Pair<SalesContract, Double> pp : capltaplSalesContractRelativeEntitlements) {
						capltaplSalesContractRunningAllocation.compute(pp.getFirst(), (k, v) -> v+((Double) (additionalAllocation*pp.getSecond())).longValue());
					}
					for (final Pair<DESSalesMarket, Double> pp : capltaplMarketRelativeEntitlements) {
						capltaplMarketRunningAllocation.compute(pp.getFirst(), (k, v)-> v+ ((Double) (additionalAllocation*pp.getSecond())).longValue());
					}
				}
			}
			final LocalDate currentDate = entry.getKey();
			while (currentExistingSlot != null && currentExistingSlot.getWindowStart().equals(currentDate)) {
				final BaseLegalEntity entity = currentExistingSlot.getEntity();
				final Long entityAllocation = runningAllocation.get(entity);
				if (entityAllocation != null) {
					runningAllocation.put(entity, entityAllocation - currentExistingSlot.getMaxQuantity());
				}
				currentExistingSlot = iterExistingLoads.hasNext() ? iterExistingLoads.next() : null;
			}
			while (runningVolume < dailyEvent.minVolume && !newLoadSlots.isEmpty()) {
				final LoadSlot oldLoadSlot = ((LinkedList<LoadSlot>) newLoadSlots).removeLast();
				tempSlots.add(oldLoadSlot);
				runningVolume += oldLoadSlot.getMaxQuantity();
				runningAllocation.compute(oldLoadSlot.getEntity(), (k, v) -> v + oldLoadSlot.getMaxQuantity());
			}
			final Entry<BaseLegalEntity, Long> mullEntity = runningAllocation.entrySet().stream() //
					.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
			if (mullEntity.getKey().equals(capltaplEntity)) {
				
				SalesContract currentSalesContract = null;
				DESSalesMarket currentMarket = null;
				Vessel currentVessel = null;
				List<Vessel> vessList = null;
				int allocationDrop;
				
				final Entry<SalesContract, Long> mullSalesContract = capltaplSalesContractRunningAllocation.entrySet().stream() //
						.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
				
				final Entry<DESSalesMarket, Long> mullMarket = capltaplMarketRunningAllocation.entrySet().stream() //
						.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
				
				if (mullSalesContract.getValue() > mullMarket.getValue()) {
					currentSalesContract = mullSalesContract.getKey();
					vessList = capltaplContractVesselLists.get(currentSalesContract);
					
				} else {
					currentMarket = mullMarket.getKey();
					vessList = capltaplMarketVesselLists.get(currentMarket);
				}
				
				if (!vessList.isEmpty()) {
					currentVessel = vessList.stream().min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2))).get();
					allocationDrop = (int) (currentVessel.getVesselOrDelegateCapacity()*currentVessel.getVesselOrDelegateFillCapacity());
				} else {
					allocationDrop = cargoVolume;
				}
				
				int tempRunningVolume = runningVolume - allocationDrop;
				if (tempRunningVolume > dailyEvent.minVolume && !flaggedDates.contains(currentDate)) {
					final LoadSlot slot;
					if (tempSlots.isEmpty()) {
						final String nextName = inventory.getName() + "-" + Integer.toString(idx);
						slot = CargoFactory.eINSTANCE.createLoadSlot();
						slot.setPort(inventoryPort);
						slot.setVolumeLimitsUnit(VolumeUnits.M3);
						slot.setMinQuantity(allocationDrop-500);
						slot.setMaxQuantity(allocationDrop);
						slot.setName(nextName);
						slot.setWindowSize(loadWindow);
						slot.setWindowSizeUnits(TimePeriod.DAYS);
						slot.setContract(chosenPurchaseContract);
						++idx;
					} else {
						slot = tempSlots.pop();
					}
					slot.setWindowStart(currentDate);
					mullEntity.setValue(mullEntity.getValue()-allocationDrop);
					slot.setEntity(mullEntity.getKey());
					newLoadSlots.add(slot);
					runningVolume = tempRunningVolume;

					if (currentSalesContract != null) {
						slotToSalesContract.add(new Triple<>(slot, currentSalesContract, currentVessel));
						mullSalesContract.setValue(mullSalesContract.getValue()-allocationDrop);
					} else {
						capltaplSlotToMarket.add(new Triple<>(slot, currentMarket, currentVessel));
						mullMarket.setValue(mullMarket.getValue()-allocationDrop);
					}
					if (currentVessel != null) {
						// capltaplSalesContractVesselIndices.compute(currentSalesContract, (k, i) -> (Integer) (i+1)%vessList.size());
						vesselToMostRecentUseDate.put(currentVessel, currentDate);
					}
				}
			} else {
				final BaseLegalEntity ent = mullEntity.getKey();
				Vessel currentVessel = null;
				List<Vessel> vessList = thirdPartyVesselList.get(ent);
				int allocationDrop;
				if (!vessList.isEmpty()) {
					// currentVessel = vessList.get(thirdPartyVesselIndex.get(ent));
					currentVessel = vessList.stream().min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2))).get();
					allocationDrop = (int) (currentVessel.getVesselOrDelegateCapacity()*currentVessel.getVesselOrDelegateFillCapacity());
				} else {
					allocationDrop = cargoVolume;
				}
				
				int tempRunningVolume = runningVolume - allocationDrop;
				if (tempRunningVolume > dailyEvent.minVolume && !flaggedDates.contains(currentDate)) {
					final LoadSlot slot;
					if (tempSlots.isEmpty()) {
						final String nextName = inventory.getName() + "-" + Integer.toString(idx);
						slot = CargoFactory.eINSTANCE.createLoadSlot();
						slot.setPort(inventoryPort);
						slot.setVolumeLimitsUnit(VolumeUnits.M3);
						slot.setMinQuantity(allocationDrop-500);
						slot.setMaxQuantity(allocationDrop);
						slot.setName(nextName);
						slot.setWindowSize(loadWindow);
						slot.setWindowSizeUnits(TimePeriod.DAYS);
						slot.setContract(chosenPurchaseContract);
						++idx;
					} else {
						slot = tempSlots.pop();
					}
					slot.setWindowStart(currentDate);
					mullEntity.setValue(mullEntity.getValue()-allocationDrop);
					slot.setEntity(mullEntity.getKey());
					newLoadSlots.add(slot);
					runningVolume = tempRunningVolume;
					if (currentVessel != null) {
						// thirdPartyVesselIndex.put(ent, (thirdPartyVesselIndex.get(ent)+1)%thirdPartyVesselList.get(ent).size());
						vesselToMostRecentUseDate.put(currentVessel, currentDate);
					}
					if (nonCaplTaplVesselSharingEntities.contains(mullEntity.getKey())) {
						slotToFOBSaleVessels.add(new Triple<>(slot, thirdPartyMarketAllocs.get(ent).get(0).getMarket(), currentVessel));
					} else {
						slotToMarketVessels.add(new Triple<>(slot, thirdPartyMarketAllocs.get(ent).get(0).getMarket(), currentVessel));
					}
				}
			}
		}
		if (!newLoadSlots.isEmpty()) {
//			if (ScenarioModelUtil.getCargoModel(sm).getLoadSlots().isEmpty()) {
//				cmd.append(SetCommand.create(ed, ScenarioModelUtil.getCargoModel(sm), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, newLoadSlots));
//			} else {
//				cmd.append(AddCommand.create(ed, ScenarioModelUtil.getCargoModel(sm), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS , newLoadSlots));
//			}
			cmd.append(AddCommand.create(ed, ScenarioModelUtil.getCargoModel(sm), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, newLoadSlots));
			// cmd.append(AddCommand.create(ed, profile, ADPPackage.Literals.INVENTORY_PROFILE__GENERATED_SLOTS, newLoadSlots));
			editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
			
			for (Triple<LoadSlot, DESSalesMarket, Vessel> t : capltaplSlotToMarket) {
				final List<Command> setCommands = new LinkedList<>();
				final List<EObject> deleteObjects = new LinkedList<>();
				LoadSlot l = t.getFirst();
				DESSalesMarket m = t.getSecond();
				Vessel v = t.getThird();
				
				DischargeSlot dSlot = cec.createNewSpotDischarge(setCommands, ScenarioModelUtil.getCargoModel(sm), m);
				ZonedDateTime cal = l.getSchedulingTimeWindow().getStart();
				IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
				final int travelTime = CargoEditorMenuHelper.getTravelTime(l, dSlot, v, sdp);
				if (travelTime == Integer.MAX_VALUE) {
					final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", l.getPort().getName(),
							dSlot.getPort().getName(), travelTime);
					throw new RuntimeException(message);
				}
				cal = cal.plusHours(travelTime);
				cal = cal.plusHours(l.getSchedulingTimeWindow().getDuration());
				
				cal = cal.withDayOfMonth(1).withHour(0);
				final LocalDate dischargeCal = cal.toLocalDate();
				final String yearMonthString = CargoEditorMenuHelper.getKeyForDate(dischargeCal);
				dSlot.setWindowStart(dischargeCal);
				dSlot.setWindowStartTime(0);
				
				dSlot.setWindowSize(1);
				dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
				dSlot.setRestrictedVesselsArePermissive(true);
				dSlot.setRestrictedVesselsOverride(true);
				dSlot.getRestrictedVessels().addAll(capltaplMarketVesselLists.get(m));
				
				String id = "chev-spot-sale-" + l.getName();
				
				dSlot.setName(id);
				// l.setNominatedVessel(v);
				// last parameter should be unused since cim is null
				CargoEditingCommands.runWiringUpdate(ed, sm.getCargoModel(), setCommands, deleteObjects, l, dSlot, null, 0);
				final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
				for (final Command c : setCommands) {
					currentWiringCommand.append(c);
				}
				
				if (!deleteObjects.isEmpty()) {
					currentWiringCommand.append(DeleteCommand.create(ed, deleteObjects));
				}
				if (!currentWiringCommand.isEmpty()) {
					ed.getCommandStack().execute(currentWiringCommand);
					if (v != null) {
						VesselAvailability va = vessToVA.get(v);
						if (va != null) {
							ed.getCommandStack().execute(SetCommand.create(ed, l.getCargo(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), va));
						};
					}
				}
			}
			for (Triple<LoadSlot, DESSalesMarket, Vessel> t : slotToMarketVessels) {
				final List<Command> setCommands = new LinkedList<>();
				final List<EObject> deleteObjects = new LinkedList<>();
				LoadSlot l = t.getFirst();
				DESSalesMarket m = t.getSecond();
				Vessel v = t.getThird();
				
				DischargeSlot dSlot = cec.createNewSpotDischarge(setCommands, ScenarioModelUtil.getCargoModel(sm), m);
				ZonedDateTime cal = l.getSchedulingTimeWindow().getStart();
				IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
				final int travelTime = CargoEditorMenuHelper.getTravelTime(l, dSlot, v, sdp);
				if (travelTime == Integer.MAX_VALUE) {
					final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", l.getPort().getName(),
							dSlot.getPort().getName(), travelTime);
					throw new RuntimeException(message);
				}
				cal = cal.plusHours(travelTime);
				cal = cal.plusHours(l.getSchedulingTimeWindow().getDuration());
				
				cal = cal.withDayOfMonth(1).withHour(0);
				final LocalDate dischargeCal = cal.toLocalDate();
				final String yearMonthString = CargoEditorMenuHelper.getKeyForDate(dischargeCal);
				dSlot.setWindowStart(dischargeCal);
				dSlot.setWindowStartTime(0);
				
				dSlot.setWindowSize(1);
				dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
				dSlot.setRestrictedVesselsArePermissive(true);
				dSlot.setRestrictedVesselsOverride(true);
				dSlot.getRestrictedVessels().addAll(thirdPartyVesselList.get(l.getEntity()));
				
				String id = "spot-sale-" + l.getName();
				
				dSlot.setName(id);
				// l.setNominatedVessel(v);
				// last parameter should be unused since cim is null
				CargoEditingCommands.runWiringUpdate(ed, sm.getCargoModel(), setCommands, deleteObjects, l, dSlot, null, 0);
				final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
				for (final Command c : setCommands) {
					currentWiringCommand.append(c);
				}
				
				if (!deleteObjects.isEmpty()) {
					currentWiringCommand.append(DeleteCommand.create(ed, deleteObjects));
				}
				if (!currentWiringCommand.isEmpty()) {
					ed.getCommandStack().execute(currentWiringCommand);
					if (v != null) {
						VesselAvailability va = vessToVA.get(v);
						if (va != null) {
							l.getCargo().setAllowRewiring(false);
							ed.getCommandStack().execute(SetCommand.create(ed, l.getCargo(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), va));
						};
					}
				}
			}
			for (Triple<LoadSlot, DESSalesMarket, Vessel> t : slotToFOBSaleVessels) {
				final List<Command> setCommands = new LinkedList<>();
				final List<EObject> deleteObjects = new LinkedList<>();
				LoadSlot l = t.getFirst();
				DESSalesMarket m = t.getSecond();
				Vessel v = t.getThird();
				
				DischargeSlot dSlot = cec.createNewDischarge(setCommands, ScenarioModelUtil.getCargoModel(sm), true);
				dSlot.setWindowStart(l.getWindowStart());
				dSlot.setWindowStartTime(0);
				
				dSlot.setWindowSize(1);
				dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
				
				String id = "fob-sale-" + l.getName();
				
				dSlot.setPriceExpression("JKM");
				dSlot.setPort(l.getPort());
				dSlot.setFobSaleDealType(FOBSaleDealType.SOURCE_ONLY);
				dSlot.setEntity(l.getEntity());
				dSlot.setNominatedVessel(v);
				
				dSlot.setName(id);
				// last parameter should be unused since cim is null
				CargoEditingCommands.runWiringUpdate(ed, sm.getCargoModel(), setCommands, deleteObjects, l, dSlot, null, 0);
				final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
				for (final Command c : setCommands) {
					currentWiringCommand.append(c);
				}
				
				if (!deleteObjects.isEmpty()) {
					currentWiringCommand.append(DeleteCommand.create(ed, deleteObjects));
				}
				if (!currentWiringCommand.isEmpty()) {
					ed.getCommandStack().execute(currentWiringCommand);
					l.getCargo().setAllowRewiring(false);
//					if (v != null) {
//						VesselAvailability va = vessToVA.get(v);
//						if (va != null) {
//							ed.getCommandStack().execute(SetCommand.create(ed, l.getCargo(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), va));
//						};
//					}
				}
			}
			for (Triple<LoadSlot, SalesContract, Vessel> t : slotToSalesContract) {
				final List<Command> setCommands = new LinkedList<>();
				final List<EObject> deleteObjects = new LinkedList<>();
				LoadSlot l = t.getFirst();
				SalesContract salesContract = t.getSecond();
				Vessel v = t.getThird();
				//DischargeSlot dSlot = cec.createNewSpotDischarge(setCommands, ScenarioModelUtil.getCargoModel(sm), m);
				
				DischargeSlot dSlot = cec.createNewDischarge(setCommands, ScenarioModelUtil.getCargoModel(sm), false);
				dSlot.setContract(salesContract);
				dSlot.setName("chev-contract-"+l.getName());
				dSlot.setEntity(l.getEntity());
				dSlot.setPort(salesContract.getPreferredPort());
				ZonedDateTime cal = l.getSchedulingTimeWindow().getStart();
				IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
				final int travelTime = CargoEditorMenuHelper.getTravelTime(l, dSlot, v, sdp);
				if (travelTime == Integer.MAX_VALUE) {
					final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", l.getPort().getName(),
							dSlot.getPort().getName(), travelTime);
					throw new RuntimeException(message);
				}
				cal = cal.plusHours(travelTime);
				cal = cal.plusHours(l.getSchedulingTimeWindow().getDuration());

				cal = cal.withDayOfMonth(1).withHour(0);
				final LocalDate dischargeCal = cal.toLocalDate();
				final String yearMonthString = CargoEditorMenuHelper.getKeyForDate(dischargeCal);
				dSlot.setWindowStart(dischargeCal);
				dSlot.setWindowStartTime(0);

				dSlot.setWindowSize(1);
				dSlot.setWindowSizeUnits(TimePeriod.MONTHS);

				CargoEditingCommands.runWiringUpdate(ed, sm.getCargoModel(), setCommands, deleteObjects, l, dSlot, null, 0);
				final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
				for (final Command c : setCommands) {
					currentWiringCommand.append(c);
				}
				if (!deleteObjects.isEmpty()) {
					currentWiringCommand.append(DeleteCommand.create(ed, deleteObjects));
				}
				if (!currentWiringCommand.isEmpty()) {
					ed.getCommandStack().execute(currentWiringCommand);
					if (v != null) {
						VesselAvailability va = vessToVA.get(v);
						if (va != null) {
							ed.getCommandStack().execute(SetCommand.create(ed, l.getCargo(), CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), va));
						};
					}
				}
			}
			cec.verifyCargoModel(ScenarioModelUtil.getCargoModel(sm));
			//editorData.getDefaultCommandHandler().handleCommand(cmd, null, null);
		} else if (cmd.isEmpty()) {
			return IdentityCommand.INSTANCE;
		}
		return cmd;
	}

	
	private Command populateModelFromMultipleInventories(EditingDomain ed, LNGScenarioModel sm, ADPModel adpModel, MultipleInventoryProfile profile) {
		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");
		
		CargoEditingCommands cec = new CargoEditingCommands(ed, sm, ScenarioModelUtil.getCargoModel(sm), ScenarioModelUtil.getCommercialModel(sm),
				Activator.getDefault().getModelFactoryRegistry());
		
//		List<LoadSlot> slotsToRemove = profile.getGeneratedSlots();
//		if (!slotsToRemove.isEmpty()) {
//			cmd.append(DeleteCommand.create(ed, slotsToRemove));
//		}
		final int loadWindow = profile.getWindowSize();
		final int volumeFlex = profile.getVolumeFlex();
		
		final boolean dynamicWindowGeneration = false;
		final boolean woodsideDES = false;
		
		final int fullCargoLotValue = profile.getFullCargoLotValue();
		
		final List<Triple<LoadSlot, DESSalesMarket, Vessel>> slotToMarketVessels = new LinkedList<>();
		final List<Triple<LoadSlot, SalesContract, Vessel>> slotToThirdPartySalesContract = new LinkedList<>();
		final List<Triple<LoadSlot, SalesContract, Vessel>> slotToSalesContract = new LinkedList<>();
		final List<Triple<LoadSlot, DESSalesMarket, Vessel>> slotToFOBSaleVessels = new LinkedList<>();
		final List<Triple<LoadSlot, DESSalesMarket, Vessel>> capltaplSlotToMarket = new LinkedList<>();
		final Map<Vessel, LocalDate> vesselToMostRecentUseDate = new HashMap<>();
		
		final BaseLegalEntity capltaplEntity = ScenarioModelUtil.getCommercialModel(sm).getEntities().stream().filter(e -> e.getName().equalsIgnoreCase("capl/tapl")).findAny().get();
		
//		final Map<BaseLegalEntity,Integer> thirdPartyVesselIndex = new HashMap<>();
		final Map<BaseLegalEntity, List<Vessel>> thirdPartyVesselList = new HashMap<>();
		final Map<BaseLegalEntity, List<SalesContract>> thirdPartySalesContractAllocs = new HashMap<>();
		final Map<BaseLegalEntity, List<MarketAllocationRow>> thirdPartyMarketAllocs = new HashMap<>();
		
		final Map<Inventory, Map<BaseLegalEntity, SalesContract[]>> thirdPartySalesContracts = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, DESSalesMarket[]>> thirdPartyMarkets = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, List<Pair<SalesContract, Double>>>> thirdPartySalesContractRelativeEntitlements = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Map<SalesContract, Long>>> thirdPartySalesContractRunningAllocation = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, List<Pair<DESSalesMarket, Double>>>> thirdPartyMarketRelativeEntitlements = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Map<DESSalesMarket, Long>>> thirdPartyMarketRunningAllocation = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Map<SalesContract, List<Vessel>>>> thirdPartySalesContractVesselLists = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Map<DESSalesMarket, List<Vessel>>>> thirdPartyMarketVesselLists = new HashMap<>();
		
		// int capltaplContractIndex = 0;
		
		
		final Map<Inventory, SalesContract[]> capltaplSalesContracts = new HashMap<>();
		final Map<Inventory, DESSalesMarket[]> capltaplMarkets = new HashMap<>();
		final Map<Inventory, List<Pair<SalesContract, Double>>> capltaplSalesContractRelativeEntitlements = new HashMap<>();
		final Map<Inventory, Map<SalesContract, Long>> capltaplSalesContractRunningAllocation = new HashMap<>();
		final Map<Inventory, List<Pair<DESSalesMarket, Double>>> capltaplMarketRelativeEntitlements = new HashMap<>();
		final Map<Inventory, Map<DESSalesMarket, Long>> capltaplMarketRunningAllocation = new HashMap<>();
		
		
//		SalesContract[] capltaplSalesContracts = null;
//		DESSalesMarket[] capltaplMarkets = null;
		final Map<SalesContract, Integer> capltaplSalesContractVesselIndices = new HashMap<>();
		
//		final List<Pair<SalesContract, Double>> capltaplSalesContractRelativeEntitlements = new LinkedList<>();
//		final Map<SalesContract, Long> capltaplSalesContractRunningAllocation = new HashMap<>();
//		final List<Pair<DESSalesMarket, Double>> capltaplMarketRelativeEntitlements = new LinkedList<>();
//		final Map<DESSalesMarket, Long> capltaplMarketRunningAllocation = new HashMap<>();
//		
		final Map<SalesContract, List<Vessel>> capltaplContractVesselLists = new HashMap<>();
		final Map<DESSalesMarket, List<Vessel>> capltaplMarketVesselLists = new HashMap<>();
		final Set<Vessel> capltaplVessels = new HashSet<>();
		
		final Map<Inventory, List<Pair<BaseLegalEntity, Double>>> relativeEntitlements = new HashMap<>();
		final Map<Inventory, Map<BaseLegalEntity, Long>> runningAllocation = new HashMap<>();
		
//		final List<Pair<BaseLegalEntity, Double>> relativeEntitlements = new ArrayList<>();
//		final Map<BaseLegalEntity, Long> runningAllocation = new HashMap<>();
		
		final Map<Inventory, Map<BaseLegalEntity, MUDContainer>> mudContainers = new HashMap<>();
		
		
		LocalDate dayBeforeADPStart = adpModel.getYearStart().atDay(1).minusDays(1);
		
		for (InventorySubprofile subprofile : profile.getInventories()) {
			final Inventory currentInventory = subprofile.getInventory();
			Map<BaseLegalEntity, Map<SalesContract, List<Vessel>>> currentThirdPartyToSalesContractToVesselsList = new HashMap<>();
			thirdPartySalesContractVesselLists.put(currentInventory, currentThirdPartyToSalesContractToVesselsList);
			Map<BaseLegalEntity, Map<DESSalesMarket, List<Vessel>>> currentThirdPartyToMarketToVesselsList = new HashMap<>();
			thirdPartyMarketVesselLists.put(currentInventory, currentThirdPartyToMarketToVesselsList);
			Map<BaseLegalEntity, List<Pair<SalesContract, Double>>> currentThirdPartyToSalesContractAndRelEnt = new HashMap<>();
			thirdPartySalesContractRelativeEntitlements.put(currentInventory, currentThirdPartyToSalesContractAndRelEnt);
			Map<BaseLegalEntity, List<Pair<DESSalesMarket, Double>>> currentThirdPartyToMarketAndRelEnt = new HashMap<>();
			thirdPartyMarketRelativeEntitlements.put(currentInventory, currentThirdPartyToMarketAndRelEnt);
			Map<BaseLegalEntity, Map<SalesContract, Long>> currentThirdPartyToSalesContractToRunningAlloc = new HashMap<>();
			thirdPartySalesContractRunningAllocation.put(currentInventory, currentThirdPartyToSalesContractToRunningAlloc);
			Map<BaseLegalEntity, Map<DESSalesMarket, Long>> currentThirdPartyToMarketToRunningAlloc = new HashMap<>();
			thirdPartyMarketRunningAllocation.put(currentInventory, currentThirdPartyToMarketToRunningAlloc);
			Map<BaseLegalEntity, SalesContract[]> currentThirdPartySalesContracts = new HashMap<>();
			thirdPartySalesContracts.put(currentInventory, currentThirdPartySalesContracts);
			Map<BaseLegalEntity, DESSalesMarket[]> currentThirdPartyMarketContracts = new HashMap<>();
			thirdPartyMarkets.put(currentInventory, currentThirdPartyMarketContracts);
			
			final Map<BaseLegalEntity, MUDContainer> currentMUDContainers = new HashMap<>();
			mudContainers.put(currentInventory, currentMUDContainers);
			
			
			final List<Pair<BaseLegalEntity, Double>> currentRelativeEntitlements = new ArrayList<>();
			final Map<BaseLegalEntity, Long> currentRunningAllocations = new HashMap<>();
			for (InventoryADPEntityRow row : subprofile.getEntityTable()) {
				final BaseLegalEntity currentEntity = row.getEntity();
				if (currentEntity.equals(capltaplEntity)) {
					List<Pair<SalesContract, Double>> relEntitles = new LinkedList<>();
					Map<SalesContract, Long> runningAllocs = new HashMap<>();
					SalesContract[] currentSalesContracts = new SalesContract[row.getContractAllocationRows().size()];
					double totalWeight = IntStream.concat(
							row.getContractAllocationRows().stream().mapToInt(ContractAllocationRow::getWeight),
							row.getMarketAllocationRows().stream().mapToInt(MarketAllocationRow::getWeight)
						).sum();
					int i = 0;
					for (ContractAllocationRow contractAllocationRow : row.getContractAllocationRows()) {
						final SalesContract currentSalesContract = contractAllocationRow.getContract();
						final List<Vessel> currentVessels = contractAllocationRow.getVessels();
						currentSalesContracts[i] = currentSalesContract;
						currentVessels.stream().forEach(v-> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
						capltaplVessels.addAll(currentVessels);
						capltaplContractVesselLists.put(currentSalesContract, currentVessels);
						final double relEntitle = contractAllocationRow.getWeight()/totalWeight;
						relEntitles.add(new Pair<>(currentSalesContract, relEntitle));
						runningAllocs.put(currentSalesContract, 0L);
						i++;
					}
					capltaplSalesContractRelativeEntitlements.put(currentInventory, relEntitles);
					capltaplSalesContractRunningAllocation.put(currentInventory, runningAllocs);
					capltaplSalesContracts.put(currentInventory, currentSalesContracts);
					
					
					i = 0;
					List<Pair<DESSalesMarket, Double>> currentMarketRelEntitles = new LinkedList<>();
					Map<DESSalesMarket, Long> currentDESRunningAllocs = new HashMap<>();
					DESSalesMarket[] currentDESSalesMarkets = new DESSalesMarket[row.getMarketAllocationRows().size()];
					for (MarketAllocationRow marAllocationRow : row.getMarketAllocationRows()) {
						final DESSalesMarket currentSalesMarket = marAllocationRow.getMarket();
						currentDESSalesMarkets[i] = currentSalesMarket;
						final List<Vessel> currentVessels = marAllocationRow.getVessels();
						currentVessels.stream().forEach(v -> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
						capltaplVessels.addAll(currentVessels);
						capltaplMarketVesselLists.put(currentSalesMarket, currentVessels);
						final double relEntitle = marAllocationRow.getWeight()/totalWeight;
						currentMarketRelEntitles.add(new Pair<>(currentSalesMarket, relEntitle));
						currentDESRunningAllocs.put(currentSalesMarket, 0L);
						i++;
					}
					capltaplMarketRelativeEntitlements.put(currentInventory, currentMarketRelEntitles);
					capltaplMarketRunningAllocation.put(currentInventory, currentDESRunningAllocs);
					capltaplMarkets.put(currentInventory, currentDESSalesMarkets);
					
					currentMUDContainers.put(currentEntity, new MUDContainer(row));
				} else {
					Map<SalesContract, List<Vessel>> currentSalesContractToVesselsList = new HashMap<>();
					currentThirdPartyToSalesContractToVesselsList.put(currentEntity, currentSalesContractToVesselsList);
					List<Pair<SalesContract, Double>> salesContractsRelEntitles = new LinkedList<>();
					currentThirdPartyToSalesContractAndRelEnt.put(currentEntity, salesContractsRelEntitles);
					Map<SalesContract, Long> runningSalesContractAllocs = new HashMap<>();
					currentThirdPartyToSalesContractToRunningAlloc.put(currentEntity, runningSalesContractAllocs);
					SalesContract[] currentSalesContracts = new SalesContract[row.getContractAllocationRows().size()];
					currentThirdPartySalesContracts.put(currentEntity, currentSalesContracts);
					double totalWeight = IntStream.concat(
							row.getContractAllocationRows().stream().mapToInt(ContractAllocationRow::getWeight),
							row.getMarketAllocationRows().stream().mapToInt(MarketAllocationRow::getWeight)
						).sum();
					int i = 0;
					for (ContractAllocationRow contractAllocationRow : row.getContractAllocationRows()) {
						final SalesContract currentSalesContract = contractAllocationRow.getContract();
						final List<Vessel> currentVessels = contractAllocationRow.getVessels();
						currentSalesContracts[i] = currentSalesContract;
						currentVessels.stream().forEach(v -> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
						List<Vessel> storedVessels = thirdPartyVesselList.get(currentEntity);
						if (storedVessels == null) {
							storedVessels = new LinkedList<>();
							thirdPartyVesselList.put(currentEntity, storedVessels);
						}
						storedVessels.addAll(currentVessels);
						currentSalesContractToVesselsList.put(currentSalesContract, currentVessels);
						final double relEntitle = contractAllocationRow.getWeight()/totalWeight;
						salesContractsRelEntitles.add(new Pair<>(currentSalesContract, relEntitle));
						runningSalesContractAllocs.put(currentSalesContract, 0L);
						i++;
					}
					
					Map<DESSalesMarket, List<Vessel>> currentMarketToVesselsList = new HashMap<>();
					currentThirdPartyToMarketToVesselsList.put(currentEntity, currentMarketToVesselsList);
					List<Pair<DESSalesMarket, Double>> marketRelEntitles = new LinkedList<>();
					currentThirdPartyToMarketAndRelEnt.put(currentEntity, marketRelEntitles);
					Map<DESSalesMarket, Long> runningMarketAllocs = new HashMap<>();
					currentThirdPartyToMarketToRunningAlloc.put(currentEntity, runningMarketAllocs);
					DESSalesMarket[] currentMarkets = new DESSalesMarket[row.getMarketAllocationRows().size()];
					currentThirdPartyMarketContracts.put(currentEntity, currentMarkets);
					
					i = 0;
					for (MarketAllocationRow marketAllocationRow : row.getMarketAllocationRows()) {
						final DESSalesMarket currentMarket = marketAllocationRow.getMarket();
						final List<Vessel> currentVessels = marketAllocationRow.getVessels();
						currentMarkets[i] = currentMarket;
						currentVessels.stream().forEach(v -> vesselToMostRecentUseDate.put(v, dayBeforeADPStart));
						List<Vessel> storedVessels = thirdPartyVesselList.get(currentEntity);
						if (storedVessels == null) {
							storedVessels = new LinkedList<>();
							thirdPartyVesselList.put(currentEntity, storedVessels);
						}
						storedVessels.addAll(currentVessels);
						currentMarketToVesselsList.put(currentMarket, currentVessels);
						final double relEntitle = marketAllocationRow.getWeight()/totalWeight;
						marketRelEntitles.add(new Pair<>(currentMarket, relEntitle));
						runningMarketAllocs.put(currentMarket, 0L);
						i++;
					}
					currentMUDContainers.put(currentEntity, new MUDContainer(row));
				}
				currentRelativeEntitlements.add(new Pair<>(currentEntity, row.getRelativeEntitlement()));
				currentRunningAllocations.put(currentEntity, Long.parseLong(row.getInitialAllocation()));
				
			}
			relativeEntitlements.put(currentInventory, currentRelativeEntitlements);
			runningAllocation.put(currentInventory, currentRunningAllocations);
		}
		
		Set<BaseLegalEntity> nonCaplTaplVesselSharingEntities = new HashSet<>();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			for (InventoryADPEntityRow row : sProfile.getEntityTable()) {
				if (!row.getEntity().equals(capltaplEntity) && 
						Stream.concat(
								row.getMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), 
								row.getContractAllocationRows().stream().flatMap(con -> con.getVessels().stream())
							).noneMatch(capltaplVessels::contains)) {
					nonCaplTaplVesselSharingEntities.add(row.getEntity());
				}
			}
		}
		
//		for (InventoryADPEntityRow row : profile.getEntityTable()) {
//			if (!row.getEntity().equals(capltaplEntity) && Stream.concat(row.getMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), row.getContractAllocationRows().stream().flatMap(con -> con.getVessels().stream())).noneMatch(capltaplVessels::contains)){
//				nonCaplTaplVesselSharingEntities.add(row.getEntity());
//			}
//		}
//		
//		final Set<Vessel> expectedVesselsa = profile.getEntityTable().stream() //
//				.flatMap(row -> Stream.concat(row.getMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), row.getContractAllocationRows().stream().flatMap(con -> con.getVessels().stream()))) //
////				.flatMap(row -> row.getMarketAllocationRows().stream()) //
////				.flatMap(mar -> mar.getVessels().stream()) //
//				.collect(Collectors.toSet());


		final Set<Vessel> expectedVessels = profile.getInventories().stream() //
				.flatMap(sProfile -> sProfile.getEntityTable().stream() //
						.flatMap(row -> Stream.concat(
								row.getMarketAllocationRows().stream().flatMap(mar -> mar.getVessels().stream()), 
								row.getContractAllocationRows().stream().flatMap(con -> con.getVessels().stream())
						)) //
				).collect(Collectors.toSet());
		final Map<Vessel, VesselAvailability> vessToVA = sm.getCargoModel().getVesselAvailabilities().stream().filter(va -> expectedVessels.contains(va.getVessel())).collect(Collectors.toMap(VesselAvailability::getVessel, Function.identity()));
		for (Vessel v : expectedVessels) {
			if (!vessToVA.containsKey(v)) {
				System.out.println("No availability for "+ v.getName());
			}
		}

		final LocalDate startDate = adpModel.getYearStart().atDay(1);
		
		final Map<Inventory, PurchaseContract> inventoryPurchaseContracts = new HashMap<>();
		final List<PurchaseContract> pcs = ScenarioModelUtil.getCommercialModel(sm).getPurchaseContracts();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			inventoryPurchaseContracts.put(
					sProfile.getInventory(), 
					pcs.stream().filter(c -> sProfile.getInventory().getPort().equals(c.getPreferredPort())).findFirst().get()
			);
		}
		final Map<Inventory, TreeMap<LocalDate, InventoryDailyEvent>> inventoryInsAndOuts = new HashMap<>();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			inventoryInsAndOuts.put(sProfile.getInventory(), getInventoryInsAndOuts(sProfile.getInventory(), sm));
		}
		final int cargoVolume = 160000;
		
		for (InventorySubprofile sProfile : profile.getInventories()) {
			int totalInventoryVolume = 0;
			final Inventory currentInventory = sProfile.getInventory();
			final TreeMap<LocalDate, InventoryDailyEvent> currentInsAndOuts = inventoryInsAndOuts.get(currentInventory);
			while (currentInsAndOuts.firstKey().isBefore(startDate)) {
				final InventoryDailyEvent event = currentInsAndOuts.remove(currentInsAndOuts.firstKey());
				totalInventoryVolume += event.netVolumeIn;
			}
			currentInsAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
		}
		
		Map<Inventory, Set<LocalDate>> inventoryFlaggedDates = new HashMap<>();
		
		final LocalDate dayBeforeStart = startDate.minusDays(1);
		for (InventorySubprofile sProfile : profile.getInventories()) {
			final Port inventoryPort = sProfile.getInventory().getPort();
			final Set<LocalDate> flaggedDates = new HashSet<>();
			List<LoadSlot> existingLoads = sm.getCargoModel().getLoadSlots().stream() //
					.filter(s -> s.getPort().equals(inventoryPort) && dayBeforeStart.isBefore(s.getWindowStart())) //
					.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())) //
					.collect(Collectors.toList());
			existingLoads.stream().map(LoadSlot::getWindowStart).forEach(flaggedDates::add);
			inventoryFlaggedDates.put(sProfile.getInventory(), flaggedDates);
		}

		Map<Inventory, List<LoadSlot>> newInventoryLoadSlots = new HashMap<>();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			newInventoryLoadSlots.put(sProfile.getInventory(), new LinkedList<>());
		}
		int idx = 0;
		
		Entry<LocalDate, InventoryDailyEvent> currentEntry = null;
		LoadSlot currentExistingSlot = null;
		
		Map<Inventory, Stack<LoadSlot>> inventoryTempSlots = new HashMap<>();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			inventoryTempSlots.put(sProfile.getInventory(), new Stack<>());
		}
		
		List<Pair<Inventory, Iterator<Entry<LocalDate, InventoryDailyEvent>>>> iterSwap = new LinkedList<>();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			iterSwap.add(new Pair<>(sProfile.getInventory(), inventoryInsAndOuts.get(sProfile.getInventory()).entrySet().iterator()));
		}
		
		Map<Inventory, Integer> inventoryRunningVolume = new HashMap<>();
		for (InventorySubprofile sProfile : profile.getInventories()) {
			inventoryRunningVolume.put(sProfile.getInventory(), 0);
		}
		
		final Map<Inventory, MULLContainer> mullMap = new HashMap<>();
		for (final InventorySubprofile sProfile : profile.getInventories()) {
			mullMap.put(sProfile.getInventory(), new MULLContainer(runningAllocation.get(sProfile.getInventory()), fullCargoLotValue, relativeEntitlements.get(sProfile.getInventory()), mudContainers.get(sProfile.getInventory())));
		}
		
		while (iterSwap.stream().allMatch(p -> p.getSecond().hasNext())) {
			for (Pair<Inventory, Iterator<Entry<LocalDate, InventoryDailyEvent>>> curr : iterSwap) {
				Inventory currentInventory = curr.getFirst();
				Iterator<Entry<LocalDate, InventoryDailyEvent>> currentIter = curr.getSecond();
				if (!currentIter.hasNext()) {
					continue;
				}
				final Entry<LocalDate, InventoryDailyEvent> entry = currentIter.next();
				final InventoryDailyEvent dailyEvent = entry.getValue();
				
//				final List<Pair<BaseLegalEntity, Double>> thisRelativeEntitlements = relativeEntitlements.get(currentInventory);
//				final Map<BaseLegalEntity, Long> thisRunningAllocation = runningAllocation.get(currentInventory);
				
//				final List<Pair<SalesContract, Double>> thisCapltaplSalesContractRelativeEntitlements = capltaplSalesContractRelativeEntitlements.get(currentInventory);
//				final Map<SalesContract, Long> thisCapltaplSalesContractRunningAllocation = capltaplSalesContractRunningAllocation.get(currentInventory);
//				final List<Pair<DESSalesMarket, Double>> thisCapltaplMarketRelativeEntitlements = capltaplMarketRelativeEntitlements.get(currentInventory);
//				final Map<DESSalesMarket, Long> thisCapltaplMarketRunningAllocation = capltaplMarketRunningAllocation.get(currentInventory);
				
//				final Map<BaseLegalEntity, List<Pair<SalesContract, Double>>> thisThirdPartySalesContractRelativeEntitlements = thirdPartySalesContractRelativeEntitlements.get(currentInventory);
//				final Map<BaseLegalEntity, Map<SalesContract, Long>> thisThirdPartySalesContractRunningAllocation = thirdPartySalesContractRunningAllocation.get(currentInventory);
//				final Map<BaseLegalEntity, List<Pair<DESSalesMarket, Double>>> thisThirdPartyMarketRelativeEntitlements = thirdPartyMarketRelativeEntitlements.get(currentInventory);
//				final Map<BaseLegalEntity, Map<DESSalesMarket, Long>> thisThirdPartyMarketRunningAllocation = thirdPartyMarketRunningAllocation.get(currentInventory);
				
				inventoryRunningVolume.compute(currentInventory, (k, vol) -> vol + dailyEvent.netVolumeIn);
				
				final MULLContainer currentMULLContainer = mullMap.get(currentInventory);
				currentMULLContainer.updateRunningAllocation(dailyEvent.netVolumeIn);
				
//				for (final Pair<BaseLegalEntity, Double> p : thisRelativeEntitlements) {
//					final Long additionalAllocation = ((Double) (dailyEvent.netVolumeIn*p.getSecond())).longValue();
//					thisRunningAllocation.compute(p.getFirst(), (k, v) -> {
//						return v + additionalAllocation;
//					});
//					BaseLegalEntity currentEntity = p.getFirst();
//					if (currentEntity.equals(capltaplEntity)) {
//						for (final Pair<SalesContract, Double> pp : thisCapltaplSalesContractRelativeEntitlements) {
//							thisCapltaplSalesContractRunningAllocation.compute(pp.getFirst(), (k, v) -> v+((Double) (additionalAllocation*pp.getSecond())).longValue());
//						}
//						for (final Pair<DESSalesMarket, Double> pp : thisCapltaplMarketRelativeEntitlements) {
//							thisCapltaplMarketRunningAllocation.compute(pp.getFirst(), (k, v)-> v+ ((Double) (additionalAllocation*pp.getSecond())).longValue());
//						}
//					} else {
//						for (final Pair<SalesContract, Double> pp : thisThirdPartySalesContractRelativeEntitlements.get(currentEntity)) {
//							thisThirdPartySalesContractRunningAllocation.get(currentEntity).compute(pp.getFirst(), (k, v) -> v+((Double) (additionalAllocation*pp.getSecond())).longValue());
//						}
//						for (final Pair<DESSalesMarket, Double> pp : thisThirdPartyMarketRelativeEntitlements.get(currentEntity)) {
//							thisThirdPartyMarketRunningAllocation.get(currentEntity).compute(pp.getFirst(), (k, v) -> v+ ((Double) (additionalAllocation*pp.getSecond())).longValue());
//						}
//					}
//				}
				
				final LocalDate currentDate = entry.getKey();
				
				if (currentDate.getDayOfMonth() == 1) {
					final YearMonth currentYM = YearMonth.from(currentDate);
					final int monthIn = inventoryInsAndOuts.get(currentInventory).entrySet().stream() //
						.filter(p -> YearMonth.from(p.getKey()).equals(currentYM)) //
						.mapToInt(p -> p.getValue().netVolumeIn) //
						.sum();
					currentMULLContainer.updateCurrentMonthAbsoluteEntitlement(monthIn);
				}
				
				while (currentExistingSlot != null && currentExistingSlot.getWindowStart().equals(currentDate)) {
					final BaseLegalEntity entity = currentExistingSlot.getEntity();
//					final Long entityAllocation = thisRunningAllocation.get(entity);
//					if (entityAllocation != null) {
//						thisRunningAllocation.put(entity, entityAllocation - currentExistingSlot.getMaxQuantity());
//					}
					currentMULLContainer.updateEntityRunningAllocation(currentExistingSlot);
					// currentExistingSlot = iterExistingLoads.hasNext() ? iterExistingLoads.next() : null;
				}
				Integer thisRunningVolume = inventoryRunningVolume.get(currentInventory);
				final List<LoadSlot> thisNewLoadSlots = newInventoryLoadSlots.get(currentInventory);
				final Stack<LoadSlot> thisTempSlots = inventoryTempSlots.get(currentInventory);
				while (thisRunningVolume < dailyEvent.minVolume && !thisNewLoadSlots.isEmpty()) {
					final LoadSlot oldLoadSlot = ((LinkedList<LoadSlot>) thisNewLoadSlots).removeLast();
					thisTempSlots.add(oldLoadSlot);
					thisRunningVolume += oldLoadSlot.getMaxQuantity();
//					thisRunningAllocation.compute(oldLoadSlot.getEntity(), (k, v) -> v + oldLoadSlot.getMaxQuantity());
					currentMULLContainer.reverseEntityLoadSlot(oldLoadSlot);
				}
				
//				assert currentMULLContainer.runningAllocationEqual(thisRunningAllocation);
				inventoryRunningVolume.put(currentInventory, thisRunningVolume);
				
				LocalDate expDate = LocalDate.of(2021, 9, 28);
				if (currentInventory.getName().equalsIgnoreCase("gorgon") && currentDate.getMonthValue() == 9) {
					int t = 0;
				}
				
				final BaseLegalEntity mullEntity2 = currentMULLContainer.calculateMull(vesselToMostRecentUseDate);
//				final Entry<BaseLegalEntity, Long> mullEntity = thisRunningAllocation.entrySet().stream() //
//						.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
				
//				if (!mullEntity2.equals(mullEntity.getKey())) {
//					int i = 0;
//				}
				// assert mullEntity2.equals(mullEntity.getKey());
				
				if (mullEntity2.equals(capltaplEntity)) {
//					SalesContract currentSalesContract = null;
//					DESSalesMarket currentMarket = null;
//					Vessel currentVessel = null;
//					List<Vessel> vessList = null;
					int allocationDrop;
					
					SalesContract currentSalesContract2 = null;
					DESSalesMarket currentMarket2 = null;
					Vessel currentVessel2 = null;
					List<Vessel> vessList2 = null;
					
					final Entry<SalesContract, Long> mudSalesContract = currentMULLContainer.getMUDSalesContract(capltaplEntity);
					final Entry<DESSalesMarket, Long> mudMarket = currentMULLContainer.getMUDMarket(capltaplEntity);
					
					if (mudSalesContract != null) {
						if (mudMarket != null) {
							if (mudSalesContract.getValue() > mudMarket.getValue()) {
								currentSalesContract2 = mudSalesContract.getKey();
								vessList2 = capltaplContractVesselLists.get(currentSalesContract2);
							} else {
								currentMarket2 = mudMarket.getKey();
								vessList2 = capltaplMarketVesselLists.get(currentMarket2);
							}
						} else {
							currentSalesContract2 = mudSalesContract.getKey();
							vessList2 = capltaplContractVesselLists.get(currentSalesContract2);
						}
					} else {
						currentMarket2 = mudMarket.getKey();
						vessList2 = capltaplMarketVesselLists.get(currentMarket2);
					}
					
//					final Entry<SalesContract, Long> mullSalesContract = thisCapltaplSalesContractRunningAllocation.entrySet().stream() //
//							.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
//					
//					final Entry<DESSalesMarket, Long> mullMarket = thisCapltaplMarketRunningAllocation.entrySet().stream() //
//							.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
//					
//					if (mullSalesContract.getValue() > mullMarket.getValue()) {
//						currentSalesContract = mullSalesContract.getKey();
//						vessList = capltaplContractVesselLists.get(currentSalesContract);
//						
//					} else {
//						currentMarket = mullMarket.getKey();
//						vessList = capltaplMarketVesselLists.get(currentMarket);
//					}
					
//					if (!vessList.isEmpty()) {
//						currentVessel = vessList.stream().min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2))).get();
//						allocationDrop = (int) (currentVessel.getVesselOrDelegateCapacity()*currentVessel.getVesselOrDelegateFillCapacity() - currentVessel.getVesselOrDelegateSafetyHeel());
//					} else {
//						allocationDrop = cargoVolume;
//					}
					
					if (!vessList2.isEmpty()) {
						currentVessel2 = vessList2.stream().min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2))).get();
						allocationDrop = (int) (currentVessel2.getVesselOrDelegateCapacity()*currentVessel2.getVesselOrDelegateFillCapacity() - currentVessel2.getVesselOrDelegateSafetyHeel());
					} else {
						allocationDrop = cargoVolume;
					}
					
//					assert (currentSalesContract == currentSalesContract2);
//					assert (currentMarket == currentMarket2);
					
					int tempRunningVolume = inventoryRunningVolume.get(currentInventory) - allocationDrop;
					final Set<LocalDate> thisFlaggedDates = inventoryFlaggedDates.get(currentInventory);
					if (tempRunningVolume > dailyEvent.minVolume && !thisFlaggedDates.contains(currentDate)) {
						final LoadSlot slot;
						if (thisTempSlots.isEmpty()) {
							final String nextName = currentInventory.getName() + "-" + Integer.toString(idx);
							slot = CargoFactory.eINSTANCE.createLoadSlot();
							slot.setPort(currentInventory.getPort());
							slot.setVolumeLimitsUnit(VolumeUnits.M3);
							slot.setMinQuantity(allocationDrop-volumeFlex);
							slot.setMaxQuantity(allocationDrop+volumeFlex);
							slot.setName(nextName);
							slot.setWindowSizeUnits(TimePeriod.DAYS);
							slot.setContract(inventoryPurchaseContracts.get(currentInventory));
							++idx;
						} else {
							slot = thisTempSlots.pop();
						}
						slot.setWindowStart(currentDate);
						if (!thisNewLoadSlots.isEmpty() && dynamicWindowGeneration) {
							LoadSlot previousLoadSlot = ((LinkedList<LoadSlot>) thisNewLoadSlots).getLast();
							final LocalDate previousDate = previousLoadSlot.getWindowStart();
							final long days = Days.between(previousDate, currentDate);
							final long actualDays = days + 1;
							final YearMonth currentYearMonth = YearMonth.from(previousDate);
							final LocalDate endWindowDate = currentDate.plusDays(actualDays);
							final LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
							if (lastDayOfMonth.isBefore(endWindowDate)) {
								previousLoadSlot.setWindowSize(Days.between(previousDate, lastDayOfMonth));
							} else {
								previousLoadSlot.setWindowSize((int) actualDays);
							}
						}
						final YearMonth currentYearMonth = YearMonth.from(currentDate);
						final LocalDate endWindowDate = currentDate.plusDays(loadWindow);
						final LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
						if (lastDayOfMonth.isBefore(endWindowDate)) {
							slot.setWindowSize(Days.between(currentDate, lastDayOfMonth));
						} else {
							slot.setWindowSize(loadWindow);
						}
						// mullEntity.setValue(mullEntity.getValue()-allocationDrop);
						currentMULLContainer.dropEntityAllocation(mullEntity2, allocationDrop);
						// slot.setEntity(mullEntity.getKey());
						slot.setEntity(mullEntity2);
						thisNewLoadSlots.add(slot);
						inventoryRunningVolume.put(currentInventory, tempRunningVolume);
//						runningVolume = tempRunningVolume;

						if (currentSalesContract2 != null) {
							slotToSalesContract.add(new Triple<>(slot, currentSalesContract2, currentVessel2));
							// mullSalesContract.setValue(mullSalesContract.getValue()-allocationDrop);
							currentMULLContainer.dropAllocation(mullEntity2, currentSalesContract2, (long) allocationDrop);
							
						} else {
							capltaplSlotToMarket.add(new Triple<>(slot, currentMarket2, currentVessel2));
							//mullMarket.setValue(mullMarket.getValue()-allocationDrop);
							currentMULLContainer.dropAllocation(mullEntity2, currentMarket2, (long) allocationDrop); 
						}
						if (currentVessel2 != null) {
							// capltaplSalesContractVesselIndices.compute(currentSalesContract, (k, i) -> (Integer) (i+1)%vessList.size());
							vesselToMostRecentUseDate.put(currentVessel2, currentDate);
						}
					}
				} else {
//					final BaseLegalEntity ent = mullEntity.getKey();
					final BaseLegalEntity ent = mullEntity2;
//					SalesContract currentSalesContract = null;
//					DESSalesMarket currentMarket = null;
//					Vessel currentVessel = null;
//					List<Vessel> vessList = null;
					int allocationDrop;
					
					SalesContract currentSalesContract2 = null;
					DESSalesMarket currentMarket2 = null;
					Vessel currentVessel2 = null;
					List<Vessel> vessList2 = null;
					
//					Map<SalesContract, Long> currentSalesContractRunningAllocations = thisThirdPartySalesContractRunningAllocation.get(ent);
//					Map<DESSalesMarket, Long> currentMarketRunningAllocations = thisThirdPartyMarketRunningAllocation.get(ent);
					
					final Entry<SalesContract, Long> mudSalesContract = currentMULLContainer.getMUDSalesContract(ent);
					final Entry<DESSalesMarket, Long> mudMarket = currentMULLContainer.getMUDMarket(ent);
					
					if (mudSalesContract != null) {
						if (mudMarket != null) {
							if (mudSalesContract.getValue() > mudMarket.getValue()) {
								currentSalesContract2 = mudSalesContract.getKey();
								vessList2 = thirdPartySalesContractVesselLists.get(currentInventory).get(ent).get(currentSalesContract2);
							} else {
								currentMarket2 = mudMarket.getKey();
								vessList2 = thirdPartyMarketVesselLists.get(currentInventory).get(ent).get(currentMarket2);
							}
						} else {
							currentSalesContract2 = mudSalesContract.getKey();
							vessList2 = thirdPartySalesContractVesselLists.get(currentInventory).get(ent).get(currentSalesContract2);
						}
					} else {
						currentMarket2 = mudMarket.getKey();
						vessList2 = thirdPartyMarketVesselLists.get(currentInventory).get(ent).get(currentMarket2);
					}
					
//					if (!currentSalesContractRunningAllocations.isEmpty()) {
//						final Entry<SalesContract, Long> mullSalesContract = currentSalesContractRunningAllocations.entrySet().stream() //
//								.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
//						if (!currentMarketRunningAllocations.isEmpty()) {
//							final Entry<DESSalesMarket, Long> mullMarket = currentMarketRunningAllocations.entrySet().stream() //
//									.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
//							if (mullSalesContract.getValue() > mullMarket.getValue()) {
//								currentSalesContract = mullSalesContract.getKey();
//								vessList = thirdPartySalesContractVesselLists.get(currentInventory).get(ent).get(currentSalesContract);
//							} else {
//								currentMarket = mullMarket.getKey();
//								vessList = thirdPartyMarketVesselLists.get(currentInventory).get(ent).get(currentMarket);
//							}
//						} else {
//							currentSalesContract = mullSalesContract.getKey();
//							vessList = thirdPartySalesContractVesselLists.get(currentInventory).get(ent).get(currentSalesContract);
//						}
//					} else {
//						final Entry<DESSalesMarket, Long> mullMarket = currentMarketRunningAllocations.entrySet().stream() //
//								.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
//						currentMarket = mullMarket.getKey();
//						vessList = thirdPartyMarketVesselLists.get(currentInventory).get(ent).get(currentMarket);
//					}
					
//					if (!vessList.isEmpty()) {
//						currentVessel = vessList.stream().min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2))).get();
//						allocationDrop = (int) (currentVessel.getVesselOrDelegateCapacity()*currentVessel.getVesselOrDelegateFillCapacity() - currentVessel.getVesselOrDelegateSafetyHeel());
//					} else {
//						allocationDrop = cargoVolume;
//					}
					
					if (!vessList2.isEmpty()) {
						currentVessel2 = vessList2.stream().min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2))).get();
						allocationDrop = (int) (currentVessel2.getVesselOrDelegateCapacity()*currentVessel2.getVesselOrDelegateFillCapacity() - currentVessel2.getVesselOrDelegateSafetyHeel());
					} else {
						allocationDrop = cargoVolume;
					}
					
//					assert (currentSalesContract == currentSalesContract2);
//					assert (currentMarket == currentMarket2);
					
					int tempRunningVolume = inventoryRunningVolume.get(currentInventory) - allocationDrop;
					final Set<LocalDate> thisFlaggedDates = inventoryFlaggedDates.get(currentInventory);
					if (tempRunningVolume > dailyEvent.minVolume && !thisFlaggedDates.contains(currentDate)) {
						final LoadSlot slot;
						if (thisTempSlots.isEmpty()) {
							final String nextName = currentInventory.getName() + "-" + Integer.toString(idx);
							slot = CargoFactory.eINSTANCE.createLoadSlot();
							slot.setPort(currentInventory.getPort());
							slot.setVolumeLimitsUnit(VolumeUnits.M3);
							slot.setMinQuantity(allocationDrop-volumeFlex);
							slot.setMaxQuantity(allocationDrop+volumeFlex);
							slot.setName(nextName);
							// slot.setWindowSize(loadWindow);
							slot.setWindowSizeUnits(TimePeriod.DAYS);
							slot.setContract(inventoryPurchaseContracts.get(currentInventory));
							++idx;
						} else {
							slot = thisTempSlots.pop();
						}
						slot.setWindowStart(currentDate);
//						if (!thisNewLoadSlots.isEmpty() && dynamicWindowGeneration) {
//							LoadSlot previousLoadSlot = ((LinkedList<LoadSlot>) thisNewLoadSlots).getLast();
//							final long days = ChronoUnit.DAYS.between(previousLoadSlot.getWindowStart(), currentDate);
//							previousLoadSlot.setWindowSize((int) days + 1);
//						}
						if (!thisNewLoadSlots.isEmpty() && dynamicWindowGeneration) {
							LoadSlot previousLoadSlot = ((LinkedList<LoadSlot>) thisNewLoadSlots).getLast();
							final LocalDate previousDate = previousLoadSlot.getWindowStart();
							final long days = Days.between(previousDate, currentDate);
							final long actualDays = days + 1;
							final YearMonth currentYearMonth = YearMonth.from(previousDate);
							final LocalDate endWindowDate = currentDate.plusDays(actualDays);
							final LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
							if (lastDayOfMonth.isBefore(endWindowDate)) {
								previousLoadSlot.setWindowSize(Days.between(previousDate, lastDayOfMonth));
							} else {
								previousLoadSlot.setWindowSize((int) actualDays);
							}
						}
						final YearMonth currentYearMonth = YearMonth.from(currentDate);
						final LocalDate endWindowDate = currentDate.plusDays(loadWindow);
						final LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();
						if (lastDayOfMonth.isBefore(endWindowDate)) {
							slot.setWindowSize(Days.between(currentDate, lastDayOfMonth));
						} else {
							slot.setWindowSize(loadWindow);
						}
//						mullEntity.setValue(mullEntity.getValue()-allocationDrop);
						currentMULLContainer.dropEntityAllocation(mullEntity2, allocationDrop);
						// slot.setEntity(mullEntity.getKey());
						slot.setEntity(mullEntity2);
						thisNewLoadSlots.add(slot);
						inventoryRunningVolume.put(currentInventory, tempRunningVolume);
						if (ent.getName().equalsIgnoreCase("kuf")) {
							int i = 0;
						}
						if (currentSalesContract2 != null) {
							if (woodsideDES && mullEntity2.getName().equals("Woodside") && currentSalesContract2.getName().equals("TED1")) {
								slot.setDESPurchase(true);
							}
							slotToThirdPartySalesContract.add(new Triple<>(slot, currentSalesContract2, currentVessel2));
//							currentSalesContractRunningAllocations.compute(currentSalesContract, (k, v) -> v -allocationDrop);
							currentMULLContainer.dropAllocation(mullEntity2, currentSalesContract2, (long) allocationDrop);
						} else {
							if (nonCaplTaplVesselSharingEntities.contains(mullEntity2)) {
								slotToFOBSaleVessels.add(new Triple<>(slot, currentMarket2, currentVessel2));
							} else {
								slotToMarketVessels.add(new Triple<>(slot, currentMarket2, currentVessel2));
							}
//							currentMarketRunningAllocations.compute(currentMarket, (k, v) -> v - allocationDrop);
							currentMULLContainer.dropAllocation(mullEntity2, currentMarket2, (long) allocationDrop);
						}
						
						if (currentVessel2 != null) {
							// thirdPartyVesselIndex.put(ent, (thirdPartyVesselIndex.get(ent)+1)%thirdPartyVesselList.get(ent).size());
							vesselToMostRecentUseDate.put(currentVessel2, currentDate);
						}
//						if (nonCaplTaplVesselSharingEntities.contains(mullEntity.getKey())) {
//							slotToFOBSaleVessels.add(new Triple<>(slot, thirdPartyMarketAllocs.get(ent).get(0).getMarket(), currentVessel));
//						} else {
//							slotToMarketVessels.add(new Triple<>(slot, thirdPartyMarketAllocs.get(ent).get(0).getMarket(), currentVessel));
//						}
					}
				}
//				currentMULLContainer.runningAllocationEqual(thisRunningAllocation);
			}
		}
		if (newInventoryLoadSlots.values().stream().anyMatch(c-> !c.isEmpty())) {
			
			// final List<LoadSlot> loadSlotsToAdd = new LinkedList<>();
			
			final List<Command> dischargeSlotSetCommands = new LinkedList<>();
			final List<Command> cargoSetCommands = new LinkedList<>();
			
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
			for(List<LoadSlot> newSlots : newInventoryLoadSlots.values()) {
				if (!newSlots.isEmpty()) {
					cmd.append(AddCommand.create(ed, cargoModel, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, newSlots));
				}
			}
			for (Triple<LoadSlot, DESSalesMarket, Vessel> t : capltaplSlotToMarket) {
				final LoadSlot currentLoadSlot = t.getFirst();
				final DESSalesMarket currentMarket = t.getSecond();
				final Vessel currentVessel = t.getThird();
				
				final DischargeSlot currentDischargeSlot = buildMarketDischargeSlot(cec, dischargeSlotSetCommands, cargoModel, currentMarket, currentLoadSlot, currentVessel, capltaplMarketVesselLists.get(currentMarket), "chev-spot-sale-");
				
				final Cargo currentCargo = CargoEditingCommands.createNewCargo(ed, cargoSetCommands, cargoModel, null, 0);
				currentLoadSlot.setCargo(currentCargo);
				currentDischargeSlot.setCargo(currentCargo);
				
				if (currentVessel != null) {
					final VesselAvailability currentVesselVA = vessToVA.get(currentVessel);
					if (currentVesselVA != null) {
						currentCargo.setVesselAssignmentType(currentVesselVA);
					}
				}
			}
			for (Triple<LoadSlot, DESSalesMarket, Vessel> t : slotToMarketVessels) {
				LoadSlot currentLoadSlot = t.getFirst();
				DESSalesMarket currentMarket = t.getSecond();
				Vessel currentVessel = t.getThird();
				
				final DischargeSlot currentDischargeSlot = buildMarketDischargeSlot(cec, dischargeSlotSetCommands, cargoModel, currentMarket, currentLoadSlot, currentVessel, thirdPartyVesselList.get(currentLoadSlot.getEntity()), "spot-sale-");

				final Cargo currentCargo = CargoEditingCommands.createNewCargo(ed, cargoSetCommands, cargoModel, null, 0);
				currentLoadSlot.setCargo(currentCargo);
				currentDischargeSlot.setCargo(currentCargo);
				
				if (currentVessel != null) {
					final VesselAvailability currentVesselVA = vessToVA.get(currentVessel);
					if (currentVesselVA != null) {
						currentCargo.setAllowRewiring(false);
						currentCargo.setVesselAssignmentType(currentVesselVA);
					}
				}
			}
			for (Triple<LoadSlot, DESSalesMarket, Vessel> t : slotToFOBSaleVessels) {
				LoadSlot currentLoadSlot = t.getFirst();
				Vessel currentVessel = t.getThird();
				
				final DischargeSlot currentDischargeSlot = buildFOBSaleDischargeSlot(cec, dischargeSlotSetCommands, cargoModel, currentLoadSlot, currentVessel, "fob-sale-");
				// last parameter should be unused since cim is null
				
				final Cargo currentCargo = CargoEditingCommands.createNewCargo(ed, cargoSetCommands, cargoModel, null, 0);
				currentLoadSlot.setCargo(currentCargo);
				currentDischargeSlot.setCargo(currentCargo);
				currentCargo.setAllowRewiring(false);
			}
			for (Triple<LoadSlot, SalesContract, Vessel> t : slotToSalesContract) {
				final LoadSlot currentLoadSlot = t.getFirst();
				final SalesContract currentSalesContract = t.getSecond();
				final Vessel currentVessel = t.getThird();
				
				final DischargeSlot currentDischargeSlot = buildDESSaleDischargeSlot(cec, dischargeSlotSetCommands, cargoModel, currentSalesContract, currentLoadSlot, currentVessel, null, "chev-contract-");
				
				final Cargo currentCargo = CargoEditingCommands.createNewCargo(ed, cargoSetCommands, cargoModel, null, 0);
				currentLoadSlot.setCargo(currentCargo);
				currentDischargeSlot.setCargo(currentCargo);
				
				if (currentVessel != null) {
					final VesselAvailability currentVesselVA = vessToVA.get(currentVessel);
					if (currentVesselVA != null) {
						currentCargo.setVesselAssignmentType(currentVesselVA);
					}
				}
			}
			for (Triple<LoadSlot, SalesContract, Vessel> t : slotToThirdPartySalesContract) {
				final List<Command> setCommands = new LinkedList<>();
				final List<EObject> deleteObjects = new LinkedList<>();
				final LoadSlot currentLoadSlot = t.getFirst();
				final SalesContract currentSalesContract = t.getSecond();
				final Vessel currentVessel = t.getThird();
				
				final DischargeSlot currentDischargeSlot = buildDESSaleDischargeSlot(cec, dischargeSlotSetCommands, cargoModel, currentSalesContract, currentLoadSlot, currentVessel, null, "third-party-contract-");
				
				final Cargo currentCargo = CargoEditingCommands.createNewCargo(ed, cargoSetCommands, cargoModel, null, 0);
				currentLoadSlot.setCargo(currentCargo);
				currentDischargeSlot.setCargo(currentCargo);
				currentCargo.setAllowRewiring(false);
				
				if (currentVessel != null) {
					final VesselAvailability currentVesselVA = vessToVA.get(currentVessel);
					if (currentVesselVA != null) {
						currentCargo.setVesselAssignmentType(currentVesselVA);
					}
				}
			}
			int i = 0;
			dischargeSlotSetCommands.forEach(cmd::append);
			cargoSetCommands.forEach(cmd::append);
			editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
			cec.verifyCargoModel(ScenarioModelUtil.getCargoModel(sm));
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
			
			MultipleInventoryProfile profile = adpModel.getMultipleInventoriesProfile();
			if (profile == null) {
				profile = ADPFactory.eINSTANCE.createMultipleInventoryProfile();
			}
			objects.add(profile);
			objects.addAll(cargoModel.getInventoryModels());
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
		} else if (object instanceof Inventory) {
			final Inventory inventory = (Inventory) object;
			for (final InventoryProfile profile : editorData.adpModel.getInventoryProfiles()) {
				if (profile.getInventory() == inventory) {
					target = profile;
					break;
				}
			}
			if (target == null) {
				final InventoryProfile profile = ADPFactory.eINSTANCE.createInventoryProfile();
				target = profile;
				if (target != null) {
					final CompoundCommand cmd = new CompoundCommand("Create ADP Inventory Profile");
					cmd.append(AddCommand.create(editorData.getEditingDomain(), editorData.adpModel, ADPPackage.eINSTANCE.getADPModel_InventoryProfiles(), Collections.singletonList(profile)));
					editorData.getEditingDomain().getCommandStack().execute(cmd);
				}
				profile.setInventory(inventory);
			}
		} else if (object instanceof MultipleInventoryProfile) {
			if (editorData.adpModel.getMultipleInventoriesProfile() == null) {
				//final CompoundCommand cmd = new CompoundCommand("Create ADP Multiple Inventory Profile");
				editorData.adpModel.setMultipleInventoriesProfile((MultipleInventoryProfile) object);
				//cmd.append(SetCommand.create(editorData.getEditingDomain(), editorData.adpModel, ADPPackage.eINSTANCE.getADPModel_MultipleInventoriesProfile(), target));
				// editorData.getEditingDomain().getCommandStack().execute(cmd);
			}
			
			target = object;
		}

		detailComposite.setInput(target);
		updatePreviewPaneInput(target);
	}

	private void updatePreviewPaneInput(final EObject target) {
		if (editorData != null && editorData.getScenarioModel() != null) {
			ADPModel adpModel = editorData.adpModel;
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
				} else if (target instanceof InventoryProfile) {
					final InventoryProfile inventoryProfile = (InventoryProfile) target;
					final List<Object> o = new LinkedList<>();
					o.addAll(inventoryProfile.getGeneratedSlots());
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
	
	// Returns a tree map where each key value pair represents the date of some activity and the sum total of gas moved
	private TreeMap<LocalDate, InventoryDailyEvent> getInventoryInsAndOuts(Inventory inventory, LNGScenarioModel scenarioModel) {
		List<InventoryCapacityRow> capacities = inventory.getCapacities();
		
		TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap = 
				capacities.stream()
				.collect(Collectors.toMap(InventoryCapacityRow::getDate,
							c -> c,
							(oldValue, newValue) -> newValue,
							TreeMap::new));
		
		TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = new TreeMap<>();
		
		// add all feeds to map
		List<InventoryEventRow> feeds = inventory.getFeeds();
		int feedSize = feeds.size();
		addNetVolumes(inventory.getFeeds(), capcityTreeMap, insAndOuts, Function.identity());
		List<InventoryEventRow> offtakes = inventory.getOfftakes();
		int i = offtakes.size();
		addNetVolumes(inventory.getOfftakes(), capcityTreeMap, insAndOuts, a -> -a);
		Port inventoryPort = inventory.getPort();
		scenarioModel.getCargoModel().getLoadSlots().forEach(s -> {
			if (inventoryPort.equals(s.getPort())) {
				InventoryDailyEvent inventoryDailyEvent = insAndOuts.get(s.getWindowStart());
				if (inventoryDailyEvent == null) {
					inventoryDailyEvent = new InventoryDailyEvent();
					inventoryDailyEvent.date = s.getWindowStart();
					InventoryCapacityRow capacityRow = capcityTreeMap.get(s.getWindowStart()) == null //
							? capcityTreeMap.lowerEntry(s.getWindowStart()).getValue() //
							: capcityTreeMap.get(s.getWindowStart());
					inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
					inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
					insAndOuts.put(s.getWindowStart(), inventoryDailyEvent);
				}
				inventoryDailyEvent.addVolume(-s.getMaxQuantity());
			}
		});
		return insAndOuts;
	}
	
	private void addNetVolumes(List<InventoryEventRow> events, TreeMap<LocalDate, InventoryCapacityRow> capcityTreeMap, TreeMap<LocalDate, InventoryDailyEvent> insAndOuts, Function<Integer, Integer> volumeFunction) {
		for (InventoryEventRow inventoryEventRow : events) {
			if (inventoryEventRow.getStartDate() != null) {
				InventoryDailyEvent inventoryDailyEvent = insAndOuts.get(inventoryEventRow.getStartDate());
				if (inventoryDailyEvent == null) {
					inventoryDailyEvent = new InventoryDailyEvent();
					inventoryDailyEvent.date = inventoryEventRow.getStartDate();
					InventoryCapacityRow capacityRow = capcityTreeMap.get(inventoryDailyEvent.date) == null //
							? capcityTreeMap.lowerEntry(inventoryDailyEvent.date).getValue() //
							: capcityTreeMap.get(inventoryDailyEvent.date);
					inventoryDailyEvent.minVolume = capacityRow.getMinVolume();
					inventoryDailyEvent.maxVolume = capacityRow.getMaxVolume();
					insAndOuts.put(inventoryEventRow.getStartDate(), inventoryDailyEvent);
				}
				inventoryDailyEvent.addVolume(volumeFunction.apply(inventoryEventRow.getReliableVolume()));
			}
		}
	}
	
	private DischargeSlot buildCapltaplMarketDischargeSlot(CargoEditingCommands cec, List<Command> setCommands, CargoModel cargoModel, DESSalesMarket market, LoadSlot loadSlotToMatch, Vessel modelVessel, List<Vessel> vesselRestrictions) {
		DischargeSlot dSlot = cec.createNewSpotDischarge(setCommands, cargoModel, market);
		IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
		final int travelTime = CargoEditorMenuHelper.getTravelTime(loadSlotToMatch, dSlot, modelVessel, sdp);
		if (travelTime == Integer.MAX_VALUE) {
			final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", loadSlotToMatch.getPort().getName(),
					dSlot.getPort().getName(), travelTime);
			throw new RuntimeException(message);
		}
		
		final SchedulingTimeWindow loadSTW = loadSlotToMatch.getSchedulingTimeWindow();
		final LocalDate dischargeDate = loadSTW.getStart().plusHours(travelTime + loadSTW.getDuration()).withDayOfMonth(1).withHour(0).toLocalDate();
		dSlot.setWindowStart(dischargeDate);
		dSlot.setWindowStartTime(0);
		
		dSlot.setWindowSize(1);
		dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
		dSlot.setRestrictedVesselsArePermissive(true);
		dSlot.setRestrictedVesselsOverride(true);
		dSlot.getRestrictedVessels().addAll(vesselRestrictions);
		
		String id = "chev-spot-sale-" + loadSlotToMatch.getName();
		
		dSlot.setName(id);
		return dSlot;
	}
	
	private DischargeSlot buildMarketDischargeSlot(CargoEditingCommands cec, List<Command> setCommands, CargoModel cargoModel, DESSalesMarket market, LoadSlot loadSlotToMatch, Vessel modelVessel, List<Vessel> vesselRestrictions, String namePrefix) {
		DischargeSlot dSlot = cec.createNewSpotDischarge(setCommands, cargoModel, market);
		
		final LocalDate dischargeDate = calculateDischargeDate(loadSlotToMatch, dSlot, modelVessel);
		dSlot.setWindowStart(dischargeDate);
		dSlot.setWindowStartTime(0);
		
		dSlot.setWindowSize(1);
		dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
		dSlot.setRestrictedVesselsArePermissive(true);
		dSlot.setRestrictedVesselsOverride(true);
		dSlot.getRestrictedVessels().addAll(vesselRestrictions);
		
		String id = namePrefix + loadSlotToMatch.getName();
		
		dSlot.setName(id);
		return dSlot;
	}
	
	private DischargeSlot buildFOBSaleDischargeSlot(CargoEditingCommands cec, List<Command> setCommands, CargoModel cargoModel, LoadSlot loadSlotToMatch, Vessel assignedVessel, String namePrefix) {
		DischargeSlot dSlot = cec.createNewDischarge(setCommands, cargoModel, true);
		dSlot.setWindowStart(loadSlotToMatch.getWindowStart());
		dSlot.setWindowStartTime(0);
		
		dSlot.setWindowSize(1);
		dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
		
		String id = namePrefix + loadSlotToMatch.getName();
		
		dSlot.setPriceExpression("JKM");
		dSlot.setPort(loadSlotToMatch.getPort());
		dSlot.setFobSaleDealType(FOBSaleDealType.SOURCE_ONLY);
		dSlot.setEntity(loadSlotToMatch.getEntity());
		dSlot.setNominatedVessel(assignedVessel);
		
		dSlot.setName(id);
		return dSlot;
	}
	
	private DischargeSlot buildDESSaleDischargeSlot(CargoEditingCommands cec, List<Command> setCommands, CargoModel cargoModel, SalesContract salesContract, LoadSlot loadSlotToMatch, Vessel modelVessel, List<Vessel> vesselRestrictions, String namePrefix) {
		final DischargeSlot dSlot = cec.createNewDischarge(setCommands, cargoModel, false);
		dSlot.setContract(salesContract);
		dSlot.setName(namePrefix+loadSlotToMatch.getName());
		dSlot.setEntity(loadSlotToMatch.getEntity());
		dSlot.setPort(salesContract.getPreferredPort());
		
		final SchedulingTimeWindow loadSTW = loadSlotToMatch.getSchedulingTimeWindow();
		final LocalDate dischargeDate = calculateDischargeDate(loadSlotToMatch, dSlot, modelVessel);
		dSlot.setWindowStart(dischargeDate);
		dSlot.setWindowStartTime(0);
		
		dSlot.setWindowSize(1);
		dSlot.setWindowSizeUnits(TimePeriod.MONTHS);
		
		if (vesselRestrictions != null) {
			dSlot.setRestrictedVesselsArePermissive(true);
			dSlot.setRestrictedVesselsOverride(true);
			dSlot.getRestrictedVessels().addAll(vesselRestrictions);
		}
		
		String id = namePrefix + loadSlotToMatch.getName();
		dSlot.setName(id);
		return dSlot;
	}
	
	private LocalDate calculateDischargeDate(LoadSlot loadSlot, DischargeSlot dischargeSlot, Vessel vessel) {
		final IScenarioDataProvider sdp = editorData.getScenarioDataProvider();
		final int travelTime = CargoEditorMenuHelper.getTravelTime(loadSlot, dischargeSlot, vessel, sdp);
		if (travelTime == Integer.MAX_VALUE) {
			final String message = String.format("Can not determine travel time between %s and %s. \n Travel time can not be %d hours.", loadSlot.getPort().getName(),
					dischargeSlot.getPort().getName(), travelTime);
			throw new RuntimeException(message);
		}
		final SchedulingTimeWindow loadSTW = loadSlot.getSchedulingTimeWindow();
		return loadSTW.getStart().plusHours(travelTime + (long) loadSTW.getDuration()).withDayOfMonth(1).withHour(0).toLocalDate();
	}
}
