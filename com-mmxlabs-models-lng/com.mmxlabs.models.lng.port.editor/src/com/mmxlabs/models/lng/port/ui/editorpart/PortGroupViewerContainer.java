/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

public class PortGroupViewerContainer {
	protected TableViewer groupViewer;
	protected CheckboxTableViewer contentViewer;
	private PortModel portModel;
	private EditingDomain editingDomain;
	
	public void setEditingDomain(final EditingDomain domain) {
		this.editingDomain = domain;
	}
	
	public void createViewer(final Composite container) {
		final SashForm sash = new SashForm(container, SWT.VERTICAL);
		
		groupViewer = new TableViewer(sash, SWT.SINGLE|SWT.V_SCROLL|SWT.BORDER);
		
		contentViewer = CheckboxTableViewer.newCheckList(sash, SWT.SINGLE|SWT.V_SCROLL|SWT.BORDER);
		
		sash.setWeights(new int[]{1,9});
		
		final ILabelProvider nameLabelProvider = new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof NamedObject) {
					return ((NamedObject) element).getName();
				} else {
					return "";
				}
			}
		};
		
		contentViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				viewer.refresh();
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof PortModel) {
					return ((PortModel) inputElement).getPorts().toArray();
				}
				return new Object[0];
			}
		});
		
		groupViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				viewer.refresh();
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof PortModel) {
					return ((PortModel) inputElement).getPortGroups().toArray();
				}
				return new Object[0];
			}
		});
		
		contentViewer.setLabelProvider(nameLabelProvider);
		groupViewer.setLabelProvider(nameLabelProvider);
		
		contentViewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				return false;
			}
			
			@Override
			public boolean isChecked(final Object element) {
				final ISelection selection = groupViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object o = ((IStructuredSelection) selection).getFirstElement();
					if (o instanceof PortGroup) {
						return ((PortGroup) o).getContents().contains(element);
					}
				}
				
				return false;
			}
		});
		
		contentViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				final Object p = event.getElement();
				final ISelection selection = groupViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object o = ((IStructuredSelection) selection).getFirstElement();
					
					if (o instanceof PortGroup) {
						if (event.getChecked()) 
							editingDomain.getCommandStack().execute(
									AddCommand.create(editingDomain, o, PortPackage.eINSTANCE.getPortGroup_Contents(), p));
						else
							editingDomain.getCommandStack().execute(
									RemoveCommand.create(editingDomain, o, PortPackage.eINSTANCE.getPortGroup_Contents(), p));
					}
				}
			}
		});
		
		groupViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object o = ((IStructuredSelection) selection).getFirstElement();
					if (o instanceof PortGroup) {
						contentViewer.setInput(portModel);
						return;
					}
				}
				contentViewer.setInput(null);
			}
		});
	}
	
	public void setInput(final PortModel portModel) {
		this.portModel = portModel;
		groupViewer.setInput(portModel);
		contentViewer.setInput(null);		
		groupViewer.refresh();
		contentViewer.refresh();
	}

	public Viewer getGroupViewer() {
		return groupViewer;
	}
}
