/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import scenario.ScenarioObject;
import scenario.contract.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Booked Revenue</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.BookedRevenue#getEntity <em>Entity</em>}</li>
 *   <li>{@link scenario.schedule.BookedRevenue#getDate <em>Date</em>}</li>
 *   <li>{@link scenario.schedule.BookedRevenue#getLineItems <em>Line Items</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getBookedRevenue()
 * @model
 * @generated
 */
public interface BookedRevenue extends ScenarioObject {
	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(Entity)
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_Entity()
	 * @model required="true"
	 * @generated
	 */
	Entity getEntity();

	/**
	 * Sets the value of the '{@link scenario.schedule.BookedRevenue#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(Entity value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_Date()
	 * @model required="true"
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link scenario.schedule.BookedRevenue#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * Returns the value of the '<em><b>Line Items</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.LineItem}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line Items</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Items</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getBookedRevenue_LineItems()
	 * @model containment="true"
	 * @generated
	 */
	EList<LineItem> getLineItems();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the total untaxed revenue (or if it's a net loss, a negative revenue) which the associated cargo
	 * has produced for the associated entity.
	 * 
	 * This is just the sum of the revenue associated with each line item.
	 * <!-- end-model-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='int untaxedRevenue = 0;\n\nfor (final LineItem item : getLineItems()) {\n\tuntaxedRevenue += item.getValue();\n}\n\nreturn untaxedRevenue;'"
	 * @generated
	 */
	int getUntaxedValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the total revenue after tax associated with this line item.
	 * <!-- end-model-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final int untaxedRevenue = getUntaxedValue();\n\nif (untaxedRevenue <= 0) {\n\treturn untaxedRevenue;\n} else {\n\treturn (int) (untaxedRevenue * (1.0 - getEntity().getTaxRate()));\n}'"
	 * @generated
	 */
	int getTaxedValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the total revenue before tax associated with this line item.
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='int untaxedRevenue = 0;\n\nfor (final LineItem item : getLineItems()) {\n\tif (item.getValue() > 0)\n\t\tuntaxedRevenue += item.getValue();\n}\n\nreturn untaxedRevenue;'"
	 * @generated
	 */
	int getUntaxedRevenues();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the total costs before tax associated with this revenue
	 * <!-- end-model-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='int untaxedRevenue = 0;\n\nfor (final LineItem item : getLineItems()) {\n\tif (item.getValue() < 0)\n\tuntaxedRevenue += item.getValue();\n}\n\nreturn untaxedRevenue;'"
	 * @generated
	 */
	int getUntaxedCosts();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Returns the total amount of tax paid on this revenue
	 * <!-- end-model-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getUntaxedValue() - getTaxedValue();'"
	 * @generated
	 */
	int getTaxCost();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

} // BookedRevenue
