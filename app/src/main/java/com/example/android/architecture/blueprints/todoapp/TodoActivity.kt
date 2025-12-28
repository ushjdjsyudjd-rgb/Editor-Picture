package com.example.android.architecture.blueprints.todoapp

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class TodoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoEditorScreen()
        }
    }
}

@Composable
fun PhotoEditorScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
        uri?.let {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap = ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
            Text("انتخاب عکس")
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxWidth().height(400.dp))
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Button(onClick = {
                val matrix = ColorMatrix().apply { setSaturation(0f) }
                val filter = ColorMatrixColorFilter(matrix)
                val newBitmap = Bitmap.createBitmap(it.width, it.height, it.config)
                val canvas = Canvas(newBitmap)
                val paint = Paint().apply { colorFilter = filter }
                canvas.drawBitmap(it, 0f, 0f, paint)
                bitmap = newBitmap
            }, modifier = Modifier.fillMaxWidth()) {
                Text("سیاه و سفید کن")
            }
        }
    }
}
