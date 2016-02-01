/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.change.ChangePackage;
import org.eclipse.emf.ecore.xml.namespace.XMLNamespacePackage;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

public class ExhaustiveModelChecker {
	//private Map<EClass, List<EObject>> objectsByClass = new HashMap<EClass, List<EObject>>();
	private Map<EStructuralFeature, Set<Object>> fieldValues = new HashMap<EStructuralFeature, Set<Object>>();
	private Set<EObject> registeredObjects = new HashSet<EObject>();
	private Set<EClass> allClasses = new HashSet<EClass>();
	private Map<EClass, Set<EClass>> descendants = new HashMap<EClass, Set<EClass>>();

	static class AutoSetMap<Key, Value> extends HashMap<Key, Set<Value>> { 
		@Override
		public Set<Value> get(Object key) {
			if (containsKey(key) == false) {
				put((Key) key, new HashSet<Value>());
			}
			return super.get(key);
		}
	}
	
	/*
	public class OverridingSetWrapper<E> {
		final Set<E> additions = new HashSet<E>();
		final Set<E> exclusions = new HashSet<E>();
		
		public void add(E e) {
			additions.add(e);
			exclusions.remove(e);
		}
		
		public void remove(E e) {
			exclusions.add(e);
			additions.remove(e);
		}
		
		public Set<E> wrap(final Set<E> set) {
			final HashSet<E> result = new HashSet<E>(set);
			result.addAll(additions);
			result.removeAll(exclusions);
			return result;		
		}
	} 
	*/
	
	// class for dummy value objects
	private static class DummyValue {};
	
	// special object representing the unset value
	protected static Object unsetValueObject = new DummyValue() { 
		@Override
		public String toString() { return "<Unset Value>"; }
	}; 
	
	// special object representing an empty list
	protected static Object emptyListObject = new DummyValue() {
		@Override
		public String toString() { return "<Empty List>"; }
	}; 

	private List<EStructuralFeature> excludedFeatures = createExcludedFeatures();
	private AutoSetMap<EStructuralFeature, Object> additionalRequiredValues = createAdditionalRequiredValues();
	private AutoSetMap<EStructuralFeature, Object> excludedRequiredValues = createExcludedRequiredValues();
	private Set<EClass> excludedEClasses = createExcludedEClasses();
	private Collection<Class<?>> excludedPackageTypes = createExcludedPackageTypes();

	protected AutoSetMap<EStructuralFeature, Object> createAdditionalRequiredValues() {
		AutoSetMap<EStructuralFeature, Object> result = new AutoSetMap<EStructuralFeature, Object>();
		return result ;
	}
	
	protected Collection<Class<?>> createExcludedPackageTypes() {
		Collection<Class<?>> result = new LinkedList<Class<?>>();
		result.add(EcorePackage.class);
		result.add(XMLNamespacePackage.class);
		result.add(XMLTypePackage.class);
		result.add(ChangePackage.class);
		return result;
	}

	protected Set<EClass> createExcludedEClasses() {
		return new HashSet<EClass>();
	}

	protected AutoSetMap<EStructuralFeature, Object> createExcludedRequiredValues() {
		return new AutoSetMap<EStructuralFeature, Object>();
	}
	
	protected List<EStructuralFeature> createExcludedFeatures() {
		return new LinkedList<EStructuralFeature>();
	}
	
	protected Set<Object> getRequiredValues(EStructuralFeature feature) {
		Set<Object> result = new HashSet<Object>(getDefaultRequiredValues(feature));

		result.addAll(additionalRequiredValues.get(feature));
		result.removeAll(excludedRequiredValues.get(feature));
		
		return result;
	}
	
	protected List<EClass> getRequiredEClasses() {
		LinkedList<EClass> result = new LinkedList<EClass>(getDefaultRequiredEClasses());
		
		result.removeAll(excludedEClasses);
		
		return result;
	}
	
	protected List<EStructuralFeature> getRequiredFeatures() {
		List<EStructuralFeature> result = new LinkedList<EStructuralFeature>();
		for (EClass eClass: getRequiredEClasses()) {
			result.addAll(eClass.getEStructuralFeatures());
		}
		result.removeAll(excludedFeatures);
		return result;
	}
	
