/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTMarking;

/**
 *
 * @author richard
 */
public abstract class AbstractTimedMarking extends AbstractPTMarking {

	private static final long serialVersionUID = -5795568492094277347L;
	
	protected Map<Double, List<TokenConstraints<Integer>>> pendingActions = new HashMap<>();

	public AbstractTimedMarking() {
        super();
    }
	
	public void addPendingAction(String placeName, double time, int tokens) {
		TokenConstraints<Integer> constraint = new TokenConstraints<>(placeName, tokens);
		if (pendingActions.containsKey(time) && pendingActions.get(time) != null) {
			// time entry is available
			pendingActions.get(time).add(constraint);
		} else { // List not initialized. Create and add List
			ArrayList<TokenConstraints<Integer>> pendingActionList = new ArrayList<>();
			pendingActionList.add(constraint);
			pendingActions.put(time, pendingActionList);
		}
	}

}