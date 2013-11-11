/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.util.DialogEcoreCopier;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.forms.AbstractDataBindingFormDialog;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * A dialog for editing scenario objects using the generated detail views.
 * 
 * @author hinton
 */
public class DetailCompositeDialog extends AbstractDataBindingFormDialog {
	private static final Logger log = LoggerFactory.getLogger(DetailCompositeDialog.class);

	private IScenarioEditingLocation scenarioEditingLocation;

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

	private ICommandHandler commandHandler;

	/**
	 * This is the list of all input objects passed in for editing
	 */
	private final List<EObject> inputs = new ArrayList<EObject>();

	/**
	 * A map from duplicated input objects to original input objects
	 */
	private final Map<EObject, EObject> duplicateToOriginal = new HashMap<EObject, EObject>();
	/**
	 * A map from original input objects to duplicated ones.
	 */
	private final Map<EObject, EObject> originalToDuplicate = new HashMap<EObject, EObject>();

	/**
	 * The objects which are currently being edited (duplicates)
	 */
	private final List<EObject> currentEditorTargets = new ArrayList<EObject>();

	private IDisplayCompositeFactory displayCompositeFactory;

	private final Map<EObject, Collection<EObject>> ranges = new HashMap<EObject, Collection<EObject>>();

	private boolean displaySidebarList = false;

	private Composite sidebarSash;

	private TableViewer selectionViewer;

	private final DialogEcoreCopier dialogEcoreCopier = new DialogEcoreCopier();

	/**
	 * Contains elements which have been removed from {@link #inputs}, which will be deleted if the dialog is OKed.
	 */
	private final List<EObject> deletedInputs = new ArrayList<EObject>();

	/**
	 * Contains elements which have been added (these will actually be added into the model, so they must be deleted on cancel)
	 */
	private final List<EObject> addedInputs = new ArrayList<EObject>();

	private CopyDialogToClipboard copyDialogToClipboardEditorWrapper;

	private DialogValidationSupport dialogValidationSupport;

	/**
	 * Get the duplicate object (for editing) corresponding to the given input object.
	 * 
	 * @param input
	 * @param displayComposite
	 * @return a duplicated object suitable for editing.
	 */
	private EObject getDuplicate(final EObject input, final IDisplayComposite displayComposite) {
		final EObject original = input;
		if (!originalToDuplicate.containsKey(original)) {
			// Use a set to avoid duplicates in the list
			final Set<EObject> range = new LinkedHashSet<EObject>(displayCompositeFactory.getExternalEditingRange(rootObject, original));
			range.add(original);
			// range is the full set of objects which the display composite
			// might touch; we need to duplicate all of these

			// however, there is a possibility that two things will have overlapping range
			// so we don't want to duplicate again if that happens.

			final ArrayList<EObject> reducedRange = new ArrayList<EObject>(range);

			final Iterator<EObject> iterator = reducedRange.iterator();
			final ArrayList<Pair<Integer, EObject>> alreadyDuplicated = new ArrayList<Pair<Integer, EObject>>();
			int index = 0;

			while (iterator.hasNext()) {
				final EObject o = iterator.next();
				if (originalToDuplicate.containsKey(o)) {
					iterator.remove();
					alreadyDuplicated.add(new Pair<Integer, EObject>(index, o));
				}
				index++;
			}

			final List<EObject> duplicateRange = new ArrayList<EObject>(dialogEcoreCopier.copyAll(reducedRange));

			// re-insert the duplicates back into the range
			for (final Pair<Integer, EObject> duplicated : alreadyDuplicated) {
				final EObject duplicate = originalToDuplicate.get(duplicated.getSecond());
				duplicateRange.add(duplicated.getFirst(), duplicate);
			}

			ranges.put(original, duplicateRange);
			final Iterator<EObject> rangeIterator = range.iterator();
			final Iterator<EObject> duplicateRangeIterator = duplicateRange.iterator();
			while (rangeIterator.hasNext() && duplicateRangeIterator.hasNext()) {
				final EObject originalOne = rangeIterator.next();
				final EObject duplicateOne = duplicateRangeIterator.next();
				duplicateToOriginal.put(duplicateOne, originalOne);
				originalToDuplicate.put(originalOne, duplicateOne);
				if (returnDuplicates) {
					dialogValidationSupport.getValidationContext().setApparentContainment(duplicateOne, originalOne.eContainer(), (EReference) originalOne.eContainingFeature());
				}
			}
			final Iterator<EObject> rangeIterator2 = range.iterator();
			final Iterator<EObject> duplicateIterator = duplicateRange.iterator();

			final CommandProviderAwareEditingDomain domain;
			{
				final EditingDomain ed = commandHandler.getEditingDomain();
				if (ed instanceof CommandProviderAwareEditingDomain)
					domain = (CommandProviderAwareEditingDomain) ed;
				else
					domain = null;
			}

			while (rangeIterator2.hasNext() && duplicateIterator.hasNext()) {
				final EObject original2 = rangeIterator2.next();
				final EObject duplicate = duplicateIterator.next();
				if (!returnDuplicates) {
					dialogValidationSupport.getValidationContext().replace(original2, duplicate);
					if (domain != null) {
						domain.setOverride(original2, duplicate);
					}
				}
				if (domain != null) {
					domain.addEditObject(duplicate);
				}
			}
		}

		return originalToDuplicate.get(original);
	}

