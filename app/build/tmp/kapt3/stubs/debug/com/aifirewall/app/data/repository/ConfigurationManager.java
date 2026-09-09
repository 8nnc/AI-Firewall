package com.aifirewall.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J$\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0012\u0010\u0013J$\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0016"}, d2 = {"Lcom/aifirewall/app/data/repository/ConfigurationManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "database", "Lcom/aifirewall/app/data/local/db/FirewallDatabase;", "gson", "Lcom/google/gson/Gson;", "profileDao", "Lcom/aifirewall/app/data/local/db/dao/FirewallProfileDao;", "ruleDao", "Lcom/aifirewall/app/data/local/db/dao/AppRuleDao;", "exportConfiguration", "Lkotlin/Result;", "", "uri", "Landroid/net/Uri;", "exportConfiguration-gIAlu-s", "(Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "importConfiguration", "importConfiguration-gIAlu-s", "app_debug"})
public final class ConfigurationManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.local.db.FirewallDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.local.db.dao.FirewallProfileDao profileDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.aifirewall.app.data.local.db.dao.AppRuleDao ruleDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    
    public ConfigurationManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
}