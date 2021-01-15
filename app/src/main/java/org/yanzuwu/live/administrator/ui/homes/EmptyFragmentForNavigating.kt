package org.yanzuwu.live.administrator.ui.homes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.yanzuwu.live.administrator.R

class EmptyFragmentForNavigating:Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().navigate(R.id.toSubNavTask)
    }
}