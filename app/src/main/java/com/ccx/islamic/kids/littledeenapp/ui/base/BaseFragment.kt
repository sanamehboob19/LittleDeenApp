package com.ccx.islamic.kids.littledeenapp.ui.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment()
{

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding cannot be accessed when view is not created")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        initObservers()
    }

    /**
     * Override to initialize views, click listeners, and adapters.
     */
    protected abstract fun setupViews()

    /**
     * Override to observe ViewModel LiveData / StateFlow.
     */
    protected open fun initObservers() {}

    override fun onDestroyView() {
        super.onDestroyView()
        // Prevents memory leaks in Fragments
        _binding = null
    }
}