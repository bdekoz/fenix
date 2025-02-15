/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.components.metrics

import android.content.Context
import mozilla.components.browser.errorpages.ErrorType
import mozilla.components.browser.search.SearchEngine
import mozilla.components.support.base.Component
import mozilla.components.support.base.facts.Fact
import mozilla.components.support.base.facts.FactProcessor
import mozilla.components.support.base.facts.Facts
import mozilla.components.support.base.log.logger.Logger
import org.mozilla.fenix.BuildConfig
import org.mozilla.fenix.GleanMetrics.Collections
import org.mozilla.fenix.GleanMetrics.ContextMenu
import org.mozilla.fenix.GleanMetrics.CrashReporter
import org.mozilla.fenix.GleanMetrics.ErrorPage
import org.mozilla.fenix.GleanMetrics.Events
import org.mozilla.fenix.GleanMetrics.Library
import org.mozilla.fenix.GleanMetrics.SearchShortcuts
import org.mozilla.fenix.R
import java.util.Locale

sealed class Event {

    // Interaction Events
    object OpenedAppFirstRun : Event()
    object InteractWithSearchURLArea : Event()
    object DismissedOnboarding : Event()
    object ClearedPrivateData : Event()
    object SearchShortcutMenuOpened : Event()
    object SearchShortcutMenuClosed : Event()
    object AddBookmark : Event()
    object RemoveBookmark : Event()
    object OpenedBookmark : Event()
    object OpenedBookmarkInNewTab : Event()
    object OpenedBookmarksInNewTabs : Event()
    object OpenedBookmarkInPrivateTab : Event()
    object OpenedBookmarksInPrivateTabs : Event()
    object EditedBookmark : Event()
    object MovedBookmark : Event()
    object ShareBookmark : Event()
    object CopyBookmark : Event()
    object AddBookmarkFolder : Event()
    object RemoveBookmarkFolder : Event()
    object RemoveBookmarks : Event()
    object QuickActionSheetOpened : Event()
    object QuickActionSheetClosed : Event()
    object QuickActionSheetShareTapped : Event()
    object QuickActionSheetBookmarkTapped : Event()
    object QuickActionSheetDownloadTapped : Event()
    object QuickActionSheetOpenInAppTapped : Event()
    object CustomTabsClosed : Event()
    object CustomTabsActionTapped : Event()
    object CustomTabsMenuOpened : Event()
    object UriOpened : Event()
    object QRScannerOpened : Event()
    object QRScannerPromptDisplayed : Event()
    object QRScannerNavigationAllowed : Event()
    object QRScannerNavigationDenied : Event()
    object LibraryOpened : Event()
    object LibraryClosed : Event()
    object SyncAuthOpened : Event()
    object SyncAuthClosed : Event()
    object SyncAuthSignIn : Event()
    object SyncAuthSignOut : Event()
    object SyncAuthScanPairing : Event()
    object SyncAuthCreateAccount : Event()
    object SyncAccountOpened : Event()
    object SyncAccountClosed : Event()
    object SyncAccountSyncNow : Event()
    object HistoryOpened : Event()
    object HistoryItemShared : Event()
    object HistoryItemOpened : Event()
    object HistoryItemRemoved : Event()
    object HistoryAllItemsRemoved : Event()
    object ReaderModeAvailable : Event()
    object ReaderModeOpened : Event()
    object ReaderModeAppearanceOpened : Event()
    object CollectionRenamed : Event()
    object CollectionTabRestored : Event()
    object CollectionAllTabsRestored : Event()
    object CollectionTabRemoved : Event()
    object CollectionShared : Event()
    object CollectionRemoved : Event()
    object CollectionTabSelectOpened : Event()
    object CollectionTabLongPressed : Event()
    object CollectionAddTabPressed : Event()
    object CollectionRenamePressed : Event()
    object SearchWidgetNewTabPressed : Event()
    object SearchWidgetVoiceSearchPressed : Event()
    object FindInPageOpened : Event()
    object FindInPageClosed : Event()
    object FindInPageNext : Event()
    object FindInPagePrevious : Event()
    object FindInPageSearchCommitted : Event()
    object PrivateBrowsingGarbageIconTapped : Event()
    object PrivateBrowsingSnackbarUndoTapped : Event()
    object PrivateBrowsingNotificationTapped : Event()
    object PrivateBrowsingNotificationOpenTapped : Event()
    object PrivateBrowsingNotificationDeleteAndOpenTapped : Event()

    // Interaction events with extras

