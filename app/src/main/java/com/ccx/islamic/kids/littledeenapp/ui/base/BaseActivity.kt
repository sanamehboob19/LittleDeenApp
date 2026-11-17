package com.ccx.islamic.kids.littledeenapp.ui.base


import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding



abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> VB
) : AppCompatActivity()
{

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding cannot be accessed before onCreate or after onDestroy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)
        setContentView(binding.root)

        setupViews()
        initObservers()
    }

    /**
     * Override to set up UI components, adapters, click listeners, etc.
     */
    protected abstract fun setupViews()

    /**
     * Override to observe ViewModel LiveData / StateFlow.
     */
    protected open fun initObservers() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}