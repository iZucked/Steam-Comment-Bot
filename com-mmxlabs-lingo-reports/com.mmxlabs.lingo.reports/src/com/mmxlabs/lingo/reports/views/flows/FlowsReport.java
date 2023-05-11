/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.flows;

import java.nio.channels.IllegalSelectorException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.google.common.util.concurrent.AtomicDouble;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.ScenarioResult;

public class FlowsReport extends ViewPart {

	private final IFlowSorter flowSorter = new FlatnessAndNameSorter();

	// TODO: Override toString for display in actions
	enum CountMode {
		Volume, Cargo
	};

	enum DataMode {
		Port, Contract
	};

	// Raw input data from scenarios. Do not change in paint method
	class Flow {
		String from;
		String to;
		int value;

	}

	// Render state info for a node.
	class Node {
		// Node label
		String name;
		// Node colour
		Color c;

		// This is the rendered position of the node
		int yOffset;
		int xOffset;

		/** Node height. This is the scaled sum of flow values */
		float height;

		/** This is the unscaled sum of flow values */
		int rawValue; // For tooltip

		/** Current offset for the next flow line end at. */
		float lhsOffset;

		/** Current offset for the next flow line start from. */
		float rhsOffset;

		List<Link> lhsLinks = new LinkedList<>();
		List<Link> rhsLinks = new LinkedList<>();
	}

	class Link {
		Node from;
		Node to;
		float value; // Rendered height
		int rawValue; // For tooltip

	}

	class State {
		List<Flow> lhsFlows = new LinkedList<>();
		List<Flow> rhsFlows = new LinkedList<>();

		List<Node> lhsNodes;
		List<Node> middleNodes;
		List<Node> rhsNodes;
	}

	private State state = null;
	private final ImageRegistry gradientImageRegistry = new ImageRegistry();

	private final Color[] colors = new Color[] { hex2Rgb("#a6cee3"), hex2Rgb("#b2df8a"), hex2Rgb("#fb9a99"), hex2Rgb("#fdbf6f"), hex2Rgb("#cab2d6"), hex2Rgb("#ffff99"), hex2Rgb("#1f78b4"),
			hex2Rgb("#33a02c") };

