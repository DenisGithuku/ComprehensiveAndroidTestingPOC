package com.githukudenis.testingcompose

class HomeViewModel : StateFulViewModel<HomeUiState>(HomeUiState()) {

    private val favourites = mutableListOf(2, 9, 5)

    private var personList = listOf(
        PersonUiState(id = 1, firstname = "John", lastname = "Doe"),
        PersonUiState(id = 2, firstname = "Jane", lastname = "Wright"),
        PersonUiState(id = 3, firstname = "Peter", lastname = "Owanga"),
        PersonUiState(id = 4, firstname = "Andrew", lastname = "Ombachi"),
        PersonUiState(id = 5, firstname = "Sam", lastname = "Kimani"),
        PersonUiState(id = 6, firstname = "Allan", lastname = "Kamau"),
        PersonUiState(id = 7, firstname = "Sarah", lastname = "Mbinya"),
        PersonUiState(id = 8, firstname = "Alice", lastname = "Juma"),
        PersonUiState(id = 9, firstname = "Maya", lastname = "Mwanyigha"),
        PersonUiState(id = 10, firstname = "Aria", lastname = "Ogolla"),
    )


    init {
        update {
            copy(persons = personList.map { it.copy(isFavourite = it.id in favourites) })
        }
    }


    fun toggleFavouritePersons(id: Int) {
        if (id in favourites) {
            favourites.remove(id)
        } else {
            favourites.add(id)
        }

        val people = when(state.value.selectedPersonFilterType) {
            PersonsFilterType.ALL -> personList.map { it.copy(isFavourite = it.id in favourites) }
            PersonsFilterType.FAVOURITE -> personList.filter { it.id in favourites }.map { it.copy(isFavourite = it.id in favourites) }
        }
        update { copy(persons =  people)}
    }

    fun onToggleFilterValue(personsFilterType: PersonsFilterType) {
        val filteredPersons = when (personsFilterType) {
            PersonsFilterType.ALL -> personList.map { it.copy(isFavourite = it.id in favourites) }
            PersonsFilterType.FAVOURITE -> personList.filter { it.id in favourites }.map { it.copy(isFavourite = it.id in favourites) }
        }
        update { copy(persons = filteredPersons, selectedPersonFilterType = personsFilterType) }
    }

}

enum class PersonsFilterType {
    ALL, FAVOURITE
}