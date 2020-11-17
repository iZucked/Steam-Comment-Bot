/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
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
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.AllocationElement;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.InventoryProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.RelativeEntitlementElement;
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.ecore.SafeEContentAdapter;

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
						if (element instanceof Inventory) {
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
						
						generateButton.setEnabled(target instanceof Contract || target instanceof Inventory);
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
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");
							final List<RelativeEntitlementElement> relativeEntitlements = profile.getRelativeEntitlements();
							final List<AllocationElement> initialAllocations = profile.getInitialAllocations();
							// Map<String,BaseLegalEntity> entityMap = new HashMap<>();
//							final Map<String, Pair<Long, Double>> entityTable = new HashMap<>();
//							if (profile.getInventory().getName().equals("Gorgon")) {
//								entityTable.put("CAPL/TAPL", new Pair<>(0L, 0.47333));
//								entityTable.put("Exxon", new Pair<>(75759L, 0.25));
//								entityTable.put("Shell", new Pair<>(38073L, 0.25));
//								entityTable.put("OGG", new Pair<>(-61282L, 0.0125));
//								entityTable.put("TGG", new Pair<>(1724L, 0.01));
//								entityTable.put("JERA", new Pair<>(-26809L, 0.00417));
//							} else if (profile.getInventory().getName().equals("Wheatstone")) {
//								entityTable.put("CAPL/TAPL", new Pair<>(0L, 0.642905203));
//								entityTable.put("Woodside", new Pair<>(0L, 0.131410260));
//								entityTable.put("PEW", new Pair<>(0L, 0.078696752));
//								entityTable.put("KUF", new Pair<>(0L, 0.136076266));
//								entityTable.put("QEW", new Pair<>(0L, 0.01091120));
//							} else {
//								throw new IllegalStateException("Not Implemented Error");
//							}
							for (final InventoryADPEntityRow row : profile.getEntityTable()) {
								final AllocationElement allocElem = ADPFactory.eINSTANCE.createAllocationElement();
								allocElem.setEntity(row.getEntity());
								allocElem.setAllocation(Integer.parseInt(row.getInitialAllocation()));
								initialAllocations.add(allocElem);
								final RelativeEntitlementElement reElem = ADPFactory.eINSTANCE.createRelativeEntitlementElement();
								reElem.setEntity(row.getEntity());
								reElem.setEntitlement(row.getRelativeEntitlement());
								relativeEntitlements.add(reElem);
							}
//							for (final BaseLegalEntity entity : editorData.scenarioModel.getReferenceModel().getCommercialModel().getEntities()) {
//								final Pair<Long, Double> pair = entityTable.get(entity.getName());
//								if (pair != null) {
//									final AllocationElement allocElem = ADPFactory.eINSTANCE.createAllocationElement();
//									allocElem.setEntity(entity);
//									allocElem.setAllocation(pair.getFirst().intValue());
//									initialAllocations.add(allocElem);
//									final RelativeEntitlementElement reElem = ADPFactory.eINSTANCE.createRelativeEntitlementElement();
//									reElem.setEntity(entity);
//									reElem.setEntitlement(pair.getSecond());
//									relativeEntitlements.add(reElem);
//								}
//							}
//							assert initialAllocations.size() == entityTable.size();
//							assert relativeEntitlements.size() == entityTable.size();
							
							final Command populateModelCommand = populateModelFromInventory(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							cmd.append(populateModelCommand);
							editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
							input = profile.getInventory();
						}
						updateDetailPaneInput(input);
					}
				});
			}
			
			{
				generateFromInventoryButton = new Button(toolbarComposite, SWT.PUSH);
				generateFromInventoryButton.setText("Generate slots from inventory");
				generateFromInventoryButton.setEnabled(true);
				generateFromInventoryButton.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final LNGScenarioModel scenarioModel = editorData.getScenarioModel();
						final List<Inventory> inventoryModels = scenarioModel.getCargoModel().getInventoryModels();
						final LocalDate startDate = editorData.adpModel.getYearStart().atDay(1);
						final List<LoadSlot> newCmdLoadSlots = new LinkedList<>();
						final CompoundCommand cmd = new CompoundCommand("Generate inventory based ADP slots");
						for (final Inventory currentInventory : inventoryModels) {
							final List<LoadSlot> newLoadSlots = new LinkedList<>();
							final Optional<PurchaseContract> pcFetch = ScenarioModelUtil.getCommercialModel(scenarioModel).getPurchaseContracts().stream() //
									.filter(c -> currentInventory.getPort().equals(c.getPreferredPort())) //
									.findFirst();
							assert pcFetch.isPresent();
							final PurchaseContract chosenPurchaseContract = pcFetch.get();
						
							final TreeMap<LocalDate, InventoryDailyEvent> insAndOuts = getInventoryInsAndOuts(currentInventory, scenarioModel);
						
							int totalInventoryVolume = 0;
							// int globalLoadTrigger = 158000;
							
							
							int cargoVolume = 160000;
							BaseLegalEntity[] entities = null;
							Map<BaseLegalEntity, Long> runningAllocation = new HashMap<>();
							final List<Pair<BaseLegalEntity, Double>> relativeEntitlements = new ArrayList<>();
							if (currentInventory.getName().equals("Gorgon")) {
								BaseLegalEntity caplTapl = null;
								BaseLegalEntity exxon = null;
								BaseLegalEntity shell = null;
								BaseLegalEntity ogg = null;
								BaseLegalEntity tgg = null;
								BaseLegalEntity jera = null;
								for (BaseLegalEntity entity : scenarioModel.getReferenceModel().getCommercialModel().getEntities()) {
									String entityName = entity.getName();
									if (entityName.equals("CAPL/TAPL")) {
										caplTapl = entity;
										runningAllocation.put(entity, 0L);
										relativeEntitlements.add(new Pair<>(entity, 0.47333));
									} else if (entityName.equals("Exxon")) {
										exxon = entity;
										runningAllocation.put(entity, 75759L);
										relativeEntitlements.add(new Pair<>(entity, 0.25));
									} else if (entityName.equals("Shell")) {
										shell = entity;
										runningAllocation.put(entity, 38073L);
										relativeEntitlements.add(new Pair<>(entity, 0.25));
									} else if (entityName.equals("OGG")) {
										ogg = entity;
										runningAllocation.put(entity, -61282L);
										relativeEntitlements.add(new Pair<>(entity, 0.0125));
									} else if (entityName.equals("TGG")) {
										tgg = entity;
										runningAllocation.put(entity, 1724L);
										relativeEntitlements.add(new Pair<>(entity, 0.01));
									} else if (entityName.equals("JERA")) {
										jera = entity;
										runningAllocation.put(entity, -26809L);
										relativeEntitlements.add(new Pair<>(entity, 0.00417));
									}
								}
								entities = new BaseLegalEntity[]{caplTapl, exxon, shell, ogg, tgg, jera};
							} else if (currentInventory.getName().equals("Wheatstone")) {
								BaseLegalEntity caplTapl = null;
								BaseLegalEntity woodside = null;
								BaseLegalEntity pew = null;
								BaseLegalEntity kuf = null;
								BaseLegalEntity qew = null;
								for (BaseLegalEntity entity : scenarioModel.getReferenceModel().getCommercialModel().getEntities()) {
									String entityName = entity.getName();
									if (entityName.equals("CAPL/TAPL")) {
										caplTapl = entity;
										runningAllocation.put(entity, 0L);
										relativeEntitlements.add(new Pair<>(entity, 0.642905203));
									} else if (entityName.equals("Woodside")) {
										woodside = entity;
										runningAllocation.put(entity, 0L);
										relativeEntitlements.add(new Pair<>(entity, 0.131410260));
									} else if (entityName.equals("PEW")) {
										pew = entity;
										runningAllocation.put(entity, 0L);
										relativeEntitlements.add(new Pair<>(entity, 0.078696752));
									} else if (entityName.equals("KUF")) {
										kuf = entity;
										runningAllocation.put(entity, 0L);
										relativeEntitlements.add(new Pair<>(entity, 0.136076266));
									} else if (entityName.equals("QEW")) {
										qew = entity;
										runningAllocation.put(entity, 0L);
										relativeEntitlements.add(new Pair<>(entity, 0.01091120));
									}
								}
								entities = new BaseLegalEntity[]{caplTapl, woodside, pew, kuf, qew};
							}
							assert entities != null;
							for (BaseLegalEntity entity : entities) {
								assert entity != null;
							}
							assert entities.length == relativeEntitlements.size();

							while (insAndOuts.firstKey().isBefore(startDate)) {
								final InventoryDailyEvent event = insAndOuts.remove(insAndOuts.firstKey());
								totalInventoryVolume += event.netVolumeIn;
							}
							insAndOuts.firstEntry().getValue().addVolume(totalInventoryVolume);
							Port inventoryPort = currentInventory.getPort();
							Set<LocalDate> flaggedDates = new HashSet<>();
							scenarioModel.getCargoModel().getLoadSlots().stream() //
									.forEach(s -> {
										if (s.getPort().equals(inventoryPort)) {
											flaggedDates.add(s.getWindowStart());
										}
									});
							
							List<LoadSlot> existingLoads = scenarioModel.getCargoModel().getLoadSlots().stream() //
									.filter(s -> s.getPort().equals(inventoryPort)) //
									.sorted((s1, s2) -> s1.getWindowStart().compareTo(s2.getWindowStart())) //
									.collect(Collectors.toList());

							//final List<LocalDate> loadDates = new LinkedList<>();
							
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
									runningAllocation.compute(p.getFirst(), (k, v) -> {
										Double change = dailyEvent.netVolumeIn*p.getSecond();
										return v + change.longValue();
									});
								}
								
								final LocalDate currentDate = entry.getKey();
								while (currentExistingSlot != null && currentExistingSlot.getWindowStart().equals(currentDate)) {
									final BaseLegalEntity entity = currentExistingSlot.getEntity();
									Long entityAllocation = runningAllocation.get(entity);
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
								int tempRunningVolume = runningVolume - cargoVolume;
								if (tempRunningVolume > dailyEvent.minVolume && !flaggedDates.contains(currentDate)) {
									final LoadSlot slot;
									if (tempSlots.isEmpty()) {
										final String nextName = currentInventory.getName()+"-"+Integer.toString(idx);
										slot = CargoFactory.eINSTANCE.createLoadSlot();
										slot.setPort(currentInventory.getPort());
										slot.setVolumeLimitsUnit(VolumeUnits.M3);
										slot.setMinQuantity(cargoVolume);
										slot.setMaxQuantity(cargoVolume);
										slot.setName(nextName);
										slot.setWindowSize(3);
										slot.setWindowSizeUnits(TimePeriod.DAYS);
										slot.setContract(chosenPurchaseContract);
										++idx;
									} else {
										slot = tempSlots.pop();
									}
									slot.setWindowStart(currentDate);
									final Entry<BaseLegalEntity, Long> mullEntity = runningAllocation.entrySet().stream() //
											.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
								
									mullEntity.setValue(mullEntity.getValue()-cargoVolume);
									slot.setEntity(mullEntity.getKey());
									newLoadSlots.add(slot);
									runningVolume = tempRunningVolume;
								}
							}
							newCmdLoadSlots.addAll(newLoadSlots);
						}
						if (!newCmdLoadSlots.isEmpty()) {
							cmd.append(AddCommand.create(editorData.getEditingDomain(), ScenarioModelUtil.getCargoModel(scenarioModel), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, newCmdLoadSlots));
							editorData.getDefaultCommandHandler().handleCommand(cmd, null, null);
						//	int i = 0;
						//updateDetailPaneInput(object);
						}
						
						
