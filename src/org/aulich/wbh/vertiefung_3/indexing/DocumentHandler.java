package org.aulich.wbh.vertiefung_3.indexing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.document.*;
import org.aulich.wbh.vertiefung_3.utils.FieldName;
import org.aulich.wbh.vertiefung_3.utils.FileUtils;
import org.aulich.wbh.vertiefung_3.utils.MetaData;
import org.aulich.wbh.vertiefung_3.utils.TikaDocument;

import java.io.File;

/**
 * DocumentHandler encapsulates the parsing tu fulltext and prepares
 * a Lucene document to be ready for writing in an index.
 *
 * @author Thomas Aulich
 * @version 1.0
 */
public class DocumentHandler {
    private static final Logger logger = LogManager.getLogger(DocumentHandler.class);

    public Document getDocument(File file) throws Exception {
        logger.debug("Preparing file " + file.getName());
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