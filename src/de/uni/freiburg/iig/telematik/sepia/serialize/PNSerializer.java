package de.uni.freiburg.iig.telematik.sepia.serialize;

import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.ParameterException.ErrorCode;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;

public abstract class PNSerializer<P extends AbstractPlace<F,S>, 
   							  	   T extends AbstractTransition<F,S>, 
   							  	   F extends AbstractFlowRelation<P,T,S>, 
   							  	   M extends AbstractMarking<S>, 
   							  	   S extends Object,
   							  	   X extends AbstractMarkingGraphState<M, S>,
   							  	   Y extends AbstractMarkingGraphRelation<M, X, S>,
   							  	   N extends AbstractPetriNet<P,T,F,M,S,X,Y>,
							  	   G extends AbstractPNGraphics<P,T,F,M,S>> {
	
	protected N petriNet = null;
	protected G graphics = null;
	protected SerializationSupport support = null;
	
	public PNSerializer(N petriNet) {
		validatePetriNet(petriNet);
		this.petriNet = petriNet;
	}
	
	public PNSerializer(AbstractGraphicalPN<P,T,F,M,S,X,Y,N,G> petriNet) {
		Validate.notNull(petriNet);
		validatePetriNet(petriNet.getPetriNet());
		this.petriNet = petriNet.getPetriNet();
		this.graphics = petriNet.getPetriNetGraphics();
	}
	
	private void validatePetriNet(N petriNet) {
		Validate.notNull(petriNet);
		Class<?> requiredClassType = NetType.getClassType(acceptedNetType());
		if(!(requiredClassType.isAssignableFrom(petriNet.getClass()))){
			throw new ParameterException(ErrorCode.INCOMPATIBILITY, "This serializer requires nets of type \""+requiredClassType+"\"\n The given net is of type \""+petriNet.getClass()+"\"");
		}
	}
	
	public N getPetriNet(){
		return petriNet;
	}
	
	public G getGraphics(){
		return graphics;
	}
	
	public SerializationSupport getSupport(){
		return support;
	}
	
	protected boolean hasGraphics(){
		return graphics != null;
	}
	
	public abstract NetType acceptedNetType();
	
	public abstract String serialize() throws SerializationException;
	
	public abstract void serialize(String path, String fileName) throws SerializationException, IOException;
			
}
