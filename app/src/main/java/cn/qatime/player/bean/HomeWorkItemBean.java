package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/9/8.
 */

public class HomeWorkItemBean implements Serializable{
   public int parent_id;
   public String content;
   public List<AttachmentsBean> imageItems;
   public AttachmentsBean audioAttachment;


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      HomeWorkItemBean itemBean = (HomeWorkItemBean) o;

      return parent_id == itemBean.parent_id;
   }

   @Override
   public int hashCode() {
      return parent_id;
   }
}
