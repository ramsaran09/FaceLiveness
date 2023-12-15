package com.muthuram.faceliveness.activityUi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muthuram.faceliveness.R

@Composable
fun Tab(
    items: List<String>,
    onItemSelected: (Int) -> Unit,
    selectedItem: Int,
) {
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(start = 30.dp, top = 20.dp, bottom = 15.dp)
    ) {
        itemsIndexed(items) { index, item ->
            TabItem(
                value = item,
                onClick = { onItemSelected(index) },
                isSelected = selectedItem == index
            )
        }
    }
}

@Composable
private fun TabItem(
    value: String,
    onClick: () -> Unit,
    isSelected: Boolean,
) {
    val backgroundColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.35f)
    val textColor = if (isSelected) colorResource(id = R.color.digi_blue) else Color.Black
    Box(
        modifier = Modifier
            .height(30.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(color = backgroundColor)
            .clickable(enabled = !isSelected, onClick = onClick)
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = value,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = textColor,
        )
    }
}
