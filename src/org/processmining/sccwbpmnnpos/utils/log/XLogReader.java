package org.processmining.sccwbpmnnpos.utils.log;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.stochasticbpmn.algorithms.reader.*;

import java.io.File;
import java.io.InputStream;

public interface XLogReader extends ObjectReader<InputStream, XLog> {
    static XLogReader fromInputStream() {
        return new XLogReaderImpl();
    }

    static ObjectReader<File, XLog> fromFile() {
        return new ObjectFileReader<>(fromInputStream());
    }

    static ObjectReader<String, XLog> fromFileName() {
        return new ObjectFilePathReader<>(fromFile());
    }

    XLog read(final InputStream source) throws Exception;
}
