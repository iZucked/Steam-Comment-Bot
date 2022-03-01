/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.port.Port;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spacing Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.SpacingProfile#getDefaultPort <em>Default Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SpacingProfile#getFobSpacingAllocations <em>Fob Spacing Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.SpacingProfile#getDesSpacingAllocations <em>Des Spacing Allocations</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpacingProfile()
 * @model
 * @generated
 */
public interface SpacingProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Default Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Port</em>' reference.
	 * @see #setDefaultPort(Port)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpacingProfile_DefaultPort()
	 * @model
	 * @generated
	 */
	Port getDefaultPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.SpacingProfile#getDefaultPort <em>Default Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Port</em>' reference.
	 * @see #getDefaultPort()
	 * @generated
	 */
	void setDefaultPort(Port value);

	/**
	 * Returns the value of the '<em><b>Fob Spacing Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.FobSpacingAllocation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fob Spacing Allocations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpacingProfile_FobSpacingAllocations()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<FobSpacingAllocation> getFobSpacingAllocations();

	/**
	 * Returns the value of the '<em><b>Des Spacing Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.DesSpacingAllocation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Spacing Allocations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getSpacingProfile_DesSpacingAllocations()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<DesSpacingAllocation> getDesSpacingAllocations();

} // SpacingProfile
