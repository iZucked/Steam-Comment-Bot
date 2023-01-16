/**
 */
package com.mmxlabs.models.lng.fleet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Emission Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.EmissionParameters#getBaseFuelRate <em>Base Fuel Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.EmissionParameters#getBogRate <em>Bog Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.EmissionParameters#getPilotLightBaseRate <em>Pilot Light Base Rate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getEmissionParameters()
 * @model
 * @generated
 */
public interface EmissionParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Fuel Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Rate</em>' attribute.
	 * @see #setBaseFuelRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getEmissionParameters_BaseFuelRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kg/MT' formatString='##0.###'"
	 * @generated
	 */
	double getBaseFuelRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.EmissionParameters#getBaseFuelRate <em>Base Fuel Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Rate</em>' attribute.
	 * @see #getBaseFuelRate()
	 * @generated
	 */
	void setBaseFuelRate(double value);

	/**
	 * Returns the value of the '<em><b>Bog Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bog Rate</em>' attribute.
	 * @see #setBogRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getEmissionParameters_BogRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kg/m\263' formatString='##0.###'"
	 * @generated
	 */
	double getBogRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.EmissionParameters#getBogRate <em>Bog Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bog Rate</em>' attribute.
	 * @see #getBogRate()
	 * @generated
	 */
	void setBogRate(double value);

	/**
	 * Returns the value of the '<em><b>Pilot Light Base Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pilot Light Base Rate</em>' attribute.
	 * @see #setPilotLightBaseRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getEmissionParameters_PilotLightBaseRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kg/MT' formatString='##0.###'"
	 * @generated
	 */
	double getPilotLightBaseRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.EmissionParameters#getPilotLightBaseRate <em>Pilot Light Base Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pilot Light Base Rate</em>' attribute.
	 * @see #getPilotLightBaseRate()
	 * @generated
	 */
	void setPilotLightBaseRate(double value);

} // EmissionParameters
