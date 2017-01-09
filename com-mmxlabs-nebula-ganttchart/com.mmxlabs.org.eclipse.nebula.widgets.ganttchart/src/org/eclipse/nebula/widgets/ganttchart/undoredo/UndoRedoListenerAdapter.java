/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.undoredo;

import org.eclipse.nebula.widgets.ganttchart.undoredo.commands.IUndoRedoCommand;

/**
 * Basic implementation of {@link IUndoRedoListener}.
 * 
 * @author Emil
 *
 */
public class UndoRedoListenerAdapter implements IUndoRedoListener {

    public void canRedoChanged(boolean canRedo) {
    }

    public void canUndoChanged(boolean canUndo) {
    }

    public void commandRedone(IUndoRedoCommand command) {
    }

    public void commandUndone(IUndoRedoCommand command) {
    }

    public void undoableCommandAdded(IUndoRedoCommand command) {
    }

}
