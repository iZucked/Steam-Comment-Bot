/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.types.AVesselSet;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Usage Distribution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.VesselUsageDistribution#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.VesselUsageDistribution#getVessels <em>Vessels</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getVesselUsageDistribution()
 * @model
 * @generated
 */
public interface VesselUsageDistribution extends EObject {
	/**
	 * Returns the value of the '<em><b>Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargoes</em>' attribute.
	 * @see #setCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getVesselUsageDistribution_Cargoes()
	 * @model
	 * @generated
	 */
	int getCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.VesselUsageDistribution#getCargoes <em>Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargoes</em>' attribute.
	 * @see #getCargoes()
	 * @generated
	 */
	void setCargoes(int value);

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}<code>&lt;com.mmxlabs.models.lng.fleet.Vessel&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getVesselUsageDistribution_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getVessels();

} // VesselUsageDistribution
