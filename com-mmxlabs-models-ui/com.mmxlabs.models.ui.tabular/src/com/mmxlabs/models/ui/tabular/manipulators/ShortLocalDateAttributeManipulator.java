/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.time.DMYUtil;
import com.mmxlabs.common.time.DMYUtil.DayMonthOrder;
import com.mmxlabs.common.time.DMYUtil.DayMonthYearOrder;
import com.mmxlabs.models.ui.editors.ICommandHandler;

/**
 * Subclass of LocalDateAttributeManipulator to hide the current year or show a 2 digit year for display only. Editing still requires 4 digit year.
 * 
 * @author Simon Goodall
 * 
 */
public class ShortLocalDateAttributeManipulator extends LocalDateAttributeManipulator {

	private final DayMonthOrder dmMode = DMYUtil.getDayMonthOrder();
	private final DayMonthYearOrder dmyMode = DMYUtil.getDayMonthYearOrder();

	public ShortLocalDateAttributeManipulator(final EStructuralFeature field, final ICommandHandler commandHandler) {
		super(field, commandHandler);
	}

	@Override
	public String renderSetValue(final Object owner, final Object object) {
		if (object instanceof LocalDate d) {
			java.time.format.DateTimeFormatter formatter;
			if (LocalDate.now().getYear() == d.getYear()) {
				formatter = switch (dmMode) {
				case DAY_MONTH -> java.time.format.DateTimeFormatter.ofPattern("dd/MM");
				case MONTH_DAY -> java.time.format.DateTimeFormatter.ofPattern("MM/dd");
				};
			} else {
				formatter = switch (dmyMode) {
				case DAY_MONTH_YEAR -> java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy");
				case MONTH_DAY_YEAR -> java.time.format.DateTimeFormatter.ofPattern("MM/dd/yy");
				case YEAR_MONTH_DAY -> java.time.format.DateTimeFormatter.ofPattern("yy/MM/dd");
				};
			}
			return d.format(formatter);
		} else {
			return super.renderSetValue(owner, object);
		}
	}
}