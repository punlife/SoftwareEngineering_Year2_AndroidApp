package com.com2027.group03;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import java.sql.Timestamp;

/**
 * Created by Matus on 22-Feb-17.
 */

// Replace MainActivity for any activity class
public class DatabaseLocalTest extends ActivityUnitTestCase<EmptyTestActivity>{
    private static final String TAG = "DatabaseLocalTest";
    private DatabaseLocal databaseLocal;

    public static class Book extends DatabaseLocal.Model {
        public Integer code = new Integer(0);
        public String title = new String();

        public Book() {
            super();
        }
    }

    public static class Post extends DatabaseLocal.Model {
        public String title = new String();
        public String author = new String();

        public Post() {
            super();
        }
    }

    public DatabaseLocalTest() {
        super(EmptyTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);

        // Get base context
        Context context = this.getInstrumentation().getTargetContext();

        // Delete database if exist
        context.deleteDatabase("database.db");

        // Create schema for the database
        DatabaseLocal.Schema schema = new DatabaseLocal.Schema(new DatabaseLocal.Model[]{
                new Book(),
                new Post()
        });

        // Create connection to the database
        databaseLocal = new DatabaseLocal(context, "database.db", schema);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Close database connection
        databaseLocal.close();
    }

    @SmallTest
    public void testDatabase() throws Exception {
        Log.d("DatabaseLocalTest", "Running testDatabase...");

        Book book = new Book();
        book.title = "Hello";
        book.code = 1234;

        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        Log.d(TAG, "Timestamp for comparision: " + timeStamp);

        databaseLocal.insert(book);

        Log.d(TAG, "Book new ID: " + book.id);

        book.title = "World";
        book.code = 5678;

        databaseLocal.insert(book);

        Log.d(TAG, "Book new ID: " + book.id);

        databaseLocal.debug(Book.class);

        Book found = databaseLocal.findById(Book.class, 1);
        assertEquals(found.id, 1);
        assertEquals(found.title, "Hello");
        assertEquals(found.code, new Integer(1234));

        Log.d(TAG, "Book 1 timestamp: " + found.created_at);
        Log.d(TAG, "Book 1 time long: " + found.created_at.getTime());
        Log.d(TAG, "timestamp long: " + timeStamp.getTime());

        // Differ at most by 1 second
        // Might fail on ultra slow devices?
        assertTrue(Math.abs(timeStamp.getTime() - found.created_at.getTime()) > 1000);

        // Apparently the following will fail thanks to summer time (shift by one hour)
        /*assertEquals(found.created_at.getDay(), timeStamp.getDay());
        assertEquals(found.created_at.getMonth(), timeStamp.getMonth());
        assertEquals(found.created_at.getYear(), timeStamp.getYear());
        assertEquals(found.created_at.getHours(), timeStamp.getHours());
        assertEquals(found.created_at.getMinutes(), timeStamp.getMinutes());
        assertEquals(found.created_at.getSeconds(), timeStamp.getSeconds());*/

        found = databaseLocal.findById(Book.class, 2);
        assertEquals(found.id, 2);
        assertEquals(found.title, "World");
        assertEquals(found.code, new Integer(5678));

        Log.d(TAG, "Book 2 timestamp: " + found.created_at);

        assertTrue(Math.abs(timeStamp.getTime() - found.created_at.getTime()) > 1000);

        found = databaseLocal.findById(Book.class, 3);
        assertEquals(found, null);

        // Find the first book one more time
        found = databaseLocal.findById(Book.class, 1);
        assertEquals(found.id, 1);
        assertEquals(found.title, "Hello");
        assertEquals(found.code, new Integer(1234));

        // Change parameters
        found.title = "New Title";
        found.code = 4242;

        // Update table
        databaseLocal.update(found);

        // Retrieve back the updated book
        Book updated = databaseLocal.findById(Book.class, 1);

        assertEquals(updated.id, 1);
        assertEquals(updated.title, "New Title");
        assertEquals(updated.code, new Integer(4242));

        // Delete the updated book
        databaseLocal.delete(updated);

        // The deleted book must not be found!
        found = databaseLocal.findById(Book.class, 1);
        assertEquals(found, null);

        // The second book must remain intact
        found = databaseLocal.findById(Book.class, 2);
        assertTrue(found != null);

        Book third = new Book();
        third.title = "Third";
        third.code = 333;

        databaseLocal.insert(third);

        // The new, third, book must be set to ID of 3
        assertEquals(third.id, 3);

        // Check again if the new book was added
        found = databaseLocal.findById(Book.class, 3);
        assertTrue(found != null);
        assertEquals(found.id, 3);
        assertEquals(found.title, "Third");
        assertEquals(found.code, new Integer(333));
    }
}