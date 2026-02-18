package com.facephi.demonfc.ui.composables.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.facephi.demonfc.R
import com.facephi.nfc_component.data.result.NfcSdkPersonalInformation
import kotlin.text.all
import kotlin.text.ifBlank
import kotlin.text.isDigit
import kotlin.text.substring

@Composable
fun PersonalInfoCard(
    info: NfcSdkPersonalInformation,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Header
            Text(
                text = stringResource(id = R.string.nfc_info_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Campos principales
            InfoRow(label = stringResource(id = R.string.nfc_info_name), value = info.name)
            InfoRow(label = stringResource(id = R.string.nfc_info_surname), value = info.surname)
            InfoRow(label = stringResource(id = R.string.nfc_info_number), value = info.personalNumber)
            InfoRow(label = stringResource(id = R.string.nfc_info_nationality), value = info.nationality)
            InfoRow(label = stringResource(id = R.string.nfc_info_gender), value = info.gender)
            InfoRow(label = stringResource(id = R.string.nfc_info_birth_date), value = formatBirthDate(info.birthdate))
            InfoRow(label = stringResource(id = R.string.nfc_info_birth_place), value = info.placeOfBirth)
            InfoRow(label = stringResource(id = R.string.nfc_info_address), value = info.address)
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value.ifBlank { "—" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

private fun formatBirthDate(raw: String): String {
    return if (raw.length == 6 && raw.all { it.isDigit() }) {
        val year = raw.substring(0, 2)
        val month = raw.substring(2, 4)
        val day = raw.substring(4, 6)
        "$year-$month-$day"
    } else {
        raw
    }
}