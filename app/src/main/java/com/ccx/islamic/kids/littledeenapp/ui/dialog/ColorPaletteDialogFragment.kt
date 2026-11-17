package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ccx.islamic.kids.littledeenapp.databinding.DialogColorPaletteDrawingPadBinding
import com.ccx.islamic.kids.littledeenapp.ui.adapter.ColorPaletteAdapter


class ColorPaletteDialogFragment : DialogFragment() {

    private var _binding: DialogColorPaletteDrawingPadBinding? = null
    private val binding get() = _binding!!

    private var initialColor: Int = Color.parseColor("#E91E63")
    private var onColorPicked: ((Int) -> Unit)? = null

    // 16 curated kids-friendly vibrant colors
    private val colorList = listOf(
        Color.parseColor("#E91E63"), // Pink
        Color.parseColor("#9C27B0"), // Purple
        Color.parseColor("#673AB7"), // Deep Purple
        Color.parseColor("#3F51B5"), // Indigo
        Color.parseColor("#2196F3"), // Blue
        Color.parseColor("#00BCD4"), // Cyan
        Color.parseColor("#009688"), // Teal
        Color.parseColor("#4CAF50"), // Green
        Color.parseColor("#8BC34A"), // Light Green
        Color.parseColor("#CDDC39"), // Lime
        Color.parseColor("#FFEB3B"), // Yellow
        Color.parseColor("#FF9800"), // Orange
        Color.parseColor("#FF5722"), // Deep Orange / Coral
        Color.parseColor("#795548"), // Brown
        Color.parseColor("#607D8B"), // Blue Grey
        Color.parseColor("#000000")  // Black
    )

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogColorPaletteDrawingPadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ColorPaletteAdapter(colorList, initialColor) { chosenColor ->
            onColorPicked?.invoke(chosenColor)
            dismiss()
        }

        binding.rvColorGrid.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ColorPaletteDialogFragment"

        fun newInstance(
            currentColor: Int,
            onColorPicked: (Int) -> Unit
        ): ColorPaletteDialogFragment {
            return ColorPaletteDialogFragment().apply {
                this.initialColor = currentColor
                this.onColorPicked = onColorPicked
            }
        }
    }
}