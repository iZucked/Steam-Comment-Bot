package com.mmxlabs.lngscheduler.emf.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import scenario.Scenario;
import scenario.ScenarioPackage;

/**
 * Utility class for doing things to scenarios.
 * @author hinton
 *
 */
public class ScenarioUtils {
	public static Scenario readScenarioFile(final String filepath) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI,
				ScenarioPackage.eINSTANCE);
		
		Resource resource = resourceSet.getResource(
				URI.createFileURI(filepath),
				true);
		for (EObject e : resource.getContents()) {
			if (e instanceof Scenario) {
				return (Scenario) e;
			}
		}
		return null;
	}
}
