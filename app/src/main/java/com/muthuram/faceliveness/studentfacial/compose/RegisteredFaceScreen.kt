package com.muthuram.faceliveness.studentfacial.compose

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muthuram.faceliveness.R

@Composable
fun RegisteredFaceScreen(
    studentFaceList : ArrayList<Bitmap>,
    onCloseClicked : () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.white),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                        contentDescription = "Back Arrow",
                        tint = Color.Black,
                        modifier = Modifier
                            .clickable { onCloseClicked() }
                            .padding(horizontal = 10.dp),
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding()
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp)
                ) {
                    items(studentFaceList) { image ->
                        StudentFaceView(image)
                    }
                }
            }
        }
    }
}

@Composable
fun StudentFaceView(
    studentImage : Bitmap
) {
    Image(
        bitmap = studentImage.asImageBitmap(),
        contentDescription = "image",
        modifier = Modifier
            .width(200.dp)
            .height(150.dp)
            .background(color = Color.White, RoundedCornerShape(8.dp))
            .padding(5.dp)
    )
}