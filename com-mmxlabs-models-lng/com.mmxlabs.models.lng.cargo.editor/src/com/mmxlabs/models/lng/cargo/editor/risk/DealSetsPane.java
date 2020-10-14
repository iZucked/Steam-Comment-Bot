/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.util.DefaultExposuresCustomiser;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class DealSetsPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;
	
	@Inject
	private Iterable<IExposuresCustomiser> exposuresCustomisers;

	public DealSetsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;

		final BundleContext bc = FrameworkUtil.getBundle(DealSetsPane.class).getBundleContext();
		final Injector injector = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new DealSetsEditorProviderModule());
		injector.injectMembers(this);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		//final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		
		final GridViewerColumn gtc = addNameManipulator("Name");
		gtc.getColumn().setTree(true);
		
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final DealSetDropTargetListener listener = new DealSetDropTargetListener(scenarioEditingLocation, getScenarioViewer());

			final DropTarget dropTarget = new DropTarget(viewer.getControl(), DND.DROP_MOVE);
			dropTarget.setTransfer(types);
			dropTarget.addDropListener(listener);
		}

		setTitle("Deal Sets");
		if (getScenarioViewer() != null) {
			getScenarioViewer().setContentProvider(new SimpleTabularReportContentProvider<DealSet>());
		}
	}
	
	public class SimpleTabularReportContentProvider<T> implements ITreeContentProvider {

		@Override
		public Object[] getElements(final Object inputElement) {

			if (inputElement instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>) inputElement;
				return collection.toArray();
			}
			if (inputElement instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) inputElement;
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
				final List<DealSet> dealSets = cargoModel.getDealSets();
				return dealSets.toArray();
			}
			return new Object[0];
		}

		@Override
		public void dispose() {
			
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Collection<?>) {
				Collection<?> collection = (Collection<?>) parentElement;
				return collection.toArray();
			}
			if (parentElement instanceof DealSet) {
				boolean goAhead = false;
				final List<Object> objects = new ArrayList<Object>();
				final DealSet ds = (DealSet) parentElement;
				if (ds.getSlots() != null) {
					objects.addAll(ds.getSlots());
					goAhead = true;
				}
				if (ds.getPaperDeals() != null) {
					objects.addAll(ds.getPaperDeals());
					goAhead = true;
				}
				if (goAhead) {
					return objects.toArray();
				}
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof Collection<?> || (element instanceof DealSet && ((((DealSet) element).getSlots() != null) || (((DealSet) element).getPaperDeals() != null)));
		}
		
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			
			if (oldInput instanceof LNGScenarioModel) {
				final CargoModel cargoModel = ((LNGScenarioModel)oldInput).getCargoModel();
				((EObjectTableViewer)viewer).setCurrentContainerAndContainment(cargoModel, null);
			}
			if (newInput instanceof LNGScenarioModel) {
				final CargoModel cargoModel = ((LNGScenarioModel)newInput).getCargoModel();
				((EObjectTableViewer)viewer).setCurrentContainerAndContainment(cargoModel, null);
			}
		}
	}
	
	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return new ScenarioModifyingAction("Delete") {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
				viewer.addSelectionChangedListener(this);
			}

			@Override
			public void run() {

				// Delete commands can be slow, so show the busy indicator while deleting.
				final Runnable runnable = new Runnable() {

					@Override
					public void run() {

						final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
						editorLock.lock();
						try {
							getJointModelEditorPart().setDisableUpdates(true);
							
							if (getLastSelection() instanceof TreeSelection) {
								final TreeSelection ts = (TreeSelection) getLastSelection();
								final TreePath[] tp = ts.getPaths();
								if (tp.length == 0) return;
								final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
								final List<DealSet> dealsToDelete = new ArrayList<DealSet>();
								final Map<DealSet, List<Object>> featuresToRemove = new HashMap<DealSet, List<Object>>();
								for (final TreePath tpo :tp) {
									final int cnt = tpo.getSegmentCount();
									if (cnt == 1 && tpo.getSegment(0) instanceof DealSet) {
										dealsToDelete.add((DealSet)tpo.getSegment(0));
									} else {
										List<Object> a = featuresToRemove.computeIfAbsent((DealSet)tpo.getSegment(0), k -> new ArrayList<Object>());
										a.add(tpo.getSegment(1));
									}
								}
								dealsToDelete.forEach(featuresToRemove::remove);
								final CompoundCommand cmd = new CompoundCommand();
								if (!dealsToDelete.isEmpty()) {
									cmd.append(DeleteCommand.create(ed, dealsToDelete));
								}
								if (!featuresToRemove.isEmpty()) {
									for(Map.Entry<DealSet, List<Object>> entry : featuresToRemove.entrySet()) {
										for (final Object value : entry.getValue()) {
											if (value instanceof Slot) {
												cmd.append(RemoveCommand.create(ed, entry.getKey(), CargoPackage.Literals.DEAL_SET__SLOTS, value));
											} else {
												cmd.append(RemoveCommand.create(ed, entry.getKey(), CargoPackage.Literals.DEAL_SET__PAPER_DEALS, value));
											}
										}
									}
								}
								if(!cmd.isEmpty()) {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
								}
							}
						} finally {
							editorLock.unlock();
							getJointModelEditorPart().setDisableUpdates(false);
						}
					}

				};
				BusyIndicator.showWhile(null, runnable);
			}

			@Override
			protected boolean isApplicableToSelection(final ISelection selection) {
				return selection.isEmpty() == false && selection instanceof IStructuredSelection;
			}
		};
	}
	
	@Override
	protected Action createAddAction(final EReference containment) {
		final Action generateFromCargoesAction = createDealSetsFromCargoesAction();
		final Action generateFromContractsAction = createDealSetsFromContractsAction();
		final Action generateFromCurvesAction = createDealSetsFromCurvesAction();
		final Action generateFromIndicesAction = createDealSetsFromIndicesAction();
		
		final List<Action> actionList = new LinkedList<>();
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_CARGOES)) {
			actionList.add(generateFromCargoesAction);
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_CONTRACTS)) {
			actionList.add(generateFromContractsAction);
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_CURVES)) {
			actionList.add(generateFromCurvesAction);
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_INDICES)) {
			actionList.add(generateFromIndicesAction);
		}
		final Action[] extraActions = actionList.toArray(new Action[0]);
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), extraActions);
	}
	
	private Action createDealSetsFromContractsAction() {
		return new DefaultMenuCreatorAction("From contracts") {
			
			@Override
			protected void populate(Menu menu) {
				final Action generateFromPurchaseContractsAction = createDealSetsFromPurchaseContractsAction();
				final Action generateFromSalesContractsAction = createDealSetsFromSalesContractsAction();
				addActionToMenu(generateFromPurchaseContractsAction, menu);
				addActionToMenu(generateFromSalesContractsAction, menu);
			}
			
			@Override
			public void run() {
			}
		};
	}
	
	private Action createDealSetsFromCargoesAction() {
		return new RunnableAction("From cargoes", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(jointModelEditor.getScenarioDataProvider());
				final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
				final Set<Slot> usedSlots = updateSlots(jointModelEditor.getScenarioDataProvider());
				final CompoundCommand cmd = new CompoundCommand();
				for(final Cargo cargo : cargoModel.getCargoes()) {
					if (checkContainment(cargo, usedSlots)) continue;
					final DealSet ds = CargoFactory.eINSTANCE.createDealSet();
					ds.setName(String.format("%s_%s", cargo.getLoadName(), "set"));
					cmd.append(AddCommand.create(ed, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, ds));
					for (final Slot<?> slot : cargo.getSlots()) {
						if (slot instanceof SpotSlot) continue;
						cmd.append(AddCommand.create(ed, ds, CargoPackage.Literals.DEAL_SET__SLOTS, slot));
					}
				}
				if(!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	private Action createDealSetsFromCurvesAction() {
		return new RunnableAction("From curves", new Runnable() {
			@Override
			public void run() {
				final IScenarioDataProvider scenarioDataProvider = jointModelEditor.getScenarioDataProvider();
				final ModelMarketCurveProvider mmCurveProvider = getMarketCurveProvider(scenarioDataProvider);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
				final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
				final Map<String, List<Slot<?>>> curveToSlotMap = new HashMap<>();

				final Iterator<IExposuresCustomiser> iterExposuresCustomisers = exposuresCustomisers.iterator();
				final IExposuresCustomiser exposuresCustomiser = iterExposuresCustomisers.hasNext() ? iterExposuresCustomisers.next(): new DefaultExposuresCustomiser();
				
				cargoModel.getCargoes().stream() //
						.flatMap(cargo -> cargo.getSlots().stream().filter(slot -> (!(slot instanceof SpotSlot))))
						.forEach(slot -> {
							final String priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot);
							final Collection<AbstractYearMonthCurve> curves = mmCurveProvider.getLinkedCurves(priceExpression);
							for (final AbstractYearMonthCurve curve : curves) {
								final String curveName = curve.getName();
								if (curveName != null) {
									List <Slot<?>> slotList = curveToSlotMap.get(curveName);
									if (slotList == null) {
										slotList = new LinkedList<>();
										curveToSlotMap.put(curveName, slotList);
									}
									slotList.add(slot);
								}
							}
						});
				final CompoundCommand cmd = new CompoundCommand();
				curveToSlotMap.forEach((curveName, slots) -> {
					final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
					dealSet.setName(String.format("%s_curve_set", curveName));
					cmd.append(AddCommand.create(ed, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
					slots.forEach(slot -> cmd.append(AddCommand.create(ed, dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot)));
				});
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	private Action createDealSetsFromIndicesAction() {
		return new RunnableAction("From indices", new Runnable() {
			@Override
			public void run() {
				final IScenarioDataProvider scenarioDataProvider = jointModelEditor.getScenarioDataProvider();
				final ModelMarketCurveProvider mmCurveProvider = getMarketCurveProvider(scenarioDataProvider);
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
				final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
				final Map<String, List<Slot<?>>> indexToSlotMap = new HashMap<>();

				final Iterator<IExposuresCustomiser> iterExposuresCustomisers = exposuresCustomisers.iterator();
				final IExposuresCustomiser exposuresCustomiser = iterExposuresCustomisers.hasNext() ? iterExposuresCustomisers.next(): new DefaultExposuresCustomiser();
				
				cargoModel.getCargoes().stream() //
						.flatMap(cargo -> cargo.getSlots().stream().filter(slot -> (!(slot instanceof SpotSlot))))
						.forEach(slot -> {
							final String priceExpression = exposuresCustomiser.provideExposedPriceExpression(slot);
							for (final AbstractYearMonthCurve curve : mmCurveProvider.getLinkedCurves(priceExpression)) {
								if (curve instanceof CommodityCurve) {
									CommodityCurve comCurve = (CommodityCurve) curve;
									if (comCurve.isSetMarketIndex()) {
										String marketIndexName = comCurve.getMarketIndex().getName();
										List <Slot<?>> slotList = indexToSlotMap.get(marketIndexName);
										if (slotList == null) {
											slotList = new LinkedList<>();
											indexToSlotMap.put(marketIndexName, slotList);
										}
										slotList.add(slot);
									}
								}
							}
						});
				final CompoundCommand cmd = new CompoundCommand();
				indexToSlotMap.forEach((curveName, slots) -> {
					final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
					dealSet.setName(String.format("%s_index_set", curveName));
					cmd.append(AddCommand.create(ed, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
					slots.forEach(slot -> cmd.append(AddCommand.create(ed, dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot)));
				});
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
				}
			}
		});
	}
	
	private @NonNull ModelMarketCurveProvider getMarketCurveProvider(final @NonNull IScenarioDataProvider scenarioDataProvider) {
		if (scenarioDataProvider != null) {
			final ModelMarketCurveProvider provider = scenarioDataProvider.getExtraDataProvider(LNGScenarioSharedModelTypes.MARKET_CURVES, ModelMarketCurveProvider.class);
			if (provider != null) {
				return provider;
			}
		}
		throw new IllegalStateException("Unable to get market curve provider");
	}
	
	private Action createDealSetsFromPurchaseContractsAction() {
		return new RunnableAction("From purchase contracts", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(jointModelEditor.getScenarioDataProvider());
				final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(jointModelEditor.getScenarioDataProvider());
				
				final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
				final CompoundCommand cmd = new CompoundCommand();
				populateContractGeneratedDealSets(cmd, commercialModel.getPurchaseContracts(), cargoModel, ed, "purchase");
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd,  null, null);
				}
			}
		});
	}
	
	private Action createDealSetsFromSalesContractsAction() {
		return new RunnableAction("From sales contracts", new Runnable() {
			
			@Override
			public void run() {
				final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(jointModelEditor.getScenarioDataProvider());
				final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(jointModelEditor.getScenarioDataProvider());
				final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
				final CompoundCommand cmd = new CompoundCommand();
				populateContractGeneratedDealSets(cmd, commercialModel.getSalesContracts(), cargoModel, ed, "sales");
				if (!cmd.isEmpty()) {
					scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd,  null, null);
				}
			}
		});
	}
	
	private void populateContractGeneratedDealSets(final CompoundCommand cmd, final List<? extends Contract> contracts, final CargoModel cargoModel, final EditingDomain ed, final String suffix) {
		for (final Contract contract : contracts) {
			final List<Slot<?>> usedSlots = cargoModel.getCargoes().stream() //
					.flatMap(cargo -> cargo.getSlots().stream() //
							.filter(slot -> (!(slot instanceof SpotSlot)) && slot.getContract() == contract) //
					).collect(Collectors.toList());
			if (!usedSlots.isEmpty()) {
				final DealSet dealSet = CargoFactory.eINSTANCE.createDealSet();
				dealSet.setName(String.format("%s_%s_set", contract.getName(), suffix));
				cmd.append(AddCommand.create(ed, cargoModel, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS, dealSet));
				for (final Slot<?> slot : usedSlots) {
					cmd.append(AddCommand.create(ed,  dealSet, CargoPackage.Literals.DEAL_SET__SLOTS, slot));
				}
			}
		}
	}
	
	private Set<Slot> updateSlots(final IScenarioDataProvider sdp) {
		final Set<Slot> usedSlots = new HashSet<Slot>();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		for (final DealSet dealSet : cargoModel.getDealSets()) {
			usedSlots.addAll(dealSet.getSlots());
		}
		return usedSlots;
	}
	
	private boolean checkContainment(final Cargo cargo, final Set<Slot> usedSlots) {
		for (final Slot<?> slot : cargo.getSlots()) {
			if (usedSlots.contains(slot)) return true;
		}
		return false;
	}
}
