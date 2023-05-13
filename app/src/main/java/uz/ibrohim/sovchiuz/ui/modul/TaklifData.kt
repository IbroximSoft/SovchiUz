package uz.ibrohim.sovchiuz.ui.modul

class TaklifData {
    var key: String? = null
    var from_id: String? = null
    var send_id: String? = null
    var last_name: String? = null
    var yosh = 0
    var manzil: String? = null

    constructor() {}

    constructor(
        key: String?,
        from_id: String?,
        send_id: String?,
        last_name: String?,
        yosh: Int,
        manzil: String?
    ) {
        this.key = key
        this.from_id = from_id
        this.send_id = send_id
        this.last_name = last_name
        this.yosh = yosh
        this.manzil = manzil
    }
}