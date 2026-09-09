package com.aifirewall.app.presentation.screens.diagnostics;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a+\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0011\u0010\u0006\u001a\r\u0012\u0004\u0012\u00020\u00010\u0007\u00a2\u0006\u0002\b\bH\u0007\u001a\u0010\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\u0010\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0007\u001a$\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015\u00a8\u0006\u0016"}, d2 = {"Box", "", "modifier", "Landroidx/compose/ui/Modifier;", "contentAlignment", "Landroidx/compose/ui/Alignment;", "content", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "DiagnosticRow", "result", "Lcom/aifirewall/app/presentation/screens/diagnostics/DiagnosticResult;", "DiagnosticsScreen", "navController", "Landroidx/navigation/NavController;", "runDiagnostics", "", "context", "Landroid/content/Context;", "firewallState", "Lcom/aifirewall/app/domain/model/FirewallState;", "(Landroid/content/Context;Lcom/aifirewall/app/domain/model/FirewallState;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class DiagnosticsScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void DiagnosticsScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.Object runDiagnostics(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.aifirewall.app.domain.model.FirewallState firewallState, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.aifirewall.app.presentation.screens.diagnostics.DiagnosticResult>> $completion) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DiagnosticRow(@org.jetbrains.annotations.NotNull()
    com.aifirewall.app.presentation.screens.diagnostics.DiagnosticResult result) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void Box(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Alignment contentAlignment, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
}