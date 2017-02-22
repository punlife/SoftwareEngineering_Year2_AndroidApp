package com.com2027.group03;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by Matus on 22-Feb-17.
 */

// Replace MainActivity for any activity class
public class ActivityTest extends ActivityUnitTestCase<MainActivity>{
    public ActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void test(){

    }
}
