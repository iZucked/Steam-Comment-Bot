package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.List;

import com.mmxlabs.scenario.service.ScenarioResult;

public record NinetyDayScheduleInput(ScenarioResult pinned, List<ScenarioResult> other) {

}
