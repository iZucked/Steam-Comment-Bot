/**
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Panama Canal Tariff</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getBands <em>Bands</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff()
 * @model
 * @generated
 */
public interface PanamaCanalTariff extends EObject {
	/**
	 * Returns the value of the '<em><b>Bands</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bands</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bands</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff_Bands()
	 * @model containment="true"
	 * @generated
	 */
	EList<PanamaCanalTariffBand> getBands();

} // PanamaCanalTariff
