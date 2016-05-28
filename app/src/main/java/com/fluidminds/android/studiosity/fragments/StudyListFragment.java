package com.fluidminds.android.studiosity.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.StudyListAdapter;
import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.DividerItemDecoration;

import java.util.ArrayList;

/**
 * A fragment to display Cards for studying.
 */
public class StudyListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private StudyListAdapter mStudyAdapter;

    private RecyclerView mRecyclerCards;
    private TextView mNoRecords;

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;

    private static final int CARD_LOADER = 0;

    private static final String[] CARD_COLUMNS = {
        CardEntry.TABLE_NAME + "." + CardEntry._ID,
        CardEntry.COLUMN_DECK_ID,
        CardEntry.COLUMN_QUESTION,
        CardEntry.COLUMN_ANSWER,
        CardEntry.COLUMN_RECENT_SCORES,
        CardEntry.COLUMN_PERCENT_CORRECT
    };

    // These indices are tied to CARD_COLUMNS.
    public static final int COL_ID = 0;
    public static final int COL_QUESTION = 1;
    public static final int COL_ANSWER = 2;
    public static final int COL_RECENT_SCORES = 3;
    public static final int COL_PERCENT_CORRECT = 4;

    public StudyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mStudyAdapter = new StudyListAdapter();

        Intent intent = getActivity().getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");
        mDeckModel = intent.getParcelableExtra("deckmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_list, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecyclerCards = (RecyclerView) view.findViewById(R.id.recyclerCards);
        mRecyclerCards.setHasFixedSize(true);
        mRecyclerCards.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerCards.setAdapter(mStudyAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerCards.addItemDecoration(itemDecoration);

        mStudyAdapter.setOnItemClickListener(new StudyListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                // do nothing
            }
        });
        mNoRecords = (TextView) view.findViewById(R.id.textNoRecords);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CARD_LOADER, null, this).forceLoad();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // query Cards for the given parent Deck
        String selection = CardEntry.COLUMN_DECK_ID + " = ?";
        String[] selectionArgs = new String[] {
            mDeckModel.getId().toString()
        };

        return new CursorLoader(getActivity(),
                CardEntry.CONTENT_URI,
                CARD_COLUMNS,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<CardModel> cards = new ArrayList<>();
        while (data.moveToNext()) {
            CardModel model = new CardModel(data.getLong(0), data.getLong(1), data.getString(2), data.getString(3), data.getString(4), data.getInt(5));
            cards.add(model);
        }

        mStudyAdapter.swapData(cards);

        mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mStudyAdapter.swapData(null);
    }
}
