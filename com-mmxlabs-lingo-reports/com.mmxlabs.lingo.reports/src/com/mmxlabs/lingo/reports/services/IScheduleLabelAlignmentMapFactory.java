package com.mmxlabs.lingo.reports.services;

import java.util.List;
import java.util.Map;

import org.eclipse.nebula.widgets.ganttchart.label.EEventLabelAlignment;
import org.eclipse.nebula.widgets.ganttchart.label.IEventTextPropertiesGenerator;

public interface IScheduleLabelAlignmentMapFactory {

	List<Map<EEventLabelAlignment, IEventTextPropertiesGenerator>> buildShowDaysLabelAlignmentMaps();

	List<Map<EEventLabelAlignment, IEventTextPropertiesGenerator>> buildDestinationLabelAlignmentMaps();

	List<Map<EEventLabelAlignment, IEventTextPropertiesGenerator>> buildCanalAlignmentMaps();

}
