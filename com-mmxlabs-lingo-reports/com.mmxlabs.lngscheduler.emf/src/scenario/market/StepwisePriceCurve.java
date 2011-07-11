/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.market;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stepwise Price Curve</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.market.StepwisePriceCurve#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link scenario.market.StepwisePriceCurve#getPrices <em>Prices</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getStepwisePriceCurve()
 * @model
 * @generated
 */
public interface StepwisePriceCurve extends EObject {
	/**
	 * Returns the value of the '<em><b>Prices</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.market.StepwisePrice}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prices</em>' containment reference list.
	 * @see scenario.market.MarketPackage#getStepwisePriceCurve_Prices()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<StepwisePrice> getPrices();

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(float)
	 * @see scenario.market.MarketPackage#getStepwisePriceCurve_DefaultValue()
	 * @model
	 * @generated
	 */
	float getDefaultValue();

	/**
	 * Sets the value of the '{@link scenario.market.StepwisePriceCurve#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(float value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" dateRequired="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final EList<StepwisePrice> prices = getPrices();\n\njava.util.Collections.sort(prices,\nnew java.util.Comparator<StepwisePrice>() {\n\tpublic int compare(StepwisePrice a, StepwisePrice b) {\n\t\treturn a.getDate().compareTo(b.getDate());\n\t}\n});\n\nfloat previousPrice = getDefaultValue();\nfor (final StepwisePrice p : prices) {\n\tif (p.getDate().after(date)) {\n\t\treturn previousPrice;\n\t}\n\tpreviousPrice = p.getPriceFromDate();\n}\nreturn previousPrice;'"
	 * @generated
	 */
	float getValueAtDate(Date date);

} // StepwisePriceCurve
