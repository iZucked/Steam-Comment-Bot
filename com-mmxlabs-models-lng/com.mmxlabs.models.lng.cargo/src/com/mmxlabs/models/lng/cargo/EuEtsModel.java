/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.PortGroup;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Eu Ets Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EuEtsModel#getEuaPriceExpression <em>Eua Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EuEtsModel#getEuPortGroup <em>Eu Port Group</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EuEtsModel#getEmissionsCovered <em>Emissions Covered</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEuEtsModel()
 * @model
 * @generated
 */
public interface EuEtsModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Eua Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Eua Price Expression</em>' attribute.
	 * @see #setEuaPriceExpression(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEuEtsModel_EuaPriceExpression()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getEuaPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EuEtsModel#getEuaPriceExpression <em>Eua Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Eua Price Expression</em>' attribute.
	 * @see #getEuaPriceExpression()
	 * @generated
	 */
	void setEuaPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Emissions Covered</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.EmissionsCoveredTable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Emissions Covered</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEuEtsModel_EmissionsCovered()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<EmissionsCoveredTable> getEmissionsCovered();

	/**
	 * Returns the value of the '<em><b>Eu Port Group</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Eu Port Group</em>' reference.
	 * @see #setEuPortGroup(PortGroup)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEuEtsModel_EuPortGroup()
	 * @model
	 * @generated
	 */
	PortGroup getEuPortGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EuEtsModel#getEuPortGroup <em>Eu Port Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Eu Port Group</em>' reference.
	 * @see #getEuPortGroup()
	 * @generated
	 */
	void setEuPortGroup(PortGroup value);

} // EuEtsModel
