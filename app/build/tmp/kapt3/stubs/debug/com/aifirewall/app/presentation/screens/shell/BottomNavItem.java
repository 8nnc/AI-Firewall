package com.aifirewall.app.presentation.screens.shell;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u000f\u0010\u0011\u0012\u0013B\u001f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u0082\u0001\u0005\u0014\u0015\u0016\u0017\u0018\u00a8\u0006\u0019"}, d2 = {"Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem;", "", "route", "", "titleResId", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "(Ljava/lang/String;ILandroidx/compose/ui/graphics/vector/ImageVector;)V", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getRoute", "()Ljava/lang/String;", "getTitleResId", "()I", "Activity", "Applications", "Dashboard", "DataUsage", "Settings", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Activity;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Applications;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Dashboard;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$DataUsage;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Settings;", "app_debug"})
public abstract class BottomNavItem {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    private final int titleResId = 0;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    
    private BottomNavItem(java.lang.String route, int titleResId, androidx.compose.ui.graphics.vector.ImageVector icon) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    public final int getTitleResId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Activity;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem;", "()V", "app_debug"})
    public static final class Activity extends com.aifirewall.app.presentation.screens.shell.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.shell.BottomNavItem.Activity INSTANCE = null;
        
        private Activity() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Applications;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem;", "()V", "app_debug"})
    public static final class Applications extends com.aifirewall.app.presentation.screens.shell.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.shell.BottomNavItem.Applications INSTANCE = null;
        
        private Applications() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Dashboard;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem;", "()V", "app_debug"})
    public static final class Dashboard extends com.aifirewall.app.presentation.screens.shell.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.shell.BottomNavItem.Dashboard INSTANCE = null;
        
        private Dashboard() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$DataUsage;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem;", "()V", "app_debug"})
    public static final class DataUsage extends com.aifirewall.app.presentation.screens.shell.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.shell.BottomNavItem.DataUsage INSTANCE = null;
        
        private DataUsage() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem$Settings;", "Lcom/aifirewall/app/presentation/screens/shell/BottomNavItem;", "()V", "app_debug"})
    public static final class Settings extends com.aifirewall.app.presentation.screens.shell.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.aifirewall.app.presentation.screens.shell.BottomNavItem.Settings INSTANCE = null;
        
        private Settings() {
        }
    }
}