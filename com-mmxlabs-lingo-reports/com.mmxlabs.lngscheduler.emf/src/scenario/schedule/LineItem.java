/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule;

import scenario.NamedObject;

import scenario.contract.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Line Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.LineItem#getValue <em>Value</em>}</li>
 *   <li>{@link scenario.schedule.LineItem#getParty <em>Party</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getLineItem()
 * @model
 * @generated
 */
public interface LineItem extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(int)
	 * @see scenario.schedule.SchedulePackage#getLineItem_Value()
	 * @model required="true"
	 * @generated
	 */
	int getValue();

	/**
	 * Sets the value of the '{@link scenario.schedule.LineItem#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(int value);

	/**
	 * Returns the value of the '<em><b>Party</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Party</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Party</em>' reference.
	 * @see #setParty(Entity)
	 * @see scenario.schedule.SchedulePackage#getLineItem_Party()
	 * @model required="true"
	 * @generated
	 */
	Entity getParty();

	/**
	 * Sets the value of the '{@link scenario.schedule.LineItem#getParty <em>Party</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Party</em>' reference.
	 * @see #getParty()
	 * @generated
	 */
	void setParty(Entity value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getValue() < 0;'"
	 * @generated
	 */
	boolean isCost();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return !isCost();'"
	 * @generated
	 */
	boolean isRevenue();

} // LineItem
