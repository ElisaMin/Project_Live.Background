package org.yanzuwu.live.administrator

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import java.lang.Exception

abstract class RepositoryTesting(
        private val context: Context
) {

    init {
        checkUri()
    }
    abstract fun getBaseUrl():String
    private fun checkUri() {
        if (getBaseUrl().last() == '/') throw Exception("do not put the '/' on end")
    }
    private fun getUrl(path: String) = Uri.parse("${getBaseUrl()}$path")

    fun getTesting(path:String) {
        context.contentResolver.query(

        )
    }
}

class Providers : ContentProvider {
    override fun onCreate(): Boolean {
        
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {

    }

    override fun getType(uri: Uri): String? {

    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }
}