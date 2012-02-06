/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IDetailComposite;
import com.mmxlabs.models.lng.port.PortPackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for RouteLine instances
 *
 * @generated
 */
public class RouteLineComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = 
		new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public RouteLineComponentHelper() {
		this(Platform.getAdapterManager());
	}
	
	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public RouteLineComponentHelper(IAdapterManager adapterManager) {
		superClassesHelpers.add((IComponentHelper) adapterManager.getAdapter(MMXCorePackage.Literals.MMX_OBJECT, IComponentHelper.class));
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IDetailComposite detailComposite) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite);
		add_fromEditor(detailComposite);
		add_toEditor(detailComposite);
		add_distanceEditor(detailComposite);
	}

	/**
	 * Create the editor for the from feature on RouteLine
	 *
	 * @generated
	 */
	protected void add_fromEditor(final IDetailComposite detailComposite) {
		ComponentHelperUtils.addDefaultEditor(detailComposite, PortPackage.Literals.ROUTE_LINE__FROM);
	}

	/**
	 * Create the editor for the to feature on RouteLine
	 *
	 * @generated
	 */
	protected void add_toEditor(final IDetailComposite detailComposite) {
		ComponentHelperUtils.addDefaultEditor(detailComposite, PortPackage.Literals.ROUTE_LINE__TO);
	}

	/**
	 * Create the editor for the distance feature on RouteLine
	 *
	 * @generated
	 */
	protected void add_distanceEditor(final IDetailComposite detailComposite) {
		ComponentHelperUtils.addDefaultEditor(detailComposite, PortPackage.Literals.ROUTE_LINE__DISTANCE);
	}
}