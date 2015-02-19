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

import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class DataModelCellEditingSupport extends ObservableValueEditingSupport {
	private final TextCellEditor cellEditor;

	public DataModelCellEditingSupport(final ColumnViewer viewer, final DataBindingContext dbc) {
		super(viewer, dbc);
		cellEditor = new TextCellEditor((Composite) viewer.getControl());
	}

	@Override
	protected IObservableValue doCreateCellEditorObservable(final CellEditor cellEditor) {
		return SWTObservables.observeText(cellEditor.getControl(), SWT.Modify);
	}

	@Override
	protected boolean canEdit(Object element) {
		if (element instanceof UserGroup) {
			return true;
		}
		return false;
	}

	@Override
	protected IObservableValue doCreateElementObservable(final Object element, final ViewerCell cell) {
		if (element instanceof UserGroup) {
			return EMFProperties.value(ScheduleReportPackage.Literals.USER_GROUP__COMMENT).observe(element);
		}
		return null;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		return cellEditor;
	}
}