	private CountMode mode = CountMode.Cargo;
	private DataMode dm = DataMode.Contract;
	private ScenarioComparisonService scenarioComparisonService;
	private final ISelectedScenariosServiceListener listener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull final ISelectedDataProvider ss, final boolean block) {

			final State state = new State();
			// Scale factor
			if (ss.inPinDiffMode()) {
				// Pin Diff mode

				final Map<Pair<String, String>, Integer> m = new HashMap<>();

				{
					final ScheduleModel scheduleModel = ss.getPinnedScenarioResult().getTypedResult(ScheduleModel.class);
					for (final CargoAllocation ca : scheduleModel.getSchedule().getCargoAllocations()) {
						final Pair<String, String> p = getKey(dm, ca);
						if (p.getFirst().equals(p.getSecond())) {
							continue;
						}
						if (mode == CountMode.Volume) {
							m.merge(p, -ca.getSlotAllocations().get(0).getVolumeTransferred(), Integer::sum);
						} else if (mode == CountMode.Cargo) {
							m.merge(p, -1, Integer::sum);
						} else {
							throw new IllegalSelectorException();
						}
					}

				}
				{
					for (final ScenarioResult r : ss.getOtherScenarioResults()) {
						if (r == ss.getPinnedScenarioResult()) {
							continue;
						}
						final ScheduleModel scheduleModel = r.getTypedResult(ScheduleModel.class);
						for (final CargoAllocation ca : scheduleModel.getSchedule().getCargoAllocations()) {

							final Pair<String, String> p = getKey(dm, ca);
							if (p.getFirst().equals(p.getSecond())) {
								continue;
							}
							if (mode == CountMode.Volume) {
								m.merge(p, ca.getSlotAllocations().get(0).getVolumeTransferred(), Integer::sum);
							} else if (mode == CountMode.Cargo) {
								m.merge(p, 1, Integer::sum);
							} else {
								throw new IllegalSelectorException();
							}
						}
						break;
					}

				}
				// ADP/LT scenarios may not have any before data, so revert to single solution mode.
				boolean hasLhs = false;
				for (final var e : m.entrySet()) {
					if (e.getValue() < 0) {
						hasLhs = true;
						break;
					}
				}
				if (hasLhs) {
					for (final var e : m.entrySet()) {
						if (e.getValue() < 0) {
							final Flow flow = new Flow();
							/// Reverse mapping
							flow.to = e.getKey().getFirst();
							flow.from = e.getKey().getSecond();
							// Negate the negative value - the report only works with positive values.
							flow.value = -e.getValue();
							state.lhsFlows.add(flow);
						} else if (e.getValue() > 0) {
							final Flow flow = new Flow();

							flow.from = e.getKey().getFirst();
							flow.to = e.getKey().getSecond();
							flow.value = e.getValue();
							state.rhsFlows.add(flow);
						}
					}
				} else {
					// Fallback to single solution mode
					for (final var e : m.entrySet()) {
						final Flow flow = new Flow();
						flow.from = e.getKey().getFirst();
						flow.to = e.getKey().getSecond();
						// Ensure positive value
						flow.value = Math.abs(e.getValue());
						state.lhsFlows.add(flow);
					}
				}
			} else {
				final Map<Pair<String, String>, Integer> m = new HashMap<>();
				if (!ss.getAllScenarioResults().isEmpty()) {
					// Single report mode
					final ScheduleModel scheduleModel = ss.getAllScenarioResults().get(0).getTypedResult(ScheduleModel.class);
					for (final CargoAllocation ca : scheduleModel.getSchedule().getCargoAllocations()) {

						final Pair<String, String> p = getKey(dm, ca);
						if (p.getFirst().equals(p.getSecond())) {
							continue;
						}
						if (mode == CountMode.Volume) {
							m.merge(p, ca.getSlotAllocations().get(0).getVolumeTransferred(), Integer::sum);
						} else if (mode == CountMode.Cargo) {
							m.merge(p, 1, Integer::sum);
						} else {
							throw new IllegalSelectorException();
						}
					}

					for (final var e : m.entrySet()) {
						final Flow flow = new Flow();
						flow.from = e.getKey().getFirst();
						flow.to = e.getKey().getSecond();
						flow.value = e.getValue();
						state.lhsFlows.add(flow);
					}
				}
			}

			build(state);

			FlowsReport.this.state = state;

			if (canvas != null) {
				canvas.redraw();
			}
		}

		@Override
		public void selectedObjectChanged(@Nullable final MPart source, final ISelection selection) {
			if (canvas != null) {
				// canvas.redraw();
			}
		}
	};

	private Composite canvas;

	private ScrolledComposite parent;

	private Image pickerImage = null;

	private final Map<Integer, Object> pickerMap = new ConcurrentHashMap<>();

	@Override
	public void createPartControl(final Composite pp) {

		this.parent = new ScrolledComposite(pp, SWT.H_SCROLL | SWT.V_SCROLL | SWT.DOUBLE_BUFFERED);
		scenarioComparisonService = getSite().getService(ScenarioComparisonService.class);

		canvas = new Composite(this.parent, SWT.DOUBLE_BUFFERED);
		canvas.setVisible(true);

		canvas.addPaintListener(this::doPaint);

		canvas.addMouseMoveListener(e -> {
			final Image img = pickerImage;
			if (img != null) {
				// Shift off the alpha value
				final int pixel = img.getImageData().getPixel(e.x, e.y) >> 8;
				final Object o = pickerMap.get(pixel);
				if (o instanceof final Link l) {
					canvas.setToolTipText(String.format("%s -> %s: %s%n", l.from.name, l.to.name, l.rawValue));
					return;
				} else if (o instanceof final Node n) {
					canvas.setToolTipText(String.format("%s: Total %s%n", n.name, n.rawValue));
					return;
				}
			}
			canvas.setToolTipText(null);
		});
		this.parent.setLayout(new FillLayout());
		this.parent.setContent(canvas);

		this.parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// Set an initial size to the paint listener will kick in.
		this.canvas.setSize(50, 50);

		final EnumMap<DataMode, Action> m = new EnumMap<>(DataMode.class);
		for (final var v : DataMode.values()) {
			final Action a = new RunnableAction(v.toString(), SWT.TOGGLE, () -> {
				dm = v;
				scenarioComparisonService.triggerListener(listener, false);
				m.entrySet().forEach(e -> e.getValue().setChecked(e.getKey() == v));
			});
			a.setChecked(dm == v);
			getViewSite().getActionBars().getMenuManager().add(a);
			m.put(v, a);
		}
		getViewSite().getActionBars().getMenuManager().add(new Separator());
		final EnumMap<CountMode, Action> n = new EnumMap<>(CountMode.class);

		for (final var v : CountMode.values()) {
			final Action a = new RunnableAction(v.toString(), SWT.TOGGLE, () -> {
				mode = v;
				scenarioComparisonService.triggerListener(listener, false);
				n.entrySet().forEach(e -> e.getValue().setChecked(e.getKey() == v));
			});
			a.setChecked(mode == v);
			getViewSite().getActionBars().getMenuManager().add(a);
			n.put(v, a);
		}

		scenarioComparisonService.addListener(listener);
		scenarioComparisonService.triggerListener(listener, false);
	}

	private void build(final State state) {

		if (state.lhsFlows.isEmpty() && state.rhsFlows.isEmpty()) {
			return;
		}

		final Set<String> lhs = new LinkedHashSet<>();
		final Set<String> middle = new LinkedHashSet<>();
		final Set<String> rhs = new LinkedHashSet<>();

		for (final Flow f : state.lhsFlows) {
			lhs.add(f.from);
			middle.add(f.to);
		}
		for (final Flow f : state.rhsFlows) {
			middle.add(f.from);
			rhs.add(f.to);
		}

		// Assign a unique colour per node name (not per node).
		final AtomicInteger colIdx = new AtomicInteger(0); // Object needed in lambda
		final Map<String, Color> colourMap = new HashMap<>();

		// Generate nodes and assign colours. Position will be assigned during rendering
		final List<Node> lhsNodes = new LinkedList<>();
		final List<Node> middleNodes = new LinkedList<>();
		final List<Node> rhsNodes = new LinkedList<>();
		{
			for (final String name : lhs) {
				final Node n = new Node();
				n.name = name;
				// Use same colour for same label
				n.c = colourMap.computeIfAbsent(n.name, key -> colors[colIdx.getAndIncrement() % colors.length]);

				for (final Flow f : state.lhsFlows) {
					if (n.name.equals(f.from)) {
						// Sum up flow heights
						n.rawValue += f.value;
					}
				}
				lhsNodes.add(n);
			}

			for (final String name : middle) {
				final Node n = new Node();
				n.name = name;
				n.c = colourMap.computeIfAbsent(n.name, key -> colors[colIdx.getAndIncrement() % colors.length]);
				// Compare flows for both sides of the node
				int rawLhsHeight = 0;
				for (final Flow f : state.lhsFlows) {
					if (n.name.equals(f.to)) {
						rawLhsHeight += f.value;
					}
				}
				int rawRhsHeight = 0;
				for (final Flow f : state.rhsFlows) {
					if (n.name.equals(f.from)) {
						rawRhsHeight += f.value;
					}
				}
				n.rawValue = Math.max(rawLhsHeight, rawRhsHeight);

				middleNodes.add(n);
			}

			for (final String name : rhs) {
				final Node n = new Node();
				n.name = name;
				n.c = colourMap.computeIfAbsent(n.name, key -> colors[colIdx.getAndIncrement() % colors.length]);
				for (final Flow f : state.rhsFlows) {
					if (n.name.equals(f.to)) {
						n.rawValue += f.value;
					}
				}
				rhsNodes.add(n);
			}
		}

		// Populate links
		for (final Flow f : state.lhsFlows) {
			final Link link = new Link();
			link.rawValue = f.value;
			for (final Node n : lhsNodes) {
				if (n.name.equals(f.from)) {
					link.from = n;
					n.rhsLinks.add(link);
					break;
				}
			}

			for (final Node n : middleNodes) {
				if (n.name.equals(f.to)) {
					link.to = n;
					n.lhsLinks.add(link);
					break;
				}
			}
		}

		for (final Flow f : state.rhsFlows) {
			final Link link = new Link();
			link.rawValue = f.value;
			for (final Node n : middleNodes) {
				if (n.name.equals(f.from)) {
					link.from = n;
					n.rhsLinks.add(link);
					break;
				}
			}

			for (final Node n : rhsNodes) {
				if (n.name.equals(f.to)) {
					link.to = n;
					n.lhsLinks.add(link);
					break;
				}
			}
		}

		flowSorter.sortNodesAndLinks(lhsNodes, middleNodes, rhsNodes);

		state.lhsNodes = lhsNodes;
		state.middleNodes = middleNodes;
		state.rhsNodes = rhsNodes;
	}

	private void doPaint(final PaintEvent e) {

		final GC gc = e.gc;

		// Reset canvas and temporary state

		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(canvas.getClientArea());
		// Clear out old image picker data
		if (pickerImage != null) {
			pickerImage.dispose();
			pickerImage = null;
		}

		if (this.state == null) {
			gc.drawText("No data", 0, 0);
			return;
		}

		final int margin = 5;
		final int nodeWidth = 10;
		final int nodePadding = 5;
		final int columnSpacing = 300;

		// Determine y scale factor
		int sumLhsFrom = 0;
		int sumLhsTo = 0;
		int sumRhsFrom = 0;
		int sumRhsTo = 0;

		for (final Flow f : state.lhsFlows) {
			sumLhsFrom += f.value;
			sumLhsTo += f.value;
		}
		for (final Flow f : state.rhsFlows) {
			sumRhsFrom += f.value;
			sumRhsTo += f.value;
		}

		final int total = Math.max(sumLhsFrom, Math.max(sumLhsTo, Math.max(sumRhsFrom, sumRhsTo)));

		if (total == 0) {
			// TODO: No data, because there is no data, or the scenario delta are zero
			if (state.lhsFlows.isEmpty() && state.rhsFlows.isEmpty()) {
				gc.drawText("No data", 0, 0);
			} else if (!state.lhsFlows.isEmpty()) {
				gc.drawText("No change between scenarios", 0, 0);
			}
			return;
		}

		// Create a new image to handle move overs. As the Path objects are not simple shapes it is hard to determine whether or not our cursor is over the shape.
		// While Path has a contains method, it also needs a GC which is not available in the mouse listener.
		// Instead we create secondary image and render everything twice. Once properly in the canvas GC and once in the secondary image.
		// The trick is that each element rendered in the secondary image has a unique colour - thus by inspecting the pixel the mouse is over we can map back to the object that rendered it.
		final Image pickImage = new Image(null, canvas.getClientArea());
		final GC pickerGC = new GC(pickImage);
		// Start at 1 as by default all pixels will have a value of 0.
		final AtomicInteger pickerColour = new AtomicInteger(1);

		final BiConsumer<Object, Consumer<GC>> pickerAction = (n, action) -> {
			final int px = pickerColour.getAndIncrement();
			final int r = px & 0xFF;
			final int g = (px >> 8) & 0xFF;
			final int b = (px >> 16) & 0xFF;
			final Color c = new Color(new RGB(r, g, b));
			try {
				pickerGC.setBackground(c);
				// Caller should run gc.fillXXXX
				action.accept(pickerGC);
				pickerMap.put(px, n);
			} finally {
				c.dispose();
			}
		};

		//// Apply scale factor

		// Y scale factor to (roughly) fit view area
		// Get visible height, subtract absolute pixels (margins, padding between nodes) to leave the pixels available for the nodes. Then divide by max height/value across all columns to get the
		// scale factor.

		// TODO: We may want to have a min link height as in some cases scale * flow.value may round down to zero!
		int clientY = parent.getClientArea().height;
		// take off top and bottom margins
		clientY -= margin;
		clientY -= nodePadding;
		// Take off estimated max padding zones between nodes
		clientY -= Math.max(state.lhsNodes.size(), Math.max(state.middleNodes.size(), state.rhsNodes.size())) * nodePadding;
		// Take off a bit more for rounding.
		clientY -= 2;
		final float scale = (float) (clientY) / (float) total;

		// Compute scaled value and reset link offset state
		{
			final Consumer<Node> action = n -> {
				n.xOffset = 0;
				n.yOffset = 0;
				n.lhsOffset = 0.0f;
				n.rhsOffset = 0.0f;
				n.height = Math.round(Math.max(1, n.rawValue * scale));

				for (final var l : n.rhsLinks) {
					l.value = Math.round(Math.max(1, l.rawValue * scale));
				}
			};
			state.lhsNodes.forEach(action);
			state.middleNodes.forEach(action);
			state.rhsNodes.forEach(action);
		}
		// Once sorted, compute the node positions
		{
			final AtomicInteger xOffset = new AtomicInteger();
			final AtomicDouble y = new AtomicDouble();

			final Consumer<Node> action = n -> {
				// Set position and update for next node
				n.xOffset = xOffset.get();
				n.yOffset = (int) Math.round(y.get());
				y.addAndGet(n.height);
				y.addAndGet(nodePadding);
			};

			xOffset.set(margin); // Col position 1
			y.set(margin); // Reset y
			state.lhsNodes.forEach(action);

			xOffset.set(margin + nodeWidth + columnSpacing); // Col position 2
			y.set(margin); // Reset y
			state.middleNodes.forEach(action);

			xOffset.set(margin + nodeWidth + columnSpacing + nodeWidth + columnSpacing); // Col position 3
			y.set(margin); // Reset y
			state.rhsNodes.forEach(action);
		}

		// TODO: Calculate link positions rather than in drawLinks

		// Draw links first, with transparency. We prefer nodes drawing over the links, rather than links over nodes
		gc.setAlpha(127);

		drawLinks(gc, nodeWidth, nodePadding, columnSpacing, state.lhsNodes, state.middleNodes, pickerAction);
		drawLinks(gc, nodeWidth, nodePadding, columnSpacing, state.middleNodes, state.rhsNodes, pickerAction);

		// No transparency for node and labels
		gc.setAlpha(255);

		// variable to store final rendered height for scrollbar computation.
		int height = 0;
		{
			// Draw LHS nodes
			for (final Node n : state.lhsNodes) {
				drawNode(gc, nodeWidth, pickerAction, n, SWT.LEFT);
				height = Math.max(height, Math.round(n.yOffset + n.height + nodePadding));
			}

			// Draw middle nodes
			// Determine text alignment
			final int align = state.rhsNodes.isEmpty() ? SWT.RIGHT : SWT.CENTER;
			for (final Node n : state.middleNodes) {
				drawNode(gc, nodeWidth, pickerAction, n, align);
				height = Math.max(height, Math.round(n.yOffset + n.height + nodePadding));
			}

			// Draw rhs nodes
			for (final Node n : state.rhsNodes) {
				drawNode(gc, nodeWidth, pickerAction, n, SWT.RIGHT);
				height = Math.max(height, Math.round(n.yOffset + n.height + nodePadding));

			}
		}

		// Adjust the canvas size for scrollbars update
		int width = margin + nodeWidth + columnSpacing + nodeWidth + margin;
		if (!state.rhsNodes.isEmpty()) {
			width += columnSpacing + nodeWidth;
		}
		// Keep a min size otherwise we may disable painting
		canvas.setSize(Math.max(50, width), Math.max(50, height + margin));

		// Update the mouse-move image
		this.pickerImage = pickImage;
		pickerGC.dispose();
	}

	private void drawNode(final GC gc, final int nodeWidth, final BiConsumer<Object, Consumer<GC>> pickerAction, final Node n, final int textAlign) {
		gc.setBackground(n.c);
		gc.fillRectangle(n.xOffset, n.yOffset, nodeWidth, (int) n.height);

		if (textAlign == SWT.LEFT) {
			gc.drawText(n.name, n.xOffset + 2, n.yOffset + (int) n.height / 2 - gc.textExtent(n.name).y / 2, true);
		} else if (textAlign == SWT.CENTER) {
			gc.drawText(n.name, n.xOffset - (gc.textExtent(n.name).x - nodeWidth) / 2, n.yOffset + (int) n.height / 2 - gc.textExtent(n.name).y / 2, true);
		} else if (textAlign == SWT.RIGHT) {
			gc.drawText(n.name, n.xOffset - 2 + nodeWidth - gc.textExtent(n.name).x, n.yOffset + (int) n.height / 2 - gc.textExtent(n.name).y / 2, true);
		} else {
			throw new IllegalArgumentException();
		}

		pickerAction.accept(n, g -> g.fillRectangle(n.xOffset, n.yOffset, nodeWidth, (int) n.height));

	}

	private void drawLinks(final GC gc, final int nodeWidth, final int nodePadding, final int columnSpacing, final List<Node> lhsNodes, final List<Node> middleNodes,
			final BiConsumer<Object, Consumer<GC>> pickerAction) {

		// Percentage of distance (relative to both line ends) for the curve waypoints points
		final float dx = 0.3f;
		final float dy = 0.1f;
		for (final Node lhsNode : lhsNodes) {
			for (final Link f : lhsNode.rhsLinks) {
				final int x1;
				final int y1;
				Color from = null;
				{
					final Node n = f.from;
					assert n == lhsNode;
					x1 = n.xOffset + nodeWidth;
					// Find current flow use and increment for next flow
					y1 = Math.round(n.yOffset + n.rhsOffset);
					n.rhsOffset += f.value;
					from = n.c;
				}

				Color to = null;
				final int x2;
				final int y2;
				{
					final Node n = f.to;
					x2 = n.xOffset;
					// Find current flow use and increment for next flow
					y2 = Math.round(n.yOffset + n.lhsOffset);
					n.lhsOffset += f.value;
					to = n.c;
				}

				// Render the curved line
				{
					final Image image = getImage(from, to, x1, x2, columnSpacing);
					final Pattern pat = new Pattern(null, image);

					gc.setBackgroundPattern(pat);
					final float y1a = y1 + f.value;
					final float y2a = y2 + f.value;

					final Path path = new Path(null);
					path.moveTo(x1, y1);
					path.cubicTo(x1 + (x2 - x1) * dx, y1 + (y2 - y1) * dy, x1 + (x2 - x1) * (1.0f - dx), y1 + (y2 - y1) * (1.0f - dy), x2, y2);
					path.lineTo(x2, y2a);
					path.cubicTo(x2 + (x1 - x2) * dx, y2a + (y1a - y2a) * dy, x2 + (x1 - x2) * (1.0f - dx), y2a + (y1a - y2a) * (1.0f - dy), x1, y1a);
					path.lineTo(x1, y1); // Complete the path to origin

					gc.fillPath(path);
					pickerAction.accept(f, g -> g.fillPath(path));
					path.dispose();
					pat.dispose();
				}
			}
		}
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}

	@Override
	public void dispose() {
		for (final var c : colors) {
			c.dispose();
		}
		gradientImageRegistry.dispose();
		if (scenarioComparisonService != null) {
			scenarioComparisonService.removeListener(listener);
		}
		if (pickerImage != null) {
			pickerImage.dispose();
		}

	}

	private Pair<String, String> getKey(final DataMode dm, final CargoAllocation ca) {
		if (dm == DataMode.Contract) {
			String sa;
			{
				final SlotAllocation a = ca.getSlotAllocations().get(0);
				final Contract ac = a.getContract();
				if (ac != null) {
					sa = ac.getName();
				} else {
					sa = a.getPort().getName();
				}
			}
			String sb;
			{
				final SlotAllocation b = ca.getSlotAllocations().get(1);
				final Contract bc = b.getContract();
				if (bc != null) {
					sb = bc.getName();
				} else {
					sb = b.getPort().getName();
				}
			}
			return Pair.of(sa, sb);
		} else {
			return Pair.of(ca.getSlotAllocations().get(0).getPort().getName(), ca.getSlotAllocations().get(1).getPort().getName());
		}
	}

	/**
	 * From https://stackoverflow.com/questions/4129666/how-to-convert-hex-to-rgb-using-java
	 */
	public static Color hex2Rgb(final String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	/**
	 * Get a gradient image between nodes. This will be used in a pattern. Patterns repeat, but are relative to the canvas 0,0, not the location of the fill. Hence we can create a 1px high
	 * 
	 * @param from
	 * @param to
	 * @param x1
	 * @param x2
	 * @param columnSpacing
	 * @return
	 */
	private Image getImage(final Color from, final Color to, final int x1, final int x2, final int columnSpacing) {

		// Cache key
		final String key = String.format("%s-%s-%s-%s-%s", from, to, x1, x2, columnSpacing);
		Image image = gradientImageRegistry.get(key);
		if (image != null) {
			return image;
		} else {
			image = new Image(Display.getDefault(), x2, 1);

			final GC gc2 = new GC(image);
			gc2.setAdvanced(true);

			gc2.setForeground(from);
			gc2.setBackground(to);

			gc2.fillGradientRectangle(x1, 0, columnSpacing, 1, false);
			gc2.dispose();

			gradientImageRegistry.put(key, image);
			return image;
		}
	}
}
