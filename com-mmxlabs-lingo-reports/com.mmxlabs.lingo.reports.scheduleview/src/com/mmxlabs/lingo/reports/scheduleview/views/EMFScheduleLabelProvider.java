/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import static com.mmxlabs.lingo.reports.scheduleview.views.SchedulerViewConstants.Highlight_;
import static com.mmxlabs.lingo.reports.scheduleview.views.SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME;
import static com.mmxlabs.lingo.reports.scheduleview.views.SchedulerViewConstants.Show_Canals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartColourProvider;
import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
import com.mmxlabs.lingo.reports.scheduleview.internal.Activator;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.schedule.CanalBookingEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterAvailableFromEvent;
import com.mmxlabs.models.lng.schedule.CharterAvailableToEvent;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.InventoryChangeEvent;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * @author hinton
 * 
 */
public class EMFScheduleLabelProvider extends BaseLabelProvider implements IGanttChartToolTipProvider, IGanttChartColourProvider {

	private final Map<String, IScheduleViewColourScheme> colourSchemesById = new HashMap<>();
	private final List<IScheduleViewColourScheme> colourSchemes = new ArrayList<>();

	private final List<IScheduleViewColourScheme> highlighters = new ArrayList<>();
	private final Map<String, IScheduleViewColourScheme> highlightersById = new HashMap<>();

	private IScheduleViewColourScheme currentScheme = null;

	private final List<IScheduleViewColourScheme> currentHighlighters = new ArrayList<>();

	private final GanttChartViewer viewer;

	private final IMemento memento;

	private boolean showCanals = false;
	private final SelectedScenariosService selectedScenariosService;

	private final VesselAssignmentFormatter vesselFormatter = new VesselAssignmentFormatter();
	Image pinImage = null;

	public EMFScheduleLabelProvider(final GanttChartViewer viewer, final IMemento memento, @NonNull final SelectedScenariosService selectedScenariosService) {
		this.viewer = viewer;
		this.memento = memento;
		this.selectedScenariosService = selectedScenariosService;
		this.showCanals = memento.getBoolean(Show_Canals);

		final ImageDescriptor imageDescriptor = Activator.getImageDescriptor("icons/PinnedRow.gif");
		pinImage = imageDescriptor.createImage();
	}

	@Override
	public void dispose() {
		if (pinImage != null) {
			pinImage.dispose();
			pinImage = null;

		}
		super.dispose();
	}

	@Override
	public Image getImage(final Object element) {
		if (element instanceof Sequence) {
			final Sequence sequence = (Sequence) element;
			@Nullable
			final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
			if (currentSelectedDataProvider != null) {
				if (currentSelectedDataProvider.isPinnedObject(sequence)) {
					return pinImage;
				}
			}
		} else if (element instanceof CombinedSequence) {
			if (element instanceof CombinedSequence) {
				final CombinedSequence combinedSequence = (CombinedSequence) element;
				if (!combinedSequence.getSequences().isEmpty()) {
					final Sequence sequence = combinedSequence.getSequences().get(0);
					@Nullable
					final ISelectedDataProvider currentSelectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
					if (currentSelectedDataProvider != null) {
						if (currentSelectedDataProvider.isPinnedObject(sequence)) {
							return pinImage;
						}
					}
				}

			}
		}
		return null;
	}

	@Override
	public String getText(final Object element) {
		String text = null;
		if (element instanceof Sequence) {
			final Sequence sequence = (Sequence) element;

			String seqText = vesselFormatter.render(sequence);

			// Add scenario instance name to field if multiple scenarios are selected
			final Object input = viewer.getInput();
			if (input instanceof Collection<?>) {
				final Collection<?> collection = (Collection<?>) input;

				if (collection.size() > 1) {
					final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
					if (selectedDataProvider != null) {
						if (selectedScenariosService.getPinnedScenario() != null) {
							// Do nothing now we have a pin icon
						} else {
							final ScenarioResult scenarioResult = selectedDataProvider.getScenarioResult(sequence);
							if (scenarioResult != null) {
								seqText += "\n" + scenarioResult.getModelRecord().getName();
							}
						}
					}
				}
			}
			text = seqText;
		} else if (element instanceof Journey) {
			final Journey j = (Journey) element;
			final RouteOption routeOption = j.getRouteOption();
			if (routeOption != RouteOption.DIRECT) {
				if (memento.getBoolean(Show_Canals)) {
					text = PortModelLabeller.getName(routeOption);
				}
			}
		} else if (element instanceof CombinedSequence) {
			final CombinedSequence combinedSequence = (CombinedSequence) element;
			final Vessel vessel = combinedSequence.getVessel();
			String seqText = vessel == null ? "<Unallocated>" : vesselFormatter.render(vessel);
			// Add scenario instance name to field if multiple scenarios are selected
			final Object input = viewer.getInput();
			if (input instanceof Collection<?>) {
				final Collection<?> collection = (Collection<?>) input;

				if (collection.size() > 1) {
					final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
					if (selectedDataProvider != null) {
						if (selectedScenariosService.getPinnedScenario() != null) {
							// Do nothing now we have a pin icon
						} else {
							final ScenarioResult scenarioResult = selectedDataProvider.getScenarioResult(combinedSequence.getSequences().get(0));
							if (scenarioResult != null) {
								seqText += "\n" + scenarioResult.getModelRecord().getName();
							}
						}
					}
				}
			}
			text = seqText;
		} else if (element instanceof InventoryEvents) {
			final InventoryEvents inventoryEvents = (InventoryEvents) element;
			return inventoryEvents.getFacility().getName();
		} else if (element instanceof InventoryChangeEvent) {
			final InventoryChangeEvent event = (InventoryChangeEvent) element;
			// return event.to String();
		}
		return text;
	}

