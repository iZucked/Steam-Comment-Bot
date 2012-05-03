package com.mmxlabs.scenario.service.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.TableView;

import javax.swing.text.TabableView;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;

public class ScenarioServiceSelectionGroup extends Composite {
	// The listener to notify of events
	private final Listener listener;

	private Container selectedContainer;

	private TreeViewer treeViewer;

	public ScenarioServiceSelectionGroup(final Composite parent, final Listener listener, int style) {
		super(parent, style);
		this.listener = listener;
		createContents();
	}

	public void createContents() {
		final GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		setLayout(layout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		treeViewer = new TreeViewer(this, SWT.NONE);
		final ScenarioServiceContentProvider cp = new ScenarioServiceContentProvider();
		cp.setShowOnlyContainers(true);
		treeViewer.setContentProvider(cp);
		treeViewer.setLabelProvider(new ScenarioServiceLabelProvider());
		treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event) {
				final IStructuredSelection selection = (IStructuredSelection) event.getSelection();

				final Object firstElement = selection.getFirstElement();
				if (firstElement instanceof Container) {
					containerSelectionChanged((Container) firstElement);
				} else {
					containerSelectionChanged(null);
				}
			}
		});

		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(final DoubleClickEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object item = ((IStructuredSelection) selection).getFirstElement();
					if (item == null) {
						return;
					}
					if (treeViewer.getExpandedState(item)) {
						treeViewer.collapseToLevel(item, 1);
					} else {
						treeViewer.expandToLevel(item, 1);
					}
				}
			}
		});
	}

	public void setSelectedContainer(final Container container) {
		selectedContainer = container;

		// expand to and select the specified container
		final List<Object> itemsToExpand = new ArrayList<Object>();
		Container parent = container.getParent();
		while (parent != null) {
			itemsToExpand.add(0, parent);
			parent = parent.getParent();
		}
		treeViewer.setExpandedElements(itemsToExpand.toArray());
		treeViewer.setSelection(new StructuredSelection(container), true);
	}

	private void containerSelectionChanged(final Container container) {
		selectedContainer = container;

		if (listener != null) {
			final Event changeEvent = new Event();
			changeEvent.type = SWT.Selection;
			changeEvent.widget = this;
			listener.handleEvent(changeEvent);
		}
	}

	public Container getSelectedContainer() {
		return selectedContainer;
	}

	public void setInput(final ScenarioServiceRegistry registry) {
		treeViewer.setInput(registry);
	}
}