//						CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
//						final CompoundCommand cmd = new CompoundCommand("Generate inventory based ADP slots");
//						final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
//						final BundleContext bundleContext = bundle.getBundleContext();
//						Collection<ServiceReference<IProfileGenerator>> serviceReferences;
//						try {
//							serviceReferences = bundleContext.getServiceReferences(IProfileGenerator.class, null);
//							final List<IProfileGenerator> generators = new LinkedList<>();
//							
//							for (final ServiceReference<IProfileGenerator> ref : serviceReferences) {
//								generators.add(bundleContext.getService(ref));
//							}
//						} catch (final UserFeedbackException ex) {
//							MessageDialog.openError(null, "Error", ex.getMessage());
////							Logger.error(ex.getMessage(), ex);
//						} catch (final InvalidSyntaxException ee) {
////							Logger.error("Invalid syntax: ", ee);
//						}
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
		List<LoadSlot> slotsToRemove = profile.getGeneratedSlots();
		if (!slotsToRemove.isEmpty()) {
			cmd.append(DeleteCommand.create(ed, slotsToRemove));
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
		Map<BaseLegalEntity, Long> runningAllocation = new HashMap<>();
		for (AllocationElement allocationElem : profile.getInitialAllocations()) {
			runningAllocation.put(allocationElem.getEntity(), (long) allocationElem.getAllocation());
		}
		List<Pair<BaseLegalEntity, Double>> relativeEntitlements = new ArrayList<>();
		for (RelativeEntitlementElement reElem : profile.getRelativeEntitlements()) {
			relativeEntitlements.add(new Pair<>(reElem.getEntity(), reElem.getEntitlement()));
		}
		
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
				runningAllocation.compute(p.getFirst(), (k, v) -> {
					return v + ((Double) (dailyEvent.netVolumeIn*p.getSecond())).longValue();
				});
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
			int tempRunningVolume = runningVolume - cargoVolume;
			if (tempRunningVolume > dailyEvent.minVolume && !flaggedDates.contains(currentDate)) {
				final LoadSlot slot;
				if (tempSlots.isEmpty()) {
					final String nextName = inventory.getName() + "-" + Integer.toString(idx);
					slot = CargoFactory.eINSTANCE.createLoadSlot();
					slot.setPort(inventoryPort);
					slot.setVolumeLimitsUnit(VolumeUnits.M3);
					slot.setMinQuantity(cargoVolume);
					slot.setMaxQuantity(cargoVolume);
					slot.setName(nextName);
					slot.setWindowSize(3);
					slot.setWindowSizeUnits(TimePeriod.DAYS);
					slot.setContract(chosenPurchaseContract);
					++idx;
				} else {
					slot = tempSlots.pop();
				}
				slot.setWindowStart(currentDate);
				final Entry<BaseLegalEntity, Long> mullEntity = runningAllocation.entrySet().stream() //
						.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
				mullEntity.setValue(mullEntity.getValue()-cargoVolume);
				slot.setEntity(mullEntity.getKey());
				newLoadSlots.add(slot);
				runningVolume = tempRunningVolume;
			}
		}
		if (!newLoadSlots.isEmpty()) {
			cmd.append(AddCommand.create(ed, ScenarioModelUtil.getCargoModel(sm), CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS , newLoadSlots));
			//editorData.getDefaultCommandHandler().handleCommand(cmd, null, null);
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
				profile.setInventory(inventory);
				target = profile;
			}
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
				} else if (target instanceof InventoryProfile) {
					final InventoryProfile inventoryProfile = (InventoryProfile) target;
					previewViewer.setInput(inventoryProfile.getGeneratedSlots());
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
}
