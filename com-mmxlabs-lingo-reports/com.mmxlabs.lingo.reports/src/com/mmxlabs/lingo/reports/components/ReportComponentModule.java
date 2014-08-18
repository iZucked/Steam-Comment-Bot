package com.mmxlabs.lingo.reports.components;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.views.schedule.extpoint.ScheduleBasedReportModule;

public class ReportComponentModule extends AbstractModule {

	@Override
	protected void configure() {
		
		install(new ScheduleBasedReportModule());
		
//		
//		bind(ScheduleBasedReportBuilder.class);
//		
//		bind(iterable(ICustomRelatedSlotHandlerExtension.class)).toProvider(service(ICustomRelatedSlotHandlerExtension.class).multiple());
	}
}
