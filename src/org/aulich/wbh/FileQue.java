package org.aulich.wbh;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileQue {
    private static final String[] extensions = {"txt", "doc", "docx", "pdf"};
    Queue<File> fileQueue = new LinkedList<File>();
    Queue<File> dirQueue = new LinkedList<File>();


    public FileQue(File basePoint) throws FileNotFoundException {
        if (basePoint.exists() && basePoint.isDirectory()) {
            if (basePoint.isDirectory()) {
                dirQueue.add(basePoint);
            } else {
                fileQueue.add(basePoint);
            }
        } else {
            throw new FileNotFoundException("No existing file or no directory");
        }
    }

    public File getNext() {
        File nextFile = fileQueue.poll();
        while (nextFile == null) {
            File nextDir = dirQueue.poll();
            if (nextDir == null) {
                return null;
            }
            loadDirectory(nextDir);
            nextFile = fileQueue.poll();
        }
        return nextFile;
    }

    private void loadDirectory(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                dirQueue.add(file);
            } else {
                for (String extension : extensions) {
                    if (file.getName().toLowerCase().endsWith("." + extension)) {
                        fileQueue.add(file);
                    }

                }
            }
        }
    }
}

