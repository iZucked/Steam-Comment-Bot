/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.ui.inlineeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A component helper which adds an assignment editor to the cargo view.
 * 
 * @author hinton
 * 
 */
public class AssignmentInlineEditorComponentHelper extends BaseComponentHelper {
	public final class AssignmentInlineEditor extends MMXAdapterImpl implements IInlineEditor {
		private Label label;
		private ICommandHandler handler;
		private Combo combo;
		private final ArrayList<String> nameList = new ArrayList<String>();
		private final ArrayList<EObject> valueList = new ArrayList<EObject>();

		private EObject inputObject;
		private Button lock;
		private final DisposeListener disposeListener;
		private IReferenceValueProvider valueProvider;
		private Collection<EObject> range;
		private ElementAssignment elementAssignment;
		private MMXRootObject rootObject;

		/**
		 * {@link ControlDecoration} used to show validation messages.
		 */
		private ControlDecoration validationDecoration;

		/**
		 * Cached reference to the Information icon
		 */
		protected final FieldDecoration decorationInfo = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION);

		/**
		 * Cached reference to the Warning icon
		 */
		protected final FieldDecoration decorationWarning = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);

		/**
		 * Cached reference to the Error icon
		 */
		protected final FieldDecoration decorationError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		private boolean editorLocked = false;
		private boolean editorEnabled = true;
		private boolean editorVisible = true;

		public AssignmentInlineEditor() {
			disposeListener = new DisposeListener() {
				@Override
				public void widgetDisposed(final DisposeEvent e) {
					if (AssignmentInlineEditor.this.inputObject != null) {
						AssignmentInlineEditor.this.inputObject.eAdapters().remove(AssignmentInlineEditor.this);
					}
					if (AssignmentInlineEditor.this.range != null) {
						for (final EObject obj : AssignmentInlineEditor.this.range) {
							obj.eAdapters().remove(AssignmentInlineEditor.this);
						}
					}

					e.widget.removeDisposeListener(this);
				}

			};
		}

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
			if (status.isOK()) {
				// No problems, so hide decoration
				validationDecoration.hide();
			} else {
				// Default severity
				int severity = IStatus.INFO;

				// Builder used to accumulate messages
				final StringBuilder sb = new StringBuilder();

				if (!status.isMultiStatus()) {
					if (checkStatus(status)) {

						sb.append(status.getMessage());

						// Is severity worse, then note it
						if (status.getSeverity() > severity) {
							severity = status.getSeverity();
						}
					}

				} else {
					final IStatus[] children = status.getChildren();
					for (final IStatus element : children) {
						if (checkStatus(element)) {

							sb.append(element.getMessage());
							sb.append("\n");
							// Is severity worse, then note it
							if (element.getSeverity() > severity) {
								severity = element.getSeverity();
							}
						}
					}
				}

				if (sb.toString().isEmpty()) {
					// No problems, so hide decoration
					validationDecoration.hide();
					return;
				}

				// Update description text
				validationDecoration.setDescriptionText(sb.toString());

				// Update icon
				switch (severity) {
				case IStatus.INFO:
					validationDecoration.setImage(decorationInfo.getImage());
					break;
				case IStatus.WARNING:
					validationDecoration.setImage(decorationWarning.getImage());
					break;
				case IStatus.ERROR:
				default:
					validationDecoration.setImage(decorationError.getImage());
					break;
				}

				// Show the decoration.
				validationDecoration.show();
			}
		}

		/**
		 * Check status message applies to this editor.
		 * 
		 * @param status
		 * @return
		 */
		private boolean checkStatus(final IStatus status) {

			if (status instanceof IDetailConstraintStatus) {
				final IDetailConstraintStatus s = (IDetailConstraintStatus) status;

				final Collection<EObject> objects = s.getObjects();
				if (objects.contains(elementAssignment)) {
					if (s.getFeaturesForEObject(elementAssignment).contains(InputPackage.eINSTANCE.getElementAssignment_Locked())
							|| s.getFeaturesForEObject(elementAssignment).contains(InputPackage.eINSTANCE.getElementAssignment_Assignment())
							|| s.getFeaturesForEObject(elementAssignment).contains(InputPackage.eINSTANCE.getElementAssignment_AssignedObject())) {
						return true;
					}
				}
			}

			return false;
		}

		public Control wrapControl(final Control c) {
			// Create decorator for validation items
			{
				validationDecoration = new ControlDecoration(c, SWT.LEFT | SWT.TOP);
				validationDecoration.hide();

				// These should be the defaults...
				validationDecoration.setShowHover(true);
				validationDecoration.setShowOnlyOnFocus(false);

				// Set a default image
				// commented out, because this takes about 70% of the runtime of displaying the editor
				// validationDecoration.setImage(decorationInfo.getImage());

				// Hide by default
			}

			c.addDisposeListener(disposeListener);
			return c;
		}

		@Override
		public EStructuralFeature getFeature() {
			return InputPackage.eINSTANCE.getElementAssignment_Assignment();
		}

		public EObject getEditorTarget() {
			return elementAssignment;
		}

		@Override
		public void display(final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
			label.setText("Assigned to");

			if (inputObject != null) {
				inputObject.eAdapters().remove(AssignmentInlineEditor.this);
			}
			if (this.range != null) {
				for (final EObject obj : this.range) {
					obj.eAdapters().remove(AssignmentInlineEditor.this);
				}
			}

			this.inputObject = object;
			this.rootObject = scenario;
			this.range = range;

			if (inputObject != null) {
				inputObject.eAdapters().add(AssignmentInlineEditor.this);
			}
			if (this.range != null) {
				for (final EObject obj : this.range) {
					obj.eAdapters().add(AssignmentInlineEditor.this);
				}
			}

			valueProvider = handler.getReferenceValueProviderProvider().getReferenceValueProvider(InputPackage.eINSTANCE.getElementAssignment(),
					InputPackage.eINSTANCE.getElementAssignment_Assignment());

			updateDisplay(object);

			combo.addDisposeListener(disposeListener);

			if (object instanceof Cargo) {

				final Cargo cargo = (Cargo) object;
				if (cargo.getLoadSlot() != null && cargo.getLoadSlot().isDESPurchase()) {
					setEditorEnabled(false);
				}
				if (cargo.getDischargeSlot() != null && cargo.getDischargeSlot().isFOBSale()) {
					setEditorEnabled(false);
				}
			}
		}

		public void updateDisplay(final EObject object) {
			ElementAssignment assignment = null;
			for (final EObject r: range) {
				if (r instanceof ElementAssignment) 
					assignment = (ElementAssignment) r;
				
			}
				
			final EObject target = (assignment == null ? object : assignment);
			
			final List<Pair<String, EObject>> values = valueProvider.getAllowedValues(target, InputPackage.eINSTANCE.getAssignment_Vessels());			

			combo.removeAll();
			nameList.clear();
			valueList.clear();

			for (final Pair<String, EObject> v : values) {
				valueList.add(v.getSecond());
				nameList.add(v.getFirst());
				combo.add(v.getFirst());
			}

			if (assignment != null) {
				this.elementAssignment = assignment;
				Object theAssignment = assignment.getAssignment();
				final int index = valueList.indexOf(theAssignment);
				combo.setText(nameList.get(index));
				combo.select(index);
				lock.setSelection(this.elementAssignment.isLocked());
			}
		}

		@Override
		public Control createControl(final Composite parent) {

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
						handler.handleCommand(AssignmentEditorHelper.lockElement(handler.getEditingDomain(), elementAssignment), elementAssignment,
								InputPackage.eINSTANCE.getElementAssignment_Locked());
					} else {
						handler.handleCommand(AssignmentEditorHelper.unlockElement(handler.getEditingDomain(), elementAssignment), elementAssignment,
								InputPackage.eINSTANCE.getElementAssignment_Locked());
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
						// Uh oh.....
						if (elementAssignment != null) {
							if (vessel instanceof VesselClass) {
								// assign to a new spot
								final InputModel im = rootObject.getSubModel(InputModel.class);
								if (im != null) {
									int maxSpot = 0;
									for (final ElementAssignment ea : im.getElementAssignments()) {
										maxSpot = Math.max(maxSpot, ea.getSpotIndex());
									}
									maxSpot++;
									handler.handleCommand(AssignmentEditorHelper.reassignElement(handler.getEditingDomain(), (AVesselSet) vessel, elementAssignment, maxSpot), elementAssignment,
											InputPackage.eINSTANCE.getElementAssignment_Assignment());
									return;
								}
							}
							handler.handleCommand(AssignmentEditorHelper.reassignElement(handler.getEditingDomain(), (AVesselSet) vessel, elementAssignment), elementAssignment,
									InputPackage.eINSTANCE.getElementAssignment_Assignment());
						}
					}
				}
			});

			return wrapControl(sub);
		}

		private void setControlsEnabled(final boolean enabled) {
			combo.setEnabled(enabled);
			lock.setEnabled(enabled);
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			final Object input = notification.getNotifier();
			boolean enabled = true;

			Cargo cargo = null;
			if (input instanceof LoadSlot) {
				cargo = ((LoadSlot) input).getCargo();
			} else if (input instanceof DischargeSlot) {
				cargo = ((DischargeSlot) input).getCargo();
			}

			if (cargo != null) {

				if (cargo.getLoadSlot() != null && cargo.getLoadSlot().isDESPurchase()) {
					enabled = false;
				}
				if (cargo.getDischargeSlot() != null && cargo.getDischargeSlot().isFOBSale()) {
					enabled = false;
				}
			}

			setEditorEnabled(enabled);

			Object feature = notification.getFeature(); 
			if (valueProvider.updateOnChangeToFeature(feature))
				updateDisplay(inputObject);

			if (feature == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
				updateDisplay(inputObject);
			}
			if (feature == CargoPackage.eINSTANCE.getDischargeSlot()) {
				updateDisplay(inputObject);
			}
		}

		@Override
		public Label getLabel() {
			return label;
		}

		@Override
		public void setEditorLocked(boolean locked) {
			this.editorLocked = locked;
			setControlsEnabled(isEditorEnabled() && !isEditorLocked());
		}

		@Override
		public boolean isEditorLocked() {
			return editorLocked;
		}

		@Override
		public void setEditorEnabled(final boolean enabled) {
			this.editorEnabled = enabled;
			setControlsEnabled(isEditorEnabled() && !isEditorLocked());
		}

		@Override
		public boolean isEditorEnabled() {
			return editorEnabled;
		}

		@Override
		public void setEditorVisible(boolean visible) {
			this.editorVisible = visible;
			this.combo.setVisible(visible);
			this.lock.setVisible(visible);
		}

		@Override
		public boolean isEditorVisible() {
			return editorVisible;
		}

		@Override
		public void addNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeNotificationChangedListener(IInlineEditorExternalNotificationListener listener) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		final IInlineEditor assignmentEditor = new AssignmentInlineEditor();
		detailComposite.addInlineEditor(assignmentEditor);
	}

	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass displayedClass) {
		addEditorsToComposite(detailComposite);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		final EObject assignment = (EObject) AssignmentEditorHelper.getElementAssignment(root.getSubModel(InputModel.class), (UUIDObject) value);
		if (assignment == null) {
			return super.getExternalEditingRange(root, value);
		}
		return Collections.singletonList(assignment);
	}

	@Override
	public int getDisplayPriority() {
		return 1;
	}
}
