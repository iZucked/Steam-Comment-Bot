/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import scenario.AnnotatedObject;
import scenario.NamedObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Vessel</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getStartRequirement <em>Start Requirement</em>}</li>
 *   <li>{@link scenario.fleet.Vessel#getEndRequirement <em>End Requirement</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVessel()
 * @model
 * @generated
 */
public interface Vessel extends NamedObject, AnnotatedObject {
	/**
	 * Returns the value of the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' reference.
	 * @see #setClass(VesselClass)
	 * @see scenario.fleet.FleetPackage#getVessel_Class()
	 * @model
	 * @generated
	 */
	VesselClass getClass_();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' reference.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Start Requirement</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Requirement</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Requirement</em>' containment reference.
	 * @see #setStartRequirement(PortTimeAndHeel)
	 * @see scenario.fleet.FleetPackage#getVessel_StartRequirement()
	 * @model containment="true" resolveProxies="true" required="true"
	 * @generated
	 */
	PortTimeAndHeel getStartRequirement();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getStartRequirement <em>Start Requirement</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Requirement</em>' containment reference.
	 * @see #getStartRequirement()
	 * @generated
	 */
	void setStartRequirement(PortTimeAndHeel value);

	/**
	 * Returns the value of the '<em><b>End Requirement</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Requirement</em>' containment reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Requirement</em>' containment reference.
	 * @see #setEndRequirement(PortAndTime)
	 * @see scenario.fleet.FleetPackage#getVessel_EndRequirement()
	 * @model containment="true" resolveProxies="true" required="true"
	 * @generated
	 */
	PortAndTime getEndRequirement();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getEndRequirement <em>End Requirement</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Requirement</em>' containment reference.
	 * @see #getEndRequirement()
	 * @generated
	 */
	void setEndRequirement(PortAndTime value);

	/**
	 * Returns the value of the '<em><b>Time Chartered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Chartered</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Chartered</em>' attribute.
	 * @see #setTimeChartered(boolean)
	 * @see scenario.fleet.FleetPackage#getVessel_TimeChartered()
	 * @model required="true"
	 * @generated
	 */
	boolean isTimeChartered();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#isTimeChartered <em>Time Chartered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Chartered</em>' attribute.
	 * @see #isTimeChartered()
	 * @generated
	 */
	void setTimeChartered(boolean value);

	/**
	 * Returns the value of the '<em><b>Daily Charter Out Price</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The cost per day of spot chartering vessels
	 * of this class, expressed in dollars <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Daily Charter Out Price</em>' attribute.
	 * @see #isSetDailyCharterOutPrice()
	 * @see #unsetDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @see scenario.fleet.FleetPackage#getVessel_DailyCharterOutPrice()
	 * @model unsettable="true"
	 * @generated
	 */
	int getDailyCharterOutPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.Vessel#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Charter Out Price</em>' attribute.
	 * @see #isSetDailyCharterOutPrice()
	 * @see #unsetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 */
	void setDailyCharterOutPrice(int value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.Vessel#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @generated
	 */
	void unsetDailyCharterOutPrice();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.Vessel#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Daily Charter Out Price</em>' attribute is set.
	 * @see #unsetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @generated
	 */
	boolean isSetDailyCharterOutPrice();

} // Vessel
