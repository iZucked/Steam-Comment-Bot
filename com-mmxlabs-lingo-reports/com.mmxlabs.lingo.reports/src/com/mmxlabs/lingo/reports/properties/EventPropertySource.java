/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.google.common.collect.Lists;
import com.mmxlabs.lingo.reports.properties.ScheduledEventPropertySourceProvider.SimpleLabelProvider;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.models.util.emfpath.EMFPathPropertyDescriptor;

/**
 */
public class EventPropertySource implements IPropertySource {

	private final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
	private final Event event;
	private IPropertyDescriptor[] descriptors = null;

	public EventPropertySource(final Event event) {
		this.event = event;
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public synchronized IPropertyDescriptor[] getPropertyDescriptors() {
		// create descriptors for whatever normal fields the event has
		// then create descriptors for fuel mix, because fuel mix is special

		if (descriptors != null) {
			return descriptors;
		}
		final EClass eClass = event.eClass();

		final ArrayList<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
		final SimpleLabelProvider lp = new SimpleLabelProvider();
		for (final EAttribute attribute : eClass.getEAllAttributes()) {
			// display attributes

			if (attribute.equals(SchedulePackage.eINSTANCE.getEvent_Start()) || attribute.equals(SchedulePackage.eINSTANCE.getEvent_End())) {
				continue;
			}

			final PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, attribute), unmangle(attribute.getName()));
			descriptor.setCategory(eClass.getName());
			descriptor.setLabelProvider(lp);

			list.add(descriptor);
		}

		// add any port references
		for (final EReference ref : eClass.getEAllReferences()) {
			if (ref.isContainment() || ref.isMany() || !(ref.getEType().equals(PortPackage.eINSTANCE.getPort()))) {
				continue;
			}
			final PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, ref), unmangle(ref.getName()));
			descriptor.setCategory(eClass.getName());
			descriptor.setLabelProvider(lp);

			list.add(descriptor);
		}

		if (event instanceof SlotVisit) {
			final PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, SchedulePackage.eINSTANCE.getSlotVisit_SlotAllocation(), SchedulePackage.eINSTANCE.getSlotAllocation_Slot(),
					MMXCorePackage.eINSTANCE.getNamedObject_Name()), "Slot ID");
			descriptor.setCategory(eClass.getName());
			descriptor.setLabelProvider(lp);

			list.add(descriptor);

			addPropertyDescriptor(list, "Slot ID", MMXCorePackage.eINSTANCE.getNamedObject_Name(),
					Lists.<ETypedElement> newArrayList(SchedulePackage.eINSTANCE.getSlotVisit_SlotAllocation(), SchedulePackage.eINSTANCE.getSlotAllocation_Slot()));
			// if (((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
			// descriptor = new PropertyDescriptor(
			// new EMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation(), SchedulePackage.eINSTANCE.getCargoAllocation__GetLoadVolume()), "Load Volume");
			// descriptor.setCategory(eClass.getName());
			// descriptor.setLabelProvider(lp);
			//
			// list.add(descriptor);
			// } else {
			// descriptor = new PropertyDescriptor(new EMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation(),
			// SchedulePackage.eINSTANCE.getCargoAllocation_DischargeVolume()), "Discharge Volume");
			// descriptor.setCategory(eClass.getName());
			// descriptor.setLabelProvider(lp);
			//
			// list.add(descriptor);
			// }
		}

		if (event instanceof VesselEventVisit) {

			// {

			// addPropertyDescriptor(list, null, FleetPackage.eINSTANCE.getCharterOutEvent_HireRate(), Collections.<Object> singletonList(SchedulePackage.eINSTANCE.getVesselEventVisit_VesselEvent()));
			// addPropertyDescriptor(list, null, FleetPackage.eINSTANCE.getCharterOutEvent_RepositioningFee(),
			// Collections.<Object> singletonList(SchedulePackage.eINSTANCE.getVesselEventVisit_VesselEvent()));
			// }
			//
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
			if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
				{
					final PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, SchedulePackage.eINSTANCE.getVesselEventVisit_VesselEvent(),
							CargoPackage.eINSTANCE.getCharterOutEvent_HireRate()), "Daily Hire Rate");
					descriptor.setCategory(eClass.getName());
					descriptor.setLabelProvider(lp);

					list.add(descriptor);
				}
				{
					final PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, SchedulePackage.eINSTANCE.getVesselEventVisit_VesselEvent(),
							CargoPackage.eINSTANCE.getCharterOutEvent_RepositioningFee()), "Repositioning Fee");
					descriptor.setCategory(eClass.getName());
					descriptor.setLabelProvider(lp);

					list.add(descriptor);
				}
			}
