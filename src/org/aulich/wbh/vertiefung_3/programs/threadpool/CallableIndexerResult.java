package org.aulich.wbh.vertiefung_3.programs.threadpool;

import org.apache.lucene.document.Document;

public class CallableIndexerResult {
    private final String name;
    private final Document document;

    public CallableIndexerResult(String name, Document document) {
        this.name = name;
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public Document getDocument() {
        return document;
    }

}
