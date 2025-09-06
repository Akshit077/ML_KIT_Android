# 📱 ML Kit Android Demo

A comprehensive Android application showcasing Google's ML Kit capabilities with **Document Scanning** and **Facial Recognition** features built using Jetpack Compose and Kotlin.

## 🚀 Features

### 📄 Document Scanner
- **Professional Document Scanning** using Google ML Kit Document Scanner API
- **Automatic Edge Detection** and perspective correction
- **Multi-page Scanning** support (up to 5 pages per session)
- **PDF Generation** with high-quality output
- **Download to Device** - Save PDFs directly to Downloads folder
- **Real-time Preview** of scanned documents

### 👤 Facial Recognition
- **Real-time Face Detection** using ML Kit Face Detection API
- **Facial Landmark Detection** with visual overlay
- **Face Analysis** including:
  - Smile detection
  - Eye open/closed detection
  - Head pose angles (Y & Z rotation)
  - Confidence scoring
- **Multiple Face Support** - Detect and analyze multiple faces simultaneously
- **Live Camera Feed** with front-facing camera

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Compose Navigation
- **ML Kit APIs**:
  - Document Scanner API
  - Face Detection API
  - Text Recognition API
- **Camera**: CameraX
- **Image Loading**: Coil
- **Async Operations**: Kotlin Coroutines
- **Material Design**: Material 3

## 📋 Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24 (Android 7.0) or higher
- Device with camera support
- Google Play Services (for ML Kit APIs)

## 🔧 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/ml-kit-android-demo.git
   cd ml-kit-android-demo
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Sync Project**
   - Let Gradle sync the project dependencies
   - Ensure all ML Kit dependencies are downloaded

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button or use `Ctrl+R`

## 📱 Usage

### Document Scanning
1. Launch the app and tap **"Document Scanner"**
2. Tap **"Start Document Scan"** to open the scanner
3. Point camera at documents - automatic edge detection will activate
4. Capture multiple pages if needed
5. Review scanned documents in the app
6. Tap **"Download PDF"** to save to Downloads folder

### Facial Recognition
1. From the main screen, tap **"Facial Recognition"**
2. Allow camera permissions when prompted
3. Point the front camera at faces
4. View real-time analysis:
   - Face landmarks (green dots overlay)
   - Smile detection status
   - Eye open/closed status
   - Head rotation angles
   - Detection confidence

## 🏗️ Project Structure

```
app/src/main/java/com/example/machinelearning/
├── MainActivity.kt                 # Main entry point with navigation
├── document_scanning/
│   └── DocumentScanningScreen.kt   # Document scanner implementation
├── facial_recognition/
│   └── FacialRecognitionScreen.kt  # Face detection implementation
└── ui/theme/                       # Material Design theme
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## 🔑 Key Dependencies

```kotlin
// ML Kit
implementation 'com.google.mlkit:face-detection:16.1.7'
implementation 'com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1'
implementation 'com.google.android.gms:play-services-mlkit-text-recognition:19.0.1'
implementation 'com.google.mlkit:common:18.10.0'

// Camera
implementation 'androidx.camera:camera-core:1.3.4'
implementation 'androidx.camera:camera-camera2:1.3.4'
implementation 'androidx.camera:camera-lifecycle:1.3.4'
implementation 'androidx.camera:camera-view:1.3.4'

// Compose & Navigation
implementation 'androidx.navigation:navigation-compose:2.8.4'
implementation 'io.coil-kt:coil-compose:2.7.0'
```

## 🔐 Permissions

The app requires the following permissions:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

## 📸 Screenshots

### Main Screen
- Clean Material 3 design with feature cards
- Easy navigation to both ML Kit features

### Document Scanner
- Professional scanning interface
- Multi-page support with preview
- One-tap PDF download

### Facial Recognition
- Real-time face detection
- Detailed facial analysis
- Visual landmark overlay

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google ML Kit** for providing powerful machine learning APIs
- **Android Jetpack** for modern Android development tools
- **Material Design** for beautiful UI components

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/yourusername/ml-kit-android-demo/issues) page
2. Create a new issue with detailed description
3. Include device information and Android version

## 🔄 Version History

- **v1.0.0** - Initial release
  - Document scanning with PDF download
  - Facial recognition with landmark detection
  - Material 3 UI design
  - Multi-page document support

## 🎯 Future Enhancements

- [ ] Text extraction from scanned documents
- [ ] Document classification (ID, passport, etc.)
- [ ] Face recognition (identification)
- [ ] Barcode/QR code scanning
- [ ] Cloud storage integration
- [ ] OCR with multiple languages

---

**Built with ❤️ using Google ML Kit and Jetpack Compose**

⭐ **Star this repository if you found it helpful!**
