package org.aulich.wbh.vertiefung_3.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Class provides an easy access to a stack of relevant files (identified by file-extension)
 * that are suitable for fulltext-indexing.
 *
 * @author Thomas Aulich
 */
public class FileFiFoStack {
    private static final String[] extensions = {"txt", "doc", "docx", "pdf", "xls", "xlsx", "xml", "eml", "msg"};
    Queue<File> fileQueue = new LinkedList<File>();
    Queue<File> dirQueue = new LinkedList<File>();


    /**
     * In the constructor the class needs the entry-point for the stack.
     * In case of a file it just adds this file to the stack. In case of a directory all
     * relevant files of this directory and subdirectories will be added to the stack.
     *
     * @param basePoint
     * @throws FileNotFoundException
     */
    public FileFiFoStack(File basePoint) throws FileNotFoundException {
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

    /**
     * Method delivers the next relevant file in the stack. It will return null,
     * if no more file is available.
     *
     * @return nextFile
     */
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

