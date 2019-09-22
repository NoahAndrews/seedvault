package com.stevesoltys.backup.ui.recoverycode

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.stevesoltys.backup.R
import com.stevesoltys.backup.ui.BackupActivity
import com.stevesoltys.backup.ui.INTENT_EXTRA_IS_RESTORE
import com.stevesoltys.backup.ui.INTENT_EXTRA_IS_SETUP_WIZARD
import com.stevesoltys.backup.ui.LiveEventHandler

class RecoveryCodeActivity : BackupActivity() {

    private lateinit var viewModel: RecoveryCodeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isSetupWizard()) hideSystemUI()

        setContentView(R.layout.activity_recovery_code)

        viewModel = ViewModelProviders.of(this).get(RecoveryCodeViewModel::class.java)
        viewModel.isRestore = isRestore()
        viewModel.confirmButtonClicked.observeEvent(this, LiveEventHandler { clicked ->
            if (clicked) showInput(true)
        })
        viewModel.recoveryCodeSaved.observeEvent(this, LiveEventHandler { saved ->
            if (saved) {
                setResult(RESULT_OK)
                finishAfterTransition()
            }
        })

        if (savedInstanceState == null) {
            if (viewModel.isRestore) showInput(false)
            else showOutput()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showOutput() {
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment, RecoveryCodeOutputFragment(), "Code")
                .commit()
    }

    private fun showInput(addToBackStack: Boolean) {
        val tag = "Confirm"
        val fragmentTransaction = supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, RecoveryCodeInputFragment(), tag)
        if (addToBackStack) fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    private fun isRestore(): Boolean {
        return intent?.getBooleanExtra(INTENT_EXTRA_IS_RESTORE, false) ?: false
    }

    private fun isSetupWizard(): Boolean {
        return intent?.getBooleanExtra(INTENT_EXTRA_IS_SETUP_WIZARD, false) ?: false
    }

}