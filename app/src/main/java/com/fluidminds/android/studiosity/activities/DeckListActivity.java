package com.fluidminds.android.studiosity.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.eventbus.SubjectChangedEvent;
import com.fluidminds.android.studiosity.models.SubjectModel;

import org.greenrobot.eventbus.Subscribe;

/**
 * An activity to display Card Decks for the requested Subject.
 */
public class DeckListActivity extends BaseActivity {

    private SubjectModel mSubjectModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deck_list);

        Intent intent = getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle(mSubjectModel.getSubject());
        }

        // Register as a subscriber
        mEventBus.register(this);
    }

    @Override
    protected void onDestroy() {
        // Unregister
        mEventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deck_list, menu);
        mMenu = menu;

        // color toolbar based on model Theme Color
        colorizeToolbar(mSubjectModel.getColorInt());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case android.R.id.edit:
                Intent intent = new Intent(this, SubjectEditActivity.class);
                intent.putExtra("subjectmodel", mSubjectModel);

                startActivity(intent);
                return true;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_delete_title)
                        .setMessage(String.format(getString(R.string.delete_subject), mSubjectModel.getSubject()))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String error = mSubjectModel.delete();
                                if (error.isEmpty())
                                    finish();
                                else {
                                    Toast.makeText(DeckListActivity.this, error, Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEvent(SubjectChangedEvent event){
        mSubjectModel = event.getModel();

        colorizeToolbar(mSubjectModel.getColorInt());

        getSupportActionBar().setTitle(mSubjectModel.getSubject());
    }
}
