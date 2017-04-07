/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.modelfactories;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * The default model factory, which just instantiates stuff of the given class.
 * 
 * @author hinton
 * 
 */
public class DefaultModelFactory implements IModelFactory {
	protected Date now;
	private String extensionID;
	private String label;
	protected String prototypeClass;
	/**
	 */
	protected String referenceToUseGivenClass;
	/**
	 */
	protected String classToUseForGivenReference;

	/**
	 */
	protected EClass findEClassInClassifiers(String name, EList<EClassifier> eclassifiers) {
		for (final EClassifier e : eclassifiers) {
			if (e.getInstanceClass() != null && e.getInstanceClass().getCanonicalName().equals(name)) {
				return (EClass) e;
			}
		}
		return null;
	}

	/**
	 */
	protected EClass getEClassFromName(String name, final EReference containment) {
		if (name != null) {
			EClass result;

			if (containment != null) {
				result = findEClassInClassifiers(name, containment.getEReferenceType().getEPackage().getEClassifiers());
				if (result != null)
					return result;
			}

			// All registry packages...
			for (final Object obj : Registry.INSTANCE.values()) {
				if (obj instanceof EPackage) {
					result = findEClassInClassifiers(name, ((EPackage) obj).getEClassifiers());
					if (result != null)
						return result;
				}

			}
		}
		if (containment != null) {
			return containment.getEReferenceType();
		}
		return null;
	}

	@Override
	public Collection<ISetting> createInstance(final MMXRootObject rootObject, final EObject container, final EReference containment, @Nullable Collection<@NonNull EObject> selection) {
		EObject output = null;

		if (prototypeClass == null) {
			output = constructInstance(containment.getEReferenceType());
		} else {
			for (final EClassifier e : containment.getEReferenceType().getEPackage().getEClassifiers()) {
				if (e instanceof EClass) {
					if (e.getInstanceClass() != null && e.getInstanceClass().getCanonicalName().equals(prototypeClass)) {
						output = constructInstance((EClass) e);
						break;
					}
				}
			}

			if (output == null) {
				// All registry packages...
				for (final Object obj : Registry.INSTANCE.values()) {
					if (obj instanceof EPackage) {
						final EPackage ePackage2 = (EPackage) obj;
						for (final EClassifier e : ePackage2.getEClassifiers()) {
							if (e instanceof EClass) {
								if (e.getInstanceClass() != null && e.getInstanceClass().getCanonicalName().equals(prototypeClass)) {
									output = constructInstance((EClass) e);
									break;
								}
							}
						}
					}

				}

			}

			if (output == null)
				output = constructInstance(containment.getEReferenceType());
		}

		addSelectionToInstance(output.eClass(), output, selection);

		final Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		nowCal.clear(Calendar.MILLISECOND);
		nowCal.clear(Calendar.SECOND);
		nowCal.clear(Calendar.MINUTE);
		nowCal.clear(Calendar.HOUR_OF_DAY);

		now = nowCal.getTime();
		postprocess(output);
		final EObject finalOutput = output;
		final ISetting setting = new ISetting() {
			@Override
			public EObject getInstance() {
				return finalOutput;
			}

			@Override
			public EObject getContainer() {
				return container;
			}

			@Override
			public EReference getContainment() {
				return containment;
			}

			@Override
			public @Nullable Collection<@NonNull EObject> getSelection() {
				return selection;
			}
		};
		return Collections.singleton(setting);
	}

	protected EObject constructInstance(final EClass eClass) {
		final EObject object = eClass.getEPackage().getEFactoryInstance().create(eClass);

		// create singly-contained sub objects, by default

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (reference.isMany() == false && reference.isContainment() == true && !reference.getEReferenceType().isInterface() && !reference.getEReferenceType().isAbstract()) {
				object.eSet(reference, createSubInstance(object, reference));
			}
		}

		return object;
	}

	protected EObject createSubInstance(final EObject top, final EReference reference) {
		// override the instance class for the specified attribute if necessary
		if (referenceToUseGivenClass != null) {
			final EStructuralFeature referenceToReplace = top.eClass().getEStructuralFeature(referenceToUseGivenClass);
			if (referenceToReplace == reference) {
				final EClass classToReplaceWith = getEClassFromName(classToUseForGivenReference, null);
				if (classToReplaceWith != null) {
					return constructInstance(classToReplaceWith);
				}
			}
		}

		return constructInstance(reference.getEReferenceType());
	}

	protected void postprocess(final EObject top) {
		for (final EAttribute attribute : top.eClass().getEAllAttributes()) {
			if (attribute.getEAttributeType() == EcorePackage.eINSTANCE.getEDate() && !attribute.isUnsettable() && top.eGet(attribute) == null) {
				top.eSet(attribute, now);
			}
		}
		for (final EObject o : top.eContents()) {
			postprocess(o);
		}
	}

	/**
	 * Opportunity to process the selection and update the newly created instance.
	 * 
	 * @param cls
	 * @param instance
	 * @param selection
	 */
	protected void addSelectionToInstance(final @NonNull EClass cls, final @NonNull EObject instance, @Nullable Collection<@NonNull EObject> selection) {

	}

	/**
	 */
	@Override
	public void initFromExtension(final String ID, final String label, final String prototype, final String replacementEReference, final String replacementEClass) {
		this.label = label;
		this.extensionID = ID;
		this.prototypeClass = prototype;
		this.referenceToUseGivenClass = replacementEReference;
		this.classToUseForGivenReference = replacementEClass;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
