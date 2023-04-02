package uz.ibrohim.sovchiuz.message

data class NotificationModel(
    val to: String,
    val data: NotificationDataModel
)

data class NotificationDataModel(
    val key: String,
    val title: String,
    val body: String,
    val token: String,
    val to_id: String,
    val click_action: String,
    val image: String? // agar rasm yuborilmasa null bo'lishi mumkin
)

