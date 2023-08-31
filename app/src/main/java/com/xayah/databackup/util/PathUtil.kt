package com.xayah.databackup.util

import android.annotation.SuppressLint
import android.content.Context
import com.xayah.databackup.DataBackupApplication
import java.nio.file.Paths
import kotlin.io.path.pathString

fun Context.filesPath(): String = filesDir.path

fun Context.binPath(): String = "${filesPath()}/bin"

fun Context.binArchivePath(): String = "${filesPath()}/bin.zip"

fun Context.extendPath(): String = "${filesPath()}/extend"

fun Context.iconPath(): String = "${filesPath()}/icon"

fun Context.databasePath(): String = "${filesDir.parent}/databases"

@SuppressLint("SdCardPath")
object PathUtil {
    fun getParentPath(path: String): String = Paths.get(path).parent.pathString

    // Paths for internal usage.
    fun getIconPath(context: Context, packageName: String): String = "${context.iconPath()}/${packageName}.png"

    // Exclude dirs while running `tree` command.
    fun getExcludeDirs(): List<String> = listOf("tree", "icon", "databases", "log")

    // Paths for backup save dir.
    private fun getBackupSavePath(): String = DataBackupApplication.application.readBackupSavePath()
    private fun getTreeSavePath(): String = "${getBackupSavePath()}/tree"
    fun getTreeSavePath(timestamp: Long): String = "${getTreeSavePath()}/tree_${timestamp}"
    fun getIconSavePath(): String = "${getBackupSavePath()}/icon"
    fun getIconNoMediaSavePath(): String = "${getIconSavePath()}/.nomedia"
    fun getDatabaseSavePath(): String = "${getBackupSavePath()}/databases"
    private fun getLogSavePath(): String = "${getBackupSavePath()}/log"
    fun getLogSavePath(timestamp: Long): String = "${getLogSavePath()}/log_${timestamp}"

    // Paths for processing.
    fun getPackageUserPath(userId: Int): String = "/data/user/${userId}"
    fun getPackageUserDePath(userId: Int): String = "/data/user_de/${userId}"
    fun getPackageDataPath(userId: Int): String = "/data/media/${userId}/Android/data"
    fun getPackageObbPath(userId: Int): String = "/data/media/${userId}/Android/obb"
    fun getPackageMediaPath(userId: Int): String = "/data/media/${userId}/Android/media"
}
