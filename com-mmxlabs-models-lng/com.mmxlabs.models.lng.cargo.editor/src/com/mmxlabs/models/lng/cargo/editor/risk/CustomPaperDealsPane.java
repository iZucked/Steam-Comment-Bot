/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.impl.SafeMMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class CustomPaperDealsPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;
	private final SafeMMXContentAdapter dealSetsContentAdapter;
	private final Set<PaperDeal> usedPaperDeals = new HashSet<>();

	public CustomPaperDealsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
		updatePaperDeals();
		dealSetsContentAdapter = new SafeMMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(Notification notification) {
				updatePaperDeals();
			}

			@Override
			protected synchronized void missedNotifications(final List<Notification> missed) {
				updatePaperDeals();
			}

		};
	}

	public void updatePaperDeals() {
		usedPaperDeals.clear();
		final IScenarioDataProvider sdp = jointModelEditor.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		for (final DealSet dealSet : cargoModel.getDealSets()) {
			usedPaperDeals.addAll(dealSet.getPaperDeals());
		}
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addNameManipulator("Name");
		addColumn("Side", createPaperDealTypeFormatter(), null);
		addTypicalColumn("Start Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_StartDate(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("End Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_EndDate(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Price", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Price(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Index", new StringAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Index(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Quantity", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Quantity(), jointModelEditor.getEditingDomain()));

		setTitle("Paper", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		final DragSource source = new DragSource(getScenarioViewer().getControl(), DND.DROP_MOVE);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer) {
			@Override
			public void dragStart(final DragSourceEvent event) {
				super.dragStart(event);
			}
		});
	}

	protected LNGScenarioModel getScenarioModel() {
		if (scenarioEditingLocation == null)
			return null;
		return ((LNGScenarioModel) scenarioEditingLocation.getRootObject());
	}

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

						return cargoModel.getPaperDeals()//
								.stream()//
								.filter(pd -> !usedPaperDeals.contains(pd))//
								.collect(Collectors.toList())//
								.toArray();
					}

					@Override
					public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

						if (oldInput instanceof LNGScenarioModel) {
							final CargoModel cargoModel = ((LNGScenarioModel) oldInput).getCargoModel();
							cargoModel.eAdapters().remove(dealSetsContentAdapter);
						}
						if (newInput instanceof LNGScenarioModel) {
							final CargoModel cargoModel = ((LNGScenarioModel) newInput).getCargoModel();
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
			}
		};

		return scenarioViewer;
	}

	private ICellRenderer createPaperDealTypeFormatter() {
		return new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				return render(object);
			}

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof BuyPaperDeal) {
					return "Buy";
				} else if (object instanceof SellPaperDeal) {
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

	@Override
	protected Action createAddAction(final EReference containment) {
		return null;
	}

	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return null;
	}
}
