package org.processmining.sccwbpmnnpos.utils.log;

import org.deckfour.xes.model.XLog;
import org.processmining.stochasticbpmn.algorithms.reader.ObjectReader;
import org.processmining.stochasticbpmn.algorithms.reader.StochasticBPMNInputStreamReader;

import java.io.InputStream;

public interface XLogReader extends ObjectReader<InputStream, XLog> {
    XLog read(final InputStream source) throws Exception;
}
