package com.mmxlabs.models.lng.input.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.AssignmentEditor;
import com.mmxlabs.models.lng.input.editor.IAssignmentInformationProvider;
import com.mmxlabs.models.lng.input.editor.IAssignmentListener;
import com.mmxlabs.models.lng.input.editor.IAssignmentProvider;
import com.mmxlabs.models.lng.input.editor.ISizeListener;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
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
		if (cargoModel != null) {
			for (final Cargo c : cargoModel.getCargoes()) {
				if (c.getCargoType() == CargoType.FLEET) {
					tasks.add(c);
				}
			}
		}
		if (fleetModel != null) tasks.addAll(fleetModel.getVesselEvents());
		
		editor.setTasks(tasks);
		editor.update();
	}
	
	@Override
	public void addPages(Composite parent) {
		final Composite outer = new Composite(parent, SWT.NONE);
		final GridLayout outerLayout = new GridLayout(4, false);
		outerLayout.marginHeight = outerLayout.marginWidth = 4;
		outer.setLayout(outerLayout);
		
		
		final Text resourceFilterText;
		final Text taskFilterText;
		
		{
			final Label lr = new Label(outer,SWT.NONE);
			lr.setText("Vessel:");
			lr.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			final Text tr = new Text(outer, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
			tr.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			final Label lt = new Label(outer,SWT.NONE);
			lt.setText("Task:");
			lt.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			final Text tt = new Text(outer, SWT.BORDER | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
			tt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			resourceFilterText = tr;
			taskFilterText = tt;
		}
		
		final ScrolledComposite scroller = new ScrolledComposite(outer, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		
		scroller.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
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
				return AssignmentEditorHelper.getStartDate(task);
			}
			
			@Override
			public Date getEndDate(UUIDObject task) {
				return AssignmentEditorHelper.getEndDate(task);
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
				String secondLine = "";
				if (task instanceof Cargo) {
					secondLine = "\n" + ((Cargo) task).getLoadSlot().getPort().getName() + " to "
							+ ((Cargo)task).getDischargeSlot().getPort().getName();
				} else if (task instanceof VesselEvent) {
					secondLine = "\n" + ((VesselEvent) task).getPort().getName();
					if (task instanceof CharterOutEvent) {
						if (((CharterOutEvent)task).isSetRelocateTo())
							secondLine += " to " + ((CharterOutEvent) task).getRelocateTo().getName();
					}
				}
				return getLabel(task) + secondLine;
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
				final Command c = AssignmentEditorHelper.taskReassigned(editorPart.getEditingDomain(), modelObject, task, beforeTask, afterTask, oldResource, newResource);
				
				editorPart.getEditingDomain().getCommandStack().execute(c);
				
				editor.update();
			}

			@Override
			public void taskUnassigned(UUIDObject task, Assignment oldResource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				
				final Command cc = AssignmentEditorHelper.totallyUnassign(ed, modelObject, task);
				
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
		
		
		final IFilter resourceFilter = new IFilter() {
			@Override
			public boolean select(Object toTest) {
				final String name = timing.getResourceLabel((Assignment) toTest);
				final String pattern = resourceFilterText.getText().trim();
				if (pattern.isEmpty()) return true;
				return name.toLowerCase().contains(pattern.toLowerCase());
			}
		};
		
		final IFilter taskFilter = new IFilter() {
			@Override
			public boolean select(Object toTest) {
				final String name = timing.getLabel((UUIDObject) toTest);
				final String pattern = taskFilterText.getText().trim();
				if (pattern.isEmpty()) return true;
				return name.toLowerCase().contains(pattern.toLowerCase());
			}
		};
		
		final ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
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

	@Override
	public void dispose() {
		modelObject.eAdapters().remove(adapter);
	}

	@Override
	public void setLocked(boolean locked) {
		
	}

}
