package com.github.redreaperlp.json.storage.control;

public enum EPermission {
    PERMISSION_START("pStar"),
    PERMISSION_STOP("pSto"),
    PERMISSION_LOG("pL"),
    PERMISSION_STATUS("pStat"),
    PERMISSION_RESTART("pR"),
    PERMISSION_PANEL("panel");

    private final String key;

    EPermission(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
