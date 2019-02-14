/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.minimaxlabs.rnd.representation;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Interval implements Serializable{
    private double start, end;

    public Interval(ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
    }

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public double getEnd() {
        return end;
    }

    public double getStart() {
        return start;
    }
}