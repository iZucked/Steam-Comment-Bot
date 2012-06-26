package com.mmxlabs.models.lng.input.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.AssignmentEditor;
import com.mmxlabs.models.lng.input.editor.IAssignmentInformationProvider;
import com.mmxlabs.models.lng.input.editor.IAssignmentListener;
import com.mmxlabs.models.lng.input.editor.IAssignmentProvider;
import com.mmxlabs.models.lng.input.editor.ISizeListener;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.input.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;

public class InputJointModelEditorContribution extends
		BaseJointModelEditorContribution<InputModel> {
	
	private final HashMap<Pair<Port, Port>, Integer> minTravelTimes = new HashMap<Pair<Port, Port>, Integer>();
	private AssignmentEditor<CollectedAssignment, UUIDObject> editor;
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
		updateMinTravelTimes();
		
		
		CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		
		final List<Object> resources = new ArrayList<Object>();
		resources.addAll(fleetModel.getVessels());
		
		// try and set up spot vessels. 
		
		editor.setResources(AssignmentEditorHelper.collectAssignments(modelObject, fleetModel));
		
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
	
	protected void updateMinTravelTimes() {
		final PortModel pm = rootObject.getSubModel(PortModel.class);
		final FleetModel fm = rootObject.getSubModel(FleetModel.class);
		minTravelTimes.clear();
		if (pm != null && fm != null) {
			double maxSpeed = 0;
			for (final VesselClass vc : fm.getVesselClasses()) {
				maxSpeed = Math.max(maxSpeed, vc.getMaxSpeed());
			}
			for (final Route route : pm.getRoutes()) {
				for (final RouteLine line : route.getLines()) {
					final Pair<Port, Port> p = new Pair<Port, Port>(line.getFrom(), line.getTo());
					Integer i = minTravelTimes.get(p);
					int t = (int) (line.getDistance() / maxSpeed);
					if (i == null || t < i) {
						minTravelTimes.put(p, t);
					}
				}
			}
		}
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
			public void requiredSizeUpdated(int width, int height) {
				scroller.setMinSize(width, height);
			}
		});
		
		final IAssignmentInformationProvider<CollectedAssignment, UUIDObject> timing = new IAssignmentInformationProvider<CollectedAssignment, UUIDObject>() {
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
			public String getResourceLabel(CollectedAssignment resource) {
				return resource.getVesselOrClass().getName();
			}

			private Port getEndPort(final UUIDObject task) {
				if (task instanceof Cargo) {
					return ((Cargo)task).getDischargeSlot().getPort();
				} else if (task instanceof CharterOutEvent) {
					return ((CharterOutEvent) task).getEndPort();
				} else if (task instanceof VesselEvent) {
					return ((VesselEvent) task).getPort();
				} else {
					return null;
				}
			}
			
			private Port getStartPort(final UUIDObject task) {
				if (task instanceof Cargo) {
					return ((Cargo)task).getLoadSlot().getPort();
				} else if (task instanceof VesselEvent) {
					return ((VesselEvent) task).getPort();
				} else {
					return null;
				}
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
				return getLabel(task) + secondLine;// + "\n" + AssignmentEditorHelper.getElementAssignment(modelObject, task).getSequence();
			}

			@Override
			public boolean isLocked(UUIDObject task) {
				final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(modelObject, task);
				return elementAssignment == null ? false : elementAssignment.isLocked();
			}

			@Override
			public Date getResourceStartDate(final CollectedAssignment resource) {
				if (resource.isSpotVessel() == false) {
					
						final Vessel v2 = (Vessel) resource.getVesselOrClass();
						if (v2.getAvailability().isSetStartAfter()) {
							return v2.getAvailability().getStartAfter();
						}
					
				}
				return null;
			}

			@Override
			public Date getResourceEndDate(final CollectedAssignment resource) {
				if (resource.isSpotVessel() == false) {
						final Vessel v2 = (Vessel) resource.getVesselOrClass();
						if (v2.getAvailability().isSetStartAfter()) {
							return v2.getAvailability().getEndBy();
						}
					}
				
				return null;
			}

			@Override
			public boolean isSensibleSequence(final UUIDObject task1, final UUIDObject task2) {
				final Date end1 = getEndDate(task1);
				final Date start2 = getStartDate(task2);

				if (end1.before(start2)) {
					final long time = (start2.getTime() - end1.getTime()) / Timer.ONE_HOUR;
					final Port p1 = getEndPort(task1);
					final Port p2 = getStartPort(task2);
					// guess travel time
					final Integer travelTime = minTravelTimes.get(new Pair<Port, Port>(p1, p2));
					if (travelTime != null) {
						return time >= travelTime;
					}
				}
				return false;
			}
		};
		
		editor.setInformationProvider(timing);
		
		editor.setAssignmentProvider(new IAssignmentProvider<CollectedAssignment, UUIDObject>() {
			@Override
			public List<UUIDObject> getAssignedObjects(CollectedAssignment resource) {
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
			public void taskReassigned(UUIDObject task, UUIDObject beforeTask, UUIDObject afterTask, CollectedAssignment oldResource, CollectedAssignment newResource) {
//				final Command c = AssignmentEditorHelper.taskReassigned(editorPart.getEditingDomain(), modelObject, task, beforeTask, afterTask, oldResource, newResource);
				
//				editorPart.getEditingDomain().getCommandStack().execute(c);
				
				final EditingDomain ed = editorPart.getEditingDomain();
				//TODO sort out spot vessels.
				ed.getCommandStack().execute(AssignmentEditorHelper.reassignElement(ed, modelObject, beforeTask, task, afterTask, newResource.getVesselOrClass(), newResource.getSpotIndex()));
				
				updateEditorInput();
//				editor.update();
			}

			@Override
			public void taskUnassigned(UUIDObject task, CollectedAssignment oldResource) {
				final EditingDomain ed = editorPart.getEditingDomain();
				
//				final Command cc = AssignmentEditorHelper.totallyUnassign(ed, modelObject, task);
				
//				ed.getCommandStack().execute(cc);
				
				ed.getCommandStack().execute(AssignmentEditorHelper.unassignElement(ed, modelObject, task));
				updateEditorInput();
//				editor.update();
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
			public void taskLocked(final UUIDObject task, final CollectedAssignment resource) {
				final EditingDomain ed = editorPart.getEditingDomain();
//				editorPart.getEditingDomain().getCommandStack().execute(
//						AddCommand.create(ed, modelObject, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(), 
//								task));
				ed.getCommandStack().execute(AssignmentEditorHelper.lockElement(ed, modelObject, task));
				editor.redraw();
			}

			@Override
			public void taskUnlocked(final UUIDObject task, final CollectedAssignment resource) {
				final EditingDomain ed = editorPart.getEditingDomain();
//				editorPart.getEditingDomain().getCommandStack().execute(
//						RemoveCommand.create(ed, modelObject, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(), 
//								task));
				ed.getCommandStack().execute(AssignmentEditorHelper.unlockElement(ed, modelObject, task));
				editor.redraw();
			}
		});
		
		
		final IFilter resourceFilter = new IFilter() {
			@Override
			public boolean select(Object toTest) {
				final String name = timing.getResourceLabel((CollectedAssignment) toTest);
				final String pattern = resourceFilterText.getText().trim();
				return match(name, pattern);
			}
		};
		
		final IFilter taskFilter = new IFilter() {
			@Override
			public boolean select(Object toTest) {
				final String name = timing.getLabel((UUIDObject) toTest);
				final String pattern = taskFilterText.getText().trim();
				return match(name, pattern);
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

	private boolean match(String test, String pattern) {
		// simple boolean matching
		test = test.toLowerCase();
		pattern = pattern.trim();
		if (pattern.isEmpty()) return true;
		final String[] parts = pattern.split(",");
		for (final String part : parts) {
			final String trimmedPart = part.trim().toLowerCase();
			if (trimmedPart.isEmpty()) continue;
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
	public void setLocked(boolean locked) {
		
	}

}