	/**
	 * Construct a new detail composite dialog, with style.
	 * 
	 * @param style
	 *            - turns style bits on or off (since "&"ed with current); e.g. "~SWT.MAX" removes min/max button.
	 * @since 6.0
	 */
	public DetailCompositeDialog(final Shell parentShell, final ICommandHandler commandHandler, final int style) {
		this(parentShell, commandHandler);
		final int currentStyle = getShellStyle();
		setShellStyle(currentStyle & style);
	}

	/**
	 * Construct a new detail composite dialog.
	 * 
	 * @param parentShell
	 * @param valueProviderProvider
	 * @param editingDomain
	 */
	public DetailCompositeDialog(final Shell parentShell, final ICommandHandler commandHandler) {
		super(parentShell);

		this.commandHandler = new ICommandHandler() {
			@Override
			public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
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
	}

	@Override
	public void create() {
		super.create();
		// commence editing
		enableButtons();
		updateEditor();
		resizeAndCenter();
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				resizeAndCenter();
			}
		});
	}

	private void checkButtonEnablement(final boolean enabled) {
		getButton(IDialogConstants.OK_ID).setEnabled(enabled && !lockedForEditing);
	}

	private void updateEditor() {

		if (observablesManager != null) {
			observablesManager.dispose();
		}
		if (dbc != null) {
			dbc.dispose();
		}

		this.dbc = new EMFDataBindingContext();
		this.observablesManager = new ObservablesManager();

		// This call means we do not need to manually manage our databinding objects lifecycle manually.
		observablesManager.runAndCollect(new Runnable() {

			@Override
			public void run() {
				doCreateFormContent();
			}
		});
	}

	/**
	 * Create an editor view for the selected object and display it.
	 * 
	 * @since 5.0
	 */
	@Override
	protected void doCreateFormContent() {
		if (inputs.isEmpty()) {
			if (displayComposite != null) {
				displayComposite.getComposite().dispose();
				displayComposite = null;
			}
			// TODO display a message?
			return;
		}
		final EObject selection = inputs.get(selectedObjectIndex);

		getShell().setText("Editing " + EditorUtils.unmangle(selection.eClass().getName()) + " " + (1 + selectedObjectIndex) + " of " + inputs.size());

		if (displayComposite != null) {
			displayComposite.getComposite().dispose();
			displayComposite = null;
		}

		displayCompositeFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(selection.eClass());
		displayComposite = displayCompositeFactory.createToplevelComposite(dialogArea, selection.eClass(), scenarioEditingLocation, toolkit);
		// Create a new instance with the current adapter factory.
		// TODO: Dispose?
		copyDialogToClipboardEditorWrapper = new CopyDialogToClipboard(scenarioEditingLocation.getAdapterFactory());
		// Hook via editor wrappers
		displayComposite.setEditorWrapper(copyDialogToClipboardEditorWrapper);

		final EObject duplicate = getDuplicate(selection, displayComposite);
		dialogEcoreCopier.record();

		currentEditorTargets.clear();
		final Collection<EObject> range = displayCompositeFactory.getExternalEditingRange(rootObject, selection);
		range.add(selection);
		for (final EObject o : range) {
			currentEditorTargets.add(originalToDuplicate.get(o));
		}
		dialogValidationSupport.setValidationTargets(currentEditorTargets);

		displayComposite.setCommandHandler(commandHandler);

		displayComposite.getComposite().setLayoutData(new GridData(GridData.FILL_BOTH));

		displayComposite.display(scenarioEditingLocation, rootObject, duplicate, ranges.get(selection), dbc);

		getShell().layout(true, true);

		// handle enablement
		validate();

		if (lockedForEditing) {
			final String text = getShell().getText();
			getShell().setText(text + " (Editor Locked - reopen to edit)");
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

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * @since 5.0
	 */
	@Override
	protected void createFormContent(final IManagedForm managedForm) {

		this.managedForm = managedForm;
		this.toolkit = managedForm.getToolkit();

		final ScrolledForm form = managedForm.getForm();
		form.setLayoutData(new GridData(GridData.FILL_BOTH));
		form.setText("");
		toolkit.decorateFormHeading(form.getForm());
		{
			final GridLayout layout = new GridLayout(1, true);
			form.getBody().setLayout(layout);
		}
		final Composite c = managedForm.getForm().getBody();

		if (displaySidebarList) {
			sidebarSash = new Composite(c, SWT.NONE);
			{
				final GridLayout layout = new GridLayout(2, false);
				layout.marginHeight = layout.marginWidth = 0;
				sidebarSash.setLayout(layout);
			}
			sidebarSash.setLayoutData(new GridData(GridData.FILL_BOTH));
			final Composite sidebarComposite = new Composite(sidebarSash, SWT.NONE);
			sidebarComposite.setLayout(new GridLayout(1, true));
			sidebarComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));

			final ToolBarManager barManager = new ToolBarManager(SWT.BORDER | SWT.RIGHT);

			// need to populate add action - it could do a duplicate and then a
			// clear?
			// delete action can just kill the primary, and remove from inputs

			barManager.createControl(sidebarComposite).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			selectionViewer = new TableViewer(sidebarComposite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);

			createToolbarActions(barManager);
			barManager.update(true);

			selectionViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

			selectionViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(final Object element) {
					if (originalToDuplicate.containsKey(element)) {
						return ((NamedObject) originalToDuplicate.get(element)).getName();
					} else {
						return ((NamedObject) element).getName();
					}
				}
			});

			selectionViewer.setContentProvider(new ArrayContentProvider());
			selectionViewer.setInput(inputs);
			if (inputs.isEmpty() == false) {
				selectionViewer.setSelection(new StructuredSelection(inputs.get(0)));
			}
			selectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					final ISelection selection = event.getSelection();
					if (selection instanceof IStructuredSelection) {
						final Object selected = ((IStructuredSelection) selection).getFirstElement();
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
			dialogArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		} else {

			if (false) {
				// Create a toolbar for the copy action.
				final ToolBarManager barManager = new ToolBarManager(SWT.BORDER | SWT.RIGHT);

				barManager.createControl(c).setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

				final Action copy = new Action("Copy") {
					@Override
					public void run() {
						copyDialogToClipboardEditorWrapper.copyToClipboard();
					}
				};

				copy.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/copy.gif"));

				barManager.add(copy);
				barManager.update(true);
			}

			dialogArea = c;
		}
	}

	private Action createAddAction(final IModelFactory factory) {
		return new Action("Create new " + factory.getLabel(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD)) {
			@Override
			public void run() {
				final Collection<? extends ISetting> settings = factory.createInstance(rootObject, sidebarContainer, sidebarContainment, null);
				if (settings.isEmpty())
					return;
				// now create an add command, which will include adding any
				// other relevant objects
				final CompoundCommand add = new CompoundCommand();
				for (final ISetting setting : settings) {
					add.append(AddCommand.create(commandHandler.getEditingDomain(), setting.getContainer(), setting.getContainment(), setting.getInstance()));
				}
				add.execute();
				inputs.add(settings.iterator().next().getInstance());
				addedInputs.add(settings.iterator().next().getInstance());
				if (inputs.get(inputs.size() - 1) instanceof NamedObject) {
					((NamedObject) inputs.get(inputs.size() - 1)).setName("New " + factory.getLabel());
				}
				selectionViewer.refresh();
				selectionViewer.setSelection(new StructuredSelection(inputs.get(inputs.size() - 1)));
			}
		};
	}

	/**
	 * When the sidebar is displayed, this method is invoked to add actions to the toolbar above it.
	 * 
	 * @param barManager
	 */
	private void createToolbarActions(final ToolBarManager barManager) {
		// create add actions
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(sidebarContainment.getEReferenceType());
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
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				selectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						setEnabled(event.getSelection().isEmpty() == false);
					}
				});
			}

			@Override
			public void run() {
				final ISelection selection = selectionViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object element = ((IStructuredSelection) selection).getFirstElement();
					if (element instanceof EObject) {
						final int elementIndex = inputs.indexOf(element);
						if (selectedObjectIndex >= elementIndex) {
							selectedObjectIndex--;
						}

						if (selectedObjectIndex < 0) {
							selectedObjectIndex = 0;
						}

						deletedInputs.add((EObject) element);

						// remove from change map
						for (final EObject o : ranges.get(element)) {
							// o is the duplicate for a real thing,
							// which is being deleted; forget about
							// both sides.
							final EObject original = duplicateToOriginal.get(o);
							originalToDuplicate.remove(original);
							duplicateToOriginal.remove(o);
							dialogValidationSupport.getValidationContext().ignore(o);
							dialogValidationSupport.getValidationContext().ignore(original);
						}

						ranges.remove(element);
						inputs.remove(elementIndex);
						if (selectedObjectIndex >= 0 && inputs.size() > 0) {
							selectionViewer.setSelection(new StructuredSelection(inputs.get(selectedObjectIndex)));
						} else {
							selectionViewer.setSelection(StructuredSelection.EMPTY);
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
			backButton = createButton(parent, IDialogConstants.BACK_ID, IDialogConstants.BACK_LABEL, true);
			nextButton = createButton(parent, IDialogConstants.NEXT_ID, IDialogConstants.NEXT_LABEL, true);
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

	public int open(final IScenarioEditingLocation editorPart, final MMXRootObject rootObject, final List<EObject> objects) {
		return open(editorPart, rootObject, objects, false);
	}

	private boolean lockedForEditing = false;

	private MMXRootObject rootObject;

	private EReference sidebarContainment;

	private EObject sidebarContainer;

	/**
	 * This version of the open method also displays the sidebar and allows for creation and deletion of objects in the sidebar.
	 * 
	 * @param part
	 * @param rootObject
	 * @param container
	 * @param containment
	 * @return
	 */
	public int open(final IScenarioEditingLocation part, final MMXRootObject rootObject, final EObject container, final EReference containment) {
		this.scenarioEditingLocation = part;
		dialogValidationSupport = new DialogValidationSupport(scenarioEditingLocation.getExtraValidationContext());
		this.rootObject = rootObject;
		lockedForEditing = false;
		this.inputs.clear();
		this.originalToDuplicate.clear();
		this.duplicateToOriginal.clear();
		this.displaySidebarList = true;
		inputs.addAll((List) container.eGet(containment));

		this.sidebarContainer = container;
		this.sidebarContainment = containment;

		scenarioEditingLocation.pushExtraValidationContext(dialogValidationSupport.getValidationContext());
		try {
			final int value = open();
			if (value == OK) {
				final CompoundCommand cc = new CompoundCommand();

				final EditingDomain editingDomain = commandHandler.getEditingDomain();
				if (editingDomain instanceof CommandProviderAwareEditingDomain) {
					((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(true);
				}
				try {
					cc.append(dialogEcoreCopier.createEditCommand());
				} finally {
					if (editingDomain instanceof CommandProviderAwareEditingDomain) {
						((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(false);
					}
				}

				// new objects should already be added wherever they belong.

				// delete any objects which have been removed from the sidebar
				// list
				if (!deletedInputs.isEmpty()) {
					final Command cmd = DeleteCommand.create(commandHandler.getEditingDomain(), deletedInputs);
					if (cmd != null) {
						cc.append(cmd);
					}
				}
				if (!cc.isEmpty()) {
					executeFinalCommand(cc);
				}
			} else {
				// any new objects will have been added from the inputs list, so
				// we have to delete them
				// we want to directly execute the command so that it doesn't go
				// into the undo stack.
				if (!addedInputs.isEmpty()) {
					final Command delete = DeleteCommand.create(commandHandler.getEditingDomain(), addedInputs);
					if (delete.canExecute()) {
						// System.err.println("Execute delete");
						delete.execute();
					} else {
						log.error("Cannot execute delete", new RuntimeException());
					}
				}
			}
			return value;
		} finally {

			final CommandProviderAwareEditingDomain domain;
			{
				final EditingDomain ed = commandHandler.getEditingDomain();
				if (ed instanceof CommandProviderAwareEditingDomain)
					domain = (CommandProviderAwareEditingDomain) ed;
				else
					domain = null;
			}

			if (domain != null) {
				for (final EObject original : originalToDuplicate.keySet()) {
					domain.clearOverride(original);
					domain.removeEditObject(originalToDuplicate.get(original));
				}
			}

			scenarioEditingLocation.popExtraValidationContext();
		}
	}

	public int open(final IScenarioEditingLocation part, final MMXRootObject rootObject, final List<EObject> objects, final boolean locked) {
		this.scenarioEditingLocation = part;
		dialogValidationSupport = new DialogValidationSupport(scenarioEditingLocation.getExtraValidationContext());
		scenarioEditingLocation.pushExtraValidationContext(dialogValidationSupport.getValidationContext());
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
					for (final Map.Entry<EObject, EObject> entry : originalToDuplicate.entrySet()) {
						final EObject original = entry.getKey();
						final EObject duplicate = entry.getValue();

						if (duplicate instanceof UUIDObject) {
							((UUIDObject) duplicate).eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						}

						adder.append(AddCommand.create(commandHandler.getEditingDomain(), original.eContainer(), original.eContainingFeature(), Collections.singleton(duplicate)));
					}
					final boolean isExecutable = adder.canExecute();
					if (isExecutable) {
						executeFinalCommand(adder);
					} else {
						MessageDialog.openError(getShell(), "Error applying change",
								"An error occurred applying the change - the command to apply it was not executable. Refer to the error log for more details");
						log.error("Unable to apply change", new RuntimeException("Unable to apply change"));
					}

				} else {

					final CompoundCommand cc = new CompoundCommand();

					final EditingDomain editingDomain = commandHandler.getEditingDomain();
					if (editingDomain instanceof CommandProviderAwareEditingDomain) {
						((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(true);
					}
					try {
						cc.append(dialogEcoreCopier.createEditCommand());
					} finally {
						if (editingDomain instanceof CommandProviderAwareEditingDomain) {
							((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(false);
						}
					}

					// TODO check this experimental option works properly
					// cc.append(replaceOriginals(commandHandler.getEditingDomain(), rootObject));

					final boolean isExecutable = cc.canExecute();
					if (isExecutable) {

						executeFinalCommand(cc);

					} else {
						MessageDialog.openError(getShell(), "Error applying change",
								"An error occurred applying the change - the command to apply it was not executable. Refer to the error log for more details");
						log.error("Unable to apply change", new RuntimeException("Unable to apply change"));
					}
				}
			}
			return value;
		} finally {
			final CommandProviderAwareEditingDomain domain;
			{
				final EditingDomain ed = commandHandler.getEditingDomain();
				if (ed instanceof CommandProviderAwareEditingDomain)
					domain = (CommandProviderAwareEditingDomain) ed;
				else
					domain = null;
			}

			if (domain != null) {
				for (final EObject original : originalToDuplicate.keySet()) {
					domain.clearOverride(original);
					domain.removeEditObject(originalToDuplicate.get(original));
				}
			}

			scenarioEditingLocation.popExtraValidationContext();
		}
	}

	private void executeFinalCommand(final CompoundCommand cc) {
		commandHandler.getEditingDomain().getCommandStack().execute(cc);
	}

	private void validate() {
		final IStatus status = dialogValidationSupport.validate();

		if (status.isOK()) {
			managedForm.getForm().setMessage(null, IMessageProvider.NONE);
		} else {
			final StringBuilder sb = new StringBuilder();
			processMessages(sb, status);

			managedForm.getForm().setMessage(sb.toString().trim(), convertType(status.getSeverity()));
		}

		if (displayComposite != null) {
			displayComposite.displayValidationStatus(status);
		}

		checkButtonEnablement(true);// !status.matches(IStatus.ERROR));
	}

	private void processMessages(final StringBuilder sb, final IStatus status) {
		if (status.isMultiStatus()) {
			for (final IStatus s : status.getChildren()) {
				processMessages(sb, s);
			}
		} else {
			sb.append(status.getMessage());
			sb.append("\n");
		}
	}
}
