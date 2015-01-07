package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * A {@link IDiffProcessor} instance calls {@link Row#setVisible(boolean)} to unhide rows which present some kind of difference. By default in diff mode all rows are hidden.
 * 
 * @author Simon Goodall
 * 
 */
public interface IDiffProcessor {

	/**
	 * This method is called for each scenario/schedule in the current view allowing the {@link IDiffProcessor} to pre-process data.
	 * 
	 * @param schedule
	 */
	void processSchedule(Schedule schedule, boolean isPinned);

	/**
	 * Invoked once all the {@link Schedule}s have been processed and the basic table data model has been created.
	 * 
	 * @param table
	 * @param referenceElements
	 * @param uniqueElements
	 * @param equivalancesMap
	 * @param elementToRowMap
	 */
	void runDiffProcess(Table table, List<EObject> referenceElements, List<EObject> uniqueElements, final Map<EObject, Set<EObject>> equivalancesMap, Map<EObject, Row> elementToRowMap);

}
