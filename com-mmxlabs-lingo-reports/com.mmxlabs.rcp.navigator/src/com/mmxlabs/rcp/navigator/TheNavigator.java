package com.mmxlabs.rcp.navigator;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.IJobManager;
import com.mmxlabs.jobcontroller.core.IJobManagerListener;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

public class TheNavigator extends CommonNavigator {

	@Override
	protected CommonViewer createCommonViewerObject(final Composite aParent) {
		return new CommonViewer(getViewSite().getId(), aParent, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
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
						&& event.item.getData() instanceof ScenarioTreeNodeClass) {
					final ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) event.item
							.getData();
					final Button button = (Button) event.item;
					
					// Set selection status
					Activator
							.getDefault()
							.getJobManager()
							.setResourceSelection(node.getResource(),
									button.getSelection());
				}
			}
		});

		IJobManagerListener jobManagerListener = new IJobManagerListener() {
			
			void checkItems(TreeItem item, IResource res, boolean check) {
				
				if (item.getData() == res) {
					item.setChecked(check);
				}
				for (TreeItem i : item.getItems()) {
					checkItems(i, res, check);
				}
			}
			
			@Override
			public void jobSelected(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				
				// TODO: Update Check status of widget
				
				// TODO Auto-generated method stub
				TreeItem[] items = TheNavigator.this.getCommonViewer().getTree().getItems();
				for (TreeItem i : items) {
					checkItems(i, resource, true);
				}
				TheNavigator.this.getCommonViewer().getTree().redraw();
			}
			
			@Override
			public void jobRemoved(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void jobDeselected(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				// TODO Auto-generated method stub
				TreeItem[] items = TheNavigator.this.getCommonViewer().getTree().getItems();
				for (TreeItem i : items) {
					checkItems(i, resource, false);
				}
			}
			
			@Override
			public void jobAdded(IJobManager jobManager, IManagedJob job,
					IResource resource) {
				// TODO Auto-generated method stub
				
			}
		};
		Activator
		.getDefault()
		.getJobManager().addJobManagerListener(jobManagerListener);
		
		return aViewer;
	}
}
