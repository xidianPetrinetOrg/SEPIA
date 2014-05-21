package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractDeclassificationTransition;


public class DeclassificationTransition extends AbstractDeclassificationTransition<IFNetFlowRelation>{
	
	protected DeclassificationTransition(){
		super();
	}
	
	public DeclassificationTransition(String name, String label, boolean isEmpty) {
		super(name, label, isEmpty);
	}

	public DeclassificationTransition(String name, boolean isEmpty) {
		this(name, name, isEmpty);
	}

	public DeclassificationTransition(String name, String label) {
		this(name, label, false);
	}

	public DeclassificationTransition(String name) {
		this(name, name, false);
	}

	@Override
	protected DeclassificationTransition newInstance() {
		return new DeclassificationTransition();
	}

	@Override
	public DeclassificationTransition clone() {
		return (DeclassificationTransition) super.clone();
	}

	
	
}
