/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.ganttviewer.internal.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartContentProvider;
import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;

public class GanttViewerTest {

	public static class GanttLabelProvider implements ILabelProvider,
			IGanttChartToolTipProvider, IColorProvider {

		public GanttLabelProvider() {

		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public Color getForeground(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Color getBackground(Object element) {
			Random r = new Random();
			// TODO Auto-generated method stub
			return new Color(null, r.nextInt(255), r.nextInt(255),
					r.nextInt(255));
		}

		@Override
		public String getToolTipText(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getToolTipTitle(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Image getToolTipImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(Object element) {
			// TODO Auto-generated method stub
			return element.toString();
		}
	}

	public static class GanttContentProvider extends TreeNodeContentProvider
			implements IGanttChartContentProvider {

		int counter = 0;

		Map<Object, Calendar> map = new HashMap<Object, Calendar>();

		@Override
		public Calendar getElementStartTime(Object element) {
			return createCalendar(element);
		}

		@Override
		public Calendar getElementEndTime(Object element) {
			return createCalendar(element);
		}

		Calendar createCalendar(Object o) {
			if (map.containsKey(o)) {
				return map.get(o);
			} else {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.HOUR, counter++ * 24);
				map.put(o, c);
				return c;

			}
		}

		@Override
		public Calendar getElementPlannedStartTime(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Calendar getElementPlannedEndTime(Object element) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Gantt Viewer Tester");
		shell.setLayout(new FillLayout());

		GanttChartViewer viewer = new GanttChartViewer(shell);

		viewer.setContentProvider(new GanttContentProvider());
		viewer.setLabelProvider(new GanttLabelProvider());

		TreeNode resources[] = new TreeNode[2];
		resources[0] = new TreeNode("resource1");
		resources[1] = new TreeNode("resource2");

		TreeNode[] children1 = new TreeNode[2];
		children1[0] = new TreeNode("child1");
		children1[1] = new TreeNode("child2");

		TreeNode[] children2 = new TreeNode[2];
		children2[0] = new TreeNode("child3");
		children2[1] = new TreeNode("child4");

		resources[0].setChildren(children1);
		resources[1].setChildren(children2);

		viewer.setInput(resources);

		// shell.setMaximized(true);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
