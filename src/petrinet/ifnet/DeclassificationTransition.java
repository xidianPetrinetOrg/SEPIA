package petrinet.ifnet;

import java.util.HashSet;
import java.util.Set;

import misc.SetUtils;
import validate.ParameterException;
import validate.Validate;
import exception.PNValidationException;

public class DeclassificationTransition extends AbstractIFNetTransition{
	
	protected IFNet sNet = null;
	
	protected DeclassificationTransition(){
		super();
	}
	
	public DeclassificationTransition(IFNet sNet, String name, String label, boolean isEmpty) throws ParameterException {
		super(name, label, isEmpty);
		this.sNet = sNet;
	}

	public DeclassificationTransition(IFNet sNet, String name, boolean isEmpty) throws ParameterException {
		this(sNet, name, name, isEmpty);
	}

	public DeclassificationTransition(IFNet sNet, String name, String label) throws ParameterException {
		this(sNet, name, label, false);
	}

	public DeclassificationTransition(IFNet sNet, String name) throws ParameterException {
		this(sNet, name, name, false);
	}
	
	@Override
	public boolean isDeclassificator() {
		return true;
	}
	
	@Override
	public void checkValidity() throws PNValidationException{
		super.checkValidity();
		
		// Property 1: Only one input and output place
		if(getIncomingRelations().size() > 1)
			throw new PNValidationException("Declassificators can only have one input place.");
		if(getOutgoingRelations().size() > 1)
			throw new PNValidationException("Declassificators can only have one output place.");
		
		// Property 2: The declassification transition must be effective, i.e. 
		//             consume at least one colored token (which is not the control flow token).
		Set<String> consumedAttributes = getConsumedAttributes();
		if(consumedAttributes.isEmpty())
			throw new PNValidationException("Declassificators must consume at least one colored token (which is not the control flow token).");
		
		// Property 3: The sets of consumed colors and produced colors have no elements in common
		
		if(!SetUtils.intersection(getConsumedAttributes(), getProducedAttributes()).isEmpty())
			throw new PNValidationException("The sets of consumed and produced token colors must not have common elements.");
		
		// Property 4: No other net transition creates a token with the same color than any of the produced colors of this transition.
		//      (Either as regular transition with CREATE mode or as declassification transition)
		Set<AbstractIFNetTransition> otherNetTransitions = new HashSet<AbstractIFNetTransition>();
		otherNetTransitions.addAll(sNet.getTransitions());
		otherNetTransitions.remove(this);
		for(AbstractIFNetTransition otherTransition: otherNetTransitions){
			if(otherTransition.isDeclassificator()){
				for(String color: getProducedAttributes()){
					if(otherTransition.producesColor(color))
						throw new PNValidationException("There is another declassification transition which produces color \""+color+"\"");
				}
			} else {
				try{
				for(String color: getProducedAttributes()){
					if(otherTransition.producesColor(color) && ((RegularIFNetTransition) otherTransition).getAccessModes(color).contains(AccessMode.CREATE))
						throw new PNValidationException("There is another net transition which creates tokens of color \""+color+"\"");
				}
				}catch(ParameterException e){
					e.printStackTrace();
				}
			}
		}
		
		// Property 5: Token color constraints
		consumedAttributes = getConsumedAttributes();
		Set<String> producedAttributes = getProducedAttributes();
		// 4 a) For each input token color there is one output token color.
		if(consumedAttributes.size() != producedAttributes.size())
			throw new PNValidationException("The number of consumed token colors does not match the number of produced token colors.");
		// 4 b) The number of consumed tokens for each color matches the number of produced tokens for another color.
		Set<String> assignedAttributes = new HashSet<String>();
		for(String consumedAttribute: consumedAttributes){
			for(String producedAttribute: producedAttributes){
				if(!assignedAttributes.contains(producedAttribute) && getConsumedTokens(consumedAttribute) == getProducedTokens(producedAttribute)){
					assignedAttributes.add(consumedAttribute);
					break;
				}
			}
		}
		if(!assignedAttributes.containsAll(consumedAttributes))
			throw new PNValidationException("For at least one input token color, there is no output token color where the number of produced/consumed tokens equals.");
		
	}  
	
	protected void setSNet(IFNet sNet) throws ParameterException{
		Validate.notNull(sNet);
		this.sNet = sNet;
	}

	@Override
	public String toPNML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DeclassificationTransition newInstance() {
		return new DeclassificationTransition();
	}

	@Override
	public DeclassificationTransition clone() {
		DeclassificationTransition result = (DeclassificationTransition) super.clone();
		try {
			result.setSNet(sNet);
		} catch (ParameterException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	

}