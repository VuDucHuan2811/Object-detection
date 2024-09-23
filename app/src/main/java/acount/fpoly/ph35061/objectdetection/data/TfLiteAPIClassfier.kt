package acount.fpoly.ph35061.objectdetection.data

import acount.fpoly.ph35061.objectdetection.dioman.APIClassifier
import acount.fpoly.ph35061.objectdetection.dioman.Classification
import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TfLiteAPIClassfier(
    private val context: Context,
    private val threshold: Float = 0.5f,
    private val maxResults: Int = 1,
): APIClassifier {
    private var classifier: ImageClassifier? = null


    // Phương thức khởi tạo bộ phân loại
    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)
            .build()

        // Tạo bộ phân loại từ tệp mô hình "API.tflite"
        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context, "API.tflite",
                options
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    // Phương thức chính để phân loại một ảnh bitmap
    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier == null) {
            setupClassifier()
        }
            val imageProcessor = ImageProcessor.Builder().build()
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

            // Thiết lập các tùy chọn xử lý ảnh như xoay ảnh dựa trên thông số `rotation`
            val imageProcessingOptions = ImageProcessingOptions.builder()
                .setOrientation(getOrientationFromRotation(rotation))
                .build()

            // Phân loại ảnh đã xử lý và trả về kết quả
            val results = classifier?.classify(tensorImage, imageProcessingOptions)

            return results?.flatMap { classications ->
                classications.categories.map { category ->
                    Classification(
                        name = category.displayName,
                        score = category.score
                    )
                }
            }?.distinctBy { it.name } ?: emptyList()
        }

    // Phương thức để chuyển đổi góc xoay thành hướng phù hợp cho TensorFlow Lite
    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation{
        return when(rotation){
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}

