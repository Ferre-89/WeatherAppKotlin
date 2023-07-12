package Activities

import Utilities.Utilities
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.ui.ui.App
import java.text.DateFormat

interface ToolbarManager {
    val toolbar: androidx.appcompat.widget.Toolbar

    var toolbarTitle: String
        get() = toolbar.title.toString()
        set(value) {
            toolbar.title = value
        }

    fun initToolBar() {
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> Utilities.WEATHER.showToast(
                    toolbar.context,
                    "Settings",
                    Toast.LENGTH_LONG
                )

                else -> Utilities.WEATHER.showToast(
                    toolbar.context,
                    "Unknown option",
                    Toast.LENGTH_LONG
                )
            }
            true
        }
    }

    fun enableHomeAsUp(up: () -> Unit) {
        toolbar.navigationIcon = createUpDrawable()
        toolbar.setNavigationOnClickListener { up() }
    }

    private fun createUpDrawable() = DrawerArrowDrawable(toolbar.context).apply { progress = 1f }

    fun attachToScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    toolbar.slideExit()
                } else {
                    toolbar.slideEnter()
                }
            }
        })
    }

    fun View.slideExit() {
        if (translationY == 0f) animate().translationY(-height.toFloat())
    }

    fun View.slideEnter() {
        if (translationY < 0f) animate().translationY(0f)
    }
}
