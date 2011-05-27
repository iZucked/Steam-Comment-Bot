/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.wiringeditor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;

import com.mmxlabs.common.Pair;

/**
 * A composite for displaying a wiring editor
 * 
 * @author Tom Hinton
 * 
 */
public class WiringComposite extends Composite {
	private final ArrayList<Cargo> cargos = new ArrayList<Cargo>();
	private final ArrayList<Integer> wiring = new ArrayList<Integer>();

	/**
	 * @param parent
	 * @param style
	 */
	public WiringComposite(Composite parent, int style) {
		super(parent, style);
		createLayout();
		// setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

	/**
	 * Set the cargos, and reset the wiring to match these cargos.
	 * 
	 * @param cargos
	 */
	public void setCargos(final List<Cargo> cargos) {
		wiring.clear();
		this.cargos.clear();
		this.cargos.addAll(cargos);
		for (int i = 0; i < cargos.size(); i++) {
			wiring.add(i); // set default wiring
		}
		createChildren();
	}

	private void createLayout() {
		final GridLayout layout = new GridLayout();

		layout.numColumns = 4;
		// layout.makeColumnsEqualWidth = false;

		setLayout(layout);
	}

	//TODO fix this, it's not quite correct.
	private class Highlight {
		private final Label idLabel, leftLabel, rightLabel;
		private final int index;
		private final WiringDiagram diagram;

		public Highlight(WiringDiagram diagram, Label idLabel, Label leftLabel,
				Label rightLabel, int index) {
			super();
			this.diagram = diagram;
			this.idLabel = idLabel;
			this.leftLabel = leftLabel;
			this.rightLabel = rightLabel;
			this.index = index;
		}

		public void activate() {
			diagram.setWireColor(index,
					Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
			diagram.redraw();
		}

		public void deactivate() {
			diagram.setWireColor(index,
					Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			diagram.redraw();
		}
	}

	private void createChildren() {
		final List<Label> lhsLabels = new ArrayList<Label>(cargos.size());
		// final Composite labels = new Composite(this, getStyle() &
		// ~SWT.BORDER);
		// final Composite leftHandSide = new Composite(this, getStyle()
		// & ~SWT.BORDER);
		WiringDiagram wiringDiagram = null;
		// final Composite rightHandSide = new Composite(this, getStyle()
		// & ~SWT.BORDER);

		// labels.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));
		// leftHandSide
		// .setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		// rightHandSide.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
		// true));

		// leftHandSide.setLayout(new GridLayout());
		// rightHandSide.setLayout(new GridLayout());
		// labels.setLayout(new GridLayout());

		final MouseTrackListener highlighter = new MouseTrackAdapter() {
			private Highlight oldHighlight = null;

			@Override
			public void mouseEnter(final MouseEvent e) {
				final Highlight newHighlight = (Highlight) e.widget.getData();
				if (oldHighlight != null) {
					oldHighlight.deactivate();
				}
				newHighlight.activate();
				oldHighlight = newHighlight;
			}

			@Override
			public void mouseExit(final MouseEvent e) {
				if (oldHighlight != null) {
					oldHighlight.deactivate();
					oldHighlight = null;
				}
			}
		};

		int index = 0;
		for (final Cargo cargo : cargos) {

			final Label idLabel = new Label(this, SWT.RIGHT | SWT.BORDER);
			final Label loadLabel = new Label(this, SWT.LEFT | SWT.BORDER);

			if (wiringDiagram == null) {
				wiringDiagram = new WiringDiagram(this, getStyle()
						& ~SWT.BORDER) {
					@Override
					protected List<Float> getTerminalPositions() {
						final float littleOffset = getBounds().y;
						final ArrayList<Float> vMidPoints = new ArrayList<Float>(
								cargos.size());
						// get vertical coordinates of labels
						for (final Label l : lhsLabels) {
							final Rectangle lbounds = l.getBounds();
							vMidPoints.add(-littleOffset + lbounds.y
									+ lbounds.height / 2.0f);
						}
						return vMidPoints;
					}

					@Override
					protected void wiringChanged(final List<Integer> newWiring) {
						WiringComposite.this.wiring.clear();
						WiringComposite.this.wiring.addAll(newWiring);
						final Color red = Display.getCurrent().getSystemColor(
								SWT.COLOR_RED);
						final Color green = Display.getCurrent()
								.getSystemColor(SWT.COLOR_GREEN);
						for (int i = 0; i < newWiring.size(); i++) {
							setLeftTerminalColor(i, red);
							setRightTerminalColor(i, red);
						}
						for (int i = 0; i < newWiring.size(); i++) {
							final int j = newWiring.get(i);
							if (j == -1)
								continue;
							setLeftTerminalColor(i, green);
							setRightTerminalColor(j, green);
						}
						WiringComposite.this.notifyListeners(SWT.Modify,
								new Event());
					}
				};
			}

			// wiring diagram is tall
			wiringDiagram.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 1, cargos.size()));

			final Label dischargeLabel = new Label(this, SWT.LEFT | SWT.BORDER);
			final String id = cargo.getId();

			// idLabel.setBackground(Display.getCurrent().getSystemColor(
			// SWT.COLOR_WHITE));
			//
			// loadLabel.setBackground(Display.getCurrent().getSystemColor(
			// SWT.COLOR_WHITE));
			//
			// dischargeLabel.setBackground(Display.getCurrent().getSystemColor(
			// SWT.COLOR_WHITE));

			lhsLabels.add(loadLabel);

			idLabel.setText(id);
			loadLabel.setText(cargo.getLoadSlot().getPort().getName());
			dischargeLabel
					.setText(cargo.getDischargeSlot().getPort().getName());

			idLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			loadLabel
					.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			dischargeLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false,
					false));

			idLabel.addMouseTrackListener(highlighter);
			loadLabel.addMouseTrackListener(highlighter);
			dischargeLabel.addMouseTrackListener(highlighter);

			final Highlight h = new Highlight(wiringDiagram, idLabel,
					loadLabel, dischargeLabel, index);
			
			idLabel.setData(h);
			loadLabel.setData(h);
			dischargeLabel.setData(h);

			index++;
		}

		wiringDiagram.setWiring(wiring);
		final ArrayList<Pair<Color, Color>> terminalColors = new ArrayList<Pair<Color, Color>>();
		final ArrayList<Color> wireColors = new ArrayList<Color>();
		final Color green = Display.getCurrent()
				.getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent()
				.getSystemColor(SWT.COLOR_BLACK);
		for (final Cargo cargo : cargos) {
			terminalColors.add(new Pair<Color, Color>(green, green));
			wireColors.add(black);
		}
		wiringDiagram.setWireColors(wireColors);
		wiringDiagram.setTerminalColors(terminalColors);
	}

	/*
	 * TODO highlight lines when mouseover labels TODO feedback validation state
	 * to containing dialog TODO generate command to apply changes.
	 */

	/**
	 * @return
	 */
	public boolean isWiringFeasible() {
		return wiring.indexOf(-1) == -1;
	}

	public Command createApplyCommand(final EditingDomain domain) {
		final CompoundCommand command = new CompoundCommand();
		for (int i = 0; i < wiring.size(); i++) {
			int newTail = wiring.get(i);
			if (newTail != i) {
				final Slot slot = cargos.get(newTail).getDischargeSlot();
				final Cargo cargo = cargos.get(i);
				command.append(SetCommand.create(domain, cargo,
						CargoPackage.eINSTANCE.getCargo_DischargeSlot(), slot));
			}
		}
		return command;
	}
}
