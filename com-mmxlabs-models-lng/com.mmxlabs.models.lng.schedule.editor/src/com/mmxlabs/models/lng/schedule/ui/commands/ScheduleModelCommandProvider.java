/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.ui.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider;

/**
 * Command provider which detects the deletion of cargoes and kills the
 * corresponding entries in any schedule.
 * 
 * @author hinton
 * 
 */
public class ScheduleModelCommandProvider extends BaseModelCommandProvider {
	@Override
	protected boolean shouldHandleDeletion(Object deletedObject) {
		if (getContext() != null)
			return false;
		if (deletedObject instanceof VesselEvent
				|| deletedObject instanceof Cargo
				|| deletedObject instanceof Vessel
				|| deletedObject instanceof Slot
				|| deletedObject instanceof Port
				|| deletedObject instanceof VesselClass) {
			setContext(Boolean.TRUE);
			return true;
		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider#objectDeleted
	 * (org.eclipse.emf.edit.domain.EditingDomain,
	 * com.mmxlabs.models.mmxcore.MMXRootObject, java.lang.Object)
	 */
	@Override
	protected Command objectDeleted(EditingDomain domain,
			MMXRootObject rootObject, Object deleted) {
		final ScheduleModel scheduleModel = rootObject
				.getSubModel(ScheduleModel.class);
		if (scheduleModel == null)
			return null;
		
		final List<EObject> delete = new ArrayList<EObject>(2);
		
		if (scheduleModel.getInitialSchedule() != null)
			delete.add(scheduleModel.getInitialSchedule());
		
		if (scheduleModel.getOptimisedSchedule() != null)
			delete.add(scheduleModel.getOptimisedSchedule());
		
		if (delete.isEmpty())return null;
		return DeleteCommand.create(domain, delete);
	}
}
