import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MediaTypeRegistry;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.CompositeParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.aulich.wbh.vertiefung_3.FileFiFoStack;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;
import java.io.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.aulich.wbh.vertiefung_3.configuration.ConfigurationModel;
import org.aulich.wbh.vertiefung_3.utils.FieldName;
import org.aulich.wbh.vertiefung_3.utils.FileUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        logger.info("Program starts ... ");
        ConfigurationModel cfgM = Configuration.getConfiguration().getConfigurationModel();
        String rootPath = Configuration.getConfiguration().getConfigurationModel().getRootPath();
        File f;

        File indexDir = new File(cfgM.getIndexDirectory());
        // Check existence of index-directory
        if (!FileUtils.createOrCleanDirectory(indexDir.toPath().toString())) {
            logger.error("Could not get access to index-directory " + cfgM.getIndexDirectory() + ". Execution will stop now ...");
            return;
        }

        //checkTika();

        // Create a new index
        Directory index =
                FSDirectory.open(indexDir.toPath());
        IndexWriterConfig indexConfig = new IndexWriterConfig();
        IndexWriter writer = new IndexWriter(index, indexConfig);

        // Go trough the file-system ...
        int i = 0;
        try {
            FileFiFoStack myQue = new FileFiFoStack(new File(rootPath));
            while ((f = myQue.getNext()) != null) {
                Document doc = new Document();
                doc.add(new StringField(FieldName.PATH, f.getAbsolutePath(), Field.Store.YES));
                doc.add(new StringField(FieldName.NAME, f.getName(), Field.Store.YES));
                doc.add(new LongPoint(FieldName.SIZE, f.length()));
                doc.add(new StringField(FieldName.SIZE, String.valueOf(f.length()), Field.Store.YES));
                doc.add(new StringField(FieldName.TYPE, FileUtils.getExtensionByStringHandling(f), Field.Store.YES));
                // Fulltext-Index PARSER!!!
                doc.add(new TextField(FieldName.FULLTEXT, parseToPlainText(f)));
                writer.addDocument(doc);
                i++;
                //if (i > 5) {
                //    break;
                //}
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found or accessible: " + e.getMessage());
        } catch (TikaException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        logger.debug("That's it, number of files: " + i);
        writer.close();
        index.close();
        logger.info("Program stopped");
    }

    public static Reader parseToPlainText(File f) throws IOException, TikaException, SAXException {
        logger.debug("Parsing " + f.getName());
        InputStream is = new FileInputStream(f);
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        parser.parse(is, handler, metadata, context);
        return new StringReader(handler.toString());
    }

    private static void checkTika() {
        // Mimetype-check
        // Get your Tika Config, eg
        TikaConfig config = TikaConfig.getDefaultConfig();
        // Get the registry
        MediaTypeRegistry registry = config.getMediaTypeRegistry();
        // List
        for (MediaType type : registry.getTypes()) {
            String typeStr = type.toString();
            logger.debug(typeStr);
        }
        // Get your Tika object, eg
        Tika tika = new Tika();
        // Get the root parser
        CompositeParser parser = new CompositeParser();
        // Fetch the types it supports
        for (MediaType type : parser.getSupportedTypes(new ParseContext())) {
            String typeStr = type.toString();
        }
        // Fetch the parsers that make it up (note - may need to recurse if any are a CompositeParser too)
        for (Parser p : parser.getAllComponentParsers()) {
            String parserName = p.getClass().getName();
            logger.debug(parserName);
        }
    }


}