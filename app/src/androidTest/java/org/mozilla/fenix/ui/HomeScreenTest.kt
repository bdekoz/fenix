/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
<<<<<<< HEAD
import org.junit.Ignore
=======
import org.junit.Before
import org.junit.After
>>>>>>> fix: broken test
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.helpers.TestAssetHelper.waitingTime
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import okhttp3.mockwebserver.MockWebServer
import org.mozilla.fenix.ui.robots.PRIVATE_SESSION_MESSAGE
import org.mozilla.fenix.ui.robots.homeScreen

/**
 *  Tests for verifying the presence of home screen and first-run homescreen elements
 *
 *  Note: For private browsing, navigation bar and tabs see separate test class
 *
 */

class HomeScreenTest {
    /* ktlint-disable no-blank-line-before-rbrace */ // This imposes unreadable grouping.

    private val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private lateinit var mockWebServer: MockWebServer


    @get:Rule
    val activityTestRule = HomeActivityTestRule()


    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            setDispatcher(AndroidAssetDispatcher())
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun homeScreenItemsTest() {
        homeScreen { }.dismissOnboarding()

        homeScreen {
            verifyHomeScreen()
            verifyNavigationToolbar()
            verifyHomePrivateBrowsingButton()
            verifyHomeMenu()
            verifyHomeWordmark()
            verifyOpenTabsHeader()
            verifyAddTabButton()
            verifyNoTabsOpenedHeader()
            verifyNoTabsOpenedText()
            verifyCollectionsHeader()
            verifyNoCollectionsHeader()
            verifyNoCollectionsText()
            verifyHomeToolbar()
            verifyHomeComponent()
        }
    }

    @Test
    fun firstRunHomeScreenItemsTest() {
        homeScreen {
            verifyHomeScreen()
            verifyNavigationToolbar()
            verifyHomePrivateBrowsingButton()
            verifyHomeMenu()
            verifyHomeWordmark()

            verifyWelcomeHeader()

            verifyGetTheMostHeader()
            verifyAccountsSignInButton()
            verifyGetToKnowHeader()

            swipeUpToDismissFirstRun()

            verifyChooseThemeHeader()
            verifyChooseThemeText()
            verifyLightThemeToggle()
            verifyLightThemeDescription()
            verifyDarkThemeToggle()
            verifyDarkThemeDescription()
            verifyAutomaticThemeToggle()
            verifyAutomaticThemeDescription()

            verifyProtectYourselfHeader()
            verifyTrackingProtectionToggle()
            verifyProtectYourselfText()

            verifyBrowsePrivatelyHeader()
            verifyBrowsePrivatelyText()
            verifyYourPrivacyHeader()
            verifyYourPrivacyText()

            verifyPrivacyNoticeButton()
            verifyStartBrowsingButton()
        }
    }

    @Test
    @Ignore("Temp disable broken test - see:  https://github.com/mozilla-mobile/fenix/issues/5050")

    fun privateModeScreenItemsTest() {
        val defaultWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        homeScreen { }.dismissOnboarding()
        homeScreen { }.togglePrivateBrowsingMode()

        homeScreen {
            //open a demo site
            verifyHomeScreen()
            verifyNavigationToolbar()
            verifyHomePrivateBrowsingButton()
//            verifyHomeMenu()
            verifyHomeWordmark()
            verifyPrivateSessionHeader()
            verifyShareTabsButtonDoesNotExist()
            verifyCloseTabsButtonDoesNotExist()
            verifyAddTabButton()
            verifyPrivateSessionMessage(visible = true)
            verifyHomeToolbar()
            verifyHomeComponent()

        // browse to mock web page so tab elements appear
        }.enterURLAndEnterToBrowser(defaultWebPage.url) {
        }.goBackToHomeScreen {
            verifyHomeScreen()
            verifyShareTabsButton()
            verifyCloseTabButton()
//        }

        // replace mozilla.org w/ localhost
//        homeScreen {
//        }.addNewTab()
//        homeScreen {
            // To deal with the race condition where multiple "add tab" buttons are present,
            // we need to wait until previous HomeFragment View objects are gone.
            mDevice.wait(Until.gone(By.text(PRIVATE_SESSION_MESSAGE)), waitingTime)
            verifyHomeScreen()
            verifyNavigationToolbar()
            verifyHomePrivateBrowsingButton()
            verifyHomeMenu()
            verifyHomeWordmark()
            verifyAddTabButton()
            verifyShareTabsButton()
            verifyCloseTabButton()
            verifyPrivateSessionHeader()
            verifyPrivateSessionMessage(visible = false)
            verifyHomeToolbar()
            verifyHomeComponent()
        }
    }
}
