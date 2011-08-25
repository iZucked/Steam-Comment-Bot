/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
/**
 * 
 */
package com.mmxlabs.shiplingo.ui.detailview.containers;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;


import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.editors.ICommandProcessor;

/**
 * A property sheet page for displaying detail composites. 
 * 
 * @author Tom Hinton
 * 
 */
public class DetailCompositePropertySheetPage extends DetailCompositeContainer
		implements IPropertySheetPage {
	private Composite control;// , top;
	private ScrolledComposite sc;

	public DetailCompositePropertySheetPage(final EditingDomain editingDomain,
			final IValueProviderProvider valueProviderProvider) {
		super(valueProviderProvider, editingDomain, new ICommandProcessor() {
			@Override
			public void processCommand(final Command command,
					final EObject target, final EStructuralFeature feature) {
				editingDomain.getCommandStack().execute(command);
			}
		});
	}

	@Override
	public void createControl(final Composite parent) {
		sc = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		sc.setLayout(new FillLayout());
		control = new Composite(sc, SWT.NONE);
		sc.setContent(control);

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(false);
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

	private AbstractDetailComposite activeDetailView = null;
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
					final AbstractDetailComposite eodv = getDetailView(
							((EObject) object).eClass(), control);
					if (eodv != activeDetailView) {
						// make view visible
						if (activeDetailView != null) {
							((GridData) activeDetailView.getLayoutData()).exclude = true;
							activeDetailView.setVisible(false);
							activeDetailView.setInput((EObject) null);
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
						activeDetailView.setInput((EObject) null);
					}
					activeDetailView = null;
				}
				final Point p;
				if (activeDetailView != null) {
					p = activeDetailView.getPreferredSize(sc.getSize().x - 16);
				} else {
					p = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				}
				control.setSize(p);
				sc.setMinSize(p);
				control.layout(true);
			}
		} finally {
			selectionChanging = false;
		}
	}
}
