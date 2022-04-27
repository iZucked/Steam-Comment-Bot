/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;

import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CommodityCurveOverlayItemProvider extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommodityCurveOverlayItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addReferenceCurvePropertyDescriptor(object);
			addAlternativeCurvesPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Reference Curve feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferenceCurvePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CommodityCurveOverlay_referenceCurve_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CommodityCurveOverlay_referenceCurve_feature", "_UI_CommodityCurveOverlay_type"),
				 AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__REFERENCE_CURVE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Alternative Curves feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAlternativeCurvesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CommodityCurveOverlay_alternativeCurves_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CommodityCurveOverlay_alternativeCurves_feature", "_UI_CommodityCurveOverlay_type"),
				 AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns CommodityCurveOverlay.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CommodityCurveOverlay"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((CommodityCurveOverlay)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_CommodityCurveOverlay_type") :
			getString("_UI_CommodityCurveOverlay_type") + " " + label;
	}


	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(CommodityCurveOverlay.class)) {
			case AnalyticsPackage.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				 PricingFactory.eINSTANCE.createYearMonthPointContainer()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				 PricingFactory.eINSTANCE.createCommodityCurve()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				 PricingFactory.eINSTANCE.createCharterCurve()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				 PricingFactory.eINSTANCE.createBunkerFuelCurve()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.COMMODITY_CURVE_OVERLAY__ALTERNATIVE_CURVES,
				 PricingFactory.eINSTANCE.createCurrencyCurve()));
	}

}
