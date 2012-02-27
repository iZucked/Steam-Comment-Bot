/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.validation.context.ValidationSupport;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * A dialog for editing scenario objects using the generated detail views.
 * 
 * @author hinton
 */
public class DetailCompositeDialog extends Dialog {
	private static final Logger log = LoggerFactory
			.getLogger(DetailCompositeDialog.class);

	private IDisplayComposite displayComposite;

	/**
	 * The top composite in which we store our detail views
	 */
	private Composite dialogArea = null;
	/**
	 * The back and forward buttons
	 */
	private Button backButton, nextButton;

	/**
	 * The index in {@link #inputs} of the displayed object
	 */
	private int selectedObjectIndex = 0;

	/**
	 * A validator used to check whether the OK button should be enabled.
	 */
	final IValidator<EObject> validator = ModelValidationService.getInstance()
			.newValidator(EvaluationMode.BATCH);

	private ICommandHandler commandHandler;

	/**
	 * This is the list of all input objects passed in for editing
	 */
	private List<EObject> inputs = new ArrayList<EObject>();
	
	/**
	 * A map from duplicated input objects to original input objects
	 */
	private Map<EObject, EObject> duplicateToOriginal = new HashMap<EObject, EObject>();
	/**
	 * A map from original input objects to duplicated ones.
	 */
	private Map<EObject, EObject> originalToDuplicate = new HashMap<EObject, EObject>();

	/**
	 * A map from objects to validity; says whether the current state is valid
	 */
	private Map<EObject, Boolean> objectValidity = new HashMap<EObject, Boolean>();
	
	/**
	 * The objects which are currently being edited (duplicates)
	 */
	private List<EObject> currentEditorTargets = new ArrayList<EObject>();
	
	/**
	 * Get the duplicate object (for editing) corresponding to the given input
	 * object.
	 * 
	 * @param input
	 * @param displayComposite
	 * @return a duplicated object suitable for editing.
	 */
	private EObject getDuplicate(final EObject input,
			final IDisplayComposite displayComposite) {
		final EObject original = input;
		if (!originalToDuplicate.containsKey(original)) {
			final Collection<EObject> range = displayComposite.getEditingRange(
					rootObject, original);
			// range is the full set of objects which the display composite
			// might touch; we need to duplicate all of these
			final Collection<EObject> duplicateRange = EcoreUtil.copyAll(range);
			final Iterator<EObject> rangeIterator = range.iterator();
			final Iterator<EObject> duplicateRangeIterator = duplicateRange
					.iterator();
			while (rangeIterator.hasNext() && duplicateRangeIterator.hasNext()) {
				final EObject originalOne = rangeIterator.next();
				final EObject duplicateOne = duplicateRangeIterator.next();
				duplicateToOriginal.put(duplicateOne, originalOne);
				originalToDuplicate.put(originalOne, duplicateOne);
			}
			
			ValidationSupport.getInstance().startEditingObjects(range,duplicateRange);
		}
		return originalToDuplicate.get(original);
	}

