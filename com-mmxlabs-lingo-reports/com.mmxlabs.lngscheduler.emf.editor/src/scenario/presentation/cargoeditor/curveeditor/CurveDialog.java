/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.curveeditor;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import scenario.optimiser.Discount;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;

/**
 * @author Tom Hinton
 * 
 */
public class CurveDialog extends Dialog {
	private static final int ZOOM_OUT_ID = 0x2;
	private static final int ZOOM_IN_ID = 0x3;

	private CurveCanvas curveCanvas;
	private ScrolledComposite scroller;
	private DiscountCurve curve;
	private DateTime datePicker;
	private Button dateSelectionButton;

	private Date startDate = null;

	/**
	 * @param parentShell
	 */
	public CurveDialog(final Shell parentShell) {
		super(parentShell);
	}

	public DiscountCurve createNewCurve() {
		final DiscountCurve result = OptimiserFactory.eINSTANCE
				.createDiscountCurve();

		for (final Map.Entry<Integer, Double> point : curveCanvas.getPoints()
				.entrySet()) {
			final Discount d = OptimiserFactory.eINSTANCE.createDiscount();
			d.setTime(point.getKey());
			d.setDiscountFactor(point.getValue().floatValue());
			result.getDiscounts().add(d);
		}

		if (startDate != null) {
			result.setStartDate(startDate);
		}

		return result;
	}

	public int open(final DiscountCurve curve) {
		setDiscountCurve(curve);
		return super.open();
	}

	public void setDiscountCurve(final DiscountCurve curve) {
		this.curve = curve;
		if (curve == null) {
			startDate = null;
		} else {
			startDate = curve.isSetStartDate() ? curve.getStartDate() : null;
		}
		setCanvasPoints();
		setDatePicker();
	}

	/**
	 * copy points from the discount object into the curve diagram
	 */
	private void setCanvasPoints() {
		if (curveCanvas != null && curve != null) {
			final Map<Integer, Double> points = new TreeMap<Integer, Double>();
			for (final Discount d : curve.getDiscounts()) {
				points.put(d.getTime(), (double) d.getDiscountFactor());
			}
			curveCanvas.setPoints(points);
		}
	}

	private void setDatePicker() {
		if (datePicker != null) {
			if (startDate != null) {
				final Calendar c = Calendar.getInstance(TimeZone
						.getTimeZone("UTC"));
				c.clear();
				c.setTime(startDate);

				datePicker.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
				datePicker.setEnabled(true);
				dateSelectionButton.setSelection(true);
			} else {
				datePicker.setEnabled(false);
				dateSelectionButton.setSelection(false);
			}
		}
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Discount Curve Editor");
		getShell().setSize(640, 480);

		final Rectangle shellBounds = getParentShell().getBounds();
		final Point dialogSize = getShell().getSize();

		getShell().setLocation(
				shellBounds.x + (shellBounds.width - dialogSize.x) / 2,
				shellBounds.y + (shellBounds.height - dialogSize.y) / 2);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);

		final ScrolledComposite scroller = new ScrolledComposite(c, SWT.BORDER
				| SWT.H_SCROLL);
		this.scroller = scroller;
		scroller.setLayoutData(new GridData(GridData.FILL_BOTH));
		// scroller.setExpandHorizontal(true);
		// scroller.setExpandVertical(false);

		final CurveCanvas cc = new CurveCanvas(scroller, SWT.NONE);
		this.curveCanvas = cc;

		setCanvasPoints();

		scroller.setContent(cc);

		scroller.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				resizeCanvas(scroller, cc);
				// scroller.setMinWidth(newSize.x);
				// scroller.setMinHeight(newSize.y);
			}
		});

		Composite composite = new Composite(c, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		composite.setLayout(new GridLayout(4, false));

		Label lblControlclickPointsTo = new Label(composite, SWT.NONE);
		lblControlclickPointsTo.setFont(SWTResourceManager.getFont("Tahoma", 8,
				SWT.ITALIC));
		lblControlclickPointsTo.setText("Control-click points to remove them");

		Label lblStartDate = new Label(composite, SWT.NONE);
		lblStartDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));
		lblStartDate.setText("Fix Start Date:");

		final Button button = new Button(composite, SWT.CHECK);

		button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false,
				1, 1));

		final DateTime dateTime = new DateTime(composite, SWT.BORDER);
		dateTime.setEnabled(false);
		dateTime.setToolTipText("If set, the discount curve will apply from the given fixed start date. If this value is not set, the discount curve will apply from the date of the first event in the optimisation.");
		dateTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		final SelectionListener sl = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateTime.setEnabled(button.getSelection());
				if (dateTime.isEnabled()) {
					final Calendar c = Calendar.getInstance(TimeZone
							.getTimeZone("UTC"));
					c.clear();
					c.set(datePicker.getYear(), datePicker.getMonth(),
							datePicker.getDay());
					startDate = c.getTime();
				} else {
					startDate = null;
				}
			}
		};
		
		button.addSelectionListener(sl);
		dateTime.addSelectionListener(sl);
		
		this.dateSelectionButton = button;
		this.datePicker = dateTime;
		setDatePicker();
		return c;
	}

	private void resizeCanvas(final ScrolledComposite scroller,
			final CurveCanvas cc) {
		final Point size = scroller.getSize();
		final Point newSize = cc.computeSize(size.x - 4, size.y);
		if (newSize.x > size.x - 4) {
			newSize.y -= 16;
		}
		cc.setSize(newSize);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, ZOOM_IN_ID, "+", false);
		createButton(parent, ZOOM_OUT_ID, "-", false);

		super.createButtonsForButtonBar(parent);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == ZOOM_IN_ID) {
			curveCanvas.zoomIn();
			resizeCanvas(scroller, curveCanvas);
		} else if (buttonId == ZOOM_OUT_ID) {
			curveCanvas.zoomOut();
			resizeCanvas(scroller, curveCanvas);
		} else {
			super.buttonPressed(buttonId);
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		final CurveDialog c = new CurveDialog(shell);
		c.open();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/**
	 * @param editingDomain
	 * @return
	 */
	public Command createUpdateCommand(final EditingDomain editingDomain) {
		final CompoundCommand cc = new CompoundCommand();

		cc.append(DeleteCommand.create(editingDomain, curve.getDiscounts()));

		Collection<Discount> newDiscounts = new LinkedList<Discount>();

		for (final Map.Entry<Integer, Double> point : curveCanvas.getPoints()
				.entrySet()) {
			final Discount d = OptimiserFactory.eINSTANCE.createDiscount();
			d.setTime(point.getKey());
			d.setDiscountFactor(point.getValue().floatValue());
			newDiscounts.add(d);
		}

		cc.append(AddCommand.create(editingDomain, curve,
				OptimiserPackage.eINSTANCE.getDiscountCurve_Discounts(),
				newDiscounts));

		// setter for date is missing.

		if (startDate != null) {
			cc.append(SetCommand.create(editingDomain, curve,
					OptimiserPackage.eINSTANCE.getDiscountCurve_StartDate(),
					startDate));
		} else {
			cc.append(SetCommand.create(editingDomain, curve,
					OptimiserPackage.eINSTANCE.getDiscountCurve_StartDate(),
					SetCommand.UNSET_VALUE));
		}

		return cc;
	}
}
