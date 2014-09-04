package com.grottworkshop.diegottdie.core;

/**
 * Created by fgrott on 9/4/2014.
 */
public abstract class TransitionScreen {

    private int[] transitions;

    /**
     * Sets transitions.
     *
     * @param transitions the transitions
     */
    public void setTransitions(int[] transitions) {
        this.transitions = transitions;
    }

    /**
     * Get transitions.
     *
     * @return the int [ ]
     */
    public int[] getTransitions() { return transitions; }
}
