package com.BFMe.BFMBuyer.javaBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class UploadingImageBean {
    /**
     * path : http://img01.bfme.com//Storage/Member/temp/adfa6ae9782c9b3e.jpg
     * success : true
     */

    private List<FilesBean> files;

    public List<FilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<FilesBean> files) {
        this.files = files;
    }

    public static class FilesBean {
        private String path;
        private boolean success;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
