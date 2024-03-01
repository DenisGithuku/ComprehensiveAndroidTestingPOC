package com.githukudenis.testingcompose

data class HomeUiState(
    val persons: List<PersonUiState> = emptyList(),
    val selectedPersonFilterType: PersonsFilterType = PersonsFilterType.ALL
)

data class PersonUiState(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val isFavourite: Boolean = false
)