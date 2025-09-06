package com.example.machinelearning.facial_recognition

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacialRecognitionScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var detectedFaces by remember { mutableStateOf(listOf<FaceInfo>()) }
    var faceLandmarks by remember { mutableStateOf(listOf<FaceLandmark>()) }
    var isProcessing by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Facial Recognition") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (hasCameraPermission) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Camera Preview with Face Mesh Overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    CameraPreview(
                        onFacesDetected = { faces, landmarks ->
                            detectedFaces = faces
                            faceLandmarks = landmarks
                        },
                        onProcessingChange = { processing ->
                            isProcessing = processing
                        }
                    )
                    
                    // Face Landmarks Overlay
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawFaceLandmarks(faceLandmarks)
                    }
                    
                    if (isProcessing) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Results Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Face Detection Results:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        if (detectedFaces.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.height(200.dp)
                            ) {
                                items(detectedFaces.withIndex().toList()) { (index, face) ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp)
                                        ) {
                                            Text(
                                                text = "Face ${index + 1}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Confidence: ${String.format("%.2f", face.confidence)}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Smiling: ${if (face.isSmiling) "Yes" else "No"}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Eyes Open: ${if (face.eyesOpen) "Yes" else "No"}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Head Angles - Y: ${String.format("%.1f", face.headEulerAngleY)}°, Z: ${String.format("%.1f", face.headEulerAngleZ)}°",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Landmarks: ${face.landmarksCount}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Point camera at a face to detect...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Camera permission is required for facial recognition",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { launcher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraPreview(
    onFacesDetected: (List<FaceInfo>, List<FaceLandmark>) -> Unit,
    onProcessingChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, FaceAnalyzer { faces, landmarks ->
                            onFacesDetected(faces, landmarks)
                            onProcessingChange(false)
                        })
                    }

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    // Handle exception
                }
            }, ContextCompat.getMainExecutor(ctx))
            
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}

private fun DrawScope.drawFaceLandmarks(landmarks: List<FaceLandmark>) {
    landmarks.forEach { landmark ->
        drawCircle(
            color = Color.Green,
            radius = 4f,
            center = Offset(landmark.position.x, landmark.position.y)
        )
    }
}

private class FaceAnalyzer(
    private val onFacesDetected: (List<FaceInfo>, List<FaceLandmark>) -> Unit
) : ImageAnalysis.Analyzer {
    
    private val faceDetector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()
    )
    
    private var lastAnalyzedTimestamp = 0L

    override fun analyze(imageProxy: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimestamp >= 500) { // Analyze every 500ms
            lastAnalyzedTimestamp = currentTimestamp
            
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                
                // Detect faces
                faceDetector.process(image)
                    .addOnSuccessListener { faces ->
                        val faceInfos = faces.map { face ->
                            FaceInfo(
                                confidence = if (face.trackingId != null) 1.0f else 0.8f,
                                isSmiling = face.smilingProbability?.let { it > 0.5f } ?: false,
                                eyesOpen = face.leftEyeOpenProbability?.let { leftOpen ->
                                    face.rightEyeOpenProbability?.let { rightOpen ->
                                        leftOpen > 0.5f && rightOpen > 0.5f
                                    }
                                } ?: true,
                                headEulerAngleY = face.headEulerAngleY,
                                headEulerAngleZ = face.headEulerAngleZ,
                                landmarksCount = face.allLandmarks.size
                            )
                        }
                        
                        // Collect all landmarks from all faces
                        val allLandmarks = mutableListOf<FaceLandmark>()
                        faces.forEach { face ->
                            allLandmarks.addAll(face.allLandmarks)
                        }
                        
                        onFacesDetected(faceInfos, allLandmarks)
                    }
                    .addOnFailureListener {
                        onFacesDetected(emptyList(), emptyList())
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }
}

data class FaceInfo(
    val confidence: Float,
    val isSmiling: Boolean,
    val eyesOpen: Boolean,
    val headEulerAngleY: Float,
    val headEulerAngleZ: Float,
    val landmarksCount: Int
)
