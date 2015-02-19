package com.mmxlabs.lingo.reports.diff;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class DiffSelectionAdapter implements ISelectionListener, ISelectionChangedListener {

	private Table table;

	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		if (table != null) {
			adaptSelection(event.getSelection());
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (table != null && part instanceof DiffGroupView) {
			adaptSelection(selection);
		}
	}

	private void adaptSelection(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Set<EObject> adaptedSelection = new LinkedHashSet<>();
			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				expandUp(itr.next(), adaptedSelection);
			}
			// Remove nulls
			while (adaptedSelection.remove(null))
				;
			// Replace table selected objects.
			table.getSelectedElements().clear();
			table.getSelectedElements().addAll(adaptedSelection);
		}
	}

	public static void expandAll(final Object object, final Collection<EObject> elements) {
		if (object instanceof UserGroup) {
			expandAll((UserGroup) object, elements);
		} else if (object instanceof CycleGroup) {
			expandAll((CycleGroup) object, elements);
		} else if (object instanceof Row) {
			expandAll((Row) object, elements);
		}
	}

	public static void expandUp(final Object object, final Collection<EObject> elements) {
		if (object instanceof UserGroup) {
			expandUp((UserGroup) object, elements);
		} else if (object instanceof CycleGroup) {
			expandUp((CycleGroup) object, elements);
		} else if (object instanceof Row) {
			expandUp((Row) object, elements);
		}
	}

	public static void expandAll(final UserGroup userGroup, final Collection<EObject> elements) {
		elements.add(userGroup);
		for (final CycleGroup cycleGroup : userGroup.getGroups()) {
			elements.add(cycleGroup);
			for (final Row row : cycleGroup.getRows()) {
				elements.add(row);
			}
		}
	}

	public static void expandAll(final CycleGroup cycleGroup, final Collection<EObject> elements) {
		if (cycleGroup.getUserGroup() != null) {
			expandAll(cycleGroup.getUserGroup(), elements);
		} else {
			elements.add(cycleGroup);
			for (final Row row : cycleGroup.getRows()) {
				elements.add(row);
			}
		}
	}

	public static void expandAll(final Row row, final Collection<EObject> elements) {
		if (row.getCycleGroup() != null) {
			expandAll(row.getCycleGroup(), elements);
		} else {
			elements.add(row);
			elements.add(row.getReferenceRow());
			elements.addAll(row.getReferringRows());
		}
	}

	public static void expandUp(final UserGroup userGroup, final Collection<EObject> elements) {
		elements.add(userGroup);
	}

	public static void expandUp(final CycleGroup cycleGroup, final Collection<EObject> elements) {
		if (cycleGroup.getUserGroup() != null) {
			expandUp(cycleGroup.getUserGroup(), elements);
		} else {
			elements.add(cycleGroup);
		}
	}

	public static void expandUp(final Row row, final Collection<EObject> elements) {
		if (row.getCycleGroup() != null) {
			expandUp(row.getCycleGroup(), elements);
		} else {
			elements.add(row);
			elements.add(row.getReferenceRow());
			elements.addAll(row.getReferringRows());
		}
	}

	public static void expandExternal(final Object object, Table table, final Collection<EObject> elements) {
		expandUp(object, elements);
		for (final Row row : table.getRows()) {
			for (final Object o : row.getInputEquivalents()) {
				if (o == object) {
					DiffSelectionAdapter.expandUp(row, elements);
					break;
				}
			}
		}
	}

	public static Set<Object> expandEquivalents(final Object o) {
		final Set<Object> s = new LinkedHashSet<>();
		s.add(o);
		if (o instanceof Row) {
			final Row row = (Row) o;
			s.addAll(row.getInputEquivalents());
		}
		return s;
	}

}
