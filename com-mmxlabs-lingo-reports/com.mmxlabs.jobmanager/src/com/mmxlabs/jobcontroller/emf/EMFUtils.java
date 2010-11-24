package com.mmxlabs.jobcontroller.emf;

import java.util.Date;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.mmxlabs.common.Pair;

import scenario.ScenarioPackage;

/**
 * Utility class for doing things to scenarios.
 * 
 * @author hinton
 * 
 */
public class EMFUtils {
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
