package src.main.java.Model.Data.Lib.Paths.Drivers;

import java.io.File;

public class DriverPaths {

    final File FILE = new File(DriverPaths.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    final String PARENT_PATH = FILE.getParent();

    private final String SQLITE = PARENT_PATH + File.separator + "sqlite-jdbc-3.30.1.jar";

    public String getSQLITE() {
        return String.valueOf(SQLITE);
    }
}
