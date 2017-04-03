/**
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Based Ballast Bonus Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract#getRules <em>Rules</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRuleBasedBallastBonusContract()
 * @model
 * @generated
 */
public interface RuleBasedBallastBonusContract extends BallastBonusContract {

	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.BallastBonusContractLine}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRuleBasedBallastBonusContract_Rules()
	 * @model containment="true"
	 * @generated
	 */
	EList<BallastBonusContractLine> getRules();

} // RuleBasedBallastBonusContract
