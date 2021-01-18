/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MMX Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.MMXObject#getExtensions <em>Extensions</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXObject()
 * @model abstract="true"
 * @generated
 */
public interface MMXObject extends EObject {
	/**
	 * @generated NOT
	 * @since 3.1
	 */
	public class DelegateInformation {
		// The subfield of the object to delegate to when a field is unset
		private final EStructuralFeature delegate;
		// The subfield of the delegate to use when a field is unset
		private final EStructuralFeature delegateFeature;
		// The value to use when a delegate is absent or there is no delegation
		private final Object absentDelegateValue;
		private Function<EObject, MMXObject> swapper;
		
		public DelegateInformation(EStructuralFeature delegate, EStructuralFeature delegateFeature, Object value) {
			this.delegate = delegate;
			this.delegateFeature = delegateFeature;
			this.absentDelegateValue = value;
		}
		
		public EStructuralFeature getDelegateFeature() {
			return delegateFeature;
		}

		public DelegateInformation(Function<EObject, MMXObject> swapper, EStructuralFeature delegate, EStructuralFeature delegateFeature, Object value) {
			this.swapper = swapper;
			this.delegate = delegate;
			this.delegateFeature = delegateFeature;
			this.absentDelegateValue = value;
		}
		
		/**
		 * @since 5.0
		 */
		public boolean delegatesTo(final Object changedFeature) {
			return delegate == changedFeature;
		}
		
		/**
		 * @since 5.0
		 */
		public Object getValue(final EObject object) {
			if (delegate != null) {
				MMXObject delegateObject;
				if (swapper != null) {
					delegateObject = swapper.apply(object);
				} else {
					delegateObject = (MMXObject) object.eGet(delegate);
				}
				if (delegateObject != null) {
					return delegateObject.eGet(delegateFeature);
				}
			}
			return absentDelegateValue;
		}
		
		public static DelegateInformation makeFeatureOverride(EObject object, // Object we are editing
				EStructuralFeature overrideOwnerFeature, // The reference to the object with values we are overriding
				EStructuralFeature overrideFeature, // The feature to use on the override object 
				BooleanSupplier isOverriddenSupplier, // Method to use to check if we are overridden
				Object defaultValue // default value if no other value found
				) {
			return new DelegateInformation(null, null, null) {
				
				@Override
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == overrideOwnerFeature);
				}
				
				@Override
				public Object getValue(final EObject object) {
					Object result = defaultValue;
					final EObject overrideOwner = (EObject)object.eGet(overrideOwnerFeature);
					
					if (!isOverriddenSupplier.getAsBoolean() && overrideOwner != null) {
						if (overrideOwner.eIsSet(overrideFeature)) {
							result = overrideOwner.eGet(overrideFeature);
						}
					}
					return result;
					
				}				
			};
		}
		
	}
	/**
	 * Returns the value of the '<em><b>Extensions</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extensions</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extensions</em>' containment reference list.
	 * @see com.mmxlabs.models.mmxcore.MMXCorePackage#getMMXObject_Extensions()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	EList<EObject> getExtensions();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" featureRequired="true"
	 * @generated
	 */
	Object getUnsetValue(EStructuralFeature feature);
	
	/**
	 * <!-- begin-user-doc -->
	 * Returns a {@link DelegateInformation} structure providing information about whether an unset feature
	 * is delegated to a subfield or not. If an unset feature is delegated, it should return a
	 * new DelegateInformation(delegateFeature, 
	 * 
	 * <!-- end-user-doc -->
	 * @generated NOT
	 * @since 3.1
	 */
	DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" featureRequired="true"
	 * @generated
	 */
	Object eGetWithDefault(EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true"
	 * @generated
	 */
	EObject eContainerOp();
	
	
} // MMXObject
