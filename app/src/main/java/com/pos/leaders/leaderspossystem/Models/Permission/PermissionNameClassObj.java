package com.pos.leaders.leaderspossystem.Models.Permission;

/**
 * Created by KARAM on 12/11/2017.
 */

public class PermissionNameClassObj {
    private String PermissionName;
    private Class<?> PermissionClass;

    public PermissionNameClassObj() {
    }

    public PermissionNameClassObj(String permissionName, Class<?> permissionClass) {
        PermissionName = permissionName;
        PermissionClass = permissionClass;
    }

    public String getPerName() {
        return PermissionName;
    }

    public Class<?> getPerClass() {
        return PermissionClass;
    }

    @Override
    public String toString() {
        return getPerName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return PermissionName.equals(o);
    }
}
