import org.aulich.wbh.vertiefung_3.FileFiFoStack;
import org.aulich.wbh.vertiefung_3.configuration.Configuration;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String rootPath = Configuration.getConfiguration().getConfigurationModel().getRootPath();
        File f;
        try {
            FileFiFoStack myQue = new FileFiFoStack(new File(rootPath));
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