//			if (((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
//				PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation(),
//						SchedulePackage.eINSTANCE.getCargoAllocation__GetLoadVolume()), "Load Volume");
//				descriptor.setCategory(eClass.getName());
//				descriptor.setLabelProvider(lp);
//
//				list.add(descriptor);
//			} else {
//				PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation(),
//						SchedulePackage.eINSTANCE.getCargoAllocation_DischargeVolume()), "Discharge Volume");
//				descriptor.setCategory(eClass.getName());
//				descriptor.setLabelProvider(lp);
//
//				list.add(descriptor);
//			}
		}

		{
			PropertyDescriptor descriptor = new PropertyDescriptor(new EMFPath(true, SchedulePackage.eINSTANCE.getEvent__GetDuration()), "Duration");
			descriptor.setCategory("Time");
			descriptor.setLabelProvider(new ColumnLabelProvider() {

				@Override
				public String getText(final Object element) {
					if (element instanceof Number) {
						final int x = ((Number) element).intValue();
						return Formatters.formatAsDays(x);
					}
					return super.getText(element);
				}
			});

			list.add(descriptor);
			//
			// if (event.getHireCost() > 0) {
			// descriptor = new PropertyDescriptor(new EMFPath(true, EventsPackage.eINSTANCE.getScheduledEvent__GetHireCost()), "Hire Cost ($)");
			// descriptor.setCategory(eClass.getName());
			// descriptor.setLabelProvider(lp);
			//
			// list.add(descriptor);
			// }

			descriptor = new PropertyDescriptor(new EMFPath(true, SchedulePackage.eINSTANCE.getEvent_Start()),

			"From Date");
			descriptor.setCategory("Time");
			descriptor.setLabelProvider(lp);

			list.add(descriptor);

			// addPropertyDescriptor(list, "From Time", SchedulePackage.eINSTANCE.getEvent__GetLocalStart(), Collections.<Object> emptyList());
			// addPropertyDescriptor(list, "To Time", SchedulePackage.eINSTANCE.getEvent__GetLocalEnd(), Collections.<Object> emptyList());

			descriptor = new PropertyDescriptor(new EMFPath(true, SchedulePackage.eINSTANCE.getEvent_End()),

			"To Date");
			descriptor.setCategory("Time");
			descriptor.setLabelProvider(lp);

			list.add(descriptor);
		}
		if (event instanceof FuelUsage) {
			final ColumnLabelProvider fqlp = new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					if (element instanceof FuelQuantity) {
						final FuelQuantity fq = (FuelQuantity) element;
						return String.format("%,d %s ($%,d)", fq.getAmounts().get(0).getQuantity(), fq.getAmounts().get(0).getUnit(), fq.getCost());
					}
					return super.getText(element);
				}
			};
			for (final FuelQuantity fc : ((FuelUsage) event).getFuels()) {
				if (fc.getCost() == 0) {
					continue;
				}
				final PropertyDescriptor descriptor = new PropertyDescriptor(fc.getFuel(), fc.getFuel().getName());
				descriptor.setLabelProvider(fqlp);
				descriptor.setCategory("Fuel Usage");

				list.add(descriptor);
			}
		}

		// Find the path to a ProfitAndLossContainer
		final List<ETypedElement> featureList = new ArrayList<>();
		ProfitAndLossContainer container = null;
		if (event instanceof ProfitAndLossContainer) {
			container = (ProfitAndLossContainer) event;
		} else if (event instanceof SlotAllocation) {
			container = (((SlotAllocation) event).getCargoAllocation());
			featureList.add(SchedulePackage.eINSTANCE.getSlotAllocation_CargoAllocation());
		} else if (event instanceof SlotVisit) {
			container = (((SlotVisit) event).getSlotAllocation().getCargoAllocation());
			featureList.add(SchedulePackage.eINSTANCE.getSlotVisit_SlotAllocation());
			featureList.add(SchedulePackage.eINSTANCE.getSlotAllocation_CargoAllocation());
		}
		if (container != null) {
			// Add final step the the group object
			featureList.add(SchedulePackage.eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss());
			// Create the property!
			final EMFPathPropertyDescriptor desc = EMFPathPropertyDescriptor.create(event, adapterFactory, SchedulePackage.eINSTANCE.getGroupProfitAndLoss_ProfitAndLoss(), featureList);
			if (desc != null) {
				 list.add(desc);
			}
		}
		descriptors = list.toArray(new IPropertyDescriptor[0]);

		return descriptors;
	}

	@Override
	public Object getPropertyValue(final Object id) {
		if (id instanceof EMFPath) {
			return (((EMFPath) id).get(event));
		} else if (id instanceof Fuel) {
			for (final FuelQuantity fq : ((FuelUsage) event).getFuels()) {
				if (fq.getFuel().equals(id)) {
					return fq;
				}
			}
			return null;
		} else {// properties for fuelmix
			// return delegateSource.getPropertyValue(id);
		}
		return null;
	}

	@Override
	public boolean isPropertySet(final Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(final Object id) {

	}

	@Override
	public void setPropertyValue(final Object id, final Object value) {

	}

	private String unmangle(final String name) {
		final StringBuilder sb = new StringBuilder();
		boolean lastWasLower = true;
		boolean firstChar = true;
		for (final char c : name.toCharArray()) {
			if (firstChar) {
				sb.append(Character.toUpperCase(c));
				firstChar = false;
			} else {
				if (lastWasLower && Character.isUpperCase(c)) {
					sb.append(" ");
				}
				lastWasLower = Character.isLowerCase(c);
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private void addPropertyDescriptor(List<IPropertyDescriptor> list, String displayName, final EStructuralFeature feature, final List<ETypedElement> featureList) {
		// Create the property!
		final EMFPathPropertyDescriptor desc = EMFPathPropertyDescriptor.create(event, adapterFactory, feature, featureList);
		if (desc != null) {
			if (displayName != null) {
				desc.setDisplayName(displayName);
			}
			list.add(desc);
		}
	}

}