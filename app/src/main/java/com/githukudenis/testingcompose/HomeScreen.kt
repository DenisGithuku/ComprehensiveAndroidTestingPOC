package com.githukudenis.testingcompose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTonalElevationEnabled
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, viewModel: HomeViewModel = viewModel<HomeViewModel>()
) {
    val state by viewModel.state.collectAsState()

    HomeRouteContent(
        state = state,
        onToggleFavourite = viewModel::toggleFavouritePersons,
        onFilterValue = viewModel::onToggleFilterValue
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeRouteContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onToggleFavourite: (Int) -> Unit,
    onFilterValue: (PersonsFilterType) -> Unit
) {
    var bottomSheetIsAvailable by remember {
        mutableStateOf(true)
    }

    val groupedPersons = state.persons.sortedBy { it.firstname }.groupBy { it.firstname[0] }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.bodyMedium
                )
            }, actions = {
                IconButton(onClick = {
                    bottomSheetIsAvailable = !bottomSheetIsAvailable
                }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = stringResource(id = R.string.more_options),
                        tint = Color.Black.copy(alpha = 0.7f),
                    )
                }
            })
        },
    ) { innerPadding ->
        if (bottomSheetIsAvailable) {
            ModalBottomSheet(onDismissRequest = {
                bottomSheetIsAvailable = !bottomSheetIsAvailable
            }) {
                listOf(
                    PersonsFilterType.ALL,
                    PersonsFilterType.FAVOURITE,
                ).map {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                onFilterValue(it)
                            })
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.selectedPersonFilterType == it,
                            onClick = { onFilterValue(it) },
                            modifier = Modifier.testTag(stringResource(R.string.filter_radio_label))
                        )
                        Text(
                            text = it.name.lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .testTag(tag = stringResource(id = R.string.person_list_test_tag))
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            groupedPersons.forEach { (initial, people) ->
                stickyHeader {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            modifier = Modifier.testTag(stringResource(id = R.string.initial_test_tag)),
                            text = "$initial",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                if (people.isEmpty()) {
                    item {
                        Text(
                            text = "There are no people in this category",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.8f
                            )
                        )
                    }
                } else {
                    items(people, key = { it.id }) {

                        Row(
                            modifier = Modifier
                                .testTag(tag = stringResource(R.string.person_row_test_tag))
                                .fillMaxWidth()
                                .clickable(onClick = { onToggleFavourite(it.id) })
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = it.firstname, style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = it.lastname, style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(modifier = Modifier.testTag(stringResource(id = R.string.favourite_toggle_label)),
                                onClick = {
                                    onToggleFavourite(it.id)
                                }) {
                                Icon(
                                    imageVector = if (it.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    tint = if (it.isFavourite) Color(0xFFFF9800) else Color.LightGray,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}