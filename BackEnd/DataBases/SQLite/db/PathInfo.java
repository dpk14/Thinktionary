package BackEnd.DataBases.SQLite.db;

import BackEnd.Data.Lib.Paths.Drivers.DriverPaths;

import java.io.File;

public class PathInfo {
    final File FILE = new File(DriverPaths.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    final String PARENT_PATH = FILE.getParent();

    public String getPARENT_PATH(){
        return String.valueOf(PARENT_PATH);
    }
}
