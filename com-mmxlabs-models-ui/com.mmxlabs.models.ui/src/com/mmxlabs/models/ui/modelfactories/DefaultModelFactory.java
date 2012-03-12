/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.modelfactories;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * The default model factory, which just instantiates stuff of the given class.
 * @author hinton
 *
 */
public class DefaultModelFactory implements IModelFactory {
	protected Date now;
	private String outputEClassName;
	private String extensionID;
	private String label;

	@Override
	public Collection<ISetting> createInstance(final MMXRootObject rootObject, final EObject container, final EReference containment) {
		final EObject output;
		
		if (outputEClassName == null || outputEClassName.isEmpty()) {
			output = constructInstance(containment.getEReferenceType());
		} else {
			// find the right EClass; this might be a bad way to do it
			try {
				@SuppressWarnings("unchecked")
				final Class<EObject> clazz = (Class<EObject>) Class.forName(outputEClassName);
				final EObject temp = clazz.newInstance();
				output = constructInstance(temp.eClass());
			} catch (InstantiationException e) {
				return null;
			} catch (IllegalAccessException e) {
				return null;
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		final Calendar nowCal = Calendar.getInstance();
		nowCal.clear(Calendar.MILLISECOND);
		nowCal.clear(Calendar.SECOND);
		nowCal.clear(Calendar.MINUTE);
		
		
		now = nowCal.getTime();
		postprocess(output);
		final ISetting setting = new ISetting() {
			@Override
			public EObject getInstance() {
				return output;
			}

			@Override
			public EObject getContainer() {
				return container;
			}

			@Override
			public EReference getContainment() {
				return containment;
			}
		};
		return Collections.singleton(setting);
	}

	protected EObject constructInstance(final EClass eClass) {
		final EObject object = eClass.getEPackage().getEFactoryInstance().create(eClass);

		// create singly-contained sub objects, by default
		
		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (reference.isMany() == false && reference.isContainment() == true) {
				object.eSet(reference, createSubInstance(object, reference));
			}
		}
		
		return object;
	}
	
	protected EObject createSubInstance(final EObject top, final EReference reference) {
		return constructInstance(reference.getEReferenceType());
	}
	
	protected void postprocess(final EObject top) {
		for (final EAttribute attribute : top.eClass().getEAllAttributes()) {
			if (attribute.getEAttributeType() == EcorePackage.eINSTANCE.getEDate() && 
					attribute.isUnsettable() &&
					top.eGet(attribute) == null) {
				top.eSet(attribute, now);
			}
		}
		for (final EObject o : top.eContents()) {
			postprocess(o);
		}
	}

	@Override
	public void initFromExtension(String ID, String outputEClassName, String label) {
		this.label = label;
		this.extensionID = ID;
		this.outputEClassName = outputEClassName;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
