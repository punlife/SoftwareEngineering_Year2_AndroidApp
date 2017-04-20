package com.com2027.group03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matus on 17-Apr-17.
 */
public class DatabaseLocal {
    /**
     * Model class for easier manipulation of SQL table, without writing
     * any SQL code at all. All models have unique ID 'id' and a timestamp 'created_at'
     * You need to create a custom class that extends this class and add appropriate fields.
     */
    static public abstract class Model {
        private static final String TAG = "DatabaseLocal.Model";

        /**
         * The id of the SQL row
         */
        public int id = 0;

        /**
         * The timedate when the row was created at
         */
        public Timestamp created_at;

        /**
         * Default constructor, acceps nothing
         */
        public Model() {

        }

        /**
         * Sets the integer value of a field
         * @param name The name of the field value
         * @param value The value to set the field to
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public void setInteger(String name, Integer value) throws NoSuchFieldException, IllegalAccessException {
            Field f = this.getClass().getDeclaredField(name);
            f.set(this, value);
        }

        /**
         * Sets the boolean value of a field
         * @param name The name of the field value
         * @param value The value to set the field to
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public void setBoolean(String name, Boolean value) throws NoSuchFieldException, IllegalAccessException {
            Field f = this.getClass().getDeclaredField(name);
            f.set(this, value);
        }

        /**
         * Sets the string value of a field
         * @param name The name of the field value
         * @param value The value to set the field to
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public void setString(String name, String value) throws NoSuchFieldException, IllegalAccessException {
            Field f = this.getClass().getDeclaredField(name);
            f.set(this, value);
        }

        /**
         * @param name The name of the field
         * @return Returns the value of the field
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public Integer getInteger(String name) throws NoSuchFieldException, IllegalAccessException {
            return (Integer)this.getClass().getDeclaredField(name).get(this);
        }

        /**
         * @param name The name of the field
         * @return Returns the value of the field
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public Boolean getBoolean(String name) throws NoSuchFieldException, IllegalAccessException {
            return (Boolean)this.getClass().getDeclaredField(name).get(this);
        }

        /**
         * @param name The name of the field
         * @return Returns the value of the field
         * @throws NoSuchFieldException
         * @throws IllegalAccessException
         */
        public String getString(String name) throws NoSuchFieldException, IllegalAccessException {
            return (String)this.getClass().getDeclaredField(name).get(this);
        }

        /**
         * Creates a map of all public and private fields for this class and superclass
         * @return Map<String, Object> with key as a field name and Object as a
         * reference to the field
         * @throws IllegalAccessException
         */
        public Map<String, Object> getColumns() throws IllegalAccessException {
            Map<String, Object> fields = new HashMap<String, Object>();

            // Get all fiels of this class and the upperclass
            Field[] fs = this.getClass().getDeclaredFields();
            for(Field f : fs){
                Object o = f.get(this);

                // Is the field an integer?
                if(o instanceof Integer){
                    fields.put(f.getName(), o);
                }

                // Boolean?
                else if(o instanceof Boolean){
                    fields.put(f.getName(), o);
                }

                // Or string?
                else if(o instanceof String){
                    fields.put(f.getName(), o);
                }

                // Do not accept anything else
                else {
                    // Do nothing
                }
            }

            return fields;
        }

        /**
         * @return Name of this model (e.g. the class name without package name)
         */
        String getName(){
            return this.getClass().getSimpleName();
        }

        /**
         * @return Id if this entry inside of the SQL table. Non null value indicates the entry was
         * either loaded/inserted from/to the SQL table
         */
        public int getId() {
            return id;
        }
    }

    /**
     * This class is used to connect to the database. It accepts an array of models and creates
     * an SQL statement used by the database connector.
     */
    static public class Schema {
        private static final String TAG = "DatabaseLocal.Schema";
        private Model[] models;

        /**
         * Main constructor. Pass all models you wish to create in the SQL database
         * @param models An array of Model objects
         */
        public Schema(Model[] models){
            this.models = models;
        }

