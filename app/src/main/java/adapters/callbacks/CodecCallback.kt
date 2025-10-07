package adapters.callbacks

import androidx.recyclerview.widget.DiffUtil
import models.Codec

class CodecCallback : DiffUtil.ItemCallback<Codec>() {
    override fun areItemsTheSame(oldItem: Codec, newItem: Codec): Boolean = oldItem.content == newItem.content

    override fun areContentsTheSame(oldItem: Codec, newItem: Codec): Boolean = oldItem == newItem
}