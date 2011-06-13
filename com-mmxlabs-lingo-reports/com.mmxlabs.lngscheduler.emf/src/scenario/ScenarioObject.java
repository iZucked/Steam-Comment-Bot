/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see scenario.ScenarioPackage#getScenarioObject()
 * @model abstract="true"
 * @generated
 */
public interface ScenarioObject extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * This is a hack to get around the fact that EMF is not including EObject operations in getEAllOperations() for our model, probably because we are not extending the metamodel.
	 * <!-- end-model-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return eContainer();'"
	 * @generated
	 */
	EObject getContainer();

} // ScenarioObject
