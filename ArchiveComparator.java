package kirillnsb.archivecomparator;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class ArchiveComparator {

    String name;
    int hc;
    long size;
    boolean newFileFlag = true;
    private String archive1Path;
    private String archive2Path;
    private List<ArchiveComparator> archive1Files = null;
    private List<ArchiveComparator> archive2Files = null;

    public ArchiveComparator() {
    }

    public ArchiveComparator(String archive1Path, String archive2Path) throws IOException {
        this.archive1Path = archive1Path;
        this.archive2Path = archive2Path;
        setArchive1Files(FileWorker.scanArchive(archive1Path));
        setArchive2Files(FileWorker.scanArchive(archive2Path));
        FileWorker.writeStringToFile(compare(), "result.txt");
    }

    public void setArchive1Files(List<ArchiveComparator> archive1Files) {
        this.archive1Files = archive1Files;
    }

    public void setArchive2Files(List<ArchiveComparator> archive2Files) {
        this.archive2Files = archive2Files;
    }

    private String compare() {
        String res = "   " + FileWorker.getFileName(archive1Path) + "  |  " + FileWorker.getFileName(archive2Path) + "\n\n";
        ArchiveComparator a1 = null;
        ArchiveComparator a2 = null;
        for (ArchiveComparator a1File : archive1Files) {
            a1 = a1File;
            boolean haveAnalog = false;
            for (int i = 0; i < archive2Files.size(); i++) {
                a2 = archive2Files.get(i);
                if (a1.equals(a2)) {
                    res += "= " + a1.name + "  |  " + "= " + a2.name + "\n";    //same case
                    setNewFileFlagToFalse(i, a2);
                    haveAnalog = true;
                    break;
                } else if (a1.name.equals(a2.name) && (a1.hc != a2.hc || a1.size != a2.size)) {
                    res += "* " + a1.name + "  |  " + "* " + a2.name + "\n";    //updated case
                    setNewFileFlagToFalse(i, a2);
                    haveAnalog = true;
                    break;
                } else if (a1.size == a2.size) {
                    res += "? " + a1.name + "  |  " + "? " + a2.name + "\n";    //renamed case
                    setNewFileFlagToFalse(i, a2);
                    haveAnalog = true;
                    break;
                }
            }
            if (!haveAnalog) res += "- " + a1.name + "  |\n";       //deleted case
        }
        for (ArchiveComparator a2File : archive2Files) {
            a2 = a2File;
            if (a2.newFileFlag)
                res += "\t\t  |  " + "+ " + a2.name;                //new case
        }

        System.out.println(res);
        return res;
    }

    private void setNewFileFlagToFalse(int index, ArchiveComparator a) {
        a.newFileFlag = false;
        archive2Files.set(index, a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArchiveComparator that = (ArchiveComparator) o;
        return hc == that.hc &&
                size == that.size &&
                Objects.equals(name, that.name);
    }

    public static void main(String[] args) throws IOException {
        ArchiveComparator ac = null;
        try {
            ac = new ArchiveComparator(args[0], args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            String archive1Path = FileWorker.openFileChooser("Select archive 1");
            String archive2Path = FileWorker.openFileChooser("Select archive 2");
            ac = new ArchiveComparator(archive1Path, archive2Path);
        }

    }
}