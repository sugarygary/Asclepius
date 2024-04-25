package com.dicoding.asclepius.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.room.entity.Scan
import com.dicoding.asclepius.databinding.ItemScanBinding
import com.dicoding.asclepius.helper.renderBlob


class ScanAdapter(private val onClickRoot: (Scan) -> Unit, private val onClickDelete: (Scan) -> Unit) :
    ListAdapter<Scan, ScanAdapter.ScanViewHolder>(DIFF_CALLBACK) {
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val binding = ItemScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        this.parent = parent
        return ScanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            when (item.label) {
                "Cancer" -> {
                    indicator.progressDrawable =
                        AppCompatResources.getDrawable(
                            parent.context,
                            R.drawable.progress_circle_cancer
                        )
                }

                else -> {
                    indicator.progressDrawable =
                        AppCompatResources.getDrawable(
                            parent.context,
                            R.drawable.progress_circle_non_cancer
                        )
                }
            }
            ivScan.renderBlob(item.scanImgBlob)
            val animator = ValueAnimator.ofInt(0, item.confidenceScore)
            indicator.setProgress(0, true)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.startDelay = 0
            animator.setDuration(2000)
            animator.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                indicator.setProgress(value, true)
            }
            tvTimestamp.text = item.timestamp
            tvScore.text = parent.context.getString(R.string.score_percent, item.confidenceScore)
            tvLabel.text = item.label
            ibDelete.setOnClickListener {
                onClickDelete.invoke(item)
            }
            root.setOnClickListener {
                onClickRoot.invoke(item)
            }
            animator.start()
        }
    }

    inner class ScanViewHolder(val binding: ItemScanBinding) : ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Scan>() {
            override fun areItemsTheSame(oldItem: Scan, newItem: Scan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Scan, newItem: Scan): Boolean {
                return oldItem == newItem
            }
        }
    }
}