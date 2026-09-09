package com.aifirewall.app.presentation.screens.applications;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0007\u001a\u0018\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0007\u001a4\u0010\t\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u000fH\u0007\u001a@\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u0017H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001a"}, d2 = {"ApplicationDetailsScreen", "", "navController", "Landroidx/navigation/NavController;", "packageName", "", "DetailItem", "label", "value", "PolicyRow", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "policy", "Lcom/aifirewall/app/domain/model/NetworkPolicy;", "onPolicyChange", "Lkotlin/Function1;", "PolicyToggleButton", "text", "isSelected", "", "activeColor", "Landroidx/compose/ui/graphics/Color;", "onClick", "Lkotlin/Function0;", "PolicyToggleButton-42QJj7c", "(Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;ZJLkotlin/jvm/functions/Function0;)V", "app_release"})
public final class ApplicationDetailsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ApplicationDetailsScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    java.lang.String packageName) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PolicyRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.graphics.vector.ImageVector icon, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.NetworkPolicy policy, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.aifirewall.app.domain.model.NetworkPolicy, kotlin.Unit> onPolicyChange) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DetailItem(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
}