	public void setScheme(final IScheduleViewColourScheme scheme) {
		if (currentScheme != null) {
			currentScheme.setViewer(null);
		}
		this.currentScheme = scheme;
		if (currentScheme != null) {
			currentScheme.setViewer(viewer);
			memento.putString(SCHEDULER_VIEW_COLOUR_SCHEME, scheme.getID());
		}
	}

	public void toggleHighlighter(final String id) {
		if (highlightersById.containsKey(id)) {
			toggleHighlighter(highlightersById.get(id));
		}
	}

	public void toggleHighlighter(final IScheduleViewColourScheme hi) {

		for (final IScheduleViewColourScheme highlighter : currentHighlighters) {
			if (hi != null && hi == highlighter) {
				currentHighlighters.remove(highlighter);
				highlighter.setViewer(null);
				memento.getChild(Highlight_).putBoolean(hi.getID(), false);
				return;
			}
		}
		if (hi != null) {
			currentHighlighters.add(hi);
			hi.setViewer(viewer);
			memento.putBoolean(hi.getID(), true);
		}
	}

	public void setScheme(final String id) {
		if (colourSchemesById.containsKey(id)) {
			setScheme(colourSchemesById.get(id));
		} else {
			setScheme(colourSchemes.get(0));
		}
	}

	public void toggleShowCanals() {
		showCanals = !showCanals;
		memento.putBoolean(Show_Canals, showCanals);
	}

	public boolean showCanals() {
		return showCanals;
	}

	public void addHighlighter(final String id, final IScheduleViewColourScheme cs) {
		if (id != null && !id.isEmpty()) {
			highlightersById.put(id, cs);
		}

		highlighters.add(cs);
	}

	protected final IScheduleViewColourScheme getCurrentScheme() {
		return currentScheme;
	}

	protected List<IScheduleViewColourScheme> getColourSchemes() {
		return colourSchemes;
	}

	protected List<IScheduleViewColourScheme> getHighlighters() {
		return highlighters;
	}

	protected final boolean isActive(final IScheduleViewColourScheme hi) {
		return currentHighlighters.contains(hi);
	}

