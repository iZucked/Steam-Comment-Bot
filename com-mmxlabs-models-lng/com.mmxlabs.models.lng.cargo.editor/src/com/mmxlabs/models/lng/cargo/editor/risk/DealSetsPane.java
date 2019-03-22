/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
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
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class DealSetsPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;

	public DealSetsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		
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
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), new Action[0]);
	}
}
