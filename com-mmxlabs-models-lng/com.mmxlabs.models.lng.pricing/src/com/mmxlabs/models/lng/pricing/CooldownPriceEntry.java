/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.MMXObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cooldown Price Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry#getLumpsumExpression <em>Lumpsum Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry#getVolumeExpression <em>Volume Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCooldownPriceEntry()
 * @model
 * @generated
 */
public interface CooldownPriceEntry extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCooldownPriceEntry_Ports()
	 * @model required="true"
	 * @generated
	 */
	EList<APortSet<Port>> getPorts();

	/**
	 * Returns the value of the '<em><b>Lumpsum Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lumpsum Expression</em>' attribute.
	 * @see #setLumpsumExpression(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCooldownPriceEntry_LumpsumExpression()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getLumpsumExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry#getLumpsumExpression <em>Lumpsum Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lumpsum Expression</em>' attribute.
	 * @see #getLumpsumExpression()
	 * @generated
	 */
	void setLumpsumExpression(String value);

	/**
	 * Returns the value of the '<em><b>Volume Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Expression</em>' attribute.
	 * @see #setVolumeExpression(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCooldownPriceEntry_VolumeExpression()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getVolumeExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry#getVolumeExpression <em>Volume Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Expression</em>' attribute.
	 * @see #getVolumeExpression()
	 * @generated
	 */
	void setVolumeExpression(String value);

} // CooldownPriceEntry
