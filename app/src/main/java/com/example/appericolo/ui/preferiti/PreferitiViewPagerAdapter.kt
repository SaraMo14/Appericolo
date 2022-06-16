package com.example.appericolo.ui.preferiti

import android.content.Context
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appericolo.ui.preferiti.contacts.ContattiFragment
import com.example.appericolo.ui.preferiti.luoghi.ListaLuoghiFragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class PreferitiViewPagerAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ContattiFragment()
            1 -> ListaLuoghiFragment()
            else -> ContattiFragment()
        }
    }

    @Nullable
    override fun getPageTitle(position: Int): String {
        //return super.getPageTitle(position)
        return when (position) {
            0 -> "Contatti"
            1 -> "Luoghi"
            else -> "Contatti"
        }
    }

}