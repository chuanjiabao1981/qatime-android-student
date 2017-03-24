package cn.qatime.player.bean;

/**
 * @author Tianhaoranly
 * @date 2016/12/19 11:42
 * @Description:
 */
public class CashAccountBean {

    /**
     * status : 1
     * data : {"id":70,"balance":"4244.86","total_income":"0.0","total_expenditure":"6046.15","has_password":true,"password_set_at":1484725785}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 70
         * balance : 4244.86
         * total_income : 0.0
         * total_expenditure : 6046.15
         * has_password : true
         * password_set_at : 1484725785
         */

        private int id;
        private String balance;
        private String total_income;
        private String total_expenditure;
        private boolean has_password;
        private long password_set_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getTotal_income() {
            return total_income;
        }

        public void setTotal_income(String total_income) {
            this.total_income = total_income;
        }

        public String getTotal_expenditure() {
            return total_expenditure;
        }

        public void setTotal_expenditure(String total_expenditure) {
            this.total_expenditure = total_expenditure;
        }

        public boolean isHas_password() {
            return has_password;
        }

        public void setHas_password(boolean has_password) {
            this.has_password = has_password;
        }

        public long getPassword_set_at() {
            return password_set_at;
        }

        public void setPassword_set_at(long password_set_at) {
            this.password_set_at = password_set_at;
        }
    }
}
