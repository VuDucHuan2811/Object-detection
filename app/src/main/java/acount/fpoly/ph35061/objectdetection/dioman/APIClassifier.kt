package acount.fpoly.ph35061.objectdetection.dioman

import android.graphics.Bitmap

interface APIClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}