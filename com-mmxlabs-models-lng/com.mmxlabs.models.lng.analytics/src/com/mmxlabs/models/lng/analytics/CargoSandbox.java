

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics;
import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Sandbox</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CargoSandbox#getCargoes <em>Cargoes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoSandbox()
 * @model
 * @generated
 */
public interface CargoSandbox extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Cargoes</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ProvisionalCargo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargoes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargoes</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCargoSandbox_Cargoes()
	 * @model containment="true"
	 * @generated
	 */
	EList<ProvisionalCargo> getCargoes();

} // end of  CargoSandbox

// finish type fixing
