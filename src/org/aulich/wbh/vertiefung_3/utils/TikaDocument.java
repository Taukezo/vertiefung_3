package org.aulich.wbh.vertiefung_3.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TikaDocument {
    private static final Logger logger = LogManager.getLogger(TikaDocument.class);
    private boolean available = false;
    private Metadata metadata = null;
    private ContentHandler handler = null;

    public TikaDocument(File f) throws Exception {
        InputStream is = null;
        is = new FileInputStream(f);
        Parser parser = new AutoDetectParser();
        handler = new BodyContentHandler(-1);
        metadata = new Metadata();
        ParseContext context = new ParseContext();
        parser.parse(is, handler, metadata, context);
        available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Reader getContentReader() {
        return new StringReader(handler.toString());
    }

    public List<MetaData> getMetaData() {
        List<MetaData> l = new ArrayList<MetaData>();
        for (String s : metadata.names()){
            l.add(new MetaData(s, metadata.get(s)));
        }
        return l;
    }
}
