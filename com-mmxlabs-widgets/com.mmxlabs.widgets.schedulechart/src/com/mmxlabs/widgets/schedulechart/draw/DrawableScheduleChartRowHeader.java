/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart.draw;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.cii.CIIGradeFinder;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartMode;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowPriorityType;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleFilterSupport;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;

public class DrawableScheduleChartRowHeader extends DrawableElement {
	
	private final Control parent;
	private final IScheduleChartSettings settings;
	private final IScheduleChartColourScheme colourScheme;
	private final ScheduleChartMode scm;
	private final ScheduleFilterSupport filterSupport;
	private final DrawableScheduleChartRow dscr;
	private DrawableCheckboxButton checkButton;

	public DrawableScheduleChartRowHeader(ScheduleCanvas parent, DrawableScheduleChartRow dscr, ScheduleFilterSupport filterSupport, ScheduleChartMode scm, IScheduleChartSettings settings) {
		this.parent = parent;
		this.dscr = dscr;
		this.filterSupport = filterSupport;
		this.scm = scm;
		this.colourScheme = settings.getColourScheme();
		this.settings = settings;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		final int checkBoxWidth = settings.filterModeCheckboxColumnWidth();
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, bounds.height).bgColour(colourScheme.getRowHeaderBgColour(dscr.getRowNum()))
					.borderColour(colourScheme.getRowOutlineColour(dscr.getRowNum())).create());
		
		boolean isFilterMode = scm == ScheduleChartMode.FILTER;
		if (isFilterMode) {
			final ScheduleChartRowKey rowKey = dscr.getScheduleChartRow().getKey();
			checkButton = new DrawableCheckboxButton(parent, !filterSupport.isRowHidden(rowKey)) {
				
				@Override
				public void checkAction() {
					filterSupport.toggleShowHide(rowKey);
					parent.redraw();
				}

			};
			checkButton.setBounds(new Rectangle(bounds.x, bounds.y, checkBoxWidth, bounds.height));
			res.addAll(checkButton.getBasicDrawableElements());
		}
		
		int textBoxStart = bounds.x + (isFilterMode ? checkBoxWidth : 0);
		String name = dscr.getScheduleChartRow().getName();
		
		// Add CII grade to name
		if(dscr.getScheduleChartRow().getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS)) {
			name += " " + getCIIGrade(dscr.getScheduleChartRow());
		}
		
		String scenario = dscr.getScheduleChartRow().getScenarioName();
		int heightOfText = queryResolver.findSizeOfText(name, Display.getDefault().getSystemFont(), Display.getDefault().getSystemFont().getFontData()[0].getHeight()).y;
		final int scenarioPadding  = settings.hasMultipleScenarios() && dscr.getScheduleChartRow().getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS) ? 5 : 0;
		
		// Draw pinned icon
		if(dscr.getScheduleChartRow().isPinned()) {
			Image pinImage = CommonImages.getImage(IconPaths.PinnedRow, IconMode.Enabled);
			res.add(BasicDrawableElements.Image.from(pinImage, textBoxStart + pinImage.getBounds().width, bounds.y + ((bounds.height + 1) / 2 - pinImage.getBounds().height / 2) - scenarioPadding)
					.create());
			textBoxStart += pinImage.getBounds().width + 5;
		}
		
		res.add(BasicDrawableElements.Text //
				.from(textBoxStart, bounds.y + ((bounds.height + 1) / 2 - heightOfText / 2) - scenarioPadding, name) //
				.padding(new Padding(settings.getRowHeaderLeftPadding(), settings.getRowHeaderRightPadding(), 0, 0)) //
				.textColour(colourScheme.getRowHeaderTextColour(dscr.getRowNum())) //
				.create()); //
		
		if(settings.hasMultipleScenarios() && dscr.getScheduleChartRow().getRowType().equals(ScheduleChartRowPriorityType.REGULAR_ROWS)) {
			res.add(BasicDrawableElements.Text //
					.from(textBoxStart, bounds.y + ((bounds.height + 1) / 2 - heightOfText / 2) + scenarioPadding * 2, scenario) //
					.padding(new Padding(settings.getRowHeaderLeftPadding(), settings.getRowHeaderRightPadding(), 0, 0)) //
					.textColour(colourScheme.getRowHeaderTextColour(dscr.getRowNum())) //
					.create()); //		
		}
		
		return res;
	}
	
	private String getCIIGrade(ScheduleChartRow row) {
		//
		// CII Grade appendix
		//
		String grade = "-";
		
		
		if(row.getEvents().isEmpty())
			return grade;
		
		Optional<ScheduleEvent> event = row.getEvents().stream().filter(e -> e.getData() instanceof Event).findFirst();
		
		if(event.isEmpty())
			return grade;
		
		ScheduleModel scheduleModel = row.getEvents().get(0).getScheduleModel();
		ScenarioResult scenarioResult = row.getEvents().get(0).getScenarioResult();
		Sequence sequence = ((Event) event.get().getData()).getSequence();

		if(sequence == null || scheduleModel == null || scenarioResult == null)
			return grade;
		
		final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioResult.getScenarioDataProvider());
		final CIIReferenceData ciiReferenceData = fleetModel.getCiiReferences();
		
		if(ciiReferenceData == null)
			return grade;
		
		final Year year = Year.now();
		Vessel vesselForGrade = null;
		final VesselCharter vesselCharter = sequence.getVesselCharter();
		if (vesselCharter != null) {
			vesselForGrade = vesselCharter.getVessel();
			if (vesselForGrade == null) {
				final CharterInMarket charterInMarket = sequence.getCharterInMarket();
				if (charterInMarket != null) {
					vesselForGrade = charterInMarket.getVessel();
				}
			}
		}
		if (vesselForGrade != null) {
			grade = CIIGradeFinder.findCIIGradeForScheduleVesselYear(scheduleModel, ciiReferenceData, vesselForGrade, year);
		}
		
		return grade;
	}
	
	public ScheduleChartRow getScheduleChartRow() {
		return dscr.getScheduleChartRow();
	}

	public DrawableCheckboxButton getCheckbox() {
		return checkButton;
	}

}
