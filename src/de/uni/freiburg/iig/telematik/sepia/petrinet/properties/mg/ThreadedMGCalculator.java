package de.uni.freiburg.iig.telematik.sepia.petrinet.properties.mg;

import java.awt.BorderLayout;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.component.ExecutorLabel;
import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.boundedness.BoundednessException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public class ThreadedMGCalculator<	P extends AbstractPlace<F,S>, 
									T extends AbstractTransition<F,S>, 
									F extends AbstractFlowRelation<P,T,S>, 
									M extends AbstractMarking<S>, 
									S extends Object> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,AbstractMarkingGraph<M,S,?,?>>
																				   implements MGCalculator<P,T,F,M,S>{
	
	public ThreadedMGCalculator(MGConstructorCallableGenerator<P,T,F,M,S> generator){
		super(generator);
	}
	
	public ThreadedMGCalculator(AbstractPetriNet<P,T,F,M,S> petriNet){
		this(new MGConstructorCallableGenerator<P,T,F,M,S>(petriNet));
	}
	
	@Override
	protected MGConstructorCallableGenerator<P,T,F,M,S> getGenerator() {
		return (MGConstructorCallableGenerator<P,T,F,M,S>) super.getGenerator();
	}

	@Override
	public AbstractMarkingGraph<M,S,?,?> getMarkingGraph() throws MarkingGraphException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new MarkingGraphException("Marking graph construction cancelled.", e);
		} catch (InterruptedException e) {
			throw new MarkingGraphException("Marking graph construction interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new MarkingGraphException("Exception during marking graph construction.\n" + e.getMessage(), e);
			}
			if(cause instanceof StateSpaceException){
				throw new MarkingGraphException("Exception during marking graph construction.\nCannot build whole marking graph", cause);
			}
			if(cause instanceof MarkingGraphException){
				throw (MarkingGraphException) cause;
			}
			throw new MarkingGraphException("Exception during marking graph construction.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new MarkingGraphException("Exception during marking graph construction.\n" + e.getMessage(), e);
		}
	}
	
	@Override
	public AbstractCallable<AbstractMarkingGraph<M,S,?,?>> getCallable() {
		return new MGConstructorCallable<P,T,F,M,S>(getGenerator());
	}
	
	@Override
	public void runCalculation() {
		setUpAndRun();
	}

	public static void main(String[] args) throws BoundednessException {
		PTNet net = new PTNet();
		net.addPlace("p0");
		net.addTransition("t0");
		net.addFlowRelationPT("p0", "t0");
		net.addFlowRelationTP("t0", "p0", 2);
		PTMarking marking = new PTMarking();
		marking.set("p0", 100);
		net.setInitialMarking(marking);
//		System.out.println(net.getBoundedness());
		ExecutorLabel label = new ExecutorLabel();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
//		label.setExecutor(net.checkBoundedness());
		DisplayFrame frame = new DisplayFrame(panel, true, true);
//		System.out.println(net.getBoundedness());
	}

}