/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */
package com.mmxlabs.shiplingo.ui.detailview.containers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.Adapter;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.common.Equality;
import com.mmxlabs.lngscheduler.emf.extras.validation.context.ValidationSupport;
import com.mmxlabs.lngscheduler.emf.extras.validation.status.DetailConstraintStatusDecorator;
import com.mmxlabs.shiplingo.ui.autocorrector.AutoCorrector;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.editors.ICommandProcessor;
import com.mmxlabs.shiplingo.ui.detailview.utils.CommandUtil;
import com.mmxlabs.shiplingo.ui.detailview.utils.EditorUtils;

/**
 * A dialog for editing scenario objects using the generated detail views.
 * 
 * @author hinton
 */
public class DetailCompositeDialog extends Dialog {
	/**
	 * Provides the detail views which edit the contents.
	 */
	private final DetailCompositeContainer dcc;

	/**
	 * The top composite in which we store our detail views
	 */
	private Composite dialogArea = null;
	/**
	 * The back and forward buttons
	 */
	private Button backButton, nextButton;

	/**
	 * The index in {@link #duplicates} of the displayed object
	 */
	private int selectedObjectIndex = 0;
	/**
	 * The list of objects being edited
	 */
	private List<EObject> duplicates;

	/**
	 * An array of booleans indicating which objects are valid (true = valid)
	 */
	private boolean[] objectValidity;

	/**
	 * The editor composite for the selected object (see
	 * {@link #selectedObjectIndex})
	 */
	private AbstractDetailComposite activeDetailComposite = null;

	/**
	 * The editing domain where commands are performed
	 */
	private final EditingDomain editingDomain;

	/**
	 * A validator used to check whether the OK button should be enabled.
	 */
	final IValidator<EObject> validator = ModelValidationService.getInstance()
			.newValidator(EvaluationMode.BATCH);

	final Runnable postValidationRunnable = new Runnable() {
		@Override
		public void run() {
			final boolean isNowValid = activeDetailComposite.isCurrentStateValid();
			if (isNowValid && !objectValidity[selectedObjectIndex]) {
				// has become valid, revalidate other objects.
				// this is based on an assumption that all sibling-related constraints
				// are symmetrically invalidating.
				Arrays.fill(objectValidity, true);
				final IStatus globalValidity = validator.validate(duplicates);
				for (final IStatus s : globalValidity.getChildren()) {
					if (s.matches(Status.ERROR) == false) continue;
					if (s instanceof DetailConstraintStatusDecorator) {
						for (final EObject o : ((DetailConstraintStatusDecorator) s).getObjects()) {
							objectValidity[duplicates.indexOf(o)] = false;
						}
					}
				}
			} else {
				objectValidity[selectedObjectIndex] = isNowValid;
			}

			checkValidity();
		}
	};
	
