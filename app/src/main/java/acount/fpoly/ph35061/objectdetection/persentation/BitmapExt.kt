package acount.fpoly.ph35061.objectdetection.persentation

import android.graphics.Bitmap

fun Bitmap.centerCrop(desiredWidth: Int, desiredHeight: Int):Bitmap{
    // Tính toán toạ độ điểm bắt đầu (xStart, yStart) để cắt từ trung tâm ảnh
    val xStart = (width - desiredWidth) / 2
    val yStart = (height - desiredHeight) / 2

    // Kiểm tra điều kiện xem các tham số truyền vào có hợp lệ hay không
    if (xStart < 0 || yStart < 0 || desiredWidth > width || desiredHeight > height){
        throw IllegalArgumentException("Invalid arguments for center cropping")
    }

    // Tạo một bitmap mới được cắt từ bitmap gốc với kích thước mong muốn
    return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeight)
}