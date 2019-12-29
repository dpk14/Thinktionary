package BackEnd.Main;

import BackEnd.Main.Components.Date;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry e1, Entry e2) {
        return e1.getMyCreated().compareTo(e2.getMyCreated());
    }

}