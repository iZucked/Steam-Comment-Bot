/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class ScheduledEventPropertySourceProvider implements IPropertySourceProvider {

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

	private static class SimpleLabelProvider extends ColumnLabelProvider {
		public SimpleLabelProvider() {

		}

		@Override
		public String getText(final Object element) {
			if ((element instanceof Integer) || (element instanceof Long)) {
				return String.format("%,d", element);
			} else if ((element instanceof Float) || (element instanceof Double)) {
				return String.format("%.1f", element);
			} else if (element instanceof Port) {
				return ((Port) element).getName();
			} else if (element instanceof Calendar) {
				final Calendar cal = (Calendar) element;
				final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
				df.setCalendar(cal);
				return df.format(cal.getTime()) + " (" + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT) + ")";
			}
			return super.getText(element);
		}
	}

	private class EventPropertySource implements IPropertySource {
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

				if (attribute.equals(EventsPackage.eINSTANCE.getScheduledEvent_StartTime()) || attribute.equals(EventsPackage.eINSTANCE.getScheduledEvent_EndTime())) {
					continue;
				}

				final PropertyDescriptor descriptor = new PropertyDescriptor(new CompiledEMFPath(true, attribute), unmangle(attribute.getName()));
				descriptor.setCategory(eClass.getName());
				descriptor.setLabelProvider(lp);

				list.add(descriptor);
			}

			// add any port references
			for (final EReference ref : eClass.getEAllReferences()) {
				if (ref.isContainment() || ref.isMany() || !(ref.getEType().equals(PortPackage.eINSTANCE.getPort()))) {
					continue;
				}
				final PropertyDescriptor descriptor = new PropertyDescriptor(new CompiledEMFPath(true, ref), unmangle(ref.getName()));
				descriptor.setCategory(eClass.getName());
				descriptor.setLabelProvider(lp);

				list.add(descriptor);
			}

			if (event instanceof SlotVisit) {
				PropertyDescriptor descriptor = new PropertyDescriptor(new CompiledEMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_Slot(), CargoPackage.eINSTANCE.getSlot_Id()), "Slot ID");
				descriptor.setCategory(eClass.getName());
				descriptor.setLabelProvider(lp);

				list.add(descriptor);

				if (((SlotVisit) event).getSlot() instanceof LoadSlot) {
					descriptor = new PropertyDescriptor(
							new CompiledEMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation(), SchedulePackage.eINSTANCE.getCargoAllocation__GetLoadVolume()), "Load Volume");
					descriptor.setCategory(eClass.getName());
					descriptor.setLabelProvider(lp);

					list.add(descriptor);
				} else {
					descriptor = new PropertyDescriptor(new CompiledEMFPath(true, EventsPackage.eINSTANCE.getSlotVisit_CargoAllocation(),
							SchedulePackage.eINSTANCE.getCargoAllocation_DischargeVolume()), "Discharge Volume");
					descriptor.setCategory(eClass.getName());
					descriptor.setLabelProvider(lp);

					list.add(descriptor);
				}
			}

			{
				PropertyDescriptor descriptor = new PropertyDescriptor(new CompiledEMFPath(true, EventsPackage.eINSTANCE.getScheduledEvent__GetEventDuration()), "Duration");
				descriptor.setCategory("Time");
				descriptor.setLabelProvider(new ColumnLabelProvider() {

					@Override
					public String getText(final Object element) {
						if (element instanceof Number) {
							final int x = ((Number) element).intValue();
							return String.format("%02d:%02d", x / 24, x % 24);
						}
						return super.getText(element);
					}
				});

				list.add(descriptor);

				if (event.getHireCost() > 0) {
					descriptor = new PropertyDescriptor(new CompiledEMFPath(true, EventsPackage.eINSTANCE.getScheduledEvent__GetHireCost()), "Hire Cost ($)");
					descriptor.setCategory(eClass.getName());
					descriptor.setLabelProvider(lp);

					list.add(descriptor);
				}

				descriptor = new PropertyDescriptor(new CompiledEMFPath(true, EventsPackage.eINSTANCE.getScheduledEvent__GetLocalStartTime()),

				"From Date");
				descriptor.setCategory("Time");
				descriptor.setLabelProvider(lp);

				list.add(descriptor);

				descriptor = new PropertyDescriptor(new CompiledEMFPath(true, EventsPackage.eINSTANCE.getScheduledEvent__GetLocalEndTime()),

				"To Date");
				descriptor.setCategory("Time");
				descriptor.setLabelProvider(lp);

				list.add(descriptor);
			}
			if (event instanceof FuelMixture) {
				final ColumnLabelProvider fqlp = new ColumnLabelProvider() {
					@Override
					public String getText(final Object element) {
						if (element instanceof FuelQuantity) {
							final FuelQuantity fq = (FuelQuantity) element;
							return String.format("%,d %s ($%,d)", fq.getQuantity(), fq.getFuelUnit().getName(), fq.getTotalPrice());
						}
						return super.getText(element);
					}
				};
				for (final FuelQuantity fc : ((FuelMixture) event).getFuelUsage()) {
					if (fc.getQuantity() == 0) {
						continue;
					}
					final PropertyDescriptor descriptor = new PropertyDescriptor(fc.getFuelType(), fc.getFuelType().getName());
					descriptor.setLabelProvider(fqlp);
					descriptor.setCategory("Fuel Usage");

					list.add(descriptor);
				}
			}

			descriptors = list.toArray(new IPropertyDescriptor[0]);

			return descriptors;
		}

		@Override
		public Object getPropertyValue(final Object id) {
			if (id instanceof EMFPath) {
				return (((EMFPath) id).get(event));
			} else if (id instanceof FuelType) {
				for (final FuelQuantity fq : ((FuelMixture) event).getFuelUsage()) {
					if (fq.getFuelType().equals(id)) {
						return fq;
					}
				}
				return null;
			} else {// properties for fuelmix
				return null;
			}
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

	}

	@Override
	public IPropertySource getPropertySource(final Object object) {
		if (object instanceof ScheduledEvent) {
			final ScheduledEvent event = (ScheduledEvent) object;
			return new EventPropertySource(event);
		}
		return null;
	}

}
