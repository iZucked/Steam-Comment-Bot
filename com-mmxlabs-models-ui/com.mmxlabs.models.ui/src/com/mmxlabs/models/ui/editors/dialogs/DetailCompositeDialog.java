/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;
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

	final ValidationHelper validationHelper = new ValidationHelper();

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

	private IDisplayCompositeFactory displayCompositeFactory;

	private Map<EObject, Collection<EObject>> ranges = new HashMap<EObject, Collection<EObject>>();

	private DefaultExtraValidationContext validationContext;

	private boolean displaySidebarList = false;

	private Composite sidebarSash;

	private TableViewer selectionViewer;

	/**
	 * Contains elements which have been removed from {@link #inputs}, which
	 * will be deleted if the dialog is OKed.
	 */
	private List<EObject> deletedInputs = new ArrayList<EObject>();

	/**
	 * Contains elements which have been added (these will actually be added
	 * into the model, so they must be deleted on cancel)
	 */
	private List<EObject> addedInputs = new ArrayList<EObject>();

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
			final Collection<EObject> range = displayCompositeFactory
					.getExternalEditingRange(rootObject, original);
			range.add(original);
			// range is the full set of objects which the display composite
			// might touch; we need to duplicate all of these
			final Collection<EObject> duplicateRange = EcoreUtil.copyAll(range);
			ranges.put(original, duplicateRange);
			final Iterator<EObject> rangeIterator = range.iterator();
			final Iterator<EObject> duplicateRangeIterator = duplicateRange
					.iterator();
			while (rangeIterator.hasNext() && duplicateRangeIterator.hasNext()) {
				final EObject originalOne = rangeIterator.next();
				final EObject duplicateOne = duplicateRangeIterator.next();
				duplicateToOriginal.put(duplicateOne, originalOne);
				originalToDuplicate.put(originalOne, duplicateOne);
				if (returnDuplicates) {
					validationContext.setApparentContainment(duplicateOne,
							originalOne.eContainer(),
							(EReference) originalOne.eContainingFeature());
				}
			}
			if (!returnDuplicates) {
				final Iterator<EObject> rangeIterator2 = range.iterator();
				final Iterator<EObject> duplicateIterator = duplicateRange
						.iterator();
				while (rangeIterator2.hasNext() && duplicateIterator.hasNext()) {
					final EObject original2 = rangeIterator2.next();
					final EObject duplicate = duplicateIterator.next();
					validationContext.replace(original2, duplicate);
				}
			}
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
		this.commandHandler = new ICommandHandler() {
			@Override
			public void handleCommand(final Command command,
					final EObject target, final EStructuralFeature feature) {
				// we want to directly execute these commands, because they are
				// on copies anyway
				// so (a) no undo needed and (b) don't want to make the command
				// stack dirty.
				command.execute();
				validate();
				if (selectionViewer != null)
					selectionViewer.refresh(true);
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
		final IStatus status = validationHelper.runValidation(validator,
				validationContext, currentEditorTargets);

//		if (status.isOK()) {
//			errorText.setText("");
//			errorText.setVisible(false);
//			((GridData) errorText.getLayoutData()).heightHint = 0;
//			getContents().pack();
//		} else {
//			errorText.setText(getMessages(status));
//			errorText.setVisible(true);
//			((GridData) errorText.getLayoutData()).heightHint = 50;
//			getContents().pack();
//		}

		final boolean problem = status.matches(IStatus.ERROR);
		for (final EObject object : currentEditorTargets)
			objectValidity.put(object, !problem);

		if (displayComposite != null)
			displayComposite.displayValidationStatus(status);

		checkButtonEnablement();
	}

	/**
	 * Extract message hierarchy and construct the tool tip message.
	 * 
	 * @param status
	 * @return
	 */
	private String getMessages(final IStatus status) {
		if (status.isMultiStatus()) {
			final StringBuilder sb = new StringBuilder();
			for (final IStatus s : status.getChildren()) {
				sb.append(getMessages(s));
				sb.append("\n");
			}
			return sb.toString();
		} else {
			return status.getMessage();
		}
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
		if (inputs.isEmpty()) {
			if (displayComposite != null) {
				displayComposite.getComposite().dispose();
				displayComposite = null;
			}
			// TODO display a message?
			return;
		}
		final EObject selection = inputs.get(selectedObjectIndex);

		getShell().setText(
				"Editing " + EditorUtils.unmangle(selection.eClass().getName())
						+ " " + (1 + selectedObjectIndex) + " of "
						+ inputs.size());

		if (displayComposite != null) {
			displayComposite.getComposite().dispose();
			displayComposite = null;
		}

		displayCompositeFactory = Activator.getDefault()
				.getDisplayCompositeFactoryRegistry()
				.getDisplayCompositeFactory(selection.eClass());
		displayComposite = displayCompositeFactory.createToplevelComposite(
				dialogArea, selection.eClass());

		final EObject duplicate = getDuplicate(selection, displayComposite);

		currentEditorTargets.clear();
		final Collection<EObject> range = displayCompositeFactory
				.getExternalEditingRange(rootObject, selection);
		range.add(selection);
		for (final EObject o : range) {
			currentEditorTargets.add(originalToDuplicate.get(o));
		}

		displayComposite.setCommandHandler(commandHandler);

		displayComposite.getComposite().setLayoutData(
				new GridData(GridData.FILL_BOTH));

		displayComposite.display(rootObject, duplicate, ranges.get(selection));

		getShell().layout(true, true); // argh

		// handle enablement
		validate();

		if (lockedForEditing) {
			disableControls(displayComposite.getComposite());
		}
	}

	/**
	 * @param shell
	 */
	private void disableControls(final Control control) {
		if (control instanceof Label)
			return; // don't do anything to SWT labels
		control.setEnabled(false);
		if (control instanceof Composite) {
			for (final Control c : ((Composite) control).getChildren())
				disableControls(c);
		}
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

//		errorText = new Text(c, SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
//		{
//			GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, false);
//			gd.heightHint = 0;
//			errorText.setLayoutData(gd);
//		}
//		errorText.setEditable(false);
//		errorText.setVisible(false);
		
		if (displaySidebarList) {
			sidebarSash = new Composite(c, SWT.NONE);
			{
				GridLayout layout = new GridLayout(2, false);
				layout.marginHeight = layout.marginWidth = 0;
				sidebarSash.setLayout(layout);
			}
			sidebarSash.setLayoutData(new GridData(GridData.FILL_BOTH));
			final Composite sidebarComposite = new Composite(sidebarSash,
					SWT.NONE);
			sidebarComposite.setLayout(new GridLayout(1, true));
			sidebarComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL,
					false, true));

			final ToolBarManager barManager = new ToolBarManager(SWT.BORDER
					| SWT.RIGHT);

			// need to populate add action - it could do a duplicate and then a
			// clear?
			// delete action can just kill the primary, and remove from inputs

			barManager.createControl(sidebarComposite).setLayoutData(
					new GridData(GridData.FILL_HORIZONTAL));

			selectionViewer = new TableViewer(sidebarComposite, SWT.SINGLE
					| SWT.BORDER | SWT.V_SCROLL);

			createToolbarActions(barManager);
			barManager.update(true);

			selectionViewer.getControl().setLayoutData(
					new GridData(GridData.FILL_BOTH));

			selectionViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					if (originalToDuplicate.containsKey(element)) {
						return ((NamedObject) originalToDuplicate.get(element))
								.getName();
					} else {
						return ((NamedObject) element).getName();
					}
				}
			});

			selectionViewer.setContentProvider(new ArrayContentProvider());
			selectionViewer.setInput(inputs);
			if (inputs.isEmpty() == false) {
				selectionViewer.setSelection(new StructuredSelection(inputs
						.get(0)));
			}
			selectionViewer
					.addSelectionChangedListener(new ISelectionChangedListener() {
						@Override
						public void selectionChanged(
								final SelectionChangedEvent event) {
							final ISelection selection = event.getSelection();
							if (selection instanceof IStructuredSelection) {
								final Object selected = ((IStructuredSelection) selection)
										.getFirstElement();
								final int index = inputs.indexOf(selected);
								if (index >= 0) {
									if (index != selectedObjectIndex) {
										selectedObjectIndex = index;
										updateEditor();
									}
								}
							}
						}
					});

			dialogArea = new Composite(sidebarSash, SWT.NONE);
			{
				final GridLayout layout = new GridLayout(1, false);
				layout.marginHeight = layout.marginWidth = 0;
				dialogArea.setLayout(layout);
			}
			dialogArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true));
		} else {
			dialogArea = c;
		}
		return c;
	}

	private Action createAddAction(final IModelFactory factory) {
		return new Action("Create new " + factory.getLabel(), PlatformUI
				.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJ_ADD)) {
			@Override
			public void run() {
				final Collection<? extends ISetting> settings = factory
						.createInstance(rootObject, sidebarContainer,
								sidebarContainment);
				if (settings.isEmpty())
					return;
				// now create an add command, which will include adding any
				// other relevant objects
				final CompoundCommand add = new CompoundCommand();
				for (final ISetting setting : settings) {
					add.append(AddCommand.create(
							commandHandler.getEditingDomain(),
							setting.getContainer(), setting.getContainment(),
							setting.getInstance()));
				}
				add.execute();
				inputs.add(settings.iterator().next().getInstance());
				addedInputs.add(settings.iterator().next().getInstance());
				if (inputs.get(inputs.size() - 1) instanceof NamedObject) {
					((NamedObject) inputs.get(inputs.size() - 1))
							.setName("New " + factory.getLabel());
				}
				selectionViewer.refresh();
				selectionViewer.setSelection(new StructuredSelection(inputs
						.get(inputs.size() - 1)));
			}
		};
	}

	/**
	 * When the sidebar is displayed, this method is invoked to add actions to
	 * the toolbar above it.
	 * 
	 * @param barManager
	 */
	private void createToolbarActions(final ToolBarManager barManager) {
		// create add actions
		final List<IModelFactory> factories = Activator.getDefault()
				.getModelFactoryRegistry()
				.getModelFactories(sidebarContainment.getEReferenceType());
		if (factories.isEmpty() == false) {
			if (factories.size() == 1) {
				barManager.add(createAddAction(factories.get(0)));
			} else {
				// multi-adder //TODO
			}
		}

		// create delete actions
		final Action deleteAction = new Action() {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
						.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				selectionViewer
						.addSelectionChangedListener(new ISelectionChangedListener() {
							@Override
							public void selectionChanged(
									SelectionChangedEvent event) {
								setEnabled(event.getSelection().isEmpty() == false);
							}
						});
			}

			@Override
			public void run() {
				final ISelection selection = selectionViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object element = ((IStructuredSelection) selection)
							.getFirstElement();
					if (element instanceof EObject) {
						int elementIndex = inputs.indexOf(element);
						if (selectedObjectIndex >= elementIndex) {
							selectedObjectIndex--;
						}

						deletedInputs.add((EObject) element);

						// remove from change map
						for (final EObject o : ranges.get(element)) {
							// o is the duplicate for a real thing,
							// which is being deleted; forget about
							// both sides.
							EObject original = duplicateToOriginal.get(o);
							originalToDuplicate.remove(original);
							duplicateToOriginal.remove(o);
							validationContext.ignore(o);
							validationContext.ignore(original);
							objectValidity.remove(o);
							objectValidity.remove(original);
						}

						ranges.remove(element);
						inputs.remove(elementIndex);
						if (inputs.size() > 0) {
							selectionViewer
									.setSelection(new StructuredSelection(
											inputs.get(selectedObjectIndex)));
						} else {
							selectionViewer
									.setSelection(StructuredSelection.EMPTY);
						}
						selectionViewer.refresh();
						updateEditor();
					}
				}
			}
		};

		barManager.add(deleteAction);

		// create duplicate actions

	}

	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		if (!displaySidebarList) {
			backButton = createButton(parent, IDialogConstants.BACK_ID,
					IDialogConstants.BACK_LABEL, true);
			nextButton = createButton(parent, IDialogConstants.NEXT_ID,
					IDialogConstants.NEXT_LABEL, true);
		}
		super.createButtonsForButtonBar(parent);
	}

	private void enableButtons() {
		if (!displaySidebarList) {
			backButton.setEnabled(selectedObjectIndex > 0);
			nextButton.setEnabled(selectedObjectIndex < (inputs.size() - 1));
		}
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

	private boolean returnDuplicates = false;

	public void setReturnDuplicates(final boolean returnDuplicates) {
		this.returnDuplicates = returnDuplicates;
	}

	public int open(final IScenarioEditingLocation editorPart,
			final MMXRootObject rootObject, final List<EObject> objects) {
		return open(editorPart, rootObject, objects, false);
	}

	private boolean lockedForEditing = false;

	private MMXRootObject rootObject;

	private EReference sidebarContainment;

	private EObject sidebarContainer;

	private Text errorText;

	/**
	 * This version of the open method also displays the sidebar and allows for
	 * creation and deletion of objects in the sidebar.
	 * 
	 * @param part
	 * @param rootObject
	 * @param container
	 * @param containment
	 * @return
	 */
	public int open(final IScenarioEditingLocation part,
			final MMXRootObject rootObject, final EObject container,
			final EReference containment) {
		validationContext = new DefaultExtraValidationContext(
				part.getExtraValidationContext());
		this.rootObject = rootObject;
		lockedForEditing = false;
		this.inputs.clear();
		this.originalToDuplicate.clear();
		this.duplicateToOriginal.clear();
		this.displaySidebarList = true;
		inputs.addAll((List) container.eGet(containment));

		this.sidebarContainer = container;
		this.sidebarContainment = containment;

		part.pushExtraValidationContext(validationContext);
		try {
			final int value = open();
			if (value == OK) {
				final CompoundCommand cc = new CompoundCommand();
				cc.append(IdentityCommand.INSTANCE);
				for (final Map.Entry<EObject, EObject> entry : originalToDuplicate.entrySet()) {
					final EObject original = entry.getKey();
					final EObject duplicate = entry.getValue();
					if (!original.equals(duplicate)) {
						cc.append(makeEqualizer(original, duplicate));
					}
				}

				// new objects should already be added wherever they belong.

				// delete any objects which have been removed from the sidebar
				// list
				if (!deletedInputs.isEmpty())
					cc.append(DeleteCommand.create(
							commandHandler.getEditingDomain(), deletedInputs));
				executeFinalCommand(cc);
			} else {
				// any new objects will have been added from the inputs list, so
				// we have to delete them
				// we want to directly execute the command so that it doesn't go
				// into the undo stack.
				if (!addedInputs.isEmpty()) {
					Command delete = DeleteCommand.create(
							commandHandler.getEditingDomain(), addedInputs);
					if (delete.canExecute()) {
						System.err.println("Execute delete");
						delete.execute();
					} else {
						System.err.println("Cannot execute delete");
					}
				}
			}
			return value;
		} finally {
			part.popExtraValidationContext();
		}
	}

	public int open(final IScenarioEditingLocation part,
			final MMXRootObject rootObject, final List<EObject> objects,
			final boolean locked) {
		validationContext = new DefaultExtraValidationContext(
				part.getExtraValidationContext());
		part.pushExtraValidationContext(validationContext);
		this.rootObject = rootObject;
		lockedForEditing = locked;
		this.inputs.clear();
		this.inputs.addAll(objects);
		this.originalToDuplicate.clear();
		this.duplicateToOriginal.clear();
		try {
			final int value = open();
			if (value == OK) {
				if (returnDuplicates) {
					final CompoundCommand adder = new CompoundCommand();
					for (final Map.Entry<EObject, EObject> entry : originalToDuplicate
							.entrySet()) {
						final EObject original = entry.getKey();
						final EObject duplicate = entry.getValue();

						adder.append(AddCommand.create(
								commandHandler.getEditingDomain(),
								original.eContainer(),
								original.eContainingFeature(),
								Collections.singleton(duplicate)));
					}
					final boolean isExecutable = adder.canExecute();
					if (isExecutable) {
						executeFinalCommand(adder);
					} else {
						MessageDialog
								.openError(
										getShell(),
										"Error applying change",
										"An error occurred applying the change - the command to apply it was not executable. Refer to the error log for more details");
						log.error("Unable to apply change",
								new RuntimeException("Unable to apply change"));
					}

				} else {
					final CompoundCommand cc = new CompoundCommand();
					for (final Map.Entry<EObject, EObject> entry : originalToDuplicate
							.entrySet()) {
						final EObject original = entry.getKey();
						final EObject duplicate = entry.getValue();
						if (!original.equals(duplicate)) {
							cc.append(makeEqualizer(original, duplicate));
						}
					}

					final boolean isExecutable = cc.canExecute();
					if (isExecutable) {

						executeFinalCommand(cc);

					} else {
						MessageDialog
								.openError(
										getShell(),
										"Error applying change",
										"An error occurred applying the change - the command to apply it was not executable. Refer to the error log for more details");
						log.error("Unable to apply change",
								new RuntimeException("Unable to apply change"));
					}
				}
			}
			return value;
		} finally {
			part.popExtraValidationContext();
		}
	}

	private void executeFinalCommand(final CompoundCommand cc) {
		commandHandler.getEditingDomain().getCommandStack().execute(cc);
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
		if (editingDomain instanceof CommandProviderAwareEditingDomain) {
			((CommandProviderAwareEditingDomain) editingDomain)
					.setCommandProvidersDisabled(true);
		}
		try {
			final CompoundCommand compound = new CompoundCommand();
			compound.append(new IdentityCommand());
			for (final EStructuralFeature feature : original.eClass()
					.getEAllStructuralFeatures()) {
				// For containment references, we need to compare the contained
				// object, rather than generate a SetCommand.
				if (original.eClass().getEAllContainments().contains(feature)) {
					if (feature.isMany()) {
						if (!((Collection<?>) original.eGet(feature)).isEmpty())
							compound.append(RemoveCommand.create(editingDomain,
									original, feature,
									(Collection<?>) original.eGet(feature)));
						if (!((Collection<?>) duplicate.eGet(feature))
								.isEmpty())
							compound.append(AddCommand.create(editingDomain,
									original, feature,
									(Collection<?>) duplicate.eGet(feature)));
						// final Command mas =
						// CommandUtil.createMultipleAttributeSetter(editingDomain,
						// original, feature, (Collection)
						// duplicate.eGet(feature));
						// if (mas.canExecute() == false) {
						// log.error("Unable to set the feature " +
						// feature.getName() + " on an instance of " +
						// original.eClass().getName(), new RuntimeException(
						// "Attempt to set feature which could not be set."));
						// }
						// compound.append(mas);
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
						&& (!feature.isUnsettable() || (original
								.eIsSet(feature) == duplicate.eIsSet(feature)))) {
					continue;
				}
				if (feature.isMany()) {
					final Command mas = CommandUtil
							.createMultipleAttributeSetter(editingDomain,
									original, feature,
									(Collection<?>) duplicate.eGet(feature));
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
					if (duplicateToOriginal
							.containsKey(duplicate.eGet(feature)))
						continue; // do not fix references to copied items
					if (feature.isUnsettable()
							&& (duplicate.eIsSet(feature) == false)) {
						compound.append(SetCommand.create(editingDomain,
								original, feature, SetCommand.UNSET_VALUE));
					} else {
						compound.append(SetCommand.create(editingDomain,
								original, feature, duplicate.eGet(feature)));
					}
				}
			}

			return compound;
		} finally {
			if (editingDomain instanceof CommandProviderAwareEditingDomain) {
				((CommandProviderAwareEditingDomain) editingDomain)
						.setCommandProvidersDisabled(false);
			}
		}
	}
}