	private String dateToString(final ZonedDateTime date) {
		return DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateTimeStringDisplay()).format(date);
	}

	private String dateOnlyToString(final ZonedDateTime date) {
		return DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter().format(date);
	}

	private String dateToString(final LocalDate date, final String defaultString) {
		if (date == null) {
			return defaultString;
		}
		return DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter().format(date);
	}

	private String dateToString(final LocalDateTime date, final String defaultString) {
		if (date == null) {
			return defaultString;
		}
		return DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter().format(date);
	}

	@Override
	public String getToolTipText(final Object element) {
		if (element instanceof CharterAvailableFromEvent) {
			final CharterAvailableFromEvent evt = (CharterAvailableFromEvent) element;
			final String start = dateToString(evt.getStart());
			return "";// String.format("%s available from %s", evt.getLinkedSequence().getName(), start);
		}
		if (element instanceof CharterAvailableToEvent) {
			final CharterAvailableToEvent evt = (CharterAvailableToEvent) element;
			final String start = dateToString(evt.getStart());
			return "";// String.format("%s available until %s", evt.getLinkedSequence().getName(), start);
		}

		if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof Event) {

			// build base string - start, end and time for duration.
			final StringBuilder tt = new StringBuilder();
			final StringBuilder eventText = new StringBuilder();
			if (element instanceof Event) {

				final String name = ((Event) element).name();
				if (name != null && !name.isEmpty()) {
					tt.append("ID: " + name + "\n");
				}
			}

			{
				if (element instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) element;
					final Slot slot = ((SlotVisit) element).getSlotAllocation().getSlot();
					if (slot != null) {
						final Contract contract = slot.getContract();
						if (contract != null) {
							tt.append("Contract: " + contract.getName() + " \n");
						} else if (slot instanceof SpotSlot) {
							final SpotSlot spotSlot = (SpotSlot) slot;
							final SpotMarket market = spotSlot.getMarket();
							tt.append("Spot market: " + market.getName() + " \n");
						} else {
							// Spot purchase or sale - leave blank?
							// tt.append("Spot " + (slot instanceof LoadSlot ? "purchase" : "sale") + " \n");
						}
						tt.append(" \n");
					}
					final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
					final Sequence sequence = slotVisit.getSequence();
					if (slotAllocation != null && sequence != null) {
						final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
						if (cargoAllocation != null && cargoAllocation.getSlotAllocations().size() == 2) {
							if (sequence.getSequenceType() == SequenceType.FOB_SALE) {
								final Vessel nominatedVessel = cargoAllocation.getSlotAllocations().get(1).getSlot().getNominatedVessel();
								if (nominatedVessel != null) {
									tt.append("Vessel: " + nominatedVessel.getName() + " \n\n");
								}
							} else if (sequence.getSequenceType() == SequenceType.DES_PURCHASE) {
								final Vessel nominatedVessel = cargoAllocation.getSlotAllocations().get(0).getSlot().getNominatedVessel();
								if (nominatedVessel != null) {
									tt.append("Vessel: " + nominatedVessel.getName() + " \n\n");
								}
							}
						}
					}
				}
			}

			final Event event = (Event) element;
			final String start = dateToString(event.getStart());
			final String end = dateToString(event.getEnd());
			tt.append("Start: " + start + "\n");
			tt.append("End: " + end + "\n");
			final int days = event.getDuration() / 24;
			final int hours = event.getDuration() % 24;
			final String durationTime = days + " day" + (days > 1 || days == 0 ? "s" : "") + ", " + hours + " hour" + (hours > 1 || hours == 0 ? "s" : "");
		
			// build event specific text
			if (element instanceof Journey) {
				eventText.append("Travel time: " + durationTime + " \n");
				final Journey journey = (Journey) element;
				eventText.append(" \n");
				// if (!journey.getRoute().equalsIgnoreCase(RouteOption.DIRECT.getName())) {
				eventText.append(String.format("%.1f knots", journey.getSpeed()));
				for (final FuelQuantity fq : journey.getFuels()) {
					eventText.append(String.format(" | %s", mapFuel(fq.getFuel())));
				}
				final RouteOption routeOption = journey.getRouteOption();

				if (routeOption != null && routeOption != RouteOption.DIRECT) {
					eventText.append(" | " + PortModelLabeller.getName(routeOption) + "\n");
					if (routeOption == RouteOption.PANAMA) {
						final CanalBookingSlot canalBooking = journey.getCanalBooking();
						String direction = "";
						if (journey.getCanalEntrance() == CanalEntry.NORTHSIDE) {
							direction = "southbound";
						}
						if (journey.getCanalEntrance() == CanalEntry.SOUTHSIDE) {
							direction = "northbound";
						}
						if (canalBooking != null) {
							eventText.append(String.format("Canal booking: %s %s\n", dateToString(journey.getCanalDateTime(), "<date unknown>"), direction));
						} else {
							if (journey.getCanalDateTime() != null && journey.getCanalDateTime().equals(journey.getLatestPossibleCanalDateTime())) {
								eventText.append(String.format("Booking required: %s %s\n", dateToString(journey.getCanalDateTime(), "<date unknown>"), direction));
							} else if (journey.getCanalDateTime() != null && journey.getLatestPossibleCanalDateTime() != null
									&& journey.getLatestPossibleCanalDateTime().isBefore(journey.getCanalDateTime())) {
								// eventText.append(String.format("Infeasible booking required: %s\n", direction));
								// May be infeasible. However we do not store hour of day, so checks are not fully accurate
								eventText.append(String.format("Booking required: %s %s\n", dateToString(journey.getCanalDateTime(), "<date unknown>"), direction));
							} else if (journey.getCanalDateTime() != null && journey.getLatestPossibleCanalDateTime() != null) {
								eventText.append(String.format("Booking required between: %s and %s %s\n", dateToString(journey.getCanalDateTime(), "<date unknown>"),
										dateToString(journey.getLatestPossibleCanalDateTime(), "<date unknown>"), direction));
							}
						}
					}
				}
			} else if (element instanceof CanalBookingEvent) {
				final CanalBookingEvent canalBookingEvent = (CanalBookingEvent) element;
				final Journey journey = canalBookingEvent.getLinkedJourney();

				final CanalBookingSlot canalBooking = journey.getCanalBooking();
				String direction = "";
				if (journey.getCanalEntrance() == CanalEntry.NORTHSIDE) {
					direction = "southbound";
				}
				if (journey.getCanalEntrance() == CanalEntry.SOUTHSIDE) {
					direction = "northbound";
				}
				if (canalBooking != null) {
					eventText.append(String.format("Canal booking: %s %s\n", dateToString(journey.getCanalDateTime(), "<date unknown>"), direction));
				} else {
					eventText.append(String.format("Booking required: %s %s\n", dateToString(journey.getCanalDateTime(), "<date unknown>"), direction));
				}
			} else if (element instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) element;
				final Slot slot = ((SlotVisit) element).getSlotAllocation().getSlot();
				if (slot != null) {
					eventText.append("Time in port: " + durationTime + " \n");
					// eventText.append("Window Start: " + dateToString(slot.getWindowStartWithSlotOrPortTime()) + "\n");
					eventText.append("Window End: " + dateToString(slot.getWindowEndWithSlotOrPortTime()) + "\n");
					eventText.append(" \n");
				}
				boolean checkLateness = true;
				// Do not check divertible slots
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDivertible()) {
						checkLateness = false;
					}
				} else if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateDivertible()) {
						checkLateness = false;
					}
				}
				// lateness
				if (checkLateness) {
					if (LatenessUtils.isLateExcludingFlex(slotVisit)) {
						final int lateHours = LatenessUtils.getLatenessInHours(slotVisit);
						if (lateHours != 0) {
							eventText.append("LATE by " + LatenessUtils.formatLatenessHours(lateHours) + "\n");
						}
					}
				}
			} else if (element instanceof VesselEventVisit) {
				final VesselEventVisit vev = (VesselEventVisit) element;
				eventText.append("Duration: " + durationTime + "\n");
				if (LatenessUtils.isLateExcludingFlex(vev)) {
					final int lateHours = LatenessUtils.getLatenessInHours(vev);
					if (lateHours != 0) {
						eventText.append(" \n");
						eventText.append("LATE by " + LatenessUtils.formatLatenessHours(lateHours) + "\n");
					}
				}
			} else if (element instanceof Idle) {
				final Idle idle = (Idle) element;
				eventText.append("Idle time: " + durationTime);
				for (final FuelQuantity fq : idle.getFuels()) {
					eventText.append(String.format("\n%s\n", mapFuel(fq.getFuel())));
				}
			} else if (element instanceof CharterLengthEvent) {
				final CharterLengthEvent idle = (CharterLengthEvent) element;
				eventText.append("Duration: " + durationTime);
				for (final FuelQuantity fq : idle.getFuels()) {
					eventText.append(String.format("\n%s\n", mapFuel(fq.getFuel())));
				}
			} else if (element instanceof GeneratedCharterOut) {
				eventText.append(" \n");
				eventText.append("Duration: " + durationTime);

			} else if (element instanceof FuelUsage) {
				final FuelUsage fuel = (FuelUsage) element;
				for (final FuelQuantity fq : fuel.getFuels()) {
					eventText.append(String.format("%s: %,d %s | $%,d\n", mapFuel(fq.getFuel()), fq.getAmounts().get(0).getQuantity(), fq.getAmounts().get(0).getUnit().toString(), fq.getCost()));
				}
			}

			{
				final Integer value = getPnL(element);
				if (value != null) {
					eventText.append("\nP&L: " + String.format("$%,d", value.intValue()));
				}
			}

			tt.append(eventText);
			return tt.toString();
		} else if (element instanceof InventoryChangeEvent) {
			final InventoryChangeEvent evt = (InventoryChangeEvent) element;
			final StringBuilder tt = new StringBuilder();
			tt.append("Date: " + dateToString(evt.getDate(), "") + "\n");

			if (evt.isBreachedMax()) {
				tt.append("Tank top\n");
			} else if (evt.isBreachedMin()) {
				tt.append("Breached min level\n");
			}
			return tt.toString();
		}
		return null;

	}

	@Override
	public String getToolTipTitle(final Object element) {

		if (element instanceof CharterAvailableFromEvent) {
			final CharterAvailableFromEvent evt = (CharterAvailableFromEvent) element;
			final String start = dateOnlyToString(evt.getStart());
			return String.format("%s available from %s", evt.getLinkedSequence().getName(), start);
		}
		if (element instanceof CharterAvailableToEvent) {
			final CharterAvailableToEvent evt = (CharterAvailableToEvent) element;
			final String start = dateOnlyToString(evt.getStart());
			return String.format("%s available until %s", evt.getLinkedSequence().getName(), start);
		}

		String port = null;
		if (element instanceof Event) {
			final Port portObj = ((Event) element).getPort();
			if (portObj != null) {
				port = portObj.getName();
			}
		}
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			return port + " -- " + journey.getDestination().getName() + (journey.isLaden() ? " (Laden)" : " (Ballast)");
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			return (port == null) ? "" : ("At " + port) + (idle.isLaden() ? " (Laden" : " (Ballast") + " idle)";
		} else if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) element;
			final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
			if (vesselEvent instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEvent;
				if (charterOutEvent.getRelocateTo() != null) {
					final Port fromPort = charterOutEvent.getPort();
					final Port toPort = charterOutEvent.getRelocateTo();

					if (fromPort != null && toPort != null) {
						final String fromPortName = fromPort.getName();
						final String toPortName = toPort.getName();
						if (fromPortName != null && toPortName != null) {

							return String.format("Charter %s to %s", fromPortName, toPortName);
						}
					}

				}
				return (port == null) ? "" : ("Charter at " + port);
			}
			return (port == null) ? "" : ("At " + port);
		} else if (element instanceof Event) {
			return (port == null) ? "" : ("At " + port) + (element instanceof Cooldown ? " (Cooldown)" : ""); // + displayTypeName;
		} else if (element instanceof SlotVisit) {
			final SlotVisit sv = (SlotVisit) element;
			return (port == null) ? "" : ("At " + port + " ") + (sv.getSlotAllocation().getSlot() instanceof LoadSlot ? "(Load)" : "(Discharge)"); // + displayTypeName;

		} else if (element instanceof InventoryChangeEvent) {
			final InventoryChangeEvent evt = (InventoryChangeEvent) element;
			if (evt.eContainer() instanceof InventoryEvents) {
				final InventoryEvents inventoryEvents = (InventoryEvents) evt.eContainer();
				final Inventory facility = inventoryEvents.getFacility();
				if (facility != null) {
					return "Inventory at " + facility.getName();
				}
			}
		}
		return null;
	}

	@Override
	public Image getToolTipImage(final Object element) {
		return null;// Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
	}

	@Override
	public Color getForeground(final Object element) {
		if (currentScheme != null) {
			return currentScheme.getForeground(element);
		}
		return null;
	}

	@Override
	public Color getBackground(final Object element) {

		Color c = null;
		for (final IScheduleViewColourScheme highlighter : currentHighlighters) {
			if (c == null)
				c = highlighter.getBackground(element);
		}

		if (c == null && currentScheme != null) {
			c = currentScheme.getBackground(element);
		}
		return c;
	}

	public void addColourScheme(final String id, final IScheduleViewColourScheme colourScheme) {
		if (id != null && !id.isEmpty()) {
			colourSchemesById.put(id, colourScheme);
		}

		colourSchemes.add(colourScheme);
		if ((currentScheme == null) && (colourSchemes.size() == 1)) {
			currentScheme = colourScheme;
		}
	}

	@Override
	public int getAlpha(final Object element) {
		if (currentScheme != null) {
			return currentScheme.getAlpha(element);
		}
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
		if (currentScheme != null) {
			return currentScheme.getBorderColour(element);
		}
		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {

		if (currentScheme != null) {
			return currentScheme.getBorderWidth(element);
		}
		return 1;
	}

	private Integer getPnL(final Object object) {
		ProfitAndLossContainer container = null;

		if (object instanceof CargoAllocation) {
			container = (CargoAllocation) object;
		}
		if (object instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) object;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}
		if (object instanceof VesselEventVisit) {
			container = (VesselEventVisit) object;
		}
		if (object instanceof StartEvent) {
			container = (StartEvent) object;
		}
		if (object instanceof GeneratedCharterOut) {
			container = (GeneratedCharterOut) object;
		}

		if (container != null) {
			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				return (int) dataWithKey.getProfitAndLoss();
			}
		}

		return null;

	}

	private String mapFuel(Fuel fuel) {
		if (fuel == null) {
			return "";
		}

		switch (fuel) {
		case BASE_FUEL:
			return "base fuel";
		case FBO:
			return "FBO";
		case NBO:
			return "NBO";
		case PILOT_LIGHT:
			return "pilot light";
		default:
			return fuel.toString();
		}
	}
}
