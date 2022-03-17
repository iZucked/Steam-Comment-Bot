/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Group Canal Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters#getVesselGroup <em>Vessel Group</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselGroupCanalParameters()
 * @model
 * @generated
 */
public interface VesselGroupCanalParameters extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Vessel Group</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}<code>&lt;com.mmxlabs.models.lng.fleet.Vessel&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Group</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselGroupCanalParameters_VesselGroup()
	 * @model
	 * @generated
	 */
	EList<AVesselSet<Vessel>> getVesselGroup();

} // VesselGroupCanalParameters
