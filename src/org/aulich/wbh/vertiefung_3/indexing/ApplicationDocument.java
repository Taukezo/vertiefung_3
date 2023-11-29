package org.aulich.wbh.vertiefung_3.indexing;
import org.apache.lucene.document.*;
import org.aulich.wbh.vertiefung_3.utils.FieldName;
import org.aulich.wbh.vertiefung_3.utils.FileUtils;
import org.aulich.wbh.vertiefung_3.utils.MetaData;
import org.aulich.wbh.vertiefung_3.utils.TikaDocument;

import java.io.File;

/**
 * Wraps document processing to be detached from a specific algorithm. Makes sure that documents
 * are processed in the same way in all program alternatives.
 *
 * @author Thomas Aulich
 * @version 1.0
 */
public class ApplicationDocument {
    File file;

    public ApplicationDocument(File file) {
        this.file = file;
    }

    public Document getDocument() throws Exception {
        Document doc = new Document();
        doc.add(new StringField(FieldName.PATH, file.getAbsolutePath(), Field.Store.YES));
        doc.add(new StringField(FieldName.NAME, file.getName(), Field.Store.YES));
        doc.add(new LongPoint(FieldName.SIZE, file.length()));
        doc.add(new StringField(FieldName.SIZE, String.valueOf(file.length()), Field.Store.YES));
        doc.add(new StringField(FieldName.TYPE, FileUtils.getExtensionByStringHandling(file), Field.Store.YES));
        // Fulltext-Index & meta-data
        TikaDocument tikaDoc = new TikaDocument(file);
        if (tikaDoc.isAvailable()) {
            doc.add(new TextField(FieldName.FULLTEXT, tikaDoc.getContentReader()));
            for (MetaData m : tikaDoc.getMetaData()) {
                if (m.value() != null && !m.value().isEmpty()) {
                    doc.add(new StringField("tikameta_" + m.key(), m.value(), Field.Store.YES));
                }
            }
        }
        return doc;
    }
}