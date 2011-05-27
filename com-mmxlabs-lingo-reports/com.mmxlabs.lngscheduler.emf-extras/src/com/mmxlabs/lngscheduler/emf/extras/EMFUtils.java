/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.Slot;
import scenario.fleet.CharterOut;
import scenario.fleet.PortAndTime;

import com.mmxlabs.common.Pair;

/**
 * Utility class for doing things to scenarios.
 * 
 * @author hinton
 * 
 */
public class EMFUtils {
	/**
	 * Serialize an EObject into a byte array; currently EMF generated models do
	 * not serialize properly, particularly models with complicated containment
	 * going on.
	 * 
	 * @see #deserialiseEObject(byte[], EClass) - the inverse of this method
	 * @param object
	 *            an object to serialize
	 * @return a byte array containing a serialized form of the object
	 */
	public static byte[] serializeEObject(final EObject object) {
		final ResourceSet set = new ResourceSetImpl();
		final XMIResourceFactoryImpl rf = new XMIResourceFactoryImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", rf);

		set.getPackageRegistry().put(object.eClass().getEPackage().getNsURI(),
				object.eClass().getEPackage());

		final Resource resource = set.createResource(URI.createGenericURI(
				"invalid", "invalid", "invalid"));

		resource.getContents().add(object);

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			resource.save(baos, Collections.emptyMap());
		} catch (IOException e) {
		} // byte array cannot produce IO exception

		return baos.toByteArray();
	}

	/**
	 * Deserialize a byte array produced by {@link #serializeEObject(EObject)}.
	 * See the doc for that on why this is necessary.
	 * 
	 * @param <T>
	 * @param byteArray
	 *            the serialized form of the object
	 * @param clazz
	 *            the EClass of the object
	 * @return the deserialized object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialiseEObject(final byte[] byteArray,
			final EClass clazz) {
		final ResourceSet set = new ResourceSetImpl();
		final XMIResourceFactoryImpl rf = new XMIResourceFactoryImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", rf);

		set.getPackageRegistry().put(clazz.getEPackage().getNsURI(),
				clazz.getEPackage());

		final Resource resource = set.createResource(URI.createGenericURI(
				"invalid", "invalid", "invalid"));

		final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
		try {
			resource.load(bais, Collections.emptyMap());
		} catch (IOException e) {
		}
		return (T) clazz.getInstanceClass().cast(resource.getContents().get(0));
	}

	public static <T> T readObjectFromFile(final String filepath,
			final Class<? extends T> clazz) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI,
				ScenarioPackage.eINSTANCE);

		Resource resource = resourceSet.getResource(
				URI.createFileURI(filepath), true);
		for (EObject e : resource.getContents()) {
			if (clazz.isInstance(e)) {
				return clazz.cast(e);
			}
		}
		return null;
	}

	/**
	 * Iterate through all the attributes of the given EObject and find the
	 * earliest and latest dates
	 * 
	 * @param object
	 * @return a pair, first element containing the earliest date and second the
	 *         latest
	 */
	public static Pair<Date, Date> findMinMaxDateAttributes(final EObject object) {
		final Pair<Date, Date> result = new Pair<Date, Date>();

		findMinMaxDateAttributes(object, result);
		System.err.println(result);
		return result;

	}

	private static void updateMinMax(Pair<Date, Date> pair, Date date) {
		if (date == null)
			return;
		if (pair.getFirst() == null || date.before(pair.getFirst()))
			pair.setFirst(date);
		if (pair.getSecond() == null || date.after(pair.getSecond()))
			pair.setSecond(date);

	}

	public static Pair<Date, Date> findEarliestAndLatestEvents(
			final Scenario scenario) {
		final Pair<Date, Date> result = new Pair<Date, Date>(null, null);

		final TreeIterator<EObject> iterator = scenario.eAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof PortAndTime) {
				final PortAndTime pat = (PortAndTime) o;
				updateMinMax(result, pat.getStartTime());
				updateMinMax(result, pat.getEndTime());
			} else if (o instanceof Slot) {
				final Slot slot = (Slot) o;
				updateMinMax(result, slot.getWindowStart());
				updateMinMax(result, slot.getWindowEnd());
			} else if (o instanceof CharterOut) {
				updateMinMax(result, ((CharterOut) o).getStartDate());
				updateMinMax(result, ((CharterOut) o).getEndDate());
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
	private static void findMinMaxDateAttributes(final EObject object,
			final Pair<Date, Date> mm) {

		final TreeIterator<EObject> iter = object.eAllContents();
		while (iter.hasNext()) {
			final EObject sub = iter.next();
			final EClass eClass = sub.eClass();

			for (final EAttribute attribute : eClass.getEAllAttributes()) {
				if (sub.eIsSet(attribute)) {
					if (Date.class.isAssignableFrom(attribute
							.getEAttributeType().getInstanceClass())) {
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
			for (final EClassifier otherClass : eClass.getEPackage()
					.getEClassifiers()) {
				if (otherClass instanceof EClass) {
					if (!(((EClass) otherClass).isAbstract())
							&& eClass.isSuperTypeOf((EClass) otherClass)) {
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
		final EObject result = concrete.getEPackage().getEFactoryInstance()
				.create(concrete);

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
	 * Find an EClass which is a common superclass of the given objects
	 * NOTE this does not work with multiple supertypes
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
					while (!result.isSuperTypeOf(oClass)) {
						final EList<EClass> supers = result.getESuperTypes();
						if (supers == null || supers.isEmpty())
							return null;
						result = supers.get(0); // TODO
												// multiple
												// inheritance
												// issues?
					}
				}
			}
		}
		return result;
	}
}
