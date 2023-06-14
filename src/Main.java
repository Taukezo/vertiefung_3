import org.aulich.wbh.FileQue;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        File f;
        try {
            FileQue myQue = new FileQue(new File("C:\\WBH-ExportDokumente"));
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