package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.ImageItem;

/**
 * Created by lenovo on 2017/9/8.
 */

public class HomeWorkItemBean implements Serializable{
   public int parent_id;
   public String content;
   public List<ImageItem> imageItems;
   public String videoPath;


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
