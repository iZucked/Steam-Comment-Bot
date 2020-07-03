/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Interface to manipulate the data model once the given Schedule has been applied to it.
 */
public interface IPostExportProcessor {

	/**
	 */
	void postProcess(EditingDomain ed, MMXRootObject rootObject, Schedule scheduleModel, CompoundCommand commands);
}
