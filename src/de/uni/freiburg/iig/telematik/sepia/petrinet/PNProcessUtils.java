package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.util.Collection;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.sewol.context.constraint.ConstraintContext;
import de.uni.freiburg.iig.telematik.sewol.context.process.ProcessContext;

public class PNProcessUtils {

	/**
	 * Creates a new context on basis of the given Petri net transitions.<br>
	 * Transitions are converted into a list of activity names.
         * @param name
	 * @param transitions Petri net transitions to be used as basis for activity names.
         * @return 
	 * @throws ParameterException If activity list is <code>null</code> or empty.
	 */
	public static ConstraintContext createProcessContext(String name, Collection<AbstractTransition<?,?>> transitions){
		ConstraintContext context = new ConstraintContext(name);
		context.setActivities(PNUtils.getLabelSetFromTransitions(transitions, false));
		return context;
	}

	
	/**
	 * Creates a new context using an RBAC access control model.<br>
	 * Users and permissions to execute transactions are randomly assigned to the given roles.<br>
	 * Each person is assigned to exactly one role.
	 * @param transitions A collection of Petri net transitions.
	 * @param originatorCount The number of desired originators.
	 * @param roles The roles to use.
	 * @return A new randomly generated Context.
	 * @throws ParameterException 
	 */
	public static ConstraintContext createRandomContext(Collection<AbstractTransition<?,?>> transitions, int originatorCount, List<String> roles){
		ProcessContext duContext = ProcessContext.createRandomContext(PNUtils.getLabelSetFromTransitions(transitions, false), originatorCount, roles);
		try {
			return ConstraintContext.newInstance(duContext);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
