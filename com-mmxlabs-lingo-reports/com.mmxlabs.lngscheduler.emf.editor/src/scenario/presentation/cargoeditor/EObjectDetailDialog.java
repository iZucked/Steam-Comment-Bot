/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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

import scenario.presentation.cargoeditor.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditorFactory;

import com.mmxlabs.common.Pair;

/**
 * A dialog-type container for an EObjectDetailView, which pops up modally and
 * collects commands into a buffer, rather than doing them immediately like the
 * EObjectPropertySheetPage.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectDetailDialog extends Dialog implements IDetailViewContainer {
	final ICommandProcessor processor;
	final Map<WeaklyEqualPair<EObject, EStructuralFeature>, Command> commandBuffer = new HashMap<WeaklyEqualPair<EObject, EStructuralFeature>, Command>();
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

	private class WeaklyEqualPair<A, B> extends Pair<A, B> {
		public WeaklyEqualPair(A first, B second) {
			super(first, second);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof WeaklyEqualPair) {
				final WeaklyEqualPair other = (WeaklyEqualPair) obj;
				return other.getFirst() == getFirst()
						&& other.getSecond() == getSecond();
			}
			return false;
		}
	}

	public EObjectDetailDialog(final Shell parent, final int style,
			final EditingDomain editingDomain) {
		super(parent, style);
		this.editingDomain = editingDomain;
		processor = new ICommandProcessor() {
			@Override
			public void processCommand(Command command, EObject target,
					EStructuralFeature feature) {
				commandBuffer.put(
						new WeaklyEqualPair<EObject, EStructuralFeature>(
								target, feature), command);
			}
		};

		viewContainerDelegate.addDefaultEditorFactories();
	}

	/**
	 * Open and edit the given objects modally, returning a collection of
	 * changed objects
	 * 
	 * @param objects
	 * @return
	 */
	public Collection<EObject> open(final List<EObject> objects) {
		commandBuffer.clear();

		selectedObjectIndex = 0;

		final Shell shell = createShell(objects);

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
			final HashSet<EObject> touchedObjects = new HashSet<EObject>();
			final CompoundCommand cc = new CompoundCommand();
			for (final Map.Entry<WeaklyEqualPair<EObject, EStructuralFeature>, Command> entry : commandBuffer
					.entrySet()) {
				cc.append(entry.getValue());
				touchedObjects.add(entry.getKey().getFirst());
			}

			editingDomain.getCommandStack().execute(cc);

			return touchedObjects;
		} else {
			return Collections.emptySet();
		}
	}

	int selectedObjectIndex = 0;

	/**
	 * @return
	 */
	private Shell createShell(final List<EObject> objects) {
		final Shell shell = new Shell(getParent(), getStyle()
				| (SWT.DIALOG_TRIM & ~SWT.CLOSE) | SWT.APPLICATION_MODAL);

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

		Button btnCancel = new Button(composite_1, SWT.NONE);
		btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));

		btnCancel.setText("Cancel");

		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				performActions = false;
				shell.close();
			}
		});

		Button btnOk = new Button(composite_1, SWT.NONE);
		shell.setDefaultButton(btnOk);

		btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));

		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				performActions = true;
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

		// hide other views and display the new one
		if (eodv != activeDetailView) {
			// make view visible
			if (activeDetailView != null) {
				((GridData) activeDetailView.getLayoutData()).exclude = true;
				activeDetailView.setVisible(false);
				activeDetailView.setInput(null);
			}
			activeDetailView = eodv;
			((GridData) activeDetailView.getLayoutData()).exclude = false;
			activeDetailView.setVisible(true);
		}

		eodv.setInput(selection);
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
