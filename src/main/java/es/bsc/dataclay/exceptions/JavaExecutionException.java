
/**
 * @file GeneralException.java
 * 
 * @date Oct 22, 2012
 */

package es.bsc.dataclay.exceptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * This class represents the Java exceptions produced in Execution Environment.
 */
@SuppressWarnings("serial")
public final class JavaExecutionException extends LanguageExecutionException {

	/**
	 * Constructor used for deserialization
	 */
	public JavaExecutionException() {

	}

	/**
	 * Constructor
	 * 
	 * @param thejavaException
	 *            Java exception
	 */
	public JavaExecutionException(final Throwable thejavaException) {
		super(thejavaException);
	}

}
