package de.uni.freiburg.iig.telematik.sepia.petrinet;

import java.io.Serializable;

import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionListener;
import de.uni.freiburg.iig.telematik.sepia.event.TransitionListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;

/**
 * 
 * Abstract class that defines general properties for Petri net transitions.<br>
 * This class inherits basic Petri net node properties from {@link AbstractPNNode}<br>
 * and defines additional properties:<br>
 * <ul>
 * <li>Enabled state: Based on the state of connectet places,<br>
 * transitions can be enabled. Enabled transitions can fire, <br>
 * which means that they remove tokens from incoming places and put tokens in outgoing places.</li>
 * <li>Silent state: In some cases, Petri nets are modeled with additional<br>
 * non-functional transitions to reach a more compact net shape.<br>
 * To distinguish such transitions from regular net transitions, they can be set silent.</li>
 * </ul>
 * <br>
 * A transition allows {@link TransitionListener}s to register so that they can be notified on state changes,<br>
 * i.e. enabling/disabling/firing.
 * 
 * @author Thomas Stocker
 *
 * @param <E> The type of flow relations connected to the transition.
 */
public abstract class AbstractTransition<	E extends AbstractFlowRelation<? extends AbstractPlace<E,S>, 
											? extends AbstractTransition<E, S>, S>, S extends Object> 
											  extends AbstractPNNode<E> implements Serializable{

	private static final long serialVersionUID = 6881631518539919772L;
	
	/**
	 * Support class for {@link TransitionListener} handling.
	 */
	protected TransitionListenerSupport<AbstractTransition<E,S>> listenerSupport = new TransitionListenerSupport<AbstractTransition<E,S>>();
	/**
	 * Indicates the silent-state of the transition.<br>
	 * If <code>true</code>, the transition is treated as silent transition.
	 */
	protected boolean isSilent = false;
	/**
	 * Indicates the enabled-state of the transition<br>
	 * If <code>true</code>, the transition can fire.
	 */
	protected boolean enabled = true;
	
	
	//------- Constructors ----------------------------------------------------------------------------
	
//	protected AbstractTransition(){
//		super(PNNodeType.TRANSITION);
//	}
	
	/**
	 * Creates a new transition with the given name.
	 * @param name The name for the new Transition.
	 * @If the given name is <code>null</code>.
	 */
	public AbstractTransition(String name){
		super(PNNodeType.TRANSITION, name);
	}
	
	/**
	 * Creates a new transition with the given name and label.
	 * @param name The name for the new transition.
	 * @param label The label for the new transition.
	 * @If some parameters are <code>null</code>.
	 */
	public AbstractTransition(String name, String label){
		super(PNNodeType.TRANSITION, name, label);
	}
	
	/**
	 * Creates a new transition with the given name and silent-state.
	 * @param name The name for the new transition.
	 * @param isSilent The silent-state for the new transition.
	 * @If the given name is <code>null</code>.
	 */
	public AbstractTransition(String name, boolean isSilent){
		this(name);
		this.isSilent = isSilent;
	}
	
	/**
	 * Creates a new transition with the given name, label and silent-state.
	 * @param name The name for the new transition.
	 * @param label The label for the new transition.
	 * @param isSilent The silent-state for the new transition.
	 * @If some parameters are <code>null</code>.
	 */
	public AbstractTransition(String name, String label, boolean isSilent){
		this(name, label);
		this.isSilent = isSilent;
	}
	
	
	//------- Basic properties -----------------------------------------------------------------------
	
	@Override
	protected boolean addIncomingRelation(E relation){
		if(super.addIncomingRelation(relation)){
			checkState();
			return true;
		}
		return false;
	}

	@Override
	protected boolean addOutgoingRelation(E relation){
		if(super.addOutgoingRelation(relation)){
			checkState();
			return true;
		}
		return false;
	}
	
	/**
	 * Indicates if the transition is silent.<br>
	 * @return <code>true</code> if the transition is silent;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isSilent(){
		return isSilent;
	}
	
	/**
	 * Sets the silent-state of the transition.
	 * @param isSilent Desired silent-state of the transition.
	 */
	public void setSilent(boolean isSilent){
		this.isSilent = isSilent;
	}
	
	/**
	 * Indicates the enabled-state of the transition.
	 * @return <code>true</code> if the transition is enabled;<br>
	 * <code>false</code> otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
//	@Override
//	protected void setName(String name){
//		Validate.notNull(name);
//		if(!listenerSupport.requestNameChangePermission(this, name))
//			throw new ParameterException(ErrorCode.INCONSISTENCY, "A connected Petri net already contains a node with this name.\n Cancel renaming to avoid name clash.");
//		this.name = name;
//	}
	
	/**
	 * Checks if the Petri net transition is valid.<br>
	 * Subclasses may define the validity of a net transition e.g. in terms of specific constraints
	 * and must throw PNValidationExceptions in case any constraint is violated.
	 * 
	 * @throws PNValidationException
	 */
	public void checkValidity() throws PNValidationException{}
	
	
	//------- Functionality --------------------------------------------------------------------------
	
	/**
	 * Checks the enabled-state of the transition based on the state of connected places.<br>
	 * It checks if the input places contain the required tokens and the output places have enough capacity to consume produced tokens.<br>
	 * Depending on the state representation of places this may require different check routines,<br>
	 * which have to be implemented by subclasses.<br>
	 * <br>
	 * If the transition gets enabled or disabled, the transition notifies all transition listeners.
	 * 
	 * @see AbstractTransition#enoughTokensInInputPlaces()
	 * @see AbstractTransition#enoughSpaceInOutputPlaces()
	 */
	public void checkState() {
		boolean oldEnabledState = isEnabled();				
		
		enabled = true;
		if(!enoughTokensInInputPlaces()){
			enabled = false;
		}
		
		if(!enoughSpaceInOutputPlaces()){
			enabled = false;
		}
		
		if(enabled && !oldEnabledState){
			listenerSupport.notifyEnabling(new TransitionEvent<AbstractTransition<E,S>>(this));
		} else if(!enabled && oldEnabledState){
			listenerSupport.notifyDisabling(new TransitionEvent<AbstractTransition<E,S>>(this));
		}
	}
	
	/**
	 * Checks, if the input places connected to the transition contain enough tokens,<br>
	 * i.e. satisfy the token constraints of the corresponding incoming relations.
	 * 
	 * @return <code>true</code> if all token constraints are satisfied;<br>
	 * <code>false</code> otherwise.
	 */
	protected abstract boolean enoughTokensInInputPlaces();
	
	/**
	 * Checks if the output places connected to the transition can consume all produced tokens, i.e. have enough capacity.
	 * 
	 * @return <code>true</code> if all tokens can be consumed;<br>
	 * <code>false</code> otherwise.
	 */
	protected abstract boolean enoughSpaceInOutputPlaces();
	
	/**
	 * Fires the transition.<br>
	 * On firing, the transition removes tokens from incoming places and puts tokens in outgoing places.<br>
	 * Depending on the state representation of places this may require different check routines,<br>
	 * which have to be implemented by subclasses.
	 * 
	 * @throws PNException If transition is not enabled or not in a valid state.
	 */
	public void fire() throws PNException{
		if(!isEnabled())
			throw new PNException("Cannot fire transition "+this+": not enabled");
		try{
			checkValidity();
		} catch(PNValidationException e){
			throw new PNException("Cannot fire transition "+this+": not in valid state ["+e.getMessage()+"]");
		}
		

		for (E r : incomingRelations) {
			r.getPlace().removeTokens(r.getConstraint());
		}
		for (E r : outgoingRelations) {
			r.getPlace().addTokens(r.getConstraint());
		}
		notifyFiring();
	}
	
	protected void notifyFiring(){
		listenerSupport.notifyFiring(new TransitionEvent<AbstractTransition<E, S>>(this));
	}
	
	
	//------- Listener support ------------------------------------------------------------------------
	
	/**
	 * Adds a transition listener.
	 * @param listener The transition listener to add.
	 * @If the listener reference is <code>null</code>.
	 */
	public void addTransitionListener(TransitionListener<AbstractTransition<E,S>> listener){
		listenerSupport.addListener(listener);
	}
	
	/**
	 * Removes a transition listener.
	 * @param l The transition listener to remove.
	 * @If the listener reference is <code>null</code>.
	 */
	public void removeTransitionListener(TransitionListener<AbstractTransition<E,S>> l){
		listenerSupport.removeListener(l);
	}
	
	@Override
	protected abstract AbstractTransition<E,S> newInstance(String name);
	
	
	//------- hashCode and equals --------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (isSilent ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		AbstractTransition<E, S> other = (AbstractTransition<E, S>) obj;
		if (isSilent != other.isSilent)
			return false;
		return true;
	}
	
	
	//------- clone ----------------------------------------------------------------------------------
	

	@Override
	public AbstractTransition<E,S> clone() {
		@SuppressWarnings("unchecked")
		AbstractTransition<E,S> result = (AbstractTransition<E,S>) super.clone();
		result.setSilent(isSilent());
		return result;
	}

}
