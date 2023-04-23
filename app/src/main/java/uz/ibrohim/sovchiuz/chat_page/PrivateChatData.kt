package uz.ibrohim.sovchiuz.chat_page

class PrivateChatData {
    var id: String = ""
    var message: String = ""
    var user_id: String = ""
    var time: String = ""
    var type: String = ""
    var image_url: String = ""
    var chat_key : String = ""
    var reading: Boolean = false

    constructor(){}

    constructor(id: String, message: String, user_id: String, time: String, type: String, image_url: String,is_read: Boolean,chat:String){
        this.id = id
        this.message = message
        this.user_id = user_id
        this.time = time
        this.type =  type
        this.image_url =  image_url
        this.reading = is_read
        this.chat_key = chat
    }

}
