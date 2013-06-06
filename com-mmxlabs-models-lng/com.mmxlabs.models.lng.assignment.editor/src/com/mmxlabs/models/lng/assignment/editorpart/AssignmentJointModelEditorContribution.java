/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.editor.AssignmentEditor;
import com.mmxlabs.models.lng.assignment.editor.IAssignmentInformationProvider;
import com.mmxlabs.models.lng.assignment.editor.IAssignmentListener;
import com.mmxlabs.models.lng.assignment.editor.IAssignmentProvider;
import com.mmxlabs.models.lng.assignment.editor.ISizeListener;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.assignment.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;

public class AssignmentJointModelEditorContribution extends BaseJointModelEditorContribution<AssignmentModel> {

	private AssignmentEditor<CollectedAssignment, UUIDObject> editor;
	private final MMXContentAdapter adapter = new MMXContentAdapter() {
		private boolean process(final Notification n) {
			if (!n.isTouch()) {
				updateEditorInput();
				return true;
			}
			return false;
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			process(notification);
		}

		@Override
		protected void missedNotifications(final List<Notification> missed) {
			for (final Notification n : missed)
				if (process(n))
					return;
		}
	};

	protected void updateEditorInput() {

		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final CargoModel cargoModel = scenarioModel.getPortfolioModel().getCargoModel();
			final FleetModel fleetModel = scenarioModel.getFleetModel();
			final ScenarioFleetModel scenarioFleetModel = scenarioModel.getPortfolioModel().getScenarioFleetModel();

			final List<CollectedAssignment> resources = AssignmentEditorHelper.collectAssignments(modelObject, fleetModel, scenarioFleetModel);

			Collections.sort(resources, new Comparator<CollectedAssignment>() {
				@Override
				public int compare(final CollectedAssignment o1, final CollectedAssignment o2) {
					final int spotCompare = ((Boolean) o1.isSpotVessel()).compareTo(o2.isSpotVessel());
					if (spotCompare == 0) {
						return o1.getVesselOrClass().getName().compareTo(o2.getVesselOrClass().getName());
					} else {
						return spotCompare;
					}
				}
			});

			editor.setResources(resources);

			final List<UUIDObject> tasks = new ArrayList<UUIDObject>();
			if (cargoModel != null) {
				for (final Cargo c : cargoModel.getCargoes()) {
					if (c.getCargoType() == CargoType.FLEET) {
						tasks.add(c);
					}
				}
			}
			if (fleetModel != null) {
				tasks.addAll(scenarioModel.getPortfolioModel().getScenarioFleetModel().getVesselEvents());
			}

			editor.setTasks(tasks);
			editor.update();
		}
	}

	@Override
	public void addPages(final Composite parent) {
		final Composite outer = new Composite(parent, SWT.NONE);
		final GridLayout outerLayout = new GridLayout(4, false);
		outerLayout.marginHeight = outerLayout.marginWidth = 4;
		outer.setLayout(outerLayout);

		final Text resourceFilterText;
		final Text taskFilterText;

		{
			final Label lr = new Label(outer, SWT.NONE);
			lr.setText("Vessel:");
			lr.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			final Text tr = new Text(outer, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
			tr.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			final Label lt = new Label(outer, SWT.NONE);
			lt.setText("ID:");
			lt.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			final Text tt = new Text(outer, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
			tt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			resourceFilterText = tr;
			taskFilterText = tt;
		}

		final ScrolledComposite scroller = new ScrolledComposite(outer, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		scroller.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		editor = new AssignmentEditor<CollectedAssignment, UUIDObject>(scroller, SWT.NONE);

		scroller.setContent(editor);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);

		editor.addSizeListener(new ISizeListener() {
			@Override
			public void requiredSizeUpdated(final int width, final int height) {
				scroller.setMinSize(width, height);
			}
		});

		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final IAssignmentInformationProvider<CollectedAssignment, UUIDObject> timing = new AssignmentInformationProviderImplementation(scenarioModel, scenarioModel.getPortfolioModel());

		editor.setInformationProvider(timing);

		editor.setAssignmentProvider(new IAssignmentProvider<CollectedAssignment, UUIDObject>() {
			@Override
			public List<UUIDObject> getAssignedObjects(final CollectedAssignment resource) {
				return resource.getAssignedObjects();
			}

			@Override
			public boolean canStartEdit() {
				return editorPart.getEditorLock().claim();
			}

			@Override
			public void finishEdit() {
				editorPart.getEditorLock().release();
			}
		});

		editor.addAssignmentListener(new IAssignmentListener<CollectedAssignment, UUIDObject>() {
			@Override
			public void taskReassigned(final UUIDObject task, final UUIDObject beforeTask, final UUIDObject afterTask, final CollectedAssignment oldResource, final CollectedAssignment newResource) {
				// final Command c = AssignmentEditorHelper.taskReassigned(editorPart.getEditingDomain(), modelObject, task, beforeTask, afterTask, oldResource, newResource);

				// editorPart.getEditingDomain().getCommandStack().execute(c);

				final EditingDomain ed = editorPart.getEditingDomain();
				// TODO sort out spot vessels.
				ed.getCommandStack().execute(AssignmentEditorHelper.reassignElement(ed, modelObject, beforeTask, task, afterTask, newResource.getVesselOrClass(), newResource.getSpotIndex()));

				updateEditorInput();
				// editor.update();
			}

			@Override
			public void taskUnassigned(final UUIDObject task, final CollectedAssignment oldResource) {
				final EditingDomain ed = editorPart.getEditingDomain();

				// final Command cc = AssignmentEditorHelper.totallyUnassign(ed, modelObject, task);

				// ed.getCommandStack().execute(cc);

				ed.getCommandStack().execute(AssignmentEditorHelper.unassignElement(ed, modelObject, task));
				updateEditorInput();
				// editor.update();
			}

			@Override
			public void taskOpened(final UUIDObject task) {
				final DetailCompositeDialog dcd = new DetailCompositeDialog(editorPart.getShell(), editorPart.getDefaultCommandHandler());
				if (dcd.open(editorPart, editorPart.getRootObject(), Collections.singletonList((EObject) task), false) == Window.OK) {
					updateEditorInput();
				}
			}

			@Override
			public void taskDeleted(final UUIDObject task) {
				final Command delete = DeleteCommand.create(editorPart.getEditingDomain(), task);
				editorPart.getEditingDomain().getCommandStack().execute(delete);
			}

			@Override
			public void taskLocked(final UUIDObject task, final CollectedAssignment resource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				// editorPart.getEditingDomain().getCommandStack().execute(
				// AddCommand.create(ed, modelObject, AssignmentPackage.eINSTANCE.getAssignmentModel_LockedAssignedObjects(),
				// task));

				final CompoundCommand cmd = new CompoundCommand("Lock Vessel Assignment");

				cmd.append(AssignmentEditorHelper.lockElement(ed, modelObject, task));
				if (task instanceof Cargo) {
					cmd.append(SetCommand.create(ed, task, CargoPackage.eINSTANCE.getCargo_AllowRewiring(), false));
				}

				ed.getCommandStack().execute(cmd);
				editor.redraw();
			}

			@Override
			public void taskUnlocked(final UUIDObject task, final CollectedAssignment resource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				// editorPart.getEditingDomain().getCommandStack().execute(
				// RemoveCommand.create(ed, modelObject, AssignmentPackage.eINSTANCE.getAssignmentModel_LockedAssignedObjects(),
				// task));
				ed.getCommandStack().execute(AssignmentEditorHelper.unlockElement(ed, modelObject, task));
				editor.redraw();
			}
		});

		final IFilter resourceFilter = new IFilter() {
			@Override
			public boolean select(final Object toTest) {
				final String name = timing.getResourceLabel((CollectedAssignment) toTest);
				final String pattern = resourceFilterText.getText().trim();
				return match(name, pattern);
			}
		};

		final IFilter taskFilter = new IFilter() {
			@Override
			public boolean select(final Object toTest) {
				final String name = timing.getLabel((UUIDObject) toTest);
				final String pattern = taskFilterText.getText().trim();
				return match(name, pattern);
			}
		};

		final ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				editor.redraw();
			}
		};

		taskFilterText.addModifyListener(modifyListener);
		resourceFilterText.addModifyListener(modifyListener);

		editor.setResourceFilter(resourceFilter);
		editor.setTaskFilter(taskFilter);

		updateEditorInput();
		modelObject.eAdapters().add(adapter);
		editorPart.setPageText(editorPart.addPage(outer), "Assignments");
	}

	private boolean match(String test, String pattern) {
		// simple boolean matching
		test = test.toLowerCase();
		pattern = pattern.trim();
		if (pattern.isEmpty())
			return true;
		final String[] parts = pattern.split(",");
		for (final String part : parts) {
			final String trimmedPart = part.trim().toLowerCase();
			if (trimmedPart.isEmpty())
				continue;
			if (test.contains(trimmedPart)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void dispose() {
		modelObject.eAdapters().remove(adapter);
	}

	@Override
	public void setLocked(final boolean locked) {

	}

}
