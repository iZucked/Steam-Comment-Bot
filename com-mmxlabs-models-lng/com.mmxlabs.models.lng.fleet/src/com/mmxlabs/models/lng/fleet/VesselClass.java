

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getInaccessiblePorts <em>Inaccessible Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getCapacity <em>Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getSpotCharterDayRate <em>Spot Charter Day Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinHeel <em>Min Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getWarmingTime <em>Warming Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingTime <em>Cooling Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingVolume <em>Cooling Volume</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass()
 * @model
 * @generated
 */
public interface VesselClass extends AVesselClass {
	/**
	 * Returns the value of the '<em><b>Inaccessible Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_InaccessiblePorts()
	 * @model
	 * @generated
	 */
	EList<APortSet> getInaccessiblePorts();

	/**
	 * Returns the value of the '<em><b>Base Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel</em>' reference.
	 * @see #setBaseFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_BaseFuel()
	 * @model required="true"
	 * @generated
	 */
	BaseFuel getBaseFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getBaseFuel <em>Base Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel</em>' reference.
	 * @see #getBaseFuel()
	 * @generated
	 */
	void setBaseFuel(BaseFuel value);

	/**
	 * Returns the value of the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity</em>' attribute.
	 * @see #setCapacity(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_Capacity()
	 * @model required="true"
	 * @generated
	 */
	int getCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCapacity <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity</em>' attribute.
	 * @see #getCapacity()
	 * @generated
	 */
	void setCapacity(int value);

	/**
	 * Returns the value of the '<em><b>Fill Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fill Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fill Capacity</em>' attribute.
	 * @see #setFillCapacity(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_FillCapacity()
	 * @model required="true"
	 * @generated
	 */
	double getFillCapacity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getFillCapacity <em>Fill Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fill Capacity</em>' attribute.
	 * @see #getFillCapacity()
	 * @generated
	 */
	void setFillCapacity(double value);

	/**
	 * Returns the value of the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #setSpotCharterCount(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_SpotCharterCount()
	 * @model required="true"
	 * @generated
	 */
	int getSpotCharterCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getSpotCharterCount <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(int value);

	/**
	 * Returns the value of the '<em><b>Spot Charter Day Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Day Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Day Rate</em>' attribute.
	 * @see #setSpotCharterDayRate(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_SpotCharterDayRate()
	 * @model required="true"
	 * @generated
	 */
	int getSpotCharterDayRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getSpotCharterDayRate <em>Spot Charter Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Day Rate</em>' attribute.
	 * @see #getSpotCharterDayRate()
	 * @generated
	 */
	void setSpotCharterDayRate(int value);

	/**
	 * Returns the value of the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Attributes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Attributes</em>' containment reference.
	 * @see #setLadenAttributes(VesselStateAttributes)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_LadenAttributes()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselStateAttributes getLadenAttributes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Attributes</em>' containment reference.
	 * @see #getLadenAttributes()
	 * @generated
	 */
	void setLadenAttributes(VesselStateAttributes value);

	/**
	 * Returns the value of the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Attributes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Attributes</em>' containment reference.
	 * @see #setBallastAttributes(VesselStateAttributes)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_BallastAttributes()
	 * @model containment="true" required="true"
	 * @generated
	 */
	VesselStateAttributes getBallastAttributes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Attributes</em>' containment reference.
	 * @see #getBallastAttributes()
	 * @generated
	 */
	void setBallastAttributes(VesselStateAttributes value);

	/**
	 * Returns the value of the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Speed</em>' attribute.
	 * @see #setMinSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MinSpeed()
	 * @model required="true"
	 * @generated
	 */
	double getMinSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Speed</em>' attribute.
	 * @see #getMinSpeed()
	 * @generated
	 */
	void setMinSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Speed</em>' attribute.
	 * @see #setMaxSpeed(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MaxSpeed()
	 * @model required="true"
	 * @generated
	 */
	double getMaxSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Speed</em>' attribute.
	 * @see #getMaxSpeed()
	 * @generated
	 */
	void setMaxSpeed(double value);

	/**
	 * Returns the value of the '<em><b>Min Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Heel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Heel</em>' attribute.
	 * @see #setMinHeel(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_MinHeel()
	 * @model required="true"
	 * @generated
	 */
	int getMinHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getMinHeel <em>Min Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Heel</em>' attribute.
	 * @see #getMinHeel()
	 * @generated
	 */
	void setMinHeel(int value);

	/**
	 * Returns the value of the '<em><b>Warming Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Warming Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Warming Time</em>' attribute.
	 * @see #setWarmingTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_WarmingTime()
	 * @model required="true"
	 * @generated
	 */
	int getWarmingTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getWarmingTime <em>Warming Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Warming Time</em>' attribute.
	 * @see #getWarmingTime()
	 * @generated
	 */
	void setWarmingTime(int value);

	/**
	 * Returns the value of the '<em><b>Cooling Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling Time</em>' attribute.
	 * @see #setCoolingTime(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_CoolingTime()
	 * @model required="true"
	 * @generated
	 */
	int getCoolingTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingTime <em>Cooling Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling Time</em>' attribute.
	 * @see #getCoolingTime()
	 * @generated
	 */
	void setCoolingTime(int value);

	/**
	 * Returns the value of the '<em><b>Cooling Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling Volume</em>' attribute.
	 * @see #setCoolingVolume(int)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getVesselClass_CoolingVolume()
	 * @model required="true"
	 * @generated
	 */
	int getCoolingVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.VesselClass#getCoolingVolume <em>Cooling Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling Volume</em>' attribute.
	 * @see #getCoolingVolume()
	 * @generated
	 */
	void setCoolingVolume(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (marked.contains(this)) return org.eclipse.emf.common.util.ECollections.emptyEList();\n\tfinal org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel> result = new org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.AVessel>();\n\tmarked.add(this);\n\t\n\tfinal FleetModel myModel = (FleetModel) eContainer();\n\tfor (final Vessel v : myModel.getVessels()) {\n\t\tif (v.getVesselClass() == this) {\n\t\t\tresult.add(v);\n\t\t}\n\t}\n\nreturn result;'"
	 * @generated
	 */
	EList<AVessel> collect(EList<AVesselSet> marked);

} // end of  VesselClass

// finish type fixing
