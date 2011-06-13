/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
 *   <li>{@link scenario.market.MarketModel#getIndices <em>Indices</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getMarketModel()
 * @model
 * @generated
 */
public interface MarketModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Indices</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.market.Index}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Indices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Indices</em>' containment reference list.
	 * @see scenario.market.MarketPackage#getMarketModel_Indices()
	 * @model containment="true"
	 * @generated
	 */
	EList<Index> getIndices();

} // MarketModel
