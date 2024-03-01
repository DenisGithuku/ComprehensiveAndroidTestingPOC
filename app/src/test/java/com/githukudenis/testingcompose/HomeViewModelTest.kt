package com.githukudenis.testingcompose

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    lateinit var persons: List<PersonUiState>
    lateinit var homeUiState: HomeUiState
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        persons = (1..4).map {
            PersonUiState(
                id = it,
                firstname = "First $it",
                lastname = "Last $it",
                isFavourite = false
            )
        }
        homeUiState = HomeUiState(persons)
        homeViewModel = HomeViewModel()
        homeViewModel.update {
            homeUiState
        }
    }

    @After
    fun tearDown() {
        persons = emptyList()
        homeUiState = HomeUiState()
    }

    @Test
    fun testToggleFavouritePersons() {
        homeViewModel.toggleFavouritePersons(id = 3)
        assertTrue(homeViewModel.state.value.persons.first { it.id == 3 }.isFavourite)
    }

    @Test
    fun testToggleFilterValueChangesSelectedFilterType() {
        homeViewModel.onToggleFilterValue(PersonsFilterType.FAVOURITE)
        assertTrue(
            homeViewModel.state.value.selectedPersonFilterType == PersonsFilterType.FAVOURITE
        )
    }

    @Test
    fun testToggleFilterValueChangesListInPlace() {
        homeViewModel.onToggleFilterValue(PersonsFilterType.FAVOURITE)
        assertTrue(
            homeViewModel.state.value.persons.all { it.isFavourite }
        )
    }
}