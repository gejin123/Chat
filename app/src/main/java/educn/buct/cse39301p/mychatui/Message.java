package educn.buct.cse39301p.mychatui;

/**
 * Created by 84978 on 2017/7/6.
 */

public class Message {
    public String getLeftMsg() {
        return leftMsg;
    }

    public String getRightMsg() {
        return rightMsg;
    }

    private String leftMsg;
    private String rightMsg;

    public Message(String leftMsg, String rightMsg) {
        this.leftMsg = leftMsg;
        this.rightMsg = rightMsg;

    }
}
