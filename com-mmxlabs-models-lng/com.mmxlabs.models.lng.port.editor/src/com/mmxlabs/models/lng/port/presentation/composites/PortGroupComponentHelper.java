/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.port.PortPackage;

import java.util.ArrayList;
import java.util.List;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IDetailComposite;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for PortGroup instances
 *
 * @generated
 */
public class PortGroupComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = 
		new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortGroupComponentHelper() {
		this(Platform.getAdapterManager());
	}
	
	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortGroupComponentHelper(IAdapterManager adapterManager) {
		superClassesHelpers.add((IComponentHelper) adapterManager.getAdapter(TypesPackage.Literals.APORT_SET, IComponentHelper.class));
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IDetailComposite detailComposite) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite);
	}
}