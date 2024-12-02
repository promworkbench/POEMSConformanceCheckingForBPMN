package org.processmining.sccwbpmnnpos.utils.log;

import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.in.XesXmlParser;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;

import java.io.InputStream;

public class XLogReaderImpl implements XLogReader {
    @Override
    public XLog read(InputStream inputStream) throws Exception {
        return read(inputStream, "");
    }

    @Override
    public XLog read(InputStream inputStream, String s) throws Exception {
        XesXmlParser parser = new XesXmlParser();
        return parser.parse(inputStream).get(0);
    }
}
