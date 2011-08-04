/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import scenario.ScenarioPackage;
import scenario.port.Port;
import scenario.port.PortPackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.rcp.common.celleditors.DateAndOptionalTimeCellEditor;
import com.mmxlabs.rcp.common.celleditors.DateTimeCellEditor;

/**
 * @author hinton
 * 
 */
public class DateManipulator extends BasicAttributeManipulator {
	final EReference portReference;

	private final boolean optionalTime;

	/**
	 * @param field
	 * @param editingDomain
	 */
	public DateManipulator(final EAttribute field,
			final EditingDomain editingDomain) {
		super(field, editingDomain);
		// check for port
		final EClass container = field.getEContainingClass();
		// check for associated port reference

		optionalTime = field.getEType() == ScenarioPackage.eINSTANCE
				.getDateAndOptionalTime();

		EReference portReference = null;
		for (final EReference ref : container.getEAllReferences()) {
			if (ref.isMany())
				continue;
			if (ref.getEReferenceType().equals(PortPackage.eINSTANCE.getPort())) {
				portReference = ref;
				break;
			}
		}
		this.portReference = portReference;
	}

	@Override
	public String render(final Object object) {
		if (object == null)
			return "";
		final Calendar calendar;
		if (optionalTime) {
			calendar = ((Pair<Calendar, Boolean>) getValue(object)).getFirst();
		} else {
			calendar = (Calendar) getValue(object);
		}
		
		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		df.setCalendar(calendar);
		if (calendar == null)
			return "";
		return df.format(calendar.getTime()) + " ("
				+ calendar.getTimeZone().getDisplayName(false, TimeZone.SHORT)
				+ ")";

	}

	@Override
	public void setValue(Object object, Object value) {
		if (value == null) {
			super.setValue(object, null);
		} else {
			if (optionalTime) {
				final Pair<Calendar, Boolean> p = (Pair<Calendar, Boolean>) value;
				super.setValue(object, new DateAndOptionalTime(p.getFirst()
						.getTime(), !(p.getSecond())));
			} else {
				super.setValue(object, ((Calendar) value).getTime());
			}
		}
	}

	@Override
	public CellEditor getCellEditor(final Composite c, Object object) {
		if (optionalTime) {
			return new DateAndOptionalTimeCellEditor(c);
		} else {
			return new DateTimeCellEditor(c);
		}
	}

	@Override
	public Object getValue(Object object) {
		if (object == null)
			return null;

		final Date date = (Date) super.getValue(object);

		if (date == null)
			return null;

		final EObject obj = (EObject) object;
		final Port port = portReference == null ? null : ((Port) obj
				.eGet(portReference));
		final TimeZone zone = TimeZone.getTimeZone(port == null
				|| port.getTimeZone() == null ? "UTC" : port.getTimeZone());

		final Calendar cal = Calendar.getInstance(zone);
		cal.setTime(date);

		if (optionalTime) {
			if (((DateAndOptionalTime) date).isOnlyDate()) {
				if (port != null) {
					cal.set(Calendar.HOUR_OF_DAY, port.getDefaultWindowStart());
				}
			}
			return new Pair<Calendar, Boolean>(cal,
					!((DateAndOptionalTime) date).isOnlyDate());
		}

		return cal;
	}

	@Override
	public Comparable getComparable(Object object) {
		return (Date) super.getValue(object);
	}
}
