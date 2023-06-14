import org.aulich.wbh.FileFiFoStack;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        File f;
        try {
            FileFiFoStack myQue = new FileFiFoStack(new File("C:\\WBH-ExportDokumente\\urn_multiarchive_item_DMS_993\\Content_Files\\urn%3Amultiarchive%3Acontent%3ADMS%3A993-997#1-2-2012-04-02 Rechnung Kastration und Impfung Kaninchen Kathis (Knuffel).pdf"));
            int i = 0;
            while ((f = myQue.getNext()) != null) {
                System.out.println(f.getAbsolutePath() + " - " + f.length());
                i++;
            }
            System.out.println("That's it, number of files: " + i);
        } catch (FileNotFoundException e) {
            System.out.println("So geht das nicht: " + e.getMessage());
        }

    }
}