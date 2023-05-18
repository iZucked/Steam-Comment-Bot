package com.mmxlabs.lingo.reports.scheduleview.views.positionssequences;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.scheduleview.scheduleViewPositionsSequenceProvider")
public interface ISchedulePositionsSequenceProviderExtension {
	@MapName("id")
	String getId();

	@MapName("name")
	String getName();

	/**
	 * Create a new instance. Note method needs to start with "create" to avoid Peaberry caching the result
	 * 
	 * @return
	 */
	@MapName("class")
	ISchedulePositionsSequenceProvider createInstance();
	
	@MapName("showMenuItem")
	String showMenuItem();
}
