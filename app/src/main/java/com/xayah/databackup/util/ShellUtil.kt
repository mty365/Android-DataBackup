package com.xayah.databackup.util

import android.content.Context
import android.content.Intent
import com.topjohnwu.superuser.Shell
import com.xayah.databackup.ConsoleActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ShellUtil(private val mContext: Context) {
    private val scriptVersion = "V11.9"
    private val appListFileName = "应用列表.txt"
    private val logFileName = "执行状态日志.txt"
    private val generateAppListScriptName = "生成应用列表.sh"

    var isSuccess = false

    val console = mutableListOf<String>()

    private val filesPath: String = mContext.getExternalFilesDir(null)!!.absolutePath
    private val sdcardPath: String =
        filesPath.replace("/Android/data/com.xayah.databackup/files", "")
    private val scriptPath: String = "$sdcardPath/DataBackup/scripts"

    fun extractAssets() {
        try {
            val assets = File(filesPath, "$scriptVersion.zip")
            if (!assets.exists()) {
                val outStream = FileOutputStream(assets)
                val inputStream = mContext.resources.assets.open("$scriptVersion.zip")
                val buffer = ByteArray(1024)
                var byteCount: Int
                while (inputStream.read(buffer).also { byteCount = it } != -1) {
                    outStream.write(buffer, 0, byteCount)
                }
                outStream.flush()
                inputStream.close()
                outStream.close()
            }
            unzip("$filesPath/$scriptVersion.zip", "$sdcardPath/DataBackup/scripts")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun unzip(filePath: String, out: String) {
        if (mkdir(out))
            Shell.su("unzip $filePath -d $out").exec()
    }

    fun mkdir(path: String): Boolean {
        return Shell.su("mkdir $path").exec().isSuccess
    }

    fun rm(path: String): Boolean {
        return Shell.su("rm $path").exec().isSuccess
    }

    fun generateAppList() {
        rm("$scriptPath/$appListFileName")
        rm("$scriptPath/$logFileName")
        GlobalScope.launch() {
            isSuccess =
                Shell.su("sh $scriptPath/$generateAppListScriptName").to(console)
                    .exec().isSuccess
        }
    }

    fun onGenerateAppList() {
        val intent = Intent(mContext, ConsoleActivity::class.java)
        intent.putExtra("type", "generateAppList")
        mContext.startActivity(intent)
    }
}