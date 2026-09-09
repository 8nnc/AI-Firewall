package com.aifirewall.app.engine.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0007J\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0007H\u0082@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/aifirewall/app/engine/service/FirewallEventManager;", "", "()V", "TAG", "", "eventChannel", "Lkotlinx/coroutines/channels/Channel;", "Lcom/aifirewall/app/data/local/db/entity/FirewallEventEntity;", "isInitialized", "", "notificationHelper", "Lcom/aifirewall/app/engine/service/FirewallNotificationHelper;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "dispatchEvent", "", "event", "init", "context", "Landroid/content/Context;", "processEvent", "repository", "Lcom/aifirewall/app/data/repository/EventRepository;", "(Lcom/aifirewall/app/data/repository/EventRepository;Lcom/aifirewall/app/data/local/db/entity/FirewallEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class FirewallEventManager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "FirewallEventManager";
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.channels.Channel<com.aifirewall.app.data.local.db.entity.FirewallEventEntity> eventChannel = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.CoroutineScope scope = null;
    private static boolean isInitialized = false;
    @org.jetbrains.annotations.Nullable()
    private static com.aifirewall.app.engine.service.FirewallNotificationHelper notificationHelper;
    @org.jetbrains.annotations.NotNull()
    public static final com.aifirewall.app.engine.service.FirewallEventManager INSTANCE = null;
    
    private FirewallEventManager() {
        super();
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * Dispatch an event from the fast-path VPN loop to the background processing queue.
     */
    public final void dispatchEvent(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.data.local.db.entity.FirewallEventEntity event) {
    }
    
    private final java.lang.Object processEvent(com.aifirewall.app.data.repository.EventRepository repository, com.aifirewall.app.data.local.db.entity.FirewallEventEntity event, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}