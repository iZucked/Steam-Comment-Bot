/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.CharterContract#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCharterContract()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface CharterContract extends UUIDObject {

	/**
	 * Returns the value of the '<em><b>Ballast Bonus Contract</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Bonus Contract</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus Contract</em>' containment reference.
	 * @see #setBallastBonusContract(BallastBonusContract)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getCharterContract_BallastBonusContract()
	 * @model containment="true"
	 * @generated
	 */
	BallastBonusContract getBallastBonusContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.CharterContract#getBallastBonusContract <em>Ballast Bonus Contract</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Contract</em>' containment reference.
	 * @see #getBallastBonusContract()
	 * @generated
	 */
	void setBallastBonusContract(BallastBonusContract value);
} // CharterContract
