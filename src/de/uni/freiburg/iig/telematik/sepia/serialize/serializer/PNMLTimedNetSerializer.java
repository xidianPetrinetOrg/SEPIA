package de.uni.freiburg.iig.telematik.sepia.serialize.serializer;

import org.w3c.dom.Element;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractTimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;

public class PNMLTimedNetSerializer<P extends AbstractTimedPlace<F>, 
T extends AbstractTimedTransition<F>, 
F extends AbstractTimedFlowRelation<P,T>, 
M extends AbstractTimedMarking,
N extends AbstractTimedNet<P, T, F, M>,
G extends AbstractTimedNetGraphics<P,T,F,M>> extends PNMLPTNetSerializer<P, T, F, M, N , G> {
	
	public static final String resourceContext = "resourcecontext";
	public static final String timeContext = "timecontext";
	public static final String processContext = "processcontext";

	public PNMLTimedNetSerializer(AbstractGraphicalTimedNet<P, T, F, M, N, G> petriNet) {
		super(petriNet);
		
	}
	
	public PNMLTimedNetSerializer(N net){
		super(net);
	}
	
	@Override
	public NetType acceptedNetType() {
		return NetType.RTPTnet;
	}
	
	@Override
	protected void addHeader() {
		// Add resource context name
		Element resourceContextElement = getSupport().createElement(resourceContext);
		resourceContextElement.setTextContent(petriNet.getResourceContextName());
		getSupport().getNetElement().appendChild(resourceContextElement);
		
		// Add time context name
		Element timeContextElement = getSupport().createElement(timeContext);
		timeContextElement.setTextContent(petriNet.getTimeContextName());
		getSupport().getNetElement().appendChild(timeContextElement);
		
		// Add processContext (AccessContext)
		Element processContextElement = getSupport().createElement(processContext);
		processContextElement.setTextContent(petriNet.getProcesContextName());
		getSupport().getNetElement().appendChild(processContextElement);
	}

}