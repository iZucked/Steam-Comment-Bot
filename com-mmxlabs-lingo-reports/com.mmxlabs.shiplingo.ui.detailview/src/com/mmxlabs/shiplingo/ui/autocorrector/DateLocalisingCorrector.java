/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.autocorrector;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;

public class DateLocalisingCorrector extends BaseCorrector {

	private static TimeZone getZone(final Port port) {
		if ((port == null) || (port.getTimeZone() == null) || port.getTimeZone().equals("")) {
			return TimeZone.getDefault();
		}
		return TimeZone.getTimeZone(port.getTimeZone());
	}

	@Override
	public Pair<String, Command> correct(final Notification notification, final EditingDomain editingDomain) {
		final Object feature = notification.getFeature();

		if (notification.getEventType() == Notification.SET) {

			if (feature instanceof EReference) {
				final EReference ref = (EReference) feature;
				if (ref.getEType().equals(PortPackage.eINSTANCE.getPort())) {
					// check whether there's a nearby date attribute and
					// localize the time

					final Port oldValue = (Port) notification.getOldValue();

					if (oldValue == null) {
						return null; // break out if port was null, because this
										// is probably an import.
					}

					final Port newValue = (Port) notification.getNewValue();

					final TimeZone oldZone = getZone(oldValue);
					final TimeZone newZone = getZone(newValue);
					if (oldZone.equals(newZone)) {
						return null;
					}

					final EClass parent = ref.getEContainingClass();
					for (final EAttribute attribute : parent.getEAllAttributes()) {
						if (attribute.getEAttributeType().equals(EcorePackage.eINSTANCE.getEDate()) || attribute.getEAttributeType().equals(ScenarioPackage.eINSTANCE.getDateAndOptionalTime())) {
							// localize this date
							final Date date = (Date) ((EObject) notification.getNotifier()).eGet(attribute);

							if (date == null) {
								return null;
							}
							final Calendar c = Calendar.getInstance(oldZone);
							c.setTime(date);
							final Calendar c2 = Calendar.getInstance(newZone);
							c2.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));

							final Date result;
							if (date instanceof DateAndOptionalTime) {
								result = new DateAndOptionalTime(c2.getTime(), ((DateAndOptionalTime) date).isOnlyDate());
							} else {
								result = c2.getTime();
							}

							return new Pair<String, Command>("Adjust date to local time", makeSetter(editingDomain, (EObject) notification.getNotifier(), attribute, result));
						}
					}
				}
			}

		}
		return null;
	}
}
