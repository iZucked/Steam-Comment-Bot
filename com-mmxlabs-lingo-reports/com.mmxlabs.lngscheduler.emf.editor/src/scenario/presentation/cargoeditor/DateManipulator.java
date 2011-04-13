/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

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

import com.mmxlabs.rcp.common.celleditors.DateTimeCellEditor;

import scenario.port.Port;
import scenario.port.PortPackage;

/**
 * @author hinton
 * 
 */
public class DateManipulator extends BasicAttributeManipulator {
	final EReference portReference;

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
		if (object == null) return "";
		final Calendar calendar = (Calendar) getValue(object);
		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		df.setCalendar(calendar);
		if (calendar == null) return "";
		return df.format(calendar.getTime()) + " ("
				+ calendar.getTimeZone().getDisplayName(false, TimeZone.SHORT)
				+ ")";

	}

	@Override
	public void setValue(Object object, Object value) {
		if (value == null)
			super.setValue(object, null);
		else
			super.setValue(object, ((Calendar) value).getTime());
	}

	@Override
	public CellEditor getCellEditor(final Composite c, Object object) {
		return new DateTimeCellEditor(c);
	}

	@Override
	public Object getValue(Object object) {
		if (object == null) return null;
		final Date date = (Date) super.getValue(object);
	
		if (date == null) return null;
		
		final EObject obj = (EObject) object;
		final Port port = (Port) obj.eGet(portReference);
		final TimeZone zone = TimeZone.getTimeZone(portReference == null
				|| port == null || port.getTimeZone() == null ? "UTC" : port
				.getTimeZone());
		
		final Calendar cal = Calendar.getInstance(zone);
		cal.setTime(date);
		return cal;
	}

	@Override
	public Comparable getComparable(Object object) {
		return (Date) super.getValue(object);
	}
}
