/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.autocorrect;

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

import scenario.port.Port;
import scenario.port.PortPackage;

import com.mmxlabs.common.Pair;

public class DateLocalisingCorrector extends BaseCorrector {

	private static TimeZone getZone(final Port port) {
		if (port == null || port.getTimeZone() == null
				|| port.getTimeZone().equals(""))
			return TimeZone.getDefault();
		return TimeZone.getTimeZone(port.getTimeZone());
	}

	@Override
	public Pair<String, Command> correct(Notification notification,
			EditingDomain editingDomain) {
		final Object feature = notification.getFeature();

		if (notification.getEventType() == Notification.SET) {

			if (feature instanceof EReference) {
				final EReference ref = (EReference) feature;
				if (ref.getEType().equals(PortPackage.eINSTANCE.getPort())) {
					// check whether there's a nearby date attribute and
					// localize the time

					final Port oldValue = (Port) notification.getOldValue();

					if (oldValue == null)
						return null; // break out if port was null, because this
										// is probably an import.

					final Port newValue = (Port) notification.getNewValue();

					final TimeZone oldZone = getZone(oldValue);
					final TimeZone newZone = getZone(newValue);
					if (oldZone.equals(newZone))
						return null;

					final EClass parent = ref.getEContainingClass();
					for (final EAttribute attribute : parent
							.getEAllAttributes()) {
						if (attribute.getEAttributeType().equals(
								EcorePackage.eINSTANCE.getEDate())) {
							// localize this date
							final Date date = (Date) ((EObject) notification
									.getNotifier()).eGet(attribute);
							if (date == null)
								return null;
							final Calendar c = Calendar.getInstance(oldZone);
							c.setTime(date);
							final Calendar c2 = Calendar.getInstance(newZone);
							c2.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
									c.get(Calendar.DATE),
									c.get(Calendar.HOUR_OF_DAY),
									c.get(Calendar.MINUTE),
									c.get(Calendar.SECOND));
							return new Pair<String, Command>(
									"Adjust date to local time", makeSetter(
											editingDomain,
											(EObject) notification
													.getNotifier(), attribute,
											c2.getTime()));
						}
					}
				}
			}

		}
		return null;
	}
}
