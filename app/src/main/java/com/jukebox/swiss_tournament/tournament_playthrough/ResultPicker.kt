package com.jukebox.swiss_tournament.tournament_playthrough

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jukebox.swiss_tournament.data.model.StoreResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultPicker(
    options: List<String>,
    onResultChange: (newResult: String) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: MutableState<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    remember { selectedOption }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = modifier
    ) {
        TextField(
            value = mapStoreResultToDisplayResult(selectedOption.value),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                val option = mapStoreResultToDisplayResult(it)
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        selectedOption.value = option
                        expanded = false
                        onResultChange(it)
                    }
                )
            }
        }
    }


}
fun mapStoreResultToDisplayResult(storeResult: String): String {
    return when (storeResult) {
        StoreResult.whiteWon    -> "1-0"
        StoreResult.blackWon    -> "0-1"
        StoreResult.remis       -> "½-½"
        StoreResult.ongoing     -> "tbd."
        StoreResult.byeForWhite -> "1-0"
        StoreResult.byeForBlack -> "0-1"
        else -> storeResult
    }
}