	/**
	 * Construct a new detail composite dialog.
	 * 
	 * @param parentShell
	 * @param valueProviderProvider
	 * @param editingDomain
	 */
	public DetailCompositeDialog(final Shell parentShell,
			final IValueProviderProvider valueProviderProvider,
			final EditingDomain editingDomain) {
		super(parentShell);
		dcc = new DetailCompositeContainer(valueProviderProvider,
				editingDomain, ICommandProcessor.EXECUTE);
		this.editingDomain = editingDomain;
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

	private void checkValidity() {
		for (final boolean b : objectValidity) {
			if (!b) {
				// validity is false
				 getButton(IDialogConstants.OK_ID).setEnabled(false);
//				setErrorMessage("Something is not valid");
				// btnOk.setEnabled(false);
				return;
			}
		}
		// btnOk.setEnabled(true);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
//		setMessage("All fields are valid");
	}

	/**
	 * Create an editor view for the selected object and display it.
	 */
	private void updateEditor() {
		final EObject selection = duplicates.get(selectedObjectIndex);
		final AbstractDetailComposite detailComposite = dcc.getDetailView(
				selection.eClass(), dialogArea);

		getShell().setText(
				"Editing " + EditorUtils.unmangle(selection.eClass().getName())
						+ " " + (1 + selectedObjectIndex) + " of "
						+ duplicates.size());

		if (detailComposite != activeDetailComposite) {
			if (activeDetailComposite != null) {
				((GridData) activeDetailComposite.getLayoutData()).exclude = true;
				activeDetailComposite.setVisible(false);
				activeDetailComposite.setInput((EObject) null);
			}
			activeDetailComposite = detailComposite;
			((GridData) activeDetailComposite.getLayoutData()).exclude = false;
			activeDetailComposite.setVisible(true);
		}
		
		detailComposite.setPostValidationRunnable(postValidationRunnable);

		detailComposite.setInput(selection);
	}

	private void resizeAndCenter() {
		final Shell shell = getShell();
		if (shell != null) {
			shell.layout(true);

			shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			final Rectangle shellBounds = getParentShell().getBounds();
			final Point dialogSize = shell.getSize();

			shell.setLocation(shellBounds.x
					+ (shellBounds.width - dialogSize.x) / 2, shellBounds.y
					+ (shellBounds.height - dialogSize.y) / 2);
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);
		dialogArea = c;
		return c;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		backButton = createButton(parent, IDialogConstants.BACK_ID,
				IDialogConstants.BACK_LABEL, true);
		nextButton = createButton(parent, IDialogConstants.NEXT_ID,
				IDialogConstants.NEXT_LABEL, true);
		super.createButtonsForButtonBar(parent);
	}

	private void enableButtons() {
		backButton.setEnabled(selectedObjectIndex > 0);
		nextButton.setEnabled(selectedObjectIndex < duplicates.size() - 1);
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

	public int open(final List<EObject> objects) {
		final List<EObject> duplicates = new ArrayList<EObject>(objects.size());
		objectValidity = new boolean[objects.size()];
		{
			int objectIndex = 0;
			for (final EObject original : objects) {
				final EObject duplicate = EcoreUtil.copy(original);
				duplicates.add(duplicate);
				objectValidity[objectIndex++] = validator.validate(duplicate)
						.isOK();
				
				// add autocorrector adapters.
				for (final Object adapter : original.eAdapters()) {
					if (adapter instanceof AutoCorrector) {
						duplicate.eAdapters().add((Adapter) adapter);
					}
				}
			}
		}
		this.duplicates = duplicates;
		try {
			// tell validation support to ignore the originals and see the
			// duplicates

			ValidationSupport.getInstance().startEditingObjects(objects, duplicates);

			final int value = open();
			if (value == OK) {
				final CompoundCommand cc = new CompoundCommand();
				for (int i = 0; i < objects.size(); i++) {
					final EObject original = objects.get(i);
					final EObject duplicate = duplicates.get(i);
					if (!original.equals(duplicate)) {
						cc.append(makeEqualizer(original, duplicate));
					}
				}
				final boolean isExecutable = cc.canExecute();
				if (isExecutable) {
					editingDomain.getCommandStack().execute(cc);
				} else {
					System.err
							.println("Something is wrong with the equalizing command");
				}
			}
			return value;
		} finally {
			ValidationSupport.getInstance().endEditingObjects(objects, duplicates);
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
						System.err
								.println("Problem on multi-value containment setter");
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
					&& (!feature.isUnsettable() || (original.eIsSet(feature) == duplicate.eIsSet(feature)))) {
				continue;
			}
			if (feature.isMany()) {
				final Command mas = CommandUtil.createMultipleAttributeSetter(
						editingDomain, original, feature,
						(Collection) duplicate.eGet(feature));
				if (mas.canExecute() == false) {
					System.err
							.println("Problem on multi-value reference setter");
				}
				compound.append(mas);
			} else {
				if (feature.isUnsettable()
						&& duplicate.eIsSet(feature) == false) {
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
