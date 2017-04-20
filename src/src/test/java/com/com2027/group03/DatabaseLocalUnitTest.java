package com.com2027.group03;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Matus on 17-Apr-17.
 */

public class DatabaseLocalUnitTest {
    class MyTestModel extends DatabaseLocal.Model {
        public Integer fieldInteger = new Integer(0);
        public Boolean fieldBoolean = new Boolean(false);
        public String fieldString = new String();

        public MyTestModel() {
            super();
        }
    }

    class BookModel extends DatabaseLocal.Model {
        public Integer code = new Integer(0);
        public String title = new String();

        public BookModel() {
            super();
        }
    }

    class PostModel extends DatabaseLocal.Model {
        public String title = new String();
        public String author = new String();

        public PostModel() {
            super();
        }
    }

    @Test
    public void checkModelFields() throws Exception {
        MyTestModel model = new MyTestModel();

        assertEquals(model.getName(), "MyTestModel");

        model.fieldInteger = 42;
        model.fieldBoolean = true;
        model.fieldString = "Hello World!";

        Map<String, Object> columns = model.getColumns();

        assertTrue(columns.containsKey("fieldInteger"));
        assertTrue(columns.containsKey("fieldBoolean"));
        assertTrue(columns.containsKey("fieldString"));

        assertTrue((Integer)columns.get("fieldInteger") == 42);
        assertTrue((Boolean)columns.get("fieldBoolean") == true);
        assertTrue((String)columns.get("fieldString") == "Hello World!");
    }

    @Test
    public void testAccessors() throws Exception {
        MyTestModel model = new MyTestModel();

        model.fieldInteger = 42;
        model.fieldBoolean = true;
        model.fieldString = "Hello World!";

        assertEquals(model.getInteger("fieldInteger"), new Integer(42));
        assertEquals(model.getBoolean("fieldBoolean"), new Boolean(true));
        assertEquals(model.getString("fieldString"), new String("Hello World!"));

        model.setInteger("fieldInteger", 10);
        model.setBoolean("fieldBoolean", false);
        model.setString("fieldString", "ABCD");

        assertEquals(model.fieldInteger, new Integer(10));
        assertEquals(model.fieldBoolean, new Boolean(false));
        assertEquals(model.fieldString, new String("ABCD"));

        assertEquals(model.getInteger("fieldInteger"), new Integer(10));
        assertEquals(model.getBoolean("fieldBoolean"), new Boolean(false));
        assertEquals(model.getString("fieldString"), new String("ABCD"));
    }

    @Test
    public void checkSchema() throws Exception {
        BookModel bookModel = new BookModel();
        PostModel postModel = new PostModel();

        assertEquals(bookModel.getName(), "BookModel");
        assertEquals(postModel.getName(), "PostModel");

        DatabaseLocal.Schema schema = new DatabaseLocal.Schema(new DatabaseLocal.Model[]{bookModel, postModel});

        List<String> list = schema.getCreateEntries();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0), "CREATE TABLE BookModel (id INTEGER PRIMARY KEY, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, code INTEGER NOT NULL, title VARCHAR(255) NOT NULL)");
        assertEquals(list.get(1), "CREATE TABLE PostModel (id INTEGER PRIMARY KEY, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, author VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL)");
    }
}
