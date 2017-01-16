/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Utility class for doing things to scenarios.
 * 
 * @author hinton
 * 
 */
public class TransformerUtils {
	/**
	 * Serialize an EObject into a byte array; currently EMF generated models do not serialize properly, particularly models with complicated containment going on.
	 * 
	 * @see #deserialiseEObject(byte[], EClass) - the inverse of this method
	 * @param object
	 *            an object to serialize
	 * @return a byte array containing a serialized form of the object
	 */
	public static byte[] serializeEObject(final EObject object) {
		final ResourceSet set = new ResourceSetImpl();
		final XMIResourceFactoryImpl rf = new XMIResourceFactoryImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", rf);

		set.getPackageRegistry().put(object.eClass().getEPackage().getNsURI(), object.eClass().getEPackage());

		final Resource resource = set.createResource(URI.createGenericURI("invalid", "invalid", "invalid"));

		resource.getContents().add(object);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			resource.save(baos, Collections.emptyMap());
		} catch (IOException e) {
		} // byte array cannot produce IO exception

		return baos.toByteArray();
	}

	/**
	 * Deserialize a byte array produced by {@link #serializeEObject(EObject)}. See the doc for that on why this is necessary.
	 * 
	 * @param <T>
	 * @param byteArray
	 *            the serialized form of the object
	 * @param clazz
	 *            the EClass of the object
	 * @return the deserialized object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialiseEObject(final byte[] byteArray, final EClass clazz) {
		final ResourceSet set = new ResourceSetImpl();
		final XMIResourceFactoryImpl rf = new XMIResourceFactoryImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", rf);

		set.getPackageRegistry().put(clazz.getEPackage().getNsURI(), clazz.getEPackage());

		final Resource resource = set.createResource(URI.createGenericURI("invalid", "invalid", "invalid"));

		final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		try {
			resource.load(bais, Collections.emptyMap());
		} catch (IOException e) {
		}
		return (T) clazz.getInstanceClass().cast(resource.getContents().get(0));
	}

	//
	// public static <T> T readObjectFromFile(final String filepath, final Class<? extends T> clazz) {
	// ResourceSet resourceSet = new ResourceSetImpl();
	// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
	// resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI, ScenarioPackage.eINSTANCE);
	//
	// Resource resource = resourceSet.getResource(URI.createFileURI(filepath), true);
	// for (EObject e : resource.getContents()) {
	// if (clazz.isInstance(e)) {
	// return clazz.cast(e);
	// }
	// }
	// return null;
	// }

	/**
	 * Iterate through all the attributes of the given EObject and find the earliest and latest dates
	 * 
	 * @param object
	 * @return a pair, first element containing the earliest date and second the latest
	 */
	public static Pair<Date, Date> findMinMaxDateAttributes(final EObject object) {
		final Pair<Date, Date> result = new Pair<Date, Date>();

		findMinMaxDateAttributes(object, result);

		return result;

	}

	private static void updateMinMax(Pair<ZonedDateTime, ZonedDateTime> pair, ZonedDateTime date) {
		if (date == null)
			return;
		if (pair.getFirst() == null || date.isBefore(pair.getFirst()))
			pair.setFirst(date);
		if (pair.getSecond() == null || date.isAfter(pair.getSecond()))
			pair.setSecond(date);

	}

	public static Pair<ZonedDateTime, ZonedDateTime> findEarliestAndLatestEvents(final MMXRootObject rootObject) {
		final Pair<ZonedDateTime, ZonedDateTime> result = new Pair<>(null, null);

		final TreeIterator<EObject> iterator = rootObject.eAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof VesselAvailability) {
				final VesselAvailability pat = (VesselAvailability) o;
				updateMinMax(result, pat.getStartAfterAsDateTime());
				updateMinMax(result, pat.getStartByAsDateTime());
				updateMinMax(result, pat.getEndAfterAsDateTime());
				updateMinMax(result, pat.getEndByAsDateTime());
			} else if (o instanceof Slot) {
				final Slot slot = (Slot) o;
				updateMinMax(result, slot.getWindowStartWithSlotOrPortTimeWithFlex());
				updateMinMax(result, slot.getWindowEndWithSlotOrPortTimeWithFlex());
			} else if (o instanceof VesselEvent) {
				updateMinMax(result, ((VesselEvent) o).getStartAfterAsDateTime());
				updateMinMax(result, ((VesselEvent) o).getStartByAsDateTime());
			}
		}
		return result;
	}

	/**
	 * Find min/max dates and place them in the given pair, by reference.
	 * 
	 * @param object
	 * @param result
	 */
	private static void findMinMaxDateAttributes(final EObject object, final Pair<Date, Date> mm) {

		final TreeIterator<EObject> iter = object.eAllContents();
		while (iter.hasNext()) {
			final EObject sub = iter.next();
			final EClass eClass = sub.eClass();

			for (final EAttribute attribute : eClass.getEAllAttributes()) {
				if (sub.eIsSet(attribute)) {
					if (Date.class.isAssignableFrom(attribute.getEAttributeType().getInstanceClass())) {
						final Date dt = (Date) sub.eGet(attribute);

						if (mm.getFirst() == null || dt.before(mm.getFirst()))
							mm.setFirst(dt);

						if (mm.getSecond() == null || dt.after(mm.getSecond()))
							mm.setSecond(dt);
					}
				}
			}
		}
	}

	public static EClass findConcreteSubclass(final EClass eClass) {
		if (eClass.isAbstract()) {
			for (final EClassifier otherClass : eClass.getEPackage().getEClassifiers()) {
				if (otherClass instanceof EClass) {
					if (!(((EClass) otherClass).isAbstract()) && eClass.isSuperTypeOf((EClass) otherClass)) {
						return (EClass) otherClass;
					}
				}
			}
			return null;
		} else {
			return eClass;
		}
	}

	/**
	 * Create an eClass and all its contained objects
	 * 
	 * @param eClass
	 * @return
	 */
	public static EObject createEObject(final EClass eClass) {
		final EClass concrete = findConcreteSubclass(eClass);
		if (concrete == null)
			return null;
		final EObject result = concrete.getEPackage().getEFactoryInstance().create(concrete);

		for (final EReference ref : eClass.getEAllContainments()) {
			if (ref.isMany())
				continue;
			final EObject contained = createEObject(ref.getEReferenceType());
			if (contained == null)
				return null;
			result.eSet(ref, contained);
		}

		return result;
	}

	/**
	 * For this object and all its contained objects, find all singular attributes with the given data type, and if they are null at the moment either (a) unset them if they are unsettable attributes,
	 * or (b) set them to the given value if they are not unsettable.
	 * 
	 * 
	 * @param object
	 * @param dataType
	 * @param valueIfSet
	 * @return the input value.
	 */
	public static EObject unsetOrSetNullValues(final EObject input, final EDataType dataType, final Object valueIfSet) {
		EObject object = input;
		final TreeIterator<EObject> iterator = object.eAllContents();
		while (object != null) {
			for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
				if (attribute.isMany())
					continue;
				if (attribute.getEAttributeType().equals(dataType)) {
					final Object value = object.eGet(attribute);
					if (value == null) {
						if (attribute.isUnsettable()) {
							object.eUnset(attribute);
						} else {
							object.eSet(attribute, valueIfSet);
						}
					}
				}
			}

			if (iterator.hasNext()) {
				object = iterator.next();
			} else {
				object = null;
			}
		}
		return input;
	}

	/**
	 * Fix any dates which are null; dates shouldn't be null
	 * 
	 * @param input
	 * @return the input
	 */
	public static EObject fixNullDates(final EObject input) {
		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.setTime(new Date());
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		final Date date = calendar.getTime();
		// final DateAndOptionalTime daot = new DateAndOptionalTime(date, true);
		return unsetOrSetNullValues(input, EcorePackage.eINSTANCE.getEDate(), date);
	}

	/**
	 * Find an EClass which is a common superclass of the given objects NOTE this does not work with multiple supertypes
	 * 
	 * @param objects
	 * @return
	 */
	public static EClass findCommonSuperclass(final Collection<EObject> objects) {
		EClass result = null;
		for (final EObject object : objects) {
			if (result == null) {
				result = object.eClass();
			} else {
				final EClass oClass = object.eClass();
				if (!result.isSuperTypeOf(oClass)) {
					// problem! find common supertype
					while (result != null && !result.isSuperTypeOf(oClass)) {
						final EList<EClass> supers = result.getESuperTypes();
						if (supers == null || supers.isEmpty()) {
							return null;
						}
						// TODO multiple inheritance issues?
						result = supers.get(0);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns true if all the {@link EObject}s in the given collection have the same {@link EClass}.
	 * 
	 * @param objects
	 * @return true if all objects have same {@link EClass}
	 */
	public static boolean allSameEClass(final Collection<? extends EObject> objects) {
		final Iterator<? extends EObject> it = objects.iterator();
		if (it.hasNext() == false)
			return true;
		final EClass firstClass = it.next().eClass();
		while (it.hasNext()) {
			if (it.next().eClass() != firstClass)
				return false;
		}
		return true;
	}
}
