/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditorFactory;

/**
 * Why does tidy imports keep stripping my class comments? A dialog based
 * container for an EObjectDetailView, which pops up modally and executes
 * commands on copies of the original objects, making the changes properly once
 * OK is pressed.
 * 
 * TODO: make dialog hang around in memory, so that it's quicker to pop up.
 * TODO: because we are buffering the commands, the multi-edit doesn't work
 * properly. solution: duplicate input objects, apply changes, diff with
 * original, create command to update original, execute command. This also helps
 * with validation.
 * 
 * TODO use jface dialog not SWT dialog.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectDetailDialog extends Dialog implements IDetailViewContainer {
	final ICommandProcessor processor;
	final EditingDomain editingDomain;

	final EObjectDetailViewContainer viewContainerDelegate = new EObjectDetailViewContainer() {
		@Override
		protected ICommandProcessor getProcessor() {
			return processor;
		}

		@Override
		protected EditingDomain getEditingDomain() {
			return editingDomain;
		}
	};

	public EObjectDetailDialog(final Shell parent, final int style,
			final EditingDomain editingDomain) {
		super(parent, style);
		this.editingDomain = editingDomain;
		processor = new ICommandProcessor() {
			@Override
			public void processCommand(Command command, EObject target,
					EStructuralFeature feature) {
				//commands need not be in the stack, as we don't care for undoing them
				command.execute();
			}
		};

//		viewContainerDelegate.addDefaultEditorFactories();
	}

	/**
	 * Open and edit the given objects modally, returning a collection of
	 * changed objects
	 * 
	 * @param objects
	 * @return
	 */
	public Collection<EObject> open(final List<EObject> objects) {
		final List<EObject> duplicates = new ArrayList<EObject>(objects.size());

		for (final EObject original : objects) {
			duplicates.add(EcoreUtil.copy(original));
		}

		selectedObjectIndex = 0;

		final Shell shell = createShell(duplicates);

		shell.pack();
		shell.layout();

		// center in parent window
		final Rectangle shellBounds = getParent().getBounds();
		final Point dialogSize = shell.getSize();

		shell.setLocation(shellBounds.x + (shellBounds.width - dialogSize.x)
				/ 2, shellBounds.y + (shellBounds.height - dialogSize.y) / 2);

		shell.open();
		// run event loop and then close
		final Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		if (performActions) {
			final CompoundCommand cc = new CompoundCommand();
			for (int i = 0; i < objects.size(); i++) {
				final EObject original = objects.get(i);
				final EObject duplicate = duplicates.get(i);
				if (!original.equals(duplicate)) {
					cc.append(makeEqualizer(original, duplicate));
					System.err.println("Equalizing " + original + " with "
							+ duplicate);
				}
			}
			if (cc.canExecute()) {
				editingDomain.getCommandStack().execute(cc);
			} else {
				System.err
						.println("Something is wrong with the equalizing command");
			}
			// TODO this is wrong, return only modified objects
			return objects;
		} else {
			return Collections.emptySet();
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

		for (final EStructuralFeature feature : original.eClass()
				.getEAllStructuralFeatures()) {
			if (original.eGet(feature).equals(duplicate.eGet(feature)))
				continue;
			if (feature.isMany()) {
				System.err.println("Multiple valued feature: " + feature);
				Collection c = (Collection) original.eGet(feature);
				if (c.size() > 0)
					compound.append(RemoveCommand.create(editingDomain,
							original, feature, c));
				// new values
				c = (Collection) duplicate.eGet(feature);
				if (c.size() > 0)
					compound.append(AddCommand.create(editingDomain, original,
							feature, (Collection) duplicate.eGet(feature)));
			} else {
				compound.append(SetCommand.create(editingDomain, original,
						feature, duplicate.eGet(feature)));
			}
		}

		return compound;
	}

	int selectedObjectIndex = 0;

	/**
	 * @return
	 */
	private Shell createShell(final List<EObject> objects) {
		final Shell shell = new Shell(getParent(), getStyle()
				| (SWT.DIALOG_TRIM & ~SWT.CLOSE) | SWT.APPLICATION_MODAL
				| SWT.RESIZE);
		
//		shell.setSize(389, 197);

		shell.setText("Editing (1/" + objects.size() + ")");

		final GridLayout shellLayout = new GridLayout();
		shell.setLayout(shellLayout);

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		composite.setLayout(new GridLayout(1, false));

		// insert EObjectDetailView

		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		composite_1.setLayout(new GridLayout(4, false));

		Button btnPrev = new Button(composite_1, SWT.NONE);
		btnPrev.setText("&Back");
		btnPrev.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_BACK));

		btnPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedObjectIndex--;
				if (selectedObjectIndex < 0)
					selectedObjectIndex = objects.size() - 1;
				displaySelectedObject(composite,
						objects.get(selectedObjectIndex));
				shell.setText("Editing (" + (selectedObjectIndex + 1) + "/"
						+ objects.size() + ")");
			}
		});

		Button btnNext = new Button(composite_1, SWT.NONE);
		GridData gd_btnNext = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_btnNext.heightHint = 26;
		btnNext.setLayoutData(gd_btnNext);
		btnNext.setText("&Forward");
		btnNext.setImage(PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_TOOL_FORWARD));

		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedObjectIndex++;
				if (selectedObjectIndex >= objects.size())
					selectedObjectIndex = 0;
				displaySelectedObject(composite,
						objects.get(selectedObjectIndex));

				shell.setText("Editing (" + (selectedObjectIndex + 1) + "/"
						+ objects.size() + ")");
			}
		});

		Button btnOk = new Button(composite_1, SWT.NONE);
		shell.setDefaultButton(btnOk);

		GridData gd_btnOk = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1);
		gd_btnOk.widthHint = 60;
		gd_btnOk.heightHint = 26;
		btnOk.setLayoutData(gd_btnOk);

		btnOk.setText("&OK");
		btnOk.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				performActions = true;
				shell.close();
			}

		});

		Button btnCancel = new Button(composite_1, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnCancel.widthHint = 60;
		gd_btnCancel.heightHint = 26;
		btnCancel.setLayoutData(gd_btnCancel);

		btnCancel.setText("&Cancel");

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				performActions = false;
				shell.close();
			}
		});

		displaySelectedObject(composite, objects.get(selectedObjectIndex));

		return shell;
	}

	protected boolean performActions = false;

	private EObjectDetailView activeDetailView = null;

	/**
	 * @param composite
	 */
	private void displaySelectedObject(final Composite composite,
			final EObject selection) {

		final EObjectDetailView eodv = viewContainerDelegate.getDetailView(
				selection.eClass(), composite);

		boolean changed = false;
		// hide other views and display the new one
		if (eodv != activeDetailView) {
			// make view visible
			if (activeDetailView != null) {
				((GridData) activeDetailView.getLayoutData()).exclude = true;
				activeDetailView.setVisible(false);
				activeDetailView.setInput((EObject)null);
			}
			activeDetailView = eodv;
			((GridData) activeDetailView.getLayoutData()).exclude = false;
			activeDetailView.setVisible(true);
			changed = true;
		}

		eodv.setInput(selection);

		if (changed) {
			final Shell shell = composite.getShell();
			shell.layout(true);

			shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			final Rectangle shellBounds = getParent().getBounds();
			final Point dialogSize = shell.getSize();

			shell.setLocation(shellBounds.x
					+ (shellBounds.width - dialogSize.x) / 2, shellBounds.y
					+ (shellBounds.height - dialogSize.y) / 2);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scenario.presentation.cargoeditor.IDetailViewContainer#
	 * addDefaultEditorFactories()
	 */
	@Override
	public void addDefaultEditorFactories() {
		viewContainerDelegate.addDefaultEditorFactories();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * scenario.presentation.cargoeditor.IDetailViewContainer#setNameForFeature
	 * (org.eclipse.emf.ecore.EStructuralFeature, java.lang.String)
	 */
	@Override
	public void setNameForFeature(EStructuralFeature feature, String string) {
		viewContainerDelegate.setNameForFeature(feature, string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scenario.presentation.cargoeditor.IDetailViewContainer#
	 * setEditorFactoryForClassifier(org.eclipse.emf.ecore.EClassifier,
	 * scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditorFactory)
	 */
	@Override
	public void setEditorFactoryForClassifier(EClassifier classifier,
			IInlineEditorFactory factory) {
		viewContainerDelegate
				.setEditorFactoryForClassifier(classifier, factory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scenario.presentation.cargoeditor.IDetailViewContainer#
	 * setEditorFactoryForFeature(org.eclipse.emf.ecore.EStructuralFeature,
	 * scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditorFactory)
	 */
	@Override
	public void setEditorFactoryForFeature(EStructuralFeature feature,
			IInlineEditorFactory iInlineEditorFactory) {
		viewContainerDelegate.setEditorFactoryForFeature(feature,
				iInlineEditorFactory);
	}

}
