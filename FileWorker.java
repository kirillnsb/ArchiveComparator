package kirillnsb.archivecomparator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileWorker {

    public static String getFileName(String pathname) {
        File file = new File(pathname);
        return file.getName();
    }

    public static String openFileChooser(String dialogTitle) {
        JButton open1 = new JButton();
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        fc.setDialogTitle(dialogTitle);
        FileNameExtensionFilter ef = new FileNameExtensionFilter("zip Files", "zip");
        fc.setFileFilter(ef);
        fc.showOpenDialog(open1);
        return fc.getSelectedFile().getAbsolutePath();
    }

    public static void writeStringToFile(String str, String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(file);
        pw.print(str);
        pw.close();
        System.out.println("Result successfully saved in file: " + fileName);
    }

    /**
     * Returns a List containing ArchiveComparator objects by scanning zip-archive.
     *
     * @param pathname path to archive
     * @return a List containing ArchiveComparator objects by scanning zip-archive
     * @throws IOException as usual IOException cause
     * @throws FileNotFoundException as usual case of FileNotFoundException
     */
    public static List<ArchiveComparator> scanArchive(String pathname) throws IOException {
        int index;
        ZipEntry entry;
        List<ArchiveComparator> archive1Files = new ArrayList<>();
        ZipInputStream zis = new ZipInputStream(new FileInputStream(pathname));
        while ((entry = zis.getNextEntry()) != null) {
            index = 0;
            ArchiveComparator a1 = new ArchiveComparator();
            a1.name = entry.getName();
            a1.size = entry.getSize();
            a1.hc = entry.hashCode();
            archive1Files.add(index, a1);
            zis.closeEntry();
            index++;
        }
        zis.close();
        return archive1Files;
    }
}
