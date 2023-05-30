package com.mmxlabs.lingo.reports.services;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.label.AsDecimalGenerator;
import org.eclipse.nebula.widgets.ganttchart.label.EEventLabelAlignment;
import org.eclipse.nebula.widgets.ganttchart.label.FromHoursGenerator;
import org.eclipse.nebula.widgets.ganttchart.label.IEventTextPropertiesGenerator;
import org.eclipse.nebula.widgets.ganttchart.label.IFromEventTextGenerator;

import com.mmxlabs.common.time.DMYUtil;
import com.mmxlabs.common.time.DMYUtil.DayMonthOrder;
import com.mmxlabs.lingo.reports.views.formatters.ScheduleChartFormatters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.port.util.PortUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class DefaultScheduleLabelAlignmentMapFactory implements IScheduleLabelAlignmentMapFactory {}
