/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.port.Port;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repositioning Fee Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm#getOriginPort <em>Origin Port</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRepositioningFeeTerm()
 * @model
 * @generated
 */
public interface RepositioningFeeTerm extends EObject {
	/**
	 * Returns the value of the '<em><b>Origin Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Port</em>' reference.
	 * @see #setOriginPort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRepositioningFeeTerm_OriginPort()
	 * @model
	 * @generated
	 */
	Port getOriginPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm#getOriginPort <em>Origin Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Port</em>' reference.
	 * @see #getOriginPort()
	 * @generated
	 */
	void setOriginPort(Port value);

} // RepositioningFeeTerm
