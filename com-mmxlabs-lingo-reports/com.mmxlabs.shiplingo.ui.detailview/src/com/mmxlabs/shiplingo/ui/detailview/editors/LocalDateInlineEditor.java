/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import scenario.ScenarioPackage;
import scenario.port.Port;
import scenario.port.PortPackage;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.rcp.common.controls.DateAndComboTime;

public class LocalDateInlineEditor extends UnsettableInlineEditor {
	private DateAndComboTime dateAndTime;
	private final EReference portReference;

	private final boolean optionalDate;

	public LocalDateInlineEditor(EMFPath path, EStructuralFeature feature,
			EditingDomain editingDomain, final ICommandProcessor processor) {
		super(path, feature, editingDomain, processor);
		optionalDate = feature.getEType().equals(
				ScenarioPackage.eINSTANCE.getDateAndOptionalTime());
		final EClass container = feature.getEContainingClass();
		// check for associated port reference
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

	// private Date convertFromCalendar(final Calendar calendar) {
	// if (optionalDate) {
	// return new DateAndOptionalTime(calendar.getTime(),
	// !calendar.isSet(Calendar.HOUR_OF_DAY));
	// } else {
	// return calendar.getTime();
	// }
	// }

	@Override
	protected Control createValueControl(Composite parent) {
		final DateAndComboTime dateAndTime = new DateAndComboTime(parent,
				SWT.DROP_DOWN, false, 0, optionalDate);
		this.dateAndTime = dateAndTime;

		final Listener listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (optionalDate) {
					doSetValue(new DateAndOptionalTime(dateAndTime.getValue()
							.getTime(), !dateAndTime.isTimeSet()));
				} else {
					doSetValue(dateAndTime.getValue().getTime());
				}
			}
		};

		dateAndTime.addListener(SWT.Selection, listener);
		dateAndTime.addListener(SWT.DefaultSelection, listener);

		dateAndTime.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dateAndTime.removeListener(SWT.Selection, listener);
				dateAndTime.removeListener(SWT.DefaultSelection, listener);
			}
		});

		return dateAndTime;
	}

	private TimeZone getTimeZone() {
		final Port port = (Port) (portReference == null ? null : input
				.eGet(portReference));
		final TimeZone zone = TimeZone.getTimeZone(portReference == null
				|| port == null || port.getTimeZone() == null ? "UTC" : port
				.getTimeZone());
		return zone;
	}

	private Calendar getCalendar(final Date utcDate) {
		final Calendar cal = Calendar.getInstance(getTimeZone());
		if (utcDate == null)
			return null;
		cal.setTime(utcDate);
		if (optionalDate && ((DateAndOptionalTime) utcDate).isOnlyDate()) {
			final Port port = (Port) (portReference == null ? null : input
					.eGet(portReference));
			if (port != null) {
				cal.set(Calendar.HOUR_OF_DAY, port.getDefaultWindowStart());
			}
		}
		return cal;
	}

	@Override
	protected void updateControl() {
		if (dateAndTime.isDisposed())
			return;
		if (input != null && portReference != null
				&& input.eGet(portReference) != null) {
			dateAndTime.setTimeZone(getTimeZone());
		}
	}

	@Override
	protected void updateValueDisplay(final Object value) {
		if (dateAndTime.isDisposed())
			return;
		// Value will be a Date, so we need to find a port and localize the
		// date. Same as in the other date editors.
		if (optionalDate) {
			final DateAndOptionalTime utcDate = (DateAndOptionalTime) value;
			dateAndTime.setValue(getCalendar(utcDate), value == null ? false : (!utcDate.isOnlyDate()));
		} else {
			final Date utcDate = (Date) value;
			dateAndTime.setValue(getCalendar(utcDate));
		}
	}

	@Override
	protected boolean updateOnChangeToFeature(Object changedFeature) {
		return super.updateOnChangeToFeature(changedFeature)
				|| changedFeature.equals(portReference); // update if port
															// changes
	}

	@Override
	protected Object getInitialUnsetValue() {
		return new Date();
	}
}