	/**
	 * Construct a new detail composite dialog.
	 * 
	 * @param parentShell
	 * @param valueProviderProvider
	 * @param editingDomain
	 */
	public DetailCompositeDialog(final Shell parentShell,
			final ICommandHandler commandHandler) {
		super(parentShell);
		this.commandHandler = 
				new ICommandHandler() {

					@Override
					public void handleCommand(final Command command, final EObject target,
							final EStructuralFeature feature) {
						commandHandler.handleCommand(command, target, feature);
						validate();
					}
					
					@Override
					public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
						return commandHandler.getReferenceValueProviderProvider();
					}
					
					@Override
					public EditingDomain getEditingDomain() {
						return commandHandler.getEditingDomain();
					}
				};
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS,
				true);
	}

	@Override
	public void create() {
		super.create();
		// commence editing
		enableButtons();
		updateEditor();
		resizeAndCenter();
	}

	private void validate() {
		IStatus status = validator.validate(currentEditorTargets);
		final boolean problem = status.matches(IStatus.ERROR);
		for (final EObject object : currentEditorTargets)
			objectValidity.put(object, !problem);
		
		if (displayComposite != null)
			displayComposite.displayValidationStatus(status);
		
		checkButtonEnablement();
	}
	
	private void checkButtonEnablement() {
		for (final boolean b : objectValidity.values()) {
			if (!b) {
				getButton(IDialogConstants.OK_ID).setEnabled(false);
				return;
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(!lockedForEditing);
	}
	
	/**
	 * Create an editor view for the selected object and display it.
	 */
	private void updateEditor() {
		final EObject selection = inputs.get(selectedObjectIndex);
	
		getShell().setText(
				"Editing " + EditorUtils.unmangle(selection.eClass().getName())
						+ " " + (1 + selectedObjectIndex) + " of "
						+ inputs.size());

		if (displayComposite != null) {
			displayComposite.getComposite().dispose();
			displayComposite = null;
		}
		
		displayComposite = Activator.getDefault()
				.getDisplayCompositeFactoryRegistry()
				.getDisplayCompositeFactory(selection.eClass())
				.createToplevelComposite(dialogArea, selection.eClass());
		
		final EObject duplicate = getDuplicate(selection, displayComposite);
		
		currentEditorTargets.clear();
		final Collection<EObject> range = displayComposite.getEditingRange(rootObject, selection);
		
		for (final EObject o : range) {
			currentEditorTargets.add(originalToDuplicate.get(o));
		}
		
		displayComposite.setCommandHandler(commandHandler);
		displayComposite.display(rootObject, duplicate);
		displayComposite.getComposite().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		// handle enablement
		
	}

	private void resizeAndCenter() {
		final Shell shell = getShell();
		if (shell != null) {
			shell.layout(true);

			shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			final Rectangle shellBounds = getParentShell().getBounds();
			final Point dialogSize = shell.getSize();

			shell.setLocation(shellBounds.x
					+ ((shellBounds.width - dialogSize.x) / 2), shellBounds.y
					+ ((shellBounds.height - dialogSize.y) / 2));
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);
		dialogArea = c;
		return c;
	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		backButton = createButton(parent, IDialogConstants.BACK_ID,
				IDialogConstants.BACK_LABEL, true);
		nextButton = createButton(parent, IDialogConstants.NEXT_ID,
				IDialogConstants.NEXT_LABEL, true);
		super.createButtonsForButtonBar(parent);
	}

	private void enableButtons() {
		backButton.setEnabled(selectedObjectIndex > 0);
		nextButton.setEnabled(selectedObjectIndex < (inputs.size() - 1));
	}

	@Override
	protected void buttonPressed(final int buttonId) {
		switch (buttonId) {
		case IDialogConstants.BACK_ID:
			selectedObjectIndex--;
			enableButtons();
			updateEditor();
			break;
		case IDialogConstants.NEXT_ID:
			selectedObjectIndex++;
			enableButtons();
			updateEditor();
			break;
		default:
			super.buttonPressed(buttonId);
		}
	}

	public int open(final MMXRootObject rootObject, final List<EObject> objects) {
		return open(rootObject, objects, false);
	}

	private boolean lockedForEditing = false;

	private MMXRootObject rootObject;

	public int open(final MMXRootObject rootObject,
			final List<EObject> objects, final boolean locked) {
		this.rootObject = rootObject;
		lockedForEditing = locked;
		this.inputs.clear();
		this.inputs.addAll(objects);
		this.originalToDuplicate.clear();
		this.duplicateToOriginal.clear();
		try {
			final int value = open();
			if (value == OK) {
				final CompoundCommand cc = new CompoundCommand();
				for (final Map.Entry<EObject, EObject> entry : originalToDuplicate.entrySet()) {
					final EObject original = entry.getKey();
					final EObject duplicate = entry.getValue();
					if (!original.equals(duplicate)) {
						cc.append(makeEqualizer(original, duplicate));
					}
				}
				
				final boolean isExecutable = cc.canExecute();
				if (isExecutable) {

					commandHandler.getEditingDomain().getCommandStack()
							.execute(cc);

				} else {
					MessageDialog
							.openError(
									getShell(),
									"Error applying change",
									"An error occurred applying the change - the command to apply it was not executable. Refer to the error log for more details");
					log.error("Unable to apply change", new RuntimeException(
							"Unable to apply change"));
				}
			}
			return value;
		} finally {
			final List<EObject> duplicateObjects = new ArrayList<EObject>(duplicateToOriginal.keySet().size());
			final List<EObject> originalObjects = new ArrayList<EObject>(duplicateToOriginal.keySet().size());
			for (final Map.Entry<EObject, EObject> entry : duplicateToOriginal.entrySet()) {
				duplicateObjects.add(entry.getKey());
				originalObjects.add(entry.getValue());
			}
			ValidationSupport.getInstance().endEditingObjects(originalObjects, duplicateObjects);
		}
	}

	/**
	 * Make a command to set the fields on the first argument to be equal to the
	 * fields on the second argument. Presumes both arguments have the same
	 * eclass
	 * 
	 * TODO this may be a bit slow, as it just checks at the toplevel; to make
	 * it faster, we need to establish a mapping between all objects and their
	 * duplicates, including contained objects, and then use the information
	 * given to the processor to only generate set commands for changed
	 * attributes.
	 * 
	 * This will do for now.
	 * 
	 * @param eObject
	 *            the object to be adjusted
	 * @param eObject2
	 *            the object from which to copy the adjustment
	 * @return
	 */
	private Command makeEqualizer(final EObject original,
			final EObject duplicate) {
		if (original == null && duplicate == null) {
			return IdentityCommand.INSTANCE;
		}
		final EditingDomain editingDomain = commandHandler.getEditingDomain();
		final CompoundCommand compound = new CompoundCommand();
		compound.append(new IdentityCommand());
		for (final EStructuralFeature feature : original.eClass()
				.getEAllStructuralFeatures()) {
			// For containment references, we need to compare the contained
			// object, rather than generate a SetCommand.
			if (original.eClass().getEAllContainments().contains(feature)) {

				// TODO: Handle non-EObject references such as Lists (e.g. for
				// VesselClass edits
				// TODO: fix this temporary fix, which presumes that multiply
				// contained objects
				// are not referenced elsewhere and so will not create dangling
				// hrefs
				// one possible solution is to use the code in the
				// ImportCSVAction which relinks objects that have been
				// replaced.
				if (feature.isMany()) {
					final Command mas = CommandUtil
							.createMultipleAttributeSetter(editingDomain,
									original, feature,
									(Collection) duplicate.eGet(feature));
					if (mas.canExecute() == false) {
						log.error(
								"Unable to set the feature "
										+ feature.getName()
										+ " on an instance of "
										+ original.eClass().getName(),
								new RuntimeException(
										"Attempt to set feature which could not be set."));
					}
					compound.append(mas);
				} else {
					final Command c = makeEqualizer(
							(EObject) original.eGet(feature),
							(EObject) duplicate.eGet(feature));
					// if (!c.getAffectedObjects().isEmpty()) {
					compound.append(c);
					// }
				}

				continue;
			}
			// Skip items which have not changed.
			if (Equality.isEqual(original.eGet(feature),
					duplicate.eGet(feature))
					&& (!feature.isUnsettable() || (original.eIsSet(feature) == duplicate
							.eIsSet(feature)))) {
				continue;
			}
			if (feature.isMany()) {
				final Command mas = CommandUtil.createMultipleAttributeSetter(
						editingDomain, original, feature,
						(Collection) duplicate.eGet(feature));
				if (mas.canExecute() == false) {
					log.error(
							"Unable to set the feature " + feature.getName()
									+ " on an instance of "
									+ original.eClass().getName(),
							new RuntimeException(
									"Attempt to set feature which could not be set."));
				}
				compound.append(mas);
			} else {
				if (duplicateToOriginal.containsKey(duplicate.eGet(feature))) continue; // do not fix references to copied items
				if (feature.isUnsettable()
						&& (duplicate.eIsSet(feature) == false)) {
					compound.append(SetCommand.create(editingDomain, original,
							feature, SetCommand.UNSET_VALUE));
				} else {
					compound.append(SetCommand.create(editingDomain, original,
							feature, duplicate.eGet(feature)));
				}
			}
		}

		return compound;
	}

}
