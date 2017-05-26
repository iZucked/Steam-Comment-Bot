/**
 */
package com.mmxlabs.models.lng.commercial;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ballast Bonus Charter Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract#getEntity <em>Entity</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusCharterContract()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface BallastBonusCharterContract extends CharterContract {
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusCharterContract_BallastBonusContract()
	 * @model containment="true"
	 * @generated
	 */
	BallastBonusContract getBallastBonusContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract#getBallastBonusContract <em>Ballast Bonus Contract</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Contract</em>' containment reference.
	 * @see #getBallastBonusContract()
	 * @generated
	 */
	void setBallastBonusContract(BallastBonusContract value);

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusCharterContract_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

} // BallastBonusCharterContract
