package Data.Communication;

import BackEnd.Exceptions.DateExceptions.InvalidDateFormatException;
import BackEnd.Main.Components.Date;
import BackEnd.Main.Components.Topic;
import BackEnd.Main.Entry;
import BackEnd.Main.EntryComparator;
import Data.Lib.Labels;

import java.util.*;


public class DataParser {

    public static Map<Integer, Entry> parseEntryMap(List<Map<String, Object>> entryMap, List<Map<String, Object>> entryTopic)
    throws NumberFormatException, ClassCastException, InvalidDateFormatException {
        Map<Integer, Entry> ret = new HashMap<>();
        Map<String, Set<Topic>> topicSets = new HashMap<>();
        for (Map<String, Object> cols : entryTopic) {
            Topic topic = new Topic((String) cols.get(Labels.getTOPIC()), (String) cols.get(Labels.getCOLOR()));
            String entryID = (String) cols.get(Labels.getEntryId());
            Set<Topic> topics = topicSets.getOrDefault(entryID, new HashSet<>());
            topics.add(topic);
            topicSets.put((String) cols.get(Labels.getEntryId()), topics);
        }
        for (Map<String, Object> cols : entryMap) {
            String id = (String) cols.get(Labels.getEntryId());
            Set<Topic> topics = topicSets.get(id);
            Entry entry = new Entry((String) cols.get(Labels.getTITLE()), topics, (String) cols.get(Labels.getTEXT()), (String) cols.get(Labels.getCOLOR()), new Date((String) cols.get(Labels.getDATE())));
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
        Collections.sort(ret, new EntryComparator());
        return ret;
    }

    public static Set<Topic> parseTopics(List<Map<String, Object>> topics) throws ClassCastException{
        Map<String, String> topToCol = new HashMap<>();
        Set<Topic> ret = new HashSet<>();
        for(Map<String, Object> cols : topics){
            String topicName = (String) cols.get(Labels.getTOPIC());
            if(!topToCol.containsKey(topicName)){
                topToCol.put(topicName, (String) cols.get(Labels.getCOLOR()));
            }
        }
        for(String name : topToCol.keySet()){
            ret.add(new Topic(name, topToCol.get(name)));
        }
        return ret;
    }
}
