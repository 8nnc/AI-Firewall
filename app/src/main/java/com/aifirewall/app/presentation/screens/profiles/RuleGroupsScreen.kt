package com.aifirewall.app.presentation.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.data.local.db.entity.RuleGroupEntity
import com.aifirewall.app.data.repository.RuleGroupRepository
import com.aifirewall.app.presentation.screens.security.RequireAuthentication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RuleGroupsViewModel(private val repository: RuleGroupRepository) : ViewModel() {
    private val _groups = MutableStateFlow<List<RuleGroupEntity>>(emptyList())
    val groups: StateFlow<List<RuleGroupEntity>> = _groups

    init {
        viewModelScope.launch {
            repository.getAllGroupsFlow().collect {
                _groups.value = it
            }
        }
    }

    fun createGroup(name: String, description: String = "") {
        viewModelScope.launch { repository.createGroup(name, description) }
    }

    fun deleteGroup(group: RuleGroupEntity) {
        viewModelScope.launch { repository.deleteGroup(group) }
    }

    class Factory(private val repo: RuleGroupRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = RuleGroupsViewModel(repo) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuleGroupsScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { RuleGroupRepository(context) }
    val viewModel: RuleGroupsViewModel = viewModel(factory = RuleGroupsViewModel.Factory(repo))
    val groups by viewModel.groups.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var actionToAuthenticate by remember { mutableStateOf<(() -> Unit)?>(null) }

    if (actionToAuthenticate != null) {
        RequireAuthentication(
            onAuthenticated = {
                actionToAuthenticate?.invoke()
                actionToAuthenticate = null
            },
            onCancel = { actionToAuthenticate = null }
        ) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rule Groups", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { actionToAuthenticate = { showCreateDialog = true } }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Group")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(groups, key = { it.id }) { group ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(group.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (group.description.isNotBlank()) {
                                Text(group.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        
                        IconButton(onClick = {
                            actionToAuthenticate = { viewModel.deleteGroup(group) }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        var name by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create Group") },
            text = {
                Column {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (name.isNotBlank()) {
                        viewModel.createGroup(name, desc)
                        showCreateDialog = false
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
            }
        )
    }
}
