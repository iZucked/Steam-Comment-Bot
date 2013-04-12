/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import org.eclipse.emf.common.util.EList;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.PortGroup#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPortGroup()
 * @model
 * @generated
 */
public interface PortGroup extends APortSet {

	/**
	 * Returns the value of the '<em><b>Contents</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contents</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contents</em>' reference list.
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPortGroup_Contents()
	 * @model
	 * @generated
	 */
	EList<APortSet> getContents();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (marked.contains(this)) return org.eclipse.emf.common.util.ECollections.emptyEList();\n\tfinal org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.APort> result = new org.eclipse.emf.common.util.UniqueEList<com.mmxlabs.models.lng.types.APort>();\n\tmarked.add(this);\n\t\t\n\tfor (final com.mmxlabs.models.lng.types.APortSet set : getContents()) {\n\t\tresult.addAll(set.collect(marked));\n\t}\n\t\nreturn result;'"
	 * @generated
	 */
	EList<APort> collect(EList<APortSet> marked);
} // PortGroup
