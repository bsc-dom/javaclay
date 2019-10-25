
package es.bsc.dataclay.logic.classmgr.bytecode.pysrc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.logic.classmgr.bytecode.pysrc.codeutils.LineFactory;
import es.bsc.dataclay.logic.classmgr.bytecode.pysrc.codeutils.SourceContainer;
import es.bsc.dataclay.logic.classmgr.bytecode.pysrc.constants.StubImportNames;
import es.bsc.dataclay.logic.classmgr.bytecode.pysrc.constants.StubMethodsNames;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.java.JavaImplementation;
import es.bsc.dataclay.util.management.classmgr.python.PythonClassInfo;
import es.bsc.dataclay.util.management.classmgr.python.PythonImplementation;
import es.bsc.dataclay.util.management.stubs.StubInfo;

/**
 * Generation of Python source of stubs (stub creation).
 * 
 */
public final class StubPySourceManager {

	/**
	 * Default constructor for this Utility Class.
	 */
	private StubPySourceManager() {

	}

	/**
	 * Enumeration for the Mode of the code generation.
	 */
	private enum Mode {
		/** Client Stub mode (for a getStubs typically). */
		ClientStub,
		/** Server Stub mode (when doing class deployment). */
		ServerStub
	}

	// CHECKSTYLE:OFF
	/**
	 * See generatePythonCodeInternal()
	 */
	public static byte[] generatePythonClientStub(final MetaClass metaclass, final String parentName,
			final StubInfo stubInfo) {
		return generatePythonCodeInternal(metaclass, parentName, stubInfo, Mode.ClientStub);
	}

	/**
	 * See generatePythonCodeInternal()
	 */
	public static byte[] generatePythonServerStub(final MetaClass metaclass) {
		return generatePythonCodeInternal(metaclass, null, null, Mode.ServerStub);
	}
	// CHECKSTYLE:ON

