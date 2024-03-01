package com.githukudenis.testingcompose

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import com.githukudenis.testingcompose.ui.theme.TestingComposeTheme
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        val personList = (1..10).map {
            PersonUiState(
                id = it, firstname = "First $it", lastname = "Last $it", isFavourite = it % 3 == 0
            )
        }

        composeTestRule.setContent {
            TestingComposeTheme {
                HomeRouteContent(
                    state = HomeUiState(persons = personList),
                    onToggleFavourite = {},
                    onFilterValue = {})
            }
        }
    }

    @Test
    fun testFavouriteIconHasClickAction() {
        composeTestRule.onAllNodesWithTag(testTag = composeTestRule.activity.getString(R.string.favourite_toggle_label))
            .assertAll(
                hasClickAction()
            )

    }

    @Test
    fun testUserListLazyColumnIsPresentAndScrolls() {
        composeTestRule.onNodeWithTag(
            testTag = composeTestRule.activity.getString(R.string.person_list_test_tag)
        ).assert(
                hasScrollAction()
        )
    }

    @Test
    fun testFavouriteIconPresenceInRow() {
        composeTestRule.onAllNodesWithTag(
            composeTestRule.activity.getString(R.string.person_row_test_tag)
        ).assertAll(
            hasAnyChild(
                hasTestTag(testTag = composeTestRule.activity.getString(R.string.favourite_toggle_label))
            )
        )
    }

    @Test
    fun testInitialIsDisplayed() {
        composeTestRule.onAllNodesWithTag(
            composeTestRule.activity.getString(R.string.initial_test_tag)
        ).onFirst().assertTextContains(value = "F")
    }

    @Test
    fun testRadioButtonOnFilter() {
        composeTestRule.onAllNodesWithTag(
            testTag = composeTestRule.activity.getString(R.string.filter_radio_label)
        ).onFirst().performClick()

        composeTestRule.onAllNodesWithTag(
            testTag = composeTestRule.activity.getString(R.string.filter_radio_label)
        ).onFirst().assertIsSelected()
    }
}