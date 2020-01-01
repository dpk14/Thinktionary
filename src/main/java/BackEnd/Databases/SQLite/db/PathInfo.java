package src.main.java.BackEnd.Databases.SQLite.db;

import src.main.java.BackEnd.Data.Lib.Paths.Drivers.DriverPaths;

import java.io.File;

public class PathInfo {
    final File FILE = new File(DriverPaths.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    final String PARENT_PATH = FILE.getParent();

    public String getPARENT_PATH(){
        return String.valueOf(PARENT_PATH);
    }
}
