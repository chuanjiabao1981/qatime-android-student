package cn.qatime.player.im.doodle;


import com.orhanobut.logger.Logger;

import java.io.Serializable;

public class Transaction implements Serializable {
    private static final String TAG = Transaction.class.getSimpleName();

    public interface ActionStep {
        byte START = 1;
        byte MOVE = 2;
        byte END = 3;
        byte REVOKE = 4;
        byte SERIAL = 5;
        byte CLEAR = 6;
        byte CLEAR_ACK = 7;
        byte SYNC_REQUEST = 8;
        byte SYNC = 9;
        byte SYNC_PREPARE = 10;
        byte SYNC_PREPARE_ACK = 11;
        byte LASER_PEN = 12;
        byte LASER_PEN_END = 13;
        //        byte Flip = 14;
        byte SwitchTeacher = 16;
        byte SwitchStudent = 17;
    }

    private byte step = ActionStep.START;
    private float x = 0.0f;
    private float y = 0.0f;
    private int rgb;
    private String uid;
    private int end;

    /****自定义字段****/
    private long id;
    private long parentID;
    private String type;
    private String status;

    public long getId() {
        return id;
    }

    public long getParentID() {
        return parentID;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    /****自定义字段****/

    public Transaction() {
    }

    public Transaction(byte step, long id, long parentID, String type, String status) {
        this.step = step;
        this.id = id;
        this.parentID = parentID;
        this.type = type;
        this.status = status;
    }

    public Transaction(byte step, float x, float y, int rgb) {
        this.step = step;
        this.x = x;
        this.y = y;
        this.rgb = rgb;
    }

    public Transaction(byte step, String uid, int end) {
        this.step = step;
        this.uid = uid;
        this.end = end;
    }

//    public Transaction(byte step, String docId, int currentPageNum, int pageCount, int type) {
//        this.step = step;
//        this.docId = docId;
//        this.currentPageNum = currentPageNum;
//        this.pageCount = pageCount;
//        this.type = type;
//    }

    public Transaction(byte step) {
        this.step = step;
    }

    public static String pack(Transaction t) {
        if (t.getStep() == ActionStep.SYNC) {
            return String.format("%d:%s,%d;", t.getStep(), t.getUid(), t.getEnd());
        } else if (t.getStep() == ActionStep.CLEAR || t.getStep() == ActionStep.REVOKE
                || t.getStep() == ActionStep.CLEAR_ACK || t.getStep() == ActionStep.SYNC_REQUEST
                || t.getStep() == ActionStep.SERIAL
                || t.getStep() == ActionStep.SYNC_PREPARE || t.getStep() == ActionStep.SYNC_PREPARE_ACK) {
            return String.format("%d:;", t.getStep());
//        } else if (t.getStep() == ActionStep.Flip) {
//            return String.format("%d:%s,%d,%d,%d;",
//                    t.getStep(), t.getDocId(), t.getCurrentPageNum(), t.getPageCount(), t.getType());
        } else if (t.getStep() == ActionStep.SwitchStudent) {
            return String.format("%d:%d,%d,%s,%s;", t.getStep(), t.getId(), t.getParentID(), t.getType(), t.getStatus());
        }
        return String.format("%d:%f,%f,%d;", t.getStep(), t.getX(), t.getY(), t.getRgb());
    }

    public static String packIndex(int index) {
        return String.format("5:%d,0;", index);
    }

    public static Transaction unpack(String data) {
        int sp1 = data.indexOf(":");
        byte step;
        try {
            if (sp1 <= 0) {
                step = Byte.parseByte(data);
            } else {
                step = Byte.parseByte(data.substring(0, sp1));
            }

            if (step == ActionStep.START || step == ActionStep.MOVE || step == ActionStep.END
                    || step == ActionStep.LASER_PEN) {
                // 画笔 起始点，移动点，结束点
                int sp2 = data.indexOf(",");
                if (sp2 <= 2) {
                    return null;
                }

                float x;
                float y = 0;
                int rgb = 0;

                x = Float.parseFloat(data.substring(sp1 + 1, sp2));

                int sp3 = data.indexOf(",", sp2 + 1);
                if (sp3 > 4) {
                    y = Float.parseFloat(data.substring(sp2 + 1, sp3));
                    rgb = Integer.parseInt(data.substring(sp3 + 1));
                }
                return new Transaction(step, x, y, rgb);
            } else if (step == ActionStep.SYNC) {
                // 同步
                int sp2 = data.indexOf(",");
                if (sp2 <= 2) {
                    return null;
                }

                String uid = data.substring(sp1 + 1, sp2);
                int end = Integer.parseInt(data.substring(sp2 + 1));
                Logger.e(TAG, "Syncing，recive sync data, account:" + uid + ", end:" + end);
                return new Transaction(step, uid, end);
            } else if (step == 5) {
                // 包序号
                String id = data.substring(sp1 + 1);
                Logger.e(TAG, "RECV DATA" + id);
//            } else if (step == ActionStep.Flip) {
//                Logger.e(TAG, "Receive Flip Data");
//                // 翻页
//                int sp2 = data.indexOf(",");
//                String docId = data.substring(sp1 + 1, sp2);
//                int sp3 = data.indexOf(",", sp2 + 1);
//                int currentPageNum = Integer.parseInt(data.substring(sp2 + 1, sp3));
//                int sp4 = data.indexOf(",", sp3 + 1);
//                int countTotal = Integer.parseInt(data.substring(sp3 + 1, sp4));
//                int type = Integer.parseInt(data.substring(sp4 + 1));
//                return new Transaction(step, docId, currentPageNum, countTotal, type);
            } else if (step == ActionStep.SwitchTeacher) {
                int sp2 = data.indexOf(",");
                long id = Long.parseLong(data.substring(sp1 + 1, sp2));
                int sp3 = data.indexOf(",", sp2 + 1);
                long parentID = Long.parseLong(data.substring(sp2 + 1, sp3));
                int sp4 = data.indexOf(",", sp3 + 1);
                String type = data.substring(sp3 + 1, sp4);
                String status = data.substring(sp4 + 1);
                return new Transaction(step, id, parentID, type, status);
            } else {
                Logger.e(TAG, "recieve step:" + step);
                // 其他控制指令
                return new Transaction(step);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void make(byte step, float x, float y, int rgb) {
        this.step = step;
        this.x = x;
        this.y = y;
        this.rgb = rgb;
    }

    private void make(byte step, String uid, int end) {
        this.step = step;
        this.uid = uid;
        this.end = end;
    }

//    /**
//     * 翻页
//     *
//     * @param step           命令
//     * @param docId          文档id
//     * @param currentPageNum 当前页数，1开始计算
//     * @param pageCount      总页数
//     * @param type           状态通知 0：无。1：翻页操作
//     */
//    private void make(byte step, String docId, int currentPageNum, int pageCount, int type) {
//        this.step = step;
//        this.docId = docId;
//        this.currentPageNum = currentPageNum;
//        this.pageCount = pageCount;
//        this.type = type;
//
//    }

    private void make(byte step) {
        this.step = step;
    }

    public Transaction makeStartTransaction(float x, float y, int rgb) {
        make(ActionStep.START, x, y, rgb);
        return this;
    }

    public Transaction makeMoveTransaction(float x, float y, int rgb) {
        make(ActionStep.MOVE, x, y, rgb);
        return this;
    }

    public Transaction makeEndTransaction(float x, float y, int rgb) {
        make(ActionStep.END, x, y, rgb);
        return this;
    }

    public Transaction makeRevokeTransaction() {
        make(ActionStep.REVOKE);
        return this;
    }

    public Transaction makeClearSelfTransaction() {
        make(ActionStep.CLEAR);
        return this;
    }

    public Transaction makeClearAckTransaction() {
        make(ActionStep.CLEAR_ACK);
        return this;
    }

    public Transaction makeSyncRequestTransaction() {
        make(ActionStep.SYNC_REQUEST);
        return this;
    }

    public Transaction makeSyncTransaction(String uid, int end) {
        make(ActionStep.SYNC, uid, end);
        return this;
    }

    public Transaction makeSyncPrepareTransaction() {
        make(ActionStep.SYNC_PREPARE);
        return this;
    }

    public Transaction makeSyncPrepareAckTransaction() {
        make(ActionStep.SYNC_PREPARE_ACK);
        return this;
    }

    public Transaction makeStudentResponseTransaction(long id) {
        make(ActionStep.SwitchStudent, System.currentTimeMillis(), id);
        return this;
    }

    private void make(byte step, long id, long parentId) {
        this.step = step;
        this.id = id;
        this.parentID = parentId;
        this.type = "InteractiveSwitchResponse";
        this.status = "read";
    }

//    public Transaction makeFlipTranscation(String docId, int currentPageNum, int pageCount, int type) {
//        make(ActionStep.Flip, docId, currentPageNum, pageCount, type);
//        return this;
//    }

    public int getStep() {
        return step;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getRgb() {
        return rgb;
    }

    public String getUid() {
        return uid;
    }

    public int getEnd() {
        return end;
    }

//    public String getDocId() {
//        return docId;
//    }
//
//    public void setDocId(String docId) {
//        this.docId = docId;
//    }
//
//    public int getCurrentPageNum() {
//        return currentPageNum;
//    }
//
//    public void setCurrentPageNum(int currentPageNum) {
//        this.currentPageNum = currentPageNum;
//    }
//
//    public int getPageCount() {
//        return pageCount;
//    }
//
//    public void setPageCount(int pageCount) {
//        this.pageCount = pageCount;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }

    public boolean isPaint() {
        return !isRevoke() && !isClearSelf() && !isClearAck()
                && !isSyncRequest() && !isSyncPrepare() && !isSyncPrepareAck()
//                && !isFlip()
                && !isSwitchTeacher() && !isSwitchStudent();
    }

    public boolean isRevoke() {
        return step == ActionStep.REVOKE;
    }

    public boolean isClearSelf() {
        return step == ActionStep.CLEAR;
    }

    public boolean isClearAck() {
        return step == ActionStep.CLEAR_ACK;
    }

    public boolean isSyncRequest() {
        return step == ActionStep.SYNC_REQUEST;
    }

    public boolean isSync() {
        return step == ActionStep.SYNC;
    }

    public boolean isSyncPrepare() {
        return step == ActionStep.SYNC_PREPARE;
    }

    public boolean isSyncPrepareAck() {
        return step == ActionStep.SYNC_PREPARE_ACK;
    }

    public boolean isLaserPen() {
        return step == ActionStep.LASER_PEN;
    }

    public boolean isLaserPenEnd() {
        return step == ActionStep.LASER_PEN_END;
    }

//    public boolean isFlip() {
//        return step == ActionStep.Flip;
//    }

    public boolean isSwitchTeacher() {
        return step == ActionStep.SwitchTeacher;
    }

    public boolean isSwitchStudent() {
        return step == ActionStep.SwitchStudent;
    }

}
