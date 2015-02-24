/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Widget;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportFactory;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class DiffViewDropTargetAdapter extends DropTargetAdapter {
	@Override
	public void drop(final DropTargetEvent event) {

		// if (event.data instanceof TreeSelection) {
		Object target = extractDropTarget(event.item);
		final Collection<?> extractDragSource = extractDragSource(event.data);

		if (target instanceof Row) {
			target = ((Row) target).getCycleGroup();
		}

		if (target instanceof CycleGroup) {
			CycleGroup cycleGroup = (CycleGroup) target;
			target = cycleGroup.getUserGroup();
			if (target == null) {
				Table table = (Table) cycleGroup.eContainer();
				UserGroup userGroup = ScheduleReportFactory.eINSTANCE.createUserGroup();
				userGroup.setComment("New Group");
				
				cycleGroup.setUserGroup(userGroup);
				table.getUserGroups().add(userGroup);
				target = userGroup;

			}
		}

		if (target instanceof UserGroup) {
			final UserGroup userGroup = (UserGroup) target;
			for (final Object o : extractDragSource) {

				if (o instanceof CycleGroup) {
					final CycleGroup cycleGroup = (CycleGroup) o;

					final UserGroup oldGroup = cycleGroup.getUserGroup();
					((CycleGroup) o).setUserGroup(userGroup);

					if (oldGroup != null && oldGroup.eContainer() != null && oldGroup.getGroups().isEmpty()) {
						// Remove from container
						((Table) oldGroup.eContainer()).getUserGroups().remove(oldGroup);
					}
				}
			}
		}
		super.drop(event);
	}

	@Override
	public void dragEnter(final DropTargetEvent event) {
		super.dragEnter(event);
	}

	@Override
	public void dragLeave(final DropTargetEvent event) {
		super.dragLeave(event);
	}

	@Override
	public void dragOver(final DropTargetEvent event) {
		super.dragOver(event);
	}

	@Override
	public void dragOperationChanged(final DropTargetEvent event) {
		super.dragOperationChanged(event);
	}

	@Override
	public void dropAccept(final DropTargetEvent event) {
		super.dropAccept(event);
	}

	protected Collection<?> extractDragSource(final Object object) {
		// Transfer the data and convert the structured selection to a collection of objects.
		//
		if (object instanceof IStructuredSelection) {
			final List<?> list = ((IStructuredSelection) object).toList();
			return list;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	protected Object extractDropTarget(final Widget item) {
		if (item == null)
			return null;
		return item.getData();
	}
}
