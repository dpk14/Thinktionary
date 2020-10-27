package Model.Data.API.Run;

import Model.API.Journal.Entry;
import Model.API.Journal.JournalDBParser;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Utils.ErrorHandling.Exceptions.ServerExceptions.NoSuchEntryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class
JournalDBAPI extends RunDBAPI {

    Integer myUserID;

    public JournalDBAPI() {
        super();
    }

    public JournalDBAPI(int userID) {
        super();
        myUserID = userID;
    }

    /*
    ----------------------------
    Public Run:
    ----------------------------
     */

    //Loading:

    public List<Map<String, Object>> loadEntryTable() {
        return loadTableByParamater(TableNames.getEntryInfo(), ColumnInfo.getUSERID(), myUserID);
    }

    public List<Map<String, Object>> loadEntryTopicsTable() {
        return loadTableByParamater(TableNames.getEntryToTopic(), ColumnInfo.getUSERID(), myUserID);
    }

    public List<Map<String, Object>> loadTopicBankTable() {
        return loadTableByParamater(TableNames.getUserTopic(), ColumnInfo.getUSERID(), myUserID);
    }

    //Communicators:

    public boolean usesTopic(String topicName) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), myUserID));
        conditions.add(new Equals(ColumnInfo.getTOPIC(), topicName));
        List<Map<String, Object>> entries = userQuery(SQLQueryBuilder.select(TableNames.getEntryToTopic(), conditions));
        return entries.size() > 0;
    }

    public void removeTopicFromBank(String topicName) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), myUserID));
        conditions.add(new Equals(ColumnInfo.getTOPIC(), topicName));
        userAction(SQLQueryBuilder.remove(TableNames.getUserTopic(), conditions));
    }

    public void removeTopicFromEntry(Integer entryID, String topicName) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), myUserID));
        conditions.add(new Equals(ColumnInfo.getEntryId(), entryID));
        conditions.add(new Equals(ColumnInfo.getTOPIC(), topicName));
        userAction(SQLQueryBuilder.remove(TableNames.getEntryToTopic(), conditions));
    }

    public void save(Integer entryID, Entry entry) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERID(), myUserID));
        parameters.add(new Parameter(ColumnInfo.getTITLE(), entry.getmyTitle()));
        parameters.add(new Parameter(ColumnInfo.getTEXT(), entry.getmyText()));
        parameters.add(new Parameter(ColumnInfo.getCREATED(), entry.getMyCreated()));
        parameters.add(new Parameter(ColumnInfo.getMODIFIED(), entry.getMyModfied()));

        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEntryId(), entryID));

        userAction(SQLQueryBuilder.modify(TableNames.getEntryInfo(), parameters, conditions));
    }

    public int addToEntryInfo(Entry entry) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERID(), myUserID));
        parameters.add(new Parameter(ColumnInfo.getTITLE(), entry.getmyTitle()));
        parameters.add(new Parameter(ColumnInfo.getTEXT(), entry.getmyText()));
        parameters.add(new Parameter(ColumnInfo.getCREATED(), entry.getMyCreated()));
        parameters.add(new Parameter(ColumnInfo.getMODIFIED(), entry.getMyModfied()));
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), myUserID));
        conditions.add(new Equals(ColumnInfo.getTITLE(), entry.getmyTitle()));
        conditions.add(new Equals(ColumnInfo.getTEXT(), entry.getmyText()));
        conditions.add(new Equals(ColumnInfo.getCREATED(), entry.getMyCreated()));
        conditions.add(new Equals(ColumnInfo.getMODIFIED(), entry.getMyModfied()));

        userAction(SQLQueryBuilder.insert(TableNames.getEntryInfo(), parameters));
        userQuery(SQLQueryBuilder.select(TableNames.getEntryInfo(), conditions));
        List<Map<String, Object>> ret = userQuery(SQLQueryBuilder.select(TableNames.getEntryInfo(), conditions));
        return JournalDBParser.getEntryID(ret);
    }

    public void addToTopicBank(Map<String, String> topicToColor) {
        for (String topic : topicToColor.keySet()) {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(ColumnInfo.getUSERID(), myUserID));
            parameters.add(new Parameter(ColumnInfo.getTOPIC(), topic));
            parameters.add(new Parameter(ColumnInfo.getCOLOR(), topicToColor.get(topic)));

            userAction(SQLQueryBuilder.insert(TableNames.getUserTopic(), parameters));
        }
    }

    public void addToEntryTopic(Integer entryID, String topic, String color) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERID(), myUserID));
        parameters.add(new Parameter(ColumnInfo.getEntryId(), entryID));
        parameters.add(new Parameter(ColumnInfo.getTOPIC(), topic));
        parameters.add(new Parameter(ColumnInfo.getCOLOR(), color));

        userAction(SQLQueryBuilder.insert(TableNames.getEntryToTopic(), parameters));
    }

    public void removeEntry(Integer entryID) throws NoSuchEntryException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), myUserID));
        conditions.add(new Equals(ColumnInfo.getEntryId(), entryID));

        List<Map<String, Object>> toBeRemoved = userQuery(SQLQueryBuilder.select(TableNames.getEntryInfo(), conditions));
        if (toBeRemoved.size() == 0) {
            throw new NoSuchEntryException(entryID);
        }
        userAction(SQLQueryBuilder.remove(TableNames.getEntryInfo(), conditions));
        userAction(SQLQueryBuilder.remove(TableNames.getEntryToTopic(), conditions));
    }

}
