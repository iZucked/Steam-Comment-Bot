/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.ui.inlineeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
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
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
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

		private boolean editorAppliesToObject = false;
		private boolean showLockControl = false;

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
		private Composite editorComposite;

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
					if (s.getFeaturesForEObject(elementAssignment).contains(AssignmentPackage.eINSTANCE.getElementAssignment_Locked())
							|| s.getFeaturesForEObject(elementAssignment).contains(AssignmentPackage.eINSTANCE.getElementAssignment_Assignment())
							|| s.getFeaturesForEObject(elementAssignment).contains(AssignmentPackage.eINSTANCE.getElementAssignment_AssignedObject())) {
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
			return AssignmentPackage.eINSTANCE.getElementAssignment_Assignment();
		}

		public EObject getEditorTarget() {
			return elementAssignment;
		}

		@Override
		public void display(final IScenarioEditingLocation location, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
			if (inputObject != null) {
				inputObject.eAdapters().remove(AssignmentInlineEditor.this);
			}
			if (this.range != null) {
				for (final EObject obj : this.range) {
					obj.eAdapters().remove(AssignmentInlineEditor.this);
				}
			}

			editorAppliesToObject = false;
			showLockControl = false;
			if (object instanceof VesselEvent) {
				editorAppliesToObject = true;
				showLockControl = true;
			} else if (object instanceof Cargo) {
				final Cargo cargo = (Cargo) object;
				if (cargo.getCargoType() == CargoType.FLEET) {
					editorAppliesToObject = true;
					showLockControl = true;
				}
			} else if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				editorAppliesToObject = loadSlot.isDESPurchase();
			} else if (object instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) object;
				editorAppliesToObject = dischargeSlot.isFOBSale();
			}

			label.setText("Assigned to");

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

			valueProvider = handler.getReferenceValueProviderProvider().getReferenceValueProvider(AssignmentPackage.eINSTANCE.getElementAssignment(),
					AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());

			updateDisplay(object);

			combo.addDisposeListener(disposeListener);

			setEditorEnabled(editorAppliesToObject);
			setEditorVisible(editorAppliesToObject);
			this.lock.setVisible(showLockControl);
			recursiveShowHide(editorComposite, editorAppliesToObject);
			editorComposite.pack();
			// this.
		}

		/**
		 * Recursively process the control and children to find {@link GridData} layout data objects and set the exclude flag.
		 * @param control
		 * @param editorAppliesToObject
		 */
		private void recursiveShowHide(final Control control, final boolean editorAppliesToObject) {
			if (control instanceof Composite) {
				final Composite composite = (Composite) control;
				for (final Control c : composite.getChildren()) {
					recursiveShowHide(c, editorAppliesToObject);
				}
			}
			final Object layoutData = control.getLayoutData();
			if (layoutData instanceof GridData) {
				final GridData gridData = (GridData) layoutData;
				gridData.exclude = !editorAppliesToObject;
			}
		}

		public void updateDisplay(final EObject object) {
			ElementAssignment assignment = null;
			for (final EObject r : range) {
				if (r instanceof ElementAssignment) {
					assignment = (ElementAssignment) r;
					if (assignment.getAssignedObject() == object) {
						break;
					} else {
						assignment = null;
					}
				}
			}

			final EObject target = (assignment == null ? object : assignment);

			final List<Pair<String, EObject>> values = valueProvider.getAllowedValues(target, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());

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
				final Object theAssignment = assignment.getAssignment();
				final int index = valueList.indexOf(theAssignment);
				combo.setText(nameList.get(index));
				combo.select(index);
				lock.setSelection(this.elementAssignment.isLocked());
			}
		}

		@Override
		public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

			editorComposite = toolkit.createComposite(parent);
			final GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = layout.marginWidth = 0;
			editorComposite.setLayout(layout);

			final Combo combo = new Combo(editorComposite, SWT.READ_ONLY);
			this.combo = combo;
			toolkit.adapt(combo);

			combo.setLayoutData(new GridData(GridData.FILL_BOTH));

			final Button lock = toolkit.createButton(editorComposite, "locked", SWT.CHECK);
			this.lock = lock;
			lock.setLayoutData(new GridData(GridData.FILL_VERTICAL));

			lock.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (lock.getSelection()) {
						handler.handleCommand(AssignmentEditorHelper.lockElement(handler.getEditingDomain(), elementAssignment), elementAssignment,
								AssignmentPackage.eINSTANCE.getElementAssignment_Locked());
					} else {
						handler.handleCommand(AssignmentEditorHelper.unlockElement(handler.getEditingDomain(), elementAssignment), elementAssignment,
								AssignmentPackage.eINSTANCE.getElementAssignment_Locked());
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
							if (vessel == null) {
								handler.handleCommand(AssignmentEditorHelper.unassignElement(handler.getEditingDomain(), elementAssignment), elementAssignment,
										AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
							} else if (vessel instanceof VesselClass) {
								// assign to a new spot
								if (rootObject instanceof LNGScenarioModel) {
									final AssignmentModel im = ((LNGScenarioModel) rootObject).getPortfolioModel().getAssignmentModel();
									if (im != null) {
										int maxSpot = 0;
										for (final ElementAssignment ea : im.getElementAssignments()) {
											maxSpot = Math.max(maxSpot, ea.getSpotIndex());
										}
										maxSpot++;
										handler.handleCommand(AssignmentEditorHelper.reassignElement(handler.getEditingDomain(), (VesselClass) vessel, elementAssignment, maxSpot), elementAssignment,
												AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
										return;
									}
								}
							} else if (vessel instanceof Vessel) {
								handler.handleCommand(AssignmentEditorHelper.reassignElement(handler.getEditingDomain(), (Vessel) vessel, elementAssignment), elementAssignment,
										AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
							} else {
								throw new RuntimeException("Unexpected vessel assignment type");
							}
						}
					}
				}
			});

			return wrapControl(editorComposite);
		}

		private void setControlsEnabled(final boolean enabled) {
			combo.setEnabled(enabled);
			lock.setEnabled(enabled);
		}

		@Override
		public void reallyNotifyChanged(final Notification notification) {

			final Object feature = notification.getFeature();
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
		public void setEditorLocked(final boolean locked) {
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
		public void setEditorVisible(final boolean visible) {
			this.editorVisible = visible;
			this.combo.setVisible(visible);
			this.lock.setVisible(visible);
			this.label.setVisible(visible);
		}

		@Override
		public boolean isEditorVisible() {
			return editorVisible;
		}

		@Override
		public void addNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
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
		if (root instanceof LNGScenarioModel) {
			final EObject assignment = (EObject) AssignmentEditorHelper.getElementAssignment(((LNGScenarioModel) root).getPortfolioModel().getAssignmentModel(), (UUIDObject) value);
			if (assignment != null) {
				return Collections.singletonList(assignment);
			}
		}
		return super.getExternalEditingRange(root, value);
	}

	@Override
	public int getDisplayPriority() {
		return 1;
	}
}
