package BackEnd.Main;

import BackEnd.Main.Components.Topic;

import java.util.*;

public class DataParser {

    String TOPIC = "Topic";
    String COLOR = "Color";
    String ENTRY_ID = "Entry ID";
    String TITLE = "Title";
    String DATE = "Date";
    String TEXT = "Text";

    public static Map<Integer, Entry> parseEntryMap(List<Map<String, Object>> entryMap, List<Map<String, Object>> entryTopic) {
        Map<Integer, Entry> ret = new HashMap<>();
        try {
            Map<String, Set<Topic>> topicSets = new HashMap<>();
            for (Map<String, Object> cols : entryTopic) {
                Topic topic = new Topic(cols.get(TOPIC), cols.get(COLOR));
                topicSets.put(cols.get(ENTRY_ID), topic);
            }
            for (Map<String, Object> cols : entryMap) {
                String id = cols.get(ENTRY_ID);
                Set<Topic> topics = topicSets.get(id);
                Entry entry = new Entry(cols.get(TITLE), topics, cols.get(TEXT), cols.get(COLOR), cols.get(DATE));
                int ID = Integer.parseInt(id);
                ret.put(ID, entry);
            }
        } catch (NumberFormatException e) {
            System.out.print(e.getStackTrace());
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

    public static Set<Topic> parseTopics(List<Map<String, Object>> topics) {
        Map<String, String> topToCol = new HashMap<>();
        Set<Topic> ret = new HashSet<>();
        for(Map<String, Object> cols : topics){
            String topicName = cols.get(TOPIC);
            if(!topToCol.containsKey(topicName)){
                topToCol.put(topicName, cols.get(COLOR));
            }
        }
        for(String name : topToCol.keySet()){
            ret.add(new Topic(name, topToCol.get(name)));
        }
        return ret;
    }
}
