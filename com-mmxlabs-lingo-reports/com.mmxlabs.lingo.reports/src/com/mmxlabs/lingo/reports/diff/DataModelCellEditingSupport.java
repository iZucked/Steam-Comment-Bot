package com.mmxlabs.lingo.reports.diff;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class DataModelCellEditingSupport extends ObservableValueEditingSupport {
	private final ColumnViewer columnViewer;
	private final TextCellEditor cellEditor;

	// private IObservableMap[] attributeMaps;

	public DataModelCellEditingSupport(final ColumnViewer viewer, final DataBindingContext dbc) {
		super(viewer, dbc);
		this.columnViewer = viewer;

		cellEditor = new TextCellEditor((Composite) viewer.getControl());
		//
		// attributeMaps = new IObservableMap[3];
		// attributeMaps[0] = EMFProperties.value(ScheduleReportPackage.Literals.USER_GROUP__COMMENT).observeDetail(cp.getKnownElements());
		// attributeMaps[1] = EMFProperties.value(ScheduleReportPackage.Literals.CYCLE_GROUP__INDEX).observeDetail(cp.getKnownElements());
		// attributeMaps[2] = EMFProperties.value(ScheduleReportPackage.Literals.ROW__NAME).observeDetail(cp.getKnownElements());
	}

	@Override
	protected IObservableValue doCreateCellEditorObservable(final CellEditor cellEditor) {
		return SWTObservables.observeText(cellEditor.getControl(), SWT.Modify);
		// return Observables.observeSingleSelection(columnViewer);
	}

	@Override
	protected IObservableValue doCreateElementObservable(final Object element, final ViewerCell cell) {
		if (element instanceof UserGroup) {
			return EMFProperties.value(ScheduleReportPackage.Literals.USER_GROUP__COMMENT).observe(element);
		} else if (element instanceof CycleGroup) {
			return EMFProperties.value(ScheduleReportPackage.Literals.CYCLE_GROUP__INDEX).observe(element);
		} else if (element instanceof Row) {
			return EMFProperties.value(ScheduleReportPackage.Literals.ROW__NAME).observe(element);
		}
		return null;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		return cellEditor;
	}
}