package uz.ibrohim.sovchiuz.message;

public class UserChatData {
    // var uid1: String? = null
    //    var uid2: String? =null
    //    var status: String? = null
    //    var chat: PrivateChatData? = null
    //    var key: String? = null   Hozir 5 minut

    String uid1;
    String uid2;
    String status;
    String key;

    public UserChatData() {
    }

    public UserChatData(String uid1, String uid2, String status, String key) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.status = status;
        this.key = key;
    }

    public String getUid1() {
        return uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }
}
