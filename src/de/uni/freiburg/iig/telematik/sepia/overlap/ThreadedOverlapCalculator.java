package de.uni.freiburg.iig.telematik.sepia.overlap;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.invation.code.toval.thread.AbstractCallable;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.properties.threaded.AbstractThreadedPNPropertyChecker;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;

public class ThreadedOverlapCalculator<P extends AbstractPlace<F,S>, 
										T extends AbstractTransition<F,S>, 
										F extends AbstractFlowRelation<P,T,S>, 
										M extends AbstractMarking<S>, 
										S extends Object,
										E extends LogEntry> extends AbstractThreadedPNPropertyChecker<P,T,F,M,S,OverlapResult<E>>{
	
	protected ThreadedOverlapCalculator(OverlapCallableGenerator<P,T,F,M,S,E> generator){
		super(generator);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected OverlapCallableGenerator<P,T,F,M,S,E> getGenerator() {
		return (OverlapCallableGenerator<P,T,F,M,S,E>) super.getGenerator();
	}

	@Override
	protected AbstractCallable<OverlapResult<E>> getCallable() {
		return new OverlapCallable<P,T,F,M,S,E>(getGenerator());
	}
	
	public void runCalculation(){
		setUpAndRun();
	}
	
	public OverlapResult<E> getOverlapResult() throws OverlapException{
		try {
			return getResult();
		} catch (CancellationException e) {
			throw new OverlapException("Overlap calculation cancelled.", e);
		} catch (InterruptedException e) {
			throw new OverlapException("Overlap calculation interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new OverlapException("Exception during overlap calculation.\n" + e.getMessage(), e);
			}
			if(cause instanceof OverlapException){
				throw (OverlapException) cause;
			}
			throw new OverlapException("Exception during overlap calculation.\n" + e.getMessage(), e);
		} catch(Exception e){
			throw new OverlapException("Exception during overlap calculation.\n" + e.getMessage(), e);
		}
	}
}