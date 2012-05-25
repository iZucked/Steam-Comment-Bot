package com.mmxlabs.models.lng.input.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.AssignmentEditor;
import com.mmxlabs.models.lng.input.editor.IAssignmentListener;
import com.mmxlabs.models.lng.input.editor.IAssignmentProvider;
import com.mmxlabs.models.lng.input.editor.ISizeListener;
import com.mmxlabs.models.lng.input.editor.IAssignmentInformationProvider;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;

public class InputJointModelEditorContribution extends
		BaseJointModelEditorContribution<InputModel> {
	
	private AssignmentEditor<Assignment, UUIDObject> editor;
	private MMXContentAdapter adapter = new MMXContentAdapter() {
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
			for (final Notification n : missed) if (process(n)) return;
		}
	};

	protected void updateEditorInput() {
		editor.setResources((List) modelObject.getAssignments());
		
		CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		
		final List<UUIDObject> tasks = new ArrayList<UUIDObject>();
		if (cargoModel != null) tasks.addAll(cargoModel.getCargoes());
		if (fleetModel != null) tasks.addAll(fleetModel.getVesselEvents());
		
		editor.setTasks(tasks);
		editor.update();
	}
	
	@Override
	public void addPages(Composite parent) {
		final ScrolledComposite scroller = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		
		editor = new AssignmentEditor<Assignment, UUIDObject>(scroller, SWT.NONE);

		scroller.setContent(editor);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);
		
		editor.addSizeListener(new ISizeListener() {
			@Override
			public void requiredSizeUpdated(int width, int height) {
				scroller.setMinSize(width, height);
			}
		});
		
		final IAssignmentInformationProvider<Assignment, UUIDObject> timing = new IAssignmentInformationProvider<Assignment, UUIDObject>() {
			@Override
			public Date getStartDate(UUIDObject task) {
				if (task instanceof Cargo) {
					return ((Cargo) task).getLoadSlot()
							.getWindowStartWithSlotOrPortTime();
				} else if (task instanceof VesselEvent) {
					return
							((VesselEvent) task).getStartBy();
				} else if (task instanceof Slot) {
					return ((Slot) task).getWindowStartWithSlotOrPortTime();
				} else {
					return null;
				}
			}
			
			@Override
			public Date getEndDate(UUIDObject task) {
				if (task instanceof Cargo) {
					return ((Cargo) task).getDischargeSlot().getWindowEndWithSlotOrPortTime();
				} else if (task instanceof VesselEvent) {
					return 
							new Date(
							((VesselEvent) task).getStartBy().getTime()
							+ Timer.ONE_DAY * ((VesselEvent)task).getDurationInDays());
				} else if (task instanceof Slot) {
					return ((Slot) task).getWindowEndWithSlotOrPortTime();
				} else {
					return null;
				}
			}

			@Override
			public String getLabel(final UUIDObject element) {
				if (element instanceof NamedObject) {
					return ((NamedObject) element).getName();
				} else {
					return "";
				}
			}

			@Override
			public String getResourceLabel(Assignment resource) {
				if (resource.getVessels().isEmpty()) return "";
				return resource.getVessels().iterator().next().getName();
			}

			@Override
			public String getTooltip(UUIDObject task) {
				return getLabel(task);
			}

			@Override
			public boolean isLocked(UUIDObject task) {
				return modelObject.getLockedAssignedObjects().contains(task);
			}
		};
		
		editor.setInformationProvider(timing);
		
		editor.setAssignmentProvider(new IAssignmentProvider<Assignment, UUIDObject>() {
			@Override
			public List<UUIDObject> getAssignedObjects(Assignment resource) {
				return resource.getAssignedObjects();
			}
		});
		
		editor.addAssignmentListener(new IAssignmentListener<Assignment, UUIDObject>() {
			@Override
			public void taskReassigned(UUIDObject task, UUIDObject beforeTask,
					UUIDObject afterTask, Assignment oldResource,
					Assignment newResource) {
				final CompoundCommand cc = new CompoundCommand();
				final EditingDomain ed = editorPart.getEditingDomain();
				
				if (oldResource != null) {
					cc.append(RemoveCommand.create(ed, oldResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
				}
				
				int position;
				if (beforeTask != null) {
					position = newResource.getAssignedObjects().indexOf(beforeTask);
				} else if (afterTask != null) {
					position = newResource.getAssignedObjects().indexOf(afterTask) + 1;
				} else {
					position = 0;
					final Date start = timing.getStartDate(task);
					final Date end = timing.getEndDate(task);
					for (final UUIDObject o : newResource.getAssignedObjects()) {
						if (end.before(timing.getStartDate(o))) {
							break;
						} else if (start.after(timing.getEndDate(o))) {
							position++;
							break;
						}
						position++;
					}
				}
				
				if (newResource.getAssignedObjects().isEmpty() || position == newResource.getAssignedObjects().size()) {
					cc.append(AddCommand.create(ed, newResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
				} else {
					cc.append(AddCommand.create(ed, newResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task, position));
				}
				
				ed.getCommandStack().execute(cc);
				
				editor.update();
			}

			@Override
			public void taskUnassigned(UUIDObject task, Assignment oldResource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				
				final CompoundCommand cc = new CompoundCommand();
				cc.append(RemoveCommand.create(ed, oldResource, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), task));
				cc.append(RemoveCommand.create(ed, modelObject, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(), task));
				ed.getCommandStack().execute(cc);
				
				editor.update();
			}

			@Override
			public void taskOpened(final UUIDObject task) {
				final DetailCompositeDialog dcd = new DetailCompositeDialog(editorPart.getShell(),editorPart.getDefaultCommandHandler());
				if(dcd.open(editorPart, editorPart.getRootObject(), Collections.singletonList((EObject)task), false) == Window.OK) {
					updateEditorInput();
				}
			}

			@Override
			public void taskDeleted(final UUIDObject task) {
				final Command delete = DeleteCommand.create(editorPart.getEditingDomain(), task);
				editorPart.getEditingDomain().getCommandStack().execute(delete);
			}

			@Override
			public void taskLocked(final UUIDObject task, final Assignment resource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				editorPart.getEditingDomain().getCommandStack().execute(
						AddCommand.create(ed, modelObject, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(), 
								task));
			}

			@Override
			public void taskUnlocked(final UUIDObject task, final Assignment resource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				editorPart.getEditingDomain().getCommandStack().execute(
						RemoveCommand.create(ed, modelObject, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(), 
								task));
			}
		});
		
		
		updateEditorInput();
		modelObject.eAdapters().add(adapter);
		editorPart.setPageText(editorPart.addPage(scroller), "Assignments");
	}

	@Override
	public void dispose() {
		modelObject.eAdapters().remove(adapter);
	}

	@Override
	public void setLocked(boolean locked) {
		
	}

}
