package adapters

import adapters.callbacks.CodecCallback
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.estanesti.isogen.databinding.AdapterRecyclerIsoBinding
import models.Codec

class AdapterRecyclerIso() : ListAdapter<Codec, AdapterRecyclerIso.ViewHolder>(CodecCallback()) {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(AdapterRecyclerIsoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.b.tvContent.text = item.content
        holder.b.ivCopy.visibility = if (item.isIso) View.VISIBLE else View.GONE
        holder.b.ivCopy.setOnClickListener {
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("ISO", item.content))
            Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(val b: AdapterRecyclerIsoBinding) : RecyclerView.ViewHolder(b.root)
}