	private Map<EClass, Set<EClass>> calculateDescendants(Set<EClass> classes) {
		Map<EClass, Set<EClass>> result = new HashMap<EClass, Set<EClass>>();
		for (EClass eClass: classes) {
			result.put(eClass, new HashSet<EClass>());
		}
		
		for (EClass eClass: classes) {
			result.get(eClass).add(eClass);
			for (EClass parent: eClass.getEAllSuperTypes()) {
				if (classes.contains(parent) == false) {
					System.err.println("Class list does not contain " + parent.getName());
				}
				else {
					result.get(parent).add(eClass);
				}
			}
		}
		return result;
	}
	
	private void registerObjectFeature(EStructuralFeature feature, EObject object) {
		final boolean unset = feature.isUnsettable() && (object.eIsSet(feature) == false);
		
		if (unset) {
			// for unset fields, register the special unsetValueObject
			registerFieldContents(feature, unsetValueObject);
		}
		else {
			// for multiple content lists, register each element in the list
			if (feature.isMany()) {
				final EList values = (EList) object.eGet(feature);  
				for (Object value: values) {
					registerFieldContents(feature, value);
				}
				// for empty lists, register the special emptyListObject
				if (values.isEmpty()) {
					registerFieldContents(feature, emptyListObject);					
				}
			}
			// for anything else, register the value directly
			else {
				registerFieldContents(feature, object.eGet(feature));
			}
		}
			
	}
	
	/**
	 * Returns the object to store against the containing EMF feature for a particular value of that feature.
	 * EMF objects are not stored directly since we only care about their EClass.
	 * 
	 * @param value
	 * @return
	 */
	private Object getStoreValue(Object value) {
		if (value instanceof EObject) {
			return ((EObject) value).eClass();
		}
		
		return value;
	}
	
	/**
	 * Registers the contents of an EMF feature against that feature, and recursively registers any EObject values.
	 * @param feature
	 * @param rawValue
	 */
	private void registerFieldContents(EStructuralFeature feature, Object rawValue) {	
		final Object storeValue = getStoreValue(rawValue);
		
		if (fieldValues.containsKey(feature) == false) {
			fieldValues.put(feature, new HashSet<Object>());
		}
		
		fieldValues.get(feature).add(storeValue);
		
		if (rawValue instanceof EObject) {
			registerObject((EObject) rawValue);
		}
		
	}

	/**
	 * Records all of an object's fields, and all of its sub-objects' fields, against
	 * the EStructuralFeature objects which represent those fields. If a field is unset,
	 * the special unsetObject object is stored; if the EStructuralFeature is a reference
	 * type, the EClass of the relevant object is stored; otherwise, the actual value 
	 * of the field is stored.
	 * 
	 * @param object
	 */
	private void registerObject(EObject object) {
		// only register EObject objects
		if ((object instanceof EObject) == false) {
			return;
		}
		
		// avoid infinite loops
		if (registeredObjects.contains(object)) {
			return;
		}	
		
		registeredObjects.add(object);
		
		EClass clazz = object.eClass();
		/*
		// add the object to the list of objects for this class
		if (objectsByClass.containsKey(clazz) == false) {
			objectsByClass.put(clazz, new LinkedList<EObject>());
		}
		objectsByClass.get(clazz).add(object);
		*/
				
		// for each feature in the object, register that feature
		for (EStructuralFeature feature: clazz.getEAllStructuralFeatures()) {
			registerObjectFeature(feature, object);
		}		
	}
	
	/**
	 * Returns a list of the default required values for a feature.
	 * 
	 * @param feature
	 * @return
	 */
	private Set<Object> getDefaultRequiredValues(EStructuralFeature feature) {
		final HashSet<Object> result = new HashSet<Object>();
		
		if (feature.isUnsettable()) {
			result.add(unsetValueObject);
		}
		
		/*
		if (feature.isMany()) {
			result.add(emptyListObject);
		}
		*/
		
		if (feature instanceof EReference) {
			EClass type = ((EReference) feature).getEReferenceType();
			
			if (allClasses.contains(type) == false) {
				System.err.println("Class list does not contain " + type.getName());
			}
			else {
				for (EClass eClass: descendants.get(type)) {
					if (eClass.isAbstract() == false)
					result.add(eClass);
				}
			}
		}
		
		return result;
	}
	
	public String expectedValueLabel(Object value) {
		if (value instanceof EClass) {
			return String.format("any %s object", ((EClass) value).getName());
		}
		return value.toString();
	}
	
