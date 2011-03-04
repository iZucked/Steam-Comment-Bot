package com.mmxlabs.lngscheduler.emf.extras;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import scenario.ScenarioPackage;

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
	 * @param byteArray the serialized form of the object
	 * @param clazz the EClass of the object
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
		final EClass eClass = object.eClass();

		for (final EAttribute attribute : eClass.getEAllAttributes()) {
			if (object.eIsSet(attribute)) {
				if (Date.class.isAssignableFrom(attribute.getEAttributeType()
						.getInstanceClass())) {
					final Date dt = (Date) object.eGet(attribute);

					if (mm.getFirst() == null || dt.before(mm.getFirst()))
						mm.setFirst(dt);

					if (mm.getSecond() == null || dt.after(mm.getSecond()))
						mm.setSecond(dt);
				}
			}
		}

		for (final EObject child : object.eContents()) {
			findMinMaxDateAttributes(child, mm);
		}
	}
}
