/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Annotated Object</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.AnnotatedObject#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.ScenarioPackage#getAnnotatedObject()
 * @model
 * @generated
 */
public interface AnnotatedObject extends EObject {
	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see scenario.ScenarioPackage#getAnnotatedObject_Notes()
	 * @model default="" required="true"
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link scenario.AnnotatedObject#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

} // AnnotatedObject
