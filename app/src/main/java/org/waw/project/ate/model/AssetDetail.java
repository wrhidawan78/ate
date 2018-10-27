package org.waw.project.ate.model;

public class AssetDetail {

    private Integer id;
    private String deviceID;
    private String assetTypeName;
    private String assetID;
    private String filePath;


    public AssetDetail(Integer id, String deviceID, String assetTypeName, String assetID, String filePath) {
        this.id = id;
        this.deviceID = deviceID;
        this.assetTypeName = assetTypeName;
        this.assetID = assetID;
        this.filePath = filePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getAssetTypeName() {
        return assetTypeName;
    }

    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
