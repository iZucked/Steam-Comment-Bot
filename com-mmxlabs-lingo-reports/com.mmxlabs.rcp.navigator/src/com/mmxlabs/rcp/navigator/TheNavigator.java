package com.mmxlabs.rcp.navigator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.impl.JobManagerListener;
import com.mmxlabs.rcp.common.actions.PackTreeColumnsAction;

public class TheNavigator extends CommonNavigator {

	final IJobManagerListener jobManagerListener = new JobManagerListener() {

		/**
		 * Update the checked status of items in the tree
		 * 
		 * @param item
		 * @param res
		 * @param check
		 */
		private void checkItems(final TreeItem item, final IResource res,
				final boolean check) {

			// See if we have a match
			if (item.getData() instanceof IResource) {
				final IResource resource = (IResource) item.getData();
				if (resource == res) {
					item.setChecked(check);
					// TODO: Break out of recursive loop?
				}
			}
			// Recurse down the tree...
			for (final TreeItem i : item.getItems()) {
				checkItems(i, res, check);
			}
		}

		@Override
		public void jobSelected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {

			final TreeItem[] items = TheNavigator.this.getCommonViewer()
					.getTree().getItems();
			for (final TreeItem i : items) {
				checkItems(i, resource, true);
			}
		}

		@Override
		public void jobDeselected(final IJobManager jobManager,
				final IManagedJob job, final IResource resource) {
			final TreeItem[] items = TheNavigator.this.getCommonViewer()
					.getTree().getItems();
			for (final TreeItem i : items) {
				checkItems(i, resource, false);
			}
		}
	};

	@Override
	protected CommonViewer createCommonViewerObject(final Composite aParent) {
		return new CommonViewer(getViewSite().getId(), aParent, SWT.MULTI
				| SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL /*
																	 * |
																	 * SWT.CHECK
																	 */);
	}

	@Override
	protected CommonViewer createCommonViewer(final Composite aParent) {

		final CommonViewer aViewer = createCommonViewerObject(aParent);

		final Tree treeTable = aViewer.getTree();

		treeTable.setHeaderVisible(true);
		treeTable.setLinesVisible(true);
		// treeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		// treeTable.setFont(aParent.getFont());
		// PlatformUI.getWorkbench().getHelpSystem()
		// .setHelp(treeTable, ContextIds.VIEW_SERVERS);

		// add columns
		final TreeColumn column = new TreeColumn(treeTable, SWT.SINGLE);
		column.setText("Resource");
		column.setWidth(150);
		// column.setWidth(cols[0]);
		// column.addSelectionListener(getHeaderListener(0));
		treeTable.setSortColumn(column);
		treeTable.setSortDirection(SWT.UP);

		final TreeColumn column2 = new TreeColumn(treeTable, SWT.SINGLE);

		column2.setText("Status");
		column2.setWidth(150);
		// column2.setWidth(cols[1]);
		// column2.addSelectionListener(getHeaderListener(1));
		
		initListeners(aViewer);
		aViewer.getNavigatorContentService().restoreState(memento);

		treeTable.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(final Event event) {
				if (event.item instanceof Button
						&& event.item.getData() instanceof IResource) {
					final IResource resource = (IResource) event.item.getData();
					final Button button = (Button) event.item;

					// Set selection status
					Activator
							.getDefault()
							.getJobManager()
							.setResourceSelection(resource,
									button.getSelection());
				}
			}
		});

		Activator.getDefault().getJobManager()
				.addJobManagerListener(jobManagerListener);

		final Action a = new PackTreeColumnsAction(aViewer);
		final IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(a);

		bars.getToolBarManager().update(true);

		return aViewer;
	}

	@Override
	public void dispose() {

		Activator.getDefault().getJobManager()
				.removeJobManagerListener(jobManagerListener);

		super.dispose();
	}
}
