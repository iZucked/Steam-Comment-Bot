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
 * A component helper for Port instances
 *
 * @generated
 */
public class PortComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = 
		new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortComponentHelper() {
		this(Platform.getAdapterManager());
	}
	
	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortComponentHelper(IAdapterManager adapterManager) {
		superClassesHelpers.add((IComponentHelper) adapterManager.getAdapter(TypesPackage.Literals.APORT, IComponentHelper.class));
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IDetailComposite detailComposite) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite);
		add_capabilitiesEditor(detailComposite);
	}

	/**
	 * Create the editor for the capabilities feature on Port
	 *
	 * @generated
	 */
	protected void add_capabilitiesEditor(final IDetailComposite detailComposite) {
		ComponentHelperUtils.addDefaultEditor(detailComposite, PortPackage.Literals.PORT__CAPABILITIES);
	}
}