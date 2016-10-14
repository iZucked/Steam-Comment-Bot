/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.undoredo.commands;

public interface IUndoRedoCommand {

    /**
     * Undoes an event in the chart. This should put the event back to the state it was <b>prior</b> to the event taking place. 
     */
    void undo();
    
    /**
     * Redoes an event in the chart. This should put the event back to the state it was <b>after</b> the event took place.
     */
    void redo();
    
    /**
     * Called when the event is about to be destroyed. If any resources need to be cleaned up you should do so here.
     */
    void dispose();
    
}
