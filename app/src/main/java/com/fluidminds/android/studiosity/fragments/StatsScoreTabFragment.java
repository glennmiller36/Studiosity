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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fluidminds.android.studiosity.R;
import com.fluidminds.android.studiosity.adapters.StatsScoreAdapter;
import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.eventbus.NoQuizzesForDeckEvent;
import com.fluidminds.android.studiosity.models.CardModel;
import com.fluidminds.android.studiosity.models.DeckModel;
import com.fluidminds.android.studiosity.models.SubjectModel;
import com.fluidminds.android.studiosity.utils.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Stats for each individual Question..
 */
public class StatsScoreTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private StatsScoreAdapter mScoreAdapter;

    private RecyclerView mRecyclerCards;
    private LinearLayout mLastFiveQuizzesMessage;
    private TextView mNoRecords;

    private SubjectModel mSubjectModel;
    private DeckModel mDeckModel;

    protected EventBus mEventBus = EventBus.getDefault();

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

    public StatsScoreTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mScoreAdapter = new StatsScoreAdapter();

        Intent intent = getActivity().getIntent();
        mSubjectModel = intent.getParcelableExtra("subjectmodel");
        mDeckModel = intent.getParcelableExtra("deckmodel");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats_score_tab, container, false);

        // Get a reference to the RecyclerView, and attach this adapter to it.
        mRecyclerCards = (RecyclerView) view.findViewById(R.id.recyclerCards);
        mRecyclerCards.setHasFixedSize(true);
        mRecyclerCards.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerCards.setAdapter(mScoreAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerCards.addItemDecoration(itemDecoration);

        mScoreAdapter.setOnItemClickListener(new StatsScoreAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                // do nothing
            }
        });

        mLastFiveQuizzesMessage = (LinearLayout) view.findViewById(R.id.linearLastFiveQuizzesMessage);
        mNoRecords = (TextView) view.findViewById(R.id.textNoRecords);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CARD_LOADER, null, this).forceLoad();

        // Register as a subscriber
        mEventBus.register(this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // Unregister
        mEventBus.unregister(this);
        super.onDestroy();
    }

    /**
     * Called when a new Loader needs to be created.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Sort order:  Descending by PercentComplete.
        String sortOrder = CardEntry.COLUMN_PERCENT_CORRECT + " COLLATE NOCASE DESC";

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
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<CardModel> cards = new ArrayList<>();
        while (data.moveToNext()) {
            CardModel model = new CardModel(data.getLong(0), data.getLong(1), data.getString(2), data.getString(3), data.getString(4), data.getInt(5));
            cards.add(model);
        }

        // display Cards unless Stats Trend tab has already determined that no Quizzes exist
        if (mNoRecords.getVisibility() == View.GONE) {
            mScoreAdapter.swapData(cards);

            mNoRecords.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mScoreAdapter.swapData(null);
    }

    @Subscribe
    public void onEvent(NoQuizzesForDeckEvent event){
        // Stats Trend tab LoadCursor returned No Quizzes so honor that on the Stats Score tab
        if (event.getModel().getId() == mDeckModel.getId()) {
            mLastFiveQuizzesMessage.setVisibility(View.GONE);
            mRecyclerCards.setVisibility(View.GONE);
            mNoRecords.setVisibility(View.VISIBLE);
        }
    }
}
