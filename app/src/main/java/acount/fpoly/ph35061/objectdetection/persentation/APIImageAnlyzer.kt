package acount.fpoly.ph35061.objectdetection.persentation

import acount.fpoly.ph35061.objectdetection.dioman.APIClassifier
import acount.fpoly.ph35061.objectdetection.dioman.Classification
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class APIImageAnlyzer (
    private val classifier: APIClassifier,// Đối tượng APIClassifier dùng để phân loại hình ảnh
    private val onResults: (List<Classification>) -> Unit// Callback khi có kết quả phân loại
): ImageAnalysis.Analyzer{

    private var frameSkipCounter = 0// Biến đếm để bỏ qua các khung hình không cần phân tích

    override fun analyze(image: ImageProxy) {
        // Mỗi khi đạt đến khung hình thứ 60 thì thực hiện phân tích ảnh (1/60 khung hình)
        if (frameSkipCounter % 60 == 0){
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321, 321)

            // Phân loại hình ảnh với bitmap và độ xoay
            val results = classifier.classify(bitmap, rotationDegrees)
            onResults(results)

        }
        frameSkipCounter++

        image.close()
    }


}