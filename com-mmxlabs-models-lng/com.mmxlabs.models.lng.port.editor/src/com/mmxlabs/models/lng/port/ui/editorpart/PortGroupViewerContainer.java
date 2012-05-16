package com.mmxlabs.models.lng.port.ui.editorpart;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.NamedObject;

public class PortGroupViewerContainer {
	protected TableViewer groupViewer;
	protected CheckboxTableViewer contentViewer;
	private PortModel portModel;
	
	public void createViewer(final Composite container) {
		final Composite inner = new Composite(container, SWT.NONE);
		inner.setLayout(new GridLayout(2, true));
		
		final Composite g = new Composite(inner, SWT.NONE);
		
		g.setLayout(new GridLayout(1, true));
		g.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final Composite g_buttons = new Composite(g, SWT.NONE);
		g_buttons.setLayout(new GridLayout(2, false));
		g_buttons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		
		
		groupViewer = new TableViewer(g, SWT.SINGLE|SWT.V_SCROLL|SWT.BORDER);
		
		groupViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		contentViewer = CheckboxTableViewer.newCheckList(inner, SWT.SINGLE|SWT.V_SCROLL|SWT.BORDER);
		
		contentViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		
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
							((PortGroup) o).getContents().add((APortSet) p);
						else
							((PortGroup) o).getContents().remove(p);
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
}