        /**
         * Creates a list of SQL statements to create all tables for all models.
         * @return List of SQL strings
         */
        public List<String> getCreateEntries(){
            // List to hold SQL statements
            List<String> list = new ArrayList<String>();

            // Loop through all models passed to the constructor
            for (Model m : models) {
                StringBuilder str = new StringBuilder();

                try {
                    // The beginning of the SQL statement
                    str.append("CREATE TABLE " + m.getName() + " (");
                    str.append("id INTEGER PRIMARY KEY, ");
                    str.append("created_at DATETIME DEFAULT CURRENT_TIMESTAMP, ");

                    Map<String, Object> columns = m.getColumns();
                    int count = 0;

                    // Loop through all fields of the Model class
                    for(Map.Entry<String, Object> col : columns.entrySet()){
                        Object o = col.getValue();

                        if(o instanceof Integer){
                            str.append(col.getKey() + " INTEGER NOT NULL");
                        }
                        else if(o instanceof Boolean){
                            str.append(col.getKey() + " BOOLEAN NOT NULL");
                        }
                        else if(o instanceof String){
                            str.append(col.getKey() + " VARCHAR(255) NOT NULL");
                        }

                        count++;

                        // Is this NOT the last field?
                        if(count != columns.size()){
                            str.append(", ");
                        }
                    }

                    str.append(")");

                } catch (IllegalAccessException e){
                    Log.e(TAG, e.getMessage());
                }

                list.add(str.toString());
            }

            return list;
        }

        /**
         * Same as getCreateEntries, except this one crates a SQL statement(s) to destroy tables
         * @return List of SQL strings
         */
        public List<String> getDeleteEntries(){
            // List to hold SQL statements
            List<String> list = new ArrayList<String>();

            // Loop through all models passed to the constructor
            for (Model m : models) {
                StringBuilder str = new StringBuilder();

                str.append("DROP TABLE IF EXISTS " + m.getName());

                list.add(str.toString());
            }

            return list;
        }
    }

    /**
     * This class is used to connect (open connection) to the database and create/delete tables
     * if necessary
     */
    private static class Connector extends SQLiteOpenHelper {
        private static final String TAG = "DatabaseLocal.Connector";
        private static final int DATABASE_VERSION = 1;
        private Schema schema = null;

        /**
         * Main constructor
         * @param context Context reference of the activity class
         * @param databaseName The database name as a filename (example: "database.db")
         * @param schema Schema that contains all models
         */
        public Connector(Context context, String databaseName, Schema schema){
            super(context, databaseName, null, DATABASE_VERSION);
            this.schema = schema;
        }

