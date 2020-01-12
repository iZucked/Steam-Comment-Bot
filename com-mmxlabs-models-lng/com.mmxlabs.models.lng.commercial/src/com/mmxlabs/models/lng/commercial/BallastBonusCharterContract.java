/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
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
 *   <li>{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract#getRepositioningFee <em>Repositioning Fee</em>}</li>
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
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositioning Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #setRepositioningFee(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getBallastBonusCharterContract_RepositioningFee()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getRepositioningFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.BallastBonusCharterContract#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(String value);

} // BallastBonusCharterContract
