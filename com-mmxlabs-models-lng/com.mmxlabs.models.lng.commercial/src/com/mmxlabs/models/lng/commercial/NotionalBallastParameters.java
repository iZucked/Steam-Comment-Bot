

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notional Ballast Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getRoutes <em>Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getBaseConsumption <em>Base Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getReturnPort <em>Return Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getVessels <em>Vessels</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters()
 * @model
 * @generated
 */
public interface NotionalBallastParameters extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Routes</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.Route}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Routes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Routes</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters_Routes()
	 * @model
	 * @generated
	 */
	EList<Route> getRoutes();

	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #isSetSpeed()
	 * @see #unsetSpeed()
	 * @see #setSpeed(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters_Speed()
	 * @model unsettable="true"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #isSetSpeed()
	 * @see #unsetSpeed()
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpeed()
	 * @see #getSpeed()
	 * @see #setSpeed(double)
	 * @generated
	 */
	void unsetSpeed();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getSpeed <em>Speed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Speed</em>' attribute is set.
	 * @see #unsetSpeed()
	 * @see #getSpeed()
	 * @see #setSpeed(double)
	 * @generated
	 */
	boolean isSetSpeed();

	/**
	 * Returns the value of the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nbo Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nbo Rate</em>' attribute.
	 * @see #isSetNboRate()
	 * @see #unsetNboRate()
	 * @see #setNboRate(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters_NboRate()
	 * @model unsettable="true"
	 * @generated
	 */
	int getNboRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nbo Rate</em>' attribute.
	 * @see #isSetNboRate()
	 * @see #unsetNboRate()
	 * @see #getNboRate()
	 * @generated
	 */
	void setNboRate(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNboRate()
	 * @see #getNboRate()
	 * @see #setNboRate(int)
	 * @generated
	 */
	void unsetNboRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getNboRate <em>Nbo Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Nbo Rate</em>' attribute is set.
	 * @see #unsetNboRate()
	 * @see #getNboRate()
	 * @see #setNboRate(int)
	 * @generated
	 */
	boolean isSetNboRate();

	/**
	 * Returns the value of the '<em><b>Base Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Consumption</em>' attribute.
	 * @see #isSetBaseConsumption()
	 * @see #unsetBaseConsumption()
	 * @see #setBaseConsumption(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters_BaseConsumption()
	 * @model unsettable="true"
	 * @generated
	 */
	int getBaseConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getBaseConsumption <em>Base Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Consumption</em>' attribute.
	 * @see #isSetBaseConsumption()
	 * @see #unsetBaseConsumption()
	 * @see #getBaseConsumption()
	 * @generated
	 */
	void setBaseConsumption(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getBaseConsumption <em>Base Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetBaseConsumption()
	 * @see #getBaseConsumption()
	 * @see #setBaseConsumption(int)
	 * @generated
	 */
	void unsetBaseConsumption();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getBaseConsumption <em>Base Consumption</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Base Consumption</em>' attribute is set.
	 * @see #unsetBaseConsumption()
	 * @see #getBaseConsumption()
	 * @see #setBaseConsumption(int)
	 * @generated
	 */
	boolean isSetBaseConsumption();

	/**
	 * Returns the value of the '<em><b>Return Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Port</em>' reference.
	 * @see #isSetReturnPort()
	 * @see #unsetReturnPort()
	 * @see #setReturnPort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters_ReturnPort()
	 * @model unsettable="true"
	 * @generated
	 */
	Port getReturnPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getReturnPort <em>Return Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Port</em>' reference.
	 * @see #isSetReturnPort()
	 * @see #unsetReturnPort()
	 * @see #getReturnPort()
	 * @generated
	 */
	void setReturnPort(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getReturnPort <em>Return Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetReturnPort()
	 * @see #getReturnPort()
	 * @see #setReturnPort(Port)
	 * @generated
	 */
	void unsetReturnPort();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters#getReturnPort <em>Return Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Return Port</em>' reference is set.
	 * @see #unsetReturnPort()
	 * @see #getReturnPort()
	 * @see #setReturnPort(Port)
	 * @generated
	 */
	boolean isSetReturnPort();

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNotionalBallastParameters_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getVessels();

} // end of  NotionalBallastParameters

// finish type fixing
