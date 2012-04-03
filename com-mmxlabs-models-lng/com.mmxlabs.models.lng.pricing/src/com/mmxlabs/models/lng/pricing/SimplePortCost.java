

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Port Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SimplePortCost#getReferenceCapacity <em>Reference Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SimplePortCost#getAppliesTo <em>Applies To</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSimplePortCost()
 * @model
 * @generated
 */
public interface SimplePortCost extends PortCostDefinition {
	/**
	 * Returns the value of the '<em><b>Reference Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Capacity</em>' attribute.
	 * @see #isSetReferenceCapacity()
	 * @see #unsetReferenceCapacity()
	 * @see #setReferenceCapacity(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSimplePortCost_ReferenceCapacity()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getReferenceCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SimplePortCost#getReferenceCapacity <em>Reference Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Capacity</em>' attribute.
	 * @see #isSetReferenceCapacity()
	 * @see #unsetReferenceCapacity()
	 * @see #getReferenceCapacity()
	 * @generated
	 */
	void setReferenceCapacity(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.SimplePortCost#getReferenceCapacity <em>Reference Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetReferenceCapacity()
	 * @see #getReferenceCapacity()
	 * @see #setReferenceCapacity(int)
	 * @generated
	 */
	void unsetReferenceCapacity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.SimplePortCost#getReferenceCapacity <em>Reference Capacity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Reference Capacity</em>' attribute is set.
	 * @see #unsetReferenceCapacity()
	 * @see #getReferenceCapacity()
	 * @see #setReferenceCapacity(int)
	 * @generated
	 */
	boolean isSetReferenceCapacity();

	/**
	 * Returns the value of the '<em><b>Applies To</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Applies To</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Applies To</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSimplePortCost_AppliesTo()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getAppliesTo();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" vesselRequired="true" vesselClassRequired="true" activityRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='for (final PortCostEntry entry : getEntries()) {\n\tif (entry.getActivity() == activity) {\n\t\treturn (int)\n\t\t\t(entry.getCost() * vesselClass.getCapacity() / (double) getReferenceCapacity());\n\t}\n}\nreturn 0;'"
	 * @generated
	 */
	int getPortCost(AVessel vessel, AVesselClass vesselClass, PortCapability activity);

} // end of  SimplePortCost

// finish type fixing
