/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fleet Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#isIncludeEnabledCharterMarkets <em>Include Enabled Charter Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile()
 * @model
 * @generated
 */
public interface FleetProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Availabilities</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselAvailability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Availabilities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Availabilities</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_VesselAvailabilities()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselAvailability> getVesselAvailabilities();

	/**
	 * Returns the value of the '<em><b>Include Enabled Charter Markets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Include Enabled Charter Markets</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Enabled Charter Markets</em>' attribute.
	 * @see #setIncludeEnabledCharterMarkets(boolean)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_IncludeEnabledCharterMarkets()
	 * @model
	 * @generated
	 */
	boolean isIncludeEnabledCharterMarkets();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FleetProfile#isIncludeEnabledCharterMarkets <em>Include Enabled Charter Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Enabled Charter Markets</em>' attribute.
	 * @see #isIncludeEnabledCharterMarkets()
	 * @generated
	 */
	void setIncludeEnabledCharterMarkets(boolean value);

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.FleetConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_Constraints()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<FleetConstraint> getConstraints();

} // FleetProfile
