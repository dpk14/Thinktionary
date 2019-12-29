package BackEnd.Main;

import BackEnd.Main.Components.Topic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
}
