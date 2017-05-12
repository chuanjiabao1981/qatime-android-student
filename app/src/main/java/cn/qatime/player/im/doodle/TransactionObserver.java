package cn.qatime.player.im.doodle;

import java.util.List;

interface TransactionObserver {
    void onTransaction(String account, List<Transaction> transactions);
}
