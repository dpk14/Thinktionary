package BackEnd.DB.Communication;

import BackEnd.API.Exceptions.DateExceptions.InvalidDateFormatException;
import BackEnd.API.EntryComponents.Date;
import BackEnd.API.EntryComponents.Topic;
import BackEnd.API.Entry;
import BackEnd.DB.Lib.SQLStrings.ColumnLabels;

import java.util.*;


public class DataParser {

    public static Map<Integer, Entry> parseEntryMap(List<Map<String, Object>> entryMap, List<Map<String, Object>> entryTopic)
    throws NumberFormatException, ClassCastException, InvalidDateFormatException {
        Map<Integer, Entry> ret = new HashMap<>();
        Map<String, Set<Topic>> topicSets = new HashMap<>();
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
            Entry entry = new Entry((String) cols.get(ColumnLabels.getTITLE()), topics, (String) cols.get(ColumnLabels.getTEXT()), (String) cols.get(ColumnLabels.getCOLOR()), new Date((String) cols.get(ColumnLabels.getCREATED())));
            int ID = Integer.parseInt(id);
            ret.put(ID, entry);
        }
        return ret;
    }

    public static List<Entry> parseEntries(Map<Integer, Entry> entryMap) {
        List<Entry> ret = new ArrayList<>();
        for (int id : entryMap.keySet()) {
            ret.add(entryMap.get(id));
        }
        Collections.sort(ret, new Entry.EntryComparator());
        return ret;
    }

    public static Set<Topic> parseTopics(List<Map<String, Object>> topics) throws ClassCastException{
        Map<String, String> topToCol = new HashMap<>();
        Set<Topic> ret = new HashSet<>();
        for(Map<String, Object> cols : topics){
            String topicName = (String) cols.get(ColumnLabels.getTOPIC());
            if(!topToCol.containsKey(topicName)){
                topToCol.put(topicName, (String) cols.get(ColumnLabels.getCOLOR()));
            }
        }
        for(String name : topToCol.keySet()){
            ret.add(new Topic(name, topToCol.get(name)));
        }
        return ret;
    }
}