        public void onCreate(SQLiteDatabase db) {
            List<String> tables = schema.getCreateEntries();
            Log.d(TAG, "Creating " + tables.size() + " tables!");
            for(String s : tables){
                Log.d(TAG, "Creating table: " + s);
                db.execSQL(s);
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            List<String> tables = schema.getDeleteEntries();
            Log.d(TAG, "Destroying " + tables.size() + " tables!");
            for(String s : tables){
                Log.d(TAG, "Destroying table: " + s);
                db.execSQL(s);
            }
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static final String TAG = "DatabaseLocal";
    private Connector connector = null;
    private SQLiteDatabase db;

    /**
     * Main constructor
     * @param context Context reference of the activity class
     * @param databaseName The database name as a filename (example: "database.db")
     * @param schema Schema that contains all models
     */
    public DatabaseLocal(Context context, String databaseName, Schema schema){
        connector = new Connector(context, databaseName, schema);
        // We will write and read from this database
        db = connector.getWritableDatabase();
    }

    /**
     * Closes the connection to the database
     */
    public void close(){
        db.close();
    }

    /**
     * Inserts a new row into the table. The model should have all the fields set. The table
     * that is being updated is the name of this model.
     * @param model Instance of the derived class of the model.
     * @throws IllegalAccessException If there was an error with accessing fields.
     * @throws IllegalArgumentException If there was an error with model or SQL insert statement.
     */
    public void insert(Model model) throws IllegalAccessException, IllegalArgumentException {
        if(model == null)throw new IllegalArgumentException("Model argument cannot be null!");

        ContentValues values = new ContentValues();

        Map<String, Object> columns = model.getColumns();

        // Loop through all fields (columns)
        for(Map.Entry<String, Object> col : columns.entrySet()){
            Object o = col.getValue();

            if(o instanceof Integer){
                values.put(col.getKey(), (Integer)o);
            }
            else if(o instanceof Boolean){
                values.put(col.getKey(), (Boolean)o);
            }
            else if(o instanceof String){
                values.put(col.getKey(), (String)o);
            }
        }

        long newRowId = db.insert(model.getName(), null, values);
        if(newRowId == -1) throw new IllegalArgumentException("Row not inserted into the SQL table!");

        // The returned value of db.insert is the ID value!
        model.id = (int)newRowId;
    }

    /**
     * Updates a row in the SQL table
     * @param model The model to update with appropriate fields.
     * @throws IllegalAccessException If there was an error with accessing fields.
     * @throws IllegalArgumentException If there was an error with model or SQL update statement.
     */
    public void update(Model model) throws IllegalAccessException, IllegalArgumentException {
        if(model == null)throw new IllegalArgumentException("Model argument cannot be null!");

        ContentValues values = new ContentValues();

        Map<String, Object> columns = model.getColumns();

        // Loop through all fields (columns)
        for(Map.Entry<String, Object> col : columns.entrySet()){
            Object o = col.getValue();

            if(o instanceof Integer){
                values.put(col.getKey(), (Integer)o);
            }
            else if(o instanceof Boolean){
                values.put(col.getKey(), (Boolean)o);
            }
            else if(o instanceof String){
                values.put(col.getKey(), (String)o);
            }
        }

        int numUpdated = db.update(model.getName(), values, "id=" + model.id, null);
        if(numUpdated != 1) throw new IllegalArgumentException("Row not updated in the SQL table!");
    }

    /**
     * Deletes a specific row in the SQL table
     * @param model The model to delete
     * @throws IllegalAccessException If there was an error with accessing fields.
     * @throws IllegalArgumentException If there was an error with model or SQL delete statement.
     */
    public void delete(Model model) throws IllegalAccessException, IllegalArgumentException {
        if(model == null)throw new IllegalArgumentException("Model argument cannot be null!");

        int numUpdated = db.delete(model.getName(), "id=" + model.id, null);
        if(numUpdated != 1) throw new IllegalArgumentException("Row not deleted from the SQL table!");
    }

    /**
     * Prints all rows and columns of the table (model)
     * @param type The model class you wish to print out (for example: UserModel.class)
     */
    public <T extends Model> void debug(Class<T> type) {
        try {
            // Select all from table 'type'
            Cursor cursor = db.rawQuery("SELECT * FROM " + type.getSimpleName(), null);

            boolean first = true;
            if (cursor.moveToFirst()) {
                do {
                    StringBuilder str = new StringBuilder();
                    int count = cursor.getColumnCount();

                    // If this is the first time run, print column names
                    if (first) {
                        for (int i = 0; i < count; i++) {
                            str.append(cursor.getColumnName(i) + ", ");
                        }
                        Log.d(TAG, str.toString());
                        str = new StringBuilder();
                        first = false;
                    }

                    // Print all columns in one line
                    for (int i = 0; i < count; i++) {
                        str.append(cursor.getString(i) + ", ");
                    }
                    Log.d(TAG, str.toString());
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    /**
     * Finds an entry (row of the SQL table) based on the ID
     * @param type The model class of the requested entry (for example: UserModel.class)
     * @param id The ID of the row
     * @return Instance of the custom class with all fields set
     */
    public <T extends Model> T findById(Class<T> type, int id){
        try {
            T entry = type.newInstance();

            // Select by ID
            Cursor cursor = db.rawQuery("SELECT * FROM " + type.getSimpleName() + " WHERE id = ?", new String[]{"" + id});

            // Did we find anything?
            if(cursor.moveToFirst()){
                entry.id = id;

                Map<String, Object> columns = entry.getColumns();

                // Loop through all fields of the given model
                for(Map.Entry<String, Object> col : columns.entrySet()) {
                    Object o = col.getValue();

                    if(o instanceof Integer){
                        entry.setInteger(col.getKey(), cursor.getInt(cursor.getColumnIndex(col.getKey())));
                    }
                    else if(o instanceof Boolean){
                        entry.setBoolean(col.getKey(), cursor.getInt(cursor.getColumnIndex(col.getKey())) != 0);
                    }
                    else if(o instanceof String){
                        entry.setString(col.getKey(), cursor.getString(cursor.getColumnIndex(col.getKey())));
                    }
                }

                // The timestamp column is a simple string value
                entry.created_at = Timestamp.valueOf(cursor.getString(1));
            } else {
                return null;
            }

            return entry;
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }

        return null;
    }


}
