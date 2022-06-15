package com.example.appericolo.ui.preferiti

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.appericolo.databinding.FragmentPreferitiBinding
import com.example.appericolo_from_bottomnavigation.ui.preferiti.PreferitiViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class PreferitiFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private var _binding: FragmentPreferitiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPreferitiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        tabLayout = binding.tabLayout
        viewPager = binding.preferitiViewpager
        val newFragment = this.context?.let { PreferitiViewPagerAdapter(it, childFragmentManager, 2) }
        viewPager.adapter = newFragment
        tabLayout.setupWithViewPager(viewPager)



        //val textView: TextView = binding.textNotifications
        //notificationsViewModel.text.observe(viewLifecycleOwner) {
          //  textView.text = it
        //}
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}