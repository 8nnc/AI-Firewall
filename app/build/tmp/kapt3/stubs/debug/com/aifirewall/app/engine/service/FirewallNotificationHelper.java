package com.aifirewall.app.engine.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\t\u001a\u00020\nH\u0002J\u000e\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\rJ\b\u0010\u000e\u001a\u00020\nH\u0002J\u0016\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/aifirewall/app/engine/service/FirewallNotificationHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "helperScope", "Lkotlinx/coroutines/CoroutineScope;", "notificationManager", "Landroid/app/NotificationManager;", "createChannels", "", "notifyBlockedConnection", "event", "Lcom/aifirewall/app/data/local/db/entity/FirewallEventEntity;", "sendBatchedBlockNotification", "showSecurityEvent", "title", "", "message", "Companion", "app_debug"})
public final class FirewallNotificationHelper {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SECURITY_EVENTS_CHANNEL_ID = "security_events_channel";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BLOCKED_CONNECTIONS_CHANNEL_ID = "blocked_connections_channel";
    private static final int SECURITY_NOTIFICATION_ID = 10102;
    private static final int BLOCK_NOTIFICATION_ID = 10103;
    private static long lastBlockNotificationTime = 0L;
    private static int pendingBlockCount = 0;
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String pendingBlockedPackage = "";
    @org.jetbrains.annotations.NotNull()
    private final android.app.NotificationManager notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope helperScope = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.service.FirewallNotificationHelper.Companion Companion = null;
    
    public FirewallNotificationHelper(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final void createChannels() {
    }
    
    public final void showSecurityEvent(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    /**
     * Throttled block notification to prevent spamming the user.
     */
    public final void notifyBlockedConnection(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallEventEntity event) {
    }
    
    private final void sendBatchedBlockNotification() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/aifirewall/app/engine/service/FirewallNotificationHelper$Companion;", "", "()V", "BLOCKED_CONNECTIONS_CHANNEL_ID", "", "BLOCK_NOTIFICATION_ID", "", "SECURITY_EVENTS_CHANNEL_ID", "SECURITY_NOTIFICATION_ID", "lastBlockNotificationTime", "", "pendingBlockCount", "pendingBlockedPackage", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}