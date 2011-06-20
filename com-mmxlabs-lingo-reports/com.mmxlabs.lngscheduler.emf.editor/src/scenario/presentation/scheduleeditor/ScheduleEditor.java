/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.scheduleeditor;

import java.util.Calendar;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartContentProvider;

/**
 * A composite for editing a schedule. Contains a simplified gantt chart view,
 * which just displays the cargos, but no journey information.
 * 
 * Should let you assign cargos from one vessel to another, and also display
 * cargos not allocated to a vessel somewhere.
 * 
 * Could also let you change cargo properties?
 * 
 * @author Tom Hinton
 * 
 */
public class ScheduleEditor extends Composite implements
		IGanttChartContentProvider {
	public ScheduleEditor(final Composite parent, final int style) {
		super(parent, style);

		createChildren();
	}

	/**
	 * Create the contained views
	 */
	private void createChildren() {
		final GanttChartViewer ganttViewer = new GanttChartViewer(this);

		ganttViewer.setContentProvider(this);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get children; this will return the vessels in the selected schedule, and
	 * their children will be the <em>cargos</em> their elements correspond to.
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Calendar getElementStartTime(Object element) {
		return null;
	}

	@Override
	public Calendar getElementEndTime(Object element) {
		return null;
	}

	@Override
	public Calendar getElementPlannedStartTime(Object element) {
		return null;
	}

	@Override
	public Calendar getElementPlannedEndTime(Object element) {
		return null;
	}
}
