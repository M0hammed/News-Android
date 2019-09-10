package com.me.daggersample.customViews

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.me.daggersample.R

/**
 * created By Mohamed Abdel Mohsen on 9/8/2019
 */
class CustomProgressDialog(context: Context) : AlertDialog(context) {
    // dialog types
    companion object {
        const val SPINNER_PROGRESS_DIALOG = 0
        const val HORIZONTAL_PROGRESS_DIALOG = 1
        const val MESSAGE_DIALOG = 2
    }

    private var message: String? = null
    private var tvMessage: TextView? = null
    private var tvTitle: TextView? = null
    private var tvPositive: TextView? = null
    private var tvNegative: TextView? = null
    private var tvPercent: TextView? = null
    private var tvFilesNum: TextView? = null
    private var progressBar: ProgressBar? = null
    private var horizontalProgressbar: ProgressBar? = null
    private var title: String? = null
    private var lnClickableLayout: LinearLayout? = null
    private var lnHorizontalProgressbar: LinearLayout? = null
    private var positiveCallback: DialogButtonCallback? = null
    private var negativeCallback: DialogButtonCallback? = null
    private var negativeMessage: String? = null
    private var positiveMessage: String? = null
    private var type = SPINNER_PROGRESS_DIALOG
    private var maxProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog_layout)
        iniViews()
        initComponents()
        clickListeners()
    }

    // init Views
    private fun iniViews() {
        tvMessage = findViewById(R.id.tvMessage)
        progressBar = findViewById(R.id.circularProgressBar)
        horizontalProgressbar = findViewById(R.id.horizontalProgressbar)
        tvTitle = findViewById(R.id.tvTitle)
        tvPositive = findViewById(R.id.tvPositive)
        tvNegative = findViewById(R.id.tvNegative)
        lnClickableLayout = findViewById(R.id.lnClickableLayout)
        lnHorizontalProgressbar = findViewById(R.id.lnHorizontalProgressLayout)
        tvPercent = findViewById(R.id.tvPercent)
        tvFilesNum = findViewById(R.id.tvFilesNum)
    }

    // init components
    private fun initComponents() {
        initDialogType(type)

        tvMessage!!.text = message

        if (title != null && !TextUtils.isEmpty(title))
            tvTitle!!.text = title

        if (positiveMessage != null)
            tvPositive!!.text = positiveMessage

        if (negativeMessage != null)
            tvNegative!!.text = negativeMessage

        if (horizontalProgressbar != null && horizontalProgressbar!!.getVisibility() == View.VISIBLE) {
            horizontalProgressbar!!.max = maxProgress
            updateProgressIncrement("0", "0")
        }

    }

    private fun clickListeners() {
        tvPositive!!.setOnClickListener(View.OnClickListener {
            if (positiveCallback != null) {
                positiveCallback!!.onClick(this@CustomProgressDialog)
            }
        })

        tvNegative!!.setOnClickListener(View.OnClickListener {
            if (negativeCallback != null) {
                negativeCallback!!.onClick(this@CustomProgressDialog)
            }
        })
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setCancellable(cancellable: Boolean) {
        setCancelable(cancellable)
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun setTitle(title: String) {
        this.title = title
    }

    // call back for positive btn
    fun setPositiveCallback(positiveBtnText: String, dialogButtonCallback: DialogButtonCallback) {
        this.positiveMessage = positiveBtnText
        this.positiveCallback = dialogButtonCallback
    }

    // call back for negative btn
    fun setNegativeCallback(negativeMessage: String, dialogButtonCallback: DialogButtonCallback) {
        this.negativeMessage = negativeMessage
        this.negativeCallback = dialogButtonCallback
    }


    fun setMaxProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
    }

    fun incrementProgress(incrementBy: Int) {
        if (horizontalProgressbar != null) {
            val currentProgress = horizontalProgressbar!!.getProgress()
            val currentPercentNum = currentProgress + incrementBy
            horizontalProgressbar!!.setProgress(currentPercentNum)
            updateProgressIncrement(
                currentPercentNum.toString(),
                getCurrentPercentValue(currentPercentNum).toString()
            )
        }
    }

    private fun updateProgressIncrement(currentFile: String, progress: String) {
//        if (tvFilesNum != null && tvPercent != null) {
//            tvFilesNum!!.text = getContext().getString(
//                R.string.progress_files_num, currentFile,
//                maxProgress.toString()
//            )
//            tvPercent!!.setText(getContext().getString(R.string.files_transfer_percent, progress))
//        }
    }

    private fun getCurrentPercentValue(currentPercentNum: Int): Int {
        val currentPercentValue = currentPercentNum.toFloat() / maxProgress

        return (currentPercentValue * 100).toInt()
    }

    interface DialogButtonCallback {
        fun onClick(dialog: CustomProgressDialog)
    }

    private fun initDialogType(dialogType: Int) {
        when (dialogType) {
            MESSAGE_DIALOG -> {
                progressBar!!.visibility = View.GONE
                tvTitle!!.setVisibility(View.VISIBLE)
                lnClickableLayout!!.visibility = View.VISIBLE
                lnHorizontalProgressbar!!.visibility = View.GONE
            }

            HORIZONTAL_PROGRESS_DIALOG -> {
                progressBar!!.visibility = View.GONE
                tvTitle!!.setVisibility(View.VISIBLE)
                lnClickableLayout!!.visibility = View.GONE
                lnHorizontalProgressbar!!.visibility = View.VISIBLE
            }
            SPINNER_PROGRESS_DIALOG -> {
                progressBar!!.visibility = View.VISIBLE
                tvTitle!!.visibility = View.GONE
                lnClickableLayout!!.visibility = View.GONE
                lnHorizontalProgressbar!!.visibility = View.GONE
            }
            else -> {
                progressBar!!.visibility = View.VISIBLE
                tvTitle!!.visibility = View.GONE
                lnClickableLayout!!.visibility = View.GONE
                lnHorizontalProgressbar!!.visibility = View.GONE
            }
        }
    }
}
