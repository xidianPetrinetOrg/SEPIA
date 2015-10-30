package de.uni.freiburg.iig.telematik.sepia.parser.pnml.timedNet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractTimedNetGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.AbstractPNMLPTNetParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;
import de.uni.freiburg.iig.telematik.sepia.serialize.serializer.PNMLTimedNetSerializer;

public abstract class AbstractPNMLTimedNetParser <P extends AbstractTimedPlace<F>,
T extends AbstractTimedTransition<F>,
F extends AbstractTimedFlowRelation<P, T>,
M extends AbstractTimedMarking,
N extends AbstractTimedNet<P, T, F, M>,
G extends AbstractTimedNetGraphics<P, T, F, M>> extends AbstractPNMLPTNetParser<P, T, F, M, N, G>{

	 @Override
	    public void parseDocument(Document pnmlDocument) throws ParserException {
	
	        super.parseDocument(pnmlDocument);
	        
	        //read context names
	        net.setProcesContextName(getNodeContent(PNMLTimedNetSerializer.processContext, pnmlDocument));
	        net.setTimeContextName(getNodeContent(PNMLTimedNetSerializer.timeContext, pnmlDocument));
	        net.setResourceContextName(getNodeContent(PNMLTimedNetSerializer.resourceContext, pnmlDocument));
	        
	 }
	 
	 protected String getNodeContent(String tagName, Document pnmlDocument){
		 try{
			 NodeList nodes = pnmlDocument.getElementsByTagName(tagName);
			 Node node = nodes.item(0);
			 return node.getChildNodes().item(0).getNodeValue();
		 } catch (Exception e){
			 return "";
		 }
	 }
	
	

}