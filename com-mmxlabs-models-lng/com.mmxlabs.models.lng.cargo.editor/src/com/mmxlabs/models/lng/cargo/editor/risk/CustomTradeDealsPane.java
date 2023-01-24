/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.GroupData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.ContractManipulator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.impl.SafeMMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class CustomTradeDealsPane extends ScenarioTableViewerPane {

	private final SafeMMXContentAdapter dealSetsContentAdapter;
	private final Set<Slot> usedSlots = new HashSet< >();

	public CustomTradeDealsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		updateSlots();
		dealSetsContentAdapter = new SafeMMXContentAdapter() {
			
			@Override
			public void reallyNotifyChanged(Notification notification) {
				updateSlots();
			}
			
			@Override
			protected synchronized void missedNotifications(final List<Notification> missed) {
				updateSlots();
			}
			
		};
	}
	
	public void updateSlots() {
		usedSlots.clear();
		final IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		for (final DealSet dealSet : cargoModel.getDealSets()) {
			usedSlots.addAll(dealSet.getSlots());
		}
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		
		final IReferenceValueProviderProvider provider = scenarioEditingLocation.getReferenceValueProviderCache();

		addNameManipulator("Name");
		
		addColumn("Side", createSlotTypeFormatter(), null);
		addReadOnlyColumn("Window", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), getCommandHandler()));
		addReadOnlyColumn("Volume", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), getCommandHandler()));
		addReadOnlyColumn("Port", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), provider, getCommandHandler()));
		addReadOnlyColumn("Contract", new ContractManipulator(provider, getCommandHandler()));
		addReadOnlyColumn("Conterparty", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Counterparty(), getCommandHandler()));

		setTitle("Physical", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
		
		{
			final DragSource source = new DragSource(getScenarioViewer().getControl(), DND.DROP_MOVE);
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			source.setTransfer(types);
			source.addDragListener(new BasicDragSource(getScenarioViewer()));
		}
	}
	
	
	
	protected LNGScenarioModel getScenarioModel() {
		if (scenarioEditingLocation == null) return null;
		return ((LNGScenarioModel) scenarioEditingLocation.getRootObject());
	}
	
	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {
			@Override
			public void init(final AdapterFactory adapterFactory, final ModelReference modelReference, final EReference... path) {
				super.init(adapterFactory, modelReference, path);

				init(new ITreeContentProvider() {

					@Override
					public void dispose() {
						final CargoModel cargoModel = getScenarioModel().getCargoModel();
						cargoModel.eAdapters().remove(dealSetsContentAdapter);
					}

					@Override
					public Object[] getElements(final Object inputElement) {

						final CargoModel cargoModel = getScenarioModel().getCargoModel();

						final List<Slot> slots = new ArrayList<>();
						slots.addAll(cargoModel.getLoadSlots()//);
								.stream()//
								.filter(s -> !(s instanceof SpotSlot)
										&& !(usedSlots.contains(s)))//
								.collect(Collectors.toList()));
						slots.addAll(cargoModel.getDischargeSlots()//);
								.stream()//
								.filter(s -> !(s instanceof SpotSlot)
										&& !(usedSlots.contains(s)))//
								.collect(Collectors.toList()));

						return slots.toArray();
					}

					@Override
					public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
						
						if (oldInput instanceof LNGScenarioModel) {
							final CargoModel cargoModel = ((LNGScenarioModel)oldInput).getCargoModel();
							cargoModel.eAdapters().remove(dealSetsContentAdapter);
						}
						if (newInput instanceof LNGScenarioModel) {
							final CargoModel cargoModel = ((LNGScenarioModel)newInput).getCargoModel();
							cargoModel.eAdapters().add(dealSetsContentAdapter);
						}
					}

					@Override
					public Object[] getChildren(final Object parentElement) {
						return null;
					}

					@Override
					public Object getParent(final Object element) {
						return null;
					}

					@Override
					public boolean hasChildren(final Object element) {
						return false;
					}

				}, modelReference);
				// Get default comparator
				final ViewerComparator vc = getComparator();
				// Wrap around with group sorter
				setComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object e1, final Object e2) {
						GroupData g1 = null;
						GroupData g2 = null;
						if (e1 instanceof RowData) {
							g1 = ((RowData) e1).getGroup();
						}
						if (e2 instanceof RowData) {
							g2 = ((RowData) e2).getGroup();
						}
						if (g1 == g2) {
							return vc.compare(viewer, e1, e2);
						} else {
							final Object rd1 = (g1 == null || g1.getRows().isEmpty()) ? e1 : g1.getRows().get(0);
							final Object rd2 = (g2 == null || g2.getRows().isEmpty()) ? e2 : g2.getRows().get(0);
							return vc.compare(viewer, rd1, rd2);
						}
					}
				});
			}
		};
		
		return scenarioViewer;
	}
	
	private <T extends ICellManipulator & ICellRenderer> void addReadOnlyColumn(final String columnName, final T manipulatorAndRenderer, final ETypedElement... path) {
		addTypicalColumn(columnName, new ReadOnlyManipulatorWrapper(manipulatorAndRenderer));
	}
	
	@Override
	protected Action createAddAction(final EReference containment) {
		return null;
	}
	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return null;
	}
	
	private ICellRenderer createSlotTypeFormatter() {
		return new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				return render(object);
			}

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof LoadSlot) {
					return "Buy";
				} else if (object instanceof DischargeSlot) {
					return "Sell";
				}
				return "";
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return null;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return null;
			}
		};
	}
}
