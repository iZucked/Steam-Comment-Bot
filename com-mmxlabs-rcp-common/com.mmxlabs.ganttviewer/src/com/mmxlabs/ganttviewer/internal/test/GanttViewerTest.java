/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.ganttviewer.internal.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.jface.viewers.IColorProvider;
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

	public static class GanttLabelProvider implements IGanttChartToolTipProvider, IColorProvider {

		public GanttLabelProvider() {

		}

		@Override
		public void addListener(final ILabelProviderListener listener) {

		}

		@Override
		public void dispose() {

		}

		@Override
		public boolean isLabelProperty(final Object element, final String property) {
			return false;
		}

		@Override
		public void removeListener(final ILabelProviderListener listener) {

		}

		@Override
		public Color getForeground(final Object element) {
			return null;
		}

		@Override
		public Color getBackground(final Object element) {
			final Random r = new Random();
			return new Color(null, r.nextInt(255), r.nextInt(255), r.nextInt(255));
		}

		@Override
		public String getToolTipText(final Object element) {
			return null;
		}

		@Override
		public String getToolTipTitle(final Object element) {
			return null;
		}

		@Override
		public Image getToolTipImage(final Object element) {
			return null;
		}

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			return element.toString();
		}
	}

	public static class GanttContentProvider extends TreeNodeContentProvider implements IGanttChartContentProvider {

		int counter = 0;

		Map<Object, Calendar> map = new HashMap<Object, Calendar>();

		@Override
		public Calendar getElementStartTime(final Object element) {
			return createCalendar(element);
		}

		@Override
		public Calendar getElementEndTime(final Object element) {
			return createCalendar(element);
		}

		Calendar createCalendar(final Object o) {
			if (map.containsKey(o)) {
				return map.get(o);
			} else {
				final Calendar c = Calendar.getInstance();
				c.add(Calendar.HOUR, counter++ * 24);
				map.put(o, c);
				return c;

			}
		}

		@Override
		public Calendar getElementPlannedStartTime(final Object element) {
			return null;
		}

		@Override
		public Calendar getElementPlannedEndTime(final Object element) {
			return null;
		}

		@Override
		public String getGroupIdentifier(final Object element) {
			return null;
		}

		@Override
		public Object getElementDependency(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isVisibleByDefault(Object resource) {
			return true;
		}
	}

	public static void main(final String[] args) {

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Gantt Viewer Tester");
		shell.setLayout(new FillLayout());

		final GanttChartViewer viewer = new GanttChartViewer(shell);

		viewer.setContentProvider(new GanttContentProvider());
		viewer.setLabelProvider(new GanttLabelProvider());

		final TreeNode resources[] = new TreeNode[2];
		resources[0] = new TreeNode("resource1");
		resources[1] = new TreeNode("resource2");

		final TreeNode[] children1 = new TreeNode[2];
		children1[0] = new TreeNode("child1");
		children1[1] = new TreeNode("child2");

		final TreeNode[] children2 = new TreeNode[2];
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