    data class PreferenceToggled(val preferenceKey: String, val enabled: Boolean, val context: Context) : Event() {
        private val switchPreferenceTelemetryAllowList = listOf(
            context.getString(R.string.pref_key_show_search_suggestions),
            context.getString(R.string.pref_key_remote_debugging),
            context.getString(R.string.pref_key_telemetry),
            context.getString(R.string.pref_key_tracking_protection)
        )

        override val extras: Map<Events.preferenceToggledKeys, String>?
            get() = mapOf(
                Events.preferenceToggledKeys.preferenceKey to preferenceKey,
                Events.preferenceToggledKeys.enabled to enabled.toString()
            )

        init {
            // If the event is not in the allow list, we don't want to track it
            if (!switchPreferenceTelemetryAllowList.contains(preferenceKey)) { throw IllegalArgumentException() }
        }
    }

    data class OpenedApp(val source: Source) : Event() {
        enum class Source { APP_ICON, LINK, CUSTOM_TAB }
        override val extras: Map<Events.appOpenedKeys, String>?
            get() = hashMapOf(Events.appOpenedKeys.source to source.name)
    }

    data class CollectionSaveButtonPressed(val fromScreen: String) : Event() {
        override val extras: Map<Collections.saveButtonKeys, String>?
            get() = mapOf(Collections.saveButtonKeys.fromScreen to fromScreen)
    }

    data class CollectionSaved(val tabsOpenCount: Int, val tabsSelectedCount: Int) : Event() {
        override val extras: Map<Collections.savedKeys, String>?
            get() = mapOf(
                Collections.savedKeys.tabsOpen to tabsOpenCount.toString(),
                Collections.savedKeys.tabsSelected to tabsSelectedCount.toString()
            )
    }

    data class CollectionTabsAdded(val tabsOpenCount: Int, val tabsSelectedCount: Int) : Event() {
        override val extras: Map<Collections.tabsAddedKeys, String>?
            get() = mapOf(
                Collections.tabsAddedKeys.tabsOpen to tabsOpenCount.toString(),
                Collections.tabsAddedKeys.tabsSelected to tabsSelectedCount.toString()
            )
    }

    data class LibrarySelectedItem(val item: String) : Event() {
        override val extras: Map<Library.selectedItemKeys, String>?
            get() = mapOf(Library.selectedItemKeys.item to item)
    }

    data class ErrorPageVisited(val errorType: ErrorType) : Event() {
        override val extras: Map<ErrorPage.visitedErrorKeys, String>?
            get() = mapOf(ErrorPage.visitedErrorKeys.errorType to errorType.name)
    }

    data class SearchBarTapped(val source: Source) : Event() {
        enum class Source { HOME, BROWSER }
        override val extras: Map<Events.searchBarTappedKeys, String>?
            get() = mapOf(Events.searchBarTappedKeys.source to source.name)
    }

    data class EnteredUrl(val autoCompleted: Boolean) : Event() {
        override val extras: Map<Events.enteredUrlKeys, String>?
            get() = mapOf(Events.enteredUrlKeys.autocomplete to autoCompleted.toString())
    }

    data class PerformedSearch(val eventSource: EventSource) : Event() {
        sealed class EngineSource {
            data class Default(val engine: SearchEngine) : EngineSource()
            data class Shortcut(val engine: SearchEngine) : EngineSource()

            val searchEngine: SearchEngine
                get() = when (this) {
                    is Default -> engine
                    is Shortcut -> engine
                }

            val descriptor: String
                get() = when (this) {
                    is Default -> "default"
                    is Shortcut -> "shortcut"
                }
        }

        sealed class EventSource {
            data class Suggestion(val engineSource: EngineSource) : EventSource()
            data class Action(val engineSource: EngineSource) : EventSource()

            private val source: EngineSource
                get() = when (this) {
                    is Suggestion -> engineSource
                    is Action -> engineSource
                }

            private val label: String
                get() = when (this) {
                    is Suggestion -> "suggestion"
                    is Action -> "action"
                }

            val countLabel: String
                get() = "${source.searchEngine.identifier.toLowerCase(Locale.ROOT)}.$label"

            val sourceLabel: String
                get() = "${source.descriptor}.$label"
        }

        override val extras: Map<Events.performedSearchKeys, String>?
            get() = mapOf(Events.performedSearchKeys.source to eventSource.sourceLabel)
    }

    // Track only built-in engine selection. Do not track user-added engines!
    data class SearchShortcutSelected(val engine: String) : Event() {
        override val extras: Map<SearchShortcuts.selectedKeys, String>?
            get() = mapOf(SearchShortcuts.selectedKeys.engine to engine)
    }

