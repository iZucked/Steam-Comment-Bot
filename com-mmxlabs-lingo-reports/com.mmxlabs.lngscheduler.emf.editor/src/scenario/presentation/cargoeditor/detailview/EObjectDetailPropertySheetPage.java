/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
/**
 * 
 */
package scenario.presentation.cargoeditor.detailview;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;

/**
 * @author Tom Hinton
 * 
 */
public class EObjectDetailPropertySheetPage extends EObjectDetailViewContainer implements IPropertySheetPage {
	private Composite control;// , top;
	private ScrolledComposite sc;

	private final EditingDomain editingDomain;
	private final ICommandProcessor processor;

	public EObjectDetailPropertySheetPage(final EditingDomain editingDomain) {
		this.processor = new ICommandProcessor() {
			@Override
			public void processCommand(final Command command,
					final EObject target,
					final EStructuralFeature feature) {
				editingDomain.getCommandStack().execute(command);
			}
		};
		this.editingDomain = editingDomain;
//		addDefaultEditorFactories();
	}

	@Override
	public void createControl(final Composite parent) {

		sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setLayout(new FillLayout());
		control = new Composite(sc, SWT.NONE);
		sc.setContent(control);

		sc.setExpandHorizontal(true);
		// sc.setExpandVertical(false);
		control.setLayout(new GridLayout(1, false));
	}

	@Override
	public void dispose() {
		control.dispose();
	}

	@Override
	public Control getControl() {
		return sc;
	}

	@Override
	public void setActionBars(IActionBars actionBars) {

	}

	@Override
	public void setFocus() {
		control.setFocus(); // TODO set focus to child entry
	}

	private EObjectDetailView activeDetailView = null;
	private boolean selectionChanging = false;

	@Override
	public void selectionChanged(final IWorkbenchPart part,
			final ISelection selection) {
		// watch out of re-entry
		if (selectionChanging) {
			System.err.println("re-entrant selection?");
			return;
		}
		selectionChanging = true;
		try {
			if (selection instanceof IStructuredSelection) {
				final Object object = ((IStructuredSelection) selection)
						.getFirstElement();

				if (object instanceof EObject) {
					final EObjectDetailView eodv = getDetailView(((EObject) object)
							.eClass(), control);
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
					}
					eodv.setInput((EObject) object);
				} else {
					if (activeDetailView != null) {
						((GridData) activeDetailView.getLayoutData()).exclude = true;
						activeDetailView.setVisible(false);
						activeDetailView.setInput((EObject)null);
					}
					activeDetailView = null;
				}
				control.layout(true);
				control.setSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				// sc.setContent(null);
				// sc.setContent(control);
			}
		} finally {
			selectionChanging = false;
		}
	}

	
	/* (non-Javadoc)
	 * @see scenario.presentation.cargoeditor.EObjectDetailViewContainer#getProcessor()
	 */
	@Override
	protected ICommandProcessor getProcessor() {
		return processor;
	}

	@Override
	protected EditingDomain getEditingDomain() {
		return editingDomain;
	}
}
