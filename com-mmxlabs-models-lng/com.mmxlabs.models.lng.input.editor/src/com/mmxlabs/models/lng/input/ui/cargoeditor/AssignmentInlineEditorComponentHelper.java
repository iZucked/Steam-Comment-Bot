package com.mmxlabs.models.lng.input.ui.cargoeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.management.timer.Timer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A component helper which adds an assignment editor to the cargo view.
 * 
 * @author hinton
 *
 */
public class AssignmentInlineEditorComponentHelper extends BaseComponentHelper {
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		final IInlineEditor assignmentEditor = new IInlineEditor() {
			private Label label;
			private ICommandHandler handler;
			private Combo combo;
			
			private final ArrayList<String> nameList = new ArrayList<String>();
			private final ArrayList<EObject> valueList = new ArrayList<EObject>();
			private InputModel inputModel;
			private EObject inputObject;
			private Button lock;

			@Override
			public void setLabel(final Label label) {
				this.label = label;
			}
			
			@Override
			public void setCommandHandler(final ICommandHandler handler) {
				this.handler = handler;
			}
			
			@Override
			public void processValidation(final IStatus status) {
				
			}
			
			@Override
			public EStructuralFeature getFeature() {
				return null;
			}
			
			@Override
			public void display(final MMXRootObject scenario, final EObject object,
					final Collection<EObject> range) {
				label.setText("Assigned to:");
				
				this.inputObject = object;
				
				final IReferenceValueProvider valueProvider = handler.getReferenceValueProviderProvider().getReferenceValueProvider(
						InputPackage.eINSTANCE.getAssignment(),
						InputPackage.eINSTANCE.getAssignment_Vessels());
				
				List<Pair<String, EObject>> values = valueProvider.getAllowedValues(null, 
						InputPackage.eINSTANCE.getAssignment_Vessels());
				
				combo.removeAll();
				nameList.clear();
				valueList.clear();

				for (final Pair<String, EObject> v : values) {
					valueList.add(v.getSecond());
					nameList.add(v.getFirst());
					combo.add(v.getFirst());
				}
				
				for (final EObject r : range) {
					if (r instanceof InputModel) {
						final InputModel i = (InputModel) r;
						this.inputModel = i;
						top:
						for (final Assignment a : i.getAssignments()) {
							if (a.getVessels().isEmpty()) continue;
							for (final UUIDObject o : a.getAssignedObjects()) {
								if (o == object) {
									combo.setText(nameList.get(valueList.indexOf(a.getVessels().iterator().next())));
									break top;
								}
							}
						}
						
						lock.setSelection(i.getLockedAssignedObjects().contains(inputObject));
					}
				}
			}
			
			@Override
			public Control createControl(Composite parent) {
				
				final Composite sub = new Composite(parent, SWT.NONE);
				final GridLayout layout = new GridLayout(2, false);
				layout.marginHeight = layout.marginWidth = 0;
				sub.setLayout(layout);
				
				final Combo combo = new Combo(sub, SWT.READ_ONLY);
				this.combo = combo;
				
				combo.setLayoutData(new GridData(GridData.FILL_BOTH));
				
				final Button lock = new Button(sub, SWT.CHECK);
				this.lock = lock;
				lock.setText("Locked");
				lock.setLayoutData(new GridData(GridData.FILL_VERTICAL));
				
				lock.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						if (lock.getSelection()) {
							handler.handleCommand(AddCommand.create(
									handler.getEditingDomain(), inputModel, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(),
									inputObject), inputModel, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects());
						} else {
							handler.handleCommand(RemoveCommand.create(
									handler.getEditingDomain(), inputModel, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects(),
									inputObject), inputModel, InputPackage.eINSTANCE.getInputModel_LockedAssignedObjects());
						}
					}
				});
				
				combo.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						// apply change
						final int index = combo.getSelectionIndex();
						if (index >= 0) {
							final EObject vessel = valueList.get(index);
							if (inputModel != null) {
								final CompoundCommand cc = new CompoundCommand();
								boolean hadExistingAssignment = false;
								for (final Assignment a : inputModel.getAssignments()) {
									if (a.getAssignedObjects().contains(inputObject)) {
										if (a.getVessels().contains(vessel) == false) {
											cc.append(RemoveCommand.create(handler.getEditingDomain(), a, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), inputObject));
										} else {
											return;
										}
									} else if (!(vessel instanceof VesselClass) && a.getVessels().contains(vessel)) {
										// insert at a suitable index
										hadExistingAssignment = true;
										final Date thisStartDate = ((Cargo) inputObject).getLoadSlot().getWindowStartWithSlotOrPortTime();
										final Date thisEndDate = ((Cargo) inputObject).getLoadSlot().getWindowEndWithSlotOrPortTime();
										int position = 0;
										boolean inserted = false;
										for (final UUIDObject o : a.getAssignedObjects()) {
											final Date otherEndDate;
											final Date otherStartDate;
											if (o instanceof Cargo) {
												otherStartDate = ((Cargo) o).getDischargeSlot().getWindowStartWithSlotOrPortTime();
												otherEndDate = ((Cargo) o).getDischargeSlot().getWindowEndWithSlotOrPortTime();
											} else if (o instanceof VesselEvent) {
												otherStartDate = ((VesselEvent) o).getStartAfter();
												otherEndDate = new Date(((VesselEvent) o).getStartBy().getTime() + ((VesselEvent)o).getDurationInDays() * Timer.ONE_DAY);
											} else {
												otherEndDate = null;
												otherStartDate = null;
											}
											if (thisStartDate.after(otherEndDate)) {
												// insert this after other
												cc.append(AddCommand.create(handler.getEditingDomain(), a, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), inputObject, position+1));
												inserted = true;
												break;
											} else if (thisEndDate.before(otherStartDate)) {
												// insert this before other.
												cc.append(AddCommand.create(handler.getEditingDomain(), a, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), inputObject, position));
												inserted = true;
												break;
											}
											position++;
										}
										
										// no hits. add at end.
										if (!inserted) {
											cc.append(AddCommand.create(handler.getEditingDomain(), a, InputPackage.eINSTANCE.getAssignment_AssignedObjects(), inputObject));
										}
									}
								}
								
								if (!hadExistingAssignment) {
									// need to create an entirely new assignment
									final Assignment a = InputFactory.eINSTANCE.createAssignment();
									a.getVessels().add((AVesselSet) vessel);
									if (vessel instanceof VesselClass) {
										// spot
										a.setAssignToSpot(true);
									}
									a.getAssignedObjects().add((UUIDObject) inputObject);
									cc.append(AddCommand.create(handler.getEditingDomain(), inputModel, InputPackage.eINSTANCE.getInputModel_Assignments(), a));
								}
								
								handler.handleCommand(cc, inputModel, null);
							}
						}
					}
				});
				
				return sub;
			}
		};
		detailComposite.addInlineEditor(assignmentEditor);
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite,
			final EClass displayedClass) {
		addEditorsToComposite(detailComposite);
	}

	@Override
	public List<EObject> getExternalEditingRange(MMXRootObject root,
			EObject value) {
		final InputModel input = root.getSubModel(InputModel.class);
		if (input != null) {
			return Collections.singletonList((EObject) input);
		}
		return super.getExternalEditingRange(root, value);
	}

	@Override
	public int getDisplayPriority() {
		return 1;
	}
}
