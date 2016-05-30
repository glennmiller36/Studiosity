package com.fluidminds.android.studiosity.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fluidminds.android.studiosity.data.DataContract.CardEntry;
import com.fluidminds.android.studiosity.data.DataContract.DeckEntry;
import com.fluidminds.android.studiosity.data.DataContract.SubjectEntry;
import com.fluidminds.android.studiosity.data.DataContract.QuizEntry;

/**
 * Manages a local database data.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "studiosity.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            // enable CASCADE DELETE
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold Subjects.
        final String SQL_CREATE_SUBJECT_TABLE = "CREATE TABLE " + SubjectEntry.TABLE_NAME + " (" +
                SubjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SubjectEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                SubjectEntry.COLUMN_COLOR + " INTEGER NOT NULL, " +
                SubjectEntry.IS_SAMPLE_DATA + " INTEGER NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_SUBJECT_TABLE);

        // Create a table to hold Card Decks.
        final String SQL_CREATE_DECK_TABLE = "CREATE TABLE " + DeckEntry.TABLE_NAME + " (" +
                DeckEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DeckEntry.COLUMN_SUBJECT_ID + " INTEGER NOT NULL, " +
                DeckEntry.COLUMN_NAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                " FOREIGN KEY(" + DeckEntry.COLUMN_SUBJECT_ID + ") REFERENCES " + SubjectEntry.TABLE_NAME + "(" + SubjectEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_DECK_TABLE);

        // Create a table to hold Cards for a Deck.
        final String SQL_CREATE_CARD_TABLE = "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CardEntry.COLUMN_DECK_ID + " INTEGER NOT NULL, " +
                CardEntry.COLUMN_QUESTION + " TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                CardEntry.COLUMN_ANSWER + " TEXT NOT NULL, " +
                CardEntry.COLUMN_RECENT_SCORES + " TEXT, " +
                CardEntry.COLUMN_PERCENT_CORRECT + " INTEGER NOT NULL, " +
                " FOREIGN KEY(" + CardEntry.COLUMN_DECK_ID + ") REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_CARD_TABLE);

        // Create a table to hold Quiz history.
        final String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE " + QuizEntry.TABLE_NAME + " (" +
                QuizEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QuizEntry.COLUMN_DECK_ID + " INTEGER NOT NULL, " +
                QuizEntry.COLUMN_START_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                QuizEntry.COLUMN_NUM_CORRECT + " INTEGER NOT NULL, " +
                QuizEntry.COLUMN_TOTAL_CARDS + " INTEGER NOT NULL, " +
                QuizEntry.COLUMN_PERCENT_CORRECT + " INTEGER NOT NULL, " +
                " FOREIGN KEY(" + QuizEntry.COLUMN_DECK_ID + ") REFERENCES " + DeckEntry.TABLE_NAME + "(" + DeckEntry._ID + ") ON DELETE CASCADE " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_QUIZ_TABLE);

        seedData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }

    private void seedData(SQLiteDatabase sqLiteDatabase) {

        // Subject
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('Geography', -14575885, 1)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('History', -769226, 1)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('Math', -16121, 1)");
        sqLiteDatabase.execSQL("INSERT INTO SUBJECT (Name, Color, IsSampleData) VALUES ('Science', -11751600, 1)");

        // Decks
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (1, 'Continents and Oceans')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (1, 'U.S. State Capitals')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (2, 'World War I')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (2, 'World War II')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (3, 'Math Terms')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (3, 'Polygons')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (4, 'Periodic Table')");
        sqLiteDatabase.execSQL("INSERT INTO DECK (SubjectId, Name) VALUES (4, 'Planets')");


        // Cards
        /* Continents and Oceans */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Name the oceans of the world', '5 oceans: Arctic, Atlantic, Indian, Pacific and Southern', '1,1,0,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Name the largest ocean', 'Pacific Ocean', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Name the smallest ocean', 'Arctic Ocean', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'What percent of the planet is ocean?', '70 %', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'What percent of life on Earth is aquatic?', '94 %', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Name the continents', '7 continents: North America, South America, Asia, Australia, Africa, Antarctica, and Europe', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Name the largest continent', 'Asia', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Name the smallest continent', 'Australia', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Which continent has no active volcanoes?', 'Australia', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (1, 'Which continent is the windiest?', 'Antarctica', '0,1,0,1,1', 60)");

        /* U.S. State Capitals */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Arizona', 'Phoenix', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'California', 'Sacramento', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Illinois', 'Springfield', '1,1,0,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Louisiana', 'Baton Rouge', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Maryland', 'Annapolis', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Massachusetts', 'Boston', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Minnesota', 'St. Paul', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'New York', 'Albany', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Ohio', 'Columbus', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (2, 'Washington', 'Olympia', '1,0,0,1,1', 60)");

        /* World War I */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'What treaty marked the end of World War I?', 'Treaty of Versailles', '0,0,0,1,1', 40)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Which country did Germany first declare war on?', 'Russia', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'What countries were considered to be The Allies?', 'France, Great Britain, Russia, Japan, Italy, and USA', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'What year did the United States declare war on Germany?', '1917', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Who was the commander of U.S. forces in Europe?', 'John J. Pershing', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'How many died in World War I?', '17 million', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'Where was Archduke Ferdinand assassinated?', 'Sarajevo, Bosnia', '0,1 0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (3, 'What was the Lusitania?', 'A British ocean liner sunk by a German U-boat', '0,1,1,0,1', 60)");

        /* World War 2 */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'Which general commanded the Allied invasion of Normandy, also known as D-Day?', 'Dwight D. Eisenhower', '0,0,1,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'What two countries have not signed a peace treaty ending World War II?', 'Russia and Japan', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'Which country did Germany invade first?', 'Poland', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'What countries were considered to be The Axis?', 'Germany, Japan, and Italy', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'Who were the U.S. presidents during World War II?', 'Franklin Delano Roosevelt (1933-1945) and Harry S. Truman (1945-1953)', '1,0,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'How long did World War II last?', '6 years (1939-1945)', '0,0,0,1,1', 40)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (4, 'What is the name of U.S. battleship that accounted for almost half the casualties at Pearl Harbor?', 'USS Arizona', '1,0,0,0,1', 40)");

        /* Math Terms */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Top symbol or number of a fraction', 'Numerator', '1,1,1,0,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Bottom symbol or number of a fraction', 'Denominator', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'A number that can be divided by only itself and one', 'Prime number', '0,1,0,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Horizontal axis on a coordinate graph', 'X-axis', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Vertical axis on a coordinate graph', 'Y-axis', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'The distance from the center outwards', 'Radius', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Goes straight across the circle, through the center', 'Diameter', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'The distance once around the circle', 'Circumference', '1,1,1,1,0', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Two or more lines which are always the same distance apart. They never meet.', 'Parallel lines', '0,1,1,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (5, 'Two lines which intersect at right angles', 'Perpendicular lines', '0,0,1,1,1', 60)");

        /* Polygons */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'What do you call a polygon with equal sides?', 'Regular Polygon', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Which polygon has exactly five sides?', 'Pentagon', '1,1,0,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'What is the name of a polygon with four sides?', 'Quadrilateral', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'Which polygon has the least number of sides?', 'Triangle', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'What is the name of a polygon with ten sides?', 'Decagon', '1,1,1,0,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'How many sides are there in an octagon?', 'Eight', '0,1,1,0,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'In a regular polygon, what do you call the distance from the center to the side?', 'Apothem', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (6, 'What is the name of a polygon with twelve sides?', 'Dodecagon', '0,0,1,1,1', 60)");

        /* Periodic Table */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'How many known elements are in the periodic table?', '118 elements', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'How are elements arranged?', 'Atomic number', '0,0,1,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'Which scientist came up with the concept of a periodic table?', 'Dmitri Mendeleev', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What are the rows called?', 'Periods', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What are the columns called?', 'Groups', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What does the atomic number of an element tell you?', 'The number of electrons and protons in a neutral atom', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What is the heaviest naturally occuring element?', 'Uranium (U)', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What is the heaviest man-made element?', 'Ununoctium (Uuo)', '0,0,0,1,1', 40)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What are the distinct physical forms that matter can take?', 'Solid, liquid, and gas', '0,0,1,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (7, 'What are the only liquid elements at standard temperature and pressure?', 'Bromine (Br) and Mercury (Hg)', '1,1,1,1,1', 100)");

        /* Planets */
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Name the planets', '8 planets: Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, and Neptune (Pluto is considered a Dwarf planet)', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'What is the largest planet?', 'Jupiter', '0,1,0,1,1', 60)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'What is the smallest planet?', 'Mercury', '1,1,0,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Which planet rotates the fastest?', 'Jupiter has the shortest rotational (spin) period', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'How old is the solar system?', '5 billion years', '1,1,1,0,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'How many days does it take the Earth to travel around the sun?', '365 days', '1,0,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'What planet is closet to Earth?', 'Mars', '0,1,1,1,1', 80)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'What is the only planet not named for a mythological god or goddess?', 'Earth', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'Which planet is the hottest?', 'Venus', '1,1,1,1,1', 100)");
        sqLiteDatabase.execSQL("INSERT INTO CARD (DeckId, Question, Answer, RecentScores, PercentCorrect) VALUES (8, 'What planet is the farthest from the Sun?', 'Neptune', '1,1,1,0,1', 80)");


        // Quiz History
        /* Continents and Oceans */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-21 14:01:00.000', 5, 10, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-22 07:04:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-23 10:11:00.000', 4, 10, 40)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-24 17:23:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (1, '2016-03-25 18:35:00.000', 10, 10, 100)");

        /* U.S. State Capitals */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-23 18:01:00.000', 5, 10, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-24 14:04:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-25 11:11:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-26 09:23:00.000', 10, 10, 100)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (2, '2016-03-27 21:35:00.000', 10, 10, 100)");

        /* World War I */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-08 10:01:00.000', 4, 8, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-09 15:04:00.000', 4, 8, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-10 08:11:00.000', 5, 8, 62)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-11 10:23:00.000', 7, 8, 87)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (3, '2016-03-12 13:35:00.000', 8, 8, 100)");

        /* World War II */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-10 09:01:00.000', 4, 7, 57)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-11 10:04:00.000', 2, 7, 28)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-12 07:11:00.000', 3, 7, 42)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-13 11:23:00.000', 6, 7, 85)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (4, '2016-03-14 14:35:00.000', 7, 7, 100)");

        /* Math Terms */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-15 08:01:00.000', 5, 10, 50)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-16 09:04:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-17 11:11:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-18 13:23:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (5, '2016-03-19 16:35:00.000', 9, 10, 90)");

        /* Polygons */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-11 15:01:00.000', 5, 8, 62)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-12 11:04:00.000', 6, 8, 75)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-13 08:11:00.000', 6, 8, 75)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-14 14:23:00.000', 6, 8, 75)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (6, '2016-03-15 15:35:00.000', 8, 8, 100)");

        /* Periodic Table */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-01 13:01:00.000', 3, 10, 30)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-02 08:04:00.000', 7, 10, 70)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-03 11:11:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-04 15:23:00.000', 10, 10, 100)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (7, '2016-03-05 18:35:00.000', 10, 10, 100)");

        /* Planets */
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-04 11:01:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-05 10:04:00.000', 9, 10, 90)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-06 09:11:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-07 14:23:00.000', 8, 10, 80)");
        sqLiteDatabase.execSQL("INSERT INTO QUIZ (DeckId, StartDate, NumCorrect, TotalCards, PercentCorrect) VALUES (8, '2016-03-08 16:35:00.000', 10, 10, 100)");
    }
}
