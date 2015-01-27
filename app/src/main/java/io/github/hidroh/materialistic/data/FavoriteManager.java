package io.github.hidroh.materialistic.data;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.CursorWrapper;
import android.text.format.DateUtils;

import io.github.hidroh.materialistic.HackerNewsClient;
import io.github.hidroh.materialistic.R;

public class FavoriteManager {

    public static final int LOADER = 0;

    public static void add(Context context, HackerNewsClient.Item story) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_ITEM_ID, String.valueOf(story.getId()));
        contentValues.put(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_URL, String.valueOf(story.getUrl()));
        contentValues.put(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_TITLE, String.valueOf(story.getTitle()));
        contentValues.put(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_TIME, String.valueOf(System.currentTimeMillis()));
        new AsyncQueryHandler(context.getContentResolver()) { }
                .startInsert(0, null, MaterialisticProvider.URI_FAVORITE, contentValues);
    }

    public static void clear(Context context) {
        new AsyncQueryHandler(context.getContentResolver()) { }
                .startDelete(0, null, MaterialisticProvider.URI_FAVORITE, null, null);
    }

    public static class Favorite {
        private String itemId;
        private String url;
        private String title;
        private long time;

        private Favorite(String itemId, String url, String title, long time) {
            this.itemId = itemId;
            this.url = url;
            this.title = title;
            this.time = time;
        }

        public String getItemId() {
            return itemId;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getCreated(Context context) {
            return context.getString(R.string.saved, DateUtils.getRelativeDateTimeString(context, time,
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.YEAR_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_MONTH));
        }
    }

    public static class Cursor extends CursorWrapper {
        public Cursor(android.database.Cursor cursor) {
            super(cursor);
        }

        public Favorite getFavorite() {
            final String itemId = getString(getColumnIndexOrThrow(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_ITEM_ID));
            final String url = getString(getColumnIndexOrThrow(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_URL));
            final String title = getString(getColumnIndexOrThrow(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_TITLE));
            final String time = getString(getColumnIndexOrThrow(MaterialisticProvider.FavoriteEntry.COLUMN_NAME_TIME));
            return new Favorite(itemId, url, title, Long.valueOf(time));
        }
    }

    public static class CursorLoader extends android.support.v4.content.CursorLoader {
        public CursorLoader(Context context) {
            super(context, MaterialisticProvider.URI_FAVORITE, null, null, null, null);
        }
    }

}
