package com.maxistar.morsetrainer;

import com.maxistar.morsetrainer.activities.MainActivity;
import com.maxistar.morsetrainer.activities.TrainingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.ContentView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import androidx.test.filters.LargeTest;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.locale.LocaleTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

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
    public void goToTraining() {
        onView(withId(R.id.button_training)).check(matches(isDisplayed()));
        Screengrab.screenshot("main");
        onView(withId(R.id.button_training)).perform(click());
        Screengrab.screenshot("training");
    }

    @Test
    public void goToProgress() {
        onView(withId(R.id.button_progress)).perform(click());
        Screengrab.screenshot("progress");
    }
}