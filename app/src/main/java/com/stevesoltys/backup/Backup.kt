package com.stevesoltys.backup

import android.app.Application
import android.app.backup.IBackupManager
import android.content.Context.BACKUP_SERVICE
import android.os.Build
import android.os.ServiceManager.getService
import com.stevesoltys.backup.crypto.KeyManager
import com.stevesoltys.backup.crypto.KeyManagerImpl
import com.stevesoltys.backup.settings.SettingsManager

/**
 * @author Steve Soltys
 * @author Torsten Grote
 */
class Backup : Application() {

    companion object {
        val backupManager: IBackupManager by lazy {
            IBackupManager.Stub.asInterface(getService(BACKUP_SERVICE))
        }
        val keyManager: KeyManager by lazy {
            KeyManagerImpl()
        }
    }

    val settingsManager by lazy {
        SettingsManager(this)
    }
    val notificationManager by lazy {
        BackupNotificationManager(this)
    }

}

fun isDebugBuild() = Build.TYPE == "userdebug"
