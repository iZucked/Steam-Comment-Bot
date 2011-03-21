package com.mmxlabs.rcp.navigator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import com.mmxlabs.jobcontoller.Activator;
import com.mmxlabs.jobcontroller.core.JobManager;
import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

public class TheNavigator extends CommonNavigator {
	
	
	protected CommonViewer createCommonViewerObject(Composite aParent) {
		return new CommonViewer(getViewSite().getId(), aParent, SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
	}

	protected CommonViewer createCommonViewer(Composite aParent) {

		CommonViewer aViewer = createCommonViewerObject(aParent);

		Tree treeTable = aViewer.getTree();

		treeTable.setHeaderVisible(true);
		treeTable.setLinesVisible(true);
		// treeTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		// treeTable.setFont(aParent.getFont());
		// PlatformUI.getWorkbench().getHelpSystem()
		// .setHelp(treeTable, ContextIds.VIEW_SERVERS);

		// add columns
		TreeColumn column = new TreeColumn(treeTable, SWT.SINGLE);
		column.setText("Resource");
		column.setWidth(150);
		// column.setWidth(cols[0]);
		// column.addSelectionListener(getHeaderListener(0));
		treeTable.setSortColumn(column);
		treeTable.setSortDirection(SWT.UP);

		TreeColumn column2 = new TreeColumn(treeTable, SWT.SINGLE);

		column2.setText("Status");
		column2.setWidth(150);
		// column2.setWidth(cols[1]);
		// column2.addSelectionListener(getHeaderListener(1));

		initListeners(aViewer);
		aViewer.getNavigatorContentService().restoreState(memento);

		treeTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String string = event.detail == SWT.CHECK ? "Checked"
						: "Selected";
				System.out.println(event.item + " " + string);
				System.out.println(((TreeItem)event.item).getChecked());
				if (event.item.getData() instanceof ScenarioTreeNodeClass) {
					ScenarioTreeNodeClass node = (ScenarioTreeNodeClass)event.item.getData();
//					Activator.getDefault().getJobManager().setResourceSelection(node.getResource(), event.s)
				}
				// HOok into job manager
				
			}
		});

		return aViewer;
	}
}
