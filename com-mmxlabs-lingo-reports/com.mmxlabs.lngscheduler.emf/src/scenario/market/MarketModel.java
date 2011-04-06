/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.market;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.market.MarketModel#getMarkets <em>Markets</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getMarketModel()
 * @model
 * @generated
 */
public interface MarketModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Markets</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.market.Market}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Markets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Markets</em>' containment reference list.
	 * @see scenario.market.MarketPackage#getMarketModel_Markets()
	 * @model containment="true"
	 * @generated
	 */
	EList<Market> getMarkets();

} // MarketModel
