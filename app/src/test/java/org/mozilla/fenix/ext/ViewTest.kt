package org.mozilla.fenix.ext

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import mozilla.components.support.ktx.android.util.dpToPx
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import io.mockk.every
import kotlinx.coroutines.ObsoleteCoroutinesApi
import androidx.core.content.ContextCompat
import mozilla.components.support.test.robolectric.testContext
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.mozilla.fenix.TestApplication
import io.mockk.Runs
import io.mockk.just
// For layouts
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.os.Bundle

@ObsoleteCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)

class ViewTest {

    @Test
    fun testIncreaseTapArea() {
        val mockView = spyk(View(testContext))
        //mockkStatic(View::class)
        //mockkStatic(ViewParent::class)
       // val mockParent: ViewParent = mockk(relaxed=true)
        //val mockParent = spyk(ViewParent())
        //val mockGroup: ViewGroup = mockk(relaxed=true)
        //mockGroup.addView(mockView)

        //Let's try  with layouts
        //val inflateView: LinearLayout = mockk(relaxed=true)
        //inflateView.addView(mockView)
       // every {(mockParent.getParent())} returns mockGroup
       // every { (mockView.getParent()) } answers {(mockParent.getParent())}
       // every {mockView.parent} returns mockGroup
        every {mockView.post({println("hello world")})} returns true

        mockView.increaseTapArea(4)
        verify {mockView.increaseTapArea(4)}
        //verify { mockView.parent.setTouchDelegate() }

    }
}

