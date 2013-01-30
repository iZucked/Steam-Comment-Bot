/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

/**
 * A class based on CCombo which lets you put whatever you like in the dropdown box.
 */
public abstract class PopupComposite extends Composite {

	Composite inline;
	// List list;
	int visibleItemCount = 5;
	Shell popup;
	Button arrow;
	boolean hasFocus;
	Listener listener, filter;
	Color foreground, background;
	Font font;
	Shell _shell;

	static final String PACKAGE_PREFIX = "scenario.presentation.cargoeditor.widgets."; //$NON-NLS-1$

	/**
	 * Constructs a new instance of this class given its parent and a style value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class <code>SWT</code> which is applicable to instances of this class, or must be built by <em>bitwise OR</em>'ing together (that
	 * is, using the <code>int</code> "|" operator) two or more of those <code>SWT</code> style constants. The class description lists the style constants that are applicable to the class. Style bits
	 * are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot be null)
	 * @param style
	 *            the style of widget to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
	 *                </ul>
	 * 
	 * @see SWT#BORDER
	 * @see SWT#READ_ONLY
	 * @see SWT#FLAT
	 * @see Widget#getStyle()
	 */
	public PopupComposite(final Composite parent, int style) {
		super(parent, style = checkStyle(style));
		_shell = super.getShell();

		int textStyle = SWT.SINGLE;
		if ((style & SWT.READ_ONLY) != 0) {
			textStyle |= SWT.READ_ONLY;
		}
		if ((style & SWT.FLAT) != 0) {
			textStyle |= SWT.FLAT;
		}
		inline = createSmallWidget(this, textStyle);
		int arrowStyle = SWT.ARROW | SWT.DOWN;
		if ((style & SWT.FLAT) != 0) {
			arrowStyle |= SWT.FLAT;
		}
		arrow = new Button(this, arrowStyle);

		listener = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (isDisposed()) {
					return;
				}
				if (popup == event.widget) {
					popupEvent(event);
					return;
				}
				if (inline == event.widget) {
					// textEvent(event);
					return;
				}
				// if (list == event.widget) {
				// listEvent(event);
				// return;
				// }
				if (arrow == event.widget) {
					arrowEvent(event);
					return;
				}
				if (PopupComposite.this == event.widget) {
					comboEvent(event);
					return;
				}
				if (getShell() == event.widget) {
					getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (isDisposed()) {
								return;
							}
							handleFocus(SWT.FocusOut);
						}
					});
				}
			}
		};
		filter = new Listener() {
			@Override
			public void handleEvent(final Event event) {
				if (isDisposed()) {
					return;
				}
				if (event.type == SWT.Selection) {
					if (event.widget instanceof ScrollBar) {
						handleScroll(event);
					}
					return;
				}
				final Shell shell = ((Control) event.widget).getShell();
				if (shell == PopupComposite.this.getShell()) {
					handleFocus(SWT.FocusOut);
				}
			}
		};

		final int[] comboEvents = { SWT.Dispose, SWT.FocusIn, SWT.Move, SWT.Resize };
		for (int i = 0; i < comboEvents.length; i++) {
			this.addListener(comboEvents[i], listener);
		}

		final int[] textEvents = { SWT.DefaultSelection, SWT.FocusIn, SWT.Selection };
		for (int i = 0; i < textEvents.length; i++) {
			inline.addListener(textEvents[i], listener);
		}

		final int[] arrowEvents = { SWT.DragDetect, SWT.MouseDown, SWT.MouseEnter, SWT.MouseExit, SWT.MouseHover, SWT.MouseMove, SWT.MouseUp, SWT.MouseWheel, SWT.Selection, SWT.FocusIn };
		for (int i = 0; i < arrowEvents.length; i++) {
			arrow.addListener(arrowEvents[i], listener);
		}

		createPopup();
		// initAccessible();
	}

	static int checkStyle(final int style) {
		final int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		return SWT.NO_FOCUS | (style & mask);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when the user changes the receiver's selection, by sending it one of the messages defined in the
	 * <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the combo's list selection changes. <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the user changes the receiver's selection
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		final TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	void arrowEvent(final Event event) {
		switch (event.type) {
		case SWT.FocusIn: {
			handleFocus(SWT.FocusIn);
			break;
		}
		case SWT.DragDetect:
		case SWT.MouseDown:
		case SWT.MouseUp:
		case SWT.MouseMove:
		case SWT.MouseEnter:
		case SWT.MouseExit:
		case SWT.MouseHover: {
			final Point pt = getDisplay().map(arrow, this, event.x, event.y);
			event.x = pt.x;
			event.y = pt.y;
			notifyListeners(event.type, event);
			event.type = SWT.None;
			break;
		}
		case SWT.MouseWheel: {
			final Point pt = getDisplay().map(arrow, this, event.x, event.y);
			event.x = pt.x;
			event.y = pt.y;
			notifyListeners(SWT.MouseWheel, event);
			event.type = SWT.None;
			if (isDisposed()) {
				break;
			}
			if (!event.doit) {
				break;
			}
			if (event.count != 0) {
				event.doit = false;
				// int oldIndex = getSelectionIndex();
				// if (event.count > 0) {
				// select(Math.max(oldIndex - 1, 0));
				// } else {
				// select(Math.min(oldIndex + 1, getItemCount() - 1));
				// }
				// if (oldIndex != getSelectionIndex()) {
				// Event e = new Event();
				// e.time = event.time;
				// e.stateMask = event.stateMask;
				// notifyListeners(SWT.Selection, e);
				// }
				if (isDisposed()) {
					break;
				}
			}
			break;
		}
		case SWT.Selection: {
			inline.setFocus();
			dropDown(!isDropped());
			break;
		}
		}
	}

	@Override
	protected void checkSubclass() {
		// String name = getClass().getName();
		// int index = name.lastIndexOf('.');
		// if (!name.substring(0, index + 1).equals(PACKAGE_PREFIX)) {
		// SWT.error(SWT.ERROR_INVALID_SUBCLASS);
		// }
	}

	/**
	 * Sets the selection in the receiver's text field to an empty selection starting just before the first character. If the text field is editable, this has the effect of placing the i-beam at the
	 * start of the text.
	 * <p>
	 * Note: To clear the selected items in the receiver's list, use <code>deselectAll()</code>.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #deselectAll
	 */
	public void clearSelection() {
		checkWidget();
		// text.clearSelection();
	}

	void comboEvent(final Event event) {
		switch (event.type) {
		case SWT.Dispose:
			removeListener(SWT.Dispose, listener);
			notifyListeners(SWT.Dispose, event);
			event.type = SWT.None;

			if ((popup != null) && !popup.isDisposed()) {
				// list.removeListener(SWT.Dispose, listener);
				popup.dispose();
			}
			final Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			popup = null;
			inline = null;
			arrow = null;
			_shell = null;
			break;
		case SWT.FocusIn:
			final Control focusControl = getDisplay().getFocusControl();
			if (focusControl == arrow /* || focusControl == list */) {
				return;
			}
			if (isDropped()) {
				// list.setFocus();
			} else {
				inline.setFocus();
			}
			break;
		case SWT.Move:
			dropDown(false);
			break;
		case SWT.Resize:
			internalLayout(false);
			break;
		}
	}

	@Override
	public Point computeSize(final int wHint, final int hHint, final boolean changed) {
		checkWidget();
		int width = 0, height = 0;
		// String[] items = list.getItems();

		final Point textSize = inline.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		final Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		// Point listSize = list.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
		final int borderWidth = getBorderWidth();

		height = Math.max(textSize.y, arrowSize.y);
		width = textSize.x + 4 + arrowSize.x + (2 * borderWidth);
		if (wHint != SWT.DEFAULT) {
			width = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			height = hHint;
		}
		return new Point(width + (2 * borderWidth), height + (2 * borderWidth));
	}

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public void copy() {
		checkWidget();
		// text.copy();
	}

	protected abstract void createPopupContents(final Shell popup);

	protected abstract Composite createSmallWidget(Composite parent, int style);

	void createPopup() {
		// create shell and list
		popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);

		// list = new List(popup, listStyle);
		// if (font != null)
		// list.setFont(font);
		// if (foreground != null)
		// list.setForeground(foreground);
		// if (background != null)
		// list.setBackground(background);

		createPopupContents(popup);

		final int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };
		for (int i = 0; i < popupEvents.length; i++) {
			popup.addListener(popupEvents[i], listener);
			// int[] listEvents = { SWT.MouseUp, SWT.Selection, SWT.Traverse,
			// SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.Dispose };
			// for (int i = 0; i < listEvents.length; i++)
			// list.addListener(listEvents[i], listener);
		}

		// if (items != null)
		// list.setItems(items);
		// if (selectionIndex != -1)
		// list.setSelection(selectionIndex);
	}

	/**
	 * Cuts the selected text.
	 * <p>
	 * The current selection is first copied to the clipboard and then deleted from the widget.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public void cut() {
		checkWidget();

	}

	void dropDown(final boolean drop) {
		if (drop == isDropped()) {
			return;
		}
		final Display display = getDisplay();
		if (!drop) {
			display.removeFilter(SWT.Selection, filter);
			popup.setVisible(false);
			if (!isDisposed() && isFocusControl()) {
				inline.setFocus();
			}
			return;
		}
		// if (!isVisible())
		// return;
		if (getShell() != popup.getParent()) {
			// String[] items = list.getItems();
			// int selectionIndex = list.getSelectionIndex();
			// list.removeListener(SWT.Dispose, listener);
			popup.dispose();
			popup = null;
			// list = null;
			createPopup();
		}

		// Point size = getSize();
		// int itemCount = list.getItemCount();
		// itemCount = (itemCount == 0) ? visibleItemCount : Math.min(
		// visibleItemCount, itemCount);
		// int itemHeight = list.getItemHeight() * itemCount;
		// Point listSize = list.computeSize(SWT.DEFAULT, itemHeight, false);
		// list.setBounds(1, 1, Math.max(size.x - 2, listSize.x), listSize.y);

		// int index = list.getSelectionIndex();
		// if (index != -1)
		// list.setTopIndex(index);
		popup.pack();// autosize?
		final Rectangle listRect = popup.getBounds();
		final Rectangle parentRect = display.map(getParent(), null, getBounds());
		final Point comboSize = getSize();
		final Rectangle displayRect = getMonitor().getClientArea();
		final int width = Math.max(comboSize.x, listRect.width + 2);
		final int height = listRect.height + 2;
		int x = parentRect.x;
		int y = parentRect.y + comboSize.y;
		if ((y + height) > (displayRect.y + displayRect.height)) {
			y = parentRect.y - height;
		}
		if ((x + width) > (displayRect.x + displayRect.width)) {
			x = (displayRect.x + displayRect.width) - listRect.width;
		}
		popup.setBounds(x, y, width, height);
		popup.setVisible(true);
		if (isFocusControl()) {
			popup.getChildren()[0].setFocus();
		}

		popup.open();

		/*
		 * Add a filter to listen to scrolling of the parent composite, when the drop-down is visible. Remove the filter when drop-down is not visible.
		 */
		display.removeFilter(SWT.Selection, filter);
		display.addFilter(SWT.Selection, filter);
	}

	/*
	 * Return the Label immediately preceding the receiver in the z-order, or null if none.
	 */
	String getAssociatedLabel() {
		final Control[] siblings = getParent().getChildren();
		for (int i = 0; i < siblings.length; i++) {
			if (siblings[i] == this) {
				if (i > 0) {
					final Control sibling = siblings[i - 1];
					if (sibling instanceof Label) {
						return ((Label) sibling).getText();
					}
					if (sibling instanceof CLabel) {
						return ((CLabel) sibling).getText();
					}
				}
				break;
			}
		}
		return null;
	}

	@Override
	public Control[] getChildren() {
		checkWidget();
		return new Control[0];
	}

	/**
	 * Gets the editable state.
	 * 
	 * @return whether or not the receiver is editable
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public boolean getEditable() {
		checkWidget();
		// return text.getEditable();
		return true;
	}

	/**
	 * Returns <code>true</code> if the receiver's list is visible, and <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition makes the receiver not visible, this method may still indicate that it is considered visible even though it may not
	 * actually be showing.
	 * </p>
	 * 
	 * @return the receiver's list's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.4
	 */
	public boolean getListVisible() {
		checkWidget();
		return isDropped();
	}

	@Override
	public Menu getMenu() {
		return inline.getMenu();
	}

	@Override
	public Shell getShell() {
		checkWidget();
		final Shell shell = super.getShell();
		if (shell != _shell) {
			if ((_shell != null) && !_shell.isDisposed()) {
				_shell.removeListener(SWT.Deactivate, listener);
			}
			_shell = shell;
		}
		return _shell;
	}

	@Override
	public int getStyle() {
		final int style = super.getStyle();
		// style &= ~SWT.READ_ONLY;
		// if (!text.getEditable())
		// style |= SWT.READ_ONLY;
		return style;
	}

	void handleFocus(final int type) {
		switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) {
				return;
			}
			// if (getEditable())
			// text.selectAll();
			hasFocus = true;
			final Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			shell.addListener(SWT.Deactivate, listener);
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			display.addFilter(SWT.FocusIn, filter);
			final Event e = new Event();
			notifyListeners(SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus) {
				return;
			}
			final Control focusControl = getDisplay().getFocusControl();
			if ((focusControl == arrow // || focusControl == list
					)
					|| (focusControl == inline)) {
				return;
			}
			hasFocus = false;
			final Shell shell = getShell();
			shell.removeListener(SWT.Deactivate, listener);
			final Display display = getDisplay();
			display.removeFilter(SWT.FocusIn, filter);
			final Event e = new Event();
			notifyListeners(SWT.FocusOut, e);
			break;
		}
		}
	}

	void handleScroll(final Event event) {
		final ScrollBar scrollBar = (ScrollBar) event.widget;
		final Control scrollableParent = scrollBar.getParent();
		// if (scrollableParent.equals(list))
		// return;
		if (isParentScrolling(scrollableParent)) {
			dropDown(false);
		}
	}

	boolean isDropped() {
		return popup.getVisible();
	}

	private boolean containsFocusControl(final Composite c) {
		if (c.isFocusControl()) {
			return true;
		}
		for (final Control ch : c.getChildren()) {
			if (ch instanceof Composite) {
				if (containsFocusControl((Composite) ch)) {
					return true;
				}
			} else if (ch.isFocusControl()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isFocusControl() {
		checkWidget();
		if (inline.isFocusControl() || arrow.isFocusControl()
		/* || list.isFocusControl() */|| containsFocusControl(popup)) {
			return true;
		}

		return super.isFocusControl();
	}

	boolean isParentScrolling(final Control scrollableParent) {
		Control parent = this.getParent();
		while (parent != null) {
			if (parent.equals(scrollableParent)) {
				return true;
			}
			parent = parent.getParent();
		}
		return false;
	}

	void internalLayout(final boolean changed) {
		if (isDropped()) {
			dropDown(false);
		}
		final Rectangle rect = getClientArea();
		final int width = rect.width;
		final int height = rect.height;
		final Point arrowSize = arrow.computeSize(SWT.DEFAULT, height, changed);
		inline.setBounds(0, 0, width - arrowSize.x, height);
		arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
	}

	void popupEvent(final Event event) {
		switch (event.type) {
		// case SWT.Paint:
		// // draw black rectangle around list
		// Rectangle listRect = list.getBounds();
		// Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		// event.gc.setForeground(black);
		// event.gc.drawRectangle(0, 0, listRect.width + 1,
		// listRect.height + 1);
		// break;
		case SWT.Close:
			event.doit = false;
			dropDown(false);
			break;
		case SWT.Deactivate:
			/*
			 * Bug in GTK. When the arrow button is pressed the popup control receives a deactivate event and then the arrow button receives a selection event. If we hide the popup in the deactivate
			 * event, the selection event will show it again. To prevent the popup from showing again, we will let the selection event of the arrow button hide the popup. In Windows, hiding the popup
			 * during the deactivate causes the deactivate to be called twice and the selection event to be disappear.
			 */
			if (!"carbon".equals(SWT.getPlatform())) {
				final Point point = arrow.toControl(getDisplay().getCursorLocation());
				final Point size = arrow.getSize();
				final Rectangle rect = new Rectangle(0, 0, size.x, size.y);
				if (!rect.contains(point)) {
					dropDown(false);
				}
			} else {
				dropDown(false);
			}
			break;
		}
	}

	@Override
	public void redraw() {
		super.redraw();
		inline.redraw();
		arrow.redraw();
		// if (popup.isVisible())
		// list.redraw();
	}

	@Override
	public void redraw(final int x, final int y, final int width, final int height, final boolean all) {
		super.redraw(x, y, width, height, true);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified when the receiver's text is modified.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(final ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		removeListener(SWT.Modify, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified when the user changes the receiver's selection.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(final SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		removeListener(SWT.Selection, listener);
		removeListener(SWT.DefaultSelection, listener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified when the control is verified.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see VerifyListener
	 * @see #addVerifyListener
	 * 
	 * @since 3.3
	 */
	public void removeVerifyListener(final VerifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		removeListener(SWT.Verify, listener);
	}

	@Override
	public void setBackground(final Color color) {
		super.setBackground(color);
		background = color;
		if (inline != null) {
			inline.setBackground(color);
		}
		// if (list != null)
		// list.setBackground(color);
		if (arrow != null) {
			arrow.setBackground(color);
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		if (popup != null) {
			popup.setVisible(false);
		}
		if (inline != null) {
			inline.setEnabled(enabled);
		}
		if (arrow != null) {
			arrow.setEnabled(enabled);
		}
	}

	@Override
	public boolean setFocus() {
		checkWidget();
		if (!isEnabled() || !isVisible()) {
			return false;
		}
		if (isFocusControl()) {
			return true;
		}
		return inline.setFocus();
	}

	@Override
	public void setFont(final Font font) {
		super.setFont(font);
		this.font = font;
		inline.setFont(font);
		// list.setFont(font);
		internalLayout(true);
	}

	@Override
	public void setForeground(final Color color) {
		super.setForeground(color);
		foreground = color;
		if (inline != null) {
			inline.setForeground(color);
		}
		// if (list != null)
		// list.setForeground(color);
		if (arrow != null) {
			arrow.setForeground(color);
		}
	}

	/**
	 * Sets the layout which is associated with the receiver to be the argument which may be null.
	 * <p>
	 * Note: No Layout can be set on this Control because it already manages the size and position of its children.
	 * </p>
	 * 
	 * @param layout
	 *            the receiver's new layout or null
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setLayout(final Layout layout) {
		checkWidget();
		return;
	}

	@Override
	public void setMenu(final Menu menu) {
		inline.setMenu(menu);
	}

	@Override
	public void setToolTipText(final String string) {
		checkWidget();
		super.setToolTipText(string);
		arrow.setToolTipText(string);
		inline.setToolTipText(string);
	}

	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		/*
		 * At this point the widget may have been disposed in a FocusOut event. If so then do not continue.
		 */
		if (isDisposed()) {
			return;
		}
		// TEMPORARY CODE
		if ((popup == null) || popup.isDisposed()) {
			return;
		}
		if (!visible) {
			popup.setVisible(false);
		}
	}

	/**
	 * Sets the number of items that are visible in the drop down portion of the receiver's list.
	 * 
	 * @param count
	 *            the new number of items to be visible
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public void setVisibleItemCount(final int count) {
		checkWidget();
		if (count < 0) {
			return;
		}
		visibleItemCount = count;
	}

	String stripMnemonic(final String string) {
		int index = 0;
		final int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&')) {
				index++;
			}
			if (++index >= length) {
				return string;
			}
			if (string.charAt(index) != '&') {
				return string.substring(0, index - 1) + string.substring(index, length);
			}
			index++;
		} while (index < length);
		return string;
	}

	//
	// void textEvent(Event event) {
	// switch (event.type) {
	// case SWT.FocusIn: {
	// handleFocus(SWT.FocusIn);
	// break;
	// }
	// case SWT.DefaultSelection: {
	// dropDown(false);
	// Event e = new Event();
	// e.time = event.time;
	// e.stateMask = event.stateMask;
	// notifyListeners(SWT.DefaultSelection, e);
	// break;
	// }
	// case SWT.DragDetect:
	// case SWT.MouseDoubleClick:
	// case SWT.MouseMove:
	// case SWT.MouseEnter:
	// case SWT.MouseExit:
	// case SWT.MouseHover: {
	// Point pt = getDisplay().map(text, this, event.x, event.y);
	// event.x = pt.x;
	// event.y = pt.y;
	// notifyListeners(event.type, event);
	// event.type = SWT.None;
	// break;
	// }
	// case SWT.KeyDown: {
	// Event keyEvent = new Event();
	// keyEvent.time = event.time;
	// keyEvent.character = event.character;
	// keyEvent.keyCode = event.keyCode;
	// keyEvent.keyLocation = event.keyLocation;
	// keyEvent.stateMask = event.stateMask;
	// notifyListeners(SWT.KeyDown, keyEvent);
	// if (isDisposed())
	// break;
	// event.doit = keyEvent.doit;
	// if (!event.doit)
	// break;
	// if (event.keyCode == SWT.ARROW_UP
	// || event.keyCode == SWT.ARROW_DOWN) {
	// event.doit = false;
	// if ((event.stateMask & SWT.ALT) != 0) {
	// boolean dropped = isDropped();
	// text.selectAll();
	// if (!dropped)
	// setFocus();
	// dropDown(!dropped);
	// break;
	// }
	//
	// int oldIndex = getSelectionIndex();
	// if (event.keyCode == SWT.ARROW_UP) {
	// select(Math.max(oldIndex - 1, 0));
	// } else {
	// select(Math.min(oldIndex + 1, getItemCount() - 1));
	// }
	// if (oldIndex != getSelectionIndex()) {
	// Event e = new Event();
	// e.time = event.time;
	// e.stateMask = event.stateMask;
	// notifyListeners(SWT.Selection, e);
	// }
	// if (isDisposed())
	// break;
	// }
	//
	// // Further work : Need to add support for incremental search in
	// // pop up list as characters typed in text widget
	// break;
	// }
	// case SWT.KeyUp: {
	// Event e = new Event();
	// e.time = event.time;
	// e.character = event.character;
	// e.keyCode = event.keyCode;
	// e.keyLocation = event.keyLocation;
	// e.stateMask = event.stateMask;
	// notifyListeners(SWT.KeyUp, e);
	// event.doit = e.doit;
	// break;
	// }
	// case SWT.MenuDetect: {
	// Event e = new Event();
	// e.time = event.time;
	// notifyListeners(SWT.MenuDetect, e);
	// break;
	// }
	// case SWT.Modify: {
	// list.deselectAll();
	// Event e = new Event();
	// e.time = event.time;
	// notifyListeners(SWT.Modify, e);
	// break;
	// }
	// case SWT.MouseDown: {
	// Point pt = getDisplay().map(text, this, event.x, event.y);
	// Event mouseEvent = new Event();
	// mouseEvent.button = event.button;
	// mouseEvent.count = event.count;
	// mouseEvent.stateMask = event.stateMask;
	// mouseEvent.time = event.time;
	// mouseEvent.x = pt.x;
	// mouseEvent.y = pt.y;
	// notifyListeners(SWT.MouseDown, mouseEvent);
	// if (isDisposed())
	// break;
	// event.doit = mouseEvent.doit;
	// if (!event.doit)
	// break;
	// if (event.button != 1)
	// return;
	// if (text.getEditable())
	// return;
	// boolean dropped = isDropped();
	// text.selectAll();
	// if (!dropped)
	// setFocus();
	// dropDown(!dropped);
	// break;
	// }
	// case SWT.MouseUp: {
	// Point pt = getDisplay().map(text, this, event.x, event.y);
	// Event mouseEvent = new Event();
	// mouseEvent.button = event.button;
	// mouseEvent.count = event.count;
	// mouseEvent.stateMask = event.stateMask;
	// mouseEvent.time = event.time;
	// mouseEvent.x = pt.x;
	// mouseEvent.y = pt.y;
	// notifyListeners(SWT.MouseUp, mouseEvent);
	// if (isDisposed())
	// break;
	// event.doit = mouseEvent.doit;
	// if (!event.doit)
	// break;
	// if (event.button != 1)
	// return;
	// if (text.getEditable())
	// return;
	// text.selectAll();
	// break;
	// }
	// case SWT.MouseWheel: {
	// notifyListeners(SWT.MouseWheel, event);
	// event.type = SWT.None;
	// if (isDisposed())
	// break;
	// if (!event.doit)
	// break;
	// if (event.count != 0) {
	// event.doit = false;
	// int oldIndex = getSelectionIndex();
	// if (event.count > 0) {
	// select(Math.max(oldIndex - 1, 0));
	// } else {
	// select(Math.min(oldIndex + 1, getItemCount() - 1));
	// }
	// if (oldIndex != getSelectionIndex()) {
	// Event e = new Event();
	// e.time = event.time;
	// e.stateMask = event.stateMask;
	// notifyListeners(SWT.Selection, e);
	// }
	// if (isDisposed())
	// break;
	// }
	// break;
	// }
	// case SWT.Traverse: {
	// switch (event.detail) {
	// case SWT.TRAVERSE_ARROW_PREVIOUS:
	// case SWT.TRAVERSE_ARROW_NEXT:
	// // The enter causes default selection and
	// // the arrow keys are used to manipulate the list contents so
	// // do not use them for traversal.
	// event.doit = false;
	// break;
	// case SWT.TRAVERSE_TAB_PREVIOUS:
	// event.doit = traverse(SWT.TRAVERSE_TAB_PREVIOUS);
	// event.detail = SWT.TRAVERSE_NONE;
	// return;
	// }
	// Event e = new Event();
	// e.time = event.time;
	// e.detail = event.detail;
	// e.doit = event.doit;
	// e.character = event.character;
	// e.keyCode = event.keyCode;
	// e.keyLocation = event.keyLocation;
	// notifyListeners(SWT.Traverse, e);
	// event.doit = e.doit;
	// event.detail = e.detail;
	// break;
	// }
	// case SWT.Verify: {
	// Event e = new Event();
	// e.text = event.text;
	// e.start = event.start;
	// e.end = event.end;
	// e.character = event.character;
	// e.keyCode = event.keyCode;
	// e.keyLocation = event.keyLocation;
	// e.stateMask = event.stateMask;
	// notifyListeners(SWT.Verify, e);
	// event.text = e.text;
	// event.doit = e.doit;
	// break;
	// }
	// }
	// }

	@Override
	public boolean traverse(final int event) {
		/*
		 * When the traverse event is sent to the CCombo, it will create a list of controls to tab to next. Since the CCombo is a composite, the next control is the Text field which is a child of the
		 * CCombo. It will set focus to the text field which really is itself. So, call the traverse next events directly on the text.
		 */
		if ((event == SWT.TRAVERSE_ARROW_NEXT) || (event == SWT.TRAVERSE_TAB_NEXT)) {
			return inline.traverse(event);
		}
		return super.traverse(event);
	}
}
