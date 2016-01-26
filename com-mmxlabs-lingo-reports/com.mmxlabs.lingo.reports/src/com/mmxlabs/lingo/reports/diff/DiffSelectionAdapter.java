/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class DiffSelectionAdapter {

	private Table table;

	public Table getTable() {
		return table;
	}

	public void setTable(final Table table) {
		this.table = table;
	}

	public static void adaptSelection(Table table, final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Set<EObject> adaptedSelection = new LinkedHashSet<>();
			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				expandDown(itr.next(), adaptedSelection);
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

	public static IStructuredSelection expandAll(final ISelection selection, final Table table) {

		final Set<EObject> newSelectedElements = new LinkedHashSet<>();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				final Object item = itr.next();
				expandAllExternal(item, table, newSelectedElements);
			}
		}
		return new StructuredSelection(new ArrayList<>(newSelectedElements));
	}

	public static IStructuredSelection expandDown(final ISelection selection, final Table table) {

		final Set<EObject> newSelectedElements = new LinkedHashSet<>();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				final Object item = itr.next();
				expandDownExternal(item, table, newSelectedElements);
			}
		}
		return new StructuredSelection(new ArrayList<>(newSelectedElements));
	}
	public static IStructuredSelection expandToRow(final ISelection selection, final Table table) {
		
		final Set<EObject> newSelectedElements = new LinkedHashSet<>();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			final Iterator<?> itr = structuredSelection.iterator();
			while (itr.hasNext()) {
				final Object item = itr.next();
				expandUpToRowExternal(item, table, newSelectedElements);
			}
		}
		return new StructuredSelection(new ArrayList<>(newSelectedElements));
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

	public static void expandDown(final Object object, final Collection<EObject> elements) {
		if (object instanceof UserGroup) {
			expandDown((UserGroup) object, elements);
		} else if (object instanceof CycleGroup) {
			expandDown((CycleGroup) object, elements);
		} else if (object instanceof Row) {
			expandDown((Row) object, elements);
		}
	}

	public static void expandAll(final UserGroup userGroup, final Collection<EObject> elements) {
		elements.add(userGroup);
		for (final CycleGroup cycleGroup : userGroup.getGroups()) {
			elements.add(cycleGroup);
			for (final Row row : cycleGroup.getRows()) {
				elements.add(row);
				elements.addAll(row.getInputEquivalents());
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
				elements.addAll(row.getInputEquivalents());
			}
		}
	}

	public static void expandAll(final Row row, final Collection<EObject> elements) {
		if (row.getCycleGroup() != null) {
			expandAll(row.getCycleGroup(), elements);
		} else {
			elements.add(row);
			elements.addAll(row.getInputEquivalents());
			elements.add(row.getReferenceRow());
			elements.addAll(row.getReferringRows());
		}
	}

	public static void expandDown(final UserGroup userGroup, final Collection<EObject> elements) {
		elements.add(userGroup);
		for (final CycleGroup cycleGroup : userGroup.getGroups()) {
			expandDown(cycleGroup, elements);
		}
	}

	public static void expandDown(final CycleGroup cycleGroup, final Collection<EObject> elements) {

		elements.add(cycleGroup);
		for (final Row row : cycleGroup.getRows()) {
			expandDown(row, elements);
		}
	}

	public static void expandDown(final Row row, final Collection<EObject> elements) {

		elements.add(row);
		elements.addAll(row.getInputEquivalents());
//		elements.add(row.getReferenceRow());
//		elements.addAll(row.getReferringRows());

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

	public static void expandUpExternal(final Object object, final Table table, final Collection<EObject> elements) {
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

	public static void expandAllExternal(final Object object, @Nullable final Table table, @NonNull final Collection<EObject> elements) {
		expandAll(object, elements);
		if (table != null) {
			for (final Row row : table.getRows()) {
				for (final Object o : row.getInputEquivalents()) {
					if (o == object) {
						DiffSelectionAdapter.expandAll(row, elements);
						break;
					}
				}
			}
		}
	}
	
	public static void expandUpToRowExternal(final Object object, @Nullable final Table table, @NonNull final Collection<EObject> elements) {
		expandDown(object, elements);
		if (table != null) {
			for (final Row row : table.getRows()) {
				for (final Object o : row.getInputEquivalents()) {
					if (o == object) {
						DiffSelectionAdapter.expandDown(row, elements);
						break;
					}
				}
			}
		}
	}

	public static void expandDownExternal(final Object object, @Nullable final Table table, @NonNull final Collection<EObject> elements) {
		expandDown(object, elements);
		if (table != null) {
			for (final Row row : table.getRows()) {
				for (final Object o : row.getInputEquivalents()) {
					if (o == object) {
						DiffSelectionAdapter.expandDown(row, elements);
						break;
					}
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

	public static List<Object> convertToTreePaths(@NonNull final Collection<?> selection) {
		List<Object> treePaths = new ArrayList<>(selection.size());
		for (Object element : selection) {
			List<Object> path = new LinkedList<>();
			path.add(element);
			if (element instanceof Row) {
				element = ((Row) element).getCycleGroup();
				if (element != null) {
					path.add(0, element);
				}
			}
			if (element instanceof CycleGroup) {
				element = ((CycleGroup) element).getUserGroup();
				if (element != null) {
					path.add(0, element);
				}
			}
			treePaths.add(new TreePath(path.toArray()));
			
		}
		return treePaths;
	}
}
