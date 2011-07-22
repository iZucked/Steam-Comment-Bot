/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fuel Mixture</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.events.FuelMixture#getFuelUsage <em>Fuel Usage</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.events.EventsPackage#getFuelMixture()
 * @model
 * @generated
 */
public interface FuelMixture extends EObject {
	/**
	 * Returns the value of the '<em><b>Fuel Usage</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.events.FuelQuantity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Usage</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Usage</em>' containment reference list.
	 * @see scenario.schedule.events.EventsPackage#getFuelMixture_FuelUsage()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<FuelQuantity> getFuelUsage();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='long totalCost = 0;\n\nfor (final FuelQuantity quantity : getFuelUsage()) {\n\ttotalCost += quantity.getTotalPrice();\n}\n\nreturn totalCost;'"
	 * @generated
	 */
	long getTotalFuelCost();

} // FuelMixture
