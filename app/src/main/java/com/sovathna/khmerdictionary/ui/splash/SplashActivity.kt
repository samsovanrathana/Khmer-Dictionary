package com.sovathna.khmerdictionary.ui.splash

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.sovathna.androidmvi.activity.MviActivity
import com.sovathna.khmerdictionary.Const
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import com.sovathna.khmerdictionary.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.layout_download.view.*
import kotlinx.android.synthetic.main.layout_error.view.*

@AndroidEntryPoint
class SplashActivity :
  MviActivity<SplashIntent, SplashState, SplashViewModel>(
    R.layout.activity_splash
  ) {

  override val viewModel: SplashViewModel by viewModels()

  private val checkDatabase = PublishSubject.create<SplashIntent.CheckDatabase>()

  override fun intents(): Observable<SplashIntent> =
    checkDatabase.cast(SplashIntent::class.java)

  override fun render(state: SplashState) {
    with(state) {
      if (isInit) {
        checkDatabase
          .onNext(
            SplashIntent.CheckDatabase(
              getDatabasePath(Const.DB_NAME),
              getFileStreamPath(Const.DB_TMP_NAME)
            )
          )
      }

      successEvent?.getContentIfNotHandled()?.let {
        val intent = Intent(
          this@SplashActivity,
          MainActivity::class.java
        )
        intent.addFlags(
          Intent.FLAG_ACTIVITY_NEW_TASK or
              Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
      }

      pb.visibility = when {
        isProgress -> View.VISIBLE
        else -> View.GONE
      }

      if (!isInit && !isProgress) {
        vs_download?.inflate()
        findViewById<View>(R.id.layout_download)?.let {
          it.visibility = View.VISIBLE
          it.ld_pb.isIndeterminate = downloaded == total
          if (downloaded != null && total != null) {
            if (downloaded == total) {
              if (total == 0L) {
                it.ld_tv_title.text = "រៀបចំទាញយកទិន្នន័យ"
                it.ld_tv_sub_title.text = "កំពុងដំណើរការ..."
              } else {
                it.ld_tv_title.text = "ពន្លា និងរក្សាទុកទិន្នន័យ"
                it.ld_tv_sub_title.text = String.format(
                  "ទំហំ %s",
                  getFileSize(total)
                )
              }
            } else {
              it.ld_tv_title.text = "ទាញយកទិន្នន័យ"
              it.ld_tv_sub_title.text = String.format(
                "ទំហំ %s នៃ %s",
                getFileSize(downloaded),
                getFileSize(total)
              )
            }
            it.ld_pb.max = total.toInt()
            it.ld_pb.progress = downloaded.toInt()
          } else {
            it.ld_tv_title.text = "រៀបចំទាញយកទិន្នន័យ"
            it.ld_tv_sub_title.text = "កំពុងដំណើរការ..."
          }
        }
      }

      if (error == null) {
        findViewById<View>(R.id.layout_error)?.visibility = View.GONE
      }

      error?.let {
        findViewById<View>(R.id.layout_download)?.visibility = View.GONE
        vs_error?.inflate()
        findViewById<View>(R.id.layout_error)?.let {
          it.visibility = View.VISIBLE
          it.le_btn_retry.setOnClickListener {
            checkDatabase.onNext(
              SplashIntent.CheckDatabase(
                getDatabasePath(Const.DB_NAME),
                getFileStreamPath(Const.DB_TMP_NAME)
              )
            )
          }
        }
      }

    }
  }

  private fun getFileSize(value: Long): String {
    return when {
      value >= 1_000_000 -> {
        String.format("%.2f MB", value / 1_000_000f)
      }
      value >= 1_000 -> {
        String.format("%.2f KB", value / 1_000f)
      }
      else -> {
        String.format("%d %s", value, if (value == 1L) "Byte" else "Bytes")
      }

    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    Toast
      .makeText(
        this,
        "បានអីមកវិញហ្នុង?",
        Toast.LENGTH_SHORT
      )
      .show()
  }
}