	public String featureLabel(EStructuralFeature feature) {
		EClass eClass = feature.getEContainingClass();
		return String.format("%s %s.%s", eClass.getEPackage().getName(), eClass.getName(), feature.getName());
	}
	
	/**
	 * Checks that an appropriate range of values were registered against a feature.
	 * 
	 * @param feature
	 * @return A list of problems
	 */
	private List<String> checkFeatureValues(EStructuralFeature feature) {
		final List<String> problems = new LinkedList<String>();
		
		final Set<Object> values = fieldValues.get(feature);
		
		boolean hasActualValue = false;
		// check the feature had at least one non-empty value
		if (values != null) {
			for (Object value: values) {
				if ((value instanceof DummyValue) == false) {
					hasActualValue = true;
					break;
				}
			}
		}
		
		if (hasActualValue == false) {
			problems.add(String.format("%s did not contain an actual value", featureLabel(feature)));
		}
		else {
			final Set<Object> missingValues = new HashSet<Object>(getRequiredValues(feature));
			missingValues.removeAll(values);
			
			for (Object missingValue: missingValues) {
				String message = String.format("%s did not contain %s", featureLabel(feature), expectedValueLabel(missingValue));
				problems.add(message);
			}
		}
		
		return problems;
	}
	
	/**
	 * Returns the set of EClass types with registered objects.
	 * 
	 * @return A list of problems
	 */
	private Set<EClass> getObjectEClasses() {
		
		final Set<EClass> objectEClasses = new HashSet<EClass>();
		for (EObject object: registeredObjects) {
			objectEClasses.add(object.eClass());
		}
		
		return objectEClasses;		
	}
	
	/**
	 * Returns the list of EClass types which are expected to occur in the model.
	 * 
	 * @return
	 */
	private Set<EClass> getDefaultRequiredEClasses() {		
		Set<EClass> result = new HashSet<EClass>();
		for (EClass eClass: allClasses) {
			if (eClass.isAbstract() == false && eClass.isInterface() == false && excludedEClasses.contains(eClass) == false) {
				result.add(eClass);
			}
		}
		return result ;
	}

	/**
	 * checks a scenario to make sure that it has an exhaustive list
	 * of possible object types
	 * 
	 * this means at least one object for each instantiatable EClass
	 * every unsettable attribute must appear in both set and unset states
	 * 
	 */
	
	public List<String> checkModel(EObject rootObject) {
		final List<String> problems = new LinkedList<String>();
		
		allClasses = setupAllClasses();
		descendants = calculateDescendants(allClasses);
		registerObject(rootObject);		
		
		Set<EClass> objectEClasses = getObjectEClasses();
		Set<EStructuralFeature> checkedFeatures = new HashSet<EStructuralFeature>(); 
		
		// check each EClass
		for (EClass eClass: getRequiredEClasses()) {
			// check that the model contains an 
			if (objectEClasses.contains(eClass) == false) {
				problems.add(String.format("Model contains no object of class %s", eClass.getName()));
			}
			else {
				HashSet<EStructuralFeature> features = new HashSet<EStructuralFeature>(eClass.getEAllStructuralFeatures());
				features.removeAll(excludedFeatures);
				features.removeAll(checkedFeatures);
				// check that each structural feature has the required range of values
				for (EStructuralFeature feature: features) {
					problems.addAll(checkFeatureValues(feature));
					checkedFeatures.add(feature);
				}
			}
		}
				
		return problems;
	}
	
	private boolean isPackageRequired(EPackage pkg) {
		for (Class<?> clazz: excludedPackageTypes) {
			if (clazz.isInstance(pkg)) { 
				return false;
			}
		}
		return true;
	}
	
	private Collection<EPackage> getAllPackages() {
		final ArrayList<EPackage> result = new ArrayList<EPackage>();
		for (Object object: EPackage.Registry.INSTANCE.values()) {
			if (object instanceof EPackage && isPackageRequired((EPackage) object)) {
				result.add((EPackage) object);
			}
		}
		
		return result;
	}

	private Set<EClass> setupAllClasses() {
		Set<EClass> result = new HashSet<EClass>();
		
		for (EPackage pkg: getAllPackages()) {
			for (EClassifier cls: pkg.getEClassifiers()) {
				if (cls instanceof EClass) {
					result.add((EClass) cls);
				}
			}
		}
		return result;
	}
}
