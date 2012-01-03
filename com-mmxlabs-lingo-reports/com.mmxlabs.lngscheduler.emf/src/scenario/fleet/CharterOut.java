/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import scenario.port.Port;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}</li>
 *   <li>{@link scenario.fleet.CharterOut#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}</li>
 *   <li>{@link scenario.fleet.CharterOut#getRepositioningFee <em>Repositioning Fee</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getCharterOut()
 * @model
 * @generated
 */
public interface CharterOut extends VesselEvent, HeelOptions {

	/**
	 * Returns the value of the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Port</em>' reference.
	 * @see #isSetEndPort()
	 * @see #unsetEndPort()
	 * @see #setEndPort(Port)
	 * @see scenario.fleet.FleetPackage#getCharterOut_EndPort()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Port getEndPort();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Port</em>' reference.
	 * @see #isSetEndPort()
	 * @see #unsetEndPort()
	 * @see #getEndPort()
	 * @generated
	 */
	void setEndPort(Port value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndPort()
	 * @see #getEndPort()
	 * @see #setEndPort(Port)
	 * @generated
	 */
	void unsetEndPort();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.CharterOut#getEndPort <em>End Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Port</em>' reference is set.
	 * @see #unsetEndPort()
	 * @see #getEndPort()
	 * @see #setEndPort(Port)
	 * @generated
	 */
	boolean isSetEndPort();

	/**
	 * Returns the value of the '<em><b>Daily Charter Out Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Daily Charter Out Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Daily Charter Out Price</em>' attribute.
	 * @see #isSetDailyCharterOutPrice()
	 * @see #unsetDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @see scenario.fleet.FleetPackage#getCharterOut_DailyCharterOutPrice()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getDailyCharterOutPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Daily Charter Out Price</em>' attribute.
	 * @see #isSetDailyCharterOutPrice()
	 * @see #unsetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 */
	void setDailyCharterOutPrice(int value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.CharterOut#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @generated
	 */
	void unsetDailyCharterOutPrice();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.CharterOut#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Daily Charter Out Price</em>' attribute is set.
	 * @see #unsetDailyCharterOutPrice()
	 * @see #getDailyCharterOutPrice()
	 * @see #setDailyCharterOutPrice(int)
	 * @generated
	 */
	boolean isSetDailyCharterOutPrice();

	/**
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositioning Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #isSetRepositioningFee()
	 * @see #unsetRepositioningFee()
	 * @see #setRepositioningFee(int)
	 * @see scenario.fleet.FleetPackage#getCharterOut_RepositioningFee()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getRepositioningFee();

	/**
	 * Sets the value of the '{@link scenario.fleet.CharterOut#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #isSetRepositioningFee()
	 * @see #unsetRepositioningFee()
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(int value);

	/**
	 * Unsets the value of the '{@link scenario.fleet.CharterOut#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRepositioningFee()
	 * @see #getRepositioningFee()
	 * @see #setRepositioningFee(int)
	 * @generated
	 */
	void unsetRepositioningFee();

	/**
	 * Returns whether the value of the '{@link scenario.fleet.CharterOut#getRepositioningFee <em>Repositioning Fee</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Repositioning Fee</em>' attribute is set.
	 * @see #unsetRepositioningFee()
	 * @see #getRepositioningFee()
	 * @see #setRepositioningFee(int)
	 * @generated
	 */
	boolean isSetRepositioningFee();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetEndPort()) return getEndPort();\nelse return getStartPort();'"
	 * @generated
	 */
	Port getEffectiveEndPort();
} // CharterOut
