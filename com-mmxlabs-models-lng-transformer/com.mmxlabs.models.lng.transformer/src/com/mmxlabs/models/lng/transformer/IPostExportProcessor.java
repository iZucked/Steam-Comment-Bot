/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @since 2.0
 */
public interface IPostExportProcessor {

	void postProcess(EditingDomain ed, MMXRootObject rootObject, Schedule scheduleModel, InputModel inputModel, CompoundCommand commands);
}
