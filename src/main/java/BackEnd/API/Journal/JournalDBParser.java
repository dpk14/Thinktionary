package src.main.java.BackEnd.API.Journal;

import com.sun.media.sound.InvalidFormatException;
import src.main.java.BackEnd.API.Journal.EntryComponents.Date;
import src.main.java.BackEnd.API.Journal.EntryComponents.Topic;
import src.main.java.BackEnd.API.Journal.Entry;
import src.main.java.BackEnd.API.Login.User;
import src.main.java.BackEnd.Data.Lib.SQLStrings.ColumnLabels;
import src.main.java.BackEnd.ErrorHandling.Errors.CorruptDBError;

import java.util.*;

public class JournalDBParser {

    public static Map<Integer, Entry> parseEntryMap(List<Map<String, Object>> entryMap, List<Map<String, Object>> entryTopic) {
        Map<Integer, Entry> ret = new HashMap<>();
        Map<String, Set<Topic>> topicSets = new HashMap<>();
        try {
            for (Map<String, Object> cols : entryTopic) {
                Topic topic = new Topic((String) cols.get(ColumnLabels.getTOPIC()), (String) cols.get(ColumnLabels.getCOLOR()));
                String entryID = (String) cols.get(ColumnLabels.getEntryId());
                Set<Topic> topics = topicSets.getOrDefault(entryID, new HashSet<>());
                topics.add(topic);
                topicSets.put((String) cols.get(ColumnLabels.getEntryId()), topics);
            }
            for (Map<String, Object> cols : entryMap) {
                String id = (String) cols.get(ColumnLabels.getEntryId());
                Set<Topic> topics = topicSets.get(id);
                Date created = new Date((String) cols.get(ColumnLabels.getCREATED()));
                Entry entry = new Entry(topics, (String) cols.get(ColumnLabels.getTITLE()), (String) cols.get(ColumnLabels.getTEXT()), created);
                int ID = Integer.parseInt(id);
                ret.put(ID, entry);
            }
            return ret;
        }
        catch (Exception e){
            throw new CorruptDBError(e);
        }
    }

    protected static List<Entry> parseEntries(Map<Integer, Entry> entryMap) {
        List<Entry> ret = new ArrayList<>();
        for (int id : entryMap.keySet()) {
            ret.add(entryMap.get(id));
        }
        Collections.sort(ret, new Entry.EntryComparator());
        return ret;
    }

    protected static Map<String, Topic> parseTopics(List<Map<String, Object>> topics) throws ClassCastException{
        Map<String, String> topToCol = new HashMap();
        Map<String, Topic> ret = new HashMap();
        for(Map<String, Object> cols : topics){
            String topicName = (String) cols.get(ColumnLabels.getTOPIC());
            if(!topToCol.containsKey(topicName)){
                topToCol.put(topicName, (String) cols.get(ColumnLabels.getCOLOR()));
            }
        }
        for(String name : topToCol.keySet()){
            ret.put(name, new Topic(name, topToCol.get(name)));
        }
        return ret;
    }

    protected static int getUserID(List<Map<String, Object>> userInfo) throws ClassCastException{
        return (int) userInfo.get(0).get(ColumnLabels.getUSERID());
    }

    public static int getEntryID(List<Map<String, Object>> ent) throws ClassCastException{
        return (int) ent.get(0).get(ColumnLabels.getEntryId());
    }


}
