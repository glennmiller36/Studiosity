package com.fluidminds.android.studiosity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.fragments.SlideFragment;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Cool intro for Studiosity.
 */
public final class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(SlideFragment.newInstance(R.layout.fragment_intro_subjects));
        addSlide(SlideFragment.newInstance(R.layout.fragment_intro_decks));
        addSlide(SlideFragment.newInstance(R.layout.fragment_intro_cards));
        addSlide(SlideFragment.newInstance(R.layout.fragment_intro_study));
        addSlide(SlideFragment.newInstance(R.layout.fragment_intro_quiz));
        addSlide(SlideFragment.newInstance(R.layout.fragment_intro_stats));
    }

    private void loadMainActivity(){
        Intent intent = new Intent(this, SubjectListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }
}