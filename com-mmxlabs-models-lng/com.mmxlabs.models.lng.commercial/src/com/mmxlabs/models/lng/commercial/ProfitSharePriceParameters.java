

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
 * A representation of the model object '<em><b>Profit Share Price Parameters</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketPorts <em>Base Market Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketIndex <em>Base Market AIndex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketMultiplier <em>Base Market Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketConstant <em>Base Market Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getRefMarketIndex <em>Ref Market AIndex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getRefMarketMultiplier <em>Ref Market Multiplier</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getRefMarketConstant <em>Ref Market Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getShare <em>Share</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getMargin <em>Margin</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getSalesMultiplier <em>Sales Multiplier</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters()
 * @model
 * @generated
 */
public interface ProfitSharePriceParameters extends LNGPriceCalculatorParameters {
	/**
	 * Returns the value of the '<em><b>Base Market Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Market Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Market Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_BaseMarketPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet> getBaseMarketPorts();

	/**
	 * Returns the value of the '<em><b>Base Market AIndex</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Market AIndex</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Market AIndex</em>' reference.
	 * @see #setBaseMarketIndex(AIndex)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_BaseMarketIndex()
	 * @model required="true"
	 * @generated
	 */
	AIndex getBaseMarketIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketIndex <em>Base Market AIndex</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Market AIndex</em>' reference.
	 * @see #getBaseMarketIndex()
	 * @generated
	 */
	void setBaseMarketIndex(AIndex value);

	/**
	 * Returns the value of the '<em><b>Base Market Multiplier</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Market Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Market Multiplier</em>' attribute.
	 * @see #setBaseMarketMultiplier(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_BaseMarketMultiplier()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getBaseMarketMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketMultiplier <em>Base Market Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Market Multiplier</em>' attribute.
	 * @see #getBaseMarketMultiplier()
	 * @generated
	 */
	void setBaseMarketMultiplier(double value);

	/**
	 * Returns the value of the '<em><b>Base Market Constant</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Market Constant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Market Constant</em>' attribute.
	 * @see #setBaseMarketConstant(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_BaseMarketConstant()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getBaseMarketConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getBaseMarketConstant <em>Base Market Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Market Constant</em>' attribute.
	 * @see #getBaseMarketConstant()
	 * @generated
	 */
	void setBaseMarketConstant(double value);

	/**
	 * Returns the value of the '<em><b>Ref Market AIndex</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Market AIndex</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Market AIndex</em>' reference.
	 * @see #setRefMarketIndex(AIndex)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_RefMarketIndex()
	 * @model required="true"
	 * @generated
	 */
	AIndex getRefMarketIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getRefMarketIndex <em>Ref Market AIndex</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Market AIndex</em>' reference.
	 * @see #getRefMarketIndex()
	 * @generated
	 */
	void setRefMarketIndex(AIndex value);

	/**
	 * Returns the value of the '<em><b>Ref Market Multiplier</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Market Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Market Multiplier</em>' attribute.
	 * @see #setRefMarketMultiplier(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_RefMarketMultiplier()
	 * @model default="1" required="true"
	 * @generated
	 */
	double getRefMarketMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getRefMarketMultiplier <em>Ref Market Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Market Multiplier</em>' attribute.
	 * @see #getRefMarketMultiplier()
	 * @generated
	 */
	void setRefMarketMultiplier(double value);

	/**
	 * Returns the value of the '<em><b>Ref Market Constant</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref Market Constant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref Market Constant</em>' attribute.
	 * @see #setRefMarketConstant(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_RefMarketConstant()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getRefMarketConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getRefMarketConstant <em>Ref Market Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Market Constant</em>' attribute.
	 * @see #getRefMarketConstant()
	 * @generated
	 */
	void setRefMarketConstant(double value);

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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_Share()
	 * @model
	 * @generated
	 */
	double getShare();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getShare <em>Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Share</em>' attribute.
	 * @see #getShare()
	 * @generated
	 */
	void setShare(double value);

	/**
	 * Returns the value of the '<em><b>Margin</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Margin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Margin</em>' attribute.
	 * @see #setMargin(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_Margin()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getMargin();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getMargin <em>Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Margin</em>' attribute.
	 * @see #getMargin()
	 * @generated
	 */
	void setMargin(double value);

	/**
	 * Returns the value of the '<em><b>Sales Multiplier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sales Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sales Multiplier</em>' attribute.
	 * @see #setSalesMultiplier(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getProfitSharePriceParameters_SalesMultiplier()
	 * @model required="true"
	 * @generated
	 */
	double getSalesMultiplier();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.ProfitSharePriceParameters#getSalesMultiplier <em>Sales Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sales Multiplier</em>' attribute.
	 * @see #getSalesMultiplier()
	 * @generated
	 */
	void setSalesMultiplier(double value);

} // end of  ProfitSharePriceParameters

// finish type fixing
