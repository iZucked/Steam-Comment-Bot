/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.EObjectEditorViewerPane;
import scenario.presentation.cargoeditor.detailview.EObjectDetailDialog;
import scenario.presentation.cargoeditor.detailview.EObjectMultiDialog;
import scenario.presentation.cargoeditor.handlers.AddAction;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.lngscheduler.emf.extras.validation.context.ValidationSupport;

/**
 * This extension of {@link EObjectEditorViewerPane} adds the following
 * <ol>
 * <li>A handler which pops up either an {@link EObjectDetailDialog} or an
 * {@link EObjectMultiDialog} when you hit return</li>
 * <li>
 * An add action which displays an {@link EObjectDetailDialog} when you add a
 * new element</li>
 * 
 * @author Tom Hinton
 * 
 */
public class ScenarioObjectEditorViewerPane extends EObjectEditorViewerPane {
	final static protected EAttribute namedObjectName = ScenarioPackage.eINSTANCE
			.getNamedObject_Name();

	/**
	 * @param page
	 * @param part
	 */
	public ScenarioObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
	}

	/**
	 * A convenience version of {@link #init(List, AdapterFactory)} which takes
	 * varargs.
	 * 
	 * @param adapterFactory
	 * @param path
	 */
	public void init(final AdapterFactory adapterFactory,
			final EReference... path) {
		final List<EReference> list = Arrays.asList(path);
		init(list, adapterFactory);
	}

	/**
	 * Create a custom add action, which delegates to the default behaviour to
	 * create the object, but adds in an editor dialog.
	 */
	@Override
	protected Action createAddAction(final TableViewer viewer,
			final EditingDomain editingDomain, final EMFPath contentPath) {
		final AddAction delegate = (AddAction) super.createAddAction(viewer,
				editingDomain, contentPath);

		final Action result = new AddAction(editingDomain, contentPath
				.getTargetType().getName()) {
			@Override
			public EObject createObject(final boolean usingSelection) {
				final EObject newObject = delegate.createObject(usingSelection);

				try {
					ValidationSupport.getInstance().setContainers(
							Collections.singletonList(newObject),
							(EObject) getOwner(), (EReference) getFeature());

					final EObjectDetailDialog dialog = new EObjectDetailDialog(
							viewer.getControl().getShell(), SWT.NONE,
							editingDomain);

					part.setupDetailViewContainer(dialog);

					if (dialog.open(Collections.singletonList(newObject))
							.size() > 0) {
						return newObject;
					} else {
						return null;
					}
				} finally {
					ValidationSupport.getInstance().clearContainers(
							Collections.singletonList(newObject));
				}
			}

			@Override
			public Object getOwner() {
				return delegate.getOwner();
			}

			@Override
			public Object getFeature() {
				return delegate.getFeature();
			}
		};
		return result;
	}

	@Override
	/**
	 * Hook a key listener to the viewer to pick up return and display an editor.
	 * 
	 * TODO cache editors for efficiency.
	 */
	public Viewer createViewer(final Composite parent) {
		final Viewer v = super.createViewer(parent);
		v.getControl().addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final org.eclipse.swt.events.KeyEvent e) {

				// TODO: Wrap up in a command with keybindings
				// TODO if you edit a cell and then press return to finish
				// editing,
				// this listener still happens. Either we need to cancel the
				// event,
				// or detect editing from the viewer, or set our own flags
				// to prevent this problem.
				if (e.keyCode == '\r') {
					final ISelection selection = getViewer().getSelection();
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ssel = (IStructuredSelection) selection;
						final List l = Arrays.asList(ssel.toArray());

						if (l.size() > 1 && (e.stateMask & SWT.CONTROL) == 0) {
							final EObjectMultiDialog multiDialog = new EObjectMultiDialog(
									new IShellProvider() {
										@Override
										public Shell getShell() {
											return v.getControl().getShell();
										}
									});
							part.setupDetailViewContainer(multiDialog);
							multiDialog.setEditorFactoryForFeature(
									CargoPackage.eINSTANCE.getCargo_Id(), null);
							multiDialog.setEditorFactoryForFeature(
									ScenarioPackage.eINSTANCE
											.getNamedObject_Name(), null);

							multiDialog.setEditorFactoryForFeature(
									FleetPackage.eINSTANCE.getVesselEvent_Id(),
									null);
							if (multiDialog.open(l, part.getEditingDomain()) == Dialog.OK) {
								part.getEditingDomain().getCommandStack()
										.execute(multiDialog.createCommand());
							}
						} else {
							if (l.size() == 0)
								return;
							final EObjectDetailDialog dialog = new EObjectDetailDialog(
									v.getControl().getShell(), SWT.NONE, part
											.getEditingDomain());

							part.setupDetailViewContainer(dialog);

							if (dialog.open(l).size() > 0)
								getViewer().refresh();
						}
					}
				}
			}

			@Override
			public void keyPressed(final org.eclipse.swt.events.KeyEvent e) {
			}
		});
		return v;
	}
}