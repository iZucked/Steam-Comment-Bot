/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.lng.port.PortPackage;

import java.util.ArrayList;
import java.util.List;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IDetailComposite;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for PortModel instances
 *
 * @generated
 */
public class PortModelComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = 
		new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortModelComponentHelper() {
		this(Platform.getAdapterManager());
	}
	
	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortModelComponentHelper(IAdapterManager adapterManager) {
		superClassesHelpers.add((IComponentHelper) adapterManager.getAdapter(MMXCorePackage.Literals.UUID_OBJECT, IComponentHelper.class));
		superClassesHelpers.add((IComponentHelper) adapterManager.getAdapter(MMXCorePackage.Literals.NAMED_OBJECT, IComponentHelper.class));
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IDetailComposite detailComposite) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite);
		add_portsEditor(detailComposite);
		add_routesEditor(detailComposite);
	}

	/**
	 * Create the editor for the ports feature on PortModel
	 *
	 * @generated
	 */
	protected void add_portsEditor(final IDetailComposite detailComposite) {
		ComponentHelperUtils.addDefaultEditor(detailComposite, PortPackage.Literals.PORT_MODEL__PORTS);
	}

	/**
	 * Create the editor for the routes feature on PortModel
	 *
	 * @generated
	 */
	protected void add_routesEditor(final IDetailComposite detailComposite) {
		ComponentHelperUtils.addDefaultEditor(detailComposite, PortPackage.Literals.PORT_MODEL__ROUTES);
	}
}