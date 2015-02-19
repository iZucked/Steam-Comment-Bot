/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.emf.databinding.EMFProperties;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class ScheduleReportTreeFactoryImpl implements IObservableFactory {

	@Override
	public IObservable createObservable(final Object target) {
		if (target instanceof IObservableList) {
			return (IObservable) target;
		} else if (target instanceof Table) {
			return EMFProperties.multiList(ScheduleReportPackage.Literals.TABLE__USER_GROUPS, ScheduleReportPackage.Literals.TABLE__CYCLE_GROUPS).observe(target);
		} else if (target instanceof CycleGroup) {
			return EMFProperties.list(ScheduleReportPackage.Literals.CYCLE_GROUP__ROWS).observe(target);
		} else if (target instanceof UserGroup) {
			return EMFProperties.list(ScheduleReportPackage.Literals.USER_GROUP__GROUPS).observe(target);
		}

		return null;
	}
}
