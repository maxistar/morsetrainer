package com.maxistar.morsetrainer;

import com.maxistar.morsetrainer.activities.TrainingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import androidx.test.filters.LargeTest;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import tools.fastlane.screengrab.Screengrab;

import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrainingActivityTest {

    @Rule
    public ActivityScenarioRule<TrainingActivity> activityRule
            = new ActivityScenarioRule<>(TrainingActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCreate() {
        //onView(withId(R.id.code)).check(matches(withText("")));
                //.perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
    }

    @Test
    public void changeText_sameActivity() {

        // Screengrab.screenshot("training");
        // Type text and then press the button.
        //onView(withId(R.id.editTextUserInput))
        //        .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        //onView(withId(R.id.changeTextBt)).perform(click());

        // Check that the text was changed.
        //onView(withId(R.id.code)).check(matches(withText("")));
    }

    @Test
    public void addMorseCodes() {
    }
}