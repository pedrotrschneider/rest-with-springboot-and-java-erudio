package br.com.erudio.data.vo.v1;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponseVO() {
    }

    public UploadFileResponseVO(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UploadFileResponseVO that = (UploadFileResponseVO) o;
        return size == that.size && Objects.equals(fileName, that.fileName) && Objects.equals(fileDownloadUri, that.fileDownloadUri) && Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileDownloadUri, fileType, size);
    }
}
