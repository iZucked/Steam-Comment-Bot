package com.mmxlabs.lingo.reports.components;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import com.google.inject.AbstractModule;
import com.mmxlabs.lingo.reports.utils.ICustomRelatedSlotHandlerExtension;

public class ReportComponentModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(ScheduleBasedReportBuilder.class);
		
		bind(iterable(ICustomRelatedSlotHandlerExtension.class)).toProvider(service(ICustomRelatedSlotHandlerExtension.class).multiple());
	}
}
