<<<<<<<< HEAD:src/org/processmining/poemsconformancecheckingforbpmn/connections/YourConnection.java
package org.processmining.poemsconformancecheckingforbpmn.connections;

import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.poemsconformancecheckingforbpmn.models.YourFirstInput;
import org.processmining.poemsconformancecheckingforbpmn.models.YourOutput;
import org.processmining.poemsconformancecheckingforbpmn.models.YourSecondInput;
import org.processmining.poemsconformancecheckingforbpmn.parameters.YourParameters;

public class YourConnection extends AbstractConnection {

	/**
	 * Label for first input.
	 */
	public final static String FIRSTINPUT = "First Input";
	
	/**
	 * Label for second input.
	 */
	public final static String SECONDINPUT = "Second Input";
	
	/**
	 * Label for output.
	 */
	public final static String OUTPUT = "Output";

	/**
	 * Private copy of parameters.
	 */
	private YourParameters parameters;

	/**
	 * Create a connection.
	 * @param input1 First input.
	 * @param input2 Second input.
	 * @param output Output.
	 * @param parameters Parameters.
	 */
	public YourConnection(YourFirstInput input1, YourSecondInput input2, YourOutput output, YourParameters parameters) {
		super("Your Connection");
		put(FIRSTINPUT, input1);
		put(SECONDINPUT, input2);
		put(OUTPUT, output);
		this.parameters = new YourParameters(parameters);
	}

	/**
	 * 
	 * @return The parameters stored in the connection.
	 */
	public YourParameters getParameters() {
		return parameters;
	}
}
========
package org.processmining.sccwbpmnnpos.connections;

import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.sccwbpmnnpos.models.YourFirstInput;
import org.processmining.sccwbpmnnpos.models.YourOutput;
import org.processmining.sccwbpmnnpos.models.YourSecondInput;
import org.processmining.sccwbpmnnpos.parameters.YourParameters;

public class YourConnection extends AbstractConnection {

	/**
	 * Label for first input.
	 */
	public final static String FIRSTINPUT = "First Input";
	
	/**
	 * Label for second input.
	 */
	public final static String SECONDINPUT = "Second Input";
	
	/**
	 * Label for output.
	 */
	public final static String OUTPUT = "Output";

	/**
	 * Private copy of parameters.
	 */
	private YourParameters parameters;

	/**
	 * Create a connection.
	 * @param input1 First input.
	 * @param input2 Second input.
	 * @param output Output.
	 * @param parameters Parameters.
	 */
	public YourConnection(YourFirstInput input1, YourSecondInput input2, YourOutput output, YourParameters parameters) {
		super("Your Connection");
		put(FIRSTINPUT, input1);
		put(SECONDINPUT, input2);
		put(OUTPUT, output);
		this.parameters = new YourParameters(parameters);
	}

	/**
	 * 
	 * @return The parameters stored in the connection.
	 */
	public YourParameters getParameters() {
		return parameters;
	}
}
>>>>>>>> f1fdbee (Initial renaming and setup of the project):src/org/processmining/sccwbpmnnpos/connections/YourConnection.java
