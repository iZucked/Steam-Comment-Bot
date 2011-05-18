/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import scenario.cargo.Cargo;
import scenario.port.Port;
import scenario.port.PortPackage;

/**
 * Singleton for making any required amendments to EObjects after import.
 * Currently sets load and discharge slot IDs if they aren't set, and fixes
 * date/time values on objects with a port to have been local to that port
 * rather than UTC.
 * 
 * @author Tom Hinton
 * 
 */
class Postprocessor {
	private static final Postprocessor INSTANCE = new Postprocessor();

	protected Postprocessor() {

	}

	public static Postprocessor getInstance() {
		return INSTANCE;
	}

	private void postprocess(final EObject object, boolean recur) {
		// derive cargo IDs
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			if (cargo.getLoadSlot() != null
					&& (cargo.getLoadSlot().getId() == null || cargo
							.getLoadSlot().getId().equals(""))) {
				cargo.getLoadSlot().setId("load-" + cargo.getId());
			}
			if (cargo.getDischargeSlot() != null
					&& (cargo.getDischargeSlot().getId() == null || cargo
							.getDischargeSlot().getId().equals(""))) {
				cargo.getDischargeSlot().setId("discharge-" + cargo.getId());
			}
		}

		// correct local dates
		EReference portReference = null;
		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (reference.isMany() == false
					&& reference.isContainment() == false
					&& reference.getEReferenceType().equals(
							PortPackage.eINSTANCE.getPort())) {
				portReference = reference;
				break;
			}
		}
		if (portReference != null && object.eIsSet(portReference)) {
			final Port port = (Port) object.eGet(portReference);
			final String tz = port == null ? null : port.getTimeZone();
			final TimeZone zone = TimeZone.getTimeZone(tz);

			if (zone != null) {
				for (final EAttribute attribute : object.eClass()
						.getEAllAttributes()) {
					if (EcorePackage.eINSTANCE.getEDate().equals(
							attribute.getEAttributeType())) {
						// localize this date
						final Date date = (Date) object.eGet(attribute);
						if (date == null)
							continue;
						final Calendar in = Calendar.getInstance();
						final Calendar out = Calendar.getInstance(zone);
						in.setTime(date);
						out.set(in.get(Calendar.YEAR), in.get(Calendar.MONTH),
								in.get(Calendar.DATE),
								in.get(Calendar.HOUR_OF_DAY),
								in.get(Calendar.MINUTE),
								in.get(Calendar.SECOND));
						object.eSet(attribute, out.getTime());
					}
				}
			}
		}

		if (!recur)
			return;
		final TreeIterator<EObject> iterator = object.eAllContents();
		while (iterator.hasNext()) {
			postprocess(iterator.next(), false);
		}
	}

	public void postprocess(final EObject object) {
		postprocess(object, true);
	}
}
