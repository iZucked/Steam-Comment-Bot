

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.AIndex;
import com.mmxlabs.models.lng.types.APortSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profit Share Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getBaseMarket <em>Base Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getMultiplier <em>Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getShare <em>Share</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePurchaseContract()
 * @model
 * @generated
 */
public interface ProfitSharePurchaseContract extends PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Base Market</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Market</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Market</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePurchaseContract_BaseMarket()
	 * @model
	 * @generated
	 */
	EList<APortSet> getBaseMarket();

	/**
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(Index)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePurchaseContract_Index()
	 * @model required="true"
	 * @generated
	 */
	AIndex getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(AIndex value);

	/**
	 * Returns the value of the '<em><b>Constant</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constant</em>' attribute.
	 * @see #setConstant(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePurchaseContract_Constant()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getConstant <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constant</em>' attribute.
	 * @see #getConstant()
	 * @generated
	 */
	void setConstant(double value);

	/**
	 * Returns the value of the '<em><b>Multiplier</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplier</em>' attribute.
	 * @see #setMultiplier(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePurchaseContract_Multiplier()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getMultiplier <em>Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplier</em>' attribute.
	 * @see #getMultiplier()
	 * @generated
	 */
	void setMultiplier(double value);

	/**
	 * Returns the value of the '<em><b>Share</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Share</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Share</em>' attribute.
	 * @see #setShare(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePurchaseContract_Share()
	 * @model
	 * @generated
	 */
	double getShare();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract#getShare <em>Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Share</em>' attribute.
	 * @see #getShare()
	 * @generated
	 */
	void setShare(double value);

} // end of  ProfitSharePurchaseContract

// finish type fixing
