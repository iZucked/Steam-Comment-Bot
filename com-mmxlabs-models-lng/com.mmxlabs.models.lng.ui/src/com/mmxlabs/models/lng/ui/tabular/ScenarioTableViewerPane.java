/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.tabular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class ScenarioTableViewerPane extends ScenarioViewerPane {

	private static final Logger log = LoggerFactory.getLogger(ScenarioTableViewerPane.class);

	public class ScenarioTableViewerDeleteAction extends ScenarioModifyingAction {

		@Nullable
		final Function<Collection<?>, Collection<Object>> callback;

		public ScenarioTableViewerDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
			super("Delete");
			this.callback = callback;
			setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled));
			setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Disabled));
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
						getScenarioEditingLocation().setDisableUpdates(true);
						final ISelection sel = getLastSelection();
						if (sel instanceof IStructuredSelection) {
							final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
							// Copy selection
							final List<?> objects = new ArrayList<>(((IStructuredSelection) sel).toList());

							// Ensure a unique collection of objects - no duplicates
							final Set<Object> uniqueObjects = new HashSet<>(objects);

							// Pull in additional objects to delete.
							if (callback != null) {
								uniqueObjects.addAll(callback.apply(objects));
							}
							filterObjectsToDelete(uniqueObjects);

							// Clear current selection
							selectionChanged(new SelectionChangedEvent(viewer, StructuredSelection.EMPTY));

							if (!uniqueObjects.isEmpty()) {
								// Execute command
								final Command deleteCommand = DeleteCommand.create(ed, uniqueObjects);
								ed.getCommandStack().execute(deleteCommand);
							}
						}
					} finally {
						editorLock.unlock();
						getScenarioEditingLocation().setDisableUpdates(false);
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

	protected ScenarioTableViewer scenarioViewer;

	protected Action deleteAction;
	protected Action addAction;

	public ScenarioTableViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	public ScenarioTableViewer getScenarioViewer() {
		return scenarioViewer;
	}

	@Override
	public void dispose() {

		super.dispose();

		scenarioViewer = null;
	}

	@Override
	public ScenarioTableViewer createViewer(final Composite parent) {
		if (scenarioViewer == null) {
			scenarioViewer = constructViewer(parent);

			enableOpenListener();

			filterField.setFilterSupport(scenarioViewer.getFilterSupport());

			final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(scenarioViewer) {
				@Override
				protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
							|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			};

			GridViewerEditor.create(scenarioViewer, actSupport, ColumnViewerEditor.KEYBOARD_ACTIVATION | GridViewerEditor.SELECTION_FOLLOWS_EDITOR |
			// ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
					ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);

			return scenarioViewer;
		} else {
			throw new RuntimeException("Did not expect two calls to createViewer()");
		}

	}

	/**
	 */
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(event -> {
			final ISelection selection = scenarioViewer.getSelection();
			if (selection instanceof IStructuredSelection structuredSelection) {
				DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, structuredSelection);
			}
		});
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		return new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation);
	}

	public <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final ETypedElement... path) {
		return this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final ETypedElement... pathObjects) {
		return scenarioViewer.addColumn(columnName, renderer, manipulator, pathObjects);
	}

	protected GridViewerColumn withTooltip(final String tooltip, final GridViewerColumn c) {
		c.getColumn().setHeaderTooltip(tooltip);
		return c;
	}

	protected GridViewerColumn withWidth(final int width, final GridViewerColumn c) {
		c.getColumn().setWidth(width);
		return c;
	}

	protected GridViewerColumn addNameManipulator(final String nameName) {
		return addTypicalColumn(nameName, new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getCommandHandler()));
	}

	protected Action createAddAction(final EReference containment) {
		final Action duplicateAction = createDuplicateAction();
		final List<Action> eal = new ArrayList<>();
		if (duplicateAction != null) {
			eal.add(duplicateAction);
		}
		addExtraAddActions(eal);
		final Action[] extraActions = eal.toArray(new Action[eal.size()]);
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), extraActions);
	}

	protected void addExtraAddActions(final List<Action> extraActions) {
	}

	protected IAddContext getAddContext(final EReference containment) {
		return new IAddContext() {
			@Override
			public MMXRootObject getRootObject() {
				return scenarioEditingLocation.getRootObject();
			}

			@Override
			public EReference getContainment() {
				return containment;
			}

			@Override
			public EObject getContainer() {
				return scenarioViewer.getCurrentContainer();
			}

			@Override
			public ICommandHandler getCommandHandler() {
				return scenarioEditingLocation.getDefaultCommandHandler();
			}

			@Override
			public IScenarioEditingLocation getEditorPart() {
				return scenarioEditingLocation;
			}

			@Override
			public @Nullable Collection<@NonNull EObject> getCurrentSelection() {
				return SelectionHelper.convertToList(viewer.getSelection(), EObject.class);
			}
		};
	}

	/**
	 */
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		scenarioViewer.init(adapterFactory, modelReference, path.toArray(new EReference[path.size()]));
		scenarioViewer.setStatusProvider(getScenarioEditingLocation().getStatusProvider());

		final Grid table = scenarioViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		super.initToolbars();
		final ToolBarManager toolbar = getToolBarManager();
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		final EReference containment = path.get(path.size() - 1);

		addAction = createAddAction(containment);

		if (addAction != null) {
			/*
			 * // if we can't add one, we can't duplicate one either. final Action dupAction
			 * = createDuplicateAction();
			 * 
			 * if (dupAction != null) { toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction);
			 * }
			 */

			toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);
		}
		deleteAction = createDeleteAction(null);
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		final Action copyToClipboardAction = createCopyToClipboardAction();

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
		}

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);
	}

	protected Action createCopyToClipboardAction() {
		return CopyToClipboardActionFactory.createCopyToClipboardAction(scenarioViewer);
	}

	/**
	 * Return an action which duplicates the selection
	 * 
	 * @return
	 */
	protected @Nullable Action createDuplicateAction() {
		final DuplicateAction result = new DuplicateAction(getScenarioEditingLocation());
		scenarioViewer.addSelectionChangedListener(result);
		return result;
	}

	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return new ScenarioTableViewerDeleteAction(callback);
	}

	@Override
	protected void requestActivation() {
		// This call #setFocus() in the first child. This breaks cell editing if the
		// first child is not the control being edited.
		// super.requestActivation();
		scenarioEditingLocation.setCurrentViewer(scenarioViewer);

		final IActionBars pActionBars = actionBars;
		if (pActionBars != null) {
			pActionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
			pActionBars.updateActionBars();
		}
	}

	@Override
	public void setLocked(final boolean locked) {
		final ScenarioTableViewer pScenarioViewer = scenarioViewer;
		if (pScenarioViewer != null) {
			pScenarioViewer.setLocked(locked);
		}

		super.setLocked(locked);
	}

	/**
	 * Subclasses can override this to filter out object from deletion. Each dummmy
	 * UI objects that are in the selection.
	 * 
	 * @param uniqueObjects
	 */
	protected void filterObjectsToDelete(final Set<Object> uniqueObjects) {
		// TODO Auto-generated method stub

	}

	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void pack() {
		PackActionFactory.createPackColumnsAction(scenarioViewer).run();
	}
}
