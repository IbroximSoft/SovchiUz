package uz.ibrohim.sovchiuz.screens

import android.view.View
import androidx.viewpager.widget.ViewPager
import uz.ibrohim.sovchiuz.R
import kotlin.math.abs

class ScreenPageTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.tag
        val pageWidth = page.width
        val pageWidthTimesPosition = pageWidth * position
        val absPosition = abs(position)
        if (position > -1.0f && position < 1.0f) {
            if (position != 0.0f) {
                val title = page.findViewById<View>(R.id.screen_text1)
                title.alpha = 1.0f - absPosition
                val description = page.findViewById<View>(R.id.screen_text2)
                description.translationY = -pageWidthTimesPosition / 2f
                description.alpha = 1.0f - absPosition
                val computer = page.findViewById<View>(R.id.screen_image)
                if (computer != null) {
                    computer.alpha = 1.0f - absPosition
                    computer.translationX = -pageWidthTimesPosition * 1.5f
                }
            }
        }
    }
}