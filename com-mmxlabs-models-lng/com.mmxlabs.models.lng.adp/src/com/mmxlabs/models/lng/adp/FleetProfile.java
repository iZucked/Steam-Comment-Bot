/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
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
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultVessel <em>Default Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultVesselCharterInRate <em>Default Vessel Charter In Rate</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Vessel Events</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Events</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_VesselEvents()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselEvent> getVesselEvents();

	/**
	 * Returns the value of the '<em><b>Default Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Vessel</em>' reference.
	 * @see #setDefaultVessel(Vessel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_DefaultVessel()
	 * @model
	 * @generated
	 */
	Vessel getDefaultVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultVessel <em>Default Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Vessel</em>' reference.
	 * @see #getDefaultVessel()
	 * @generated
	 */
	void setDefaultVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Default Vessel Charter In Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Vessel Charter In Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Vessel Charter In Rate</em>' attribute.
	 * @see #setDefaultVesselCharterInRate(String)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFleetProfile_DefaultVesselCharterInRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getDefaultVesselCharterInRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FleetProfile#getDefaultVesselCharterInRate <em>Default Vessel Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Vessel Charter In Rate</em>' attribute.
	 * @see #getDefaultVesselCharterInRate()
	 * @generated
	 */
	void setDefaultVesselCharterInRate(String value);

} // FleetProfile
