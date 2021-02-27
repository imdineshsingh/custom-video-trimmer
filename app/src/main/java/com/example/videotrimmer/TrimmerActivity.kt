package com.example.videotrimmer

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.videotrimmer.k4lVideoTrimmer.K4LVideoTrimmer
import com.example.videotrimmer.k4lVideoTrimmer.interfaces.OnK4LVideoListener
import com.example.videotrimmer.k4lVideoTrimmer.interfaces.OnTrimVideoListener

class TrimmerActivity : AppCompatActivity(), OnTrimVideoListener,
    OnK4LVideoListener {
    private var mVideoTrimmer: K4LVideoTrimmer? = null
    private var mProgressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trimmer)
        val extraIntent = intent
        var path: String? = ""
        if (extraIntent != null) {
            path = extraIntent.getStringExtra(MainActivity.EXTRA_VIDEO_PATH)
        }

        //setting progressbar
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setMessage(getString(R.string.trimming_progress))
        mVideoTrimmer = findViewById<View>(R.id.timeLine) as K4LVideoTrimmer
        if (mVideoTrimmer != null) {
            mVideoTrimmer!!.setMaxDuration(30)
            mVideoTrimmer!!.setOnTrimVideoListener(this)
            mVideoTrimmer!!.setOnK4LVideoListener(this)
            mVideoTrimmer!!.setDestinationPath("/storage/emulated/0/DCIM/trimmer/")
            mVideoTrimmer!!.setVideoURI(Uri.parse(path))
            mVideoTrimmer!!.setVideoInformationVisibility(true)
        }
    }

    override fun onTrimStarted() {
        mProgressDialog!!.show()
    }

    override fun getResult(uri: Uri) {
        mProgressDialog!!.cancel()
        runOnUiThread {
            Toast.makeText(
                this@TrimmerActivity,
                getString(R.string.video_saved_at, uri.getPath()),
                Toast.LENGTH_SHORT
            ).show()
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setDataAndType(uri, "video/mp4")
        startActivity(intent)
        finish()
    }

    override fun cancelAction() {
        mProgressDialog!!.cancel()
        mVideoTrimmer!!.destroy()
        finish()
    }

    override fun onError(message: String?) {
        mProgressDialog!!.cancel()
        runOnUiThread { Toast.makeText(this@TrimmerActivity, message, Toast.LENGTH_SHORT).show() }
    }

    override fun onVideoPrepared() {
        runOnUiThread {
            Toast.makeText(this@TrimmerActivity, "onVideoPrepared", Toast.LENGTH_SHORT).show()
        }
    }
}
