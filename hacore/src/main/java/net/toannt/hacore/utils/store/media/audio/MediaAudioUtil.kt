package net.toannt.hacore.utils.store.media.audio

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore

const val MUSIC_ONLY_SELECTION = (MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''")

object MediaAudioUtil {

    fun getPlayListCursor(context : Context, projection: Array<String>, selection: String? = null, selectionArgs: Array<String>? = null, sortOrder: String? = null) : Cursor {
        val resolver = context.contentResolver
        return resolver.query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder)
    }

    fun getSongCountForPlaylist(context: Context, playlistId: Long): Int {
        var cursor = context.contentResolver.query(
            MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
            arrayOf(BaseColumns._ID), MUSIC_ONLY_SELECTION, null, null
        )

        cursor?.let {
            var count = 0
            if (it.moveToFirst()) {
                count = it.count
            }
            it.close()
            cursor = null
            return count
        }
        return 0
    }





}