    class ContextMenuItemTapped private constructor(val item: String) : Event() {
        override val extras: Map<ContextMenu.itemTappedKeys, String>?
            get() = mapOf(ContextMenu.itemTappedKeys.named to item)

        companion object {
            fun create(context_item: String) = allowList[context_item]?.let { ContextMenuItemTapped(it) }

            private val allowList = mapOf(
                "mozac.feature.contextmenu.open_in_new_tab" to "open_in_new_tab",
                "mozac.feature.contextmenu.open_in_private_tab" to "open_in_private_tab",
                "mozac.feature.contextmenu.open_image_in_new_tab" to "open_image_in_new_tab",
                "mozac.feature.contextmenu.save_image" to "save_image",
                "mozac.feature.contextmenu.share_link" to "share_link",
                "mozac.feature.contextmenu.copy_link" to "copy_link",
                "mozac.feature.contextmenu.copy_image_location" to "copy_image_location"
            )
        }
    }

    object CrashReporterOpened : Event()
    data class CrashReporterClosed(val crashSubmitted: Boolean) : Event() {
        override val extras: Map<CrashReporter.closedKeys, String>?
            get() = mapOf(CrashReporter.closedKeys.crashSubmitted to crashSubmitted.toString())
    }

    data class BrowserMenuItemTapped(val item: Item) : Event() {
        enum class Item {
            SETTINGS, LIBRARY, HELP, DESKTOP_VIEW_ON, DESKTOP_VIEW_OFF, FIND_IN_PAGE, NEW_TAB,
            NEW_PRIVATE_TAB, SHARE, REPORT_SITE_ISSUE, BACK, FORWARD, RELOAD, STOP, OPEN_IN_FENIX,
            SAVE_TO_COLLECTION, ADD_TO_HOMESCREEN
        }

        override val extras: Map<Events.browserMenuActionKeys, String>?
            get() = mapOf(Events.browserMenuActionKeys.item to item.toString().toLowerCase())
    }

    sealed class Search

    internal open val extras: Map<*, String>?
        get() = null
}

private fun Fact.toEvent(): Event? = when (Pair(component, item)) {
    Component.FEATURE_FINDINPAGE to "previous" -> Event.FindInPagePrevious
    Component.FEATURE_FINDINPAGE to "next" -> Event.FindInPageNext
    Component.FEATURE_FINDINPAGE to "close" -> Event.FindInPageClosed
    Component.FEATURE_FINDINPAGE to "input" -> Event.FindInPageSearchCommitted
    Component.FEATURE_CONTEXTMENU to "item" -> {
        metadata?.get("item")?.let { Event.ContextMenuItemTapped.create(it.toString()) }
    }

    Component.BROWSER_TOOLBAR to "menu" -> {
        metadata?.get("customTab")?.let { Event.CustomTabsMenuOpened }
    }
    Component.FEATURE_CUSTOMTABS to "close" -> Event.CustomTabsClosed
    Component.FEATURE_CUSTOMTABS to "action_button" -> Event.CustomTabsActionTapped

    else -> null
}

interface MetricsService {
    fun start()
    fun stop()
    fun track(event: Event)
    fun shouldTrack(event: Event): Boolean
}

interface MetricController {
    fun start()
    fun stop()
    fun track(event: Event)

    companion object {
        fun create(services: List<MetricsService>, isTelemetryEnabled: () -> Boolean): MetricController {
            return if (BuildConfig.TELEMETRY) return ReleaseMetricController(services, isTelemetryEnabled)
            else DebugMetricController()
        }
    }
}

private class DebugMetricController : MetricController {
    override fun start() {
        Logger.debug("DebugMetricController: start")
    }

    override fun stop() {
        Logger.debug("DebugMetricController: stop")
    }

    override fun track(event: Event) {
        Logger.debug("DebugMetricController: track event: $event")
    }
}

private class ReleaseMetricController(
    private val services: List<MetricsService>,
    private val isTelemetryEnabled: () -> Boolean
) : MetricController {
    private var initialized = false

    init {
        Facts.registerProcessor(object : FactProcessor {
            override fun process(fact: Fact) {
                fact.toEvent()?.also {
                    track(it)
                }
            }
        })
    }

    override fun start() {
        if (!isTelemetryEnabled.invoke() || initialized) { return }

        services.forEach { it.start() }
        initialized = true
    }

    override fun stop() {
        if (!initialized) { return }

        services.forEach { it.stop() }
        initialized = false
    }

    override fun track(event: Event) {
        if (!isTelemetryEnabled.invoke() && !initialized) { return }

        services
            .filter { it.shouldTrack(event) }
            .forEach { it.track(event) }
    }
}
