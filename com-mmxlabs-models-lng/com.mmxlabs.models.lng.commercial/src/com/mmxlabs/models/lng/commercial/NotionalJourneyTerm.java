/**
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notional Journey Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getFuelPriceExpression <em>Fuel Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getHirePriceExpression <em>Hire Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanal <em>Include Canal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanalTime <em>Include Canal Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm()
 * @model abstract="true"
 * @generated
 */
public interface NotionalJourneyTerm extends EObject {
	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #setSpeed(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm_Speed()
	 * @model default="0" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Fuel Price Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Price Expression</em>' attribute.
	 * @see #setFuelPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm_FuelPriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getFuelPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getFuelPriceExpression <em>Fuel Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Price Expression</em>' attribute.
	 * @see #getFuelPriceExpression()
	 * @generated
	 */
	void setFuelPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Hire Price Expression</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Price Expression</em>' attribute.
	 * @see #setHirePriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm_HirePriceExpression()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 * @generated
	 */
	String getHirePriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getHirePriceExpression <em>Hire Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Price Expression</em>' attribute.
	 * @see #getHirePriceExpression()
	 * @generated
	 */
	void setHirePriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Include Canal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Canal</em>' attribute.
	 * @see #setIncludeCanal(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm_IncludeCanal()
	 * @model
	 * @generated
	 */
	boolean isIncludeCanal();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanal <em>Include Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Canal</em>' attribute.
	 * @see #isIncludeCanal()
	 * @generated
	 */
	void setIncludeCanal(boolean value);

	/**
	 * Returns the value of the '<em><b>Include Canal Time</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Canal Time</em>' attribute.
	 * @see #setIncludeCanalTime(boolean)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm_IncludeCanalTime()
	 * @model default="true"
	 * @generated
	 */
	boolean isIncludeCanalTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#isIncludeCanalTime <em>Include Canal Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Canal Time</em>' attribute.
	 * @see #isIncludeCanalTime()
	 * @generated
	 */
	void setIncludeCanalTime(boolean value);

	/**
	 * Returns the value of the '<em><b>Lump Sum Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lump Sum Price Expression</em>' attribute.
	 * @see #setLumpSumPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalJourneyTerm_LumpSumPriceExpression()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getLumpSumPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalJourneyTerm#getLumpSumPriceExpression <em>Lump Sum Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lump Sum Price Expression</em>' attribute.
	 * @see #getLumpSumPriceExpression()
	 * @generated
	 */
	void setLumpSumPriceExpression(String value);

} // NotionalJourneyTerm