	/**
	 * Generates a Python stub corresponding to the MetaClass provided using the Stub info specified.
	 * @param metaclass
	 *            MetaClass from which to generate the stub
	 * @param parentName
	 *            Name of the parent class
	 * @param stubInfo
	 *            Information the stub must have
	 * @param mode
	 *            Mode for the stub code generation
	 * @return An array of bytes representing the stub of the MetaClass provided (using Python syntax).
	 */
	private static byte[] generatePythonCodeInternal(final MetaClass metaclass, final String parentName,
			final StubInfo stubInfo, final Mode mode) {
		final SourceContainer generatedCode = new SourceContainer();

		/*
		 * TODO @abarcelo June 2015: Clean this, and maybe remove Mode, as the Execution Classes are being automatically
		 * generated from the MetaClass containers.
		 */

		// ** HEADER **
		final String extraComment;
		if (mode == Mode.ClientStub) {
			extraComment = "This is a Stub, to be used for the user client";
		} else {
			extraComment = "This is an internal stub, to be used by the Execution Environment";
		}
		generatedCode.initHeader(extraComment);

		// Information about imports
		if (metaclass.getLanguageDepInfos().containsKey(Langs.LANG_PYTHON)) {
			final List<String> importInfo = 
					((PythonClassInfo) metaclass.getLanguageDepInfos().
							get(Langs.LANG_PYTHON)).getImports();
			
			generatedCode.addImportInfo(importInfo);
		}

		// ======================= FIELDS ====================== //

		// ** CLASS DEFINITION **
		final String realParentName;

		if (mode == Mode.ClientStub) {
			if (parentName.compareTo(DataClayObject.class.getName()) == 0) {
				realParentName = StubImportNames.DCLAY_BASECLASS;
			} else {
				realParentName = parentName;
			}
		} else {
			// Server --> dataClay Python generic Object
			realParentName = StubImportNames.DCLAY_OBJECT_CLSNAME;
		}

		final String[] splittedName = metaclass.getName().split("\\.");
		generatedCode.initClassDef(splittedName[splittedName.length - 1], realParentName);

		// ===================== CLASS OPERATIONS ===================== //

		// keep track of them for the dictionary information
		final StringBuffer methodNameDict = new StringBuffer();
		methodNameDict.append("{");

		final StringBuffer argumentInfo = new StringBuffer();
		argumentInfo.append("{\n");

		for (final Operation operation : metaclass.getOperations()) {
			// NOTE: For each operation StubInfo should contain only ONE implementation
			final ImplementationID opImplID = operation.getImplementations().get(0).getDataClayID();

			if (mode == Mode.ClientStub && (stubInfo.getImplementationByID(opImplID.toString()) != null)
					|| mode == Mode.ServerStub) {

				// ================= NEW METHOD ================= //
				for (final Implementation imp : operation.getImplementations()) {
					if (imp instanceof JavaImplementation) {
						// For getters, setters and similar stuff, simply hop over them
						continue;
					}
					if (mode == Mode.ClientStub) {
						// keep track of the methods name and their UUID
						methodNameDict.append("'" + operation.getName() + "': " + StubMethodsNames.UUID_MTHD
								+ "('{" + operation.getDataClayID().getId() + "}'), ");
					}

					final ImplementationID implID = imp.getDataClayID();

					final String[] implLines = LineFactory.extractPythonCodelines(
							(PythonImplementation) operation.getImplementationInOperation(implID));

					// Separate case for the constructor and the rest, to include all the necessary initializations.
					if (mode == Mode.ServerStub) {
						// FIXME the first line may give problems... (pierlauro 2018)
						final String functionDefString = implLines[0];

						implLines[0] = functionDefString.replace(operation.getName(),
								"impn" + Integer.toString(imp.getPosition()) + "_" + operation.getName());
					}

					generatedCode.addMethod(implLines);

					// Update the information about arguments of the operation
					// (ensure coherent ordering)
					final Map<String, Type> params = new LinkedHashMap<>(operation.getParams());

					final StringBuffer argNames = new StringBuffer();
					argNames.append("[");
					for (final String argKeyName : params.keySet()) {
						argNames.append("\"" + argKeyName + "\", ");
					}
					argNames.append("]");

					final StringBuffer argTypes = new StringBuffer();
					argTypes.append("[");
					for (final Type argValueType : params.values()) {
						argTypes.append("\"" + argValueType.getSignatureOrDescriptor() + "\", ");
					}
					argTypes.append("]");

					argumentInfo.append("        " + StubMethodsNames.UUID_MTHD + "('{"
							+ operation.getDataClayID().getId() + "}') : (" + argNames + ", " + argTypes
							+ ", '" + operation.getReturnType().getSignatureOrDescriptor() + "'),\n");

					if (mode == Mode.ClientStub) {
						// For the client, only one implementation is in
						break;
					}
				}
			}

		}

		// ===================== CLASS PROPERTIES ===================== //

		// keep track of them for the dictionary information
		final StringBuffer fieldNameDict = new StringBuffer();
		fieldNameDict.append("{");

		// And put their names and their types in the appropriate lists
		final StringBuffer propertyList = new StringBuffer();
		final StringBuffer propertyTypes = new StringBuffer();
		propertyList.append("[");
		propertyTypes.append("[");

		for (final Property property : metaclass.getProperties()) {
			if (mode == Mode.ClientStub && stubInfo.containsProperty(property.getName())
					|| mode == Mode.ServerStub) {

				// ================= NEW FIELD ================= //

				// keep track of their names and UUID
				fieldNameDict.append("'" + property.getName() + "': " + StubMethodsNames.UUID_MTHD + "('{"
						+ property.getDataClayID().getId() + "}'), ");

				// annotate its name
				propertyList.append("'" + property.getName() + "', ");

				// annotate its type
				propertyTypes.append("'" + property.getType().getSignatureOrDescriptor() + "', ");
			}
		}

		fieldNameDict.append("}");
		propertyList.append("]");
		propertyTypes.append("]");

		generatedCode.endClass();

		// Now get the bytes.
		return generatedCode.sourceToString().getBytes();
	}
}
