package com.example.backend4rate.models.enums;

public enum NotificationType {
    NOTIFICATION_GUEST("notification-guest"),
    NOTIFICATION_MANAGER("notification-manager");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
