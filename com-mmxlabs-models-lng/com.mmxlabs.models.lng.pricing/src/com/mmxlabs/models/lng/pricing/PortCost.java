/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortCost#getReferenceCapacity <em>Reference Capacity</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost()
 * @model
 * @generated
 */
public interface PortCost extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_Ports()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getPorts();

	/**
	 * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PortCostEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_Entries()
	 * @model containment="true"
	 * @generated
	 */
	EList<PortCostEntry> getEntries();

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
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortCost_ReferenceCapacity()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='#,###,##0'"
	 * @generated
	 */
	int getReferenceCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCost#getReferenceCapacity <em>Reference Capacity</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.PortCost#getReferenceCapacity <em>Reference Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetReferenceCapacity()
	 * @see #getReferenceCapacity()
	 * @see #setReferenceCapacity(int)
	 * @generated
	 */
	void unsetReferenceCapacity();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.PortCost#getReferenceCapacity <em>Reference Capacity</em>}' attribute is set.
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" vesselClassRequired="true" activityRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='for (final PortCostEntry entry : getEntries()) {\n\tif (entry.getActivity() == activity) {\n\t\tif (isSetReferenceCapacity()) {\n\t\t\treturn (int)\n\t\t\t\t(entry.getCost() * (((VesselClass)vesselClass).getCapacity() / (double) getReferenceCapacity()));\n\t\t} else {\n\t\t\treturn entry.getCost();\n\t\t}\n\t}\n}\nreturn 0;'"
	 * @generated
	 */
	int getPortCost(VesselClass vesselClass, PortCapability activity);

} // end of  PortCost

// finish type fixing
